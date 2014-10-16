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

import uk.org.whitecottage.ea.gnosis.repository.TechnologyDomains;
import uk.org.whitecottage.ea.gnosis.repository.ValueChain;
import uk.org.whitecottage.ea.portlet.ProcessResourceAction;
import uk.org.whitecottage.ea.portlet.ProcessResourceRequest;

public class TechnologyDomainsViewer extends Gnosis2Portlet {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.gnosis.portlet");
	
    @RenderMode(name = "view")
    public void view(RenderRequest request, RenderResponse response) throws PortletException, IOException {
    	getPortletContext().getRequestDispatcher("/WEB-INF/jsp/TechnologyDomainsViewer.jsp").include(request, response);
    }
    
    @ProcessResourceRequest(name = "jsonData")
    public void serveJSON(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
        
    	String layer = request.getPreferences().getValue("layer", "BusinessApplications");
    	String context = getPortletContext().getRealPath("");
    	TechnologyDomains domains = new TechnologyDomains(existURI, existRepositoryRoot, context);
    	String json = domains.getJSON(layer);
    	
    	response.getWriter().print(json);
    }

    @ProcessResourceRequest(name = "valueChainJsonData")
    public void serveValueChainJSON(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
        
    	String context = getPortletContext().getRealPath("");
    	ValueChain valueChain = new ValueChain(existURI, existRepositoryRoot, context);
    	String json = valueChain.getJSON(false);
    	
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
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	TechnologyDomains domains = new TechnologyDomains(existURI, existRepositoryRoot, context);
    	
    	String domainId = request.getParameter("domainId");
    	String name = request.getParameter("domainName");
    	String description = request.getParameter("domainDescription");
    	String valueChain = request.getParameter("valueChain");
       	
    	domains.updateTechnologyDomain(domainId, name, description, valueChain);
    }

    @ProcessResourceAction(name = "updateCapabilityAction")
    public void updateCapability(ResourceRequest request, ResourceResponse response) {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	TechnologyDomains domains = new TechnologyDomains(existURI, existRepositoryRoot, context);
    	
    	String capabilityId = request.getParameter("capabilityId");
    	String name = request.getParameter("capabilityName");
    	String description = request.getParameter("capabilityDescription");
   	
    	domains.updateCapability(capabilityId, name, description);
    }

    @ProcessResourceAction(name = "createDomainAction")
    public void createDomain(ResourceRequest request, ResourceResponse response) {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	TechnologyDomains domains = new TechnologyDomains(existURI, existRepositoryRoot, context);
    	
    	String domainId = request.getParameter("domainId");
    	String name = request.getParameter("domainName");
    	String description = request.getParameter("domainDescription");
    	String position = request.getParameter("position");
    	if (position == null) {
    		position = "-1";
    	}
    	
    	String layer = request.getPreferences().getValue("layer", "BusinessApplications");
       	
    	domains.createTechnologyDomain(layer, domainId, name, description, Integer.parseInt(position));
    }

    @ProcessResourceAction(name = "createCapabilityAction")
    public void createCapability(ResourceRequest request, ResourceResponse response) {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	TechnologyDomains domains = new TechnologyDomains(existURI, existRepositoryRoot, context);
    	
    	String domainId = request.getParameter("domainId");
    	String capabilityId = request.getParameter("capabilityId");
    	String name = request.getParameter("capabilityName");
    	String description = request.getParameter("capabilityDescription");
    	String position = request.getParameter("position");
    	if (position == null) {
    		position = "-1";
    	}
   	
    	domains.createCapability(domainId, capabilityId, name, description, Integer.parseInt(position));
    }

    @ProcessResourceAction(name = "moveDomainAction")
    public void deleteDomain(ResourceRequest request, ResourceResponse response) {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	TechnologyDomains domains = new TechnologyDomains(existURI, existRepositoryRoot, context);
    	
    	String domainId = request.getParameter("domainId");
    	String parentId = request.getParameter("parentId");
    	String position = request.getParameter("position");
    	
    	String layer = request.getPreferences().getValue("layer", "BusinessApplications");
       	
    	domains.moveTechnologyDomain(layer, domainId, parentId, Integer.parseInt(position));
    }

    @ProcessResourceAction(name = "moveCapabilityAction")
    public void deleteCapability(ResourceRequest request, ResourceResponse response) {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	TechnologyDomains domains = new TechnologyDomains(existURI, existRepositoryRoot, context);
    	
    	String capabilityId = request.getParameter("capabilityId");
    	String parentId = request.getParameter("parentId");
    	String position = request.getParameter("position");

    	domains.moveCapability(capabilityId, parentId, Integer.parseInt(position));
    }

    @ProcessResourceAction(name = "emptyTrashAction")
    public void emptyRecycleBin(ResourceRequest request, ResourceResponse response) {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	TechnologyDomains domains = new TechnologyDomains(existURI, existRepositoryRoot, context);
    	
    	domains.emptyRecycleBin();
    }
}
