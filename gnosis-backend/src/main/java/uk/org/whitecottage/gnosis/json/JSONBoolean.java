package uk.org.whitecottage.gnosis.json;

public class JSONBoolean extends JSONObject {
	protected boolean value;

	public JSONBoolean() {
		super();
	}

	public JSONBoolean(boolean value) {
		super();
		this.value = value;
	}

	public JSONBoolean(String fieldName, boolean value) {
		super();
		this.fieldName = fieldName;
		this.value = value;
	}

	@Override
	public String toJSON() {
		String result = createFieldName();

		result += (value) ? "true" : "false";
		
		return result;
	}

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

}
