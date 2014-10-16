package uk.org.whitecottage.ea.gnosis.repository.applications;

import java.util.Comparator;

import uk.org.whitecottage.ea.gnosis.jaxb.applications.Stage;

public class StageComparator implements Comparator<Stage> {

	@Override
	public int compare(Stage stage1, Stage stage2) {
		if (stage1.getDate() == null) {
			if (stage2.getDate() == null) {
				return 0;
			} else {
				return -1;
			}
		} else if (stage2.getDate() == null) {
			return 1;
		}
		
		return stage1.getDate().compare(stage2.getDate());
	}
}
