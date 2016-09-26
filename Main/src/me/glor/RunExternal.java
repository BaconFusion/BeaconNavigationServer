package me.glor;

import java.io.*;
import java.util.Scanner;

/**
 * Created by glor on 9/19/16.
 */
public class RunExternal {

	public PrintWriter stdin;
	public Scanner stdout;
	public Scanner stderr;
	public Process p;
	ProcessBuilder pb;
	private RunExternal() {
		throw new UnsupportedOperationException();
	}
	public RunExternal(String... command) throws IOException {
		File file = new File(command[0]);
		if (!file.exists() || !file.isFile() || !file.canExecute())
			throw new FileNotFoundException("File not a valid executable.");
		pb = new ProcessBuilder(command);
		//p = Runtime.getRuntime().exec(command);
		start();
	}

	public static String queryProgram() {
		File file;
		do {
			System.out.println("Cannot locate octave. Please manually give (absolute) path to binary:");
			Scanner sc = new Scanner(System.in);
			if (!sc.hasNextLine()) {
				throw new RuntimeException("Could not read in path.");
			}
			file = new File(sc.nextLine());
		} while (!file.exists() || !file.canExecute());
		return file.getAbsolutePath();
	}

	public static String searchProgram(String appname) {
		String delimiter;
		String dirDelimiter;

		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("nix") >= 0 || os.indexOf("bsd") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("mac") >= 0) {
			delimiter = ":";
			dirDelimiter = "/";
		} else if (os.indexOf("win") >= 0) {
			delimiter = ";";
			dirDelimiter = "/";
			System.out.println("Windows not supported yet. Don't know where octave is in windows.");
			return queryProgram();
		} else {
			throw new RuntimeException("Unknown OS. Don't know where octave is in your os.");
		}
		String[] paths = System.getenv("PATH").split(delimiter);
		for (String path : paths) {
			File file = new File(path + dirDelimiter + appname);
			if (file.exists() && file.canExecute())
				return file.getAbsolutePath();
		}
		return queryProgram();
	}

	public static String[] getCommand(String executable, String... params) {
		String[] cmd = new String[params.length + 1];
		cmd[0] = executable;
		for (int i = 0; i < params.length; i++) {
			cmd[i + 1] = params[i];
		}
		return cmd;
	}

	private static void inheritIO(final String prefix, final InputStream src, final PrintStream dest) {
		new Thread(new Runnable() {
			public void run() {
				Scanner sc = new Scanner(src);
				while (sc.hasNextLine()) {
					dest.println(prefix + sc.nextLine());
				}
			}
		}).start();
	}

	/**
	 * It is very important for the process on the other side to also flush its output buffer if you want to avoid deadlocks
	 *
	 * @param strings Strings to be sent
	 */
	public void println(String... strings) {
		for (String string : strings) {
			stdin.print(string);
		}
		stdin.println();
		stdin.flush();
	}

	public void restart() throws IOException {
		stop();
		start();
	}

	public void start() throws IOException {
		p = pb.start();
		stdin = new PrintWriter(p.getOutputStream());
		//stderr = new Scanner(p.getErrorStream());
		stdout = new Scanner(p.getInputStream());
		//inheritIO("subprocess sais: ",p.getInputStream(), System.out);
		inheritIO("Subprocess " + pb.command().get(0) + " sais: ", p.getErrorStream(), System.err);
	}

	public void stop() {
		p.destroy();
	}
}
