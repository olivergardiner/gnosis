package uk.org.whitecottage.gnosis.ui.taxonomy.process;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.declarative.Design;

@DesignRoot
@SuppressWarnings("serial")
public class ProcessFormDesign extends Window {
	protected TextField activityName;
	protected RichTextArea activityDescription;
	protected Button save;
	protected Button cancel;
	protected Button delete;

	public ProcessFormDesign() {
		init();
	}

	public ProcessFormDesign(String caption) {
		super(caption);
		init();
	}
	
	protected void init() {
		Design.read(this);
	}
}
