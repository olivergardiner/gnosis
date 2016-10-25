package uk.org.whitecottage.gnosis.backend.data.bson;

import org.bson.Document;

import uk.org.whitecottage.gnosis.backend.data.LogicalApplicationBean;

@SuppressWarnings("serial")
public class LogicalApplicationBeanBson extends LogicalApplicationBean {

	public LogicalApplicationBeanBson() {
		super();
	}

	public LogicalApplicationBeanBson(Document logicalApp) {
		init();
		
		if (logicalApp != null) {
	    	applicationName = logicalApp.getString("name");
	    	applicationDescription = logicalApp.getString("description");
	    	applicationId = logicalApp.getString("instance-id");
		}
	}

}
