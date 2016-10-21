package uk.org.whitecottage.gnosis.backend.data.bson;

import java.util.List;

import org.bson.Document;

import uk.org.whitecottage.gnosis.backend.data.ApplicationBean;

@SuppressWarnings("serial")
public class ApplicationBeanBson extends ApplicationBean {
    
    @SuppressWarnings("unchecked")
	public ApplicationBeanBson(Document application) {
    	init();
    	
    	/*
    	BsonDocument applicationBson = application.toBsonDocument(BsonDocument.class, MongoClient.getDefaultCodecRegistry());
    	*/   	
    	
    	if (application != null) {
	    	applicationName = application.getString("name");
	    	applicationDescription = application.getString("description");
	    	id = application.getString("app-id");
	    	
	    	Iterable<Document> ecosystemsList = (Iterable<Document>) application.get("ecosystems");	    	
	    	if (ecosystemsList != null) {
	    		for (Document ecosystem: ecosystemsList) {
		    		String ecosystemId = ecosystem.getString("ecosystem");
		    		
			    	Iterable<Document> classifications = (Iterable<Document>) application.get("capabilities");
					if (classifications != null) {
						for (Object logicalApplication : ecosystem.get("capabilities", List.class)) {
							classification.add(new ClassificationBeanBson(ecosystemId, (String) logicalApplication));
						}
					}
		    	}
	    	}
    	}
    } 
}
