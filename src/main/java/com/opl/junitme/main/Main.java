package com.opl.junitme.main;

import com.opl.junitme.alloy.generator.AlloyGenerator;
import com.opl.junitme.alloy.model.manager.AlloyInstance;
import com.opl.junitme.alloy.model.manager.AlloyInstanceManager;
import com.opl.junitme.alloy.parser.AlloyToJavaParser;
import com.opl.junitme.constants.Constants.Configuration;
import com.opl.junitme.junit.generator.JUnitTestClassGenerator;

public class Main {

	public static void main(final String[] args) throws Exception {

		// Check args
		if (args.length < 2 || args.length > 3) {
			displayUsage();
			return;
		}

		// Get configuration
		String srcProgram = null;
		int nbTest, nbRun;
		try {
			srcProgram = args[0];
			nbTest = Integer.valueOf(args[1]);
			nbRun = Configuration.ALLOY_RUN_CNT;
			if (args.length == 3) {
				nbRun = Integer.valueOf(args[2]);
			}
		} catch (Exception e) {
			System.out.println("Check args, something is wrong !");
			return;
		}

		// Generation of Alloy model according the given path
		AlloyGenerator.genAlloy(srcProgram, nbRun);

		// Parse alloy model to JAVA model
		new AlloyToJavaParser(Configuration.DST_GEN_MODEL).parse(false, true,
				nbTest);

		// Generate unit test
		JUnitTestClassGenerator jUnitTestClassGenerator = new JUnitTestClassGenerator(
				"GeneratedClassTest", srcProgram);
		jUnitTestClassGenerator.createBeforeClassMethod("__exceptions.log");
		for (AlloyInstance alloyInstance : AlloyInstanceManager.getInstance()) {
			jUnitTestClassGenerator.createTestMethod(alloyInstance
					.getFirstMethodCall(), alloyInstance.getConstructorCalls());
		}
		jUnitTestClassGenerator.createAfterClassMethod();
		jUnitTestClassGenerator.generateClass();
	}

	/**
	 * Display the usage.
	 */
	private static void displayUsage() {
		System.out.println("Usage : pathSource nbTest [alloyRunCnt]");
		System.out
				.println("pathSource : the source of the program to generate test with");
		System.out.println("nbTest : the number of test to generate");
		System.out
				.println("alloyRunCnt (optionnal) : the number of instance of each object for Alloy solution");

	}

}