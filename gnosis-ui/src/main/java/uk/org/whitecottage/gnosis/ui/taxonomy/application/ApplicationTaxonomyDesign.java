package uk.org.whitecottage.gnosis.ui.taxonomy.application;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Tree;
import com.vaadin.ui.declarative.Design;

import uk.org.whitecottage.gnosis.ui.taxonomy.TaxonomyView;

@DesignRoot
@SuppressWarnings("serial")
public abstract class ApplicationTaxonomyDesign extends TaxonomyView {
	
	protected Tree applicationTaxonomyTree;

	public ApplicationTaxonomyDesign() {
		Design.read(this);
		
		_taxonomyTree = applicationTaxonomyTree;
	}
}
