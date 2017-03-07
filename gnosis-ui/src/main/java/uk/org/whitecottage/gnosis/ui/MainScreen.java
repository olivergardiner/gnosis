package uk.org.whitecottage.gnosis.ui;

import uk.org.whitecottage.gnosis.Gnosis;
import uk.org.whitecottage.gnosis.ui.about.AboutView;
import uk.org.whitecottage.gnosis.ui.applications.ApplicationsView;
import uk.org.whitecottage.gnosis.ui.framework.FrameworkView;
import uk.org.whitecottage.gnosis.ui.process.ProcessTaxonomyView;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * Content of the UI when the user is logged in.
 * 
 * 
 */
@SuppressWarnings("serial")
public class MainScreen extends HorizontalLayout {
    private Menu menu;

    public MainScreen(Gnosis ui) {

        setStyleName("main-screen");

        CssLayout viewContainer = new CssLayout();
        viewContainer.addStyleName("valo-content");
        viewContainer.setSizeFull();

        final Navigator navigator = new Navigator(ui, viewContainer);
        navigator.setErrorView(ErrorView.class);
        menu = new Menu(navigator);
        menu.addView(new FrameworkView(), FrameworkView.VIEW_NAME,
                FrameworkView.VIEW_NAME, FontAwesome.EDIT);
        menu.addView(new ProcessTaxonomyView(), ProcessTaxonomyView.VIEW_NAME,
        		ProcessTaxonomyView.VIEW_NAME, FontAwesome.EDIT);
        menu.addView(new ApplicationsView(), ApplicationsView.VIEW_NAME,
                ApplicationsView.VIEW_NAME, FontAwesome.EDIT);
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
