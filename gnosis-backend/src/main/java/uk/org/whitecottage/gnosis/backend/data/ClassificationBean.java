package uk.org.whitecottage.gnosis.backend.data;

import java.io.Serializable;

import com.explicatis.ext_token_field.Tokenizable;
import com.vaadin.data.fieldgroup.PropertyId;

@SuppressWarnings("serial")
public class ClassificationBean implements Serializable, Tokenizable {
    @PropertyId("applicationId")
    protected String applicationId = "-1";
    @PropertyId("applicationName")
    protected String applicationName = "-1";
    @PropertyId("applicationDescription")
    protected String applicationDescription = "-1";

    public ClassificationBean() {
    	init();
    }

    public ClassificationBean(String applicationId) {
    	init();
    	this.applicationId = applicationId;
    }
    
    protected void init() {
    	// Standard placeholder for any bean initialisation - not required in this case
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
		return applicationName;
	}

	@Override
	public long getIdentifier() {
		return applicationId.hashCode();
	}
}
