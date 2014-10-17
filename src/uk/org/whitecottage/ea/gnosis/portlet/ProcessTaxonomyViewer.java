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

import uk.org.whitecottage.ea.gnosis.repository.ProcessTaxonomy;
import uk.org.whitecottage.ea.portlet.ProcessResourceAction;
import uk.org.whitecottage.ea.portlet.ProcessResourceRequest;

public class ProcessTaxonomyViewer extends GnosisPortlet {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.gnosis.portlet");
	
    @RenderMode(name = "view")
    public void view(RenderRequest request, RenderResponse response) throws PortletException, IOException {
    	getPortletContext().getRequestDispatcher("/WEB-INF/jsp/ProcessTaxonomyViewer.jsp").include(request, response);
    }
    
    @ProcessResourceRequest(name = "jsonData")
    public void serveJSON(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
        
    	String context = getPortletContext().getRealPath("");
    	ProcessTaxonomy taxonomy = new ProcessTaxonomy(existURI, existRepositoryRoot, context);
    	String json = taxonomy.getJSON();
    	
    	response.getWriter().print(json);
    }

    @ProcessResourceRequest(name = "icon")
    public void serveIcon(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
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

    @ProcessResourceAction(name = "updateDomainAction")
    public void updateDomain(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessTaxonomy taxonomy = new ProcessTaxonomy(existURI, existRepositoryRoot, context);
    	
    	String domainId = request.getParameter("domainId");
    	String name = request.getParameter("domainName");
    	String description = request.getParameter("domainDescription");
       	
    	taxonomy.updateProcessDomain(domainId, name, description);
    }

    @ProcessResourceAction(name = "updateProcessAction")
    public void updateProcess(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessTaxonomy taxonomy = new ProcessTaxonomy(existURI, existRepositoryRoot, context);
    	
    	String capabilityId = request.getParameter("processId");
    	String name = request.getParameter("processName");
    	String description = request.getParameter("processDescription");
   	
    	taxonomy.updateProcess(capabilityId, name, description);
    }

    @ProcessResourceAction(name = "createDomainAction")
    public void createDomain(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessTaxonomy taxonomy = new ProcessTaxonomy(existURI, existRepositoryRoot, context);
    	
    	String domainId = request.getParameter("domainId");
    	String valueChain = request.getParameter("valueChain");
    	String name = request.getParameter("domainName");
    	String description = request.getParameter("domainDescription");
    	
    	taxonomy.createProcessDomain(valueChain, domainId, name, description);
    }

    @ProcessResourceAction(name = "createProcessAction")
    public void createCapability(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessTaxonomy taxonomy = new ProcessTaxonomy(existURI, existRepositoryRoot, context);
    	
    	String domainId = request.getParameter("domainId");
    	String processId = request.getParameter("processId");
    	String name = request.getParameter("processName");
    	String description = request.getParameter("processDescription");
   	
    	taxonomy.createProcess(domainId, processId, name, description);
    }

    @ProcessResourceAction(name = "moveDomainAction")
    public void deleteDomain(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessTaxonomy taxonomy = new ProcessTaxonomy(existURI, existRepositoryRoot, context);
    	
    	String domainId = request.getParameter("domainId");
    	String parentId = request.getParameter("parentId");
    	String before = request.getParameter("before");
    	
    	taxonomy.moveProcessDomain(domainId, parentId, before);
    }

    @ProcessResourceAction(name = "moveProcessAction")
    public void deleteCapability(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessTaxonomy taxonomy = new ProcessTaxonomy(existURI, existRepositoryRoot, context);
    	
    	String processyId = request.getParameter("processId");
    	String parentId = request.getParameter("parentId");
    	String before = request.getParameter("before");

    	taxonomy.moveProcess(processyId, parentId, before);
    }

    @ProcessResourceAction(name = "emptyTrashAction")
    public void emptyRecycleBin(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessTaxonomy taxonomy = new ProcessTaxonomy(existURI, existRepositoryRoot, context);
    	
    	taxonomy.emptyRecycleBin();
    }
}
