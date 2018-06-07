


import java.io.PrintWriter;
import java.util.Vector;

public class TypeCommand implements Command{

	public void execute(String param, PrintWriter output) {
		output.println("TYPE "+param);
		output.flush();
	}

}
