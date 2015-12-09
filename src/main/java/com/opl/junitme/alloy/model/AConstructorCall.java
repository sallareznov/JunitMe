package com.opl.junitme.alloy.model;

import java.util.LinkedList;
import java.util.List;

import com.opl.junitme.alloy.model.manager.AlloyInstanceManager;
import com.opl.junitme.constants.AModelEnum;
import com.opl.junitme.constants.ASpecificationEnum;

public class AConstructorCall extends AModel {

	private AMethod method;
	private List<AObject> params;

	public AConstructorCall(final int id) {
		super(id, AModelEnum.CONSTRUCTOR_CALL);
		params = new LinkedList<AObject>();
	}

	@Override
	public void setSpecification(ASpecificationEnum field, String element) {
		AModel createdElement = AlloyInstanceManager.getCurrentAlloyInstance().addElement(element);
		switch (field) {
		case PARAMS:
			this.params.add((AObject) createdElement);
			break;
		case METHOD:
			this.method = (AMethod) AlloyInstanceManager.getCurrentAlloyInstance().addElement(element,
					AModelEnum.METHOD);
			break;
		default:
			break;
		}
	}

	public AMethod getMethod() {
		return method;
	}

	public List<AObject> getParams() {
		return params;
	}

}
