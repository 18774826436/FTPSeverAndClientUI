package operation;

import java.io.PrintWriter;

public class TypeCMD implements Command {
	String currentdir;
	String dir;
	
	FtpState ftpstate;
	public void Excecute(String param, PrintWriter out,FtpState ftpstate) {
		currentdir = ftpstate.GetCurrentdir();
		dir = ftpstate.GetDir();
		if (param.equals("A")) {
			FtpConnection.typestate = "A";
			out.println("200 命令正确 ,转 ASCII 模式");
		} else if (param.equals("I")) {
			FtpConnection.typestate = "I";
			out.println("200 命令正确 转 BINARY 模式");
		} else{
			out.println("504 命令不能执行这种参数");
		}
		out.flush();
	}

	public FtpState Excecute() {
		ftpstate = new FtpState();
		ftpstate.SetDir(dir);
		ftpstate.SetCurrentdir(currentdir);
		return ftpstate;
	}

}
