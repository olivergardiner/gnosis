package uk.org.whitecottage.gnosis.json;

public abstract class JSONObject {
	protected String fieldName = null;

	public JSONObject() {
		// TODO Auto-generated constructor stub
	}

	public abstract String toJSON();
	
	protected String createFieldName() {
		String result = "";

		if (fieldName != null) {
			result = "\"" + fieldName + "\":";
		}
		
		return result;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String safeJSONString(String value) {
		if (value == null) {
			return "";
		}
		
		return value.replace("\"", "\\\"");
	}
}
