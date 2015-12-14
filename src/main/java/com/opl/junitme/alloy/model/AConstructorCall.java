package com.opl.junitme.alloy.model;

import java.util.LinkedList;
import java.util.List;

import com.opl.junitme.alloy.model.manager.AlloyInstanceManager;
import com.opl.junitme.constants.AModelEnum;
import com.opl.junitme.constants.ASpecificationEnum;

public class AConstructorCall extends AModel {

	private AMethod method;
	private List<AType> paramTypes;

	public AConstructorCall(final int id) {
		super(id, AModelEnum.CONSTRUCTOR_CALL);
		paramTypes = new LinkedList<AType>();
	}

	@Override
	public void setSpecification(ASpecificationEnum field, String element) {
		AModel createdElement = AlloyInstanceManager.getCurrentAlloyInstance().addElement(element);
		switch (field) {
		case PARAM_TYPES:
			this.paramTypes.add((AType) createdElement);
			break;
		default:
			break;
		}
	}

	public AMethod getMethod() {
		return method;
	}

	public List<AType> getParamTypes() {
		return paramTypes;
	}

}
