package com.opl.junitme.constants;

public enum AGenType {

	INTEGER("Gen_Integer"),
	STRING("Gen_String"),
	BOOLEAN("Gen_Boolean"),
	FLOAT("Gen_Float"),
	DOUBLE("Gen_Double"),
	BYTE("Gen_Byte"),
	LONG("Gen_Long"),
	CHARACTER("Gen_Character"),
	SHORT("Gen_Short");

	private String value;

	/**
	 * Constructor.
	 * 
	 * @param value
	 *            the value of the enum.
	 */
	private AGenType(final String value) {
		this.value = value;
	}

	/**
	 * Get the enum from the given value.
	 * 
	 * @param value
	 *            the value to get enum with.
	 * @return the AModelEnum corresponding to the value.
	 */
	public static AGenType getEnumFromValue(final String value) {
		for (AGenType alloyModelEnum : AGenType.values()) {
			if (alloyModelEnum.getValue().equals(value)) {
				return alloyModelEnum;
			}
		}
		return null;
	}

	/**
	 * Get the value.
	 * 
	 * @return the value.
	 */
	private String getValue() {
		return this.value;
	}

}
