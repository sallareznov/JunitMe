package com.opl.junitme.alloy.model;

import com.opl.junitme.constants.AModelEnum;
import com.opl.junitme.constants.ASpecificationEnum;


/**
 * Represent a type alloy.
 * @author Quentin
 *
 */
public class AType extends AModel {

	/**
	 * Constructor.
	 * @param id the id of the type.
	 */
	public AType(final int id) {
		super(id, AModelEnum.TYPE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSpecification(final ASpecificationEnum field, final String element) {
	}
	
}
