package uk.org.whitecottage.gnosis.ui.taxonomy.application;

import com.vaadin.data.Item;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Tree.TreeDragMode;
import com.vaadin.ui.Tree.TreeTargetDetails;

import uk.org.whitecottage.gnosis.backend.data.ApplicationTaxonomyContainer;

@SuppressWarnings("serial")
public class ApplicationTaxonomyView extends ApplicationTaxonomyDesign implements View {

    public static final String VIEW_NAME = "Application Taxonomy";

	private ApplicationTaxonomyLogic viewLogic = new ApplicationTaxonomyLogic(this);	
	private LogicalAppForm form;
	
	private final Action insertAction = new Action("An action");
	private final Action dummyAction = new Action("Does nothing");
	
	public ApplicationTaxonomyView() {
		super();
		
		applicationTree.setImmediate(true);
		
    	form = new LogicalAppForm(viewLogic);

    	viewLogic.init();
	}

    @Override
	public void enter(ViewChangeEvent event) {
    	viewLogic.enter(event.getParameters());
	}

    public void showApplicationTaxonomy(ApplicationTaxonomyContainer applicationTaxonomyContainer) {
    	
    	applicationTree.setContainerDataSource(applicationTaxonomyContainer);
    	applicationTree.setItemCaptionPropertyId("Name");
    	
		for (Object itemId: applicationTree.getContainerDataSource()
                .getItemIds()) {
			applicationTree.collapseItem(itemId);

			if (!applicationTree.hasChildren(itemId))
				applicationTree.setChildrenAllowed(itemId, false);
		}
		
		applicationTree.addItemClickListener(new ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					editTaxonomyNode(event.getItemId());
				}
			}
		});
		
		applicationTree.addActionHandler(new Handler() {

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
		
		applicationTree.setDragMode(TreeDragMode.NODE);
		
		applicationTree.setDropHandler(new DropHandler() {

			@Override
			public void drop(DragAndDropEvent event) {
				// Wrapper for the object that is dragged
				Transferable t = event.getTransferable();
				
				// Make sure the drag source is the same tree
				if (t.getSourceComponent() != applicationTree) {
					return;
				}
				
				TreeTargetDetails target = (TreeTargetDetails) event.getTargetDetails();
				
				// Get ids of the dragged item and the target item
				Object sourceItemId = t.getData("itemId");
				Object targetItemId = target.getItemIdOver();
				
				// On which side of the target the item was dropped
				VerticalDropLocation location = target.getDropLocation();
				
				ApplicationTaxonomyContainer container = (ApplicationTaxonomyContainer) applicationTree.getContainerDataSource();
				
				if (location == VerticalDropLocation.MIDDLE) {
					// Drop right on an item -> make it a child
					//processTree.setParent(sourceItemId, targetItemId);
					container.dropMiddle(sourceItemId, targetItemId);
					viewLogic.updateApplicationTaxonomy(container);
				} else if (location == VerticalDropLocation.TOP) {
					// Drop at the top of a subtree -> make it previous
					container.dropTop(sourceItemId, targetItemId);
					viewLogic.updateApplicationTaxonomy(container);
				} else if (location == VerticalDropLocation.BOTTOM) {
					// Drop below another item -> make it next
					container.dropBottom(sourceItemId, targetItemId);
					viewLogic.updateApplicationTaxonomy(container);
				}
			}

			@Override
			public AcceptCriterion getAcceptCriterion() {
				return AcceptAll.get();
			}
		});
    }
    
    protected void editTaxonomyNode(Object itemId) {
    	Item item = null;
    	
    	if (itemId != null) {
    		item = applicationTree.getItem(itemId);
        	getUI().addWindow(form);
        } else {
       		form.close();
        }
    	
    	form.editTaxonomyNode(item);
    }
}
