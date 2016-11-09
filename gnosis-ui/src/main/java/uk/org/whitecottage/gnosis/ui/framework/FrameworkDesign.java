package uk.org.whitecottage.gnosis.ui.framework;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

@DesignRoot
@SuppressWarnings("serial")
public class FrameworkDesign extends VerticalLayout {
	protected FrameworkTree frameworkTree;
	
	public FrameworkDesign() {
		Design.read(this);
		
		frameworkTree.addContainerProperty("Name", String.class, null);
		frameworkTree.addContainerProperty("Description", String.class, null);

		frameworkTree.addItem(new Object[]{"Menu", null}, 0);
		frameworkTree.addItem(new Object[]{"Beverages", null}, 1);
		frameworkTree.setParent(1, 0);
		frameworkTree.addItem(new Object[]{"Foods", null}, 2);
		frameworkTree.setParent(2, 0);
		frameworkTree.addItem(new Object[]{"Coffee", "A hot drink"}, 3);
		frameworkTree.addItem(new Object[]{"Tea", "Another hot drink"}, 4);
		frameworkTree.setParent(3, 1);
		frameworkTree.setParent(4, 1);
		frameworkTree.addItem(new Object[]{"Bread", "Flour and water"}, 5);
		frameworkTree.addItem(new Object[]{"Cake", "Flour, sugar, butter and eggs"}, 6);
		frameworkTree.setParent(5, 2);
		frameworkTree.setParent(6, 2);

		for (Object itemId: frameworkTree.getContainerDataSource()
                .getItemIds()) {
			frameworkTree.setCollapsed(itemId, false);

			if (!frameworkTree.hasChildren(itemId))
				frameworkTree.setChildrenAllowed(itemId, false);
		}
	}
}
