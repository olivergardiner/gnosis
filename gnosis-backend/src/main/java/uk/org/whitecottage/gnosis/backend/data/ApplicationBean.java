package uk.org.whitecottage.gnosis.backend.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.vaadin.data.fieldgroup.PropertyId;

import uk.org.whitecottage.gnosis.jaxb.applications.Application;
import uk.org.whitecottage.gnosis.jaxb.applications.Capability;
import uk.org.whitecottage.gnosis.jaxb.applications.Ecosystem;

@SuppressWarnings("serial")
public class ApplicationBean implements Serializable {

    @NotNull
    @PropertyId("id")
    private String id = "-1";
    @NotNull
    @Size(min = 1, message = "Application name cannot be blank")
    @PropertyId("applicationName")
    private String applicationName = "";
    @PropertyId("applicationDescription")
    private String applicationDescription = "";
    @PropertyId("classification")
    private Collection<ClassificationBean> classification;
    
    public ApplicationBean() {
    	init();
    }

    public ApplicationBean(Application application) {
    	init();
    	
    	if (application != null) {
	    	applicationName = application.getName();
	    	applicationDescription = application.getDescription();
	    	id = application.getAppId();
	    	
	    	for (Ecosystem ecosystem: application.getEcosystem()) {
	    		for (Capability logicalApplication: ecosystem.getCapability()) {
	    			classification.add(new ClassificationBean(ecosystem.getEcosystem(), logicalApplication.getCapability()));
	    		}
	    	}
    	}
    }
    
    protected void init() {
    	classification = new ArrayList<ClassificationBean>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationDescription() {
        return applicationDescription;
    }

    public void setApplicationDescription(String applicationDescription) {
        this.applicationDescription = applicationDescription;
    }

	public Collection<ClassificationBean> getClassification() {
		return classification;
	}

	public void setClassification(Collection<ClassificationBean> classification) {
		this.classification = classification;
	}
}
