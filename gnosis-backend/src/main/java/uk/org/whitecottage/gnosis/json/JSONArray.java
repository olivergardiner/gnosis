package uk.org.whitecottage.gnosis.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSONArray extends JSONObject {
	protected List<JSONObject> value = new ArrayList<JSONObject>();

	public JSONArray() {
		super();
	}

	public JSONArray(String fieldName) {
		super();
		this.fieldName = fieldName;
	}

	@Override
	public String toJSON() {
		String result = createFieldName();
		
		result += "[";
		
		String separator = "";
		for (JSONObject object: value) {
			result += separator + object.toJSON();
			separator = ",";
		}

		result += "]";
		
		return result;
	}

	public int size() {
		return value.size();
	}

	public boolean isEmpty() {
		return value.isEmpty();
	}

	public boolean contains(Object o) {
		return value.contains(o);
	}

	public Iterator<JSONObject> iterator() {
		return value.iterator();
	}

	public boolean add(JSONObject e) {
		return value.add(e);
	}

	public boolean remove(Object o) {
		return value.remove(o);
	}

	public void clear() {
		value.clear();
	}

	public JSONObject get(int index) {
		return value.get(index);
	}

	public void add(int index, JSONObject element) {
		value.add(index, element);
	}

	public JSONObject remove(int index) {
		return value.remove(index);
	}

	public int indexOf(Object o) {
		return value.indexOf(o);
	}

}
