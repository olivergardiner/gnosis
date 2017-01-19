package uk.org.whitecottage.gnosis.ui.applications;

import java.util.Collection;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.MethodProperty;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.HtmlRenderer;

import uk.org.whitecottage.gnosis.backend.data.ApplicationBean;

/**
 * Grid of applications, handling the visual presentation and filtering of a set of
 * items. This version uses an in-memory data source that is suitable for small
 * data sets.
 */
@SuppressWarnings("serial")
public class ApplicationGrid extends Grid {

	public ApplicationGrid() {
        setSizeFull();

        setSelectionMode(SelectionMode.SINGLE);

        BeanItemContainer<ApplicationBean> container = new BeanItemContainer<ApplicationBean>(ApplicationBean.class);
        setContainerDataSource(container);
        setColumns("applicationName", "applicationDescription");
        getColumn("applicationDescription").setMaximumWidth(550);
        getColumn("applicationDescription").setRenderer(new HtmlRenderer());
    }

    /**
     * Filter the grid based on a search string that is searched for in the
     * product name, availability and category columns.
     *
     * @param filterString
     *            string to look for
     */
    public void setFilter(String filterString) {
        getContainer().removeAllContainerFilters();
        if (filterString.length() > 0) {
            SimpleStringFilter nameFilter = new SimpleStringFilter("applicationName", filterString, true, false);
            getContainer().addContainerFilter(nameFilter);
        }

    }

    @SuppressWarnings("unchecked")
	private BeanItemContainer<ApplicationBean> getContainer() {
        return (BeanItemContainer<ApplicationBean>) super.getContainerDataSource();
    }

    @Override
    public ApplicationBean getSelectedRow() throws IllegalStateException {
        return (ApplicationBean) super.getSelectedRow();
    }

    public void setApplications(Collection<ApplicationBean> applications) {
        getContainer().removeAllItems();
        getContainer().addAll(applications);
    }

    @SuppressWarnings("rawtypes")
	public void refresh(ApplicationBean application) {
        // We avoid updating the whole table through the backend here so we can
        // get a partial update for the grid
        BeanItem<ApplicationBean> item = getContainer().getItem(application);
        if (item != null) {
            // Updated product
            MethodProperty p = (MethodProperty) item.getItemProperty("id");
            p.fireValueChange();
        } else {
            // New product
            getContainer().addBean(application);
        }
    }

    public void remove(ApplicationBean application) {
        getContainer().removeItem(application);
    }
}
