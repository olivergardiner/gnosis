package uk.org.whitecottage.gnosis.ui.framework;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.declarative.Design;

@DesignRoot
@SuppressWarnings("serial")
public class FrameworkFormDesign extends Window {
	protected TextField frameworkName;
	protected RichTextArea frameworkDescription;
	protected Button save;
	protected Button cancel;
	protected Button delete;

	public FrameworkFormDesign() {
		init();
	}

	public FrameworkFormDesign(String caption) {
		super(caption);
		init();
	}
	
	protected void init() {
		Design.read(this);
	}
}
