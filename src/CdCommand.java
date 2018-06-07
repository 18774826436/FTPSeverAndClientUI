


import java.io.*;
import java.util.Vector;


public class CdCommand implements Command{

	public void execute(String param,PrintWriter output) {
		if(param!=null){
			output.println("CWD " + param);
			new ListCommand().execute(param, output);
		}else{
			System.out.println("Ä¿Â¼ÊäÈë´íÎó");
		}
	}

}
