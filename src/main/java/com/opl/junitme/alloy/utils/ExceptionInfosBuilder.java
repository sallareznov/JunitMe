package com.opl.junitme.alloy.utils;

public class ExceptionInfosBuilder {
	
	public static void printInfos(Throwable e) {
		final StackTraceElement[] stackTrace = e.getStackTrace();
		for (final StackTraceElement stackTraceElement : stackTrace) {
			System.out.println(stackTraceElement);
		}
	}

}
