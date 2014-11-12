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

import uk.org.whitecottage.ea.gnosis.jaxb.services.Services;
import uk.org.whitecottage.ea.xmldb.XmldbProcessor;

public abstract class ITServicesProcessor extends XmldbProcessor {
	protected Unmarshaller servicesUnmarshaller = null;
	protected Marshaller servicesMarshaller = null;

	public ITServicesProcessor(String URI, String repositoryRoot, String context) {
		super(URI, repositoryRoot);

		try {
		    JAXBContext servicesContext = JAXBContext.newInstance("uk.org.whitecottage.ea.gnosis.jaxb.services");
		    servicesUnmarshaller = createUnmarshaller(servicesContext, context + "/WEB-INF/xsd/services.xsd");
		    servicesMarshaller = servicesContext.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	protected Services loadServices() {
		Collection repository = null;
		XMLResource servicesResource = null;
		try {   
			repository = getCollection("");
		    servicesResource = getResource(repository, "services.xml");

		    return (Services) servicesUnmarshaller.unmarshal(servicesResource.getContentAsDOM());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if(servicesResource != null) {
		        try { ((EXistResource) servicesResource).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		    
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
		
		return null;
	}

	protected void saveServices(Services services) {
		Collection repository = null;
		try {   
			repository = getCollection("");
			
			storeJaxbResource(repository, "services.xml", services, servicesMarshaller);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
	}
}
