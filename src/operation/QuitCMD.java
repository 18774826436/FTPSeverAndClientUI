package operation;

import java.io.PrintWriter;

public class QuitCMD implements Command{

	boolean state = false;
	String currentdir;
	String dir;
	
	FtpState ftpstate;
	public void Excecute(String param,PrintWriter out,FtpState ftpstate) {
		currentdir = ftpstate.GetCurrentdir();
		dir = ftpstate.GetDir();
		out.println(" 服务关闭连接");
		out.flush();
	}
	
	public FtpState Excecute() {
		ftpstate = new FtpState();
		ftpstate.SetDir(dir);
		ftpstate.SetCurrentdir(currentdir);
		ftpstate.SetState(state);
		return ftpstate;
	}

}
