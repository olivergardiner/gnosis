package uk.org.whitecottage.gnosis.ui.process;

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
