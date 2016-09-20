package me.glor.BeaconNavigation;

import me.glor.Run;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by glor on 9/18/16.
 */
public class Logger {
	private static int logFileCounter = 0;
	private PrintWriter pw;

	@Deprecated
	private Logger() {
		throw new UnsupportedOperationException();
	}

	private Logger(PrintWriter pw) {
		this.pw = pw;
	}

	public static Logger getLogFile() {
		logFileCounter++;
		FileWriter fw = null;
		try {
			fw = new FileWriter(Run.logFilePrefix + logFileCounter + Run.logFileSuffix);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		Logger log = new Logger(new PrintWriter(fw));
		return log;
	}

	public void println(String string) {
		pw.println(string);
	}

	public void print(String string) {
		pw.println(string);
	}

	public void flush() {
		pw.flush();
	}
}
