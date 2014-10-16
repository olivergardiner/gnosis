package uk.org.whitecottage.ea.gnosis.portlet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.ClientDataRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;

import uk.org.whitecottage.ea.portlet.PortletRequestContext;

public abstract class FileUploadPortlet extends Gnosis2Portlet {
	
    protected List<FileItem> getItems(ClientDataRequest request) {
		String gnosisDataDir = System.getProperty("jboss.server.data.dir") + File.separator + "gnosis2";
		String tempDir = gnosisDataDir + File.separator + "tmp";

		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Set factory constraints
		factory.setSizeThreshold(10000000);
		factory.setRepository(new File(tempDir));

		// Create a new file upload handler
		PortletFileUpload upload = new PortletFileUpload(factory);

		// Set overall request size constraint
		upload.setSizeMax(100000000);

		List<FileItem> items;
		// Parse the request
		try {
			items = upload.parseRequest(new PortletRequestContext(request));
		} catch (FileUploadException e) {
			items = new ArrayList<FileItem>();

			e.printStackTrace();
		}
		
		return items;
    }
}
