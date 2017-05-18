package uk.org.whitecottage.gnosis.backend.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.HierarchicalContainer;

public abstract class TaxonomyContainer extends HierarchicalContainer {

	private static final String NAME = "Name";
	private static final String NODE_ID = "nodeId";
	private static final String DESCRIPTION = "Description";
	private static final long serialVersionUID = 1L;

	public TaxonomyContainer() {
    	addContainerProperty(NAME, String.class, null);
    	addContainerProperty(DESCRIPTION, String.class, null);
    	addContainerProperty(NODE_ID, String.class, null);
	}
	
	@SuppressWarnings("unchecked")
	public TaxonomyContainer(FindIterable<Document> taxonomy) {
		this();

		taxonomy.forEach((final Document document) -> {
    		Object nodeId = document.get(NODE_ID);
    		Item taxonomyNode = addItem(nodeId);
    		taxonomyNode.getItemProperty(NAME).setValue(document.get("name"));
    		taxonomyNode.getItemProperty(DESCRIPTION).setValue(document.get("description"));
    		
    		buildNode(taxonomyNode, document);

    		addchildren((Iterable<Document>) document.get("children"), nodeId);
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
    		Object nodeId = document.get(NODE_ID);
    		Item taxonomyNode = addItem(nodeId);
    		taxonomyNode.getItemProperty(NAME).setValue(document.get("name"));
    		taxonomyNode.getItemProperty(DESCRIPTION).setValue(document.get("description"));

    		if (parentId != null) {
    			setParent(nodeId, parentId);
    		}

    		buildNode(taxonomyNode, document);

    		addchildren((Iterable<Document>) document.get("children"), nodeId);
	    }
	}
	
	public List<Document> toBson() {
		List<Document> nodes = new ArrayList<>();
		
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
		
		node.append(NODE_ID, id);
		node.append("name", nodeItem.getItemProperty(NAME).getValue());
		node.append("description", nodeItem.getItemProperty(DESCRIPTION).getValue());
		
		toBson(nodeItem, node);
		
		List<Document> children = new ArrayList<>();
		
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
