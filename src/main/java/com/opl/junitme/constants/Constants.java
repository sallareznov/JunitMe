package com.opl.junitme.constants;

/**
 * Constants.
 * 
 * @author Quentin
 *
 */
public interface Constants {

	/**
	 * Configuration constants.
	 * 
	 * @author Quentin
	 *
	 */
	public interface Configuration {

		/**
		 * Destination for the generated Alloy model.
		 */
		public static final String DST_GEN_MODEL = "src/main/resources/alloyGen/FinalGen.als";

		/**
		 * Source of the basic Alloy model.
		 */
		public static final String SRC_BASIC_MODEL = "src/main/resources/alloyModel/model.als";

		/**
		 * Destination of the temporary generated model.
		 */
		public static final String DST_TMP_FILE = "src/main/resources/alloyGen/gen.als";

		/**
		 * Source of the program.
		 */
		public static final String SRC_PRG = "../tullibee-code/tullibee-api/src/main/java";
		//public static final String SRC_PRG = "src/main/resources/res";

		/**
		 * The number of instance to run with alloy.
		 */
		public static final int ALLOY_RUN_CNT = 8;

		/**
		 * Default path of the generated junit classes.
		 */
		public final static String JUNIT_CLASSES_PATH = "./generatedTests";

		/**
		 * The method name prefix for the generation.
		 */
		public final static String METHOD_NAME_PREFIX = "test";

		/**
		 * The method name suffix for the generation.
		 */
		public final static String METHOD_NAME_SUFFIX = "";

	}
}