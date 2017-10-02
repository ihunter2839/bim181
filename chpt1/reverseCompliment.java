import java.util.*;
import java.io.*;

public class reverseCompliment{

	public static BufferedReader reader;
	public static String output = "";

	public static void main(String[] args){
		File inputFile = new File(args[0]);

		try{
			reader = new BufferedReader(new FileReader(inputFile));
		} catch(FileNotFoundException e){}

		while(true){
			int curInt = 0;
			try{
				curInt = reader.read();
			} catch(IOException e){}
			
			char curChar = (char) curInt;
			if(curInt == -1) break;
			else{
				switch(curChar){
					case 'A':
						output = "T" + output;
						break;
					case 'C':
						output = "G" + output;
						break;
					case 'G':
						output = "C" + output;
						break;
					case 'T':
						output = "A" + output;
						break;
				}
			}
		}

		System.out.println(output);
	}
}