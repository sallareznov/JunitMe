package com.opl.junitme.alloy.generator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.opl.junitme.constants.Constants;
import com.opl.junitme.spoon.processors.AlloyProcessor;

import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

public class AlloyGenerator {

	public static void genAlloy(final String pathSourceProgram, final int instanceCount) throws Exception {
		
		Launcher spoon = new Launcher();
		final List<String> arguments = new LinkedList<String>();
		arguments.add("-i");
		arguments.add(pathSourceProgram);
		arguments.add("-x");
		//spoon.addInputResource(new FileSystemFolder(new File(pathSourceProgram)));
		spoon.run(arguments.toArray(new String[arguments.size()]));
		Factory factory = spoon.getFactory();
		ProcessingManager p = new QueueProcessingManager(factory);
		AlloyProcessor proc = new AlloyProcessor();
		p.addProcessor(proc);

		// init
		File f = new File(Constants.Configuration.DST_GEN_MODEL);
		File gen = new File(Constants.Configuration.DST_TMP_FILE);

		f.delete();
		f.createNewFile();
		gen.delete();
		gen.createNewFile();

		// generate gen.als
		p.process(factory.Class().getAll());

		try {
			PrintWriter writer = new PrintWriter(f);

			// read the model
			String buff = "";
			Scanner scanner = new Scanner(new File(Constants.Configuration.SRC_BASIC_MODEL));
			scanner.useDelimiter("\n");

			while (scanner.hasNext()) {
				buff += scanner.next()+"\n";
			}
			scanner.close();

			// read the gen
			scanner = new Scanner(gen);
			scanner.useDelimiter("\n");

			while (scanner.hasNext()) {
				buff += scanner.next() + "\n";
			}
			scanner.close();

			// write all
			if (!buff.isEmpty()) {
				writer.println(buff);
			}

			writer.println("run {} for " + instanceCount);

			writer.flush();

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}