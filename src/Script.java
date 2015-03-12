import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


public class Script {

	public Script() {
	}
	
	public static String loadFile(File f) {
	    try {
	       BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
	       StringWriter out = new StringWriter();
	       int b;
	       while ((b=in.read()) != -1)
	           out.write(b);
	       out.flush();
	       out.close();
	       in.close();
	       return out.toString();
	    }
	    catch (IOException ie)
	    {
	         ie.printStackTrace();
	         return "erreur";
	    }
	}
	
	public static void main(String args[]) throws IOException{
		//File f = new File("C:/Users/Pierre-Marie/Downloads/CISI/CISI/CISI.ALLnettoye");
		File f = new File("/home-reseau/piairiau/4INFO/TP4info/AcqDeCo2/Projet/CISI/CISI.ALLnettoye");
		String st = loadFile(f);
		String way = "/home-reseau/piairiau/4INFO/TP4info/AcqDeCo2/Projet/CISI/ResultScript/Test";
		PrintWriter out  = new PrintWriter(new FileWriter(way));
		int j= 1;
		for(int i = 0 ; i < st.length() ; i++) {
			if(st.charAt(i) == '.' && st.charAt(i+1) == 'I'){
				String real = way+j;
				j++;
				out.close();
				out = new PrintWriter(new FileWriter(real));
				System.out.print("test");
				i = st.indexOf("\n", i);
			}
			else
			{
				out.print(st.charAt(i));
			}
			
		}
		out.close();
		
	}
}
