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

import uk.org.whitecottage.ea.gnosis.repository.Framework;
import uk.org.whitecottage.ea.gnosis.repository.framework.FrameworkPresentation;
import uk.org.whitecottage.ea.portlet.ProcessResourceRequest;

public class FrameworkViewer extends Gnosis2Portlet {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.gnosis.portlet");
	
    @RenderMode(name = "view")
    public void view(RenderRequest request, RenderResponse response) throws PortletException, IOException {
    	getPortletContext().getRequestDispatcher("/WEB-INF/jsp/FrameworkViewer.jsp").include(request, response);
    }
    
    @ProcessResourceRequest(name = "jsonData")
    public void serveJSON(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
        
    	String context = getPortletContext().getRealPath("");
    	Framework framework = new Framework(existURI, existRepositoryRoot, context);
    	String json = framework.getJSON();
    	
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
    	Properties gnosis2Properties = getProperties();
    	String existURI = gnosis2Properties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosis2Properties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");

    	String gnosisDataDir = System.getProperty("jboss.server.data.dir") + File.separator + "gnosis2";
		String gnosisOoxmlDir = gnosisDataDir + File.separator + "ooxml" + File.separator;
        
		XMLSlideShow ppt;
    	File template = new File(gnosisOoxmlDir + "framework-tmpl.pptx");
    	if (template.exists()) {
    		ppt = new XMLSlideShow(new FileInputStream(template));
    	} else {
    		ppt = new XMLSlideShow();
    	}
    	
    	// Build the slides
    	FrameworkPresentation arb = new FrameworkPresentation(existURI, existRepositoryRoot, context);
    	arb.render(ppt);
    	
    	response.setContentType("application/vnd.openxmlformats-officedocument.presentationml.presentation");
    	//response.setHeader("Content-Disposition", "attachment;filename=\"Lifecycle.pptx\"");
    	ppt.write(response.getPortletOutputStream());
     }
}
