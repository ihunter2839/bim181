import java.io.*;
import java.util.*;

public class change{

	public static void main(String[] args) throws FileNotFoundException, IOException {
		File inputFile = new File(args[0]);
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));

		int denom = Integer.parseInt(reader.readLine());
		String[] coinStrings = reader.readLine().split(",");
		int[] coins = new int[coinStrings.length];
		int count = 0;

		for(String c : coinStrings){
			coins[count] = Integer.parseInt(c);
			count++;
		}
		//make it one large so numCoins[denom] is valid
		int[] numCoins = new int[denom + 1];
		//init to -1
		for(int z=0; z<denom+1; z++){
			numCoins[z] = -1;
		}
		for(int a=0; a<denom+1; a++){
			for(int c : coins){
				int curVal = a - c;
				if(curVal == 0) numCoins[a] = 1;
				else if(curVal > 0){
					//check the number of coins to make curVal
					if(numCoins[a] == -1 || numCoins[curVal]+1 < numCoins[a]){
						numCoins[a] = numCoins[curVal] + 1;
					}
				}

			}	
		}
		System.out.println(numCoins[denom]);
	}
}