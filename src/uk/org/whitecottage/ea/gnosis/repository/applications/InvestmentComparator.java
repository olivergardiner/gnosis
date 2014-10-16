package uk.org.whitecottage.ea.gnosis.repository.applications;

import java.util.Comparator;

import uk.org.whitecottage.ea.gnosis.jaxb.applications.Investment;

public class InvestmentComparator implements Comparator<Investment> {

	@Override
	public int compare(Investment inv1, Investment inv2) {
		if (inv1.getDate() == null) {
			if (inv2.getDate() == null) {
				return 0;
			} else {
				return -1;
			}
		} else if (inv2.getDate() == null) {
			return 1;
		}
		
		return inv1.getDate().compare(inv2.getDate());
	}
}
