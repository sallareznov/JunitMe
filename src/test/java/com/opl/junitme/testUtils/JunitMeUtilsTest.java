package com.opl.junitme.testUtils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.opl.junitme.alloy.utils.JunitMeUtils;

public class JunitMeUtilsTest {

	private final static String METHOD_NAME = "setSpecification";
	private final static String CLASS_NAME = "AModel";
	private final static String PACKAGE_NAME = "com_opl_junitme";

	private final static String ALLOY_SIG = "this/" + CLASS_NAME;
	private final static String ALLOY_INSTANCE = CLASS_NAME + "$0";
	private final static String ALLOY_CLASS = JunitMeUtilsTest.PACKAGE_NAME
			+ "_" + JunitMeUtilsTest.CLASS_NAME;
	private final static String ALLOY_METHOD = JunitMeUtilsTest.ALLOY_CLASS
			+ "_" + JunitMeUtilsTest.METHOD_NAME + "_";

	private final static String JAVA_PACKAGE_NAME = "com.opl.junitme";

	@Test
	public void testGetSigName() {
		String className = JunitMeUtils.getSigName(JunitMeUtilsTest.ALLOY_SIG);
		assertEquals(CLASS_NAME, className);
	}

	@Test
	public void testGetElementName() {
		String elementName = JunitMeUtils
				.getElementName(JunitMeUtilsTest.ALLOY_INSTANCE);
		assertEquals(CLASS_NAME, elementName);
	}

	@Test
	public void testGetElementId() {
		int elementId = JunitMeUtils
				.getElementId(JunitMeUtilsTest.ALLOY_INSTANCE);
		assertEquals(0, elementId);
	}

	@Test
	public void testGetMethodFromAlloyMethod() {
		String methodName = JunitMeUtils
				.getMethodFromAlloyMethod(JunitMeUtilsTest.ALLOY_METHOD);
		assertEquals(METHOD_NAME, methodName);
	}

	@Test
	public void testGetClassFromAlloyMethod() {
		String className = JunitMeUtils
				.getClassFromAlloyMethod(JunitMeUtilsTest.ALLOY_METHOD);
		assertEquals(CLASS_NAME, className);
	}

	@Test
	public void testGetQClassFromAlloyMehtod() {
		String qualifiedName = JunitMeUtils
				.getQClassFromAlloyMehtod(JunitMeUtilsTest.ALLOY_METHOD);
		assertEquals(ALLOY_CLASS, qualifiedName);
	}

	@Test
	public void testGetPackageFromAlloyMethod() {
		String packageName = JunitMeUtils
				.getPackageFromAlloyMethod(JunitMeUtilsTest.ALLOY_METHOD);
		assertEquals(PACKAGE_NAME, packageName);
	}

	@Test
	public void testGetClassFromAlloyClass() {
		String className = JunitMeUtils
				.getClassFromAlloyClass(JunitMeUtilsTest.ALLOY_CLASS);
		assertEquals(CLASS_NAME, className);
	}

	@Test
	public void testGetPackageFromAlloyClass() {
		String packageName = JunitMeUtils
				.getPackageFromAlloyClass(JunitMeUtilsTest.ALLOY_CLASS);
		assertEquals(PACKAGE_NAME, packageName);
	}

	@Test
	public void testConvertAlloyToJavaName() {
		String packageName = JunitMeUtils
				.convertAlloyToJavaName(JunitMeUtilsTest.PACKAGE_NAME);
		assertEquals(JAVA_PACKAGE_NAME, packageName);
	}

}
