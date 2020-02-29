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
	private static ArrayList<String> dictionary;		//dictionary as an ArrayList
	private static int[] colors;						//array for checking if words are discovered
	private static ArrayList<String> ladder;			//stores word ladder

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
		
		ArrayList<String> words = parse(kb);
		getWordLadderBFS(words.get(0), words.get(1));
		printLadder(ladder);
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
		words.add(twoWords[0].toUpperCase());
		words.add(twoWords[1].toUpperCase());

		return words;
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		start = start.toUpperCase();
		end = end.toUpperCase();
		// Returned list should be ordered start to end.  Include start and end.
		// If ladder is empty, return list with just start and end.
		if(DFSHelper(start, end)) {;
			return ladder;
		}
		else {
			ladder.add(end);
			System.out.println("no word ladder found");
		}
		return ladder; // replace this line later with real return
	}
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
    	start = start.toUpperCase();
		end = end.toUpperCase();
		Queue<String> queue = new LinkedList<String>();
		String[] parent = new String[dictionary.size()];
		//ladder.add(start);
		queue.add(start);
		colors[dictionary.indexOf(start)] = 1;
		while(!queue.isEmpty()) {
			//ladder.add(queue.peek());
			//int neighborCount = 0;
			for(int i  = 0; i < dictionary.size(); i++) {									
				if(countDifferences(queue.peek(), dictionary.get(i)) == 1 && colors[i] == 0) {
					queue.add(dictionary.get(i));
					colors[dictionary.indexOf(dictionary.get(i))] = 1;
					//neighborCount++;
					parent[i] = queue.peek();
				}
			}
//			if (neighborCount == 0) {
//				ladder.remove(ladder.size()-1);
//			}
			if(queue.remove().equals(end)) {
				ArrayList<String> reverse = new ArrayList<String>();
				String previousWord = end;
				//reverse.add(previousWord);
				while(previousWord != start) {
					reverse.add(previousWord);
					previousWord = parent[dictionary.indexOf(previousWord)];
				}
				return reverse;
			};
		}
		//populate ladder
		
		return null; // replace this line later with real return
	}
    
	
	public static void printLadder(ArrayList<String> ladder) {
		for(String s : ladder)
			System.out.println(s);
	}
	// TODO
	// Other private static methods here

	/**
	 * 
	 * @param firstword
	 * @param secondword
	 * @return number of differences in letters between first and second word
	 */
	private static int countDifferences(String firstword, String secondword) {
		if(firstword.length() != secondword.length()) {
			return -1;
		}
		int diffs = 0;
		for(int  i = 0; i < firstword.length(); i++) {
			if(!firstword.substring(i, i+1).equals(secondword.substring(i, i+1)) ) {
				diffs++;
			}
		}
		return diffs;
	}

	private static boolean DFSHelper(String start, String end) {
		colors[dictionary.indexOf(start)] = 1;
		ladder.add(start);				//build ladder
		if(start.equals(end)) {			//if word is found return true
			return true;
		}

		ArrayList<String> neighbors = new ArrayList<String>(dictionary.size());			//array to keep track of all words one letter away from start
		ArrayList<Integer> differences = new ArrayList<Integer>(dictionary.size());		//count number of different letters from end word
		for(int i  = 0; i < dictionary.size(); i++) {									
			if(countDifferences(start, dictionary.get(i)) == 1 && colors[i] == 0) {
				neighbors.add(dictionary.get(i));										
				differences.add(countDifferences(dictionary.get(i), end));
			}
		}
		int minIndex = 0;						
		if (neighbors.size()>0) {
			minIndex = differences.indexOf(Collections.min(differences));				//find word with least number of differences from end word
		}
		if (neighbors.size()==0) {				//if neighbors array is empty, there is a dead end
			return false;
		}
		if(!DFSHelper(dictionary.get(dictionary.indexOf(neighbors.get(minIndex))), end)) {	//if next word leads to a dead end
			colors[dictionary.indexOf(neighbors.get(minIndex))] = 1;						//set word to discovered
			ladder.remove(ladder.size()-1);													//remove word from ladder
			neighbors.remove(minIndex);														//remove word from potential neighbors
			differences.remove(minIndex);									
			if (neighbors.size()>0) {														//continue if there are still neighbors to check
				minIndex = differences.indexOf(Collections.min(differences));
				DFSHelper(dictionary.get(dictionary.indexOf(neighbors.get(minIndex))), end);
			}
			else return false;
		}
		return true;
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
