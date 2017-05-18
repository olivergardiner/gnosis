package uk.org.whitecottage.gnosis.ui.applications;

import com.vaadin.ui.ItemCaptionGenerator;

public class ClassificationBean implements ItemCaptionGenerator {
	private static final long serialVersionUID = 1L;
	protected String term;
	protected String termId;

	public ClassificationBean(String term, String termId) {
		this.term = term;
		this.termId = termId;
	}
	
	@Override
	public String apply(Object item) {
		// TODO Auto-generated method stub
		return null;
	}

}
