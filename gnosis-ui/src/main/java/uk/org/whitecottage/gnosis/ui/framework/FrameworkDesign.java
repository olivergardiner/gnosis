package uk.org.whitecottage.gnosis.ui.framework;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

@DesignRoot
@SuppressWarnings("serial")
public class FrameworkDesign extends VerticalLayout {
	protected FrameworkTree frameworkTree;
	
	public FrameworkDesign() {
		Design.read(this);
	}
}
