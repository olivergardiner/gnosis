package uk.org.whitecottage.gnosis.ui.taxonomy.application;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.declarative.Design;

@DesignRoot
@SuppressWarnings("serial")
public class LogicalAppFormDesign extends Window {
	protected TextField logicalAppName;
	protected RichTextArea logicalAppDescription;
	protected Button save;
	protected Button cancel;
	protected Button delete;

	public LogicalAppFormDesign() {
		init();
	}

	public LogicalAppFormDesign(String caption) {
		super(caption);
		init();
	}
	
	protected void init() {
		Design.read(this);
	}
}
