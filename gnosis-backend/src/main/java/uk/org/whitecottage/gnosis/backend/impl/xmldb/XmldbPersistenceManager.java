package uk.org.whitecottage.gnosis.backend.impl.xmldb;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.exist.xmldb.EXistResource;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import uk.org.whitecottage.gnosis.jaxb.applications.Application;
import uk.org.whitecottage.gnosis.jaxb.applications.Applications;
import uk.org.whitecottage.gnosis.jaxb.framework.Framework;
import uk.org.whitecottage.xmldb.XmldbProcessor;

public class XmldbPersistenceManager extends XmldbProcessor {

	protected Unmarshaller applicationsUnmarshaller = null;
	protected Marshaller applicationsMarshaller = null;
	protected Unmarshaller frameworkUnmarshaller = null;
	protected Marshaller frameworkMarshaller = null;

	public XmldbPersistenceManager(String URI, String repositoryRoot) {
		super(URI, repositoryRoot);

		try {
		    JAXBContext context = JAXBContext.newInstance("uk.org.whitecottage.gnosis.jaxb.applications");
		    applicationsUnmarshaller = createUnmarshaller(context, "/uk/org/whitecottage/gnosis/schema/applications.xsd");
		    applicationsMarshaller = context.createMarshaller();
		    context = JAXBContext.newInstance("uk.org.whitecottage.gnosis.jaxb.framework");
		    frameworkUnmarshaller = createUnmarshaller(context, "/uk/org/whitecottage/gnosis/schema/framework.xsd");
		    frameworkMarshaller = context.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	public List<Application> getApplications() {
		List<Application> applicationList = null;
		Collection repository = null;
		XMLResource applicationsResource = null;
		try {
			repository = getCollection("");
		    applicationsResource = getResource(repository, "applications.xml");
			Applications applications = (Applications) applicationsUnmarshaller.unmarshal(applicationsResource.getContentAsDOM());
			
			applicationList = applications.getApplication();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if(applicationsResource != null) {
		        try { ((EXistResource) applicationsResource).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		    
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
		
		return applicationList;
	}
	
	public Application getApplication(String applicationId) {
		Collection repository = null;
		Resource applicationResource = null;
		try {
			repository = getCollection("");
			ResourceSet results = queryCollection(repository, "/Applications/Application[@id='" + applicationId + "']");
			if (results.getSize() > 0) {
				applicationResource = results.getResource(0);
				if (applicationResource.getResourceType().equals("XMLResource")) {
					applicationsUnmarshaller.unmarshal(((XMLResource) applicationResource).getContentAsDOM());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if(applicationResource != null) {
		        try { ((EXistResource) applicationResource).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		    
		    if(repository != null) {
		        try { repository.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
		    }
		}
		
		return null;
	}
	
	public Framework getFramework() {
		Framework framework = null;
		Collection repository = null;
		XMLResource frameworkResource = null;
		try {
			repository = getCollection("");
		    frameworkResource = getResource(repository, "framework.xml");
			framework = (Framework) frameworkUnmarshaller.unmarshal(frameworkResource.getContentAsDOM());
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
		
		return framework;
	}
}
