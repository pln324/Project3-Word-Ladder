/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Franklin Mao>
 * <fm8487>
 * <16295>
 * <Pierce Nguyen>
 * <pln324>
 * <16295>
 * Slip days used: <0>
 * Git URL: https://github.com/EE422C/assignment3-spring-20-sp20-pr3-pair-53
 * Spring 2020
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	private static ArrayList<String> dictionary;
	private static int[] colors;
	private static ArrayList<String> ladder;

	public static void main(String[] args) throws Exception {
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out;			// default output to Stdout
		}


		initialize();
		
		// TODO methods to read in words, output ladder
	}
	
	public static void initialize() {
		dictionary = new ArrayList<>(makeDictionary());
		colors = new int[dictionary.size()];
		ladder = new ArrayList<String>();
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		String input = keyboard.nextLine();
		if(input.equals("/quit"))
			return new ArrayList<String>();
		ArrayList<String> words = new ArrayList<>(2);
		String[] twoWords = input.split(" ");
		words.add(twoWords[0]);
		words.add(twoWords[1]);

		return words;
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {

		// Returned list should be ordered start to end.  Include start and end.
		// If ladder is empty, return list with just start and end.
		DFSHelper(start, end);





		
		return ladder; // replace this line later with real return
	}
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		
		// TODO some code
		
		return null; // replace this line later with real return
	}
    
	
	public static void printLadder(ArrayList<String> ladder) {
		for(String s : ladder)
			System.out.println(s);
	}
	// TODO
	// Other private static methods here

	private static int countDifferences(String firstword, String secondword) {
		if(firstword.length() != secondword.length()) {
			return -1;
		}
		int diffs = 0;
		for(int  i = 0; i < firstword.length(); i++) {
			if(firstword.substring(i, i+1).equals(secondword.substring(i, i+1)) ) {
				diffs++;
			}
		}
		return diffs;
	}

	private static void DFSHelper(String start, String end) {
		colors[dictionary.indexOf(start)] = 1;
		ladder.add(start);
		if(start.equals(end)) {
			ladder.add(end);
			return;
		}

		ArrayList<String> neighbors = new ArrayList<String>(dictionary.size());
		ArrayList<Integer> differences = new ArrayList<Integer>(dictionary.size());
		for(int i  = 0; i < dictionary.size(); i++) {
			if(countDifferences(start, dictionary.get(i)) == 1 && colors[i] == 0) {
				neighbors.add(dictionary.get(i));
				differences.add(countDifferences(dictionary.get(i), end));
			}
		}
		int minIndex = differences.indexOf(Collections.min(differences));

		DFSHelper(dictionary.get(minIndex), end);
	}

	/* Do not modify makeDictionary */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
}
