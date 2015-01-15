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

import uk.org.whitecottage.ea.gnosis.repository.TechnologyDomains;
import uk.org.whitecottage.ea.gnosis.repository.ValueChain;
import uk.org.whitecottage.ea.portlet.ProcessResourceAction;
import uk.org.whitecottage.ea.portlet.ProcessResourceRequest;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class TechnologyDomainsViewer extends GnosisPortlet {
	@SuppressWarnings("unused")
	private static final Log log = LogFactoryUtil.getLog(TechnologyDomainsViewer.class);
	
    @RenderMode(name = "view")
    public void view(RenderRequest request, RenderResponse response) throws PortletException, IOException {
    	getPortletContext().getRequestDispatcher("/WEB-INF/jsp/TechnologyDomainsViewer.jsp").include(request, response);
    }
    
    @ProcessResourceRequest(name = "jsonData")
    public void serveJSON(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
        
    	String layer = request.getPreferences().getValue("layer", "BusinessApplications");
    	String context = getPortletContext().getRealPath("");
    	TechnologyDomains domains = new TechnologyDomains(existURI, existRepositoryRoot, context);
    	String json = domains.getJSON(layer);
    	
    	response.getWriter().print(json);
    }

    @ProcessResourceRequest(name = "valueChainJsonData")
    public void serveValueChainJSON(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
        
    	String context = getPortletContext().getRealPath("");
    	ValueChain valueChain = new ValueChain(existURI, existRepositoryRoot, context);
    	String json = valueChain.getJSON(false);
    	
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
    	TechnologyDomains domains = new TechnologyDomains(existURI, existRepositoryRoot, context);
    	
    	String domainId = getParameter(request, "domainId");
    	String name = getParameter(request, "domainName");
    	String description = getParameter(request, "domainDescription");
    	String valueChain = getParameter(request, "valueChain");
       	
    	domains.updateTechnologyDomain(domainId, name, description, valueChain);
    }

    @ProcessResourceAction(name = "updateCapabilityAction")
    public void updateCapability(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	TechnologyDomains domains = new TechnologyDomains(existURI, existRepositoryRoot, context);
    	
    	String capabilityId = getParameter(request, "capabilityId");
    	String name = getParameter(request, "capabilityName");
    	String description = getParameter(request, "capabilityDescription");
   	
    	domains.updateCapability(capabilityId, name, description);
    }

    @ProcessResourceAction(name = "createDomainAction")
    public void createDomain(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	TechnologyDomains domains = new TechnologyDomains(existURI, existRepositoryRoot, context);
    	
    	String domainId = getParameter(request, "domainId");
    	String name = getParameter(request, "domainName");
    	String description = getParameter(request, "domainDescription");
    	String position = getParameter(request, "position");
    	if (position == null) {
    		position = "-1";
    	}
    	
    	String layer = request.getPreferences().getValue("layer", "BusinessApplications");
       	
    	domains.createTechnologyDomain(layer, domainId, name, description, Integer.parseInt(position));
    }

    @ProcessResourceAction(name = "createCapabilityAction")
    public void createCapability(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	TechnologyDomains domains = new TechnologyDomains(existURI, existRepositoryRoot, context);
    	
    	String domainId = getParameter(request, "domainId");
    	String capabilityId = getParameter(request, "capabilityId");
    	String name = getParameter(request, "capabilityName");
    	String description = getParameter(request, "capabilityDescription");
    	String position = getParameter(request, "position");
    	if (position == null) {
    		position = "-1";
    	}
   	
    	domains.createCapability(domainId, capabilityId, name, description, Integer.parseInt(position));
    }

    @ProcessResourceAction(name = "moveDomainAction")
    public void deleteDomain(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	TechnologyDomains domains = new TechnologyDomains(existURI, existRepositoryRoot, context);
    	
    	String domainId = getParameter(request, "domainId");
    	String parentId = getParameter(request, "parentId");
    	String position = getParameter(request, "position");
    	
    	String layer = request.getPreferences().getValue("layer", "BusinessApplications");
       	
    	domains.moveTechnologyDomain(layer, domainId, parentId, Integer.parseInt(position));
    }

    @ProcessResourceAction(name = "moveCapabilityAction")
    public void deleteCapability(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	TechnologyDomains domains = new TechnologyDomains(existURI, existRepositoryRoot, context);
    	
    	String capabilityId = getParameter(request, "capabilityId");
    	String parentId = getParameter(request, "parentId");
    	String position = getParameter(request, "position");

    	domains.moveCapability(capabilityId, parentId, Integer.parseInt(position));
    }

    @ProcessResourceAction(name = "emptyTrashAction")
    public void emptyRecycleBin(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	TechnologyDomains domains = new TechnologyDomains(existURI, existRepositoryRoot, context);
    	
    	domains.emptyRecycleBin();
    }
}