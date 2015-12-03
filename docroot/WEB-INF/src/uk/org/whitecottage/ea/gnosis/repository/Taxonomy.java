package uk.org.whitecottage.ea.gnosis.repository;

import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.exist.xmldb.EXistResource;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import uk.org.whitecottage.ea.gnosis.json.JSONMap;
import uk.org.whitecottage.ea.xmldb.XmldbProcessor;

public class Taxonomy extends XmldbProcessor {
	protected Unmarshaller taxonomyUnmarshaller = null;
	protected Marshaller taxonomyMarshaller = null;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.gnosis.taxonomy");

	public Taxonomy(String URI, String repositoryRoot, String context) {
		super(URI, repositoryRoot);

		try {
		    JAXBContext taxonomyContext = JAXBContext.newInstance("uk.org.whitecottage.ea.gnosis.jaxb.taxonomy");
		    taxonomyUnmarshaller = createUnmarshaller(taxonomyContext, context + "/WEB-INF/xsd/taxonomy.xsd");
		    taxonomyMarshaller = taxonomyContext.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	public String getJSON() {
		String result = null;

		Collection repository = null;
		XMLResource taxonomyResource = null;
		try {   
			repository = getCollection("");
		    taxonomyResource = getResource(repository, "taxonomies.xml");
		    uk.org.whitecottage.ea.gnosis.jaxb.taxonomy.Taxonomies taxonomies = (uk.org.whitecottage.ea.gnosis.jaxb.taxonomy.Taxonomies) taxonomyUnmarshaller.unmarshal(taxonomyResource.getContentAsDOM());
			
			JSONMap taxonomyJSON = new JSONMap();
			
			result = taxonomyJSON.toJSON();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    if(taxonomyResource != null) {
		        try { ((EXistResource) taxonomyResource).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		    
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
		
		return result;
	}
}
