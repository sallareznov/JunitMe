package com.opl.junitme.spoon.processors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;

public class AlloyProcessor extends AbstractProcessor<CtClass<?>> {

	private Set<String> linesToAdd;
	private PrintWriter writer;

	public AlloyProcessor() {
		linesToAdd = new HashSet<String>();
		writer = null;
	}

	@Override
	public boolean isToBeProcessed(CtClass<?> candidate) {
		return candidate.isTopLevel() && !candidate.isInterface()
				&& !candidate.hasModifier(ModifierKind.ABSTRACT);
	}

	@Override
	public void process(CtClass<?> cl) {
		String className = cl.getQualifiedName().replace('.', '_');

		try {

			writer = new PrintWriter(new BufferedWriter(new FileWriter(
					"src/main/resources/alloyGen/gen.als", true)));

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (writer != null) {
			// writer.println("one sig " + className + " extends Type{}\n");
			linesToAdd.add(className);

			for (CtMethod<?> method : cl.getAllMethods()) {
				if (method.hasModifier(ModifierKind.PUBLIC)) {
					boolean addMethod = true;
					final StringBuilder signatureBuilder = new StringBuilder();

					String methodName = className + "_"
							+ method.getSimpleName() + "_"
							+ method.getPosition().getLine();

					signatureBuilder.append("one sig " + methodName
							+ " extends Method{}\n");
					// writer.println("one sig " + methodName + " extends
					// Method{}");

					signatureBuilder.append("fact{\n");
					// writer.println("fact{");

					signatureBuilder.append("#" + methodName + ".paramTypes="
							+ method.getParameters().size() + "\n");
					// writer.println("#" + methodName + ".paramTypes=" +
					// method.getParameters().size());

					int i = 0;
					for (CtParameter<?> param : method.getParameters()) {
						if (!param.getType().isPrimitive()) {
							String name = param.getType().toString()
									.replace(".", "_");
							if (name.contains("[]") || name.contains("<")) {
								addMethod = false;
							}
							signatureBuilder.append(methodName + ".paramTypes["
									+ (i++) + "]=" + name + "\n");
							// writer.println(methodName + ".paramTypes[" +
							// (i++) +
							// "]=" + name);
							if (addMethod) {
								linesToAdd.add(name);
							}
						} else {
							if (param.getType().getQualifiedName()
									.equals(int.class.getName())
									| param.getType().getQualifiedName()
											.equals(Integer.class.getName())) {
								signatureBuilder.append(methodName
										+ ".paramTypes[" + (i++)
										+ "]=Gen_Integer\n");
								// writer.println(methodName + ".paramTypes[" +
								// (i++) + "]=Gen_Integer");
							} else if (param.getType().getQualifiedName()
									.equals(float.class.getName())
									| param.getType().getQualifiedName()
											.equals(Float.class.getName())) {
								signatureBuilder.append(methodName
										+ ".paramTypes[" + (i++)
										+ "]=Gen_Float\n");
								// writer.println(methodName + ".paramTypes[" +
								// (i++) + "]=Gen_Float");
							} else if (param.getType().getQualifiedName()
									.equals(boolean.class.getName())
									| param.getType().getQualifiedName()
											.equals(Boolean.class.getName())) {
								signatureBuilder.append(methodName
										+ ".paramTypes[" + (i++)
										+ "]=Gen_Boolean\n");
								// writer.println(methodName + ".paramTypes[" +
								// (i++) + "]=Gen_Boolean");
							} else if (param.getType().getQualifiedName()
									.equals(byte.class.getName())
									| param.getType().getQualifiedName()
											.equals(Byte.class.getName())) {
								signatureBuilder.append(methodName
										+ ".paramTypes[" + (i++)
										+ "]=Gen_Byte\n");
								// writer.println(methodName + ".paramTypes[" +
								// (i++) + "]=Gen_Byte");
							} else if (param.getType().getQualifiedName()
									.equals(short.class.getName())
									| param.getType().getQualifiedName()
											.equals(Short.class.getName())) {
								signatureBuilder.append(methodName
										+ ".paramTypes[" + (i++)
										+ "]=Gen_Short\n");
								// writer.println(methodName + ".paramTypes[" +
								// (i++) + "]=Gen_Short");
							} else if (param.getType().getQualifiedName()
									.equals(double.class.getName())
									| param.getType().getQualifiedName()
											.equals(Double.class.getName())) {
								signatureBuilder.append(methodName
										+ ".paramTypes[" + (i++)
										+ "]=Gen_Double\n");
								// writer.println(methodName + ".paramTypes[" +
								// (i++) + "]=Gen_Double");
							} else if (param.getType().getQualifiedName()
									.equals(char.class.getName())
									| param.getType().getQualifiedName()
											.equals(Character.class.getName())) {
								signatureBuilder.append(methodName
										+ ".paramTypes[" + (i++)
										+ "]=Gen_Character\n");
								// writer.println(methodName + ".paramTypes[" +
								// (i++) + "]=Gen_Character");
							} else if (param.getType().getQualifiedName()
									.equals(long.class.getName())
									| param.getType().getQualifiedName()
											.equals(Long.class.getName())) {
								signatureBuilder.append(methodName
										+ ".paramTypes[" + (i++)
										+ "]=Gen_Long\n");
								// writer.println(methodName + ".paramTypes[" +
								// (i++) + "]=Gen_Long");
							} else {
								final String type = param.getType()
										.getQualifiedName().replace('.', '_');
								signatureBuilder.append(methodName
										+ ".paramTypes[" + (i++) + "]=Gen_"
										+ type + "\n");
								// writer.println(methodName + ".paramTypes[" +
								// (i++) + "]=Gen_" + type);
							}
						}
					}
					signatureBuilder.append(methodName + ".receiverType="
							+ className + "\n");
					// writer.println(methodName + ".receiverType=" +
					// className);

					signatureBuilder.append("}\n\n");
					// writer.println("}\n");

					if (addMethod) {
						writer.println(signatureBuilder.toString());
					}
					// writer.flush();
				}
			}

			int cpt = 1;
			for (final CtConstructor<?> constructor : cl.getConstructors()) {
				if (constructor.hasModifier(ModifierKind.PUBLIC)) {
					String constructorName = className + "_"
							+ constructor.getSimpleName() + "" + (cpt++) + "_"
							+ constructor.getPosition().getLine();
					constructorName = constructorName.replace("<", "");
					constructorName = constructorName.replace(">", "");
					boolean addConstructor = true;
					final StringBuilder signatureBuilder = new StringBuilder();

					signatureBuilder.append("one sig " + constructorName
							+ " extends ConstructorCall{}\n");
					// writer.println("one sig " + methodName + " extends
					// Method{}");

					signatureBuilder.append("fact{\n");
					// writer.println("fact{");

					signatureBuilder.append("#" + constructorName
							+ ".paramTypes="
							+ constructor.getParameters().size() + "\n");
					// writer.println("#" + methodName + ".paramTypes=" +
					// method.getParameters().size());

					int i = 0;
					for (CtParameter<?> param : constructor.getParameters()) {
						if (!param.getType().isPrimitive()) {
							String name = param.getType().toString()
									.replace(".", "_");
							if (name.contains("[]") || name.contains("<")) {
								addConstructor = false;
							}
							signatureBuilder.append(constructorName
									+ ".paramTypes[" + (i++) + "]=" + name
									+ "\n");
							// writer.println(methodName + ".paramTypes[" +
							// (i++) +
							// "]=" + name);
							if (addConstructor) {
								linesToAdd.add(name);
							}
						} else {
							if (param.getType().getQualifiedName()
									.equals(int.class.getName())
									| param.getType().getQualifiedName()
											.equals(Integer.class.getName())) {
								signatureBuilder.append(constructorName
										+ ".paramTypes[" + (i++)
										+ "]=Gen_Integer\n");
								// writer.println(methodName + ".paramTypes[" +
								// (i++) + "]=Gen_Integer");
							} else if (param.getType().getQualifiedName()
									.equals(float.class.getName())
									| param.getType().getQualifiedName()
											.equals(Float.class.getName())) {
								signatureBuilder.append(constructorName
										+ ".paramTypes[" + (i++)
										+ "]=Gen_Float\n");
								// writer.println(methodName + ".paramTypes[" +
								// (i++) + "]=Gen_Float");
							} else if (param.getType().getQualifiedName()
									.equals(boolean.class.getName())
									| param.getType().getQualifiedName()
											.equals(Boolean.class.getName())) {
								signatureBuilder.append(constructorName
										+ ".paramTypes[" + (i++)
										+ "]=Gen_Boolean\n");
								// writer.println(methodName + ".paramTypes[" +
								// (i++) + "]=Gen_Boolean");
							} else if (param.getType().getQualifiedName()
									.equals(byte.class.getName())
									| param.getType().getQualifiedName()
											.equals(Byte.class.getName())) {
								signatureBuilder.append(constructorName
										+ ".paramTypes[" + (i++)
										+ "]=Gen_Byte\n");
								// writer.println(methodName + ".paramTypes[" +
								// (i++) + "]=Gen_Byte");
							} else if (param.getType().getQualifiedName()
									.equals(short.class.getName())
									| param.getType().getQualifiedName()
											.equals(Short.class.getName())) {
								signatureBuilder.append(constructorName
										+ ".paramTypes[" + (i++)
										+ "]=Gen_Short\n");
								// writer.println(methodName + ".paramTypes[" +
								// (i++) + "]=Gen_Short");
							} else if (param.getType().getQualifiedName()
									.equals(double.class.getName())
									| param.getType().getQualifiedName()
											.equals(Double.class.getName())) {
								signatureBuilder.append(constructorName
										+ ".paramTypes[" + (i++)
										+ "]=Gen_Double\n");
								// writer.println(methodName + ".paramTypes[" +
								// (i++) + "]=Gen_Double");
							} else if (param.getType().getQualifiedName()
									.equals(char.class.getName())
									| param.getType().getQualifiedName()
											.equals(Character.class.getName())) {
								signatureBuilder.append(constructorName
										+ ".paramTypes[" + (i++)
										+ "]=Gen_Character\n");
								// writer.println(methodName + ".paramTypes[" +
								// (i++) + "]=Gen_Character");
							} else if (param.getType().getQualifiedName()
									.equals(long.class.getName())
									| param.getType().getQualifiedName()
											.equals(Long.class.getName())) {
								signatureBuilder.append(constructorName
										+ ".paramTypes[" + (i++)
										+ "]=Gen_Long\n");
								// writer.println(methodName + ".paramTypes[" +
								// (i++) + "]=Gen_Long");
							} else {
								final String type = param.getType()
										.getQualifiedName().replace('.', '_');
								signatureBuilder.append(constructorName
										+ ".paramTypes[" + (i++) + "]=Gen_"
										+ type + "\n");
								// writer.println(methodName + ".paramTypes[" +
								// (i++) + "]=Gen_" + type);
							}
						}
					}
					// writer.println(methodName + ".receiverType=" +
					// className);

					signatureBuilder.append("}\n\n");
					// writer.println("}\n");

					if (addConstructor) {
						writer.println(signatureBuilder.toString());
					}
				}

			}
			writer.flush();
		}

	}

	@Override
	public void processingDone() {
		System.out.println(linesToAdd);
		for (final String type : linesToAdd) {
			writer.println("one sig " + type + " extends Type{}");
		}
		writer.flush();
		writer.close();
	}

}
