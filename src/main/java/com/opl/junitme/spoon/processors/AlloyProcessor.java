package com.opl.junitme.spoon.processors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;

public class AlloyProcessor extends AbstractProcessor<CtClass<?>> {

	@Override
	public void process(CtClass<?> cl) {
		String className = cl.getQualifiedName().replace('.', '_');

		PrintWriter writer = null;
//		String gen = "";

		try {

			writer = new PrintWriter(new BufferedWriter(new FileWriter("src/main/resources/alloyGen/gen.als", true)));

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (writer != null) {
			writer.println("one sig " + className + " extends Type{}\n");

			for (CtMethod<?> method : cl.getAllMethods()) {
				String methodName = className + "_" + method.getSimpleName() + "_" + method.getPosition().getLine();

				writer.println("one sig " + methodName + " extends Method{}");

				writer.println("fact{");
				writer.println("#" + methodName + ".paramTypes=" + method.getParameters().size());
				int i = 0;
				for (CtParameter<?> param : method.getParameters()) {
					// int / Integer
					if (param.getType().getQualifiedName().equals(int.class.getName()) | param.getType().getQualifiedName().equals(Integer.class.getName())) {
						writer.println(methodName + ".paramTypes[" + (i++) + "]=Gen_Integer");
						// String
					} else if (param.getType().getQualifiedName().equals(String.class.getName())) {
						writer.println(methodName + ".paramTypes[" + (i++) + "]=Gen_String");
						// Float
					} else if (param.getType().getQualifiedName().equals(float.class.getName()) | param.getType().getQualifiedName().equals(Float.class.getName())) {
						writer.println(methodName + ".paramTypes[" + (i++) + "]=Gen_Float");
						// Boolean
					} else if (param.getType().getQualifiedName().equals(boolean.class.getName()) | param.getType().getQualifiedName().equals(Boolean.class.getName())) {
						writer.println(methodName + ".paramTypes[" + (i++) + "]=Gen_Boolean");
					} else {
						writer.println(methodName + ".paramTypes[" + (i++) + "]=" + param.getType());
					}
				}
				writer.println(methodName + ".receiverType=" + className);
				writer.println("}\n");

			}

			writer.flush();
			writer.close();
		}

	}
}
