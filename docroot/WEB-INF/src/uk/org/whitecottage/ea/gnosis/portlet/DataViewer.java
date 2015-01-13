package uk.org.whitecottage.ea.gnosis.portlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.io.IOUtils;

import uk.org.whitecottage.ea.portlet.ProcessResourceRequest;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class DataViewer extends GnosisPortlet {
	private static final Log log = LogFactoryUtil.getLog(DataViewer.class);
	
    @RenderMode(name = "view")
    public void view(RenderRequest request, RenderResponse response) throws PortletException, IOException {
    	getPortletContext().getRequestDispatcher("/WEB-INF/jsp/DataViewer.jsp").include(request, response);
    }
    
    @ProcessResourceRequest(name = "jsonData")
    public void serveJSONData(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
		String cldmJSONFile = dataDir + "gnosis/cldm/cldm.json";
		
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
		String gnosisCLDMDir = dataDir + "cldm";
		
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
