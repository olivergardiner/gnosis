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

import uk.org.whitecottage.ea.gnosis.repository.ValueChain;
import uk.org.whitecottage.ea.portlet.ProcessResourceAction;
import uk.org.whitecottage.ea.portlet.ProcessResourceRequest;

public class ValueChainViewer extends Gnosis2Portlet {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.gnosis.portlet");
	
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

    @ProcessResourceAction(name = "updateValueChainAction")
    public void updateDomain(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ValueChain valueChain = new ValueChain(existURI, existRepositoryRoot, context);
    	
    	String valueChainId = request.getParameter("valueChainId");
    	String name = request.getParameter("name");
    	String type = request.getParameter("type");
       	
    	valueChain.updateValueChain(valueChainId, name, type);

    	String json = valueChain.getJSON();
    	
    	response.getWriter().print(json);
    }

    @ProcessResourceAction(name = "moveValueChainAction")
    public void deleteDomain(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ValueChain valueChain = new ValueChain(existURI, existRepositoryRoot, context);
    	
    	String valueChainId = request.getParameter("valueChainId");
    	String from = request.getParameter("from");
    	String to = request.getParameter("to");
    	String position = request.getParameter("position");
       	
    	valueChain.moveValueChain(valueChainId, from, to, Integer.parseInt(position));

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
