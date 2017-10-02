import java.util.*;
import java.io.*;

public class clumpFinder2 {

	public static BufferedReader reader;
	public static boolean EndFile = false;
	//the same clump may be found in multiple windows, creating duplicates
	//if using the arraylist method
	//instead, create a permanent freqArray that holds a boolean value
	//representing that the pattern was found as a clump in some window
	//public static ArrayList<String> clumps;
	public static int[] clumps;
	public static int clumpReps = 0;

	public static void main(String[] args){

		File inputFile = new File(args[0]);

		try{
			reader = new BufferedReader(new FileReader(inputFile));
		} catch(FileNotFoundException e){}

		//line 1 of file is kmer length
		int kmerLength = 0;
		try{
			kmerLength = Integer.parseInt(reader.readLine());
		} catch(IOException e){}

		//line 2 of file is window size
		int windowSize = 0;
		try{
			windowSize = Integer.parseInt(reader.readLine());
		} catch(IOException f){}

		//line 3 of file is minimum repetition thresh-hold
		int minCount = 0;
		try{
			minCount = Integer.parseInt(reader.readLine());
		} catch(IOException g){}

		//populate window
		char[] curWindow = populateWindow(windowSize);
		
		//initialize clumps
		//clumps = new ArrayList<String>();
		int clumpSize = (int) Math.pow(4, kmerLength);
		clumps = new int[clumpSize];

		//continue to fill clumps until end of file is reached
		while(!EndFile){
			findClumps(kmerLength, windowSize, minCount, curWindow);
			curWindow = updateWindow(curWindow);
		}

		System.out.println(clumpReps);
		
		for(int i=0; i<clumps.length; i++){
			if(clumps[i] == 1){
				String temp = freqArrayTool.numberToPattern2(i, kmerLength);
				System.out.println(temp);
			}
		}

	}

	public static char[] updateWindow(char[] win){
		char[] temp = new char[win.length];
		for(int i=1; i<win.length; i++){
			temp[i-1] = win[i];
		}

		int curChar = 0;
		try{
			curChar = reader.read();
		} catch(IOException e){}

		if(curChar == -1){
			EndFile = true;
			return temp;
		}

		temp[temp.length - 1] = (char) curChar;
		return temp; 
	}

	public static void findClumps(int kmerLeng, int winSize, int min, char[] win){
		clumpReps++;
		//generate and array of size 4^k for all kmer combinations
		int a = (int) Math.pow(4, (double) kmerLeng);
		int[] freqs = new int[a];

		//iterate through window, updating frequencies
		for(int i=0; i<=winSize - kmerLeng; i++){
			//generate kmer
			String temp = "";
			for(int j = i; j < i + kmerLeng; j++){
				temp = temp + win[j];
			}
			//generate kmer value representation
			int val = freqArrayTool.patternToNumber(temp);
			//update kmer freq in this window
			freqs[val] = ++freqs[val];
		}

		//check if any frequencies meet thresh hold
		for(int k=0; k<freqs.length; k++){
			//if thesh hold is met, save the pattern
			if(freqs[k] >= min){
				clumps[k] = 1;
			}
		}

	}

	public static char[] populateWindow(int winSize){
		char[] tempWindow = new char[winSize];

		for(int i=0; i<winSize; i++){
			
			int curChar = 0;
			try{
				curChar = reader.read();
			} catch(IOException e){}
			//if end of file is reached when filling initial window, exit
			//this is not the technically correct behavior, as a kmer could still
			//appear in t times in a window of size < L and therefore appears at least
			//t times in the window of size L 
			if(curChar == -1){
				EndFile = true;
				break;
			}

			tempWindow[i] = (char) curChar;
		}

		return tempWindow;
	}
}