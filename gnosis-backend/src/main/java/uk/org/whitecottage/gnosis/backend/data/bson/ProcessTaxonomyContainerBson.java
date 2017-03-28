package uk.org.whitecottage.gnosis.backend.data.bson;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.vaadin.data.Item;

import uk.org.whitecottage.gnosis.backend.data.ProcessTaxonomyContainer;

public class ProcessTaxonomyContainerBson extends ProcessTaxonomyContainer {

	private static final long serialVersionUID = 1L;

	public ProcessTaxonomyContainerBson(FindIterable<Document> processTaxonomy) {
		super();

		processTaxonomy.forEach(new Block<Document>() {
    	    @SuppressWarnings("unchecked")
			@Override
    	    public void apply(final Document document) {
	    		Object activityId = document.get("activityId");
	    		Item activity = addItem(activityId);
	    		activity.getItemProperty("Name").setValue(document.get("name"));
	    		activity.getItemProperty("Description").setValue(document.get("description"));

	    		addActivities((Iterable<Document>) document.get("activities"), activityId);
    	    }
    	});
	}
	
	@SuppressWarnings("unchecked")
	protected void addActivities(Iterable<Document> taxonomyNode, Object parentId) {
		for (Document document: taxonomyNode) {
    		Object activityId = document.get("activityId");
    		Item activity = addItem(activityId);
    		activity.getItemProperty("Name").setValue(document.get("name"));
    		activity.getItemProperty("Description").setValue(document.get("description"));

    		if (parentId != null) {
    			setParent(activityId, parentId);
    		}
    		
    		addActivities((Iterable<Document>) document.get("activities"), activityId);
	    }
	}
	
	public List<Document> toBson() {
		List<Document> processes = new ArrayList<Document>();
		
		return processes;
	}
}
