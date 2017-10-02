import java.util.*;
import java.io.*;

public class align{

	//java align [task] [input]
	public static int flag = 1;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		File inputFile = new File(args[1]);
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		String task = args[0];
		switch(task){
			case "longestPath": manhattanDriver(reader);
				break;
			case "longestSubstring": commonSubstringDriver(reader);
				break;
			case "topo": topologicalOrderDriver(reader);
				break;
			case "dagPath": longestPathInDagDriver(reader);
				break;
			case "globe": globalAlignmentDriver(reader);
				break;
			case "local": localAlignmentDriver(reader);
				break;
			case "edit": editDistanceDriver(reader);
				break;
			case "fitting": fittingAlignmentDriver(reader);
				break;
			case "overlap": overlapAlignmentDriver(reader);
				break;
			case "afgap" : affineGapAlignmentDriver2(reader);
				break;
			case "score" : calcScore(reader);
				break;
			case "mid" : middleEdgeDriver(reader);
				break;
			case "linSpace" : linearSpaceAlignmentDrive(reader);
				break;
			case "multiple" : multipleSequenceAlignment(reader);
				break;
		}
	}

	public static void multipleSequenceAlignment(BufferedReader reader) throws IOException{
		//x
		String s0 = " " + reader.readLine();
		//y
		String s1 = " " + reader.readLine();
		//z
		String s2 = " " + reader.readLine();
		int[][][] maxVal = new int[s0.length()][s1.length()][s2.length()];
		int[][][] backPath = new int[s0.length()][s1.length()][s2.length()];
		//(1,1,1)-->(0,1,1) == 7 == s0
		//(1,1,1)-->(1,0,1) == 6 == s1
		//(1,1,1)-->(1,1,0) == 5 == s2
		//(1,1,1)-->(0,0,1) == 4 == s0, s1
		//(1,1,1)-->(1,0,0) == 3 == s1, s2
		//(1,1,1)-->(0,1,0) == 2 == s0, s2
		//(1,1,1)-->(0,0,0) == 1 == s0, s1, s2
		//0 == end of path
		//+1 for matches, no mismatch penalty
		for(int a=0; a<s0.length(); a++){
			for(int b=0; b<s1.length(); b++){
				for(int c=0; c<s2.length(); c++){
					//(0,0,0) case
					if(a==0 && b==0 && c==0){
						maxVal[a][b][c] = 0;
						backPath[a][b][c] = 0;
					}
					//movement along a single axis
					else if(a==0 && b==0){
						maxVal[a][b][c] = 0;
						backPath[a][b][c] = 5;
					}
					else if(a==0 && c==0){
						maxVal[a][b][c] = 0;
						backPath[a][b][c] = 6;
					}
					else if(b==0 && c==0){
						maxVal[a][b][c] = 0;
						backPath[a][b][c] = 7;
					}
					//all scores in planes of axis are 0
					//use diagonals until axis is reached
					else if(a==0){
						maxVal[a][b][c] = 0;
						backPath[a][b][c] = 3;
					}
					else if(b==0){
						maxVal[a][b][c] = 0;
						backPath[a][b][c] = 2;
					}
					else if(c==0){
						maxVal[a][b][c] = 0;
						backPath[a][b][c] = 4;
					}
					//otherwise all indices are greater than 0
					else {
						char c0 = s0.charAt(a);
						char c1 = s1.charAt(b);
						char c2 = s2. charAt(c);
						int iA = a-1;
						int iB = b-1;
						int iC = c-1;
						int delta = (c0==c1 && c0==c2)?1:0;
						//let a,b,c represent no change
						//let A,B,C represent change of 1
						int max = maxVal[iA][iB][iC] + delta;
						int dir = 1;
						if(maxVal[iA][iB][c] > max){
							max = maxVal[iA][iB][c];
							dir = 4;
						}
						if(maxVal[iA][b][iC] > max){
							max = maxVal[iA][b][iC];
							dir = 2;
						}
						if(maxVal[a][iB][iC] > max){
							max = maxVal[a][iB][iC];
							dir = 3;
						}
						if(maxVal[iA][b][c] > max){
							max = maxVal[iA][b][c];
							dir = 7;
						}
						if(maxVal[a][iB][c] > max){
							max = maxVal[a][iB][c];
							dir = 6;
						}
						if(maxVal[a][b][iC] > max){
							max = maxVal[a][b][iC];
							dir = 5;
						}
						maxVal[a][b][c] = max;
						backPath[a][b][c] = dir;
					}

				}
			}
		}

		int x = s0.length()-1;
		int y = s1.length()-1;
		int z = s2.length()-1;
		String out0 = "";
		String out1 = "";
		String out2 = "";

		while(x>=0 || y>=0 || z>=0){
			int dir = backPath[x][y][z];
			switch(dir){
				case 7: out0 = getStr(s0.charAt(x)) + out0;
						out1 = "-" + out1;
						out2 = "-" + out2;
						x--;
						break;
				case 6: out0 = "-" + out0;
						out1 = getStr(s1.charAt(y)) + out1;
						out2 = "-" + out2;
						y--;
						break;
				case 5: out0 = "-" + out0;
						out1 = "-" + out1;
						out2 = getStr(s2.charAt(z)) + out2;
						z--;
						break;
				case 4: out0 = getStr(s0.charAt(x)) + out0;
						out1 = getStr(s1.charAt(y)) + out1;
						out2 = "-" + out2;
						x--; y--;
						break;
				case 3: out0 = "-" + out0;
						out1 = getStr(s1.charAt(y)) + out1;
						out2 = getStr(s2.charAt(z)) + out2;
						y--; z--;
						break;
				case 2: out0 = getStr(s0.charAt(x)) + out0;
						out1 = "-" + out1;
						out2 = getStr(s2.charAt(z)) + out2;
						x--; z--;
						break;
				case 1: out0 = getStr(s0.charAt(x)) + out0;
						out1 = getStr(s1.charAt(y)) + out1;
						out2 = getStr(s2.charAt(z)) + out2;
						x--; y--; z--;
						break;
				case 0: x--; y--; z--;

			}
		}

		System.out.println(maxVal[s0.length()-1][s1.length()-1][s2.length()-1]);
		System.out.println(out0 + '\n' + out1 + '\n' + out2);

	}

	public static void linearSpaceAlignmentDrive(BufferedReader reader) throws IOException{
		File blosumMatrixFile = new File("blosum62");
		BufferedReader readerB = new BufferedReader(new FileReader(blosumMatrixFile));
		String aminoLetterString = readerB.readLine();
		//index 0 is blank
		//index 1-20 are valid aa's in alphabetical order
		aminoLetterString = aminoLetterString.substring(1, aminoLetterString.length());
		String[] aminoLetters = aminoLetterString.split("  ");
		//valid index from 0,0 to 23,23 
		int[][] blomA = new int[24][24];
		int row = 0;
		while(true){
			String grabLine = readerB.readLine();
			if(grabLine == null) break;
			String[] spaceSplit = grabLine.split(" ");
			int curCol = 0;
			for(int col=1; col<spaceSplit.length; col++){
				if(spaceSplit[col].equals("")) continue;
				blomA[row][curCol] = Integer.parseInt(spaceSplit[col]);
				curCol++;
			}
			row++;
		}
		//let a-->row and b-->col
		HashMap<String, Integer> blom = new HashMap<String, Integer>();
		for(int a=1; a<aminoLetters.length; a++){
			for(int b=1; b<aminoLetters.length; b++){
				int val = blomA[a-1][b-1];
				String rowChar = aminoLetters[a];
				String colChar = aminoLetters[b];
				String key = rowChar + colChar;
				blom.put(key, val);
			}
		}

		String nString = " " + reader.readLine();
		String mString = " " + reader.readLine();
		int left = 0;
		int right = mString.length();
		int bottom = 0;
		int top = nString.length();
		String[] output = recursiveAlignment(nString, mString, blom, top, bottom, left, right);

		nString = output[0];
		mString = output[1];

		int score = 0;
		int indel = -5;
		for(int i=0; i<nString.length(); i++){
			char nChar = nString.charAt(i);
			char mChar = mString.charAt(i);
			if(nChar == '-' || mChar == '-'){
				score = score + indel;
			}
			else {
				String key = Character.toString(nChar) + Character.toString(mChar);
				score = score + blom.get(key);
			} 
		}
		System.out.println(score);
		System.out.println(nString);
		System.out.println(mString);

	}

	public static String[] recurse(String nString, String mString, HashMap<String, Integer> blom, int bottom, int top, int left, int right){
		
		String nStringL = nString.substring(bottom, top);
		String mStringL = mString.substring(bottom, top);

	}

	public static String reverseString(String s){
		String r = "";
		for(int a=0; a<s.length(); a++){
			r = getStr(s.charAt(a)) + r;
		}
		return r;
	}

	public static ArrayList<int[]> calcMiddleNodes(String n, String m, int flag, HashMap<String, Integer> blom){
		int[] maxPathVal = new int[n.length()];
		int[] tempVals = new int[n.length()];
		int[] paths = new int[n.length()];
		int indel = -5;
		for(int b=0; b<m.length(); b++){
			for(int a=0; a<n.length(); a++){
				if(b==0){
					tempVals[a] = a * indel;
					paths[a] = (a==0)?-1:1;
				}
				else{
					if(a==0){
						tempVals[a] = maxPathVal[a] + indel;
						paths[a] = 0;
					}
					else {
						String key = getStr(n.charAt(a)) + getStr(m.charAt(b));
						int upVal = tempVals[a-1] + indel;
						int leftVal = maxPathVal[a] + indel;
						int diagVal = maxPathVal[a-1] + blom.get(key);
						int max = diagVal;
						int dir = 2;
						if(upVal > max){
							max = upVal;
							dir = 1;
						}
						if(leftVal > max){
							max = leftVal;
							dir = 0;
						}
						tempVals[a] = max;
						paths[a] = dir;
					}
				}
			}
			if(flag == 1 && b== m.length()-1) break;
			
			for(int r=0; r<tempVals.length; r++){
				maxPathVal[r] = tempVals[r];
			}
		}
		ArrayList<int[]> out = new ArrayList<int[]>();
		out.add(maxPathVal);
		out.add(tempVals);
		out.add(paths);
		return out;		
	}

	public static String[] recursiveAlignment(String nString, String mString, HashMap<String, Integer> blom, int top, int bottom, int left, int right){
		//string conversion	
		String nStringL = nString.substring(bottom, top);
		String mStringL = mString.substring(left, right);

		int midX = (left+right) / 2;

		String mSubF = mString.substring(left, midX+1);
		String mSubR =  reverseString(mStringL.substring(midX, right));
		String nStringR = reverseString(nString.substring(1, nString.length()));

		if(flag == 1){
			System.out.println();
			System.out.println("mf = " + mSubF);
			System.out.println("mr = " + mSubR);
			System.out.println("top:" + top + " bot:" + bottom);
			System.out.println("right:" + right + " left:" + left);
		}	

		if(top == bottom && left == right){
			return new String[] {"", ""};
		}
		else if(top == bottom){
			String s1 = "";
			String s2 = "";
			for(int i = left; i<=right; i++){
				s1 = s1 + "-";
				s2 = s2 + getStr(mString.charAt(i));
			}
			System.out.println("Top==Bottom base case reached");
			return new String[] {s1,s2};
		}
		else if(left == right){
			String s1 = "";
			String s2 = "";
			for(int i=bottom; i<= top; i++){
				s1 = s1 + getStr(nString.charAt(i));
				s2 = s2 + "-";
			}
			System.out.println("Left==Right base case reached");
			return new String[] {s1, s2};
		}

		int indel = -5;

		ArrayList<int[]> valsA = calcMiddleNodes(nString, mSubF, 0, blom);
		int[] maxPathValA = valsA.get(0);
		//solve with nStringForward and mSubForward

		ArrayList<int[]> valsB = calcMiddleNodes(nStringR, mSubR, 1, blom);
		int[] maxPathValB = valsB.get(0);
		int[] tempVals = valsB.get(1);
		int [] paths = valsB.get(2);

		int max = 0;
		int midY = 0;
		//find middle node
		for(int h=0; h<maxPathValA.length; h++){
			int curVal = maxPathValA[h] + tempVals[tempVals.length-h-1];
			if(curVal > max){
				max = curVal;
				midY = h;
			}
		}

		//reverse paths
		for(int f=paths.length-1; f>=paths.length/2; f--){
			int temp = paths[f];
			paths[f] = paths[paths.length-1-f];
			paths[paths.length-1-f] = temp;
		}

		int dir = paths[midY];
		System.out.println(midY + "," + midX);

		if(dir == 2){
			System.out.println("Mid is diag");
			String[] outA = recursiveAlignment(nString, mString, blom, midY+1, bottom, left, midX+1);
			String[] outB = recursiveAlignment(nString, mString, blom, top, midY+1, midX+1, right);
			String s1 = outA[0] + getStr(nString.charAt(midY+1)) + outB[0];
			String s2 = outA[1] + getStr(mString.charAt(midX+1)) + outB[1]; 
			String[] out = {s1, s2};
			return out;
		}
		else if(dir == 0){
			System.out.println("Mid is right");
			String[] outA = recursiveAlignment(nString, mString, blom, midY+1, bottom, left, midX+1);
			String[] outB = recursiveAlignment(nString, mString, blom, top, midY, midX+1, right);
			String s1 = outA[0] + "-" + outB[0];
			String s2 = outA[1] + getStr(mString.charAt(midX)) + outB[1]; 
			String[] out = {s1, s2};
			return out;			
		}
		else {
			System.out.println("Mid is down");
			String[] outA = recursiveAlignment(nString, mString, blom, midY+1, bottom, left, midX+1);
			String[] outB = recursiveAlignment(nString, mString, blom, top, midY+1, midX, right);
			String s1 = outA[0] + getStr(nString.charAt(midY)) + outB[0];
			String s2 = outA[1] + "-" + outB[1]; 
			String[] out = {s1, s2};
			return out;			
		}

		//mid edge is horizontal
		/*if(maxPathValB[midY] + indel > maxPathValB[midY + 1] + blom.get(key)){
			System.out.println("horz");
			String nSubA = nStringF.substring(1, midY+1);
			String mSubA = mSubF.substring(1, mSubF.length());
			System.out.println(nSubA.length() + " " + mSubA.length());
			String[] outA = recursiveAlignment(nSubA, mSubA, blom);
			String nSubB = nStringF.substring(midY, nStringF.length()-1);
			String mSubB = mString.substring(midX+1, mString.length());
			System.out.println(nSubB.length() + " " + mSubB.length() + " " + nSubB);
			String[] outB = recursiveAlignment(nSubB, mSubB, blom);
			System.out.println(outB[0] + ":" + outB[1]);
			String s1 = outA[0] + "-" + outA[1];
			String s2 = outB[0] + getStr(mString.charAt(midX)) + outB[1];
			String[] out = {s1, s2};
			return out;
		}
		else{
			String nSubA = nStringF.substring(1, midY+1);
			String mSubA = mSubF.substring(1, mSubF.length());
			String[] outA = recursiveAlignment(nSubA, mSubA, blom);
			String nSubB = nStringF.substring(midY+1, nStringF.length()-1);
			String mSubB = mString.substring(midX+1, mString.length());
			String[] outB = recursiveAlignment(nSubB, mSubB, blom);
			String s1 = outA[0] + getStr(nStringF.charAt(midY+1)) + outA[1];
			String s2 = outB[0] + getStr(mString.charAt(midX)) + outB[1];
			String[] out = {s1, s2};
			return out;
		}*/

	}

	public static void middleEdgeDriver(BufferedReader reader) throws IOException{
		File blosumMatrixFile = new File("blosum62");
		BufferedReader readerB = new BufferedReader(new FileReader(blosumMatrixFile));
		String aminoLetterString = readerB.readLine();
		//index 0 is blank
		//index 1-20 are valid aa's in alphabetical order
		aminoLetterString = aminoLetterString.substring(1, aminoLetterString.length());
		String[] aminoLetters = aminoLetterString.split("  ");
		//valid index from 0,0 to 23,23 
		int[][] blomA = new int[24][24];
		int row = 0;
		while(true){
			String grabLine = readerB.readLine();
			if(grabLine == null) break;
			String[] spaceSplit = grabLine.split(" ");
			int curCol = 0;
			for(int col=1; col<spaceSplit.length; col++){
				if(spaceSplit[col].equals("")) continue;
				blomA[row][curCol] = Integer.parseInt(spaceSplit[col]);
				curCol++;
			}
			row++;
		}
		//let a-->row and b-->col
		HashMap<String, Integer> blom = new HashMap<String, Integer>();
		for(int a=1; a<aminoLetters.length; a++){
			for(int b=1; b<aminoLetters.length; b++){
				int val = blomA[a-1][b-1];
				String rowChar = aminoLetters[a];
				String colChar = aminoLetters[b];
				String key = rowChar + colChar;
				blom.put(key, val);
			}
		}

		String nStringF = reader.readLine();
		String nStringR = "";
		for(int a=0; a<nStringF.length(); a++){
			nStringR = getStr(nStringF.charAt(a)) + nStringR;
		}
		int n = nStringF.length();
		nStringF = " " + nStringF;
		nStringR = " " + nStringR;

		String mString = reader.readLine();
		int m = mString.length();
		int midX = (m)/2;
		String mSubF = mString.substring(0, midX);
		String mSubR = "";
		String temp = mString.substring(midX, mString.length());
		for(int b=0; b<mString.length()-midX; b++){
			mSubR = getStr(temp.charAt(b)) + mSubR;
		}
		mSubF = " " + mSubF;
		mSubR = " " + mSubR;

		int indel = -5;

		//solve with nStringForward and mSubForward
		int[] maxPathValA = new int[nStringF.length()];
		for(int b=0; b<mSubF.length(); b++){
			int[] tempVals = new int[nStringF.length()];
			for(int a=0; a<nStringF.length(); a++){
				if(b==0){
					tempVals[a] = a * indel;
				}
				else{
					if(a==0){
						tempVals[a] = maxPathValA[a] + indel;
					}
					else {
						String key = getStr(nStringF.charAt(a)) + getStr(mSubF.charAt(b));
						int upVal = tempVals[a-1] + indel;
						int leftVal = maxPathValA[a] + indel;
						int diagVal = maxPathValA[a-1] + blom.get(key);
						int max = diagVal;
						int dir = 2;
						if(upVal > max){
							max = upVal;
							dir = 1;
						}
						if(leftVal > max){
							max = leftVal;
							dir = 0;
						}
						tempVals[a] = max;
					}
				}
			}
			maxPathValA = tempVals;
		}

		int[] maxPathValB = new int[nStringF.length()];
		int[] tempVals = new int[nStringF.length()];
		int[] paths = new int[nStringF.length()];
		for(int b=0; b<mSubR.length(); b++){
			for(int a=0; a<nStringR.length()-1; a++){
				if(b==0){
					tempVals[a] = a * indel;
					paths[a] = 1;
				}
				else{
					if(a==0){
						tempVals[a] = maxPathValB[a] + indel;
						paths[a] = 0;
					}
					else{
						String key = getStr(nStringR.charAt(a)) + getStr(mSubR.charAt(b));
						int upVal = tempVals[a-1] + indel;
						int leftVal = maxPathValB[a] + indel;
						//System.out.println(key);
						//System.out.println(a + " " + b);
						int diagVal = maxPathValB[a-1] + blom.get(key);
						int max = diagVal;
						int dir = 2;
						if(upVal > max){
							max = upVal;
							dir = 1;
						}
						if(leftVal > max){
							max = leftVal;
							dir = 0;
						}
						tempVals[a] = max;
						paths[a] = dir;
					}
				}
			}
			if(b != mSubR.length()-1) {
				//System.out.println(b);
				//System.out.println(Arrays.toString(maxPathValB));
				//System.out.println(Arrays.toString(tempVals));
				//System.out.println();
				for(int r=0; r<tempVals.length; r++){
					maxPathValB[r] = tempVals[r];
				}
			}
		}

		//reverse mathPathValB + tempVals
		//System.out.println(Arrays.toString(maxPathValB));
		//System.out.println(Arrays.toString(tempVals));
		for(int g=0; g<maxPathValB.length/2; g++){
			int temp1 = maxPathValB[g];
			int tempA = tempVals[g];
			int temp2 = maxPathValB[maxPathValB.length-g-1];
			int tempB = tempVals[tempVals.length-g-1];
			maxPathValB[g] = temp2;
			tempVals[g] = tempB;
			maxPathValB[maxPathValB.length-g-1] = temp1;
			tempVals[tempVals.length-g-1] = tempA;
		}

		int max = 0;
		int midY = 0;
		for(int h=0; h<maxPathValA.length; h++){
			int curVal = maxPathValA[h] + maxPathValB[h];
			if(curVal > max){
				max = curVal;
				midY = h;
			}
		}

		if(maxPathValB[midY] > maxPathValB[midY + 1]){
			String coord1 = "(" + Integer.toString(midY) + ", " + Integer.toString(midX) + ")";
			String coord2 = "(" + Integer.toString(midY) + ", " + Integer.toString(midX+1) + ")";
			System.out.println(coord1 + " " + coord2);
		}

		else {
			String coord1 = "(" + Integer.toString(midY) + ", " + Integer.toString(midX) + ")";
			String coord2 = "(" + Integer.toString(midY+1) + ", " + Integer.toString(midX+1) + ")";
			System.out.println(coord1 + " " + coord2);
		}



	}

	public static void affineGapAlignmentDriver2(BufferedReader reader) throws IOException{
		File blosumMatrixFile = new File("blosum62");
		BufferedReader readerB = new BufferedReader(new FileReader(blosumMatrixFile));
		String aminoLetterString = readerB.readLine();
		//index 0 is blank
		//index 1-20 are valid aa's in alphabetical order
		aminoLetterString = aminoLetterString.substring(1, aminoLetterString.length());
		String[] aminoLetters = aminoLetterString.split("  ");
		//valid index from 0,0 to 23,23 
		int[][] blomA = new int[24][24];
		int row = 0;
		while(true){
			String grabLine = readerB.readLine();
			if(grabLine == null) break;
			String[] spaceSplit = grabLine.split(" ");
			int curCol = 0;
			for(int col=1; col<spaceSplit.length; col++){
				if(spaceSplit[col].equals("")) continue;
				blomA[row][curCol] = Integer.parseInt(spaceSplit[col]);
				curCol++;
			}
			row++;
		}
		//let a-->row and b-->col
		HashMap<String, Integer> blom = new HashMap<String, Integer>();
		for(int a=1; a<aminoLetters.length; a++){
			for(int b=1; b<aminoLetters.length; b++){
				int val = blomA[a-1][b-1];
				String rowChar = aminoLetters[a];
				String colChar = aminoLetters[b];
				String key = rowChar + colChar;
				blom.put(key, val);
			}
		}

		String nString = reader.readLine();
		int n = nString.length();
		nString = " " + nString;
		String mString = reader.readLine();
		int m = mString.length();
		mString = " " + mString;

		int[][] rightMaxVal = new int[n+1][m+1];
		int[][] diagMaxVal = new int[n+1][m+1];
		int[][] downMaxVal = new int[n+1][m+1];
		int[][] rightBack = new int[n+1][m+1];
		int[][] diagBack = new int[n+1][m+1];
		int[][] downBack = new int[n+1][m+1];
		// end of down == 8
		//end of right == 7
		//diag --> down == 6
		//down --> diag == 5
		//diag --> right == 4
		//right --> diag == 3
		//diag == 2
		//up == 1
		//left == 0
		//end of path -1
		int startGap = -11;
		int extendGap = -1;

		for(int a=0; a<=n; a++){
			for(int b=0; b<=m; b++){
				//0,0 case
				if(a==0 && b==0){
					diagMaxVal[a][b] = 0;
					diagBack[a][b] = -1;
				}
				//first column
				else if(b==0){
					if(a==1){
						downMaxVal[a][b] = startGap;
						downBack[a][b] = 5;
					}
					else {
						downMaxVal[a][b] = downMaxVal[a-1][b] + extendGap;
						downBack[a][b] = 1;
					}
					diagMaxVal[a][b] = downMaxVal[a][b];
					diagBack[a][b] = 6;
				}
				//first row
				else if(a==0){
					if(b==1){
						rightMaxVal[a][b] = startGap;
						rightBack[a][b] = 3;
					}
					else {
						rightMaxVal[a][b] = rightMaxVal[a][b] + extendGap;
						rightBack[a][b] = 0;
					}
					diagMaxVal[a][b] = downMaxVal[a][b];
					diagBack[a][b] = 4;
				}
				// a>=1
				// b>=1
				else {
					//find downMax[a][b]
					if(a==1){
						downMaxVal[a][b] = diagMaxVal[a-1][b] + startGap;
						downBack[a][b] = 5;
					}
					else {
						int upVal = downMaxVal[a-1][b] + extendGap;
						int upDiag = diagMaxVal[a-1][b] + startGap;
						downMaxVal[a][b] = (upVal>upDiag)?upVal:upDiag;
						downBack[a][b] = (upVal>upDiag)?1:5;
					}
					//find rightMax[a][b]
					if(b==1){
						rightMaxVal[a][b] = diagMaxVal[a][b-1] + startGap;
						rightBack[a][b] = 3;
					}
					else {
						int leftVal = rightMaxVal[a][b-1] + extendGap;
						int leftDiag = diagMaxVal[a][b-1] + startGap;
						rightMaxVal[a][b] = (leftVal>leftDiag)?leftVal:leftDiag;
						rightBack[a][b] = (leftVal>leftDiag)?0:3;
					}
					//find diagMax[a][b]
					String key = getStr(nString.charAt(a)) + getStr(mString.charAt(b));
					int max = diagMaxVal[a-1][b-1] + blom.get(key);
					int dir = 2;
					if(rightMaxVal[a][b] > max){
						max = rightMaxVal[a][b];
						dir = 4;
					}
					if(downMaxVal[a][b] > max){
						max = downMaxVal[a][b];
						dir = 6;
					}
					diagMaxVal[a][b] = max;
					diagBack[a][b] = dir;
				}
			}
		}

		//print this shit out
		int[][] curArray = diagBack;
		String string1 = "";
		String string2 = "";
		int y = n;
		int x = m;
		int dir = 0;
		while(y!=0 || x!= 0){
			dir = curArray[y][x];
			switch(dir){
				case 6: curArray = downBack;
						break;
				case 5: curArray = diagBack;
						string1 = getStr(nString.charAt(y)) + string1;
						string2 = "-" + string2;
						y--;
						break;
				case 4: curArray = rightBack;
						break;
				case 3: curArray = diagBack;
						string1 = "-" + string1;
						string2 = getStr(mString.charAt(x)) + string2;
						x--;
						break;
				case 2: string1 = getStr(nString.charAt(y)) + string1;
						string2 = getStr(mString.charAt(x)) + string2;
						y--; x--;
						break;
				case 1: string1 = getStr(nString.charAt(y)) + string1;
						string2 = "-" + string2;
						y--;
						break;
				case 0: string1 = "-" + string1;
						string2 = getStr(mString.charAt(x)) + string2;
						x--;
						break;
			}
		}
		System.out.println(diagMaxVal[n][m]);
		System.out.println(string1);
		System.out.println(string2);
	}

	public static void calcScore(BufferedReader reader) throws IOException {
		File blosumMatrixFile = new File("blosum62");
		BufferedReader readerB = new BufferedReader(new FileReader(blosumMatrixFile));
		String aminoLetterString = readerB.readLine();
		//index 0 is blank
		//index 1-20 are valid aa's in alphabetical order
		aminoLetterString = aminoLetterString.substring(1, aminoLetterString.length());
		String[] aminoLetters = aminoLetterString.split("  ");
		//valid index from 0,0 to 23,23 
		int[][] blomA = new int[24][24];
		int row = 0;
		while(true){
			String grabLine = readerB.readLine();
			if(grabLine == null) break;
			String[] spaceSplit = grabLine.split(" ");
			int curCol = 0;
			for(int col=1; col<spaceSplit.length; col++){
				if(spaceSplit[col].equals("")) continue;
				blomA[row][curCol] = Integer.parseInt(spaceSplit[col]);
				curCol++;
			}
			row++;
		}
		//let a-->row and b-->col
		HashMap<String, Integer> blom = new HashMap<String, Integer>();
		for(int a=1; a<aminoLetters.length; a++){
			for(int b=1; b<aminoLetters.length; b++){
				int val = blomA[a-1][b-1];
				String rowChar = aminoLetters[a];
				String colChar = aminoLetters[b];
				String key = rowChar + colChar;
				blom.put(key, val);
			}
		}


		String nString = reader.readLine();
		String mString = reader.readLine();
		int[] score = new int[nString.length() + 1];

		int startGap = -11;
		int extendGap = -1;

		for(int i=1; i<=nString.length(); i++){
			char nChar = nString.charAt(i-1);
			char mChar = mString.charAt(i-1);
			if(nChar == '-'){
				if(i==1) score[i] = score[i-1] + startGap;
				else if(nString.charAt(i-2) == '-') score[i] = score[i-1] + extendGap;
				else score[i] = score[i-1] + startGap;
			}
			else if(mChar == '-'){
				if(i==1) score[i] = score[i-1] + startGap;
				else if(mString.charAt(i-2) == '-') score[i] = score[i-1] + extendGap;
				else score[i] = score[i-1] + startGap;
			}
			else {
				String key = Character.toString(nChar) + Character.toString(mChar);
				score[i] = score[i-1] + blom.get(key);
			} 
		}

		int count = 0;
		int a = -1;
		int s = -1;
		int z = -1;

		while(count < nString.length()){
			for(a = a+1; a<score.length; a++){
				System.out.print(score[a] + " ");
				if(a%21 == 0 && a != 0) break;
			}
			System.out.println();
			for(s = s+1; s<nString.length(); s++){
				System.out.print(nString.charAt(s) + "  ");
				if(s%20 == 0 && s!= 0) break;
			}
			System.out.println();
			for(z = z+1; z<mString.length(); z++){
				System.out.print(mString.charAt(z) + "  ");
				if(z%20 == 0 && z!= 0) break;
			}
			System.out.println();
			System.out.println();
			count += 20;
		}
		//System.out.println(nString);
		//System.out.println(mString);
	}

	public static void affineGapAlignmentDriver(BufferedReader reader) throws IOException {
		File blosumMatrixFile = new File("blosum62");
		BufferedReader readerB = new BufferedReader(new FileReader(blosumMatrixFile));
		String aminoLetterString = readerB.readLine();
		//index 0 is blank
		//index 1-20 are valid aa's in alphabetical order
		aminoLetterString = aminoLetterString.substring(1, aminoLetterString.length());
		String[] aminoLetters = aminoLetterString.split("  ");
		//valid index from 0,0 to 23,23 
		int[][] blomA = new int[24][24];
		int row = 0;
		while(true){
			String grabLine = readerB.readLine();
			if(grabLine == null) break;
			String[] spaceSplit = grabLine.split(" ");
			int curCol = 0;
			for(int col=1; col<spaceSplit.length; col++){
				if(spaceSplit[col].equals("")) continue;
				blomA[row][curCol] = Integer.parseInt(spaceSplit[col]);
				curCol++;
			}
			row++;
		}
		//let a-->row and b-->col
		HashMap<String, Integer> blom = new HashMap<String, Integer>();
		for(int a=1; a<aminoLetters.length; a++){
			for(int b=1; b<aminoLetters.length; b++){
				int val = blomA[a-1][b-1];
				String rowChar = aminoLetters[a];
				String colChar = aminoLetters[b];
				String key = rowChar + colChar;
				blom.put(key, val);
			}
		}

		String nString = reader.readLine();
		int n = nString.length();
		nString = " " + nString;
		String mString = reader.readLine();
		int m = mString.length();
		mString = " " + mString;
		//nstring and mstring are now of length n+1 and m+1
		//therefore, index n and m are valid indexes

		int[][] maxPathVal = new int[n+1][m+1];
		int[][] backPath = new int[n+1][m+1];

		int startGap = -11;
		int extendGap = -1;
		int globalMax = 0;

		//Let 2 be diagonal
		//let 1 be up
		//let 0 be left
		//let -1 be end of path
		for(int a=0; a<=n; a++){
			for(int b=0; b<=m; b++){
				int leftInd = b-1;
				int upInd = a-1;
				//0,0 case
				if(leftInd < 0 && upInd < 0){
					maxPathVal[a][b] = 0;
					backPath[a][b] = -1;
				}
				else {
					//if in first column
					if(leftInd < 0){
						//pos (1,0)
						if(a == 1){
							maxPathVal[a][b] = startGap;
							backPath[a][b] = 1;
						}
						//pos' (1<x<=n, 0)
						else {
							maxPathVal[a][b] = ((a-1) * extendGap) + startGap;
							backPath[a][b] = 1;
						}
					}
					//if in first row
					else if(upInd < 0){
						//pos (0,1)
						if(b == 1){
							maxPathVal[a][b] = startGap;
							backPath[a][b] = 0;
						}
						else {
							maxPathVal[a][b] = ((b-1) * extendGap) + startGap;
							backPath[a][b] = 0;
						}
					}
					else {
						//get mismatch or match val
						char nChar = nString.charAt(a);
						char mChar = mString.charAt(b);
						//if(nChar == 'R' && mChar == 'E') System.out.println("Ah " + a + " " + b );
						String key = Character.toString(nChar) + Character.toString(mChar);
						int blomScore = blom.get(key);
						//calculate previous path scores
						int diagVal = maxPathVal[upInd][leftInd] + blomScore;
						int leftVal = maxPathVal[a][leftInd];
						leftVal += (backPath[a][leftInd]==0)?extendGap:startGap;
						int upVal = maxPathVal[upInd][b];
						upVal += (backPath[upInd][b]==1)?extendGap:startGap;
						//find last vert gap
						int vertGap = a-1;
						while(backPath[vertGap][b] != 1 && vertGap > 0){
							vertGap--;
						}
						int vertGapVal = maxPathVal[vertGap][b] + ((a-vertGap)*extendGap);
						if(vertGap == 0) vertGapVal = n * startGap;
						//find last horz gap
						int horzGap = b-1;
						while(backPath[a][horzGap] != 0 && horzGap > 0){
							horzGap--;
						}
						int horzGapVal = maxPathVal[a][horzGap] * ((b-horzGap)*extendGap);
						if(horzGap == 0) horzGapVal = n * startGap;
						//get the max
						int max = diagVal;
						int dir = 2;
						if(max < leftVal) {
							max = leftVal;
							dir = 0;
						}
						if(max < upVal) {
							max = upVal;
							dir = 1;
						}
						if(max < horzGapVal && horzGapVal > vertGapVal){
							for(int g=horzGap+1; g<b; g++){
								maxPathVal[a][g] = maxPathVal[a][g-1] + extendGap;
								backPath[a][g] = 0;
							}
							max = horzGapVal;
							dir = 0;
						}
						if(max < vertGapVal && horzGapVal < vertGapVal){
							for(int g=vertGap+1; g<a; g++){
								maxPathVal[g][b] = maxPathVal[g-1][b] + extendGap;
								backPath[g][b] = 1;
							}
							max = vertGapVal;
							dir = 1;
						}
						//assign the new values
						maxPathVal[a][b] = max;
						backPath[a][b] = dir;
					}
				}
			}
		}
		//print out path
		//2 is diagonal
		//1 is up
		//0 is left
		//-1 is end
		//y is nString
		//x is mString
		int y = n;
		int x = m;
		String string1 = "";
		String string2 = "";
		while(y > 0 && x > 0){
			int dir = backPath[y][x];
			if(dir == 2){
				string1 = Character.toString(nString.charAt(y)) + string1;
				string2 = Character.toString(mString.charAt(x)) + string2;
				y--;
				x--;
			}
			else if(dir == 1){
				string1 = Character.toString(nString.charAt(y)) + string1;
				string2 = "-" + string2;
				y--;
			}
			else if(dir == 0){
				string1 = "-" + string1;
				string2 = Character.toString(mString.charAt(x)) + string2;
				x--;
			}
			else {
				y--;
				x--;
			}
		}

		/*for(int v=9; v>0; v--){
			System.out.print( Integer.toString(v) + ",3 --> ");
			System.out.print(backPath[v][3] + " ");
			System.out.print(getStr(nString.charAt(v)) + getStr(mString.charAt(3)));
			System.out.println();
		}*/
		System.out.println(maxPathVal[n][m]);
		System.out.println(string1);
		System.out.println(string2);
	}

	public static String getStr(Character c){
		return Character.toString(c);
	}

	public static void overlapAlignmentDriver(BufferedReader reader) throws IOException {
		//nString is suffix
		String nString = reader.readLine();
		int n = nString.length();

		nString = " " + nString;
		//mString is prefix
		String mString = reader.readLine();
		int m = mString.length();
		mString = " " + mString;

		int[][] maxPathVal = new int[n+1][m+1];
		int[][] backPath = new int[n+1][m+1];

		//2 is down/left diagonal
		//1 is down
		//0 is left
		//-1 is end of path
		int match = 1;
		int indel = -2;
		int mismatch = -2;

		int globalMax = 0;
		int xMax = 0;
		int yMax = 0;

		for(int a=0; a<=n; a++){
			for(int b=0; b<=m; b++){
				int leftInd = b-1;
				int downInd = a-1;
				//n,0 case
				if(leftInd < 0 && downInd < 0){
					maxPathVal[a][b] = 0;
					backPath[a][b] = -1;
				}
				//vertical axis
				else if(leftInd < 0){
					maxPathVal[a][b] = 0;
					backPath[a][b] = -1;
				}
				//horizontal axis
				else if(downInd < 0){
					maxPathVal[a][b] = (b-1) * indel;
					backPath[a][b] = 0;
				}
				//standard case
				else {
					int leftVal = maxPathVal[a][leftInd];
					if(a!=n) leftVal += indel;
					int downVal = maxPathVal[downInd][b] + indel;
					int diagVal = maxPathVal[downInd][leftInd];
					int diagDelta = (nString.charAt(a) == mString.charAt(b))?match:mismatch;
					diagVal += diagDelta;
					int max = diagVal;
					int dir = 2;
					if(downVal > max){
						max = downVal;
						dir = 1;
					}
					if(leftVal > max){
						max = leftVal;
						dir = 0;
					}
					maxPathVal[a][b] = max;
					backPath[a][b] = dir;

					if(max > globalMax && a == n){
						globalMax = max;
						yMax = a;
						xMax = b;
					}
				}
			}
		}

		int y = yMax;
		int x = xMax;
		String stringN = "";
		String stringM = "";
		//2 is down/left diagonal
		//1 is down
		//0 is left
		//-1 is end of path
		while(y>=0 && x>=0){
			int dir = backPath[y][x];
			switch(dir){
				case 2:
					stringN = Character.toString(nString.charAt(y)) + stringN;
					stringM = Character.toString(mString.charAt(x)) + stringM;
					y--;
					x--;
					break;
				case 1:
					stringN = Character.toString(nString.charAt(y)) + stringN;
					stringM = "-" + stringM;
					y--;
					break;
				case 0:
					stringN = "-" + stringN;
					stringM = Character.toString(mString.charAt(x)) + stringM;
					x--;
					break;
				case -1:
					y--;
					x--;
					break;
			}
		}
		System.out.println(maxPathVal[yMax][xMax]);
		System.out.println(stringN);
		System.out.println(stringM);
	}

	public static void fittingAlignmentDriver(BufferedReader reader) throws IOException {
		String nString = reader.readLine();
		int n = nString.length();
		nString = " " + nString;
		String mString = reader.readLine();
		int m = mString.length();
		mString = " " + mString;


		int[][] maxPathValue = new int[n+1][m+1];
		int[][] backPath = new int[n+1][m+1];

		int indel = -1;
		int yMax = 0;
		int xMax = 0;
		int maxVal = 0;
		//Let 2 be diagonal
		//let 1 be up
		//let 0 be left
		//let -1 be end of path
		for(int a=0; a<=n; a++){
			for(int b=0; b<=m; b++){
				int leftInd = b-1;
				int upInd = a-1;
				//0,0 case
				if(leftInd < 0 && upInd < 0){
					maxPathValue[a][b] = 0;
					backPath[a][b] = -1;
				}
				else {
					//if in first column
					if(leftInd < 0){
						maxPathValue[a][b] = 0;
						backPath[a][b] = -1;
					}
					//if in first row
					else if(upInd < 0){
						maxPathValue[a][b] = b * indel;
						backPath[a][b] = 0;
					}
					else {
						//get mismatch or match val
						char nChar = nString.charAt(a);
						char mChar = mString.charAt(b);
						int delta = (nChar == mChar)? 1 : -1;
						//calculate previous path scores
						int diagVal = maxPathValue[upInd][leftInd] + delta;
						int leftVal = maxPathValue[a][leftInd] + indel;
						int upVal = maxPathValue[upInd][b] + indel;
						//get the max
						int max = diagVal;
						int dir = 2;
						if(upVal > max) {
							max = upVal;
							dir = 1;
						}
						if(leftVal > max) {
							max = leftVal;
							dir = 0;
						}
						//assign the new values
						maxPathValue[a][b] = max;
						backPath[a][b] = dir;
						if(b == m){
							if(max > maxVal){
								maxVal = max;
								yMax = a;
								xMax = b;
							}
						}
					}
				}
			}
		}
		//print out path
		//2 is diagonal
		//1 is up
		//0 is left
		//-1 is end
		//y is nString
		//x is mString
		int y = yMax;
		int x = xMax;
		String string1 = "";
		String string2 = "";
		while(y>=0 && x >= 0){
			int dir = backPath[y][x];
			if(dir == 2){
				string1 = Character.toString(nString.charAt(y)) + string1;
				string2 = Character.toString(mString.charAt(x)) + string2;
				y--;
				x--;
			}
			else if(dir == 1){
				string1 = Character.toString(nString.charAt(y)) + string1;
				string2 = "-" + string2;
				y--;
			}
			else if(dir == 0){
				string1 = "-" + string1;
				string2 = Character.toString(mString.charAt(x)) + string2;
				x--;
			}
			else {
				y--;
				x--;
			}
		}
		System.out.println(maxPathValue[yMax][xMax]);
		System.out.println(string1);
		System.out.println(string2);


	}

	public static void editDistanceDriver(BufferedReader reader) throws IOException {
		String nString = reader.readLine();
		int n = nString.length();
		String mString = reader.readLine();
		int m = mString.length();

		int[][] minEdit = new int[n][m];
		for(int a=0; a<n; a++){
			for(int b=0; b<m; b++){
				int leftInd = b-1;
				int upInd = a-1;
				//edit distance of single character is
				//0 if characters are the same
				//1 otherwise
				if(leftInd < 0){
					if(nString.charAt(a) == mString.charAt(b)) minEdit[a][b] = a;
					else minEdit[a][b] = a + 1;
				}
				else if(upInd < 0){
					if(nString.charAt(a) == mString.charAt(b)) minEdit[a][b] = b;
					else minEdit[a][b] = b + 1;
				}
				else {
					int leftVal = minEdit[a][leftInd] + 1;
					int upVal = minEdit[upInd][b] + 1;
					int diagVal = minEdit[upInd][leftInd];
					if(nString.charAt(a) != mString.charAt(b)) diagVal += 1;
					int min = leftVal;
					if(upVal < min) min = upVal;
					if(diagVal < min) min = diagVal;
					minEdit[a][b] = min;
				}
			}
		}
		System.out.println(minEdit[n-1][m-1]);
	}

	public static void localAlignmentDriver(BufferedReader reader) throws FileNotFoundException, IOException{
		File pam250File = new File("pam250");
		BufferedReader readerB = new BufferedReader(new FileReader(pam250File));
		String aminoLetterString = readerB.readLine();
		//index 0 is blank
		//index 1-20 are valid aa's in alphabetical order
		aminoLetterString = aminoLetterString.substring(1, aminoLetterString.length());
		String[] aminoLetters = aminoLetterString.split("  ");
		//valid index from 0,0 to 23,23 
		int[][] pamA = new int[24][24];
		int row = 0;
		while(true){
			String grabLine = readerB.readLine();
			if(grabLine == null) break;
			String[] spaceSplit = grabLine.split(" ");
			int curCol = 0;
			for(int col=1; col<spaceSplit.length; col++){
				if(spaceSplit[col].equals("")) continue;
				pamA[row][curCol] = Integer.parseInt(spaceSplit[col]);
				curCol++;
			}
			row++;
		}
		//let a-->row and b-->col
		HashMap<String, Integer> pam = new HashMap<String, Integer>();
		for(int a=1; a<aminoLetters.length; a++){
			for(int b=1; b<aminoLetters.length; b++){
				int val = pamA[a-1][b-1];
				String rowChar = aminoLetters[a];
				String colChar = aminoLetters[b];
				String key = rowChar + colChar;
				pam.put(key, val);
			}
		}

		String nString = reader.readLine();
		int n = nString.length();
		nString = " " + nString;
		String mString = reader.readLine();
		int m = mString.length();
		mString = " " + mString;
		//nstring and mstring are now of length n+1 and m+1
		//therefore, index n and m are valid indexes

		int[][] maxPathValue = new int[n+1][m+1];
		int[][] backPath = new int[n+1][m+1];

		int indel = -5;
		//Let 2 be diagonal
		//let 1 be up
		//let 0 be left
		//let -1 be end of path
		int globalMax = 0;
		int yMax = 0;
		int xMax = 0;
		for(int a=0; a<=n; a++){
			for(int b=0; b<=m; b++){
				int leftInd = b-1;
				int upInd = a-1;
				//0,0 case
				if(leftInd < 0 && upInd < 0){
					maxPathValue[a][b] = 0;
					backPath[a][b] = -1;
				}
				else {
					//if in first column
					if(leftInd < 0){
						maxPathValue[a][b] = a * indel;
						backPath[a][b] = 1;
					}
					//if in first row
					else if(upInd < 0){
						maxPathValue[a][b] = b * indel;
						backPath[a][b] = 0;
					}
					else {
						//get mismatch or match val
						char nChar = nString.charAt(a);
						char mChar = mString.charAt(b);
						String key = Character.toString(nChar) + Character.toString(mChar);
						int blomScore = pam.get(key);
						//calculate previous path scores
						int diagVal = maxPathValue[upInd][leftInd] + blomScore;
						int leftVal = maxPathValue[a][leftInd] + indel;
						int upVal = maxPathValue[upInd][b] + indel;
						//get the max
						int max = diagVal;
						int dir = 2;
						if(upVal > max) {
							max = upVal;
							dir = 1;
						}
						if(leftVal > max) {
							max = leftVal;
							dir = 0;
						}
						if(0 > max){
							max = 0;
							dir = -1;
						}
						//assign the new values
						maxPathValue[a][b] = max;
						backPath[a][b] = dir;
						if(max > globalMax){
							globalMax = max;
							yMax = a;
							xMax = b;
						}
					}
				}
			}
		}
		//print out path
		//2 is diagonal
		//1 is up
		//0 is left
		//-1 is end
		//y is nString
		//x is mString
		int y = yMax;
		int x = xMax;
		String string1 = "";
		String string2 = "";
		while(y >= 0 && x >= 0){
			int dir = backPath[y][x];
			if(dir == 2){
				string1 = Character.toString(nString.charAt(y)) + string1;
				string2 = Character.toString(mString.charAt(x)) + string2;
				y--;
				x--;
			}
			else if(dir == 1){
				string1 = Character.toString(nString.charAt(y)) + string1;
				string2 = "-" + string2;
				y--;
			}
			else if(dir == 0){
				string1 = "-" + string1;
				string2 = Character.toString(mString.charAt(x)) + string2;
				x--;
			}
			else {
				y--;
				x--;
			}
			if(backPath[y][x] == -1) break;
		}
		System.out.println(maxPathValue[yMax][xMax]);
		System.out.println(string1);
		System.out.println(string2);

	}

	public static void globalAlignmentDriver(BufferedReader reader) throws FileNotFoundException, IOException{
		File blosumMatrixFile = new File("blosum62");
		BufferedReader readerB = new BufferedReader(new FileReader(blosumMatrixFile));
		String aminoLetterString = readerB.readLine();
		//index 0 is blank
		//index 1-20 are valid aa's in alphabetical order
		aminoLetterString = aminoLetterString.substring(1, aminoLetterString.length());
		String[] aminoLetters = aminoLetterString.split("  ");
		//valid index from 0,0 to 23,23 
		int[][] blomA = new int[24][24];
		int row = 0;
		while(true){
			String grabLine = readerB.readLine();
			if(grabLine == null) break;
			String[] spaceSplit = grabLine.split(" ");
			int curCol = 0;
			for(int col=1; col<spaceSplit.length; col++){
				if(spaceSplit[col].equals("")) continue;
				blomA[row][curCol] = Integer.parseInt(spaceSplit[col]);
				curCol++;
			}
			row++;
		}
		//let a-->row and b-->col
		HashMap<String, Integer> blom = new HashMap<String, Integer>();
		for(int a=1; a<aminoLetters.length; a++){
			for(int b=1; b<aminoLetters.length; b++){
				int val = blomA[a-1][b-1];
				String rowChar = aminoLetters[a];
				String colChar = aminoLetters[b];
				String key = rowChar + colChar;
				blom.put(key, val);
			}
		}

		String nString = reader.readLine();
		int n = nString.length();
		nString = " " + nString;
		String mString = reader.readLine();
		int m = mString.length();
		mString = " " + mString;
		//nstring and mstring are now of length n+1 and m+1
		//therefore, index n and m are valid indexes

		int[][] maxPathValue = new int[n+1][m+1];
		int[][] backPath = new int[n+1][m+1];

		int indel = -5;
		//Let 2 be diagonal
		//let 1 be up
		//let 0 be left
		//let -1 be end of path
		for(int a=0; a<=n; a++){
			for(int b=0; b<=m; b++){
				int leftInd = b-1;
				int upInd = a-1;
				//0,0 case
				if(leftInd < 0 && upInd < 0){
					maxPathValue[a][b] = 0;
					backPath[a][b] = -1;
				}
				else {
					//if in first column
					if(leftInd < 0){
						maxPathValue[a][b] = a * indel;
						backPath[a][b] = 1;
					}
					//if in first row
					else if(upInd < 0){
						maxPathValue[a][b] = b * indel;
						backPath[a][b] = 0;
					}
					else {
						//get mismatch or match val
						char nChar = nString.charAt(a);
						char mChar = mString.charAt(b);
						String key = Character.toString(nChar) + Character.toString(mChar);
						int blomScore = blom.get(key);
						//calculate previous path scores
						int diagVal = maxPathValue[upInd][leftInd] + blomScore;
						int leftVal = maxPathValue[a][leftInd] + indel;
						int upVal = maxPathValue[upInd][b] + indel;
						//get the max
						int max = diagVal;
						int dir = 2;
						if(upVal > max) {
							max = upVal;
							dir = 1;
						}
						if(leftVal > max) {
							max = leftVal;
							dir = 0;
						}
						//assign the new values
						maxPathValue[a][b] = max;
						backPath[a][b] = dir;
					}
				}
			}
		}
		//print out path
		//2 is diagonal
		//1 is up
		//0 is left
		//-1 is end
		//y is nString
		//x is mString
		int y = n;
		int x = m;
		String string1 = "";
		String string2 = "";
		while(y >= 0 && x >= 0){
			int dir = backPath[y][x];
			if(dir == 2){
				string1 = Character.toString(nString.charAt(y)) + string1;
				string2 = Character.toString(mString.charAt(x)) + string2;
				y--;
				x--;
			}
			else if(dir == 1){
				string1 = Character.toString(nString.charAt(y)) + string1;
				string2 = "-" + string2;
				y--;
			}
			else if(dir == 0){
				string1 = "-" + string1;
				string2 = Character.toString(mString.charAt(x)) + string2;
				x--;
			}
			else {
				y--;
				x--;
			}
		}
		System.out.println(maxPathValue[n][m]);
		System.out.println(string1);
		System.out.println(string2);
	}

	public static void longestPathInDagDriver(BufferedReader reader) throws IOException {
		int source = Integer.parseInt(reader.readLine());
		int sink = Integer.parseInt(reader.readLine());
		HashMap<Integer, ArrayList<Edge>> edgesOut = new HashMap<Integer, ArrayList<Edge>>();
		HashMap<Integer, ArrayList<Integer>> edgesIn = new HashMap<Integer, ArrayList<Integer>>();
		HashMap<Integer, Integer> maxDist = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> backPath = new HashMap<Integer, Integer>();
		while(true){
			//1->2:5
			//1->3:6
			//1->4:7
			String grabLine = reader.readLine();
			if(grabLine == null) break;
			String[] spaceSplit = grabLine.split("->");
			int nodeEdgeOut = Integer.parseInt(spaceSplit[0]);
			if(edgesOut.get(nodeEdgeOut) == null) edgesOut.put(nodeEdgeOut, new ArrayList<Edge>());
			if(edgesIn.get(nodeEdgeOut) == null) edgesIn.put(nodeEdgeOut, new ArrayList<Integer>());
			String[] colonSplit = spaceSplit[1].split(":");
			String toColonSplit = "";
	
			for(int a=0; a<colonSplit.length; a+=2){
				int nodeEdgeIn = Integer.parseInt(colonSplit[a]);
				int edgeWeight = Integer.parseInt(colonSplit[a+1]);
				if(edgesIn.get(nodeEdgeIn) == null) edgesIn.put(nodeEdgeIn, new ArrayList<Integer>());
				if(edgesOut.get(nodeEdgeIn) == null) edgesOut.put(nodeEdgeIn, new ArrayList<Edge>());
				edgesOut.get(nodeEdgeOut).add(new Edge(nodeEdgeIn, edgeWeight));
				edgesIn.get(nodeEdgeIn).add(nodeEdgeOut);
			}
		}
		ArrayList<Integer> sources = new ArrayList<Integer>();
		ArrayList<Integer> topo = new ArrayList<Integer>();
		for(Integer key : edgesIn.keySet()){
			if(edgesIn.get(key).size() == 0 && key != source){
				sources.add(key);
			}
		}

		//remove extraneous sources and all nodes that can only be reached
		//from them
		while(sources.size() != 0){
			int cur = sources.get(0);
			sources.remove(0);
			ArrayList<Edge> curEdgesOut = edgesOut.get(cur);
			for(Edge e : curEdgesOut){
				//remove edge from cur to nodeEdgeIn
				ArrayList<Integer> curEdgesIn = edgesIn.get(e.n());
				int removeInd = curEdgesIn.indexOf(cur);
				curEdgesIn.remove(removeInd);
				if(curEdgesIn.size() == 0 && e.n() != source) {
					sources.add(e.n());
				}
			}
		}
		for(Integer key : edgesIn.keySet()){
			System.out.print(key + " -> ");
			System.out.println(edgesIn.get(key));
		}
		//add desired source
		//generate topo
		sources.add(source);
		while(sources.size() != 0){
			int curSource = sources.get(0);
			sources.remove(0);
			topo.add(curSource);
			maxDist.put(curSource, 0);
			ArrayList<Edge> curEdgesOut = edgesOut.get(curSource);
			for(Edge e : curEdgesOut){
				ArrayList<Integer> curEdgesIn = edgesIn.get(e.n());
				int removeInd = curEdgesIn.indexOf(curSource);
				curEdgesIn.remove(removeInd);
				if(curEdgesIn.size() == 0) sources.add(e.n());
			}
		}

		backPath.put(source, -1);
		for(Integer t : topo){
			ArrayList<Edge> curEdgesOut = edgesOut.get(t);
			for(Edge e : curEdgesOut){
				int curDist = maxDist.get(t) + e.w();
				if(curDist > maxDist.get(e.n())){
					maxDist.put(e.n(), curDist);
					backPath.put(e.n(), t);
				}
			}
		}
		System.out.println(maxDist.get(sink));
		String path = Integer.toString(sink);
		int cur = backPath.get(sink);
		while(cur != -1){
			path = Integer.toString(cur) + "->" + path;
			cur = backPath.get(cur);
		}
		System.out.println(path);

		//debug
		/*
		for(int i=0; i<topo.size(); i++){
			System.out.print(topo.get(i));
			if(i != topo.size()-1) System.out.print(", ");
		}*/
		/*
		System.out.println();
		for(Integer key : edgesOut.keySet()){
			System.out.print(key + " -> ");
			ArrayList<Edge> printEdges = edgesOut.get(key);
			for(Edge e : printEdges){
				System.out.print(e.n() + ":" + e.w() + ", ");
			}
			System.out.println();
		}
		for(Integer key : edgesOut.keySet()){
			System.out.print(key + " -> ");
			ArrayList<Edge> edgesPrint = edgesOut.get(key);
			for(Edge e : edgesPrint){
				System.out.print(e.n() + ":" + e.w() + ", ");
			}
			System.out.println();
		}
		System.out.println();
		for(Integer key : edgesIn.keySet()){
			System.out.print(key + " -> ");
			System.out.println(edgesIn.get(key));
		}
		System.out.println();
		for(Integer s : sources){
			System.out.println(s);
		}
		*/
	}

	public static void topologicalOrderDriver(BufferedReader reader) throws IOException {
		HashMap<Integer, ArrayList<Integer>> edgesOut = new HashMap<Integer, ArrayList<Integer>>();
		HashMap<Integer, ArrayList<Integer>> edgesIn = new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<Integer> keys = new ArrayList<Integer>();
		while(true){
			//1 -> 2,3,4
			String grabLine = reader.readLine();
			if(grabLine == null) break;
			String[] spaceSplit = grabLine.split(" ");
			int nodeEdgeOut = Integer.parseInt(spaceSplit[0]);
			if(edgesOut.get(nodeEdgeOut) == null) edgesOut.put(nodeEdgeOut, new ArrayList<Integer>());
			if(edgesIn.get(nodeEdgeOut) == null) edgesIn.put(nodeEdgeOut, new ArrayList<Integer>());
			String[] commaSplit = spaceSplit[2].split(",");
			for(int a=0; a<commaSplit.length; a++){
				int nodeEdgeIn = Integer.parseInt(commaSplit[a]);
				if(edgesIn.get(nodeEdgeIn) == null) edgesIn.put(nodeEdgeIn, new ArrayList<Integer>());
				if(edgesOut.get(nodeEdgeIn) == null) edgesOut.put(nodeEdgeIn, new ArrayList<Integer>());
				edgesOut.get(nodeEdgeOut).add(nodeEdgeIn);
				edgesIn.get(nodeEdgeIn).add(nodeEdgeOut);
			}
		}
		ArrayList<Integer> sources = new ArrayList<Integer>();
		ArrayList<Integer> topo = new ArrayList<Integer>();
		for(Integer key : edgesIn.keySet()){
			if(edgesIn.get(key).size() == 0) sources.add(key);
		}
		
		while(sources.size() != 0){
			int curSource = sources.get(0);
			sources.remove(0);
			topo.add(curSource);
			ArrayList<Integer> curEdgesOut = edgesOut.get(curSource);
			for(Integer e : curEdgesOut){
				ArrayList<Integer> curEdgesIn = edgesIn.get(e);
				int removeInd = curEdgesIn.indexOf(curSource);
				curEdgesIn.remove(removeInd);
				if(curEdgesIn.size() == 0) sources.add(e);
			}
		}
		for(int i=0; i<topo.size(); i++){
			System.out.print(topo.get(i));
			if(i != topo.size()-1) System.out.print(", ");
		}
		//debug
		/*
		for(Integer key : edgesOut.keySet()){
			System.out.print(key + " -> ");
			System.out.println(edgesOut.get(key));
		}
		System.out.println();
		for(Integer key : edgesIn.keySet()){
			System.out.print(key + " -> ");
			System.out.println(edgesIn.get(key));
		}
		System.out.println();
		for(Integer s : sources){
			System.out.println(s);
		}*/

	}

	public static void commonSubstringDriver(BufferedReader reader) throws IOException {
		//nString is rows
		String nString = reader.readLine();
		int n = nString.length();
		//mString is cols
		String mString = reader.readLine();
		int m = mString.length();
		//Let 1 be diagonal
		//Let 0 be insert in nString == deletion in mString == up arrow
		//Let -1 be insert in mString == deletion in nString == left arrow
		int[][] maxPathValue = new int[n][m];
		int[][] backPath = new int[n][m];

		for(int a=0; a<n; a++){
			for(int b=0; b<m; b++){
				int leftInd = b-1;
				int upInd = a-1;
				//0,0 case
				if(leftInd < 0 && upInd < 0){
					if(nString.charAt(0) == mString.charAt(0)){
						maxPathValue[0][0] = 1;
						backPath[0][0] = 1;
					}
					else {
						maxPathValue[0][0] = 0;
						backPath[0][0] = -1;
					}
				}
				else {
					//if in first column
					if(leftInd < 0){
						if(nString.charAt(a) == mString.charAt(0)){
							maxPathValue[a][0] = 1;
							backPath[a][0] = 1;
						}
						else {
							maxPathValue[a][0] = 0;
							backPath[a][0] = -1;
						}
					}
					//if in first row
					else if(upInd < 0){
						if(nString.charAt(0) == mString.charAt(b)){
							maxPathValue[0][b] = 1;
							backPath[0][b] = 1;
						}
						else {
							maxPathValue[0][b] = 0;
							backPath[0][b] = -0;
						}
					}
					else {
						if(nString.charAt(a) == mString.charAt(b)){
							int max = -1;
							int diag = maxPathValue[upInd][leftInd] + 1;
							max = diag;
							maxPathValue[a][b] = diag;
							backPath[a][b] = 1;
							int left = maxPathValue[a][leftInd];
							if(left > max){
								max = left;
								maxPathValue[a][b] = left;
								backPath[a][b] = -1;
							}
							int up = maxPathValue[upInd][b];
							if(up > max){
								max = up;
								maxPathValue[a][b] = up;
								backPath[a][b] = 0;
							}
						}
						else {
							int left = maxPathValue[a][leftInd];
							maxPathValue[a][b] = left;
							backPath[a][b] = -1;
							int up = maxPathValue[upInd][b];
							if(up > left){
								maxPathValue[a][b] = up;
								backPath[a][b] = 0;
							}
						}
					}
				}
			}
		}

		//print out path
		//1 is diagonal
		//0 is up
		//-1 is left
		//y is string1
		//x is string 2
		int y = n-1;
		int x = m-1;
		String substring = "";
		while(x >= 0 && y >= 0){
			int dir = backPath[y][x];
			switch(dir){
				case 1: substring = Character.toString(nString.charAt(y)) + substring;
					y--; x--;
					break;
				case 0: y--;
					break;
				case -1: x--;
					break;
			}
		}
		System.out.println(substring);
		//System.out.println(Arrays.toString(backPath));

	}


	public static void manhattanDriver(BufferedReader reader) throws IOException{
		String grabLine = reader.readLine();
		String[] nmStrings = grabLine.split(" ");
		int n = Integer.parseInt(nmStrings[0]);
		int m = Integer.parseInt(nmStrings[1]);
		int[][] down = new int[n][m+1];
		int[][] right = new int[n+1][m];
		//read down array 
		for(int a=0; a<n; a++){
			grabLine = reader.readLine();
			String[] col = grabLine.split(" ");
			int count = 0;
			for(String c : col){
				down[a][count] = Integer.parseInt(c);
				count++;
			}
		}
		//skip dividing -
		reader.readLine();
		//read right array
		for(int b=0; b<n+1; b++){
			grabLine = reader.readLine();
			String[] row = grabLine.split(" ");
			int count = 0;
			for(String r : row){
				right[b][count] = Integer.parseInt(r);
				count++;
			} 
		}

		int[][] maxPath = new int[n+1][m+1];
		for(int i=0; i<n+1; i++){
			for(int j=0; j<m+1; j++){
				int leftInd = j-1;
				int upInd = i-1;
				//find the max between the two, add edge weight
				int leftDist = -1;
				int upDist = -1;
				//case for 0,0
				if(leftInd < 0 && upInd < 0){
					maxPath[i][j] = 0;
				}
				else{
					if(leftInd < 0){
						maxPath[i][j] = maxPath[upInd][j] + down[upInd][j];
					}
					else if(upInd < 0){
						maxPath[i][j] = maxPath[i][leftInd] + right[i][leftInd]; 
					}
					else {
						maxPath[i][j] = (maxPath[i][leftInd]+right[i][leftInd]>maxPath[upInd][j]+down[upInd][j])?
						maxPath[i][leftInd]+right[i][j-1]:maxPath[upInd][j]+down[i-1][j]; 
					}
				}
			}
		}
		
		System.out.println(maxPath[n][m]);

		//testing
		//print arrays
		/*
		System.out.println(n + "x" + m);
		for(int[] d1 : down){
			for(int v : d1){
				System.out.print(v + " ");
			}
			System.out.println();
		}
		for(int[] r1 : right){
			for(int v : r1){
				System.out.print(v + " ");
			}
			System.out.println();
		}
		*/
	}
}

class Edge {
	public int node = -1;
	public int weight = -1;

	public Edge(int n, int w){
		node = n;
		weight = w;
	}

	public int n(){
		return node;
	}
	public int w(){
		return weight;
	}
}