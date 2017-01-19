package uk.org.whitecottage.gnosis.ui.framework;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Label;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

@DesignRoot
@SuppressWarnings("serial")
public class FrameworkDesign extends VerticalLayout {
	protected FrameworkTree frameworkTree;

	//protected TextField nodeName;
	//protected RichTextArea nodeDescription;
	
	protected Label nodeName;
	protected Label nodeDescription;
	
	public FrameworkDesign() {
		Design.read(this);
	}
}
