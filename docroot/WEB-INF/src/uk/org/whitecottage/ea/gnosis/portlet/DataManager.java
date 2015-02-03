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

public class DataManager extends FileUploadPortlet {
	public static final int UPLOAD_SUCCESS = 0;
	public static final int UPLOAD_XMLDB_ERROR = 1;
	public static final int UPLOAD_UNKNOWN_ERROR = 2;

	private static final Log log = LogFactoryUtil.getLog(DataManager.class);
	private URI modelUri = null;

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
    	getPortletContext().getRequestDispatcher("/WEB-INF/jsp/DataManager.jsp").include(request, response);
    }
    
    @ProcessResourceAction(name = "uploadModelAction")
    public void uploadModel(ResourceRequest request, ResourceResponse response) {
    	log.info("Upload model");

		JSONResponse jResponse = new JSONResponse();
		jResponse.setParseStatus("Success");
		jResponse.setReturnCode(UPLOAD_SUCCESS);

		List<FileItem> items = getItems(request);
		String cldmFile = dataDir + "gnosis/uml/cldm.uml";

		// Process the uploaded items
		if (items.size() == 1) {
			FileItem item = items.get(0);
			
			if (!item.isFormField()) {
				File newCldm = new File(cldmFile);
				try {
					item.write(newCldm);
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
		
		parseModel();

		//response.setContentType("application/json");
		/*try {
			response.getWriter().write(new Gson().toJson(jResponse));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

    }
    
    protected void parseModel() {
		String cldmFile = dataDir + "gnosis/uml/cldm.uml";

		String gnosisCLDMDir = dataDir + "gnosis/cldm";
		String cldmJSONFile = dataDir + "gnosis/cldm/cldm.json";
	
		File input = new File(cldmFile);
		File templateDir = new File(getPortletContext().getRealPath("/WEB-INF/ftl"));
		File outputDir = new File(gnosisCLDMDir);
		
		cleanOutputDirectory();
		
		log.info(getPortletContext().getRealPath("/WEB-INF/ftl"));

		Configuration cfg = new Configuration();

		try {
			// Specify the data source where the template files come from
			cfg.setDirectoryForTemplateLoading(templateDir);

			// Specify how templates will see the data-model
			cfg.setObjectWrapper(new DefaultObjectWrapper());

			// Set your preferred charset template files are stored in
			cfg.setDefaultEncoding("UTF-8");

			// Sets how errors will appear. Here we assume we are developing HTML pages.
			// For production systems TemplateExceptionHandler.RETHROW_HANDLER is better.
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);

			// At least in new projects, specify that you want the fixes that aren't
			// 100% backward compatible too (these are very low-risk changes as far as the
			// 1st and 2nd version number remains):
			cfg.setIncompatibleImprovements(new Version(2, 3, 20));

			Model root = getModel(input.getAbsolutePath());
			if (root != null) {
				log.info("Initialising the CLDMProcessor");
				CLDMResourceRenderer cldm = new CLDMResourceRenderer(root);
				cldm.generateCLDMResources(cfg, outputDir);
				
				CLDMJSONRenderer cldmJSON = new CLDMJSONRenderer(root);
				PrintWriter json = new PrintWriter(new File(cldmJSONFile));
				json.print(cldmJSON.generateJSON());
				json.close();
			} else {
				log.info("Could not load the model");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    protected Model getModel(String pathToModel) {
		
		modelUri = URI.createFileURI(pathToModel);
		ResourceSet set = new ResourceSetImpl();
		set.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);		
		set.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);

		set.createResource(modelUri);
		log.info("Fetching the resource: " + modelUri.path());
		Resource r = set.getResource(modelUri, true);
		
		Model model = (Model) EcoreUtil.getObjectByType(r.getContents(), UMLPackage.Literals.MODEL);
		if (model == null) {
			log.info("Could not load model");
		}
        return model;
	}
    
    protected void cleanOutputDirectory() {
		String gnosisCLDMDir = dataDir + "gnosis/cldm";
	
		File sourceDir = new File(getPortletContext().getRealPath("/WEB-INF/init/data/gnosis/cldm"));
		File outputDir = new File(gnosisCLDMDir);
		
		log.info("source: " + sourceDir.getPath() + " " + sourceDir.exists());
		log.info("target: " + outputDir.getPath() + " " + outputDir.exists());
		
		try {
			FileUtils.deleteDirectory(outputDir);
			FileUtils.copyDirectory(sourceDir, outputDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
