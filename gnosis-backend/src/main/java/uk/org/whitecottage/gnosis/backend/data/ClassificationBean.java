package uk.org.whitecottage.gnosis.backend.data;

import java.io.Serializable;

import com.explicatis.ext_token_field.Tokenizable;
import com.vaadin.data.fieldgroup.PropertyId;

@SuppressWarnings("serial")
public class ClassificationBean implements Serializable, Tokenizable {
    @PropertyId("ecosystemId")
    protected String ecosystemId = "-1";
    @PropertyId("applicationId")
    protected String applicationId = "-1";
    @PropertyId("applicationName")
    protected String applicationName = "-1";
    @PropertyId("applicationDescription")
    protected String applicationDescription = "-1";

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

	@Override
	public String getStringValue() {
		// TODO Auto-generated method stub
		return applicationName;
	}

	@Override
	public long getIdentifier() {
		// TODO Auto-generated method stub
		return applicationId.hashCode();
	}
}
