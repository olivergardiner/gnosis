package uk.org.whitecottage.gnosis.backend.impl.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import uk.org.whitecottage.gnosis.backend.GnosisDataService;
import uk.org.whitecottage.gnosis.backend.data.ApplicationBean;
import uk.org.whitecottage.gnosis.backend.data.ClassificationMap;
import uk.org.whitecottage.gnosis.backend.data.LogicalApplicationBean;
import uk.org.whitecottage.gnosis.backend.data.bson.ApplicationBeanBson;
import uk.org.whitecottage.gnosis.backend.data.bson.LogicalApplicationBeanBson;

@SuppressWarnings("serial")
public class GnosisDataServiceImpl extends GnosisDataService {

	@SuppressWarnings("unused")
	private final static Logger LOGGER = Logger.getLogger(GnosisDataServiceImpl.class.getName());
	private static GnosisDataServiceImpl INSTANCE;
	
	protected MongoClient mongoClient;

    private GnosisDataServiceImpl() {
    	mongoClient = new MongoClient();
    }

    public synchronized static GnosisDataService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GnosisDataServiceImpl();
        }
        return INSTANCE;
    }
    
    @Override
    public synchronized void init(Properties properties) {
    	super.init(properties);
    }

    @Override
    public synchronized List<ApplicationBean> getAllApplications() {
    	List<ApplicationBean> applications = new ArrayList<ApplicationBean>();
    	MongoDatabase db = mongoClient.getDatabase("gnosis");
    	FindIterable<Document> result = db.getCollection("applications").find();
    	
    	ClassificationMap classificationMap = new ClassificationMap(getAllLogicalApplications(true));

    	result.forEach(new Block<Document>() {
    	    @Override
    	    public void apply(final Document document) {
    	    	ApplicationBean application = new ApplicationBeanBson(document, classificationMap);
    	    	applications.add(application);   	    	
    	    }
    	});
    	
        return applications;
    }

    @Override
    public synchronized void updateApplication(ApplicationBean application) {
    	MongoDatabase db = mongoClient.getDatabase("gnosis");
    	
    	boolean update = true;
    	String appId = application.getId();
    	if (appId == null || appId.equals("-1")) {
    		application.setId(UUID.randomUUID().toString());
    		update = false;
    	}
    	
    	Document document = ApplicationBeanBson.toBson(application);
    	
    	if (update) {   	
    		db.getCollection("applications").replaceOne(new Document("app-id", application.getId()), document);
    	} else {
    		db.getCollection("applications").insertOne(document);
    	}
    }

    @Override
    public synchronized ApplicationBean getApplicationById(String applicationId) {

    	ClassificationMap classificationMap = new ClassificationMap(getAllLogicalApplications(true));

    	MongoDatabase db = mongoClient.getDatabase("gnosis");
    	FindIterable<Document> result = db.getCollection("applications").find(
    			new Document("app-id", applicationId));
        return new ApplicationBeanBson(result.first(), classificationMap);
    }

    @Override
    public synchronized void deleteApplication(String applicationId) {
    	MongoDatabase db = mongoClient.getDatabase("gnosis");
    	db.getCollection("applications").deleteOne(new Document("app-id", applicationId));
/*        ApplicationBean application = getApplicationById(applicationId);
        if (application == null) {
            throw new IllegalArgumentException("Product with id " + applicationId
                    + " not found");
        }
        applications.remove(application);
*/    }

	@Override
	@SuppressWarnings("unchecked")
	public Collection<LogicalApplicationBean> getAllLogicalApplications(boolean asEcosystems) {
    	List<LogicalApplicationBean> logicalApplications = new ArrayList<LogicalApplicationBean>();
    	
    	MongoDatabase db = mongoClient.getDatabase("gnosis");
    	//FindIterable<Document> result = db.getCollection("value-chain").find().projection(fields(include("primary-activities.ecosystems.logical-apps")));
    	FindIterable<Document> result = db.getCollection("value-chain").find();
    	Document valueChain = result.first();

    	Iterable<Document> primaryActivities = (Iterable<Document>) valueChain.get("primary-activities");
    	for (Document primaryActivity: primaryActivities) {
			Iterable<Document> ecosystemsList = (Iterable<Document>) primaryActivity.get("ecosystems");	    	
	    	addEcosystems(logicalApplications, ecosystemsList);
    	}
  	
    	Iterable<Document> supportActivities = (Iterable<Document>) valueChain.get("support-activities");
    	for (Document supportActivity: supportActivities) {
			Iterable<Document> ecosystemsList = (Iterable<Document>) supportActivity.get("ecosystems");	    	
	    	addEcosystems(logicalApplications, ecosystemsList);
    	}
  	  	
		return logicalApplications;
	}
	
	@SuppressWarnings("unchecked")
	protected void addEcosystems(List<LogicalApplicationBean> logicalApplications, Iterable<Document> ecosystemsList) {

    	if (ecosystemsList != null) {
    		for (Document ecosystem: ecosystemsList) {
	    		
		    	Iterable<Document> logicalApps = (Iterable<Document>) ecosystem.get("logical-apps");
				if (logicalApps != null) {
					for (Document logicalApp : logicalApps) {
						logicalApplications.add(new LogicalApplicationBeanBson(logicalApp));
					}
				}
	    	}
    	}
	}
}
