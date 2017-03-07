package uk.org.whitecottage.gnosis.ui.process;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

@DesignRoot
@SuppressWarnings("serial")
public class ProcessTaxonomyDesign extends VerticalLayout {
	protected ProcessTaxonomyTree processTree;

	//protected TextField nodeName;
	//protected RichTextArea nodeDescription;
	
	protected Label nodeName;
	protected Label nodeDescription;
	
	public ProcessTaxonomyDesign() {
		Design.read(this);
	}
}
