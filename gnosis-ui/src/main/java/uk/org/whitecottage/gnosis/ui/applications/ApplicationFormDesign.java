package uk.org.whitecottage.gnosis.ui.applications;

import com.explicatis.ext_token_field.ExtTokenField;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.declarative.Design;

@DesignRoot
@SuppressWarnings("serial")
public class ApplicationFormDesign extends Window {
	protected TextField applicationName;
	protected RichTextArea applicationDescription;
	protected Button save;
	protected Button cancel;
	protected Button delete;
	protected ExtTokenField classification;

	public ApplicationFormDesign() {
		init();
	}

	public ApplicationFormDesign(String caption) {
		super(caption);
		init();
	}
	
	protected void init() {
		Design.read(this);
	}
}
