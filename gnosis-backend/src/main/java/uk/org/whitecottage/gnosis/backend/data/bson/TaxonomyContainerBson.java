package uk.org.whitecottage.gnosis.backend.data.bson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.vaadin.data.Item;

import uk.org.whitecottage.gnosis.backend.data.TaxonomyContainer;

public class TaxonomyContainerBson extends TaxonomyContainer {

	private static final long serialVersionUID = 1L;

	public TaxonomyContainerBson(FindIterable<Document> taxonomy) {
		super();

		taxonomy.forEach(new Block<Document>() {
    	    @SuppressWarnings("unchecked")
			@Override
    	    public void apply(final Document document) {
	    		Object nodeId = document.get("nodeId");
	    		Item taxonomyNode = addItem(nodeId);
	    		taxonomyNode.getItemProperty("Name").setValue(document.get("name"));
	    		taxonomyNode.getItemProperty("Description").setValue(document.get("description"));

	    		addchildren((Iterable<Document>) document.get("children"), nodeId);
    	    }
    	});
	}
	
	@SuppressWarnings("unchecked")
	protected void addchildren(Iterable<Document> taxonomyNodes, Object parentId) {
		for (Document document: taxonomyNodes) {
    		Object nodeId = document.get("nodeId");
    		Item taxonomyNode = addItem(nodeId);
    		taxonomyNode.getItemProperty("Name").setValue(document.get("name"));
    		taxonomyNode.getItemProperty("Description").setValue(document.get("description"));

    		if (parentId != null) {
    			setParent(nodeId, parentId);
    		}
    		
    		addchildren((Iterable<Document>) document.get("children"), nodeId);
	    }
	}
	
	public List<Document> toBson() {
		List<Document> nodes = new ArrayList<Document>();
		
		Collection<?> ids = rootItemIds();
		for (Object id: ids) {
			Document topLevelNode = toBson(id);
			nodes.add(topLevelNode);
		}
		
		return nodes;
	}
	
	protected Document toBson(Object id) {
		Item nodeItem = getItem(id);
		Document node = new Document();
		
		node.append("nodeId", id);
		node.append("name", nodeItem.getItemProperty("Name").getValue());
		node.append("description", nodeItem.getItemProperty("Description").getValue());
		
		List<Document> children = new ArrayList<Document>();
		
		Collection<?> childIds = getChildren(id);
		if (childIds != null) {
			for (Object childId: childIds) {
				children.add(toBson(childId));
			}
		}
		
		node.append("children", children);
		
		return node;
	}
}
