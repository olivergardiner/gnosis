package uk.org.whitecottage.gnosis.ui.applications;

import java.io.Serializable;

import com.vaadin.server.Page;

import uk.org.whitecottage.gnosis.Gnosis;
import uk.org.whitecottage.gnosis.backend.GnosisDataService;
import uk.org.whitecottage.gnosis.backend.data.ApplicationBean;
import uk.org.whitecottage.gnosis.backend.data.ClassificationBean;

/**
 * This class provides an interface for the logical operations between the CRUD
 * view, its parts like the product editor form and the data source, including
 * fetching and saving products.
 *
 * Having this separate from the view makes it easier to test various parts of
 * the system separately, and to e.g. provide alternative views for the same
 * data.
 */
@SuppressWarnings("serial")
public class ApplicationsLogic implements Serializable {

    private ApplicationsView view;

    public ApplicationsLogic(ApplicationsView applicationsView) {
        view = applicationsView;
    }

    public void init() {
        editApplication(null);
        // Hide and disable if not admin
        if (!Gnosis.get().getAccessControl().isUserInRole("admin")) {
            view.setNewApplicationEnabled(false);
        }

        view.showApplications(GnosisDataService.get().getAllApplications());
    }

    public void cancelApplication() {
        setFragmentParameter("");
        view.clearSelection();
        view.editApplication(null);
    }

    /**
     * Update the fragment without causing navigator to change view
     */
    private void setFragmentParameter(String applicationId) {
        String fragmentParameter;
        if (applicationId == null || applicationId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = applicationId;
        }

        Page page = Gnosis.get().getPage();
        page.setUriFragment("!" + ApplicationsView.VIEW_NAME + "/"
                + fragmentParameter, false);
    }

    public void enter(String applicationId) {
        if (applicationId != null && !applicationId.isEmpty()) {
            if (applicationId.equals("new")) {
                newApplication();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    ApplicationBean application = findApplication(applicationId);
                    view.selectRow(application);
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    private ApplicationBean findApplication(String applicationId) {
        return GnosisDataService.get().getApplicationById(applicationId);
    }

    public void saveApplication(ApplicationBean application) {
    	
    	
        view.showSaveNotification(application.getApplicationName() + " ("
                + application.getId() + ") updated");
        view.clearSelection();
        view.editApplication(null);
        view.refreshApplication(application);
        setFragmentParameter("");
    }

    public void deleteApplication(ApplicationBean application) {
    	GnosisDataService.get().deleteApplication(application.getId());
        view.showSaveNotification(application.getApplicationName() + " ("
                + application.getId() + ") removed");

        view.clearSelection();
        view.editApplication(null);
        view.removeApplication(application);
        setFragmentParameter("");
    }

    public void editApplication(ApplicationBean application) {
        if (application == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(application.getId() + "");
        }
        view.setClassifications(GnosisDataService.get().getAllLogicalApplications(true));
        view.editApplication(application);
    }

    public void newApplication() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editApplication(new ApplicationBean());
    }

    public void rowSelected(ApplicationBean application) {
        if (Gnosis.get().getAccessControl().isUserInRole("admin")) {
            view.editApplication(application);
        }
    }
}
