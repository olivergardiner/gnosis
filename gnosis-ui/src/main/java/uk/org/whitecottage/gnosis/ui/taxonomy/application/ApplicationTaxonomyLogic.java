package uk.org.whitecottage.gnosis.ui.taxonomy.application;

import java.io.Serializable;

import com.vaadin.server.Page;

import uk.org.whitecottage.gnosis.Gnosis;
import uk.org.whitecottage.gnosis.backend.GnosisDataService;
import uk.org.whitecottage.gnosis.backend.data.ApplicationTaxonomyContainer;

@SuppressWarnings("serial")
public class ApplicationTaxonomyLogic implements Serializable {

	private ApplicationTaxonomyView view;

    public ApplicationTaxonomyLogic(ApplicationTaxonomyView applicationTaxonomyView) {
        view = applicationTaxonomyView;
    }

    public void init() {
        view.showTaxonomy(GnosisDataService.get().getApplicationTaxonomy());
    }

    public void enter(String eventParameters) {
    	//System.out.println(eventParameters);
    }

    public void cancelApplicationForm() {
        setFragmentParameter("");
        view.editTaxonomyNode(null);
    }

    /**
     * Update the fragment without causing navigator to change view
     */
    private void setFragmentParameter(String activityId) {
        String fragmentParameter;
        if (activityId == null || activityId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = activityId;
        }

        Page page = Gnosis.get().getPage();
        page.setUriFragment("!" + ApplicationTaxonomyView.VIEW_NAME + "/"
                + fragmentParameter, false);
    }
    
    public void updateApplicationTaxonomy(ApplicationTaxonomyContainer applicationTaxonomy) {
    	GnosisDataService.get().updateApplicationTaxonomy(applicationTaxonomy);
    }
}
