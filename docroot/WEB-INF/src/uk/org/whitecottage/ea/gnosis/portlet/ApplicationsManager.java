package uk.org.whitecottage.ea.gnosis.portlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

import uk.org.whitecottage.ea.gnosis.cldm.CLDMJSONRenderer;
import uk.org.whitecottage.ea.gnosis.cldm.CLDMResourceRenderer;
import uk.org.whitecottage.ea.portlet.ProcessResourceAction;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class ApplicationsManager extends FileUploadPortlet {
	public static final int UPLOAD_SUCCESS = 0;
	public static final int UPLOAD_XMLDB_ERROR = 1;
	public static final int UPLOAD_UNKNOWN_ERROR = 2;

	private static final Log log = LogFactoryUtil.getLog(ApplicationsManager.class);

	protected class JSONResponse {
		protected String parseStatus;
		protected int returnCode;

		public String getParseStatus() {
			return parseStatus;
		}

		public void setParseStatus(String parseStatus) {
			this.parseStatus = parseStatus;
		}

		public int getReturnCode() {
			return returnCode;
		}

		public void setReturnCode(int returnCode) {
			this.returnCode = returnCode;
		}
	}
	
    @RenderMode(name = "view")
    public void view(RenderRequest request, RenderResponse response) throws PortletException, IOException {
    	getPortletContext().getRequestDispatcher("/WEB-INF/jsp/ApplicationsManager.jsp").include(request, response);
    }
    
    @ProcessResourceAction(name = "uploadApplicationsAction")
    public void uploadApplications(ResourceRequest request, ResourceResponse response) {
    	log.info("Upload applications spreadsheet");

		JSONResponse jResponse = new JSONResponse();
		jResponse.setParseStatus("Success");
		jResponse.setReturnCode(UPLOAD_SUCCESS);

		List<FileItem> items = getItems(request);
		//String cldmFile = dataDir + "gnosis/uml/cldm.uml";

		// Process the uploaded items
		if (items.size() == 1) {
			FileItem item = items.get(0);
			
			if (!item.isFormField()) {
				//File newCldm = new File(cldmFile);
				try {
					//item.write(newCldm);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// ...there has been a mistake
			}
		} else {
			// ...there has been a mistake 
		}
		
		//parseModel();

		//response.setContentType("application/json");
		/*try {
			response.getWriter().write(new Gson().toJson(jResponse));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

    }
}
