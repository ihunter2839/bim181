/*
argument 1 --> file name
argument 2 --> task
*/

import java.io.*;
import java.util.*;

public class motifs {
	public static BufferedReader reader;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		File inputFile = new File(args[0]);
		reader = new BufferedReader(new FileReader(inputFile));

		switch(args[1]){
			case "median":
				medianString();
				break;
			case "profile":
				profileKmer();
				break;
			case "greedy":
				greedyMotif();
				break;
			case "random":
				randomMotif();
				break;
			case "dist":
				findDist();
				break;
			case "gibbs":
				gibbsMotifs();
				break;
		}
	}

	public static void gibbsMotifs() throws IOException {
		String k_t_N = reader.readLine();
		String[] ktn = k_t_N.split(" ");
		//k is length of kmer
		//t is number of strings
		//n is number of inner iteration of the sampler
		int k = Integer.parseInt(ktn[0]);
		int t = Integer.parseInt(ktn[1]);
		int n = Integer.parseInt(ktn[2]);
		//populate with dna strings
		ArrayList<String> dna = new ArrayList<String>();
		while(true){
			String temp = reader.readLine();
			if(temp == null) break;
			dna.add(temp);
		}

		ArrayList<String> globalBestMotifs = new ArrayList<String>();
		int globalMinScore = k * t;
		int l = dna.get(0).length();

		//randomly select starting kmers 20 times
		for(int z=0; z<200; z++){
			//DONT SEED THE RANDOM TODO
			Random random = new Random();
			ArrayList<String> curMotifs = new ArrayList<String>();
			//generate random starting kmers
			for(String seq : dna){
				//int ran = random.nextInt(l-k+1);
				int ran = (int) Math.floor(Math.random() * (l-k+1));
				String sub = seq.substring(ran, ran+k);
				curMotifs.add(sub);
			}
			for(int y=0; y<n; y++){
				//pick a random string from dna to remove
				int removeIndex = random.nextInt(t);
				//int removeIndex = (int) Math.floor(Math.random() * t);
				//remove it from curMotifs
				//save the string where the removed motif originated
				String removeString = dna.get(removeIndex);
				curMotifs.remove(removeIndex);
				//generate a profile from the t-1 strings 
				double[][] curProfile = createProfile(curMotifs, k);
				//generate profile random motif from the removed string
				String profileRanMotif = genRanMotif(curProfile, curMotifs, removeString, k);
				curMotifs.add(removeIndex, profileRanMotif);
				//generate new profile and score it
				double[][] tempProfile = createProfile(curMotifs, k);
				int curScore = scoreProfile(curMotifs, tempProfile);
				if(curScore < globalMinScore){
					System.out.println("curScore= " + curScore);
					System.out.println("globalMinScore= " + globalMinScore);
					System.out.println("Answer is");
					for(String m : curMotifs){
						System.out.println(m);
					}
					globalBestMotifs = curMotifs;
					globalMinScore = curScore;
				}
			}
		}

		//for(String s : globalBestMotifs){
		//	System.out.println(s);
		//}

		/*ArrayList<String> answer = new ArrayList<String>();
		answer.add("TCTCGGGG");
		answer.add("CCAAGGTG");
		answer.add("TACAGGCG");
		answer.add("TTCAGGTG");
		answer.add("TCCACGTG");

		double[][] ansProfile = createProfile(answer, k);
		System.out.println("Answer score is " + scoreProfile(answer, ansProfile));*/

	}

	public static String genRanMotif(double[][] profile, ArrayList<String> motifs, String seq, int k){
		//stores the probability of each kmer in seq
		double[] kmerProbs = new double[seq.length()-k+1];
		//for each kmer in seq, calculate probability
		for(int i=0; i<=seq.length() - k; i++){
			double curProb = 1;
			String curKmer = seq.substring(i, i+k);
			for(int j=0; j<k; j++){
				char curChar = curKmer.charAt(j);
				switch(curChar){
					case 'A':
						curProb *= profile[0][j];
						break;
					case 'C':
						curProb *= profile[1][j];
						break;
					case 'G':
						curProb *= profile[2][j];
						break;
					case 'T':
						curProb *= profile[3][j];
						break;
				}
			}
			kmerProbs[i] = curProb;
		}
		//use the probability array to generate a random string
		int newMotifStart = genRandomFromProbs(kmerProbs);
		String newMotif = seq.substring(newMotifStart, newMotifStart+k);
		return newMotif;
	}

	public static int genRandomFromProbs(double[] probs){
		double sum = 0;
		//sum all the probabilites as to nomralize
		for(double d : probs) {
			sum += d;
		}
		//array holding the normalized probabilites
		double[] normProbs = new double[probs.length];
		for(int i=0; i<probs.length; i++){
			normProbs[i] = probs[i] / sum;
		}
		double ranVal = Math.random();
		double curVal = 0;
		//increment curval until curVal is greater than the ranVal
		//guarenteed to return since sum of normProbs is 1 
		//math.random is from (0->1]
		for(int j=0; j<normProbs.length; j++){
			curVal += normProbs[j];
			if(curVal >= ranVal) return j;
		}
		return -1;
	}


	public static void findDist() throws IOException {
		String pattern = reader.readLine();
		int k = pattern.length();
		String seqsLine = reader.readLine();
		String[] seqs = seqsLine.split(" ");
		int dist = 0;

		for(String s : seqs){
			int curDist = k;
			for(int i=0; i<=s.length()-k; i++){
				String sub = s.substring(i,i+k);
				int temp = ham(sub, pattern);
				if(temp < curDist) curDist = temp;
			}
			dist += curDist;
		}
		System.out.println(dist);
    }

	public static void randomMotif() throws IOException {
		String kAndt = reader.readLine();
		String[] zit = kAndt.split(" ");
		int k = Integer.parseInt(zit[0]);
		int t = Integer.parseInt(zit[1]);

		ArrayList<String> strings = new ArrayList<String>();
		while(true){
			String temp = reader.readLine();
			if(temp == null) break;
			strings.add(temp);
		}

		int l = strings.get(0).length();
		ArrayList<String> bestMotifs = new ArrayList<String>();
		int minScore = k * t;


		for(int i=0; i<1000; i++){
			ArrayList<String> motifs = new ArrayList<String>();
			//generate initial random motifs 
			for(String s : strings){
				//generate random starting position
				//in the range 0 <= x < l-k+1
				int ran = (int) Math.floor(Math.random() * (l-k+1));
				String sub = s.substring(ran, ran+k);
				//System.out.println(sub);
				motifs.add(sub);
			}
			//create profile from initial motifs
			double[][] curProf = createProfile(motifs, k);
			//for(double[] d : curProf){
			//	System.out.println(Arrays.toString(d));
			//}
			//System.out.println("original motifs score");
			int curScore = scoreProfile(motifs, curProf);
			//update the motifs based on the profile until min
			//has been foun
			while(true){
				ArrayList<String> temp = new ArrayList<String>();
				for(String seq : strings){
					String newKmer = profileKmer2(seq, k, curProf);
					temp.add(newKmer);
				}
				double[][] tempProf = createProfile(temp, k);
				//System.out.println("tempScore");
				int tempScore = scoreProfile(temp, tempProf);
				if(tempScore >= curScore) break;
				else {
					curScore = tempScore;
					motifs = temp;
					curProf = tempProf;
				}

			}
			if(curScore < minScore) {
				minScore = curScore;
				bestMotifs = motifs;
			}
		}

		for(String b : bestMotifs){
			System.out.println(b);
		}
	}

	public static void greedyMotif() throws IOException {
		String kAndt = reader.readLine();
		String[] zit = kAndt.split(" ");
		int k = Integer.parseInt(zit[0]);
		int t = Integer.parseInt(zit[1]);

		ArrayList<String> strings = new ArrayList<String>();
		while(true){
			String temp = reader.readLine();
			if(temp == null) break;
			strings.add(temp);
		}

		String string1 = strings.get(0);
		int l = string1.length();
		//score can never be greater than all t motifs with dist k
		int minScore = k * t;
		ArrayList<String> bestMotifs = new ArrayList<String>();
		//start by iterating through all kmers in strings1
		for(int i=0; i<=l-k; i++){
			String sub = string1.substring(i, i+k);
			ArrayList<String> motifs = new ArrayList<String>();
			motifs.add(sub);
			double[][] profile = createProfile(motifs, k);
			//System.out.println(sub);	
			//find profile-most probable kmer in next string
			for(int j=1; j<t; j++){
				String cur = profileKmer2(strings.get(j), k, profile);
				motifs.add(cur);
				profile = createProfile(motifs, k);
			}
			int score = scoreProfile(motifs, profile);
			if(score < minScore){
				bestMotifs = motifs;
				minScore = score;
			}
			//System.out.println();
		}
		for(String m : bestMotifs){
			System.out.println(m);
		}
	}

	public static int scoreProfile(ArrayList<String> motifs, double[][] profile){
		String consensus = "";
		int k = profile[0].length;
		for(int i=0; i<k; i++){
			int maxVal = 0;
			for(int j=1; j<4; j++){
				if(profile[j][i] > profile[maxVal][i]) maxVal = j;
			}
			switch(maxVal){
				case 0:
					consensus += "A";
					break;
				case 1:
					consensus += "C";
					break;
				case 2:
					consensus += "G";
					break;
				case 3:
					consensus += "T";
					break;
			}
		}
		int dist = 0;
		for(String s : motifs){
			dist += ham(s, consensus);
		}
		//System.out.println(dist);
		//System.out.println(consensus);
		return dist;
	}

	public static String profileKmer2(String seq, int k, double[][] profile) {
		double max = -1;
		String maxString = "";
		int l = seq.length();
		for(int a=0; a<=l-k;a++){
			double curFreq = 1;
			String sub = seq.substring(a, a+k);
			for(int b=0; b<sub.length(); b++){
				char curChar = sub.charAt(b);
				switch(curChar){
					case 'A':
						curFreq *= profile[0][b];
						break;
					case 'C':
						curFreq *= profile[1][b];
						break;
					case 'G':
						curFreq *= profile[2][b];
						break;
					case 'T':
						curFreq *= profile[3][b];
						break;
				}
			}
			if(curFreq > max) {
				max = curFreq;
				maxString = sub;
			}
		}
		//System.out.println(maxString);
		return maxString;
	}

	public static double[][] createProfile(ArrayList<String> seqs, int k){
		double[][] prof = new double[4][k];
		for(String s : seqs){
			for(int i=0; i<k; i++){
				char curChar = s.charAt(i);
				switch(curChar){
					case 'A': 
						prof[0][i] += 1;
						break;
					case 'C':
						prof[1][i] += 1;
						break;
					case 'G':
						prof[2][i] += 1;
						break;
					case 'T':
						prof[3][i] += 1;
						break;
				}
			}
		}
		double size = (double) seqs.size();
		for(int a=0; a<4; a++){
			for(int b=0; b<k; b++){
				//with pseudocounts
				prof[a][b] += 1;
				prof[a][b] /= (size+4);
				//without pseudocounts
				//prof[a][b] /= size;
			}
		}
		return prof;
	}

	public static void profileKmer() throws IOException {
		String seq = reader.readLine();
		int k = Integer.parseInt(reader.readLine());
		//read in profile
		// 4 lines of length k
		double[][] profile = new double[4][k];
		for(int i=0; i<4; i++){
			String temp = reader.readLine();
			String[] tempProf = temp.split(" ");
			for(int j=0; j<k; j++){
				profile[i][j] = Double.parseDouble(tempProf[j]);
			}
		}
		//print the profile to system.out
		/*for(int z=0; z<4; z++){
			for(int y=0; y<k; y++){
				System.out.print(profile[z][y] + " ");
			}
			System.out.println();
		}*/

		double max = 0;
		String maxString = "";
		int l = seq.length();
		for(int a=0; a<=l-k;a++){
			double curFreq = 1;
			String sub = seq.substring(a, a+k);
			for(int b=0; b<sub.length(); b++){
				char curChar = sub.charAt(b);
				switch(curChar){
					case 'A':
						curFreq *= profile[0][b];
						break;
					case 'C':
						curFreq *= profile[1][b];
						break;
					case 'G':
						curFreq *= profile[2][b];
						break;
					case 'T':
						curFreq *= profile[3][b];
						break;
				}
			}
			if(curFreq > max) {
				max = curFreq;
				maxString = sub;
			}
		}

		System.out.println(maxString);

	}

	public static void medianString() throws IOException {
		int k = Integer.parseInt(reader.readLine());
		//set up array to represent kmers
		int numKmers = (int) Math.pow(4,k);
		int[] kmers = new int[numKmers];
		//add all dna strings to list
		ArrayList<String> strings = new ArrayList<String>();
		while(true){
			String temp = reader.readLine();
			if(temp == null) break;
			strings.add(temp);
		}
		//for each kmer, iterate through each string to find the minHam distance
		//add minHam for current string to minHam of previous strings
		for(int i=0; i<numKmers; i++){
			String curPattern = numberToPattern(i, k);
			for(String s : strings){
				//hamming distance can never be greater than k
				int min = k + 1;
				for(int j=0; j<s.length()-k; j++){
					String subString = s.substring(j, j+k);
					int dist = ham(curPattern, subString);
					if(dist < min) min = dist;
				}
				kmers[i] = kmers[i] + min;
			}
		}
		//find smallest value(s) in kmers
		int minDist = kmers[0];
		ArrayList<Integer> smallest = new ArrayList<Integer>();
		smallest.add(0);
		for(int a=1; a<numKmers; a++){
			if(kmers[a] < minDist){
				minDist = kmers[a];
				smallest.clear();
				smallest.add(a);
			}
			else if(kmers[a] == minDist){
				smallest.add(a);
			}
		}
		int n = smallest.get(0);
		System.out.println(numberToPattern(n, k));

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
		return pattern;
	}
}