package uk.org.whitecottage.gnosis.ui.taxonomy.application;

import java.util.logging.Logger;

import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * A form for editing an application.
 *
 * Using responsive layouts, the form can be displayed either sliding out on the
 * side of the view or filling the whole screen - see the theme for the related
 * CSS rules.
 */
@SuppressWarnings("serial")
public class LogicalAppForm extends LogicalAppFormDesign {

	private ApplicationTaxonomyLogic viewLogic;
    
	@SuppressWarnings("unused")
	private final static Logger LOGGER = Logger.getLogger(LogicalAppForm.class.getName());

    public LogicalAppForm(ApplicationTaxonomyLogic processTaxonomyLogic) {
        super("Logical Application detail");
        viewLogic = processTaxonomyLogic;
        
		logicalAppName.setImmediate(true);
		logicalAppDescription.setImmediate(true);
    	//nodeDescription.setContentMode(ContentMode.HTML);

        cancel.addClickListener((ClickEvent event) -> viewLogic.cancelApplicationForm());
    }
    
    public void editTaxonomyNode(Item item) {
    	if (item != null) {
	     	logicalAppName.setValue((String) item.getItemProperty("Name").getValue());
	    	logicalAppDescription.setValue((String) item.getItemProperty("Description").getValue());
    	} else {
    		logicalAppName.setValue("");
    		logicalAppDescription.setValue("");
    	}
    }
}
