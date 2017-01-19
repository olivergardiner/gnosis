package uk.org.whitecottage.gnosis.ui.framework;

import java.io.Serializable;

import uk.org.whitecottage.gnosis.backend.GnosisDataService;

@SuppressWarnings("serial")
public class FrameworkLogic implements Serializable {

	private FrameworkView view;

    public FrameworkLogic(FrameworkView frameworkView) {
        view = frameworkView;
    }

    public void init() {
        view.showFramework(GnosisDataService.get().getFramework());
        //view.showFramework();
    }

    public void enter(String eventParameters) {
    	System.out.println(eventParameters);
    }
}
