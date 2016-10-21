package uk.org.whitecottage.gnosis.backend.data.jaxb;

import uk.org.whitecottage.gnosis.backend.data.ApplicationBean;
import uk.org.whitecottage.gnosis.jaxb.applications.Application;
import uk.org.whitecottage.gnosis.jaxb.applications.Capability;
import uk.org.whitecottage.gnosis.jaxb.applications.Ecosystem;

@SuppressWarnings("serial")
public class ApplicationBeanJaxb extends ApplicationBean {
    
    public ApplicationBeanJaxb(Application application) {
    	init();
    	
    	if (application != null) {
	    	applicationName = application.getName();
	    	applicationDescription = application.getDescription();
	    	id = application.getAppId();
	    	
	    	for (Ecosystem ecosystem: application.getEcosystem()) {
	    		for (Capability logicalApplication: ecosystem.getCapability()) {
	    			classification.add(new ClassificationBeanJaxb(ecosystem.getEcosystem(), logicalApplication.getCapability()));
	    		}
	    	}
    	}
    } 
}
