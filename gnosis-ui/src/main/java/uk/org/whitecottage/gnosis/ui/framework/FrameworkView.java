package uk.org.whitecottage.gnosis.ui.framework;

import com.vaadin.data.Item;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Tree.TreeDragMode;

import uk.org.whitecottage.gnosis.backend.data.FrameworkContainer;

@SuppressWarnings("serial")
public class FrameworkView extends FrameworkDesign implements View {

    public static final String VIEW_NAME = "Framework";

	private FrameworkLogic viewLogic = new FrameworkLogic(this);
	private FrameworkForm form;
	
	private final Action insertAction = new Action("An action");
	private final Action dummyAction = new Action("Does nothing");
	
	public FrameworkView() {
		super();
		
		frameworkTree.setImmediate(true);

    	form = new FrameworkForm(viewLogic);

		viewLogic.init();
	}

    @Override
	public void enter(ViewChangeEvent event) {
    	viewLogic.enter(event.getParameters());
	}

    public void showFramework(FrameworkContainer frameworkContainer) {
    	
    	frameworkTree.setContainerDataSource(frameworkContainer);
    	frameworkTree.setItemCaptionPropertyId("Name");
    	
		for (Object itemId: frameworkTree.getContainerDataSource()
                .getItemIds()) {
			frameworkTree.collapseItem(itemId);

			if (!frameworkTree.hasChildren(itemId))
				frameworkTree.setChildrenAllowed(itemId, false);
		}
		
		frameworkTree.addItemClickListener(new ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					editFrameworkNode(event.getItemId());
				}
			}
		});
		
		frameworkTree.addActionHandler(new Handler() {

			@Override
			public Action[] getActions(Object target, Object sender) {
			    return new Action[] { insertAction, dummyAction };
			}

			@Override
			public void handleAction(Action action, Object sender, Object target) {
				if (action == insertAction) {
					Notification.show("Insert");
				}
			}			
		});
		
		frameworkTree.setDragMode(TreeDragMode.NODE);
		
		frameworkTree.setDropHandler(new DropHandler() {

			@Override
			public void drop(DragAndDropEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public AcceptCriterion getAcceptCriterion() {
				// TODO Auto-generated method stub
				return AcceptAll.get();
			}
		});
    }
    
    protected void editFrameworkNode(Object itemId) {
    	Item item = null;
    	
    	if (itemId != null) {
    		item = frameworkTree.getItem(itemId);
        	getUI().addWindow(form);
        } else {
       		form.close();
        }
    	
    	form.editFrameworkNode(item);
    }
}
