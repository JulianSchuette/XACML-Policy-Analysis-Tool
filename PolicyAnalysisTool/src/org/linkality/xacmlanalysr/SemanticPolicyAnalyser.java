package org.linkality.xacmlanalysr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.mindswap.pellet.owlapi.Reasoner;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.io.StringInputSource;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import xacmltest.semantics.IsInstanceOfFunction;

import com.clarkparsia.explanation.PelletExplanation;
import com.clarkparsia.explanation.io.manchester.ManchesterSyntaxExplanationRenderer;
import com.clarkparsia.pellet.sparqldl.jena.SparqlDLExecutionFactory;
import com.declarativa.interprolog.SWISubprocessEngine;
import com.declarativa.interprolog.TermModel;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.sun.xacml.Obligation;
import com.sun.xacml.Policy;
import com.sun.xacml.PolicySet;
import com.sun.xacml.cond.FunctionFactory;
import com.sun.xacml.cond.FunctionFactoryProxy;
import com.sun.xacml.cond.StandardFunctionFactory;
import com.sun.xacml.finder.AttributeFinder;
import com.sun.xacml.finder.AttributeFinderModule;
import com.sun.xacml.finder.PolicyFinder;
import com.sun.xacml.finder.impl.CurrentEnvModule;
import com.sun.xacml.finder.impl.SelectorModule;
import com.sun.xacml.support.finder.URLPolicyFinderModule;

/**
 * A very basic implementation of a XACML Policy Decision Point (PDP).
 * <p>
 * This PDP can answer policy requests in XACML 2.0 format, based on policies
 * which have been added before. Additionally, this PDP supports a non-standard
 * function <i>is-instance-of</i> which is checked against an ontology by using
 * the Jena reasoner.
 * 
 * @author Julian Schuette
 * 
 */
public class SemanticPolicyAnalyser {
	private final String NS = "http://hydra.eu.com/sit/xacml#";

	// The original XACML file
	private String xacmlFileName;

	// The policy, parsed as an XACML string
	private String xacmlPolicy;

	// The policy, converted to OWL
	private String owlPolicy = null;

	// The policy, converted to Prolog
	private String prologPolicy = null;

	// The policy as an object
	private PolicySet policySet = null;
	private Policy policy = null;
	private SWISubprocessEngine engine = null;

	private URI id;

	private URI combiningAlgo;

	private String description;

	private int xacmlVersion;
	private static SemanticPolicyAnalyser myInstance;

	public static SemanticPolicyAnalyser createInstance(String fileName)
			throws Exception {
		myInstance = new SemanticPolicyAnalyser();
		myInstance.init(fileName);
		return myInstance;
	}

	public static SemanticPolicyAnalyser getInstance() throws Exception {
		if (myInstance == null) {
			throw new Exception("Must call createInstance() first.");
		}
		return myInstance;
	}

	private void init(String fileName) throws Exception {
		this.xacmlFileName = fileName;
		// Configure PDP to use these policy files:
		this.xacmlPolicy = Utils.readFileAsString(new File(fileName));

		CurrentEnvModule envAttributeModule = new CurrentEnvModule();
		SelectorModule selectorAttributeModule = new SelectorModule();

		// Setup the AttributeFinder just like we setup the PolicyFinder.
		AttributeFinder attributeFinder = new AttributeFinder();
		AttributeFinderModule sampleAttributeFinder = new SampleAttrFinderModule();
		List attributeModules = new ArrayList();
		attributeModules.add(envAttributeModule);
		attributeModules.add(sampleAttributeFinder);
		attributeModules.add(selectorAttributeModule);
		attributeFinder.setModules(attributeModules);

		// Load the customized is-instance-of function.
		FunctionFactoryProxy proxy = StandardFunctionFactory.getNewFactoryProxy();
		FunctionFactory factory = proxy.getTargetFactory();
		factory.addFunction(new IsInstanceOfFunction("urn:oasis:names:tc:xacml:1.0:function:is-instance-of"));
		FunctionFactory.setDefaultFactory(proxy);

		// Setup the PolicyFinder
		URLPolicyFinderModule urlFinder = new URLPolicyFinderModule();
		PolicyFinder policyFinder = new PolicyFinder();
		Set<URLPolicyFinderModule> policyModules = new HashSet<URLPolicyFinderModule>();
		policyModules.add(urlFinder);
		policyFinder.setModules(policyModules);

		// Load policy to OWL and Prolog
		initOWL(new ByteArrayInputStream(xacmlPolicy.getBytes()), policyFinder);
		initProlog();
	}

	private void initProlog() throws FileNotFoundException, IOException {
		// Initialise the Prolog engine
		Properties props = new Properties();
		props.load(new FileInputStream(new File("policy_analyser.properties")));
		System.setProperty("JAVA_BIN", props.getProperty("JAVA_BIN"));
		System.setProperty("SWI_BIN_DIRECTORY", props.getProperty("SWI_BIN_DIRECTORY"));
		engine = new SWISubprocessEngine();
		engine.consultAbsolute(new File("prolog/policy_analysis.pl"));

		// Load the converted policy and convert
		new File("xacml.pl").delete();
		System.out.println();
		SWISubprocessEngine engine = new SWISubprocessEngine();
		boolean success = engine.consultAbsolute(new File("prolog/policy_analysis.pl"));
		success = engine.deterministicGoal("go_thea('xacml_gen.owl','xacml.pl', dlp)");
		System.out.println(success);
		System.out.println("Converted to Prolog!");

		// Apply post-processor
		PrologPostProcessor post = PrologPostProcessor.getInstance();
		post.execute(new File("xacml.pl"));
		success = engine.consultAbsolute(new File("xacml.pl"));
		System.out.println("Loaded Prolog");
		this.prologPolicy = Utils.readFileAsString(new File("xacml.pl"));
	}

	/**
	 * Explains how a policy effect can happen.
	 * <p>
	 * This method returns a list of Strings representing a Prolog list of
	 * <code>effect</code>, <code>resource</code>, <code>action</code>,
	 * <code>subject</code>, <code>environment</code>.
	 * 
	 * @param subject
	 * @param action
	 * @param resource
	 * @param environment
	 * @param effect
	 * @return
	 */
	public String[] getExplanations(String subject, String action,
			String resource, String environment, String effect) {
		System.out.println("Getting explanations using Prolog");
		String query = "getExplanation(" + effect + "," + resource + ", "
				+ action + ", " + subject + ", " + environment + ", Result)";
		System.out.println(query);
		// TODO Warum muss hier nochmal consult aufgerufen werden? Sollte schon
		// in initProlog() vorhanden sein?
		engine.consultAbsolute(new File("xacml.pl"));

		Object[] prologResults = engine.deterministicGoal(query, null);
		System.out.println("done");
		System.out.println(prologResults.length);
		// The result will look like this:
		// getExplanation(hydra#deny,main-door,open,Var2,Var3,[[hydra#TimeRangePolicy,hydra#DenyAllOthers,hydra#deny,main-door,open,hydra#ANY,hydra#ANY],[hydra#TimeRangePolicy,hydra#NoHumans,hydra#deny,main-door,open,hydra#ANY,hydra#ANY]])
		// Get the result array
		String resultString = ((TermModel) prologResults[0]).toString();
		resultString = resultString.substring(resultString.indexOf('[') + 2, resultString.lastIndexOf(']') - 1);
		System.out.println(resultString);
		String[] result = resultString.split("],\\[");
		return result;
	}

	
	/**
	 * Returns all attributes which <i>might</i> be required to evaluate a request.
	 * <p>
	 * @param subject
	 * @param action
	 * @param resource
	 * @param environment
	 * @param effect
	 * @return
	 */
	public String[] getRequiredAttributes(String subject, String action,
			String resource, String environment, String effect) {
		//FIXME Add variables for target, include in query.
		String[] result = null;
		// First create a Jena ontology model backed by the Pellet reasoner
		// (note, the Pellet reasoner is required)
		OntModel m = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC );

		// Then read the data from the file into the ontology model
		m.read( new StringReader(this.owlPolicy),"hydra" );
		System.out.println("Size " + m.size());
		Map prefixes = m.getNsPrefixMap();
		Set prefixKeys = prefixes.keySet();
		Iterator it = prefixKeys.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			System.out.print(key + "  ");
			System.out.println(prefixes.get(key));
		}
		try {
			Query q = QueryFactory.create( "PREFIX hydra:   <http://www.hydramiddleware.eu/xacml#> \r\n" +
					"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \r\n" +
					"SELECT ?conditionElement ?value WHERE {" +
					"?conditionElement rdf:type hydra:Attribute." +
					"?conditionElement hydra:hasAttributeId ?value." +
					"}" );

			// Create a SPARQL-DL query execution for the given query and
			// ontology model
			QueryExecution qe = SparqlDLExecutionFactory.create( q, m );
	
			// We want to execute a SELECT query, do it, and return the result set
			ResultSet rs = qe.execSelect();
//			ResultSetFormatter.out(rs);
//			List resultList = ResultSetFormatter.toList(rs);
			Vector<String> results = new Vector<String>();
//			Iterator ite = resultList.iterator();
			while (rs.hasNext()) {
//				System.out.println("y: "+ite.next());
				QuerySolution solution = rs.nextSolution();
				String attributeResult = solution.get("value").toString();
				attributeResult = attributeResult.substring(0,attributeResult.indexOf('^'));
				results.add(attributeResult);
				System.out.println("x: " + attributeResult);
			}
			result = new String[results.size()];
			for (int i=0;i<results.size();i++) {
				result[i] = results.get(i);
			}
			// Print the query for better understanding
			System.out.println(q.toString());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.out.println("Returning " + result.length);
		return result;
	}

	/**
	 * Returns the policy in OWL format.
	 * 
	 * @return the OWL representation as a String.
	 */
	public String getAsOWL() {
		return this.owlPolicy;
	}

	/**
	 * Returns the policy in Prolog format.
	 * 
	 * @return the Prolog representation as a String.
	 */
	public String getAsProlog() {
		return this.prologPolicy;
	}

	/**
	 * Checks the consistency of the policy. This method confirms that the
	 * policy, converted to OWL, corresponds to the pre-defined T-box.
	 * 
	 * @return true if consistency check passed, false otherwise.
	 */
	public String checkConsistency() throws OWLOntologyCreationException,
			OWLException, IOException {
		PelletExplanation.setup();

		// The renderer is used to pretty print explanation
		ManchesterSyntaxExplanationRenderer renderer = new ManchesterSyntaxExplanationRenderer();
		// The writer used for the explanation rendered
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintWriter out = new PrintWriter(bos);
		renderer.startRendering(out);

		// Create an OWLAPI manager that allows to load an ontology file and
		// create OWLEntities
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntology(new StringInputSource(this.owlPolicy));
		OWLDataFactory factory = manager.getOWLDataFactory();

		// Create the reasoner and load the ontology
		Reasoner reasoner = new Reasoner(manager);
		reasoner.loadOntology(ontology);

		// Create an explanation generator
		PelletExplanation expGen = new PelletExplanation(reasoner);

		// Explain unsatisfiable concepts
		Set<Set<OWLAxiom>> exp = expGen.getInconsistencyExplanations();
		OWLIndividual deny = factory.getOWLIndividual(URI.create(NS + "deny"));
		// Explain why mad cow is an unsatisfiable concept
		System.out.println("Xaim:" + exp.toString());
		System.out.println("Running");
		System.out.println("Test: " + new String(bos.toByteArray()));

		String allExplanationsString = "";
		Iterator<Set<OWLAxiom>> it = exp.iterator();
		while (it.hasNext()) {
			Set<OWLAxiom> explanation = it.next();
			Iterator<OWLAxiom> et = explanation.iterator();
			String explanationString = "";
			while (et.hasNext()) {
				OWLAxiom axiom = et.next();
				explanationString = explanationString + ", " + axiom.toString();
			}
			allExplanationsString += "\n" + explanationString;
		}
		String result = "";
		result = allExplanationsString;
		System.out.println("consistency check counter-proof: " + result);
		renderer.endRendering();
		// return (exp.isEmpty());

		if (exp.isEmpty()) {
			result = "true";
		} else {
			renderer.render(exp);
			result = new String(bos.toByteArray());
		}
		renderer.endRendering();
		return result;
	}


	/**
	 * Main method for testing purposes.
	 * 
	 * @param args
	 *            No arguments required.
	 */
	public static void main(String[] args) {
		try {

			System.out.println("Creating analyser");
			SemanticPolicyAnalyser analyser = SemanticPolicyAnalyser.createInstance("policies/policy.xml");
			System.out.println("Checking for consistency");
			analyser.checkConsistency();
			System.out.println(analyser.getAsOWL());
			System.out.println("Testing getTargets:");
			Vector<String> targets = analyser.getPolicyTargets();
			for (int i = 0; i < targets.size(); i++)
				System.out.println(targets.get(i));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String encodePrologLiteral(String literal) {
		if (!literal.equals("_")
				&& literal.substring(0, 1).toLowerCase().equals(literal.substring(0, 1))) {
			literal = "'" + literal + "'";
		}
		return literal;
	}

	/**
	 * Returns all targets of the policy.
	 * 
	 * @return
	 */
	public Vector<String> getPolicyTargets() {
		if (policy == null) {
			return null;
		}
		//TODO code works but is cumbersome. Revise.
		Vector<String> targets = new Vector<String>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setNamespaceAware(true);
			factory.setValidating(false);

			// create a builder based on the factory & try to load the policy
			DocumentBuilder db = factory.newDocumentBuilder();
			String action = "";
			String resource = "";
			String subject = "";
			String environment = "";

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			policy.getTarget().getActionsSection().encode(bos);
			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
			Document doc = db.parse(bis);
			NodeList attributeValues = doc.getElementsByTagName("AttributeValue");
			for (int i = 0; i < attributeValues.getLength(); i++) {
				Node nod = attributeValues.item(i);
				if (nod != null)
					action = nod.getTextContent();
				else
					action = "_";

				bos = new ByteArrayOutputStream();
				policy.getTarget().getResourcesSection().encode(bos);
				bis = new ByteArrayInputStream(bos.toByteArray());
				doc = db.parse(bis);
				nod = doc.getElementsByTagName("AttributeValue").item(0);
				if (nod != null)
					resource = nod.getTextContent();
				else
					resource = "_";

				bos = new ByteArrayOutputStream();
				policy.getTarget().getSubjectsSection().encode(bos);
				bis = new ByteArrayInputStream(bos.toByteArray());
				doc = db.parse(bis);
				nod = doc.getElementsByTagName("AttributeValue").item(0);
				if (nod != null)
					subject = nod.getTextContent();
				else
					subject = "_";

				bos = new ByteArrayOutputStream();
				policy.getTarget().getEnvironmentsSection().encode(bos);
				bis = new ByteArrayInputStream(bos.toByteArray());
				doc = db.parse(bis);
				nod = doc.getElementsByTagName("AttributeValue").item(0);
				if (nod != null)
					environment = nod.getTextContent();
				else
					environment = "_";

				targets.add(environment + "," + subject + "," + action + ","
						+ resource);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return targets;
	}

	private void initOWL(InputStream is, PolicyFinder finder) {
		// Shortcut: return immediately if already available
		if (this.owlPolicy != null) {
			return;
		}

		try {
			// create the factory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setNamespaceAware(true);
			factory.setValidating(false);

			// create a builder based on the factory & try to load the policy
			DocumentBuilder db = factory.newDocumentBuilder();
			Document doc = db.parse(is);

			// handle the policy, if it's a known type
			Element root = doc.getDocumentElement();
			String name = root.getLocalName();

			Policy pol = null;
			if (name.equals("Policy")) {
				policy = (Policy) Policy.getInstance(root);
				pol = policy;
			} else if (name.equals("PolicySet")) {
				policySet = PolicySet.getInstance(root, finder);
			}

			id = policy.getId();
			combiningAlgo = policy.getCombiningAlg().getIdentifier();
			xacmlVersion = policy.getMetaData().getXACMLVersion();
			description = policy.getDescription();

			// TODO handle policy recursively and add policy set
			Document mainOWLDoc = db.newDocument();
			// Element policyElementOWL = mainOWLDoc.createElement("Policy");
			Element policyElementOWL = policy.toOWLNode(mainOWLDoc);
			policyElementOWL.setAttribute("rdf:ID", pol.getId().toASCIIString());
			// Element ruleCombiningAlgo =
			// mainOWLDoc.createElement("hasRuleCombiningAlgorithm");
			// ruleCombiningAlgo.setAttribute("rdf:resource",
			// pol.getCombiningAlg().getIdentifier().toString());
			// policyElementOWL.appendChild(ruleCombiningAlgo);

			Iterator<Obligation> it = pol.getObligations().iterator();
			while (it.hasNext()) {
				Element obligation = mainOWLDoc.createElement("hasObligation");
				obligation.setAttribute("rdf:resource", it.next().toString());
				policyElementOWL.appendChild(obligation);
			}

			mainOWLDoc.appendChild(policyElementOWL);

			String templ = Utils.readFileAsString(new File("XACMLOWL.template"));
			String owl = templ.replace("[XACML]", toString(mainOWLDoc).replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
			FileWriter fw = new FileWriter("xacml_gen.owl");
			fw.write(owl);
			fw.close();
			this.owlPolicy = owl;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Convert XML DOM document to a string.
	 * 
	 * @param document
	 *            XML DOM document
	 * @return XML string
	 * @throws TransformerException
	 */
	public static String toString(Document document)
			throws TransformerException {
		StringWriter stringWriter = new StringWriter();
		StreamResult streamResult = new StreamResult(stringWriter);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.transform(new DOMSource(document.getDocumentElement()), streamResult);
		return stringWriter.toString();
	}

	public Properties getInfo() {
		Properties props = new Properties();
		props.put("id", this.id);
		props.put("combiningAlgo", this.combiningAlgo);
		props.put("filename", this.xacmlFileName);
		props.put("version", this.xacmlVersion);
		return props;
	}

}
