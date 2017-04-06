package uk.org.whitecottage.gnosis.backend.data;

import com.vaadin.data.util.HierarchicalContainer;

public abstract class TaxonomyContainer extends HierarchicalContainer {

	private static final long serialVersionUID = 1L;

	public TaxonomyContainer() {
    	addContainerProperty("Name", String.class, null);
    	addContainerProperty("Description", String.class, null);
    	addContainerProperty("nodeId", String.class, null);
	}
	
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
}
