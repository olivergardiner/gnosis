package uk.org.whitecottage.gnosis.backend.data;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.vaadin.data.fieldgroup.PropertyId;

import uk.org.whitecottage.gnosis.jaxb.applications.Application;

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
    //@PropertyId("classification")
    //private Collection<String> classification;
    
    private Application application;
    
    public ApplicationBean() {
    	init();
    }

    public ApplicationBean(Application application) {
    	init();
    	this.application = application;
    	if (application != null) {
	    	applicationName = application.getName();
	    	applicationDescription = application.getDescription();
	    	id = application.getAppId();
    	}
    }
    
    protected void init() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        application.setAppId(id);
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
        application.setName(applicationName);
    }

    public String getApplicationDescription() {
        return applicationDescription;
    }

    public void setApplicationDescription(String applicationDescription) {
        this.applicationDescription = applicationDescription;
        application.setDescription(applicationDescription);
    }
}
