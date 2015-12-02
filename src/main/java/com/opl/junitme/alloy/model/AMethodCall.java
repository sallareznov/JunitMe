package com.opl.junitme.alloy.model;

import java.util.LinkedList;
import java.util.List;

import com.opl.junitme.alloy.model.manager.AlloyInstanceManager;
import com.opl.junitme.constants.AModelEnum;
import com.opl.junitme.constants.ASpecificationEnum;

/**
 * Represent the alloy Method call.
 * 
 * @author Quentin
 *
 */
public class AMethodCall extends ACallWithNext {

	/**
	 * The object which call this method.
	 */
	private AObject receiver;

	/**
	 * The type which call this method.
	 */
	private AType receiverType;
	
	/**
	 * The params of the method.
	 */
	private List<AObject> param;
	
	/**
	 * The called method of the object.
	 */
	private AMethod method;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            the id of the method call.
	 * 
	 */
	public AMethodCall(final int id) {
		this(id, false);
		this.param = new LinkedList<AObject>();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param id
	 *            the id of the method call.
	 * 
	 */
	public AMethodCall(final int id, final boolean isBeginner) {
		super(id, AModelEnum.METHOD_CALL, isBeginner);
	}

	@Override
	public void setSpecification(final ASpecificationEnum field,
			final String element) {
		super.setSpecification(field, element);
		AModel createdElement = AlloyInstanceManager.getCurrentAlloyInstance().addElement(element);
		switch (field) {
		case PARAMS:
			this.param.add((AObject) createdElement);
			break;
		case METHOD:
			this.method = (AMethod) AlloyInstanceManager.getCurrentAlloyInstance().addElement(element, AModelEnum.METHOD);
			break;
		case RECEIVER:
			this.receiver = (AObject) createdElement;
			break;
		case RECEIVER_TYPE:
			this.receiverType = (AType) createdElement;
			break;
		default:
			break;
		}
	}

	/**
	 * @return the receiver
	 */
	public AObject getReceiver() {
		return this.receiver;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(final AObject receiver) {
		this.receiver = receiver;
	}

	/**
	 * @return the receiverType
	 */
	public AType getReceiverType() {
		return this.receiverType;
	}

	/**
	 * @param receiverType the receiverType to set
	 */
	public void setReceiverType(final AType receiverType) {
		this.receiverType = receiverType;
	}

	/**
	 * @return the method
	 */
	public AMethod getMethod() {
		return this.method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(final AMethod method) {
		this.method = method;
	}

	/**
	 * @return the param
	 */
	public List<AObject> getParam() {
		return this.param;
	}

}
