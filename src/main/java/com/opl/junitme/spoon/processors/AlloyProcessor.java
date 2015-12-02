package com.opl.junitme.spoon.processors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;

public class AlloyProcessor extends AbstractProcessor<CtClass<?>> {

	private Set<String> linesToAdd;
	private PrintWriter writer;

	public AlloyProcessor() {
		linesToAdd = new HashSet<String>();
		writer = null;
	}

	@Override
	public boolean isToBeProcessed(CtClass<?> candidate) {
		return candidate.isTopLevel();
	}

	@Override
	public void process(CtClass<?> cl) {
		String className = cl.getQualifiedName().replace('.', '_');

		// String gen = "";

		try {

			writer = new PrintWriter(new BufferedWriter(new FileWriter("src/main/resources/alloyGen/gen.als", true)));

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (writer != null) {
			// writer.println("one sig " + className + " extends Type{}\n");
			linesToAdd.add(className);

			for (CtMethod<?> method : cl.getAllMethods()) {

				String methodName = className + "_" + method.getSimpleName() + "_" + method.getPosition().getLine();

				writer.println("one sig " + methodName + " extends Method{}");

				writer.println("fact{");
				writer.println("#" + methodName + ".paramTypes=" + method.getParameters().size());
				int i = 0;
				for (CtParameter<?> param : method.getParameters()) {
					if (!param.getType().isPrimitive()) {
						String name = param.getType().toString().replace(".", "_");
						name = name.replace("[]", "_array");
						writer.println(methodName + ".paramTypes[" + (i++) + "]=" + name);
						linesToAdd.add(name);
					} else {
						if (param.getType().getQualifiedName().equals(int.class.getName())
								| param.getType().getQualifiedName().equals(Integer.class.getName())) {
							writer.println(methodName + ".paramTypes[" + (i++) + "]=Gen_Integer");
						} else if (param.getType().getQualifiedName().equals(float.class.getName())
								| param.getType().getQualifiedName().equals(Float.class.getName())) {
							writer.println(methodName + ".paramTypes[" + (i++) + "]=Gen_Float");
						} else if (param.getType().getQualifiedName().equals(boolean.class.getName())
								| param.getType().getQualifiedName().equals(Boolean.class.getName())) {
							writer.println(methodName + ".paramTypes[" + (i++) + "]=Gen_Boolean");
						} else if (param.getType().getQualifiedName().equals(byte.class.getName())
								| param.getType().getQualifiedName().equals(Byte.class.getName())) {
							writer.println(methodName + ".paramTypes[" + (i++) + "]=Gen_Byte");
						} else if (param.getType().getQualifiedName().equals(short.class.getName())
								| param.getType().getQualifiedName().equals(Short.class.getName())) {
							writer.println(methodName + ".paramTypes[" + (i++) + "]=Gen_Short");
						} else if (param.getType().getQualifiedName().equals(double.class.getName())
								| param.getType().getQualifiedName().equals(Double.class.getName())) {
							writer.println(methodName + ".paramTypes[" + (i++) + "]=Gen_Double");
						} else if (param.getType().getQualifiedName().equals(char.class.getName())
								| param.getType().getQualifiedName().equals(Character.class.getName())) {
							writer.println(methodName + ".paramTypes[" + (i++) + "]=Gen_Character");
						} else if (param.getType().getQualifiedName().equals(long.class.getName())
								| param.getType().getQualifiedName().equals(Long.class.getName())) {
							writer.println(methodName + ".paramTypes[" + (i++) + "]=Gen_Long");
						} else {
							final String type = param.getType().getQualifiedName().replace('.', '_');
							writer.println(methodName + ".paramTypes[" + (i++) + "]=Gen_" + type);
						}
					}
				}
				writer.println(methodName + ".receiverType=" + className);
				writer.println("}\n");
				writer.flush();
			}

		}
		

	}

	@Override
	public void processingDone() {
		System.out.println("DONE : " + linesToAdd.size());
		for (final String type : linesToAdd) {
			writer.println("one sig " + type + " extends Type{}");
		}
		writer.flush();
		writer.close();
	}

}
