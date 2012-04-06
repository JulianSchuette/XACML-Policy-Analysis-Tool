package org.linkality.xacmlanalysr;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.sun.xacml.EvaluationCtx;
import com.sun.xacml.attr.AttributeDesignator;
import com.sun.xacml.attr.BagAttribute;
import com.sun.xacml.attr.RFC822NameAttribute;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.cond.EvaluationResult;
import com.sun.xacml.ctx.Status;
import com.sun.xacml.finder.AttributeFinderModule;

/**
 * This is an example <code>AttributeFinderModule</code> that looks up
 * attributes based on the context. This kind of functionality is useful if you
 * want to include some information in the Request and then use that information
 * as needed, during policy evaluation, to retrieve other attribute values. In
 * this case, the user's identity is included in the original Request, but the
 * policy needs to know about the user's group memberships. This module looks up
 * the groups based on the user's identity.
 * <p>
 * Because group membership is system-specific the actual retrieval of these
 * values is left out of this module. If you supply this code, however, you can
 * then retrive the values by saying
 * 
 * <pre>
 *   &lt;SubjectAttributeDesignator AttributeId=&quot;subject-groups&quot;
 *       DataType=&quot;http://www.w3.org/2001/XMLSchema#string&quot;/&gt;
 * </pre>
 * 
 * in your policies.
 * 
 * @since 1.1
 * @author seth proctor
 */
public class SampleAttrFinderModule extends AttributeFinderModule {

	// the one and only attribute identifier that this module supports
	private static final String SUPPORTED_ATTRIBUTE_ID = "group";

	// the identifier and type of the user
	private static final String USER_ID = "urn:oasis:names:tc:xacml:1.0:subject:subject-id";
	private static final String USER_ID_TYPE = RFC822NameAttribute.identifier;

	// URI versions of the subject data
	private URI userId;
	private URI userIdType;

	/**
	 * Default constructor.
	 */
	public SampleAttrFinderModule() throws URISyntaxException {
		// setup the subject identifier information
		userId = new URI(USER_ID);
		userIdType = new URI(USER_ID_TYPE);

		// this code doesn't do it, but this would be a good place to setup a
		// cache if you don't want to fetch the group information each time
	}

	/**
	 * Sepcifies whether or not this module supports AttributeDesignator
	 * queries. Since that's what this code does, this method always returns
	 * true;
	 * 
	 * @return true
	 */
	public boolean isDesignatorSupported() {
		return true;
	}

	/**
	 * Specifies the types of designators this code supports. In this case, the
	 * module supports only subject attributes.
	 * 
	 * @return a <code>Set</code> containing the supported types
	 */
	public Set getSupportedDesignatorTypes() {
		Set types = new HashSet();

		types.add(new Integer(AttributeDesignator.SUBJECT_TARGET));

		return types;
	}

	/**
	 * Specifies the identifiers that this code supports. This module has been
	 * written to support exactly one attribute, but in general you could write
	 * a module that supports any number of attributes.
	 * 
	 * @return a <code>Set</code> specifying the supported attributes ids
	 */
	public Set getSupportedIds() {
		Set ids = new HashSet();

		try {
			ids.add(new URI(SUPPORTED_ATTRIBUTE_ID));
		} catch (URISyntaxException se) {
			// this won't actually happen in this case
			return null;
		}

		return ids;
	}

	/**
	 * This is called when the PDP is trying to find a value that wasn't
	 * included in a Request. The value that the PDP is looking for may or may
	 * not be supported by this module, so you first have to check that you can
	 * handle this request.
	 */
	public EvaluationResult findAttribute(URI attributeType, URI attributeId,
			URI issuer, URI subjectCategory, EvaluationCtx context,
			int designatorType) {
		System.out.println("AttributeType: " + attributeType.toString());
		System.out.println("AttributeID: " + attributeId.toString());

		// check that this is a Subject attribute
		if (designatorType != AttributeDesignator.SUBJECT_TARGET)
			return new EvaluationResult(BagAttribute.createEmptyBag(attributeType));

		// check that this is the type and identifier that this module is
		// setup to handle
		if ((!attributeType.toString().equals(StringAttribute.identifier))
				|| (!attributeId.toString().equals(SUPPORTED_ATTRIBUTE_ID))) {
			return new EvaluationResult(BagAttribute.createEmptyBag(attributeType));
		} 
		

		// if we got here then we're looking for the one attribute that this
		// module knows how to handle, so get the user's identifier...note
		// that we don't consider the issuer here, since it pertains to the
		// issuer of the group values (ie, the values that this module is
		// supposed to return)
		EvaluationResult result = context.getSubjectAttribute(userIdType, userId, subjectCategory);

		// make sure there wasn't an error getting the identifier
		if (result.indeterminate())
			return result;

		// make sure we found exactly one value for the user's identifier
		BagAttribute bag = (BagAttribute) (result.getAttributeValue());

		if (bag.size() != 1) {
			ArrayList code = new ArrayList();
			code.add(Status.STATUS_MISSING_ATTRIBUTE);
			Status status = new Status(code, attributeId.toString());

			return new EvaluationResult(status);
		}

		// get the identifier out of the bag and get the group memberships
		RFC822NameAttribute user = (RFC822NameAttribute) (bag.iterator().next());

//		return retrieveValue(attributeType, attributeId.toString());
		return getGroups(user);
	}

	/**
	 * This is the app-specific part that you need to fill in to make this
	 * module work correctly. This method should use the user's identity to
	 * lookup the groups that the user is in. The groups will probably be
	 * returned either as multiple items in a bag, or as a single String listing
	 * all the groups (depending on what your system needs).
	 */
	private EvaluationResult getGroups(RFC822NameAttribute user) {
		// do the group lookup...note that regardless of the form of the
		// groups, you must return an EvaluationResult that contains a
		// Bag, since that's the form that must be returned from the
		// findAttribute method above
		BagAttribute groups = null;

		// if there is an error at any point in this routine, then return
		// an EvaluationResult with status to explain the error

		// finally, return the group information
		return new EvaluationResult(groups);
	}
	

	private EvaluationResult retrieveValue(URI attributeType, String attributeID) {		
		//TODO Hier die Attribute sammeln, deren Werte nicht gefunden werden konnten
		System.out.println("Need the attribute " + attributeID);
		
		
		List collection = new LinkedList();
		collection.add(new StringAttribute("Blablabla"));
		BagAttribute groups = new BagAttribute(attributeType, collection);
		
		// if there is an error at any point in this routine, then return
		// an EvaluationResult with status to explain the error

		// finally, return the group information
		return new EvaluationResult(groups);
	}	

}
