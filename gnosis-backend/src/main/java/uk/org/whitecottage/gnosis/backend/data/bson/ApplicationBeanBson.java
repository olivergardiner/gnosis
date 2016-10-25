package uk.org.whitecottage.gnosis.backend.data.bson;

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
	    	
	    	Iterable<Document> ecosystemsList = (Iterable<Document>) application.get("ecosystems");	    	
	    	if (ecosystemsList != null) {
	    		for (Document ecosystem: ecosystemsList) {
		    		String ecosystemId = ecosystem.getString("ecosystem");
		    		
			    	Iterable<Document> classifications = (Iterable<Document>) ecosystem.get("logical-apps");
					if (classifications != null) {
						for (Object logicalApplication : classifications) {
							ClassificationBean classificationBean = new ClassificationBean(ecosystemId, (String) logicalApplication);
							classificationBean.setApplicationName(classificationMap.getLogicalApplicationName((String) logicalApplication));
							System.out.println("id: " + classificationBean.getApplicationId());
							System.out.println("Name: " + classificationBean.getApplicationName());
							classification.add(classificationBean);
						}
					}
		    	}
	    	}
    	}
    } 
}
