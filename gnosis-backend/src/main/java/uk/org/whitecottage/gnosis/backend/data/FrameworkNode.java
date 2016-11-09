package uk.org.whitecottage.gnosis.backend.data;

import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

public class FrameworkNode implements Container.Hierarchical {

	private static final long serialVersionUID = 1L;

	@Override
	public Item getItem(Object itemId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<?> getContainerPropertyIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<?> getItemIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Property getContainerProperty(Object itemId, Object propertyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getType(Object propertyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean containsId(Object itemId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Item addItem(Object itemId) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object addItem() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAllItems() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<?> getChildren(Object itemId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getParent(Object itemId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<?> rootItemIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setParent(Object itemId, Object newParentId) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean areChildrenAllowed(Object itemId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRoot(Object itemId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasChildren(Object itemId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeItem(Object itemId) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return false;
	}

}
