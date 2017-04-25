package uk.org.whitecottage.gnosis.ui.taxonomy.process;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Tree;
import com.vaadin.ui.declarative.Design;

import uk.org.whitecottage.gnosis.ui.taxonomy.TaxonomyView;

@DesignRoot
@SuppressWarnings("serial")
public abstract class ProcessTaxonomyDesign extends TaxonomyView {
	protected Tree processTaxonomyTree;

	public ProcessTaxonomyDesign() {
		Design.read(this);
		
		_taxonomyTree = processTaxonomyTree;
	}
}
