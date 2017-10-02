import java.util.*;
import java.io.*;

public class overlapGraph{

    public static void main(String[] args) throws FileNotFoundException, IOException{
        File inFile = new File(args[0]);
        BufferedReader reader = new BufferedReader(new FileReader(inFile));
        
    }

    private class synBlock{
        int head = 0;
        int tail = 0;

        synBlock(int h, int t){
            head = h;
            tail = t;
        }

        public int getH(){
            return this.head;
        }
        public int getT(){
            return this.tail;
        }

    }

}