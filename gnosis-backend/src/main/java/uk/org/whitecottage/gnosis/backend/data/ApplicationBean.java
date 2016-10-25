package uk.org.whitecottage.gnosis.backend.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.vaadin.data.fieldgroup.PropertyId;

@SuppressWarnings("serial")
public class ApplicationBean implements Serializable {

    @NotNull
    @PropertyId("id")
    protected String id = "-1";
    @NotNull
    @Size(min = 1, message = "Application name cannot be blank")
    @PropertyId("applicationName")
    protected String applicationName = "";
    @PropertyId("applicationDescription")
    protected String applicationDescription = "";

    protected Collection<ClassificationBean> classification;
    
    public ApplicationBean() {
    	init();
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
