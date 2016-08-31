package uk.org.whitecottage.gnosis.ui.applications;

import java.util.logging.Logger;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import uk.org.whitecottage.gnosis.backend.GnosisDataService;
import uk.org.whitecottage.gnosis.backend.data.ApplicationBean;

/**
 * A form for editing a single product.
 *
 * Using responsive layouts, the form can be displayed either sliding out on the
 * side of the view or filling the whole screen - see the theme for the related
 * CSS rules.
 */
@SuppressWarnings("serial")
public class LogicalApplicationSelect extends LogicalApplicationSelectDesign {

    private ApplicationsLogic viewLogic;
    private BeanFieldGroup<ApplicationBean> fieldGroup;	
    
	@SuppressWarnings("unused")
	private final static Logger LOGGER = Logger.getLogger(LogicalApplicationSelect.class.getName());

    @SuppressWarnings({ "rawtypes" })
	public LogicalApplicationSelect(ApplicationsLogic applicationLogic) {
        super("Application detail");
        viewLogic = applicationLogic;
		
        //fieldGroup = new BeanFieldGroup<ApplicationBean>(ApplicationBean.class);
        //fieldGroup.bindMemberFields(this);
        
        // perform validation and enable/disable buttons while editing
        ValueChangeListener valueListener = new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                formHasChanged();
            }
        };

        for (Field f : fieldGroup.getFields()) {
            f.addValueChangeListener(valueListener);
        }

        fieldGroup.addCommitHandler(new CommitHandler() {

            @Override
            public void preCommit(CommitEvent commitEvent)
                    throws CommitException {
            }

            @Override
            public void postCommit(CommitEvent commitEvent)
                    throws CommitException {
                GnosisDataService.get().updateApplication(
                        fieldGroup.getItemDataSource().getBean());
            }
        });

        save.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    fieldGroup.commit();

                    // only if validation succeeds
                    ApplicationBean application = fieldGroup.getItemDataSource().getBean();
                    viewLogic.saveApplication(application);
                } catch (CommitException e) {
                    Notification n = new Notification(
                            "Please re-check the fields", Type.ERROR_MESSAGE);
                    n.setDelayMsec(500);
                    n.show(getUI().getPage());
                }
            }
        });

        cancel.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                viewLogic.cancelApplication();
            }
        });

    }

    public void editApplication(ApplicationBean application) {
        if (application == null) {
            application = new ApplicationBean();
        }
        fieldGroup.setItemDataSource(new BeanItem<ApplicationBean>(application));

    }

    private void formHasChanged() {
        // show validation errors after the user has changed something

    }
}
