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

import uk.org.whitecottage.ea.gnosis.jaxb.framework.Framework;
import uk.org.whitecottage.ea.xmldb.XmldbProcessor;

public abstract class FrameworkProcessor extends XmldbProcessor {
	protected Unmarshaller frameworkUnmarshaller = null;
	protected Marshaller frameworkMarshaller = null;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.xmldb");

	public FrameworkProcessor(String URI, String repositoryRoot, String context) {
		super(URI, repositoryRoot);

		try {
		    JAXBContext frameworkContext = JAXBContext.newInstance("uk.org.whitecottage.ea.gnosis.jaxb.framework");
		    frameworkUnmarshaller = createUnmarshaller(frameworkContext, context + "/WEB-INF/xsd/framework.xsd");
		    frameworkMarshaller = frameworkContext.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	protected Framework loadFramework() {
		Collection repository = null;
		XMLResource frameworkResource = null;
		try {   
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");

		    return (Framework) frameworkUnmarshaller.unmarshal(frameworkResource.getContentAsDOM());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if(frameworkResource != null) {
		        try { ((EXistResource) frameworkResource).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		    
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
		
		return null;
	}

	protected void saveFramework(Framework framework) {
		Collection repository = null;
		try {   
			repository = getCollection("");
			
			storeJaxbResource(repository, "framework.xml", framework, frameworkMarshaller);
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
