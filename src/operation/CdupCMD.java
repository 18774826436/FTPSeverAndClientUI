package operation;

import java.io.*;

public class CdupCMD implements Command{

	String currentdir;
	String dir;
	
	FtpState ftpstate;
	public void Excecute(String param, PrintWriter out, FtpState ftpstate) {
		currentdir = ftpstate.GetCurrentdir();
		dir = ftpstate.GetDir();
		
		File f = new File(currentdir);
		if (f.getParent() != null && (!currentdir.equals(dir)))// 有父路径 && 不是根路径
		{
			currentdir = f.getParent();
			out.println("200 命令正确");
		} else {
			out.println("550 当前目录无父路径");
		}
		
	}

	
	public FtpState Excecute() {
		ftpstate = new FtpState();
		ftpstate.SetDir(dir);
		ftpstate.SetCurrentdir(currentdir);
		return ftpstate;
	}

}
