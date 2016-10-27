package uk.org.whitecottage.gnosis.backend.data.bson;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import uk.org.whitecottage.gnosis.backend.data.ApplicationBean;
import uk.org.whitecottage.gnosis.backend.data.ClassificationBean;
import uk.org.whitecottage.gnosis.backend.data.ClassificationMap;

@SuppressWarnings("serial")
public class ApplicationBeanBson extends ApplicationBean {
    
	public ApplicationBeanBson() {
		super();
	}

    @SuppressWarnings("unchecked")
	public ApplicationBeanBson(Document application, ClassificationMap classificationMap) {
    	init();
    	    	
    	if (application != null) {
	    	applicationName = application.getString("name");
	    	applicationDescription = application.getString("description");
	    	id = application.getString("app-id");
	    	
	    	Iterable<Document> classifications = (Iterable<Document>) application.get("logical-apps");
			if (classifications != null) {
				for (Object logicalApplication : classifications) {
					ClassificationBean classificationBean = new ClassificationBean((String) logicalApplication);
					classificationBean.setApplicationName(classificationMap.getLogicalApplicationName((String) logicalApplication));
					classification.add(classificationBean);
				}
			}
    	}
    }
    
    public static Document toBson(ApplicationBean application) {
    	Document applicationDocument = new Document();

    	applicationDocument.append("app-id", application.getId());
    	applicationDocument.append("name", application.getApplicationName());
    	applicationDocument.append("description", application.getApplicationDescription());
    	
    	List<String> logicalApps = new ArrayList<String>();
    	for (ClassificationBean classificationBean: application.getClassification()) {
    		logicalApps.add(classificationBean.getApplicationId());
    	}
    	applicationDocument.append("logical-apps", logicalApps);

    	return applicationDocument;
    }
}
