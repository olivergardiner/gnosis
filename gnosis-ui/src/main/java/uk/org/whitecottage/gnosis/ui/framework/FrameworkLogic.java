package uk.org.whitecottage.gnosis.ui.framework;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FrameworkLogic implements Serializable {

	private FrameworkView view;

    public FrameworkLogic(FrameworkView frameworkView) {
        view = frameworkView;
    }

    public void init() {
        //view.showFramework(FrameworkDataService.get().getFramework());
        view.showFramework();
    }

    public void enter(String eventParameters) {
    	
    }
}
