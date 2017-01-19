package uk.org.whitecottage.gnosis.ui.framework;

import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;

import uk.org.whitecottage.gnosis.backend.data.FrameworkContainer;

@SuppressWarnings("serial")
public class FrameworkView extends FrameworkDesign implements View {

    public static final String VIEW_NAME = "Framework";

	private FrameworkLogic viewLogic = new FrameworkLogic(this);
	
	public FrameworkView() {
		super();
		
		frameworkTree.setImmediate(true);
		nodeName.setImmediate(true);
		nodeDescription.setImmediate(true);
    	nodeDescription.setContentMode(ContentMode.HTML);
		
		viewLogic.init();
	}

    @Override
	public void enter(ViewChangeEvent event) {
    	viewLogic.enter(event.getParameters());
	}

    public void showFramework(FrameworkContainer frameworkContainer) {
    	
    	frameworkTree.setContainerDataSource(frameworkContainer);
		/*frameworkTree.setColumnWidth("Name", 250);
		frameworkTree.setColumnWidth("Description", 800);
		frameworkTree.setColumnWidth("appId", 00);
		frameworkTree.setVisibleColumns("Name", "Description");*/
    	frameworkTree.setItemCaptionPropertyId("Name");
    	
		for (Object itemId: frameworkTree.getContainerDataSource()
                .getItemIds()) {
			//frameworkTree.setCollapsed(itemId, true);
			frameworkTree.collapseItem(itemId);
			//frameworkTree.setItemIcon(itemId, FontAwesome.ANDROID);

			if (!frameworkTree.hasChildren(itemId))
				frameworkTree.setChildrenAllowed(itemId, false);
		}
		
		frameworkTree.addItemClickListener(new ItemClickListener() {

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
    	Item item = frameworkTree.getItem(itemId);
     	nodeName.setValue((String) item.getItemProperty("Name").getValue());
    	nodeDescription.setValue((String) item.getItemProperty("Description").getValue());
    }
}
