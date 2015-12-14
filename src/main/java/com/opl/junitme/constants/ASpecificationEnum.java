package com.opl.junitme.constants;

public enum ASpecificationEnum {

	RECEIVER_TYPE("receiverType"), TYPE("type"), RECEIVER("receiver"), PARAMS(
			"params"), PARAM_TYPES("paramTypes"), METHOD("method"), METHODS(
			"methods"), NEXT_METHOD("nextMethod"), POSITION("pos"), NEXT_PARAM(
			"nextParam"), NEXT_PARAM_TYPE("nextParamType"), CONSTRUCTOR_CALL("constructor"), OBJECT("object");

	private final String value;

	private ASpecificationEnum(final String value) {
		this.value = value;
	}

	public static ASpecificationEnum getEnumFromValue(final String value) {
		for (ASpecificationEnum alloyModelEnum : ASpecificationEnum.values()) {
			if (alloyModelEnum.getValue().equals(value)) {
				return alloyModelEnum;
			}
		}
		return null;
	}

	private String getValue() {
		return this.value;
	}

}
