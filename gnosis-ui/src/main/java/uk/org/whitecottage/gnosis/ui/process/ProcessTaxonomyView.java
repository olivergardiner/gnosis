package uk.org.whitecottage.gnosis.ui.process;

import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;

import uk.org.whitecottage.gnosis.backend.data.ProcessTaxonomyContainer;

@SuppressWarnings("serial")
public class ProcessTaxonomyView extends ProcessTaxonomyDesign implements View {

    public static final String VIEW_NAME = "Process Taxonomy";

	private ProcessTaxonomyLogic viewLogic = new ProcessTaxonomyLogic(this);
	
	public ProcessTaxonomyView() {
		super();
		
		processTree.setImmediate(true);
		nodeName.setImmediate(true);
		nodeDescription.setImmediate(true);
    	nodeDescription.setContentMode(ContentMode.HTML);
		
		viewLogic.init();
	}

    @Override
	public void enter(ViewChangeEvent event) {
    	viewLogic.enter(event.getParameters());
	}

    public void showFramework(ProcessTaxonomyContainer processTaxonomyContainer) {
    	
    	processTree.setContainerDataSource(processTaxonomyContainer);
    	processTree.setItemCaptionPropertyId("Name");
    	
		for (Object itemId: processTree.getContainerDataSource()
                .getItemIds()) {
			processTree.collapseItem(itemId);
			//frameworkTree.setItemIcon(itemId, FontAwesome.ANDROID);

			if (!processTree.hasChildren(itemId))
				processTree.setChildrenAllowed(itemId, false);
		}
		
		processTree.addItemClickListener(new ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				editNode(event);
			}
		});
    }
    
    protected void editNode(ItemClickEvent event) {
    	editFrameworkNode(event.getItemId());
    }
    
    protected void editFrameworkNode(Object itemId) {
    	Item item = processTree.getItem(itemId);
     	nodeName.setValue((String) item.getItemProperty("Name").getValue());
    	nodeDescription.setValue((String) item.getItemProperty("Description").getValue());
    }
}
