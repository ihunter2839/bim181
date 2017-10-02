import java.util.*;
import java.io.*;

public class hamStrings {
	
	public static BufferedReader reader;

	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		File inputFile = new File(args[0]);
		reader = new BufferedReader(new FileReader(inputFile));

		String s1 = reader.readLine();

		String s2 = reader.readLine();

		//two null strings have a hamming distance of 0
		int hams = 0;

		for(int i=0; i<s1.length(); i++){
			if(s1.charAt(i) != s2.charAt(i)) hams++;
		}

		System.out.println(hams);
	}
}