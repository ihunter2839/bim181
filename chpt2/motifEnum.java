import java.util.*;
import java.io.*;

public class motifEnum{

	public static String[] bases;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		File input = new File(args[0]);
		BufferedReader reader = new BufferedReader(new FileReader(input));

		//First line of data is of form
		//k d
		int k = Character.getNumericValue(reader.read());
		reader.read();
		int muts = Character.getNumericValue(reader.read());
		int size = (int) Math.pow(4, (double) k);
		int[] present = new int[size];
		for(int z=0; z<present.length; z++){
			present[z] = 1;
		}

		bases = new String[4];
		bases[0] = "A";
		bases[1] = "C";
		bases[2] = "G";
		bases[3] = "T";

		reader.readLine();


		while(true){
			String tempGen = reader.readLine();
			//System.out.println(tempGen);
			int[] tempPres = new int[size];

			if(tempGen == null) break;

			for(int i=0; i<=tempGen.length() - k; i++){
				String curPat = tempGen.substring(i, i + k); 
				ArrayList<String> pats = genNeighs(curPat, muts);
				for(String s : pats){
					int val = patternToNumber(s);
					if(present[val] == 1){
						tempPres[val] = 1;
					}
				}
			}
			present = tempPres;
		}

		for(int j=0; j<present.length; j++){
			if(present[j] == 1){
				String temp = numberToPattern(j, k);
				System.out.print(temp + " ");
			}
		}

	}

	public static ArrayList<String> genNeighs(String kmer, int d){
		
		ArrayList<String> neighbors = new ArrayList<String>();
		neighbors.add("");

		int kmerLeng = kmer.length();

		for(int i=1; i<= kmerLeng; i++){
			//only grab the correct pattern substring once
			String oriKmer = kmer.substring(kmerLeng-i, kmerLeng);
			//same with arraylist size
			int neighLeng = neighbors.size(); 
			//temp to add all new words of length i

			ArrayList<String> temp = new ArrayList<String>();

			for(int j=0; j<neighLeng; j++){
				for(String base : bases){
					String curS = base + neighbors.get(j);
					if(ham(curS, oriKmer) <= d){
						temp.add(curS);
					}
				}
			}

			neighbors = temp;
		}

		return neighbors;
	}

	public static int ham(String s1, String s2){
		int dist = 0;
		//accommodation for strings of different lengths 
		int s1L = s1.length();
		int s2L = s2.length();
		int min = Math.min(s1L, s2L);

		for(int i=0; i<min; i++){
			if(s1.charAt(i) != s2.charAt(i)) dist++;
		}
		//add difference in length if one exists
		dist += s1L - s2L;
		return dist;
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

	public static String numberToPattern(int valueIn, int lengthIn){
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