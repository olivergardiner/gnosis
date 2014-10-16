package uk.org.whitecottage.ea.gnosis.portlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.portlet.PortletException;
import javax.portlet.RenderMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.io.IOUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import uk.org.whitecottage.ea.gnosis.repository.ApplicationsEstate;
import uk.org.whitecottage.ea.gnosis.repository.TechnologyDomains;
import uk.org.whitecottage.ea.gnosis.repository.applications.LifecyclePresentation;
import uk.org.whitecottage.ea.portlet.ProcessResourceAction;
import uk.org.whitecottage.ea.portlet.ProcessResourceRequest;

public class ApplicationsViewer extends Gnosis2Portlet {

	//@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.gnosis.portlet");
	
    @RenderMode(name = "view")
    public void view(RenderRequest request, RenderResponse response) throws PortletException, IOException {
    	getPortletContext().getRequestDispatcher("/WEB-INF/jsp/ApplicationsViewer.jsp").include(request, response);
    }
    
    @ProcessResourceRequest(name = "applicationsListJsonData")
    public void serveApplicationsListJSON(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	
    	ApplicationsEstate applications = new ApplicationsEstate(existURI, existRepositoryRoot, context);
    	String json = applications.getApplicationsListJSON();
    	
		response.setContentType("application/json");
    	response.getWriter().print(json);
    }

    @ProcessResourceRequest(name = "applicationJsonData")
    public void serveApplicationJSON(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	
    	ApplicationsEstate applications = new ApplicationsEstate(existURI, existRepositoryRoot, context);
    	String json = applications.getApplicationJSON(request.getParameter("applicationId"));
    	
		response.setContentType("application/json");
    	response.getWriter().print(json);
    }

    @ProcessResourceRequest(name = "classificationJsonData")
    public void serveClassificationJSON(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	
    	ApplicationsEstate applications = new ApplicationsEstate(existURI, existRepositoryRoot, context);
    	String json = applications.getApplicationJSON(request.getParameter("classification"));
    	
		response.setContentType("application/json");
    	response.getWriter().print(json);
    }

    @ProcessResourceRequest(name = "frameworkJsonData")
    public void serveFrameworkJSON(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
        
    	String context = getPortletContext().getRealPath("");
    	TechnologyDomains domains = new TechnologyDomains(existURI, existRepositoryRoot, context);
    	String json = domains.getJSON("all", false);
    	
		response.setContentType("application/json");
    	response.getWriter().print(json);
    }

    @ProcessResourceRequest(name = "icon")
    public void serveIcon(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
		String icon = request.getParameter("icon");
	
		response.setContentType("image/x-icon");
		
		File file = new File(getPortletContext().getRealPath("/WEB-INF/icons/" + icon));
		try {
			IOUtils.copy(new FileInputStream(file), response.getPortletOutputStream());
		} catch (FileNotFoundException e) {
			// Fail silently
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @ProcessResourceRequest(name = "pptx")
    public void serveLifecyclePresentation(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");

    	String gnosisDataDir = System.getProperty("jboss.server.data.dir") + File.separator + "gnosis2";
		String gnosisOoxmlDir = gnosisDataDir + File.separator + "ooxml" + File.separator;
        
		XMLSlideShow ppt;
    	File template = new File(gnosisOoxmlDir + "lifecycle-tmpl.pptx");
    	if (template.exists()) {
    		ppt = new XMLSlideShow(new FileInputStream(template));
    	} else {
    		ppt = new XMLSlideShow();
    	}
    	
    	// Build the slides
    	LifecyclePresentation arb = new LifecyclePresentation(existURI, existRepositoryRoot, context);
    	arb.render(ppt);
    	
    	response.setContentType("application/vnd.openxmlformats-officedocument.presentationml.presentation");
    	//response.setHeader("Content-Disposition", "attachment;filename=\"Lifecycle.pptx\"");
    	ppt.write(response.getPortletOutputStream());
     }
    
    @ProcessResourceAction(name = "addApplicationAction")
    public void addApplication(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
    	log.info("Add application");

    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ApplicationsEstate applications = new ApplicationsEstate(existURI, existRepositoryRoot, context);
    	
    	String applicationId = request.getParameter("applicationId");
    	String name = request.getParameter("applicationName");
    	String description = request.getParameter("applicationDescription");
       	
    	applications.addApplication(applicationId, name, description);
    	
    	String json = applications.getApplicationJSON(request.getParameter("applicationId"));    	

		response.setContentType("application/json");
		response.getWriter().print(json);
    }

    @ProcessResourceAction(name = "removeApplicationAction")
    public void removeApplication(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
    	log.info("Remove application");

    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ApplicationsEstate applications = new ApplicationsEstate(existURI, existRepositoryRoot, context);
    	
    	String applicationId = request.getParameter("applicationId");
       	
    	applications.removeApplication(applicationId);
    	
    	String json = applications.getApplicationJSON(request.getParameter("applicationId"));    	

		response.setContentType("application/json");
		response.getWriter().print(json);
    }

    @ProcessResourceAction(name = "updateApplicationBasicAction")
    public void updateApplicationBasic(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
    	log.info("Update application basic");

    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ApplicationsEstate applications = new ApplicationsEstate(existURI, existRepositoryRoot, context);
    	
    	String applicationId = request.getParameter("applicationId");
    	String name = request.getParameter("applicationName");
    	String description = request.getParameter("applicationDescription");
       	
    	applications.updateApplicationBasic(applicationId, name, description);
    	
    	String json = applications.getApplicationJSON(request.getParameter("applicationId"));    	

		response.setContentType("application/json");
		response.getWriter().print(json);
    }

    @ProcessResourceAction(name = "addCapabilityAction")
    public void updateCapabilities(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
    	log.info("Add capability");

    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ApplicationsEstate applications = new ApplicationsEstate(existURI, existRepositoryRoot, context);
    	
    	logParameters(request);
    	
    	String applicationId = request.getParameter("applicationId");
    	String capability = request.getParameter("capability");
    	       	
    	applications.addCapability(applicationId, capability);
    	
    	String json = applications.getApplicationJSON(request.getParameter("applicationId"));    	

		response.setContentType("application/json");
		response.getWriter().print(json);
    }

    @ProcessResourceAction(name = "removeCapabilityAction")
    public void removeCapability(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
    	log.info("Delete capability");

    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ApplicationsEstate applications = new ApplicationsEstate(existURI, existRepositoryRoot, context);
    	
    	logParameters(request);
    	
    	String applicationId = request.getParameter("applicationId");
    	String capability = request.getParameter("capability");
    	       	
    	applications.removeCapability(applicationId, capability);
    	
    	String json = applications.getApplicationJSON(request.getParameter("applicationId"));    	

		response.setContentType("application/json");
		response.getWriter().print(json);
    }

    @ProcessResourceAction(name = "updateLifecycleAction")
    public void updateLifecycle(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
    	log.info("Update timeline");

    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ApplicationsEstate applications = new ApplicationsEstate(existURI, existRepositoryRoot, context);
    	       	
    	String applicationId = request.getParameter("applicationId");
    	String stage = request.getParameter("stage");
    	String mode = request.getParameter("mode");
    	String date = request.getParameter("date");
    	
    	applications.updateLifecycle(applicationId, stage, mode, date);
    	
    	String json = applications.getApplicationJSON(request.getParameter("applicationId"));    	

		response.setContentType("application/json");
		response.getWriter().print(json);
   }

    @ProcessResourceAction(name = "removeLifecycleAction")
    public void removeLifecycle(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
    	log.info("Update timeline");

    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ApplicationsEstate applications = new ApplicationsEstate(existURI, existRepositoryRoot, context);
    	       	
       	String applicationId = request.getParameter("applicationId");
       	String stage = request.getParameter("stage");
    	
    	applications.removeLifecycle(applicationId, stage);
 
    	String json = applications.getApplicationJSON(request.getParameter("applicationId"));    	

    	response.setContentType("application/json");
    	response.getWriter().print(json);
    }
}
