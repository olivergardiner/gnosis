package uk.org.whitecottage.gnosis.backend.data;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.vaadin.data.fieldgroup.PropertyId;

@SuppressWarnings("serial")
public class ClassificationBean implements Serializable {
    @NotNull
    @PropertyId("ecosystemId")
    protected String ecosystemId = "-1";
    @NotNull
    @PropertyId("applicationId")
    protected String applicationId = "-1";

    public ClassificationBean() {
    	init();
    }

    public ClassificationBean(String ecosystemId, String applicationId) {
    	init();
    	this.ecosystemId = ecosystemId;
    	this.applicationId = applicationId;
    }
    
    protected void init() {
    }

	public String getEcosystemId() {
		return ecosystemId;
	}

	public void setEcosystemId(String ecosystemId) {
		this.ecosystemId = ecosystemId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
}
