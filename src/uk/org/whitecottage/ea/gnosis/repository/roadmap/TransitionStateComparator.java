package uk.org.whitecottage.ea.gnosis.repository.roadmap;

import java.util.Comparator;

import uk.org.whitecottage.ea.gnosis.jaxb.roadmap.TransitionState;

public class TransitionStateComparator implements Comparator<TransitionState> {

	@Override
	public int compare(TransitionState ts1, TransitionState ts2) {
		if (ts1.getDate() == null) {
			if (ts2.getDate() == null) {
				return 0;
			} else {
				return -1;
			}
		} else if (ts2.getDate() == null) {
			return 1;
		}
		
		return ts1.getDate().compare(ts2.getDate());
	}
}
