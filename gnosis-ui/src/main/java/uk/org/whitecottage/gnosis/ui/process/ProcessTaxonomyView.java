package uk.org.whitecottage.gnosis.ui.process;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
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

import uk.org.whitecottage.gnosis.backend.data.ProcessTaxonomyContainer;

@SuppressWarnings("serial")
public class ProcessTaxonomyView extends ProcessTaxonomyDesign implements View {

    public static final String VIEW_NAME = "Process Taxonomy";

	private ProcessTaxonomyLogic viewLogic = new ProcessTaxonomyLogic(this);	
	private ProcessForm form;
	
	private final Action insertAction = new Action("An action");
	private final Action dummyAction = new Action("Does nothing");
	
	public ProcessTaxonomyView() {
		super();
		
		processTree.setImmediate(true);
		
    	form = new ProcessForm(viewLogic);

    	viewLogic.init();
	}

    @Override
	public void enter(ViewChangeEvent event) {
    	viewLogic.enter(event.getParameters());
	}

    public void showProcessTaxonomy(ProcessTaxonomyContainer processTaxonomyContainer) {
    	
    	processTree.setContainerDataSource(processTaxonomyContainer);
    	processTree.setItemCaptionPropertyId("Name");
    	
		for (Object itemId: processTree.getContainerDataSource()
                .getItemIds()) {
			processTree.collapseItem(itemId);

			if (!processTree.hasChildren(itemId))
				processTree.setChildrenAllowed(itemId, false);
		}
		
		processTree.addItemClickListener(new ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					editTaxonomyNode(event.getItemId());
				}
			}
		});
		
		processTree.addActionHandler(new Handler() {

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
		
		processTree.setDragMode(TreeDragMode.NODE);
		
		processTree.setDropHandler(new DropHandler() {

			@Override
			public void drop(DragAndDropEvent event) {
				// TODO Auto-generated method stub
				
				// Wrapper for the object that is dragged
				Transferable t = event.getTransferable();
				
				// Make sure the drag source is the same tree
				if (t.getSourceComponent() != processTree) {
					return;
				}
				
				TreeTargetDetails target = (TreeTargetDetails) event.getTargetDetails();
				
				// Get ids of the dragged item and the target item
				Object sourceItemId = t.getData("itemId");
				Object targetItemId = target.getItemIdOver();
				
				// On which side of the target the item was dropped
				VerticalDropLocation location = target.getDropLocation();
				
				HierarchicalContainer container = (HierarchicalContainer) processTree.getContainerDataSource();
				
				if (location == VerticalDropLocation.MIDDLE) {
					// Drop right on an item -> make it a child
					//processTree.setParent(sourceItemId, targetItemId);
					((ProcessTaxonomyContainer) container).dropMiddle(sourceItemId, targetItemId);
					viewLogic.updateProcessTaxonomy((ProcessTaxonomyContainer) container);
				} else if (location == VerticalDropLocation.TOP) {
					// Drop at the top of a subtree -> make it previous
					((ProcessTaxonomyContainer) container).dropTop(sourceItemId, targetItemId);
					viewLogic.updateProcessTaxonomy((ProcessTaxonomyContainer) container);
				} else if (location == VerticalDropLocation.BOTTOM) {
					// Drop below another item -> make it next
					((ProcessTaxonomyContainer) container).dropBottom(sourceItemId, targetItemId);
					viewLogic.updateProcessTaxonomy((ProcessTaxonomyContainer) container);
				}
			}

			@Override
			public AcceptCriterion getAcceptCriterion() {
				// TODO Auto-generated method stub
				return AcceptAll.get();
			}
		});
    }
    
    protected void editTaxonomyNode(Object itemId) {
    	Item item = null;
    	
    	if (itemId != null) {
    		item = processTree.getItem(itemId);
        	getUI().addWindow(form);
        } else {
       		form.close();
        }
    	
    	form.editTaxonomyNode(item);
    }
}
