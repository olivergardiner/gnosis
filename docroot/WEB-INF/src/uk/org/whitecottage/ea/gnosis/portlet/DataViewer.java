package uk.org.whitecottage.ea.gnosis.portlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import javax.portlet.PortletException;
import javax.portlet.RenderMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.io.IOUtils;

import uk.org.whitecottage.ea.portlet.ProcessResourceRequest;

public class DataViewer extends GnosisPortlet {
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.gnosis.portlet");
	
    @RenderMode(name = "view")
    public void view(RenderRequest request, RenderResponse response) throws PortletException, IOException {
    	getPortletContext().getRequestDispatcher("/WEB-INF/jsp/DataViewer.jsp").include(request, response);
    }
    
    @ProcessResourceRequest(name = "jsonData")
    public void serveJSONData(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
		String gnosisDataDir = System.getProperty("jboss.server.data.dir") + File.separator + "gnosis";
		String gnosisCLDMDir = gnosisDataDir + File.separator + "cldm";
		String cldmJSONFile = gnosisCLDMDir + File.separator + "cldm.json";
		
		log.info("Serving json data from: " + cldmJSONFile);
	
		File file = new File(cldmJSONFile);
		try {
			IOUtils.copy(new FileInputStream(file), response.getPortletOutputStream());
		} catch (FileNotFoundException e) {
			// Fail silently
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @ProcessResourceRequest(name = "modelDetail")
    public void serveModelDetail(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
		String gnosisDataDir = System.getProperty("jboss.server.data.dir") + File.separator + "gnosis";
		String gnosisCLDMDir = gnosisDataDir + File.separator + "cldm";
		
		String path = request.getParameter("path");
		if (path.startsWith("LDM/")) {
			path = path.replaceFirst("LDM/", "");
		}
		
		log.info("Fetching: " + gnosisCLDMDir + File.separator + path);
		
		File file = new File(gnosisCLDMDir + File.separator + path);
		try {
			IOUtils.copy(new FileInputStream(file), response.getPortletOutputStream());
		} catch (FileNotFoundException e) {
			log.info(path + " not found");
			// Fail silently
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
