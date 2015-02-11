package uk.org.whitecottage.ea.gnosis.portlet;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.portlet.PortletException;
import javax.portlet.RenderMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import uk.org.whitecottage.ea.gnosis.repository.applications.ApplicationsSpreadsheet;
import uk.org.whitecottage.ea.portlet.ProcessResourceAction;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

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
    	Properties gnosisProperties = getProperties();
    	String existURI = gnosisProperties.getProperty("exist.uri");
    	String existRepositoryRoot = gnosisProperties.getProperty("exist.repository.root");
    	String context = getPortletContext().getRealPath("");

    	log.info("Upload applications spreadsheet");

		JSONResponse jResponse = new JSONResponse();
		jResponse.setParseStatus("Success");
		jResponse.setReturnCode(UPLOAD_SUCCESS);

		List<FileItem> items = getItems(request);

		// Process the uploaded items
		if (items.size() == 1) {
			FileItem item = items.get(0);
			
			if (!item.isFormField()) {
				try {
					XSSFWorkbook xlsx = new XSSFWorkbook(item.getInputStream());
					log.info("Got the workbook: " + xlsx.getNumberOfSheets());
			    	ApplicationsSpreadsheet ass = new ApplicationsSpreadsheet(existURI, existRepositoryRoot, context);
					log.info("Invoking the update");
			    	ass.update(xlsx);
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
