package uk.org.whitecottage.gnosis.ui.applications;

import java.util.Collection;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid.SelectionModel;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import uk.org.whitecottage.gnosis.backend.data.ApplicationBean;
import uk.org.whitecottage.gnosis.backend.data.LogicalApplicationBean;
import uk.org.whitecottage.gnosis.widgetset.ResetButtonForTextField;

/**
 * A view for performing create-read-update-delete operations on products.
 *
 * See also {@link ApplicationsLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
@SuppressWarnings("serial")
public class ApplicationsView extends CssLayout implements View {

    public static final String VIEW_NAME = "Applications";
    private ApplicationGrid grid;
    
    private ApplicationForm form;

    private ApplicationsLogic viewLogic = new ApplicationsLogic(this);
    private Button newApplication;

    public ApplicationsView() {
        setSizeFull();
        addStyleName("crud-view");
        HorizontalLayout topLayout = createTopBar();

        grid = new ApplicationGrid();
        grid.addSelectionListener(new SelectionListener() {

            @Override
            public void select(SelectionEvent event) {
                viewLogic.rowSelected(grid.getSelectedRow());
            }
        });

    	form = new ApplicationForm(viewLogic);
        
        VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.addComponent(topLayout);
        barAndGridLayout.addComponent(grid);
        barAndGridLayout.setMargin(true);
        barAndGridLayout.setSpacing(true);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.setExpandRatio(grid, 1);
        barAndGridLayout.setStyleName("crud-main-layout");

        addComponent(barAndGridLayout);
        //addComponent(form);

        viewLogic.init();
    }

    public HorizontalLayout createTopBar() {
        TextField filter = new TextField();
        filter.setStyleName("filter-textfield");
        filter.setInputPrompt("Filter");
        ResetButtonForTextField.extend(filter);
        filter.setImmediate(true);
        filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                grid.setFilter(event.getText());
            }
        });

        newApplication = new Button("New application");
        newApplication.addStyleName(ValoTheme.BUTTON_PRIMARY);
        newApplication.setIcon(FontAwesome.PLUS_CIRCLE);
        newApplication.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                viewLogic.newApplication();
            }
        });

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setSpacing(true);
        topLayout.setWidth("100%");
        topLayout.addComponent(filter);
        topLayout.addComponent(newApplication);
        topLayout.setComponentAlignment(filter, Alignment.MIDDLE_LEFT);
        topLayout.setExpandRatio(filter, 1);
        topLayout.setStyleName("top-bar");
        return topLayout;
    }

    @Override
    public void enter(ViewChangeEvent event) {
        viewLogic.enter(event.getParameters());
    }

    public void showError(String msg) {
        Notification.show(msg, Type.ERROR_MESSAGE);
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg, Type.TRAY_NOTIFICATION);
    }

    public void setNewApplicationEnabled(boolean enabled) {
        newApplication.setEnabled(enabled);
    }

    public void clearSelection() {
        grid.getSelectionModel().reset();
    }

    public void selectRow(ApplicationBean row) {
        ((SelectionModel.Single) grid.getSelectionModel()).select(row);
    }

    public ApplicationBean getSelectedRow() {
        return grid.getSelectedRow();
    }

    public void editApplication(ApplicationBean application) {
        if (application != null) {
            //form.setEnabled(true);
        	getUI().addWindow(form);
        } else {
            //form.setEnabled(false);
       		form.close();
        }
        
        form.editApplication(application);
    }

    public void showApplications(Collection<ApplicationBean> applications) {
        grid.setApplications(applications);
    }

    public void refreshApplication(ApplicationBean application) {
        grid.refresh(application);
        grid.scrollTo(application);
    }

    public void removeApplication(ApplicationBean application) {
        grid.remove(application);
    }

    public void setClassifications(Collection<LogicalApplicationBean> logicalApplications) {
    	form.setClassifications(logicalApplications);
    }
}
