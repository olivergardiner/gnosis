package uk.org.whitecottage.gnosis.ui;

import java.util.logging.Logger;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

import uk.org.whitecottage.gnosis.Gnosis;
import uk.org.whitecottage.gnosis.ui.about.AboutView;
import uk.org.whitecottage.gnosis.ui.applications.ApplicationsView;
import uk.org.whitecottage.gnosis.ui.taxonomy.application.ApplicationTaxonomyView;
import uk.org.whitecottage.gnosis.ui.taxonomy.process.ProcessTaxonomyView;

/**
 * Content of the UI when the user is logged in.
 * 
 * 
 */
@SuppressWarnings("serial")
public class MainScreen extends HorizontalLayout {
    private Menu menu;

	@SuppressWarnings("unused")
	private final static Logger LOGGER = Logger.getLogger(MainScreen.class.getName());

	public MainScreen(Gnosis ui) {

        setStyleName("main-screen");

        CssLayout viewContainer = new CssLayout();
        viewContainer.addStyleName("valo-content");
        viewContainer.setSizeFull();

        final Navigator navigator = new Navigator(ui, viewContainer);
        //LOGGER.info(navigator.getState());
        navigator.setErrorView(ErrorView.class);
        menu = new Menu(navigator);
        menu.addView(new ApplicationTaxonomyView(), ApplicationTaxonomyView.VIEW_NAME,
        		ApplicationTaxonomyView.VIEW_NAME, FontAwesome.EDIT);
        menu.addView(new ApplicationsView(), ApplicationsView.VIEW_NAME,
                ApplicationsView.VIEW_NAME, FontAwesome.EDIT);
        menu.addView(new ProcessTaxonomyView(), ProcessTaxonomyView.VIEW_NAME,
        		ProcessTaxonomyView.VIEW_NAME, FontAwesome.EDIT);
        menu.addView(new AboutView(), AboutView.VIEW_NAME, AboutView.VIEW_NAME,
                FontAwesome.INFO_CIRCLE);

        navigator.addViewChangeListener(viewChangeListener);

        addComponent(menu);
        addComponent(viewContainer);
        setExpandRatio(viewContainer, 1);
        setSizeFull();
    }

    // notify the view menu about view changes so that it can display which view
    // is currently active
    ViewChangeListener viewChangeListener = new ViewChangeListener() {

        @Override
        public boolean beforeViewChange(ViewChangeEvent event) {
            return true;
        }

        @Override
        public void afterViewChange(ViewChangeEvent event) {
            menu.setActiveView(event.getViewName());
        }

    };
}
