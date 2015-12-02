package com.opl.junitme.alloy.model;

import java.util.LinkedList;
import java.util.List;

import com.opl.junitme.alloy.model.manager.AlloyInstanceManager;
import com.opl.junitme.alloy.utils.JunitMeUtils;
import com.opl.junitme.constants.AModelEnum;
import com.opl.junitme.constants.ASpecificationEnum;


/**
 * Represent the alloy method object.
 * @author Quentin
 *
 */
public class AMethod extends AModel {

	/**
	 * The type that contains this method.
	 */
	private AType receiverType;
	
	/**
	 * The type of the args.
	 */
	private final List<AType> paramTypes;
	
	/**
	 * Constructor.
	 * @param id the id of the method.
	 */
	public AMethod(final int id) {
		super(id, AModelEnum.METHOD);
		this.paramTypes = new LinkedList<AType>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSpecification(final ASpecificationEnum field, final String element) {
		switch(field){
		case PARAM_TYPES:
			this.paramTypes.add((AType) AlloyInstanceManager.getCurrentAlloyInstance().addElement(element));
			break;
		case RECEIVER_TYPE:
			this.receiverType =(AType) AlloyInstanceManager.getCurrentAlloyInstance().addElement(element);
		default:
			break;
		}
	}
	
	/**
	 * Get the qualified name of its class.
	 * @return the class qualified name.
	 */
	public String getClassQualifiedName(){
		String instanceName = this.getInstanceName();
		String classInstanceName = JunitMeUtils.getQClassFromAlloyMehtod(instanceName); 
		return JunitMeUtils.convertAlloyToJavaName(classInstanceName);
	}
	
	/**
	 * Get the simple name of its class.
	 * @return the class simple name.
	 */
	public String getClassSimpleName(){
		String instanceName = this.getInstanceName();
		String classInstanceName = JunitMeUtils.getClassFromAlloyMethod(instanceName); 
		return classInstanceName;
	}
	
	/**
	 * Get the simple name of it.
	 * @return the method simple name.
	 */
	public String getSimpleName(){
		String instanceName = this.getInstanceName();
		String classInstanceName = JunitMeUtils.getMethodFromAlloyMethod(instanceName); 
		return classInstanceName;
	}
	
}
