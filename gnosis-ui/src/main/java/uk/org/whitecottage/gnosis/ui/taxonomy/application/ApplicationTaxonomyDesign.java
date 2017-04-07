package uk.org.whitecottage.gnosis.ui.taxonomy.application;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

@DesignRoot
@SuppressWarnings("serial")
public class ApplicationTaxonomyDesign extends VerticalLayout {
	protected ApplicationTaxonomyTree applicationTree;

	public ApplicationTaxonomyDesign() {
		Design.read(this);
	}
}
