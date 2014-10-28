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

import uk.org.whitecottage.ea.gnosis.repository.ProcessFlows;
import uk.org.whitecottage.ea.gnosis.repository.ProcessTaxonomy;
import uk.org.whitecottage.ea.gnosis.repository.framework.ProcessFlowPresentation;
import uk.org.whitecottage.ea.portlet.ProcessResourceAction;
import uk.org.whitecottage.ea.portlet.ProcessResourceRequest;

public class ProcessFlowViewer extends GnosisPortlet {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.gnosis.portlet");
	
    @RenderMode(name = "view")
    public void view(RenderRequest request, RenderResponse response) throws PortletException, IOException {
    	getPortletContext().getRequestDispatcher("/WEB-INF/jsp/ProcessFlowViewer.jsp").include(request, response);
    }
    
    @ProcessResourceRequest(name = "jsonData")
    public void serveJSON(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
        
    	String context = getPortletContext().getRealPath("");
    	ProcessFlows processFlows = new ProcessFlows(existURI, existRepositoryRoot, context);
    	String json = processFlows.getJSON();
    	
    	response.getWriter().print(json);
    }

    @ProcessResourceRequest(name = "processJsonData")
    public void serveProcessJSON(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
        
    	String context = getPortletContext().getRealPath("");
    	ProcessTaxonomy taxonomy = new ProcessTaxonomy(existURI, existRepositoryRoot, context);
    	String json = taxonomy.getJSON(false);
    	
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

    @ProcessResourceRequest(name = "pptx")
    public void serveLifecyclePresentation(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");

    	String gnosisDataDir = System.getProperty("jboss.server.data.dir") + File.separator + "gnosis";
		String gnosisOoxmlDir = gnosisDataDir + File.separator + "ooxml" + File.separator;
        
		XMLSlideShow ppt;
    	File template = new File(gnosisOoxmlDir + "process-flow-tmpl.pptx");
    	if (template.exists()) {
    		ppt = new XMLSlideShow(new FileInputStream(template));
    	} else {
    		ppt = new XMLSlideShow();
    	}
    	
    	// Build the slides
    	ProcessFlowPresentation arb = new ProcessFlowPresentation(existURI, existRepositoryRoot, context);
    	arb.render(ppt);
    	
    	response.setContentType("application/vnd.openxmlformats-officedocument.presentationml.presentation");
    	//response.setHeader("Content-Disposition", "attachment;filename=\"Lifecycle.pptx\"");
    	ppt.write(response.getPortletOutputStream());
     }

    @ProcessResourceAction(name = "addFlowAction")
    public void addFlow(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessFlows processFlows = new ProcessFlows(existURI, existRepositoryRoot, context);
    	
    	String flowId = request.getParameter("flowId");
        	
    	processFlows.addFlow(flowId);
    	
    	String json = processFlows.getJSON();
    	
    	response.getWriter().print(json);
    }

    @ProcessResourceAction(name = "copyFlowAction")
    public void copyFlow(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessFlows processFlows = new ProcessFlows(existURI, existRepositoryRoot, context);
    	
    	String flowId = request.getParameter("flowId");
    	String copyId = request.getParameter("copyId");
       	
    	processFlows.copyFlow(flowId, copyId);
    	
    	String json = processFlows.getJSON();
    	
    	response.getWriter().print(json);
    }

    @ProcessResourceAction(name = "renameFlowAction")
    public void renameFlow(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessFlows processFlows = new ProcessFlows(existURI, existRepositoryRoot, context);
    	
    	String flowId = request.getParameter("flowId");
    	String name = request.getParameter("name");
       	
    	processFlows.renameFlow(flowId, name);
    	
    	String json = processFlows.getJSON();
    	
    	response.getWriter().print(json);
    }

    @ProcessResourceAction(name = "deleteFlowAction")
    public void deleteFlow(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessFlows processFlows = new ProcessFlows(existURI, existRepositoryRoot, context);
    	
    	String flowId = request.getParameter("flowId");
       	
    	processFlows.deleteFlow(flowId);
    	
    	String json = processFlows.getJSON();
    	
    	response.getWriter().print(json);
    }

    @ProcessResourceAction(name = "updateInstanceAction")
    public void updateProcessInstance(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessFlows processFlows = new ProcessFlows(existURI, existRepositoryRoot, context);
    	
    	String flowId = request.getParameter("flowId");
    	String instanceId = request.getParameter("instanceId");
    	String duration = request.getParameter("duration");
    	String mode = request.getParameter("mode");
       	
    	processFlows.updateProcessInstance(flowId, instanceId, duration, mode);

    	String json = processFlows.getJSON();
    	
    	response.getWriter().print(json);
    }

    @ProcessResourceAction(name = "moveInstanceAction")
    public void moveProcessInstance(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessFlows processFlows = new ProcessFlows(existURI, existRepositoryRoot, context);
    	
    	String flowId = request.getParameter("flowId");
    	String instanceId = request.getParameter("instanceId");
    	String position = request.getParameter("position");
       	
    	processFlows.moveProcessInstance(flowId, instanceId, Integer.parseInt(position));

    	String json = processFlows.getJSON();
    	
    	response.getWriter().print(json);
    }

    @ProcessResourceAction(name = "addParentAction")
    public void addParentDependency(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessFlows processFlows = new ProcessFlows(existURI, existRepositoryRoot, context);
    	
    	String flowId = request.getParameter("flowId");
    	String instanceId = request.getParameter("instanceId");
    	String parentId = request.getParameter("parentId");
       	
    	processFlows.addParentDependency(flowId, instanceId, parentId);

    	String json = processFlows.getJSON();
    	
    	response.getWriter().print(json);
    }

    @ProcessResourceAction(name = "updatePredecessorAction")
    public void updatePredecessorDependency(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessFlows processFlows = new ProcessFlows(existURI, existRepositoryRoot, context);
    	
    	String flowId = request.getParameter("flowId");
    	String instanceId = request.getParameter("instanceId");
    	String predecessorId = request.getParameter("predecessorId");
    	String contiguous = request.getParameter("contiguous");
       	
    	processFlows.updatePredecessorDependency(flowId, instanceId, predecessorId, contiguous);

    	String json = processFlows.getJSON();
    	
    	response.getWriter().print(json);
    }

    @ProcessResourceAction(name = "deleteItemAction")
    public void deleteItem(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ProcessFlows processFlows = new ProcessFlows(existURI, existRepositoryRoot, context);
    	
    	String flowId = request.getParameter("flowId");
    	String instanceId = request.getParameter("instanceId");
    	String dependencyId = request.getParameter("dependencyId");
    	String type = request.getParameter("type");
       	
    	processFlows.deleteItem(flowId, instanceId, dependencyId, type);

    	String json = processFlows.getJSON();
    	
    	response.getWriter().print(json);
    }
}
