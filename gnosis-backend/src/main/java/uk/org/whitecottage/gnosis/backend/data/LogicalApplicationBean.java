package uk.org.whitecottage.gnosis.backend.data;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
public class LogicalApplicationBean implements Serializable {

    @NotNull
    private String applicationId = "-1";
    @NotNull
    @Size(min = 1, message = "Application name cannot be blank")
    private String applicationName = "";
    private String applicationDescription = "";
    
    public LogicalApplicationBean() {
    	init();
    }
    
	protected void init() {
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
}
