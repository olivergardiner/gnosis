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

import uk.org.whitecottage.ea.gnosis.repository.ITServices;
import uk.org.whitecottage.ea.portlet.ProcessResourceAction;
import uk.org.whitecottage.ea.portlet.ProcessResourceRequest;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public class ITServicesViewer extends GnosisPortlet {
	@SuppressWarnings("unused")
	private static final Log log = LogFactoryUtil.getLog(ITServicesViewer.class);
	
    @RenderMode(name = "view")
    public void view(RenderRequest request, RenderResponse response) throws PortletException, IOException {
    	getPortletContext().getRequestDispatcher("/WEB-INF/jsp/ITServicesViewer.jsp").include(request, response);
    }
    
    @ProcessResourceRequest(name = "jsonData")
    public void serveJSON(ResourceRequest request, ResourceResponse response) throws PortletException, java.io.IOException {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
        
    	String context = getPortletContext().getRealPath("");
    	ITServices services = new ITServices(existURI, existRepositoryRoot, context);
    	String json = services.getJSON();
    	
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

    @ProcessResourceAction(name = "updateComponentAction")
    public void updateComponent(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ITServices services = new ITServices(existURI, existRepositoryRoot, context);
    	
    	String id = request.getParameter("id");
    	String name = request.getParameter("name");
    	String description = request.getParameter("description");
       	
    	services.updateComponent(id, name, description);
    }

    @ProcessResourceAction(name = "emptyTrashAction")
    public void emptyRecycleBin(ResourceRequest request, ResourceResponse response) {
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");
    	ITServices services = new ITServices(existURI, existRepositoryRoot, context);
    	
    	services.emptyRecycleBin();
    }
}
