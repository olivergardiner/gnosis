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

public class ValueChainViewer extends GnosisPortlet {
	@SuppressWarnings("unused")
	private static final Log log = LogFactoryUtil.getLog(ValueChainViewer.class);
	
    @RenderMode(name = "view")
    public void view(RenderRequest request, RenderResponse response) throws PortletException, IOException {
    	getPortletContext().getRequestDispatcher("/WEB-INF/jsp/ValueChainViewer.jsp").include(request, response);
    }
    
    @ProcessResourceRequest(name = "jsonData")
    public void serveJSON(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
        
    	String context = getPortletContext().getRealPath("");
    	ValueChain valueChain = new ValueChain(existURI, existRepositoryRoot, context);
    	String json = valueChain.getJSON();
    	
    	response.getWriter().print(json);
    }

    @ProcessResourceRequest(name = "frameworkJsonData")
    public void serveFrameworkJSON(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
        
    	String context = getPortletContext().getRealPath("");
    	TechnologyDomains domains = new TechnologyDomains(existURI, existRepositoryRoot, context);
    	String json = domains.getJSON("all", false);
    	
		response.setContentType("application/json");
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

    @ProcessResourceAction(name = "updateValueChainAction")
    public void updateValueChain(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ValueChain valueChain = new ValueChain(existURI, existRepositoryRoot, context);
    	
    	String activityId = getParameter(request, "activityId");
    	String ecosystemId = getParameter(request, "ecosystemId");
    	String capabilityId = getParameter(request, "capabilityId");
    	String name = getParameter(request, "name");
    	String description = getParameter(request, "description");
    	String type = getParameter(request, "type");
       	
    	valueChain.updateValueChain(type, activityId, ecosystemId, capabilityId, name, description);

    	String json = valueChain.getJSON();
    	
    	response.getWriter().print(json);
    }

    @ProcessResourceAction(name = "moveValueChainAction")
    public void moveValueChain(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ValueChain valueChain = new ValueChain(existURI, existRepositoryRoot, context);
    	
    	String id = getParameter(request, "id");
    	String from = getParameter(request, "from");
    	String fromId = getParameter(request, "fromId");
    	String to = getParameter(request, "to");
    	String toId = getParameter(request, "toId");
    	String position = getParameter(request, "position");
       	
    	valueChain.moveValueChain(id, from, fromId, to, toId, Integer.parseInt(position));

    	String json = valueChain.getJSON();
    	
    	response.getWriter().print(json);
    }
    
    @ProcessResourceAction(name = "createActivityAction")
    public void createActivity(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ValueChain valueChain = new ValueChain(existURI, existRepositoryRoot, context);
    	
    	String activityId = getParameter(request, "activityId");
    	String name = getParameter(request, "name");
    	String description = getParameter(request, "description");
    	String type = getParameter(request, "type");
       	
    	valueChain.createActivity(type, activityId, name, description);

    	String json = valueChain.getJSON();
    	
    	response.getWriter().print(json);
    }

    @ProcessResourceAction(name = "createEcosystemAction")
    public void createEcosystem(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ValueChain valueChain = new ValueChain(existURI, existRepositoryRoot, context);
    	
    	String activityId = getParameter(request, "activityId");
    	String name = getParameter(request, "name");
    	String description = getParameter(request, "description");
    	String ecosystemId = getParameter(request, "ecosystemId");
       	
    	valueChain.createEcosystem(activityId, ecosystemId, name, description);

    	String json = valueChain.getJSON();
    	
    	response.getWriter().print(json);
    }

    @ProcessResourceAction(name = "createCapabilityAction")
    public void createCapabilityInstance(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ValueChain valueChain = new ValueChain(existURI, existRepositoryRoot, context);
    	
    	String ecosystemId = getParameter(request, "ecosystemId");
    	String name = getParameter(request, "name");
    	String description = getParameter(request, "description");
    	String capabilityId = getParameter(request, "capabilityId");
       	
    	valueChain.createCapabilityInstance(ecosystemId, capabilityId, name, description);

    	String json = valueChain.getJSON();
    	
    	response.getWriter().print(json);
    }

    @ProcessResourceAction(name = "emptyTrashAction")
    public void emptyRecycleBin(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ValueChain valueChain = new ValueChain(existURI, existRepositoryRoot, context);
    	
    	valueChain.emptyRecycleBin();

    	String json = valueChain.getJSON();
    	
    	response.getWriter().print(json);
    }
}
