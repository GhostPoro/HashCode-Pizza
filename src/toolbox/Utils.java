package toolbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {
	
    public static List<String> readAllLines(String filePath) {
    	try {
			FileReader fr = null;
			BufferedReader br = null;
			
			if (new File(filePath).exists()) 
			{
			    List<String> list = new ArrayList<>();
			    try {
			    	fr = new FileReader(new File(filePath));
			    	br = new BufferedReader(fr);
			    	
			        String line;
			        while ((line = br.readLine()) != null) {
			            list.add(line);
			        }
			    }
			    finally {
			    	if (br != null) { br.close(); }
			    	if (fr != null) { fr.close(); }
			    }
			    return list;
			}
		} catch (IOException e) { e.printStackTrace(); }
        return null;
    }
    
    public static String numericName(int num, int max) {
    	int maxLength = Integer.toString(max).length();
    	String numString = Integer.toString(num);
    	int numLength = numString.length();
    	
    	int size = maxLength - numLength;
    	while(size >= 0) {
    		numString = "0" + numString;
    		size--;
    	}
    	
    	return numString;
    }
}
