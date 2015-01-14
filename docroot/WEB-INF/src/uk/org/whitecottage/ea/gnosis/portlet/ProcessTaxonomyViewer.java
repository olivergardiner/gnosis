package uk.org.whitecottage.ea.gnosis.portlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class ProcessTaxonomyViewer extends GnosisPortlet {
	@SuppressWarnings("unused")
	private static final Log log = LogFactoryUtil.getLog(ProcessTaxonomyViewer.class);
	
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
		String icon = getParameter(request, "icon");
	
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
    	
    	String domainId = getParameter(request, "domainId");
    	String name = getParameter(request, "domainName");
    	String description = getParameter(request, "domainDescription");
       	
    	taxonomy.updateProcessDomain(domainId, name, description);
    }

    @ProcessResourceAction(name = "updateProcessAction")
    public void updateProcess(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessTaxonomy taxonomy = new ProcessTaxonomy(existURI, existRepositoryRoot, context);
    	
    	String capabilityId = getParameter(request, "processId");
    	String name = getParameter(request, "processName");
    	String description = getParameter(request, "processDescription");
   	
    	taxonomy.updateProcess(capabilityId, name, description);
    }

    @ProcessResourceAction(name = "createDomainAction")
    public void createDomain(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessTaxonomy taxonomy = new ProcessTaxonomy(existURI, existRepositoryRoot, context);
    	
    	String domainId = getParameter(request, "domainId");
    	String valueChain = getParameter(request, "valueChain");
    	String name = getParameter(request, "domainName");
    	String description = getParameter(request, "domainDescription");
    	
    	taxonomy.createProcessDomain(valueChain, domainId, name, description);
    }

    @ProcessResourceAction(name = "createProcessAction")
    public void createCapability(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessTaxonomy taxonomy = new ProcessTaxonomy(existURI, existRepositoryRoot, context);
    	
    	String domainId = getParameter(request, "domainId");
    	String processId = getParameter(request, "processId");
    	String name = getParameter(request, "processName");
    	String description = getParameter(request, "processDescription");
   	
    	taxonomy.createProcess(domainId, processId, name, description);
    }

    @ProcessResourceAction(name = "moveDomainAction")
    public void deleteDomain(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessTaxonomy taxonomy = new ProcessTaxonomy(existURI, existRepositoryRoot, context);
    	
    	String domainId = getParameter(request, "domainId");
    	String parentId = getParameter(request, "parentId");
    	String before = getParameter(request, "before");
    	
    	taxonomy.moveProcessDomain(domainId, parentId, before);
    }

    @ProcessResourceAction(name = "moveProcessAction")
    public void deleteCapability(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessTaxonomy taxonomy = new ProcessTaxonomy(existURI, existRepositoryRoot, context);
    	
    	String processyId = getParameter(request, "processId");
    	String parentId = getParameter(request, "parentId");
    	String before = getParameter(request, "before");

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
