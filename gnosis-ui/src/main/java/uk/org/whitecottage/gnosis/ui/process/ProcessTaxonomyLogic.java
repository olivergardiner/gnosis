package uk.org.whitecottage.gnosis.ui.process;

import java.io.Serializable;

import com.vaadin.server.Page;

import uk.org.whitecottage.gnosis.Gnosis;
import uk.org.whitecottage.gnosis.backend.GnosisDataService;
import uk.org.whitecottage.gnosis.backend.data.ProcessTaxonomyContainer;
import uk.org.whitecottage.gnosis.backend.data.TaxonomyContainer;
import uk.org.whitecottage.gnosis.ui.applications.ApplicationsView;

@SuppressWarnings("serial")
public class ProcessTaxonomyLogic implements Serializable {

	private ProcessTaxonomyView view;

    public ProcessTaxonomyLogic(ProcessTaxonomyView processTaxonomyView) {
        view = processTaxonomyView;
    }

    public void init() {
        view.showProcessTaxonomy(GnosisDataService.get().getProcessTaxonomy());
    }

    public void enter(String eventParameters) {
    	System.out.println(eventParameters);
    }

    public void cancelProcessForm() {
        setFragmentParameter("");
        view.editTaxonomyNode(null);
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
    
    public void updateProcessTaxonomy(TaxonomyContainer processTaxonomy) {
    	GnosisDataService.get().updateProcessTaxonomy(processTaxonomy);
    }
}
