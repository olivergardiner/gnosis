package uk.org.whitecottage.gnosis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Viewport;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import uk.org.whitecottage.gnosis.backend.GnosisDataService;
import uk.org.whitecottage.gnosis.ui.MainScreen;
import uk.org.whitecottage.gnosis.ui.authentication.AccessControl;
import uk.org.whitecottage.gnosis.ui.authentication.BasicAccessControl;
import uk.org.whitecottage.gnosis.ui.authentication.LoginScreen;
import uk.org.whitecottage.gnosis.ui.authentication.LoginScreen.LoginListener;

/**
 * Main UI class of the application that shows either the login screen or the
 * main view of the application depending on whether a user is signed in.
 *
 * The @Viewport annotation configures the viewport meta tags appropriately on
 * mobile devices. Instead of device based scaling (default), using responsive
 * layouts.
 */
@SuppressWarnings("serial")
@Viewport("user-scalable=no,initial-scale=1.0")
@Theme("gnosistheme")
@Widgetset("uk.org.whitecottage.gnosis.GnosisWidgetset")
public class Gnosis extends UI {

	private final static Logger LOGGER = Logger.getLogger(Gnosis.class.getName());
    private AccessControl accessControl = new BasicAccessControl();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	Properties properties = new Properties();
    	String base = vaadinRequest.getService().getBaseDirectory().getAbsolutePath();

    	try {
			FileInputStream input = new FileInputStream(new File(base + "/WEB-INF/gnosis/gnosis.properties"));
			properties.load(input);
			input.close();
		} catch (FileNotFoundException e) {
			LOGGER.warning("Gnosis properties file not found");
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
		}

    	GnosisDataService.get().init(properties);
    	
    	Responsive.makeResponsive(this);
        setLocale(vaadinRequest.getLocale());
        getPage().setTitle("Gnosis");
        if (!accessControl.isUserSignedIn()) {
            setContent(new LoginScreen(accessControl, new LoginListener() {
                @Override
                public void loginSuccessful() {
                    showMainView();
                }
            }));
        } else {
            showMainView();
        }
    }

    protected void showMainView() {
        addStyleName(ValoTheme.UI_WITH_MENU);
        setContent(new MainScreen(Gnosis.this));
        getNavigator().navigateTo(getNavigator().getState());
    }

    public static Gnosis get() {
        return (Gnosis) UI.getCurrent();
    }

    public AccessControl getAccessControl() {
        return accessControl;
    }

    @WebServlet(urlPatterns = "/*", name = "GnosisServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = Gnosis.class, productionMode = false)
    public static class GnosisServlet extends VaadinServlet {
    }
}
