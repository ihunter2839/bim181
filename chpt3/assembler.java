import java.util.*;
import java.io.*;

public class assembler {
	public static File input;
	public static BufferedReader reader;
	
	public static void main(String[] args) throws FileNotFoundException, IOException{
		//input in the form
		//java assembler [task] [inputFile]
		String task = args[0];
		input = new File(args[1]);
		reader = new BufferedReader(new FileReader(input));

		switch(task){
			case "kmerComp":
				kmerCompDriver();
				break;
			case "genPath":
				genomePathDriver();
				break;
			case "overlapGraph":
				overlapGraphDriver();
				break;
			case "deBruijn":
				deBruijnGraphDriver2();
				break;
		}
	}


	public static String suffix(String kmer){
		return (kmer.substring(1, kmer.length()));
	}

	public static String prefix(String kmer){
		return (kmer.substring(0, kmer.length() - 1));
	}

	public static ArrayList<ArrayList<String>> generateOverlapGraph(ArrayList<String> reads){
		//lists are in lexographic order
		//ie
		//first list in graph corresponds to first string in reads
		ArrayList<ArrayList<String>> adjList = new ArrayList<ArrayList<String>>();
		for(int i=0; i<reads.size(); i++){
			String curSuffix = suffix(reads.get(i));
			adjList.add(new ArrayList<String>());
			for(int j=0; j<reads.size(); j++){
				String curRead = reads.get(j);
				String curPrefix = prefix(curRead);
				if(curSuffix.equals(curPrefix)){
					adjList.get(i).add(curRead);
				}
			}
		}
		return adjList;
	}

	public static String fromPathToGenome(ArrayList<String> reads){
		String genome = "";
		int i;
		for(i=0; i<reads.size() - 1; i++){
			//take first character from each string
			genome += reads.get(i).substring(0, 1);
		}
		genome += reads.get(i);
		return genome;
	}

	public static String[] kmerComposition(int k, String seq){
		String[] comp = new String[seq.length() -k + 1];
		for(int i=0; i<= seq.length() - k; i++){
			comp[i] = seq.substring(i, i+k);
		}
		Arrays.sort(comp);
		return comp;
	}

	public static ArrayList<ArrayList<String>> generateDeBruijnGraph(String[] patterns){
		ArrayList<ArrayList<String>> graph = new ArrayList<ArrayList<String>>();
		for(int a=0; a<patterns.length; a++){
			//for every pattern, check if currents suffix overlaps with prefix of
			//another pattern. If so, then there is an edge from the current prefix to 
			//the second prefix
			String curPattern = patterns[a];
			graph.add(a, new ArrayList<String>());
			graph.get(a).add(prefix(curPattern));
			for(int b=0; b<patterns.length; b++){
				String nextPrefix = prefix(patterns[b]);
				if(a==b) continue;
				else if(suffix(curPattern).equals(nextPrefix)){
					graph.get(a).add(nextPrefix);
				}
			}
		}
		return graph;
	}

	public static ArrayList<ArrayList<String>> generateDeBruijnGraph2(String[] patterns){
		ArrayList<ArrayList<String>> adjList = new ArrayList<ArrayList<String>>();
		for(int a=0; a<patterns.length; a++){
			//add edge from prefix to suffix for every kmer
			adjList.add(a, new ArrayList<String>());
			adjList.get(a).add(prefix(patterns[a]));
			adjList.get(a).add(suffix(patterns[a]));
		}
		//scroll through all prefixes, glue equivalent nodes
		for(int b=0; b<adjList.size(); b++){
			for(int c=b+1; c<adjList.size(); c++){
				ArrayList<String> temp1 = adjList.get(b);
				ArrayList<String> temp2 = adjList.get(c);
				//if duplicated is found, copy edges
				if(temp1.get(0).equals(temp2.get(0))){
					for(int i=1; i<temp2.size(); i++){
						temp1.add(temp2.get(i));
						temp2.remove(i);
					}
				}
			}
		}
		//remove islands
		for(int z=0; z<adjList.size(); z++){
			if(adjList.get(z).size() == 1){
				adjList.remove(z);
				z--;
			}
		}
		return adjList;
	}

	public static void deBruijnGraphDriver() throws IOException{
		int k = Integer.parseInt(reader.readLine());
		String seq = reader.readLine();
		String[] kmerComp = kmerComposition(k, seq);
		Arrays.sort(kmerComp);
		ArrayList<ArrayList<String>> output = generateDeBruijnGraph(kmerComp);
		printGraph(output);

	}

	public static void deBruijnGraphDriver2() throws IOException{
		//generate from string
		//int k = Integer.parseInt(reader.readLine());
		//String seq = reader.readLine();
		//String[] kmerComp = kmerComposition(k, seq);
		//generate from kmers
		ArrayList<String> patterns = new ArrayList<String>();
		while(true){
			String temp = reader.readLine();
			if(temp == null) break;
			patterns.add(temp);
		}
		String[] kmerComp = patterns.toArray(new String[patterns.size()]);
		ArrayList<ArrayList<String>> output = generateDeBruijnGraph2(kmerComp);
		printGraph(output);

	}

	public static void printGraph(ArrayList<ArrayList<String>> graph){
		for(int z=0; z<graph.size(); z++){
			ArrayList<String> temp = graph.get(z);
			if(temp.size() == 1) continue;
			else {
				System.out.print(temp.get(0) + " -> ");
				for(int y=1; y<temp.size(); y++){
					System.out.print(temp.get(y));
					if(y != temp.size() - 1) System.out.print(", ");
				}
				System.out.println();
			}
		}
	}

	public static void overlapGraphDriver() throws IOException{
		ArrayList<String> patterns = new ArrayList<String>();
		while(true){
			String temp = reader.readLine();
			if(temp == null) break;
			patterns.add(temp);
		}

		ArrayList<ArrayList<String>> output = generateOverlapGraph(patterns);
		for(int z=0; z<output.size(); z++){
			ArrayList<String> temp = output.get(z);
			for(int y=0; y<temp.size(); y++){
				System.out.println(patterns.get(z) + " -> " + temp.get(y));
			}
		}
	}

	public static void genomePathDriver() throws IOException{
		ArrayList<String> patterns = new ArrayList<String>();
		while(true){
			String temp = reader.readLine();
			if(temp == null) break;
			patterns.add(temp);
		}

		System.out.println(fromPathToGenome(patterns));
	}

	public static void kmerCompDriver() throws IOException {
		int k = Integer.parseInt(reader.readLine());
		String seq = reader.readLine();
		String[] output = kmerComposition(k, seq);
		for(String s : output){
			System.out.println(s);
		}
	}
}