package uk.org.whitecottage.gnosis.json;

public class JSONInteger extends JSONObject {
	protected int value;

	public JSONInteger() {
		super();
	}

	public JSONInteger(int value) {
		super();
		this.value = value;
	}

	public JSONInteger(String fieldName, int value) {
		super();
		this.fieldName = fieldName;
		this.value = value;
	}

	@Override
	public String toJSON() {
		String result = createFieldName();

		result += "\"" + Integer.toString(value) + "\"";
		
		return result;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
