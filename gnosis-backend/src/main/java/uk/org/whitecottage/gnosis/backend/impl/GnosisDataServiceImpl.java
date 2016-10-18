package uk.org.whitecottage.gnosis.backend.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import uk.org.whitecottage.gnosis.backend.GnosisDataService;
import uk.org.whitecottage.gnosis.backend.data.ApplicationBean;
import uk.org.whitecottage.gnosis.backend.data.LogicalApplicationBean;
import uk.org.whitecottage.gnosis.backend.impl.xmldb.XmldbPersistenceManager;
import uk.org.whitecottage.gnosis.jaxb.applications.Application;
import uk.org.whitecottage.gnosis.jaxb.framework.Activity;
import uk.org.whitecottage.gnosis.jaxb.framework.CapabilityInstance;
import uk.org.whitecottage.gnosis.jaxb.framework.Ecosystem;
import uk.org.whitecottage.gnosis.jaxb.framework.Framework;
import uk.org.whitecottage.gnosis.jaxb.framework.ValueChain;

/**
 * Application data model. This implementation has very simplistic locking and does not
 * notify users of modifications.
 */
@SuppressWarnings("serial")
public class GnosisDataServiceImpl extends GnosisDataService {

	@SuppressWarnings("unused")
	private final static Logger LOGGER = Logger.getLogger(GnosisDataServiceImpl.class.getName());
	private static GnosisDataServiceImpl INSTANCE;

    protected XmldbPersistenceManager persistenceManager;

    private GnosisDataServiceImpl() {
    	
    	persistenceManager = new XmldbPersistenceManager("xmldb:exist://localhost:8080/exist/xmlrpc", "/db/gnosis");
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
    	persistenceManager.setCredentials(properties.getProperty("exist.username"), properties.getProperty("exist.password"));
    	persistenceManager.setURI(properties.getProperty("exist.uri"));
    	persistenceManager.setRepositoryRoot(properties.getProperty("exist.repository.root"));
    }

    @Override
    public synchronized List<ApplicationBean> getAllApplications() {
    	List<ApplicationBean> applications = new ArrayList<ApplicationBean>();
    	for (Application application: persistenceManager.getApplications()) {
    		applications.add(new ApplicationBean(application));
    	}
    	
        return applications;
    }

    @Override
    public synchronized void updateApplication(ApplicationBean application) {
/*        if (application.getId() < 0) {
            // New Application
            application.setId(nextApplicationId++);
            applications.add(application);
            return;
        }
        for (int i = 0; i < applications.size(); i++) {
            if (applications.get(i).getId() == application.getId()) {
                applications.set(i, application);
                return;
            }
        }
        throw new IllegalArgumentException("No product with id " + application.getId()
                + " found");
*/
    }

    @Override
    public synchronized ApplicationBean getApplicationById(String applicationId) {

        return new ApplicationBean(persistenceManager.getApplication(applicationId));
    }

    @Override
    public synchronized void deleteApplication(String applicationId) {
/*        ApplicationBean application = getApplicationById(applicationId);
        if (application == null) {
            throw new IllegalArgumentException("Product with id " + applicationId
                    + " not found");
        }
        applications.remove(application);
*/    }

	@Override
	public Collection<LogicalApplicationBean> getAllLogicalApplications(boolean asEcosystems) {
    	List<LogicalApplicationBean> logicalApplications = new ArrayList<LogicalApplicationBean>();
    	Framework framework = persistenceManager.getFramework();
    	ValueChain valueChain = framework.getValueChain();
    	addEcosystems(logicalApplications, valueChain.getPrimaryActivities().getActivity());
    	addEcosystems(logicalApplications, valueChain.getSupportActivities().getActivity());
    	
		return logicalApplications;
	}
	
	protected void addEcosystems(List<LogicalApplicationBean> logicalApplications, List<Activity> activities) {
		for (Activity activity: activities) {
			for (Ecosystem ecosystem: activity.getEcosystem()) {
				for (CapabilityInstance capabilityInstance: ecosystem.getCapabilityInstance()) {
					LogicalApplicationBean logicalApplication = new LogicalApplicationBean();

					logicalApplication.setApplicationId(ecosystem.getEcosystemId() + "/" + capabilityInstance.getCapabilityId());
					logicalApplication.setApplicationName(capabilityInstance.getName());
					logicalApplication.setApplicationDescription(capabilityInstance.getDescription());
					
					logicalApplications.add(logicalApplication);
				}
			}
		}
	}
}
