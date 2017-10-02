import java.io.*;
import java.util.*;

public class findSkew {
	//define skew to be (occurences of g before index i) - (occurences of c before index i)
	//find the indeces that minimize skew

	public static BufferedReader reader;
	public static void main(String[] args){

		ArrayList<Integer> minInd = new ArrayList<Integer>();
		int gCount = 0;
		int cCount = 0;
		int minCount = 0;
		int ind = 1;

		File inputFile = new File(args[0]);

		try{
			reader = new BufferedReader(new FileReader(inputFile));
		} catch(FileNotFoundException e){}

		while(true){
			int curChar = 0;
			try{
				curChar = reader.read();
			} catch(IOException e){}

			if(curChar == -1) break;

			switch((char) curChar){
				case 'C': 
					cCount++;
					break;
				case 'G':
					gCount++;
					break;
			}
			//new minimum skew found
			if(gCount - cCount < minCount){
				minCount = gCount - cCount;
				minInd.clear();
				minInd.add(ind);
			}
			//skew matching minSkew found
			else if(gCount - cCount == minCount){
				minInd.add(ind);
			}

			ind++;
		}

		for(int i=0; i<minInd.size(); i++){
			System.out.print(minInd.get(i) + " ");
		}

		try{
			reader.close();
		} catch(IOException e){}
	}	
}