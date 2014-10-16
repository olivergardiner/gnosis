package uk.org.whitecottage.ea.gnosis.portlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import uk.org.whitecottage.ea.portlet.ResourceActionPortlet;

public class Gnosis2Portlet extends ResourceActionPortlet {
	private static final Logger log = Logger.getLogger("uk.org.whitecottage.ea.gnosis.portlet");

	public Properties getProperties() {
		Properties properties = new Properties();
		String configPath = System.getProperty("jboss.server.config.dir") + File.separator + "gnosis2.properties";
		try {
			FileInputStream input = new FileInputStream(new File(configPath));
			properties.load(input);
			input.close();
		} catch (FileNotFoundException e) {
			log.warning("Gnosis2 properties file not found");
		} catch (IOException e) {
			log.severe(e.getMessage());
		}
		
		return properties;
	}

}
