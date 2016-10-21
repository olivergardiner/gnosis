package uk.org.whitecottage.gnosis.backend.data.jaxb;

import uk.org.whitecottage.gnosis.backend.data.ClassificationBean;

@SuppressWarnings("serial")
public class ClassificationBeanJaxb extends ClassificationBean {

	public ClassificationBeanJaxb() {
		super();
	}

	public ClassificationBeanJaxb(String ecosystemId, String applicationId) {
		super(ecosystemId, applicationId);
	}
}
