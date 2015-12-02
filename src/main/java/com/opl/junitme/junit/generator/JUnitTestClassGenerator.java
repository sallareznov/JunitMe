package com.opl.junitme.junit.generator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.opl.junitme.alloy.model.AMethodCall;
import com.opl.junitme.alloy.model.AObject;
import com.opl.junitme.constants.AGenType;
import com.opl.junitme.constants.Constants;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

/**
 * Class to generate JUnit Test class.
 * 
 * @author Quentin
 *
 */
public class JUnitTestClassGenerator {

	/**
	 * The path of the class to generate.
	 */
	private final String junitClassPath;

	/**
	 * The method name prefix.
	 */
	private final String methodNamePrefix;

	/**
	 * The method name suffix.
	 */
	private final String methodNameSuffix;

	/**
	 * The code model corresponding to the class.
	 */
	private final JCodeModel codeModel;

	/**
	 * The defined class corresponding to the class.
	 */
	private final JDefinedClass definedClass;

	/**
	 * The list of the JUnit test methods.
	 */
	private final List<JMethod> methodList;

	/**
	 * Class by class qualified name to avoid qualified name in the generated
	 * code.
	 */
	private final Map<String, JClass> classByQName;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            the name of the JUnit class to generate.
	 * @throws JClassAlreadyExistsException
	 *             If the generated class already exists.
	 */
	public JUnitTestClassGenerator(final String name) throws JClassAlreadyExistsException {
		this(name, Constants.Configuration.METHOD_NAME_PREFIX, Constants.Configuration.METHOD_NAME_SUFFIX);
	}

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            the name of the JUnit class to generate.
	 * @param path
	 *            the path of the JUnit class to generate. If the generated
	 *            class already exists.
	 * @throws JClassAlreadyExistsException
	 */
	public JUnitTestClassGenerator(final String name, final String path) throws JClassAlreadyExistsException {
		this(name, path, Constants.Configuration.METHOD_NAME_PREFIX, Constants.Configuration.METHOD_NAME_SUFFIX);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            the name of the JUnit class to generate.
	 * @param methodNamePrefix
	 *            the method name prefix.
	 * @param methodNameSuffix
	 *            the method name suffix.
	 * @throws JClassAlreadyExistsException
	 *             If the generated class already exists.
	 */
	public JUnitTestClassGenerator(final String name, final String methodNamePrefix, final String methodNameSuffix)
			throws JClassAlreadyExistsException {
		this(name, Constants.Configuration.JUNIT_CLASSES_PATH, methodNamePrefix, methodNameSuffix);
	}

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            the name of the JUnit class to generate.
	 * @param path
	 *            the path of the JUnit class to generate.
	 * @param methodNamePrefix
	 *            the method name prefix.
	 * @param methodNameSuffix
	 *            the mcodemodelethod name suffix.
	 * @throws JClassAlreadyExistsException
	 *             If the generated class already exists.
	 */
	public JUnitTestClassGenerator(final String name, final String path, final String methodNamePrefix,
			final String methodNameSuffix) throws JClassAlreadyExistsException {

		// Initialize configuration
		this.junitClassPath = path;
		this.methodNamePrefix = methodNamePrefix;
		this.methodNameSuffix = methodNameSuffix;
		this.methodList = new LinkedList<JMethod>();
		this.classByQName = new HashMap<String, JClass>();

		// Initialize generation
		this.codeModel = new JCodeModel();
		this.definedClass = this.codeModel._class(name);
	}

	/**
	 * Create a new JUnit test method into the class.
	 * 
	 * @param methodCall
	 *            the method call object containing methods to call.
	 */
	public void createTestMethod(final AMethodCall methodCall) {
		this.createTestMethod(this.methodNamePrefix + this.methodList.size() + this.methodNameSuffix, methodCall);
	}

	/**
	 * Create a new JUnit test method into the class.
	 * 
	 * @param name
	 *            the name of the test method to create.
	 * @param methodCall
	 *            the method call object containing methods to call.
	 */
	public void createTestMethod(final String name, final AMethodCall methodCall) {

		if (methodCall != null && methodCall.isBeginner()) {

			// Create the method
			JMethod method = this.definedClass.method(JMod.PUBLIC, void.class, name);
			method.annotate(this.codeModel.ref("org.junit.Test"));
			JBlock methodBody = method.body();
			this.methodList.add(method);

			// Declare all needed variables
			Map<String, JVar> jvarByIName = this.declareVariables(methodCall, methodBody);

			// Generate method calls between variables
			AMethodCall currentMethodCall = methodCall;
			while ((currentMethodCall = currentMethodCall.getNext()) != null && !currentMethodCall.isEnd()) {

				// Variables : RECEIVER
				AObject receiver = currentMethodCall.getReceiver();
				JVar jvarReceiver = jvarByIName.get(receiver.getInstanceName());

				// Prepare the method call
				String methodName = currentMethodCall.getMethod().getSimpleName();
				JInvocation methodInvocation = jvarReceiver.invoke(methodName);

				// Variables : Parameters
				List<AObject> params = currentMethodCall.getParam();
				if (params != null && !params.isEmpty()) {
					for (AObject param : params) {
						JVar jvarParam = jvarByIName.get(param.getInstanceName());
						methodInvocation.arg(jvarParam);
					}
				}

				// Add the method invocation to the body
				methodBody.add(methodInvocation);

			}

		}
	}

	public JClass getJClass(final String qualifiedName) {
		JClass jClass = this.classByQName.get(qualifiedName);
		if (jClass == null) {
			jClass = this.codeModel.ref(qualifiedName);
			this.classByQName.put(qualifiedName, jClass);
		}
		return jClass;
	}

	/**
	 * Generate the JUnit class.
	 * 
	 * @throws IOException
	 */
	public void generateClass() throws IOException {
		this.generateClass(this.junitClassPath);
	}

	/**
	 * Generate the JUnit class.
	 * 
	 * @param path
	 *            the path of the generated JUnitClass.
	 * @throws IOException
	 */
	public void generateClass(final String path) throws IOException {
		File file = new File(path);
		file.mkdirs();
		this.codeModel.build(file);
	}

	/**
	 * Declare all the variables for the given method call.
	 * 
	 * @param methodCall
	 *            the method call containing the variables to declare.
	 * @return all variables according instances names.
	 */
	public Map<String, JVar> declareVariables(final AMethodCall methodCall, final JBlock methodBody) {

		Map<String, JVar> jvarByIName = new HashMap<String, JVar>();

		AMethodCall currentMethodCall = methodCall;
		while ((currentMethodCall = currentMethodCall.getNext()) != null && !currentMethodCall.isEnd()) {

			// Create variables : RECEIVER
			AObject receiver = currentMethodCall.getReceiver();
			String receiverIName = receiver.getInstanceName();
			if (!jvarByIName.containsKey(receiverIName)) {
				JVar jvar = this.createVariable(receiver, methodBody);
				jvarByIName.put(receiverIName, jvar);
			}

			// Create variables : Parameters
			List<AObject> params = currentMethodCall.getParam();
			if (params != null && !params.isEmpty()) {
				for (AObject param : params) {
					String paramInstanceName = param.getInstanceName();
					if (!jvarByIName.containsKey(paramInstanceName)) {
						JVar jvar = this.createVariable(param, methodBody);
						jvarByIName.put(paramInstanceName, jvar);
					}
				}
			}
		}
		return jvarByIName;
	}

	/**
	 * Create a variable into the specified method body.
	 * 
	 * @param aobject
	 *            the object to create a variable with;
	 * @param methodBody
	 *            the method body.
	 * @return the created variable.
	 */
	public JVar createVariable(final AObject aobject, final JBlock methodBody) {
		JVar jvar = null;
		if ((jvar = createPrimitiveType(aobject, methodBody)) == null) {
			String qClassName = aobject.getQualifiedName();
			String className = aobject.getSimpleName();

			JClass jclass = this.getJClass(qClassName);
			String varName = (className + "_" + aobject.getId()).toLowerCase();
			jvar = methodBody.decl(jclass, varName);
			jvar.init(JExpr._new(jclass));

		}
		return jvar;
	}

	/**
	 * Create a variable into the specified method body.
	 * 
	 * @param aobject
	 *            the object to create a variable with;
	 * @param methodBody
	 *            the method body.
	 * @return the created variable.
	 */
	public JVar createPrimitiveType(final AObject aobject, final JBlock methodBody) {
		String instanceName = aobject.getType().getInstanceName();
		String type = instanceName.split("\\$")[0];
		System.out.println(type);
		AGenType genType = AGenType.getEnumFromValue(type);

		Random r = new Random();

		JVar jvar = null;
		String className = aobject.getSimpleName();
		String varName = (className + "_" + aobject.getId()).toLowerCase();

		if (genType != null) {
			switch (genType) {
			case BOOLEAN:
				jvar = methodBody.decl(this.codeModel._ref(boolean.class), varName).init(JExpr.lit(r.nextBoolean()));
				break;
			case DOUBLE:
				jvar = methodBody.decl(this.codeModel._ref(double.class), varName).init(JExpr.lit(r.nextDouble()));
				break;
			case FLOAT:
				jvar = methodBody.decl(this.codeModel._ref(float.class), varName).init(JExpr.lit(r.nextFloat()));
				break;
			case INTEGER:
				jvar = methodBody.decl(this.codeModel._ref(int.class), varName).init(JExpr.lit(r.nextInt()));
				break;
			case STRING:
				jvar = methodBody.decl(this.codeModel.ref(String.class), varName).init(JExpr.lit("stringTest"));
				break;
			default:
				break;
			}
		}
		return jvar;
	}

}