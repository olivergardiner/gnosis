package uk.org.whitecottage.gnosis.ui.process;

import java.io.Serializable;

import uk.org.whitecottage.gnosis.backend.GnosisDataService;

@SuppressWarnings("serial")
public class ProcessTaxonomyLogic implements Serializable {

	private ProcessTaxonomyView view;

    public ProcessTaxonomyLogic(ProcessTaxonomyView processTaxonomyView) {
        view = processTaxonomyView;
    }

    public void init() {
        view.showFramework(GnosisDataService.get().getProcessTaxonomy());
    }

    public void enter(String eventParameters) {
    	System.out.println(eventParameters);
    }
}
