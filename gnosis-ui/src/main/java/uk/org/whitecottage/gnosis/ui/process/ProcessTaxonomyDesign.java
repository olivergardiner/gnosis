package uk.org.whitecottage.gnosis.ui.process;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

@DesignRoot
@SuppressWarnings("serial")
public class ProcessTaxonomyDesign extends VerticalLayout {
	protected ProcessTaxonomyTree processTree;

	public ProcessTaxonomyDesign() {
		Design.read(this);
	}
}
