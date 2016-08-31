package uk.org.whitecottage.gnosis.ui.framework;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

@SuppressWarnings("serial")
public class FrameworkView extends FrameworkDesign implements View {

    public static final String VIEW_NAME = "Framework";

	private FrameworkLogic viewLogic = new FrameworkLogic(this);
	
	public FrameworkView() {
		super();
		
		viewLogic.init();
	}

    @Override
	public void enter(ViewChangeEvent event) {
    	viewLogic.enter(event.getParameters());
	}

    public void showFramework() {
    	
    	//frameworkTree.
    }
}
