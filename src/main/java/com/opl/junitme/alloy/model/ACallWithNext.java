package com.opl.junitme.alloy.model;

import com.opl.junitme.alloy.model.manager.AlloyInstanceManager;
import com.opl.junitme.constants.AModelEnum;
import com.opl.junitme.constants.ASpecificationEnum;


public abstract class ACallWithNext extends AModel {


	/**
	 * The next method call to call after this one.
	 */
	private AMethodCall next;
	
	/**
	 * If this call is the beginner.
	 */
	private boolean isBeginner;
	
	protected ACallWithNext(final int id, final AModelEnum alloyModelEnum) {
		super(id, alloyModelEnum);
	}

	/**
	 * Constructor.
	 * @param id the id of the call with next.
	 */
	protected ACallWithNext(final int id, final AModelEnum alloyModelEnum, final boolean isBeginner) {
		this(id, alloyModelEnum);
		this.isBeginner = isBeginner;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSpecification(final ASpecificationEnum field, final String element) {
		switch (field) {
		case NEXT_METHOD:
			this.next = (AMethodCall) AlloyInstanceManager.getCurrentAlloyInstance().addElement(element);
			break;
		default:
			break;
		}
	}
	
	/**
	 * If this call is the end.
	 * @return <i>True</i> if it does, <i>False</i> otherwise.
	 */
	public boolean isEnd(){
		return this.next == null;
	}

	/**
	 * @return the next
	 */
	public AMethodCall getNext() {
		return this.next;
	}

	/**
	 * @return the isBeginner
	 */
	public boolean isBeginner() {
		return this.isBeginner;
	}

	/**
	 * @param isBeginner the isBeginner to set
	 */
	public void setBeginner(final boolean isBeginner) {
		this.isBeginner = isBeginner;
	}
	
}
