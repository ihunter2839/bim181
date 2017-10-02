import java.util.*;
import java.io.*;

public class stringOccurence {

	public static boolean EndFile = false;
	public static int posCount = 0;
	public static File inputFile;
	public static BufferedReader reader;
	public static ArrayList<Integer> patternStartingPos;

	public static void main(String[] args){
		//agrs[0] == file name of text to search

		//create a reference to the input file 
		inputFile = new File(args[0]);
		//Create a buffered reader for the input. Buffered readers are suggested
		//by the javadocs instead of a raw filereader
		try {
			reader = new BufferedReader(new FileReader(inputFile));
		} catch (FileNotFoundException e){}

		String pattern = "";
		try{
			pattern = reader.readLine();
		} catch(IOException e){}

		patternStartingPos = new ArrayList<Integer>();

		//Method: read in initial k characters. Check if the pattern is matched.
		//Move the last k-1 characters into a new array. Fill the last character with
		//the next character read from file. Repeat until file is empty. Keep a counter
		//that tracks the index of the first position in the current kmer
		//Does not work for files with indexes > 2 ^ 31
		char[] curKmer = new char[pattern.length()];
		//populate inital curKmer
		populateKmer(curKmer);

		while(!EndFile){
			checkKmer(curKmer, pattern);
		}

		for(int i=0; i<patternStartingPos.size(); i++){
			System.out.print(patternStartingPos.get(i) + " ");
		}

	}


	public static void checkKmer(char[] kmer, String pattern){
		//if pattern is matched, populate kmer with all new values
		for(int i = 0; i <= pattern.length(); i++) {

			//if for loop has iterated past the length of the pattern, all characters
			//match and an occurence has been found
			if(i == pattern.length()){
				patternStartingPos.add(posCount);
				updateKmer(kmer);
				break;
			}
			//if the two strings do not match, break from the for loop
			if(kmer[i] != pattern.charAt(i)){
				updateKmer(kmer);
				break;
			} 
		}
	}

	//populate the kmer with all new values 
	public static void populateKmer(char[] kmer){
		for(int i=0; i < kmer.length; i++){
			
			int curChar = 0;
			try { 
				curChar = reader.read();
			} catch(IOException e) {}

			if(curChar == -1) {
				EndFile = true; //check for eof
				break;
			}
			kmer[i] = (char) curChar;
		}
		String cur = new String(kmer);
	}

	public static void updateKmer(char[] kmer){

		char[] temp = kmer.clone();
		//move characters from kmer to temp, offsetting them by one position
		//i.e. kmer[1] --> temp[0]
		for(int i=1; i<kmer.length; i++){
			kmer[i-1] = temp[i];
		}

		String tempIs = new String(kmer);

		int next = 0;
		try {
			next = reader.read();
		} catch(IOException e) {}

		if(next == -1) EndFile = true;
		else {
			kmer[kmer.length - 1] = (char) next;
		}
		//increment the starting position counter
		posCount++;
	}
}