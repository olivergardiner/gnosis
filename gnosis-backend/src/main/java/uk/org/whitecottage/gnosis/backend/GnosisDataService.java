package uk.org.whitecottage.gnosis.backend;

import java.io.Serializable;
import java.util.Collection;
import java.util.Properties;

import uk.org.whitecottage.gnosis.backend.data.ApplicationBean;
import uk.org.whitecottage.gnosis.backend.data.FrameworkContainer;
import uk.org.whitecottage.gnosis.backend.data.LogicalApplicationBean;
import uk.org.whitecottage.gnosis.backend.impl.mongo.GnosisDataServiceImpl;

/**
 * Back-end service interface for retrieving and updating product data.
 */
@SuppressWarnings("serial")
public abstract class GnosisDataService implements Serializable {
	protected Properties properties;

    public abstract Collection<ApplicationBean> getAllApplications();

    public abstract void updateApplication(ApplicationBean application);

    public abstract void deleteApplication(String applicationId);

    public abstract ApplicationBean getApplicationById(String applicationId);
    
    public abstract Collection<LogicalApplicationBean> getAllLogicalApplications(boolean asEcosystems);

    public abstract FrameworkContainer getFramework();

    public synchronized void init(Properties properties) {
    	this.properties = properties;
    }

    public static GnosisDataService get() {
        return GnosisDataServiceImpl.getInstance();
    }

}
