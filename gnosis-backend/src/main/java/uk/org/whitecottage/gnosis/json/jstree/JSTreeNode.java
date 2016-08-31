package uk.org.whitecottage.gnosis.json.jstree;

import uk.org.whitecottage.gnosis.json.JSONArray;
import uk.org.whitecottage.gnosis.json.JSONMap;
import uk.org.whitecottage.gnosis.json.JSONString;

public class JSTreeNode extends JSONMap {

	public JSTreeNode() {
		// TODO Auto-generated constructor stub
	}

	public JSTreeNode(String text, String type) {
		JSONString textJSON = new JSONString("text", text);
		put(textJSON);
		
		JSONString typeJSON = new JSONString("type", type);
		put(typeJSON);
		
		JSONArray childrenJSON = new JSONArray("children");
		put(childrenJSON);
	}

	public JSTreeNode(String text, String type, JSONMap data) {
		JSONString textJSON = new JSONString("text", text);
		put(textJSON);
		
		JSONString typeJSON = new JSONString("type", type);
		put(typeJSON);
		
		data.setFieldName("data");
		put(data);
		
		JSONArray childrenJSON = new JSONArray("children");
		put(childrenJSON);
	}

	public JSONArray getChildren() {
		return (JSONArray) get("children");
	}
}
