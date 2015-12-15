package com.opl.junitme.alloy.parser;

import java.util.Iterator;

import com.opl.junitme.alloy.model.AModel;
import com.opl.junitme.alloy.model.manager.AlloyInstance;
import com.opl.junitme.alloy.model.manager.AlloyInstanceManager;
import com.opl.junitme.alloy.utils.JunitMeUtils;
import com.opl.junitme.constants.AModelEnum;
import com.opl.junitme.constants.ASpecificationEnum;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.Field;
import edu.mit.csail.sdg.alloy4compiler.parser.CompModule;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Tuple;
import edu.mit.csail.sdg.alloy4compiler.translator.A4TupleSet;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;

/**
 * 
 * @author Quentin
 *
 */
public class AlloyToJavaParser {

	private final String filepath;

	/**
	 * 
	 * @param filepath
	 */
	public AlloyToJavaParser(final String filepath) {
		this.filepath = filepath;
	}

	/**
	 * Parse the alloy result and build java object.
	 * 
	 * @param print
	 *            if the alloy instance has to be printed on the standard
	 *            output.
	 * @param createInstance
	 *            if the java instance of the solution has to be created.
	 * @param max
	 *            the maximum instance to iterate. Specify -1 if you want to
	 *            iterate through all of them.
	 * @throws Err
	 */
	public void parse(final boolean print, final boolean createInstance, final int max) throws Err {

		CompModule model = CompUtil.parseEverything_fromFile(null, null, this.filepath);

		// Get the command to execute. for example :
		Command cmd = model.getAllCommands().get(0);
		System.out.println(cmd);
		
		// Execute the model using the command obtained in step 2
		A4Solution solution = TranslateAlloyToKodkod.execute_command(null, model.getAllReachableSigs(), cmd,
				new A4Options());

		int count = 0;
		while (solution.satisfiable() && (count < max || max == -1)) {
			if (count % 100 == 0) {
				System.out.println("Creating instance n_" + (count) + (max != -1 ? " of " + max : ""));
			}
			
			count++;

			// Prepare the string buffer.
			StringBuilder sb = new StringBuilder();

			// Prepare the JAVA alloy instance.
			AlloyInstance alloyInstance = null;
			if (createInstance) {
				alloyInstance = AlloyInstanceManager.newAlloyInstance();
			}

			// Get the next alloy solution
			solution = solution.next();

			// Iterate into the alloy instance.
			for (Sig s : solution.getAllReachableSigs()) {

				// Print the sigs (declaration of objects).
				sb.append(s.label);
				sb.append("=");

				// Create empty instance with just the informations.
				A4TupleSet a4TupleSet = solution.eval(s);
				Iterator<A4Tuple> iterator = a4TupleSet.iterator();
				A4Tuple a4Tuple;
				while (iterator.hasNext() && (a4Tuple = iterator.next()) != null) {
					for (int i = 0; i < a4Tuple.arity(); i++) {

						String value = a4Tuple.atom(i);

						// Create instance
						if (createInstance) {
							alloyInstance.addElement(value,
									AModelEnum.getEnumFromValue(JunitMeUtils.getSigName(s.label)));
						}
						// Print
						sb.append("SIG ").append(value);
					}
				}
				sb.append("\n");

				// Iterate into the value of each fields of each objects to
				// create JAVA instance.
				for (Field f : s.getFields()) {

					// Print the current field.
					sb.append(s.label);
					sb.append("<:");
					sb.append(f.label);
					sb.append("=");

					// Iterate into each tuples
					a4TupleSet = solution.eval(f);
					iterator = a4TupleSet.iterator();
					while (iterator.hasNext() && (a4Tuple = iterator.next()) != null) {

						// Create a model corresponding to the current tuple.
						AModel aModel = null;
						for (int i = 0; i < a4Tuple.arity(); i++) {

							// Print
							if (i > 0) {
								sb.append("->");
							}
							sb.append(a4Tuple.atom(i));

							// Create JAVA instance for each field properties
							if (createInstance) {
								String element = a4Tuple.atom(i);
								if (JunitMeUtils.isElement(element)) {
									if (i == 0) {
										aModel = alloyInstance.addElement(element);
									} else {
										ASpecificationEnum specification = ASpecificationEnum.getEnumFromValue(f.label);
										if (aModel != null && specification != null) {
											aModel.setSpecification(specification, element);
										}
									}
								}
							}
						}
						sb.append(" ");
					}
					sb.append("\n");

				}
			}
			// Print final result
			if (print) {
				System.out.println(sb.toString());
			}
		}

	}

}
