package uk.org.whitecottage.gnosis.backend.impl;

import static com.mongodb.client.model.Filters.where;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import uk.org.whitecottage.gnosis.backend.GnosisDataService;
import uk.org.whitecottage.gnosis.backend.data.ApplicationBean;
import uk.org.whitecottage.gnosis.backend.data.ApplicationTaxonomyContainer;
import uk.org.whitecottage.gnosis.backend.data.ClassificationMap;
import uk.org.whitecottage.gnosis.backend.data.ProcessTaxonomyContainer;

@SuppressWarnings("serial")
public class GnosisDataServiceImpl extends GnosisDataService {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(GnosisDataServiceImpl.class.getName());
	private static GnosisDataServiceImpl instance;
	
	protected transient MongoClient mongoClient;

	private static final String DB = "gnosis";
	private static final String APPLICATIONS = "applications";
	private static final String APPLICATION_TAXONOMY = "application-taxonomy";
	private static final String PROCESS_TAXONOMY = "process-taxonomy";
	
    private GnosisDataServiceImpl() {
    	mongoClient = new MongoClient();
    }

    public static synchronized GnosisDataService getInstance() {
        if (instance == null) {
            instance = new GnosisDataServiceImpl();
        }
        return instance;
    }
    
    @Override
    public synchronized void init(Properties properties) {
    	super.init(properties);
    }

    @Override
    public synchronized List<ApplicationBean> getAllApplications() {
    	List<ApplicationBean> applications = new ArrayList<>();
    	MongoDatabase db = mongoClient.getDatabase(DB);
    	FindIterable<Document> result = db.getCollection(APPLICATIONS).find();
    	
    	ClassificationMap classificationMap = new ClassificationMap(getApplicationTaxonomy());

    	result.forEach((final Document document) -> {
    	    	ApplicationBean application = new ApplicationBean(document, classificationMap);
    	    	applications.add(application);   	    	
    	    });
    	
        return applications;
    }

    @Override
    public synchronized void updateApplication(ApplicationBean application) {
    	MongoDatabase db = mongoClient.getDatabase(DB);
    	
    	boolean update = true;
    	String appId = application.getId();
    	if ("-1".equals(appId)) {
    		application.setId(UUID.randomUUID().toString());
    		update = false;
    	}
    	
    	Document document = ApplicationBean.toBson(application);
    	
    	if (update) {   	
    		db.getCollection(APPLICATIONS).replaceOne(new Document("app-id", application.getId()), document);
    	} else {
    		db.getCollection(APPLICATIONS).insertOne(document);
    	}
    }

    @Override
    public synchronized ApplicationBean getApplicationById(String applicationId) {

    	ClassificationMap classificationMap = new ClassificationMap(getApplicationTaxonomy());

    	MongoDatabase db = mongoClient.getDatabase(DB);
    	FindIterable<Document> result = db.getCollection(APPLICATIONS).find(
    			new Document("app-id", applicationId));
    	
        return new ApplicationBean(result.first(), classificationMap);
    }

    @Override
    public synchronized void deleteApplication(String applicationId) {
    	MongoDatabase db = mongoClient.getDatabase(DB);
    	db.getCollection(APPLICATIONS).deleteOne(new Document("app-id", applicationId));
    }

	@Override
	public synchronized ProcessTaxonomyContainer getProcessTaxonomy() {
    	MongoDatabase db = mongoClient.getDatabase(DB);
    	
    	FindIterable<Document> result = db.getCollection(PROCESS_TAXONOMY).find();
    	
    	return new ProcessTaxonomyContainer(result);
	}
	
	@Override
	public synchronized void updateProcessTaxonomy(ProcessTaxonomyContainer processTaxonomy) {
    	MongoDatabase db = mongoClient.getDatabase(DB);
    	
    	MongoCollection<Document> processes = db.getCollection(PROCESS_TAXONOMY);
    	processes.deleteMany(where("true"));
    	
    	processes.insertMany(processTaxonomy.toBson());
	}

	@Override
	public synchronized ApplicationTaxonomyContainer getApplicationTaxonomy() {
    	MongoDatabase db = mongoClient.getDatabase(DB);
    	
    	FindIterable<Document> result = db.getCollection(APPLICATION_TAXONOMY).find();
    	
    	return new ApplicationTaxonomyContainer(result);
	}

	@Override
	public synchronized void updateApplicationTaxonomy(ApplicationTaxonomyContainer applicationTaxonomy) {
    	MongoDatabase db = mongoClient.getDatabase(DB);
    	
    	MongoCollection<Document> processes = db.getCollection(APPLICATION_TAXONOMY);
    	processes.deleteMany(where("true"));
    	
    	processes.insertMany(applicationTaxonomy.toBson());
	}
}
