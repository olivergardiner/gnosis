package uk.org.whitecottage.gnosis.ui.taxonomy.process;

import java.io.Serializable;

import com.vaadin.server.Page;

import uk.org.whitecottage.gnosis.Gnosis;
import uk.org.whitecottage.gnosis.backend.GnosisDataService;
import uk.org.whitecottage.gnosis.backend.data.ProcessTaxonomyContainer;
import uk.org.whitecottage.gnosis.backend.data.TaxonomyContainer;

@SuppressWarnings("serial")
public class ProcessTaxonomyLogic implements Serializable {

	private ProcessTaxonomyView view;

    public ProcessTaxonomyLogic(ProcessTaxonomyView processTaxonomyView) {
        view = processTaxonomyView;
    }

    public void init() {
        view.showTaxonomy(GnosisDataService.get().getProcessTaxonomy());
    }

    public void enter(String eventParameters) {
    	//System.out.println(eventParameters);
    }

    public void cancelProcessForm() {
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
        page.setUriFragment("!" + ProcessTaxonomyView.VIEW_NAME + "/"
                + fragmentParameter, false);
    }
    
    public void updateProcessTaxonomy(TaxonomyContainer processTaxonomy) {
    	GnosisDataService.get().updateProcessTaxonomy((ProcessTaxonomyContainer) processTaxonomy);
    }
}
