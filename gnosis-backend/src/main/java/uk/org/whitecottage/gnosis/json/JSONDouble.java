package uk.org.whitecottage.gnosis.json;

public class JSONDouble extends JSONObject {
	protected double value;

	public JSONDouble() {
		super();
	}

	public JSONDouble(double value) {
		super();
		this.value = value;
	}

	public JSONDouble(String fieldName, double value) {
		super();
		this.fieldName = fieldName;
		this.value = value;
	}

	@Override
	public String toJSON() {
		String result = createFieldName();

		result += "\"" + Double.toString(value) + "\"";
		
		return result;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
