package uk.org.whitecottage.gnosis.ui.taxonomy.process;

import com.vaadin.data.Item;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import uk.org.whitecottage.gnosis.backend.data.TaxonomyContainer;

@SuppressWarnings("serial")
public class ProcessTaxonomyView extends ProcessTaxonomyDesign implements View {

    public static final String VIEW_NAME = "Process Taxonomy";

	private ProcessTaxonomyLogic viewLogic = new ProcessTaxonomyLogic(this);	
	private ProcessForm form;
	
	public ProcessTaxonomyView() {
		super();
		
		processTaxonomyTree.setImmediate(true);
		
    	form = new ProcessForm(viewLogic);

    	viewLogic.init();
	}

    @Override
	public void enter(ViewChangeEvent event) {
    	viewLogic.enter(event.getParameters());
	}
    
    protected void editTaxonomyNode(Object itemId) {
    	Item item = null;
    	
    	if (itemId != null) {
    		item = processTaxonomyTree.getItem(itemId);
        	getUI().addWindow(form);
        } else {
       		form.close();
        }
    	
    	form.editTaxonomyNode(item);
    }

	@Override
	protected void updateTaxonomy(TaxonomyContainer container) {
		viewLogic.updateProcessTaxonomy(container);
	}
}
