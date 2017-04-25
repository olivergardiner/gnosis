package uk.org.whitecottage.gnosis.ui.taxonomy.application;

import com.vaadin.data.Item;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import uk.org.whitecottage.gnosis.backend.data.ApplicationTaxonomyContainer;
import uk.org.whitecottage.gnosis.backend.data.TaxonomyContainer;

@SuppressWarnings("serial")
public class ApplicationTaxonomyView extends ApplicationTaxonomyDesign implements View {

    public static final String VIEW_NAME = "Application Taxonomy";

	private ApplicationTaxonomyLogic viewLogic = new ApplicationTaxonomyLogic(this);	
	private LogicalAppForm form;
	
	public ApplicationTaxonomyView() {
		super();		
		
		applicationTaxonomyTree.setImmediate(true);

    	form = new LogicalAppForm(viewLogic);

    	viewLogic.init();
	}

    @Override
	public void enter(ViewChangeEvent event) {
    	viewLogic.enter(event.getParameters());
	}
    
    protected void editTaxonomyNode(Object itemId) {
    	Item item = null;
    	
    	if (itemId != null) {
    		item = applicationTaxonomyTree.getItem(itemId);
        	getUI().addWindow(form);
        } else {
       		form.close();
        }
    	
    	form.editTaxonomyNode(item);
    }

	@Override
	protected void updateTaxonomy(TaxonomyContainer container) {
		viewLogic.updateApplicationTaxonomy((ApplicationTaxonomyContainer) container);
	}
}
