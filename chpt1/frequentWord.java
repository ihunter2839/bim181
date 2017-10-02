import java.util.*;
import java.io.*;

public class frequentWord{

	public static boolean EndFile = false;
	public static BufferedReader reader;
	public static ArrayList<Integer> mostFreqPatterns;
	//arg 1 is file name
	//arg 2 is kmer length
	public static void main(String[] args){

		File textFile = new File(args[0]);
		double kmerLength = 0;


		try{
			kmerLength = Double.parseDouble(args[1]);
		} catch(NullPointerException e){}

		try{
			reader = new BufferedReader(new FileReader(textFile));
		} catch(FileNotFoundException e){}

		//create array of size 4^k
		int arraySize = (int) Math.pow(4, kmerLength);
		int[] patternCount = new int[arraySize];

		char[] curKmer = new char[(int) kmerLength];
		populateKmer(curKmer);

		while(!EndFile){
			//create string from kmer array
			String curPattern = new String(curKmer);
			//convert string to value
			int patternVal = freqArrayTool.patternToNumber(curPattern);
			patternCount[patternVal] = ++patternCount[patternVal];
			updateKmer(curKmer);
		}

		int maxPattern = 0;
		mostFreqPatterns = new ArrayList<Integer>();

		for(int i =0; i<patternCount.length; i++){
			if(patternCount[i] > maxPattern) {
				maxPattern = patternCount[i];
				mostFreqPatterns.clear();
				mostFreqPatterns.add(i);
			}
			else if(patternCount[i] == maxPattern){
				mostFreqPatterns.add(i);
			}
		}

		String[] patterns = new String[mostFreqPatterns.size()];
		for(int j=0; j<patterns.length; j++){
			int valueTemp = (int) mostFreqPatterns.get(j);
			String patternTemp = freqArrayTool.numberToPattern2(valueTemp,(int) kmerLength);
			System.out.println(patternTemp);
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
	}
}