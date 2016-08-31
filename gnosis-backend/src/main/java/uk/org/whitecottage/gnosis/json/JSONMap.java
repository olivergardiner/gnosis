package uk.org.whitecottage.gnosis.json;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JSONMap extends JSONObject {
	protected Map<String, JSONObject> value = new HashMap<String, JSONObject>();

	public JSONMap() {
		super();
	}

	public JSONMap(String fieldName) {
		super();
		this.fieldName = fieldName;
	}

	@Override
	public String toJSON() {
		String result = createFieldName();
		
		result += "{";
		
		String separator = "";
		for (String key: value.keySet()) {
			result += separator + value.get(key).toJSON();
			separator = ",";
		}

		result += "}";
		
		return result;
	}
	
	public int size() {
		return value.size();
	}

	public boolean isEmpty() {
		return value.isEmpty();
	}

	public boolean containsKey(String key) {
		return value.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return this.value.containsValue(value);
	}

	public JSONObject get(String key) {
		return value.get(key);
	}

	public JSONObject put(JSONObject value) {
		return this.value.put(value.getFieldName(), value);
	}

	public JSONObject remove(String key) {
		return value.remove(key);
	}

	public void clear() {
		value.clear();
	}

	public Set<String> keySet() {
		return value.keySet();
	}

}
