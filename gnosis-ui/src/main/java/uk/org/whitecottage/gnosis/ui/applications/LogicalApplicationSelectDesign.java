package uk.org.whitecottage.gnosis.ui.applications;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Window;
import com.vaadin.ui.declarative.Design;

@DesignRoot
@SuppressWarnings("serial")
public class LogicalApplicationSelectDesign extends Window {
	protected Tree tree;
	protected Button save;
	protected Button cancel;

	public LogicalApplicationSelectDesign() {
		init();
	}

	public LogicalApplicationSelectDesign(String caption) {
		super(caption);
		init();
	}
	
	protected void init() {
		Design.read(this);
	}
}
