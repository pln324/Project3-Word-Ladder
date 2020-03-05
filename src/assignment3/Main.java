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
	private static int[] DFS_Colors;						//array for checking if words are discovered
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
		while(!words.isEmpty()) {
			ladder = getWordLadderDFS(words.get(0), words.get(1));
			printLadder(ladder);
			ladder = getWordLadderBFS(words.get(0), words.get(1));
			printLadder(ladder);
			words = parse(kb);
		}
	}
	
	public static void initialize() {
		dictionary = new ArrayList<>(makeDictionary());
		DFS_Colors = new int[dictionary.size()];
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
		if(DFSHelper(start, end)) {

			return ladder;
		}
		else {
			ladder.add(start);
			ladder.add(end);

		}
		return ladder;
	}
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
    	start = start.toUpperCase();
		end = end.toUpperCase();
		int[] BFS_Colors = new int[dictionary.size()];
		Queue<String> queue = new LinkedList<String>();
		String[] parent = new String[dictionary.size()];		//holds each word's previous node (i.e. the word that comes before it in the ladder)

		queue.add(start);
		BFS_Colors[dictionary.indexOf(start)] = 1;
		while(!queue.isEmpty()) {
			for(int i  = 0; i < dictionary.size(); i++) {									
				if(countDifferences(queue.peek(), dictionary.get(i)) == 1 && BFS_Colors[i] == 0) {
					queue.add(dictionary.get(i));							//add each word's neighbors to the queue
					BFS_Colors[dictionary.indexOf(dictionary.get(i))] = 1;	//when word is a added to queue, discover it
					parent[i] = queue.peek();								//each word is given their parent word
				}
			}

			if(queue.remove().equals(end)) {								//when end word is reached
				ArrayList<String> reverse = new ArrayList<String>();
				String previousWord = end;
				//populate ladder
				while(!previousWord.equals(start)) {
					reverse.add(previousWord);								//follow path from parent to parent, from end to start
					previousWord = parent[dictionary.indexOf(previousWord)];
				}
				reverse.add(start);
				Collections.reverse(reverse);

				return reverse;
			}
		}
		ArrayList<String> noLadder = new ArrayList<String>(2);
		noLadder.add(start);
		noLadder.add(end);

		return noLadder;
	}
    
	
	public static void printLadder(ArrayList<String> ladder) {
		String start = ladder.get(0);
		String end = ladder.get(ladder.size() - 1);
		if(ladder.size() > 2) {
			System.out.println("a " + (ladder.size() - 2) + " word ladder exists between " + start.toLowerCase() + " and " + end.toLowerCase());
			for (String s : ladder)
				System.out.println(s.toLowerCase());
		}
		else
			System.out.println("no word ladder can be found between " + start.toLowerCase() + " and " + end.toLowerCase());


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
		DFS_Colors[dictionary.indexOf(start)] = 1;
		ladder.add(start);				//build ladder
		if(start.equals(end)) {			//if word is found return true
			return true;
		}

		ArrayList<String> neighbors = new ArrayList<String>(dictionary.size());			//array to keep track of all words one letter away from start
		ArrayList<Integer> differences = new ArrayList<Integer>(dictionary.size());		//count number of different letters from end word to start
		for(int i  = 0; i < dictionary.size(); i++) {									
			if(countDifferences(start, dictionary.get(i)) == 1 && DFS_Colors[i] == 0) {
				neighbors.add(dictionary.get(i));										
				differences.add(countDifferences(dictionary.get(i), end));
			}
		}

		for(int i = 0; i < neighbors.size() - 1; i++) {
			for(int j = 0; j < neighbors.size() - i - 1; j++) {
				if(differences.get(j) > differences.get(j+1)) {
					String temp = neighbors.get(j);
					int temp2 = differences.get(j);
					neighbors.set(j, neighbors.get(j+1));
					differences.set(j, differences.get(j+1));
					neighbors.set(j+1, temp);
					differences.set(j+1, temp2);
				}
			}
		}

		if (neighbors.size()==0) {				//if neighbors array is empty, there is a dead end
			ladder.remove(ladder.size()-1);
			return false;
		}
		for(String neighbor : neighbors) {
			if(DFSHelper(neighbor, end))
				return true;
			DFS_Colors[dictionary.indexOf(neighbor)] = 1;
		}
		ladder.remove(ladder.size() -1);
		return false;
		/*if(!DFSHelper(dictionary.get(dictionary.indexOf(neighbors.get(minIndex))), end)) {	//if next word leads to a dead end
			DFS_Colors[dictionary.indexOf(neighbors.get(minIndex))] = 1;						//set word to discovered
			ladder.remove(ladder.size()-1);													//remove word from ladder
			neighbors.remove(minIndex);														//remove word from potential neighbors
			differences.remove(minIndex);
			while (neighbors.size()>0) {														//continue if there are still neighbors to check
				minIndex = differences.indexOf(Collections.min(differences));
				if(!DFSHelper(dictionary.get(dictionary.indexOf(neighbors.get(minIndex))), end))  {
					neighbors.remove(minIndex);
					differences.remove(minIndex);
					ladder.remove(ladder.size() -1);
				}
			}
			return false;
		}
		return true; */
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
