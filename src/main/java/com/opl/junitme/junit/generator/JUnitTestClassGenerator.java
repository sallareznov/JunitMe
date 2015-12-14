package com.opl.junitme.junit.generator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.opl.junitme.alloy.model.AConstructorCall;
import com.opl.junitme.alloy.model.AMethodCall;
import com.opl.junitme.alloy.model.AObject;
import com.opl.junitme.alloy.model.AType;
import com.opl.junitme.constants.AGenType;
import com.opl.junitme.constants.Constants;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCatchBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JTryBlock;
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
	public JUnitTestClassGenerator(final String name)
			throws JClassAlreadyExistsException {
		this(name, Constants.Configuration.METHOD_NAME_PREFIX,
				Constants.Configuration.METHOD_NAME_SUFFIX);
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
	public JUnitTestClassGenerator(final String name, final String path)
			throws JClassAlreadyExistsException {
		this(name, path, Constants.Configuration.METHOD_NAME_PREFIX,
				Constants.Configuration.METHOD_NAME_SUFFIX);
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
	public JUnitTestClassGenerator(final String name,
			final String methodNamePrefix, final String methodNameSuffix)
			throws JClassAlreadyExistsException {
		this(name, Constants.Configuration.JUNIT_CLASSES_PATH,
				methodNamePrefix, methodNameSuffix);
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
	public JUnitTestClassGenerator(final String name, final String path,
			final String methodNamePrefix, final String methodNameSuffix)
			throws JClassAlreadyExistsException {

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
	 * @throws ClassNotFoundException
	 */
	public void createTestMethod(final AMethodCall methodCall,
			List<AConstructorCall> constructorsCalls)
			throws ClassNotFoundException {
		this.createTestMethod(this.methodNamePrefix + this.methodList.size()
				+ this.methodNameSuffix, methodCall, constructorsCalls);
	}

	/**
	 * Create a new JUnit test method into the class.
	 * 
	 * @param name
	 *            the name of the test method to create.
	 * @param methodCall
	 *            the method call object containing methods to call.
	 * @throws ClassNotFoundException
	 */
	public void createTestMethod(final String name,
			final AMethodCall methodCall,
			List<AConstructorCall> constructorsCalls)
			throws ClassNotFoundException {

		if (methodCall != null && methodCall.isBeginner()) {

			// Create the method
			JMethod method = this.definedClass.method(JMod.PUBLIC, void.class,
					name);
			method.annotate(this.codeModel.ref("org.junit.Test"));
			method._throws(Exception.class);
			JBlock methodBody = method.body();
			this.methodList.add(method);

			JTryBlock tryBlock = methodBody._try();
			// tryBlock._catch(exception)

			// Declare all needed variables
			Map<String, JVar> jvarByIName = this.declareVariables(methodCall,
					tryBlock.body(), constructorsCalls);

			// Generate method calls between variables
			AMethodCall currentMethodCall = methodCall;
			while ((currentMethodCall = currentMethodCall.getNext()) != null
					&& !currentMethodCall.isEnd()) {
				// Variables : RECEIVER
				AObject receiver = currentMethodCall.getReceiver();
				JVar jvarReceiver = jvarByIName.get(receiver.getInstanceName());

				// Prepare the method call
				String methodName = currentMethodCall.getMethod()
						.getSimpleName();
				JInvocation methodInvocation = jvarReceiver.invoke(methodName);

				// Variables : Parameters
				List<AObject> params = currentMethodCall.getParam();
				if (params != null && !params.isEmpty()) {
					for (AObject param : params) {
						JVar jvarParam = jvarByIName.get(param
								.getInstanceName());
						methodInvocation.arg(jvarParam);
					}
				}

				// Add the method invocation to the body
				tryBlock.body().add(methodInvocation);

			}
			final JClass exceptionClass = codeModel
					.directClass("java.lang.Exception");
			final JCatchBlock catchBlock = tryBlock._catch(exceptionClass);
			catchBlock.body().directStatement(
					"ExceptionsLogger.logException(_x);");
			catchBlock.body()._throw(JExpr.direct("_x"));
		}
	}

	public void createBeforeClassMethod(String pathname) {
		JMethod method = this.definedClass.method(JMod.PUBLIC | JMod.STATIC,
				void.class, "initExceptionBuilder");
		method.annotate(this.codeModel.ref("org.junit.BeforeClass"));
		method._throws(IOException.class);
		JBlock methodBody = method.body();
		methodBody.directStatement("ExceptionsLogger.initLogFile(\""
				+ pathname + "\", \"GeneratedClassTest\");");
		this.methodList.add(method);
	}

	public void createAfterClassMethod() {
		JMethod method = this.definedClass.method(JMod.PUBLIC | JMod.STATIC,
				void.class, "closeExceptionBuilder");
		method.annotate(this.codeModel.ref("org.junit.AfterClass"));
		method._throws(IOException.class);
		JBlock methodBody = method.body();
		methodBody.directStatement("ExceptionsLogger.closeLogFile();");
		this.methodList.add(method);
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
	public Map<String, JVar> declareVariables(final AMethodCall methodCall,
			final JBlock methodBody, List<AConstructorCall> constructorsCalls) {
		Map<String, JVar> jvarByIName = new HashMap<String, JVar>();

		AMethodCall currentMethodCall = methodCall;
		while ((currentMethodCall = currentMethodCall.getNext()) != null
				&& !currentMethodCall.isEnd()) {

			// Create variables : RECEIVER
			AObject receiver = currentMethodCall.getReceiver();
			String receiverIName = receiver.getInstanceName();
			if (!jvarByIName.containsKey(receiverIName)) {
				JVar jvar = this.createVariable(receiver, methodBody,
						constructorsCalls);
				jvarByIName.put(receiverIName, jvar);
			}

			// Create variables : Parameters
			List<AObject> params = currentMethodCall.getParam();
			if (params != null && !params.isEmpty()) {
				for (AObject param : params) {
					String paramInstanceName = param.getInstanceName();
					if (!jvarByIName.containsKey(paramInstanceName)) {
						JVar jvar = this.createVariable(param, methodBody,
								constructorsCalls);
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
	public JVar createVariable(final AObject aobject, final JBlock methodBody,
			List<AConstructorCall> constructorsCalls) {
		JVar jvar = null;
		if ((jvar = createPrimitiveType(aobject, methodBody)) == null) {
			String qClassName = aobject.getQualifiedName();
			String className = aobject.getSimpleName();

			JClass jclass = this.getJClass(qClassName);
			String varName = (className + "_" + aobject.getId()).toLowerCase();
			jvar = methodBody.decl(jclass, varName);
			final JInvocation newClass = JExpr._new(jclass);
			for (final AConstructorCall call : constructorsCalls) {
				if (call.getInstanceName().contains(
						aobject.getSimpleName() + "_init")) {
					for (final AType param : call.getParamTypes()) {
						String type = param.getInstanceName().split("\\$")[0];
						if (type.equals("java_lang_String")) {
							newClass.arg(JExpr.lit("test"));
						} else {
							AGenType genType = AGenType.getEnumFromValue(type);
							if (genType == null) {
								newClass.arg(JExpr._null());
							} else {
								switch (genType) {
								case CHARACTER:
									newClass.arg(JExpr.lit('a'));
									break;
								case BOOLEAN:
									newClass.arg(JExpr.lit(false));
									break;
								case DOUBLE:
									newClass.arg(JExpr.lit(0));
									break;
								case FLOAT:
									newClass.arg(JExpr.lit(0));
									break;
								case INTEGER:
									newClass.arg(JExpr.lit(0));
									break;
								case STRING:
									newClass.arg(JExpr.lit("blabla"));
									break;
								case LONG:
									newClass.arg(JExpr.lit(0));
									break;
								case SHORT:
									newClass.arg(JExpr.lit(0));
									break;
								case BYTE:
									newClass.arg(JExpr.lit(0));
									break;
								default:
									break;
								}
							}
						}
					}
				}
			}
			jvar.init(newClass);

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
	public JVar createPrimitiveType(final AObject aobject,
			final JBlock methodBody) {
		String instanceName = aobject.getType().getInstanceName();
		String type = instanceName.split("\\$")[0];
		System.out.println(type);
		AGenType genType = AGenType.getEnumFromValue(type);

		JVar jvar = null;
		String className = aobject.getSimpleName();
		String varName = (className + "_" + aobject.getId()).toLowerCase();

		if (genType != null) {
			switch (genType) {
			case CHARACTER:
				jvar = methodBody
						.decl(this.codeModel._ref(char.class), varName).init(
								JExpr.lit('a'));
				break;
			case BOOLEAN:
				jvar = methodBody.decl(this.codeModel._ref(boolean.class),
						varName).init(JExpr.lit(false));
				break;
			case DOUBLE:
				jvar = methodBody.decl(this.codeModel._ref(double.class),
						varName).init(JExpr.lit(2));
				break;
			case FLOAT:
				jvar = methodBody.decl(this.codeModel._ref(float.class),
						varName).init(JExpr.lit(5.0));
				break;
			case INTEGER:
				jvar = methodBody.decl(this.codeModel._ref(int.class), varName)
						.init(JExpr.lit(0));
				break;
			case STRING:
				jvar = methodBody.decl(this.codeModel.ref(String.class),
						varName).init(JExpr.lit("stringTest"));
				break;
			case LONG:
				jvar = methodBody
						.decl(this.codeModel._ref(long.class), varName).init(
								JExpr.lit(0));
				break;
			case SHORT:
				jvar = methodBody.decl(this.codeModel._ref(short.class),
						varName).init(JExpr.lit(0));
				break;
			case BYTE:
				jvar = methodBody
						.decl(this.codeModel._ref(byte.class), varName).init(
								JExpr.lit(0));
				break;
			default:
				break;
			}
		}
		return jvar;
	}

}