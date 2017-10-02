import java.util.*;
import java.io.*;

public class euler {
	public static BufferedReader reader;
	public static File input;

	public static void main(String[] args) throws FileNotFoundException, IOException{
		String task = args[0];
		input = new File(args[1]);
		reader = new BufferedReader(new FileReader(input));

		switch(task){
			case "cycle":
				eulerianCycle2();
				break;
			case "path":
				eulerianPath();
				break;
		}
	}

	public static void eulerianPath() throws IOException {
		HashMap<Integer, ArrayList<Integer>> adjList = new HashMap<Integer, ArrayList<Integer>>();
		HashMap<Integer, ArrayList<Integer>> incList = new HashMap<Integer, ArrayList<Integer>>();
		int curNodeVal = 0;
		while(true){
			String wholeLine = reader.readLine();
			if(wholeLine == null) break;
			//split line of form
			//0 -> 1,2,3...
			String[] splitLine = wholeLine.split(" ");
			//discard first two elements of split
			curNodeVal = Integer.parseInt(splitLine[0]);
			String edgesString = splitLine[2];
			String[] edges = edgesString.split(",");
			//temp 4 now holds list of all edges
			
			//check if curNode has an adjacency list
			//if not make it
			if(adjList.get(curNodeVal) == null){
				adjList.put(curNodeVal, new ArrayList<Integer>());
			}
			//check if curNode has an incident list
			//if not make it
			if(incList.get(curNodeVal) == null){
				incList.put(curNodeVal, new ArrayList<Integer>());
			}
			for(int i=0; i<edges.length; i++){
				// curNodeVal -> endNode
				int endNode = Integer.parseInt(edges[i]);
				//add endnode to the adjacency list of curNodeVal
				adjList.get(curNodeVal).add(endNode);
				//check if endnode has been added to adjList, 
				//if not add it
				if(adjList.get(endNode) == null){
					adjList.put(endNode, new ArrayList<Integer>());
				}
				//check if endnode has been added to incList
				//if not add it
				if(incList.get(endNode) == null){
					incList.put(endNode, new ArrayList<Integer>());
				}
				//add curNodeVal to incident list of endNode
				incList.get(endNode).add(curNodeVal);
			}
		}
		//every node has an adjacency and incident list with
		// size >= 0
		Iterator<HashMap.Entry<Integer, ArrayList<Integer>>> adjIt = adjList.entrySet().iterator();
		Iterator<HashMap.Entry<Integer, ArrayList<Integer>>> incIt = incList.entrySet().iterator();
		int start = -1;
		int end = -1;
		while(adjIt.hasNext()){
			HashMap.Entry<Integer, ArrayList<Integer>> curEntry = adjIt.next();
			int curNode = curEntry.getKey();
			ArrayList<Integer> edgesOut = adjList.get(curNode);
			ArrayList<Integer> edgesIn = incList.get(curNode);
			//if |in| != |out| then unbalanced node has been found
			if(edgesOut.size() != edgesIn.size()){
				// |out| > |in| == start
				if(edgesOut.size() > edgesIn.size()) start = curNode;
				// |in| > |out| == end
				else end = curNode;
			}
		}

		//construct a path from start to end
		ArrayList<Integer> path = new ArrayList<Integer>();
		ArrayList<Integer> unexplored = new ArrayList<Integer>();
		int curNode = start;
		int nextNode = -1;

		while(curNode != end){
			//add current to path
			path.add(curNode);
			//get the next node to explore
			nextNode = adjList.get(curNode).get(0);
			//remove the edge that was explored
			adjList.get(curNode).remove(0);
			//if the current node still has unexplored edges,
			//add to unexplored
			if(adjList.get(curNode).size() != 0) unexplored.add(curNode);
			//update curNode to be nextNode
			curNode = nextNode;
		}
		path.add(end);
		if(adjList.get(end).size() != 0 && unexplored.indexOf(end) == -1) unexplored.add(end);

		//once path from start to finish is found
		//visit all unexplored nodes

		while(unexplored.size() != 0){
			ArrayList<Integer> tempPath = new ArrayList<Integer>();
			int newStart = unexplored.get(0);
			unexplored.remove(0);
			int notStart = 0;
			curNode = newStart;
			nextNode = -1;

			while(notStart == 0 || curNode != newStart){
				//add current to temp path
				tempPath.add(curNode);
				//get the next node to explore
				nextNode = adjList.get(curNode).get(0);
				//remove the edge that will be explored
				adjList.get(curNode).remove(0);
				//if the current node has unexplored edges
				//add to unexplored
				if(adjList.get(curNode).size() != 0){
					unexplored.add(curNode);
				}
				curNode = nextNode;
				notStart++;
			}
			//combine the temp path and the full path
			int indexOfComb = path.indexOf(newStart);
			/*
			for(Integer s : path){
				System.out.print(s + " ");
			}
			System.out.println();
			for(Integer y : tempPath){
				System.out.print(y + " ");
			}
			System.out.print("newStart is " + newStart);
			System.out.println();
			System.out.println("Index of comb is " + indexOfComb);
			*/

			int tempPathPos = 0;
			for(int a=indexOfComb; a<tempPath.size() + indexOfComb; a++){
				path.add(a, tempPath.get(tempPathPos));
				tempPathPos++;
			}
			unexplored = removeExplored2(unexplored, adjList);
		}

		for(int z=0; z<path.size(); z++){
			System.out.print(path.get(z));
			if(z != path.size()-1) System.out.print("->");
		}

	}

	public static ArrayList<Integer> removeExplored2(ArrayList<Integer> unexplored, HashMap<Integer, ArrayList<Integer>> adjList){
		for(int a=0; a<unexplored.size(); a++){
			if(adjList.get(unexplored.get(a)).size() == 0){
				unexplored.remove(a);
				a--;
			}
		}
		return unexplored;
	}

	public static ArrayList<Integer> removeExplored(ArrayList<Integer> unexplored, ArrayList<ArrayList<Integer>> adjList){
		for(int a=0; a<unexplored.size(); a++){
			if(adjList.get(unexplored.get(a)).size() == 0){
				unexplored.remove(a);
				a--;
			}
		}
		return unexplored;
	}


	public static int adjListSize(ArrayList<ArrayList<Integer>> adj){
		int size = 0;
		for(int i=0; i<adj.size(); i++){
			size += adj.get(i).size();
		}
		return size;
	}

	public static void eulerianCycle2() throws IOException {
		//read in the adjacency list
		ArrayList<ArrayList<Integer>> adjList = new ArrayList<ArrayList<Integer>>();
		int curNode1 = 0;
		while(true){
			String temp1 = reader.readLine();
			if(temp1 == null) break;
			//split line of form
			//0 -> 1,2,3...
			String[] temp2 = temp1.split(" ");
			//discard first two elements of split
			curNode1 = Integer.parseInt(temp2[0]);
			String temp3 = temp2[2];
			String[] temp4 = temp3.split(",");
			//temp 4 now holds list of all edges
			//create new list for curNode
			//add all edges from temp4
			if(curNode1 >= adjList.size()){
				for(int j=adjList.size(); j<=curNode1; j++){
					adjList.add(j, new ArrayList<Integer>());
				}
			}
			//adjList.add(curNode1, new ArrayList<Integer>());
			for(int i=0; i<temp4.length; i++){
				adjList.get(curNode1).add(Integer.parseInt(temp4[i]));
			}
		}
		Random rand = new Random();
		int startNode = rand.nextInt(adjList.size());
		ArrayList<Integer> cycle = new ArrayList<Integer>();
		ArrayList<Integer> unexplored = new ArrayList<Integer>();
		int curNode = startNode;
		int nextNode = 0;
		int notStart = 0;


		while(notStart == 0 || curNode != startNode){
			cycle.add(curNode);
			nextNode = adjList.get(curNode).get(0);
			adjList.get(curNode).remove(0);
			if(adjList.get(curNode).size() != 0){
				unexplored.add(curNode);
			}
			curNode = nextNode;
			notStart++;
		}
		//remove nodes from unused that have been exhausted
		unexplored = removeExplored(unexplored, adjList);
		//pick a node with unexplored edges. Traverse cycle from selected node, then
		//randomly walk
		while(unexplored.size() != 0){
			ArrayList<Integer> tempCycle = new ArrayList<Integer>();
			startNode = unexplored.get(0);
			curNode = startNode;
			nextNode = 0;
			notStart = 0;
			int indexOfComb = cycle.indexOf(startNode);
			for(int b=indexOfComb; b<cycle.size(); b++){
				tempCycle.add(cycle.get(b));
			}
			for(int c=0; c<indexOfComb; c++){
				tempCycle.add(cycle.get(c));
			}
			cycle = tempCycle;
			while(notStart == 0 || curNode != startNode){
				cycle.add(curNode);
				nextNode = adjList.get(curNode).get(0);
				adjList.get(curNode).remove(0);
				if(adjList.get(curNode).size() != 0){
					unexplored.add(curNode);
				}
				curNode = nextNode;
				notStart++;
			}
			unexplored = removeExplored(unexplored, adjList);
		}
		cycle.add(startNode);

		for(int z=0; z<cycle.size(); z++){
			System.out.print(cycle.get(z));
			if(z!= cycle.size()-1){
				System.out.print("->");
			}
		}
		/*
		System.out.println();
		for(Integer y : unexplored){
			System.out.print(y + " ");
		}*/
	}

	public static void eulerianCycle() throws IOException {
		//read in the adjacency list
		ArrayList<ArrayList<Integer>> adjList = new ArrayList<ArrayList<Integer>>();
		int curNode = 0;
		while(true){
			String temp1 = reader.readLine();
			if(temp1 == null) break;
			//split line of form
			//0 -> 1,2,3...
			String[] temp2 = temp1.split(" ");
			//discard first two elements of split
			String temp3 = temp2[2];
			String[] temp4 = temp3.split(",");
			//temp 4 now holds list of all edges
			//create new list for curNode
			//add all edges from temp4
			adjList.add(curNode, new ArrayList<Integer>());
			for(int i=0; i<temp4.length; i++){
				adjList.get(curNode).add(Integer.parseInt(temp4[i]));
			}
			curNode++;
		}

		//pick a random node to start
		Random rand = new Random();
		int startNode = rand.nextInt(adjList.size());

		ArrayList<Integer> out = genCycle(adjList, startNode);
		for(int z=0; z<out.size(); z++){
			System.out.print(out.get(z));
			if(z != out.size()-1){
				System.out.print("->");
			}
		}

		//add starting node to cycle and get it started

		//print out the adjList
		/*for(int a=0; a<adjList.size(); a++){
			ArrayList<Integer> temp = adjList.get(a);
			System.out.print(a + " -> ");
			for(Integer s : temp){
				System.out.print(s + ",");
			}
			System.out.println();
		}*/
	}

	public static ArrayList<Integer> genCycle(ArrayList<ArrayList<Integer>> adj, int start){
		//get edges of current node
		int curNode = start;
		int nextNode = 0;
		ArrayList<Integer> unusedEdges = new ArrayList<Integer>();
		ArrayList<Integer> cycle = new ArrayList<Integer>();
		cycle.add(start);

		while(true){
			ArrayList<Integer> curEdges = adj.get(curNode);
			//if the current node has unexplored edges
			if(curEdges.size() > 0){
				//get next node that can be reached
				nextNode = curEdges.get(0);
				//add it to cycle
				cycle.add(nextNode);
				//System.out.println("Adding " + nextNode);
				//remove the traversed edge from adj
				adj.get(curNode).remove(0);
				//if the next node has unused edges, add to list
				if(adj.get(nextNode).size() > 1){
					unusedEdges.add(nextNode);
					//System.out.println("Adding " + nextNode + " to unused");
				}
				curNode = nextNode;
			}
			//if there are unused edges
			else if(unusedEdges.size() > 0){
				//cycle.add(start);
				//System.out.println();
				ArrayList<Integer> cyclePrime = genCycle(adj, unusedEdges.get(0));
				unusedEdges.remove(0);
				//combine the cycles
				int indexOfComb = cycle.indexOf(cyclePrime.get(0));
				//System.out.println("Starting comb");
				for(int i=indexOfComb + 1; i<cycle.size(); i++){
					//System.out.println(cycle.get(i));
					cyclePrime.add(cycle.get(i));
				}
				//System.out.println("Second comb");
				for(int j=1; j<=indexOfComb; j++){
					//System.out.println(cycle.get(j));
					cyclePrime.add(cycle.get(j));
				}
				cycle = cyclePrime;
			}
			else {
				break;
			}
		}
		return cycle;

	}
}