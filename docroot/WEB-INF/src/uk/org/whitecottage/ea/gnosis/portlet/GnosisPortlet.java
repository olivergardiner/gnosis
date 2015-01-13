package uk.org.whitecottage.ea.gnosis.portlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import uk.org.whitecottage.ea.portlet.ResourceActionPortlet;

public class GnosisPortlet extends ResourceActionPortlet {
	private static final Log log = LogFactoryUtil.getLog(GnosisPortlet.class);

	public Properties getProperties() {
		Properties properties = new Properties();

		try {
			FileInputStream input = new FileInputStream(new File(dataDir + "gnosis/gnosis.properties"));
			properties.load(input);
			input.close();
		} catch (FileNotFoundException e) {
			log.warn("Gnosis properties file not found");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		
		return properties;
	}

}
