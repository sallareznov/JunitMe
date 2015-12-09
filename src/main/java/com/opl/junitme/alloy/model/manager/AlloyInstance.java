package com.opl.junitme.alloy.model.manager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.opl.junitme.alloy.model.AConstructorCall;
import com.opl.junitme.alloy.model.AMethod;
import com.opl.junitme.alloy.model.AMethodCall;
import com.opl.junitme.alloy.model.AModel;
import com.opl.junitme.alloy.model.AObject;
import com.opl.junitme.alloy.model.AType;
import com.opl.junitme.alloy.utils.JunitMeUtils;
import com.opl.junitme.constants.AModelEnum;

/**
 * The factory of alloy models.
 * 
 * @author Quentin
 *
 */
public class AlloyInstance {

	/**
	 * Trace of the existing objects.
	 */
	private final Map<String, AModel> createdObjects;

	/**
	 * Constructor.
	 */
	protected AlloyInstance() {
		this.createdObjects = new HashMap<String, AModel>();
	}

	/**
	 * Create an instance of the given element represented in the alloy model.
	 * 
	 * @param element
	 *            the element to instantiate.
	 * @param aModelEnum
	 *            the aModelEnum if null it will be determined directly from the
	 *            element.
	 * @return the new instantiated element if it doesn't not already exist, the
	 *         stored one if it does.
	 */
	public AModel addElement(final String element,
			final AModelEnum modelEnum) {

		// Extract the informations about the alloy element
		String elementName = JunitMeUtils.getElementName(element);
		int elementId = JunitMeUtils.getElementId(element);

		// Check if the instance already exists.
		AModel instance = this.createdObjects.get(element);
		if (instance == null) {

			// Create the instance
			AModelEnum aModelEnum = modelEnum == null ? AModelEnum.getEnumFromValue(elementName) : modelEnum;
			if (aModelEnum != null) {
				switch (aModelEnum) {
				case METHOD:
					instance = new AMethod(elementId);
					break;
				case BEGIN:
					instance = new AMethodCall(elementId, true);
					break;
				case END:
				case METHOD_CALL:
					instance = new AMethodCall(elementId);
					break;
				case OBJECT:
					instance = new AObject(elementId);
					break;
				case TYPE:
					instance = new AType(elementId);
					break;
				case CONSTRUCTOR_CALL:
					instance = new AConstructorCall(elementId);
					break;
				default:
					break;
				}

				if(instance != null){
					instance.setInstanceName(element);
				}

				// Reference the instance.
				this.createdObjects.put(element, instance);
			}
		}

		return instance;
	}

	/**
	 * Create an instance of the given element represented in the alloy model.
	 * 
	 * @param element
	 *            the element to instantiate.
	 * @param id
	 *            the id.
	 * @return the new instantiated element if it doesn't not already exist, the
	 *         stored one if it does.
	 */
	public AModel addElement(final String element) {
		return this.addElement(element, null);
	}

	/**
	 * The call methods objects.
	 * 
	 * @return the createdInstances
	 */
	public List<AMethodCall> getCallMethodObjects() {
		List<AMethodCall> methodCallList = new LinkedList<AMethodCall>();
		if (this.createdObjects != null && !this.createdObjects.isEmpty()) {
			Set<Entry<String, AModel>> createdInstanceEntrySet = this.createdObjects
					.entrySet();
			for (Entry<String, AModel> createdInstanceEntry : createdInstanceEntrySet) {
				AModel value = createdInstanceEntry.getValue();
				if (value instanceof AMethodCall) {
					methodCallList.add((AMethodCall) value);
				}
			}
		}
		return methodCallList;
	}
	
	public List<AConstructorCall> getConstructorCalls() {
		final List<AConstructorCall> constructorCallList = new LinkedList<AConstructorCall>();
		if (this.createdObjects != null && !this.createdObjects.isEmpty()) {
			Set<Entry<String, AModel>> createdInstanceEntrySet = this.createdObjects
					.entrySet();
			for (Entry<String, AModel> createdInstanceEntry : createdInstanceEntrySet) {
				AModel value = createdInstanceEntry.getValue();
				if (value instanceof AConstructorCall) {
					constructorCallList.add((AConstructorCall) value);
				}
			}
		}
		return constructorCallList;
	}

	/**
	 * Get the first method call of this instance of Alloy.
	 * 
	 * @return the first method call of this instance of Alloy.
	 */
	public AMethodCall getFirstMethodCall() {
		for (AMethodCall methodCall : this.getCallMethodObjects()) {
			if (methodCall.isBeginner()) {
				return methodCall;
			}
		}
		return null;
	}
}
