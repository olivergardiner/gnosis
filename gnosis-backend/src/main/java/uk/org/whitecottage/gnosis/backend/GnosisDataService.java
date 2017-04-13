package uk.org.whitecottage.gnosis.backend;

import java.io.Serializable;
import java.util.Collection;
import java.util.Properties;

import uk.org.whitecottage.gnosis.backend.data.ApplicationBean;
import uk.org.whitecottage.gnosis.backend.data.ApplicationTaxonomyContainer;
import uk.org.whitecottage.gnosis.backend.data.ProcessTaxonomyContainer;
import uk.org.whitecottage.gnosis.backend.impl.GnosisDataServiceImpl;

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
    
    public abstract ApplicationTaxonomyContainer getApplicationTaxonomy();

    public abstract void updateApplicationTaxonomy(ApplicationTaxonomyContainer applicationTaxonomy);

    public abstract ProcessTaxonomyContainer getProcessTaxonomy();

	public abstract void updateProcessTaxonomy(ProcessTaxonomyContainer processTaxonomy);

	public synchronized void init(Properties properties) {
    	this.properties = properties;
    }

    public static GnosisDataService get() {
        return GnosisDataServiceImpl.getInstance();
    }
}
