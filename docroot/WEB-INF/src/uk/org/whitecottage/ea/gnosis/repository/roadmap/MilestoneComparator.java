package uk.org.whitecottage.ea.gnosis.repository.roadmap;

import java.util.Comparator;

import uk.org.whitecottage.ea.gnosis.jaxb.framework.Milestone;

public class MilestoneComparator implements Comparator<Milestone> {

	@Override
	public int compare(Milestone m1, Milestone m2) {
		if (m1.getDate() == null) {
			if (m2.getDate() == null) {
				return 0;
			} else {
				return -1;
			}
		} else if (m2.getDate() == null) {
			return 1;
		}
		
		return m1.getDate().compare(m2.getDate());
	}
}
