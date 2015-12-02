package com.opl.junitme.alloy.model.manager;

import java.util.ArrayList;

public class AlloyInstanceManager extends ArrayList<AlloyInstance> {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 8182150489935309544L;
	
	/**
	 * Singleton instance.
	 */
	private static AlloyInstanceManager INSTANCE = new AlloyInstanceManager();
	
	/**
	 * Current alloy instance.
	 */
	private static AlloyInstance CURRENT_ALLOY_INSTANCE;

	/**
	 * Get the current instance. 
	 * @return the current instance of the alloy instance manager.
	 */
	public static AlloyInstanceManager getInstance(){
		return INSTANCE;
	}
	
	/**
	 * Create a new instance.
	 * @return the new instance of the instance manager.
	 */
	public static AlloyInstanceManager createInstance(){
		CURRENT_ALLOY_INSTANCE = new AlloyInstance();
		return INSTANCE = new AlloyInstanceManager();
	}
	
	/**
	 * Create a	new AlloyInstance and return it.
	 * @return the newly created alloy instance.
	 */
	public static AlloyInstance newAlloyInstance() {
		CURRENT_ALLOY_INSTANCE = new AlloyInstance();
		INSTANCE.add(CURRENT_ALLOY_INSTANCE);
		return CURRENT_ALLOY_INSTANCE;
	}
	
	/**
	 * Get the current alloy instance.
	 * @return the current alloy instance.
	 */
	public static AlloyInstance getCurrentAlloyInstance(){
		return CURRENT_ALLOY_INSTANCE;
	}

}
