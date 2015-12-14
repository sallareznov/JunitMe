package com.opl.junitme.junit.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExceptionsLogger {
	
	private static File logFile;
	private static BufferedWriter writer;
	private static int nbExceptions;
	
	public static synchronized void initLogFile(String pathname, String className) throws IOException {
		System.out.println("--> initLogFile(" + pathname + ")");
		nbExceptions = 0;
		logFile = new File(pathname);
		logFile.createNewFile();
		logFile.delete();
		writer = new BufferedWriter(new FileWriter(logFile));
		writer.write("Test class : " + className + "\n\n");
		System.out.println("-- initLogFile(" + pathname + ")");
	}
	
	public static synchronized void logException(Throwable e) throws IOException {
		System.out.println("--> printInfos()");
		nbExceptions++;
		writer.write(e.toString() + "\n");
		final StackTraceElement[] stackTrace = e.getStackTrace();
		for (final StackTraceElement stackTraceElement : stackTrace) {
			writer.write(stackTraceElement.toString() + "\n");
		}
		writer.write("\n\n");
		System.out.println("-- printInfos()");
	}
	
	public static synchronized void closeLogFile() throws IOException {
		System.out.println("--> closeLogFile()");
		writer.write(nbExceptions + " exceptions were thrown during the execution of the tests");
		writer.flush();
		writer.close();
		System.out.println("--> closeLogFile()");
	}

}
