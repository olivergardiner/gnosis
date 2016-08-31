package uk.org.whitecottage.gnosis.backend;

import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public abstract class DataService implements Serializable {
	protected Properties properties;
	@SuppressWarnings("unused")
	private final static Logger LOGGER = Logger.getLogger(DataService.class.getName());

    public synchronized void init(Properties properties) {
    	this.properties = properties;
    }
}
