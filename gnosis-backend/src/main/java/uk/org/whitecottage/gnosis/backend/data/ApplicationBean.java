package uk.org.whitecottage.gnosis.backend.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.bson.Document;

//import com.vaadin.data.fieldgroup.PropertyId;

@SuppressWarnings("serial")
public class ApplicationBean implements Serializable {

    @NotNull
    //@PropertyId("id")
    protected String id = "-1";
    @NotNull
    @Size(min = 1, message = "Application name cannot be blank")
    //@PropertyId("applicationName")
    protected String applicationName = "";
    //@PropertyId("applicationDescription")
    protected String applicationDescription = "";

    private Collection<ClassificationBean> classification;
    
    public ApplicationBean() {
    	init();
    }
    
    @SuppressWarnings("unchecked")
	public ApplicationBean(Document application, ClassificationMap classificationMap) {
    	init();
    	    	
    	if (application != null) {
	    	applicationName = application.getString("name");
	    	applicationDescription = application.getString("description");
	    	id = application.getString("app-id");
	    	
	    	Iterable<String> classifications = (Iterable<String>) application.get("logical-apps");
			if (classifications != null) {
				for (String logicalApplication : classifications) {
					ClassificationBean classificationBean = new ClassificationBean(logicalApplication);
					classificationBean.setApplicationName(classificationMap.getLogicalApplicationName((String) logicalApplication));
					classification.add(classificationBean);
				}
			}
    	}
    }
    
    protected void init() {
    	classification = new ArrayList<>();
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

    public static Document toBson(ApplicationBean application) {
    	Document applicationDocument = new Document();

    	applicationDocument.append("app-id", application.getId());
    	applicationDocument.append("name", application.getApplicationName());
    	applicationDocument.append("description", application.getApplicationDescription());
    	
    	List<String> logicalApps = new ArrayList<>();
    	for (ClassificationBean classificationBean: application.getClassification()) {
    		logicalApps.add(classificationBean.getApplicationId());
    	}
    	applicationDocument.append("logical-apps", logicalApps);

    	return applicationDocument;
    }
}
