package com.opl.junitme.alloy.utils;

public class JunitMeUtils {

	/**
	 * Get the name of the Sig Alloy object.
	 * 
	 * @param sigLabel
	 *            the alloy sig lable.
	 * @return the object label.
	 */
	public static String getSigName(final String sigLabel) {
		return sigLabel.replace("this/", "");
	}

	/**
	 * From a string element$id, get the element part.
	 * 
	 * @param fullElement
	 *            the full element.
	 * @return the element part.
	 */
	public static String getElementName(final String fullElement) {
		return fullElement.split("\\$")[0];
	}

	/**
	 * From a string element$id, get the id part.
	 * 
	 * @param fullElement
	 *            the full element.
	 * @return the id part.
	 */
	public static int getElementId(final String fullElement) {
		if (fullElement.contains("$")) {
			String id = fullElement.split("\\$")[1];
			if (id != null && !id.isEmpty()) {
				return Integer.valueOf(id);
			}
		}
		return -1;
	}

	/**
	 * Check that the given string is an element instance.
	 * 
	 * @param element
	 *            the element to check
	 * @return <i>True</i> if it does, <i>False</i> otherwise.
	 */
	public static boolean isElement(final String element) {
		return element != null && element.contains("$");
	}

	/**
	 * Get the method name from an Alloy method label.
	 * 
	 * @param label
	 *            the Alloy label of the method.
	 * @return the method name.
	 */
	public static String getMethodFromAlloyMethod(final String label) {
		// Method name is after the last _
		String[] splitName = label.split("_");
		return getElementName(splitName[splitName.length - 2]);
	}

	/**
	 * Get the class name from an Alloy method label.
	 * 
	 * @param label
	 *            the Alloy label of the method.
	 * @return the class name.
	 */
	public static String getClassFromAlloyMethod(final String label) {
		// Method name is just before the method name
		String[] splitName = label.split("_");
		return getElementName(splitName[splitName.length - 3]);
	}

	/**
	 * Get the class qualified name from an Alloy method label.
	 * 
	 * @param label
	 *            the Alloy label of the method.
	 * @return the qualified name of the class.
	 */
	public static String getQClassFromAlloyMehtod(final String label) {
		String[] splitName = label.split("_");
		StringBuffer qClassName = new StringBuffer();
		for (int i = 0; i <= splitName.length - 3; i++) {
			if (i != 0) {
				qClassName.append("_");
			}
			qClassName.append(splitName[i]);
		}
		return getElementName(qClassName.toString());
	}

	/**
	 * Get the class qualified name from an Alloy method label.
	 * 
	 * @param label
	 *            the Alloy label of the method.
	 * @return the qualified name of the class.
	 */
	public static String getPackageFromAlloyMethod(final String label) {
		String[] splitName = label.split("_");
		StringBuffer packageName = new StringBuffer();
		for (int i = 0; i <= splitName.length - 4; i++) {
			if (i != 0) {
				packageName.append("_");
			}
			packageName.append(splitName[i]);
		}
		return getElementName(packageName.toString());
	}

	/**
	 * Get the class qualified name from an Alloy class label.
	 * 
	 * @param label
	 *            the Alloy label of the class.
	 * @return the class name the class.
	 */
	public static String getClassFromAlloyClass(final String label) {
		// Class name is after the last _
		String[] splitName = label.split("_");
		return getElementName(splitName[splitName.length - 1]);
	}

	/**
	 * Get the package name from an Alloy class label.
	 * 
	 * @param label
	 *            the Alloy label of the class.
	 * @return the package name the class.
	 */
	public static String getPackageFromAlloyClass(final String label) {
		// Method name is just before the last _
		String[] splitName = label.split("_");
		StringBuffer packageName = new StringBuffer();
		for (int i = 0; i <= splitName.length - 2; i++) {
			if (i != 0) {
				packageName.append("_");
			}
			packageName.append(splitName[i]);
		}
		return getElementName(packageName.toString());
	}

	/**
	 * Get the package name from an Alloy class label.
	 * 
	 * @param label
	 *            the Alloy label of the class.
	 * @return the package name the class.
	 */
	public static String convertAlloyToJavaName(final String alloyQualifiedName) {
		String toReturn = alloyQualifiedName.replaceAll("_", ".");
		if (toReturn.contains("$")) {
			return toReturn.substring(0, toReturn.lastIndexOf("$"));
		}
		return getElementName(toReturn);
	}

}