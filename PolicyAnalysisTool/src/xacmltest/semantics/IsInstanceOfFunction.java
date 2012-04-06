package xacmltest.semantics;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.sun.xacml.EvaluationCtx;
import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.attr.BooleanAttribute;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.cond.EvaluationResult;
import com.sun.xacml.cond.FunctionBase;

/**
 * A class that implements an "is-instance-of" function. It takes two String
 * operands and returns a <code>BooleanAttribute</code> indicating whether the
 * first argument can be inferred from the second argument.
 * 
 * @author Julian Schuette
 */
public class IsInstanceOfFunction extends FunctionBase {

	// Directory where the ontology files are
	private static final String DATA_DIR = "ontologies";

	// File name of ontology to be used
	public static final String DEVICE_ONTOLOGY = "securityontologytest.owl";

	// Full URL of the rdf:type relation
	public static final String rdfType = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";

	/**
	 * Standard identifier for the string-equal function.
	 */
	public static final String NAME_STRING_EQUAL = FUNCTION_NS
			+ "is-instance-of";

	// private mapping of standard functions to their argument types
	private static HashMap typeMap;
	
	private static Model m;

	/**
	 * Static initializer sets up a map of standard function names to their
	 * associated datatypes
	 */
	static {
		typeMap = new HashMap();

		typeMap.put(NAME_STRING_EQUAL, StringAttribute.identifier);
		
		// Get the raw model
		m = getModel();
	}

	/**
	 * Creates a new <code>IsInstanceOfFunction</code> object that supports
	 * one of the standard type-equal functions. If you need to create an
	 * instance for a custom type, use the <code>getEqualInstance</code>
	 * method or the alternate constructor.
	 * 
	 * @param functionName
	 *            the standard XACML name of the function to be handled by this
	 *            object, including the full namespace
	 * 
	 * @throws IllegalArgumentException
	 *             if the function isn't standard
	 */
	public IsInstanceOfFunction(String functionName) {
		this(functionName, getArgumentType(functionName));
	}

	/**
	 * Creates a new <code>IsInstanceOfFunction</code> object.
	 * 
	 * @param functionName
	 *            the standard XACML name of the function to be handled by this
	 *            object, including the full namespace
	 * @param argumentType
	 *            the standard XACML name for the type of the arguments,
	 *            inlcuding the full namespace
	 */
	public IsInstanceOfFunction(String functionName, String argumentType) {
		super(functionName, 0, argumentType, false, 2,
				BooleanAttribute.identifier, false);
	}

	/**
	 * Private helper that returns the type used for the given standard
	 * type-equal function.
	 */
	private static String getArgumentType(String functionName) {
		String datatype = (String) (typeMap.get(functionName));

		if (datatype == null)
			throw new IllegalArgumentException("not a standard function: "
					+ functionName);

		return datatype;
	}

	/**
	 * Returns a <code>Set</code> containing all the function identifiers
	 * supported by this class.
	 * 
	 * @return a <code>Set</code> of <code>String</code>s
	 */
	public static Set getSupportedIdentifiers() {
		return Collections.unmodifiableSet(typeMap.keySet());
	}

	/**
	 * Evaluate the function, using specified parameters.
	 * 
	 * @param inputs
	 *            a <code>List</code> of <code>Evaluatable</code> objects
	 *            representing the arguments passed to the function
	 * @param context
	 *            an <code>EvaluationCtx</code> so that the
	 *            <code>Evaluatable</code> objects can be evaluated
	 * @return an <code>EvaluationResult</code> representing the function's
	 *         result
	 */
	public EvaluationResult evaluate(List inputs, EvaluationCtx context) {

		// Evaluate the arguments
		AttributeValue[] argValues = new AttributeValue[inputs.size()];
		EvaluationResult result = evalArgs(inputs, context, argValues);

		// If no error: result==null
		if (result != null)
			return result;

		return EvaluationResult.getInstance(isOfType(argValues[1].encode(),
				argValues[0].encode()));
	}

	/**
	 * Returns true if <code>instance</code> can be inferred from
	 * <code>type</code> using the ontology.
	 * 
	 * @param instance
	 * @param type
	 * @return
	 */
	private boolean isOfType(String instance, String type) {
		System.out.println("Checking " + instance + " against " + type);
		try {
			// Start inference (add implicit knowledge)
			Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
		    InfModel inf = ModelFactory.createInfModel(reasoner, m);

		    //This is a convenient method for the RDFS reasoner
		    //InfModel inf = ModelFactory.createRDFSModel(m);

			// Define the query: (?a rdf:type ?b)
			String queryString = "select ?device\n" + "WHERE \n" + "{ \n"
					+ "?device <" + rdfType
					+ "> <http://www.owl-ontologies.com/unnamed.owl#" + type
					+ ">.\n" + " \n} \n";

			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.create(query, inf);

			// Execute SPARQL query
			ResultSet results = qexec.execSelect();

			// Step through solution, check for instance
			boolean itemFound = false;
			while (results.hasNext()) {
				QuerySolution solution = results.nextSolution();

				// This toString().Contains() solution is quick and dirty,
				// should be improved.
				if (solution.toString().contains(instance)) {
					itemFound = true;
					System.out.println("Solution: " + solution.toString());
				}
			}
			qexec.close();
//			m.close();
			return itemFound;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Returns the model provided by the ontology file.
	 * 
	 * @return
	 */
	protected static Model getModel() {
		ModelMaker maker = ModelFactory.createFileModelMaker(DATA_DIR);

		// get the existing model with that name if we can
		Model m = maker.getModel(DEVICE_ONTOLOGY);

		// if we didn't find it, create a new one
		if (m == null) {
			m = maker.createModel(DEVICE_ONTOLOGY);
			m.withDefaultMappings(PrefixMapping.Standard);
		}

		return m;
	}

}
