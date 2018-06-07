import java.io.PrintWriter;


public class CdupCommand implements Command{

	public void execute(String param, PrintWriter output) {
		try {
			output.println("CDUP ");// CDUP√¸¡Ó
			output.flush();
			new ListCommand().execute(param, output);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
