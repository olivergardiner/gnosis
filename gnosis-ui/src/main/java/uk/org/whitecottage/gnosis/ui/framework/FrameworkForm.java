package uk.org.whitecottage.gnosis.ui.framework;

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
public class FrameworkForm extends FrameworkFormDesign {

	private FrameworkLogic viewLogic;
    
	@SuppressWarnings("unused")
	private final static Logger LOGGER = Logger.getLogger(FrameworkForm.class.getName());

    public FrameworkForm(FrameworkLogic frameworkLogic) {
        super("Application detail");
        viewLogic = frameworkLogic;
        
		frameworkName.setImmediate(true);
		frameworkDescription.setImmediate(true);

        cancel.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                viewLogic.cancelFrameworkForm();
            }
        });
    }
    
    public void editFrameworkNode(Item item) {
    	if (item != null) {
	     	frameworkName.setValue((String) item.getItemProperty("Name").getValue());
	    	frameworkDescription.setValue((String) item.getItemProperty("Description").getValue());
    	} else {
    		frameworkName.setValue("");
    		frameworkDescription.setValue("");
    	}
    }
}
