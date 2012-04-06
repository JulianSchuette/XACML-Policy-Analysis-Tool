package org.linkality.xacmlanalysr.ui.server;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.linkality.xacmlanalysr.SemanticPolicyAnalyser;
import org.linkality.xacmlanalysr.ui.client.ServerController;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The controller at the server side that handles asynchronous requests coming
 * from the client. Make sure this servlet is made available using the web.xml
 * file.
 * 
 * @author Julian Schuette (julian.schuette@sit.fraunhofer.de)
 * 
 */
public class ControllerServletImpl extends RemoteServiceServlet implements
		ServerController {
	
	public ControllerServletImpl() {
		super();
		try {
			SemanticPolicyAnalyser.createInstance("mypolicy.xacml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String handleRequest(String req) {
		String result = "";

		try {
			if (req == null) {
				throw new IllegalArgumentException("Blank submissions from the client are invalid.");
			}
			SemanticPolicyAnalyser ana = SemanticPolicyAnalyser.getInstance();
			JSONObject json;
			JSONTokener tokenizer = new JSONTokener(req.toString());
			json = new JSONObject(tokenizer);
			if (!json.has("action")) {
				throw new IllegalArgumentException("Blank submissions from the client are invalid.");
			}

			StringBuffer buf = new StringBuffer();

			if (json.get("action").toString().equals("getAsOWL")) {
				//return XACML in OWL format
				buf.append(ana.getAsOWL());
				JSONObject resultObj = new JSONObject();
				System.out.println(buf.toString());
				resultObj.put("result", buf.toString());
				result = resultObj.toString();
			} else if ((json.get("action").toString().equals("getExplanation"))) {
				//Find possible reasons for a given result
				String[] target = json.get("target").toString().split(",");
				String subject = SemanticPolicyAnalyser.encodePrologLiteral(target[0]); 
				String environment = SemanticPolicyAnalyser.encodePrologLiteral(target[1]);
				String action = SemanticPolicyAnalyser.encodePrologLiteral(target[2]);
				String resource = SemanticPolicyAnalyser.encodePrologLiteral(target[3]);
				String effect = "'hydra#"+json.get("effect").toString()+"'";
				String[] explanations = ana.getExplanations(subject, action, resource, environment, effect);
				
				JSONArray arr = new JSONArray(explanations);

				JSONObject res = new JSONObject();
				res.put("result", arr);
				result = res.toString();
			} else if ((json.get("action").toString().equals("getRequiredAttributes"))) {
				String[] requiredAttributes = ana.getRequiredAttributes("", "", "", "", "");
				JSONArray arr = new JSONArray(requiredAttributes);
				
				JSONObject res = new JSONObject();
				res.put("result", arr);
				result = res.toString();
			} else if ((json.get("action").toString().equals("getPolicyTargets"))) {
				//get all targets of the policy
				Vector<String> targets = ana.getPolicyTargets();
				JSONArray array = new JSONArray();
				for (int i=0;i<targets.size();i++) {
					String target = targets.get(i);
					array.put(target);
				}
				result = array.toString();
			} else if (json.get("action").toString().equals("getAsProlog")) {
				//return XACML policy in Prolog format
				String prolog = ana.getAsProlog();
				JSONObject resultObj = new JSONObject();
				resultObj.put("result", prolog);
				result = resultObj.toString();
			} else if (json.get("action").toString().equals("getInfo")) {
				//return some information about the policy
				Properties info = ana.getInfo();
				Enumeration keys = info.keys();
				JSONArray array = new JSONArray();
				while (keys.hasMoreElements()) {
					String key = (String) keys.nextElement();
					array.put(info.get(key));
				}
				result = array.toString();
				System.out.println(result);
			} else if (json.get("action").toString().equals("checkConsistency")) {
				//use Jena to check the consistency of the policy
				String consistency = ana.checkConsistency();
				JSONObject res = new JSONObject();
				res.put("result", consistency);
				result = res.toString();
				System.out.println(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Returning something");
		return result.toString();
	}
}