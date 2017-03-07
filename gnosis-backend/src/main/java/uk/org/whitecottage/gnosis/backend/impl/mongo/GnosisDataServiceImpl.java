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
import com.vaadin.data.Item;

import uk.org.whitecottage.gnosis.backend.GnosisDataService;
import uk.org.whitecottage.gnosis.backend.data.ApplicationBean;
import uk.org.whitecottage.gnosis.backend.data.ClassificationMap;
import uk.org.whitecottage.gnosis.backend.data.FrameworkContainer;
import uk.org.whitecottage.gnosis.backend.data.LogicalApplicationBean;
import uk.org.whitecottage.gnosis.backend.data.ProcessTaxonomyContainer;
import uk.org.whitecottage.gnosis.backend.data.bson.ApplicationBeanBson;
import uk.org.whitecottage.gnosis.backend.data.bson.LogicalApplicationBeanBson;
import uk.org.whitecottage.gnosis.backend.data.bson.ProcessTaxonomyContainerBson;

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

	@SuppressWarnings("unchecked")
	@Override
	public FrameworkContainer getFramework() {
    	MongoDatabase db = mongoClient.getDatabase("gnosis");
    	
    	FrameworkContainer framework = new FrameworkContainer();
    	
		framework.addContainerProperty("Name", String.class, null);
		framework.addContainerProperty("Description", String.class, null);
		/*framework.addContainerProperty("id", String.class, null);*/
		framework.addContainerProperty("appId", String.class, null);
		
		Item primary = framework.addItem("primary");
		primary.getItemProperty("Name").setValue("Primary activities");

		Item support = framework.addItem("support");
		support.getItemProperty("Name").setValue("Support activities");

    	FindIterable<Document> result = db.getCollection("value-chain").find();
    	Document valueChain = result.first();

    	Iterable<Document> primaryActivities = (Iterable<Document>) valueChain.get("primary-activities");
    	for (Document primaryActivity: primaryActivities) {
    		Object activityId = primaryActivity.get("activity-id");
    		Item activity = framework.addItem(activityId);
    		activity.getItemProperty("Name").setValue(primaryActivity.get("name"));
    		activity.getItemProperty("Description").setValue(primaryActivity.get("description"));
    		framework.setParent(activityId, "primary");
			Iterable<Document> ecosystemsList = (Iterable<Document>) primaryActivity.get("ecosystems");	    	
	    	addEcosystems(framework, ecosystemsList, activityId);
    	}

    	Iterable<Document> supportActivities = (Iterable<Document>) valueChain.get("support-activities");
    	for (Document supportActivity: supportActivities) {
    		Object activityId = supportActivity.get("activity-id");
    		Item activity = framework.addItem(activityId);
    		activity.getItemProperty("Name").setValue(supportActivity.get("name"));
    		activity.getItemProperty("Description").setValue(supportActivity.get("description"));
    		framework.setParent(activityId, "support");
			Iterable<Document> ecosystemsList = (Iterable<Document>) supportActivity.get("ecosystems");	    	
	    	addEcosystems(framework, ecosystemsList, activityId);
    	}

		return framework;
	}
	
	@SuppressWarnings("unchecked")
	protected void addEcosystems(FrameworkContainer framework, Iterable<Document> ecosystemsList, Object parent) {
		for (Document ecosystemDocument: ecosystemsList) {
    		Object ecosystemId = ecosystemDocument.get("ecosystem-id");
    		Item ecosystem = framework.addItem(ecosystemId);
    		ecosystem.getItemProperty("Name").setValue(ecosystemDocument.get("name"));
    		ecosystem.getItemProperty("Description").setValue(ecosystemDocument.get("description"));
    		framework.setParent(ecosystemId, parent);

    		Iterable<Document> logicalAppsList = (Iterable<Document>) ecosystemDocument.get("logical-apps");
			for (Document logicalAppDocument: logicalAppsList) {
	    		Object logicalAppId = logicalAppDocument.get("instance-id");
	    		Item logicalApp = framework.addItem(logicalAppId);
	    		logicalApp.getItemProperty("Name").setValue(logicalAppDocument.get("name"));
	    		logicalApp.getItemProperty("Description").setValue(logicalAppDocument.get("description"));
	    		logicalApp.getItemProperty("appId").setValue(logicalAppDocument.get("logical-app"));
	    		framework.setParent(logicalAppId, ecosystemId);
			}
		}
	}

	@Override
	public ProcessTaxonomyContainer getProcessTaxonomy() {
    	MongoDatabase db = mongoClient.getDatabase("gnosis");
    	
    	FindIterable<Document> result = db.getCollection("processes").find();
    	
    	ProcessTaxonomyContainer processTaxonomy = new ProcessTaxonomyContainerBson(result);
    	
    	return processTaxonomy;
	}
}
