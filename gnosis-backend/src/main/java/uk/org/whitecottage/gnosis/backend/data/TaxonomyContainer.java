package uk.org.whitecottage.gnosis.backend.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;

public abstract class TaxonomyContainer extends HierarchicalContainer {

	private static final long serialVersionUID = 1L;

	public TaxonomyContainer() {
    	addContainerProperty("Name", String.class, null);
    	addContainerProperty("Description", String.class, null);
    	addContainerProperty("nodeId", String.class, null);
	}
	
	public TaxonomyContainer(FindIterable<Document> taxonomy) {
		this();

		taxonomy.forEach(new Block<Document>() {
    	    @SuppressWarnings("unchecked")
			@Override
    	    public void apply(final Document document) {
	    		Object nodeId = document.get("nodeId");
	    		Item taxonomyNode = addItem(nodeId);
	    		taxonomyNode.getItemProperty("Name").setValue(document.get("name"));
	    		taxonomyNode.getItemProperty("Description").setValue(document.get("description"));
	    		
	    		buildNode(taxonomyNode, document);

	    		addchildren((Iterable<Document>) document.get("children"), nodeId);
    	    }
    	});
	}
	
	protected abstract void buildNode(Item item, Document document);
	
	public void dropTop(Object sourceItemId, Object targetItemId) {
		Object parentId = getParent(targetItemId);
		
		setParent(sourceItemId, parentId);
		moveAfterSibling(sourceItemId, targetItemId);
		moveAfterSibling(targetItemId, sourceItemId);
	}
	
	public void dropMiddle(Object sourceItemId, Object targetItemId) {
		setParent(sourceItemId, targetItemId);
	}

	public void dropBottom(Object sourceItemId, Object targetItemId) {
		Object parentId = getParent(targetItemId);
		
		setParent(sourceItemId, parentId);
		moveAfterSibling(sourceItemId, targetItemId);
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

    		buildNode(taxonomyNode, document);

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
		
		toBson(nodeItem, node);
		
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
	
	protected abstract void toBson(Item item, Document document);
}
