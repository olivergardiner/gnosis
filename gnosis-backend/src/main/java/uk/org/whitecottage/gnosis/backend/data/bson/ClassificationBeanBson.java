package uk.org.whitecottage.gnosis.backend.data.bson;

import uk.org.whitecottage.gnosis.backend.data.ClassificationBean;

@SuppressWarnings("serial")
public class ClassificationBeanBson extends ClassificationBean {

	public ClassificationBeanBson() {
		super();
	}

	public ClassificationBeanBson(String ecosystemId, String applicationId) {
		super(ecosystemId, applicationId);
	}
}
