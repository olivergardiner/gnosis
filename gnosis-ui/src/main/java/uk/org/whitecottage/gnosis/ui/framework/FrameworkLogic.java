package uk.org.whitecottage.gnosis.ui.framework;

import java.io.Serializable;

import com.vaadin.server.Page;

import uk.org.whitecottage.gnosis.Gnosis;
import uk.org.whitecottage.gnosis.backend.GnosisDataService;
import uk.org.whitecottage.gnosis.ui.applications.ApplicationsView;

@SuppressWarnings("serial")
public class FrameworkLogic implements Serializable {

	private FrameworkView view;

    public FrameworkLogic(FrameworkView frameworkView) {
        view = frameworkView;
    }

    public void init() {
        view.showFramework(GnosisDataService.get().getFramework());
    }

    public void enter(String eventParameters) {
    	System.out.println(eventParameters);
    }
    
    public void cancelFrameworkForm() {
        setFragmentParameter("");
        view.editFrameworkNode(null);
    }

    /**
     * Update the fragment without causing navigator to change view
     */
    private void setFragmentParameter(String applicationId) {
        String fragmentParameter;
        if (applicationId == null || applicationId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = applicationId;
        }

        Page page = Gnosis.get().getPage();
        page.setUriFragment("!" + ApplicationsView.VIEW_NAME + "/"
                + fragmentParameter, false);
    }
}
