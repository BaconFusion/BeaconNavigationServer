package me.glor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by glor on 9/19/16.
 */
public class RunExternal {

	public static final String[] octave = new String[]{"/usr/local/bin/octave", "-q", "--no-gui", "--no-window-system", "--persist", "/home/glor/halloxy.m"};
	public PrintWriter stdin;
	public Scanner stdout;
	public Scanner stderr;
	ProcessBuilder pb;
	private Process p;

	private RunExternal() {
		throw new UnsupportedOperationException();
	}

	public RunExternal(String... command) throws IOException {
		File file = new File(command[0]);
		if (!file.exists() || !file.isFile() || !file.canExecute())
			throw new FileNotFoundException("File not a valid executable.");
		pb = new ProcessBuilder(command);
		p = Runtime.getRuntime().exec(command);
		start();
	}

	public static void main(String[] args) {
		try {
			RunExternal re = new RunExternal("/home/glor/echo");

			re.stdin.println("5");
			re.stdin.close();

			//float f1 = re.stdout.nextFloat();
			//float f2 = re.stdout.nextFloat();
			System.out.println(re.stdout.nextLine());

			//System.out.println(f1);
			//System.out.println(f2);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public void restart() throws IOException {
		stop();
		start();
	}

	public void start() throws IOException {
		//p = pb.start();
		stdin = new PrintWriter(p.getOutputStream());
		stderr = new Scanner(p.getErrorStream());
		stdout = new Scanner(p.getInputStream());
	}

	public void stop() {
		p.destroy();
	}
}
