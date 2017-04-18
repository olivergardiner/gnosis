package uk.org.whitecottage.gnosis.ui.taxonomy;

import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Tree.TreeDragMode;
import com.vaadin.ui.Tree.TreeTargetDetails;

import uk.org.whitecottage.gnosis.backend.data.ApplicationTaxonomyContainer;
import uk.org.whitecottage.gnosis.backend.data.TaxonomyContainer;
import uk.org.whitecottage.gnosis.ui.taxonomy.application.ApplicationTaxonomyDesign;

@SuppressWarnings("serial")
public abstract class TaxonomyView extends ApplicationTaxonomyDesign implements View {

    public static final String VIEW_NAME = "Taxonomy";

	private final Action insertAction = new Action("An action");
	private final Action dummyAction = new Action("Does nothing");
	
	public TaxonomyView() {
		super();
		
		applicationTree.setImmediate(true);
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
		
		applicationTree.addItemClickListener((ItemClickEvent event) -> {
			if (event.isDoubleClick()) {
				editTaxonomyNode(event.getItemId());
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
					updateTaxonomy(container);
				} else if (location == VerticalDropLocation.TOP) {
					// Drop at the top of a subtree -> make it previous
					container.dropTop(sourceItemId, targetItemId);
					updateTaxonomy(container);
				} else if (location == VerticalDropLocation.BOTTOM) {
					// Drop below another item -> make it next
					container.dropBottom(sourceItemId, targetItemId);
					updateTaxonomy(container);
				}
			}

			@Override
			public AcceptCriterion getAcceptCriterion() {
				return AcceptAll.get();
			}
		});
    }
    
    protected abstract void updateTaxonomy(TaxonomyContainer container);
    
    protected abstract void editTaxonomyNode(Object itemId);
}
