package uk.org.whitecottage.gnosis.json;

public class JSONString extends JSONObject {
	protected String value;

	public JSONString() {
		super();
	}

	public JSONString(String value) {
		super();
		this.value = value;
	}

	public JSONString(String fieldName, String value) {
		super();
		this.fieldName = fieldName;
		this.value = value;
	}

	@Override
	public String toJSON() {
		String result = createFieldName();

		// Should probably make the string safe here...
		result += "\"" + safeJSONString(value) + "\"";
		
		return result;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
