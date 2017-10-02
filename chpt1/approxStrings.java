import java.io.*;
import java.util.*;

public class approxStrings{

	public static String[] bases;
	public static BufferedReader reader;

	public static void main(String[] args) throws FileNotFoundException, IOException{
		File inputFile = new File(args[0]);
		reader = new BufferedReader(new FileReader(inputFile));

		String genome = reader.readLine();
		int k = Integer.parseInt(reader.readLine());
		int numMut = Integer.parseInt(reader.readLine());

		bases = new String[4];
		bases[0] = "A";
		bases[1] = "C";
		bases[2] = "G";
		bases[3] = "T";

		int freqSize = (int) Math.pow(4, (double) k);
		int[] freq = new int[freqSize];

		for(int i=0; i<=genome.length() - k; i++){
			String temp = genome.substring(i, i+k);
			String tempRev = reverse(temp);
			ArrayList<String> tempList = genNeighs(temp, numMut);
			ArrayList<String> tempList2 = genNeighs(tempRev, numMut);
			for(String s : tempList){
				int val = freqArrayTool.patternToNumber(s);
				freq[val] = ++freq[val];
			}
			for(String r : tempList2){
				int val = freqArrayTool.patternToNumber(r);
				freq[val] = ++freq[val];
			}
		}

		ArrayList<Integer> output = new ArrayList<Integer>();
		int max = 0;
		for(int f=0; f < freq.length; f++){
			if(freq[f] > max){
				output.clear();
				output.add(f);
				max = freq[f];
			}
			else if(freq[f] == max){
				output.add(f);
			}
		}

		for(int o : output){
			System.out.println(freqArrayTool.numberToPattern2(o, k));
		}


		//ArrayList<String> neighborhood = genNeighs(pattern, numMut);
		//for(String s : neighborhood){
		//	System.out.println(s);
		//}
		
		//ArrayList<Integer> output = new ArrayList<Integer>();
		//int patternLeng = pattern.length();
		//int genomeLeng = genome.length();
		//for(int i=0; i<=(genomeLeng - patternLeng); i++){
		//	String temp = genome.substring(i, i+patternLeng);
		//	if(ham(temp, pattern) <= numMut){
		//		output.add(i);
		//	}
		//}

		//for(int a : output){
		//	System.out.print(a + " ");
		//}
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

	public static String reverse(String s){
		String temp = "";
		for(int i=0; i<s.length(); i++){
			char c = s.charAt(i);
			switch(c){
				case 'A': 
					temp = "T" + temp;
					break;
				case 'C':
					temp = "G" + temp;
					break;
				case 'G':
					temp = "C" + temp;
					break;
				case 'T':
					temp = "A" + temp;
					break;
			}
		}
		return temp;
	}
}