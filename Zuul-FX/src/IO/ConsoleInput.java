package IO;

import java.util.Scanner;

public class ConsoleInput implements Input {
	private String[] parseInput(String input) {
		input = input.trim().replaceAll(" +", " ");
		String[] arr = input.split(" ");
		return arr;
	}

	@Override
	public String[] getUserInput() {
		String data = "";
		IOHandler.output.printf(">> ");
		@SuppressWarnings("resource")
		Scanner scanInput = new Scanner(System.in);
		try {
			data = scanInput.nextLine();
		} catch (java.util.NoSuchElementException e) { // Catches Ctrl+D
			System.err.println("Interrupted by user.");
			System.exit(0);
		}
		scanInput.reset();
//		return parseInput(data);
		return new String[] {};
	}

}
