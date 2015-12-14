package com.opl.junitme.alloy.model;

import java.util.HashSet;
import java.util.Set;

import com.opl.junitme.alloy.model.manager.AlloyInstanceManager;
import com.opl.junitme.alloy.utils.JunitMeUtils;
import com.opl.junitme.constants.AModelEnum;
import com.opl.junitme.constants.ASpecificationEnum;

/**
 * Represent an alloy model object.
 * @author Quentin
 *
 */
public class AObject extends AModel {

	/**
	 * The type of the object.
	 */
	private AType type;
	
	private AConstructorCall constructor;
	
	/**
	 * The method that the object contains.
	 */
	private final Set<AMethod> methods;
	
	/**
	 * Constructor.
	 * @param id the id of the object.
	 */
	public AObject(final int id) {
		super(id, AModelEnum.OBJECT);
		this.methods = new HashSet<AMethod>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSpecification(final ASpecificationEnum specification, final String element) {
		switch(specification){
		case TYPE:
			this.type = (AType) AlloyInstanceManager.getCurrentAlloyInstance().addElement(element);
			break;
		case CONSTRUCTOR_CALL:
			final AConstructorCall constructor = (AConstructorCall) AlloyInstanceManager.getCurrentAlloyInstance().addElement(element);
			this.constructor = constructor;
			break;
		case METHODS:
			AMethod method = (AMethod) AlloyInstanceManager.getCurrentAlloyInstance().addElement(element);
			this.methods.add(method);
			break;
		default:
			break;
		}
	}
	
	public String getQualifiedName(){
		String instanceName = this.type.getInstanceName();
		String javaQualifiedName = JunitMeUtils.convertAlloyToJavaName(instanceName); 
		return javaQualifiedName;
	}
	
	public String getSimpleName(){
		return JunitMeUtils.getClassFromAlloyClass(this.type.getInstanceName());
	}

	/**
	 * @return the type
	 */
	public AType getType() {
		return this.type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(final AType type) {
		this.type = type;
	}
	
	public AConstructorCall getConstructorCall() {
		return constructor;
	}
	
	public void setConstructorCall(AConstructorCall constructor) {
		this.constructor = constructor;
	}

	/**
	 * @return the methods
	 */
	public Set<AMethod> getMethods() {
		return this.methods;
	}
	
}
