package uk.org.whitecottage.gnosis.ui.applications;

import java.util.logging.Logger;

import com.explicatis.ext_token_field.ExtTokenField;
import com.explicatis.ext_token_field.SimpleTokenizable;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
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
public class ApplicationForm extends ApplicationFormDesign {

    private ApplicationsLogic viewLogic;
    private BeanFieldGroup<ApplicationBean> fieldGroup;	
    private ComboBox classificationInput;
    
	private static final String	LABEL	= "label";
	private static String[]	LANGUAGES	= {"PHP", "Java", "JavaScript", "Scala", "Python", "C", "Ruby", "C++"};

	@SuppressWarnings("unused")
	private final static Logger LOGGER = Logger.getLogger(ApplicationForm.class.getName());

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public ApplicationForm(ApplicationsLogic applicationLogic) {
        super("Application detail");
        viewLogic = applicationLogic;
		
		classificationInput = new ComboBox();
		classificationInput.setItemCaptionPropertyId(LABEL);
		classificationInput.setInputPrompt("Type here to add");
		classificationInput.addContainerProperty(LABEL, String.class, "");

		for (String lang : LANGUAGES) {
			Object addItem = classificationInput.addItem();
			classificationInput.getItem(addItem).getItemProperty(LABEL).setValue(lang);
		}
		
		classificationInput.addValueChangeListener(getComboBoxValueChange(classification, classificationInput));

		classification.setInputField(classificationInput);
		classification.setEnableDefaultDeleteTokenAction(true);
        
        fieldGroup = new BeanFieldGroup<ApplicationBean>(ApplicationBean.class);
        fieldGroup.bindMemberFields(this);
        
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

        delete.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                ApplicationBean application = fieldGroup.getItemDataSource().getBean();
                viewLogic.deleteApplication(application);
            }
        });
    }

	private ValueChangeListener getComboBoxValueChange(ExtTokenField extTokenField, ComboBox comboBox) {
		return event -> {
			Object id = event.getProperty().getValue();
			if (id != null)
			{
				Item item = comboBox.getItem(id);
				String string = (String) item.getItemProperty(LABEL).getValue();
				int idInt = (int) id;
				SimpleTokenizable t = new SimpleTokenizable(Integer.toUnsignedLong(idInt), string);
				extTokenField.addTokenizable(t);

				// if you would use a real container, you would filter the selected tokens out

				// reset combobox
				comboBox.setValue(null);
			}
		};
	}

    public void editApplication(ApplicationBean application) {
        if (application == null) {
            application = new ApplicationBean();
        }
        fieldGroup.setItemDataSource(new BeanItem<ApplicationBean>(application));

        // before the user makes any changes, disable validation error indicator
        applicationName.setValidationVisible(false);
        applicationDescription.setValidationVisible(false);
    }

    private void formHasChanged() {
        // show validation errors after the user has changed something
        applicationName.setValidationVisible(true);
        applicationDescription.setValidationVisible(true);

        // only applications that have been saved should be removable
        boolean canRemoveApplication = false;
        BeanItem<ApplicationBean> item = fieldGroup.getItemDataSource();
        if (item != null) {
            ApplicationBean application = item.getBean();
            canRemoveApplication = !application.getId().equals("-1");
        }
        delete.setEnabled(canRemoveApplication);
    }
}
