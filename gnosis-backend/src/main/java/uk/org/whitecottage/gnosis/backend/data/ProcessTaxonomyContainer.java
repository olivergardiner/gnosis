package uk.org.whitecottage.gnosis.backend.data;

import com.vaadin.data.util.HierarchicalContainer;

public class ProcessTaxonomyContainer extends HierarchicalContainer {

	private static final long serialVersionUID = 1L;

	public ProcessTaxonomyContainer() {
    	addContainerProperty("Name", String.class, null);
    	addContainerProperty("Description", String.class, null);
    	addContainerProperty("processId", String.class, null);
	}
}
