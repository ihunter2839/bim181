import java.util.*;
import java.io.*;
import java.lang.*;

public class freqArrayTool {
	public static void main(String[] args) throws FileNotFoundException, IOException{
		File input = new File(args[0]);
		BufferedReader reader = new BufferedReader(new FileReader(input));

		String genome = reader.readLine();
		int k = Integer.parseInt(reader.readLine());

		int size = (int) Math.pow(4, (double) k);
		int[] freq = new int[size];
		for(int i=0; i<=genome.length() - k; i++){
			String temp = genome.substring(i, i + k);
			int val = patternToNumber(temp);
			freq[val] = ++freq[val];
		}

		for(int f : freq){
			System.out.print(f + " ");
		}
		/*if(args.length == 1){
			patternToNumber(args[0].toUpperCase());
		}
		else if(args.length == 2){
			numberToPattern(args[0], args[1]);
		}
		else {
			System.out.println("freqArrayTool usage:");
			System.out.println("To convert pattern to number--> freqArrayTool [pattern]");
			System.out.println("To convert number to pattern--> freqArrayTool [number] [length]");
		}*/
	}

	public static int patternToNumber(String pattern){
		long value = 0;
		double power = 0;
		for(int i=pattern.length() - 1; i > -1; i--){
			char cur = pattern.charAt(i);
			int lessThan = 0;
			switch (cur){
				case 'A':
					lessThan = 0;
					break;
				case 'C':
					lessThan = 1;
					break;
				case 'G':
					lessThan = 2;
					break;
				case 'T':
					lessThan = 3;
					break;
			}
			value += (Math.pow(4, power) * lessThan);
			power++;
		}

		//System.out.println(value);
		return (int) value;
	}

	public static String numberToPattern(String stringValue, String stringLength){
		double value =  (double) Integer.parseInt(stringValue);
		double length = (double) Integer.parseInt(stringLength);
		String pattern = "";
		for(int i = (int) length - 1; i > -1; i--){
			double charInt = value / (Math.pow(4, i));
			value = value % (Math.pow(4, i));
			switch((int) charInt){
				case 0:
					pattern = pattern + "A";
					break;
				case 1:
					pattern = pattern + "C";
					break;
				case 2:
					pattern = pattern + "G";
					break;
				case 3: 
					pattern = pattern + "T";
					break;
			}
		}
		
		//System.out.println(pattern);
		return pattern;
	}

	public static String numberToPattern2(int valueIn, int lengthIn){
		double value =  (double) valueIn;
		double length = (double) lengthIn;
		String pattern = "";
		for(int i = (int) length - 1; i > -1; i--){
			double charInt = value / (Math.pow(4, i));
			value = value % (Math.pow(4, i));
			switch((int) charInt){
				case 0:
					pattern = pattern + "A";
					break;
				case 1:
					pattern = pattern + "C";
					break;
				case 2:
					pattern = pattern + "G";
					break;
				case 3: 
					pattern = pattern + "T";
					break;
			}
		}
		
		//System.out.println(pattern);
		return pattern;
	}
}