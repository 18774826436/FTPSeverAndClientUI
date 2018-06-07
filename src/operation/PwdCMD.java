package operation;

import java.io.PrintWriter;

public class PwdCMD implements Command{

	String currentdir;
	String dir;
	
	FtpState ftpstate;
	
	public void Excecute(String param, PrintWriter out,FtpState ftpstate) {
		currentdir = ftpstate.GetCurrentdir();
		dir = ftpstate.GetDir();
		out.println("257 " + currentdir);
		
	}

	public FtpState Excecute() {
		ftpstate = new FtpState();
		ftpstate.SetDir(dir);
		ftpstate.SetCurrentdir(currentdir);
		return ftpstate;
	}

}
