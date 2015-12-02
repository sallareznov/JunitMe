package com.opl.junitme.alloy.model;

import com.opl.junitme.constants.AModelEnum;
import com.opl.junitme.constants.ASpecificationEnum;

/**
 * Abstract class of an alloy object.
 * @author Quentin
 *
 */
public abstract class AModel {

	/**
	 * The id of the instance.
	 */
	private final int id;

	/**
	 * The name of the instance. Sometimes the object in alloy does not contains
	 * any fields but it is just used to associate a type to an other object. So
	 * this field can be interpreted as the name of the object which extend this
	 * class.
	 */
	private String instanceName;

	/**
	 * The alloy model type.
	 */
	private final AModelEnum alloyModelType;

	/**
	 * Constructor.
	 * 
	 * @param id the id of the model.
	 */
	protected AModel(final int id, final AModelEnum alloyModelEnum) {
		this.id = id;
		this.alloyModelType = alloyModelEnum;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Set the specification value.
	 * 
	 * @param field the field of the object to set.
	 * @param element the element containing informations to inject.
	 */
	public abstract void setSpecification(final ASpecificationEnum field,
			final String element);

	/**
	 * @return the alloyModelType
	 */
	public AModelEnum getAlloyModelType() {
		return this.alloyModelType;
	}

	/**
	 * @param instanceName the instanceName to set
	 */
	public void setInstanceName(final String instanceName) {
		this.instanceName = instanceName;
	}

	/**
	 * @return the instanceName
	 */
	public String getInstanceName() {
		return this.instanceName;
	}

}
