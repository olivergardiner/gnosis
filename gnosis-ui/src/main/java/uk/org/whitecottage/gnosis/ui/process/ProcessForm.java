package uk.org.whitecottage.gnosis.ui.process;

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
public class ProcessForm extends ProcessFormDesign {

	private ProcessTaxonomyLogic viewLogic;
    
	@SuppressWarnings("unused")
	private final static Logger LOGGER = Logger.getLogger(ProcessForm.class.getName());

    public ProcessForm(ProcessTaxonomyLogic processTaxonomyLogic) {
        super("Process detail");
        viewLogic = processTaxonomyLogic;
        
		activityName.setImmediate(true);
		activityDescription.setImmediate(true);
    	//nodeDescription.setContentMode(ContentMode.HTML);

        cancel.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                viewLogic.cancelProcessForm();
            }
        });
    }
    
    public void editTaxonomyNode(Item item) {
    	if (item != null) {
	     	activityName.setValue((String) item.getItemProperty("Name").getValue());
	    	activityDescription.setValue((String) item.getItemProperty("Description").getValue());
    	} else {
    		activityName.setValue("");
    		activityDescription.setValue("");
    	}
    }
}
