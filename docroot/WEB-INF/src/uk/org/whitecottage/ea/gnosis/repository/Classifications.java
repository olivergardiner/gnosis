package uk.org.whitecottage.ea.gnosis.repository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.exist.xmldb.EXistResource;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import uk.org.whitecottage.ea.gnosis.jaxb.classification.Classification;
import uk.org.whitecottage.ea.gnosis.jaxb.classification.Term;
import uk.org.whitecottage.ea.gnosis.json.JSONArray;
import uk.org.whitecottage.ea.gnosis.json.JSONMap;
import uk.org.whitecottage.ea.gnosis.json.JSONObject;
import uk.org.whitecottage.ea.gnosis.json.JSONString;
import uk.org.whitecottage.ea.xmldb.XmldbProcessor;

public class Classifications extends XmldbProcessor  {

	protected Unmarshaller classificationUnmarshaller = null;
	protected Marshaller classificationMarshaller = null;

	public Classifications(String URI, String repositoryRoot, String context) {
		super(URI, repositoryRoot);

		try {
		    JAXBContext classificationContext = JAXBContext.newInstance("uk.org.whitecottage.ea.gnosis.jaxb.classification");
		    classificationUnmarshaller = createUnmarshaller(classificationContext, context + "/WEB-INF/xsd/classification.xsd");
		    classificationMarshaller = classificationContext.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public String getClassificationJSON(String classificationName) {
		String result = null;
		
		if (classificationName == null) {
			return "";
		}

		Collection repository = null;
		XMLResource classificationResource = null;
		try {
			repository = getCollection("classifications");
			classificationResource = getResource(repository, classificationName + ".xml");
			Classification classification = (Classification) classificationUnmarshaller.unmarshal(classificationResource.getContentAsDOM());
			
			JSONObject classificationJSON = renderClassification(classification);
			result = classificationJSON.toJSON();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if(classificationResource != null) {
		        try { ((EXistResource) classificationResource).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		    
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
		
		return result;
	}
	
	protected JSONArray renderClassification(Classification classification) {
		JSONArray data = new JSONArray();
		
		for (Term term: classification.getTerm()) {
			JSONMap termJSON = new JSONMap();
			
			JSONString termIdJSON = new JSONString("termId", term.getTermId());
			termJSON.put(termIdJSON);
			
			JSONString nameJSON = new JSONString("name", term.getName());
			termJSON.put(nameJSON);
			
			data.add(termJSON);
		}
		
		return data;
	}
}
