package com.opl.junitme.constants;

public enum AModelEnum {

	OBJECT("Object"), METHOD_CALL("MethodCall"), CALL_WITH_NEXT("CallWithNext"), BEGIN("Begin"), END("End"), METHOD(
			"Method"), TYPE("Type"), PARAM_TYPE("ParamType"), PARAM("Param"), CONSTRUCTOR_CALL("ConstructorCall");

	private String value;

	/**
	 * Constructor.
	 * 
	 * @param value
	 *            the value of the enum.
	 */
	private AModelEnum(final String value) {
		this.value = value;
	}

	/**
	 * Get the enum from the given value.
	 * 
	 * @param value
	 *            the value to get enum with.
	 * @return the AModelEnum corresponding to the value.
	 */
	public static AModelEnum getEnumFromValue(final String value) {
		for (AModelEnum alloyModelEnum : AModelEnum.values()) {
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
