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
			out.println("200 ������ȷ ,ת ASCII ģʽ");
		} else if (param.equals("I")) {
			FtpConnection.typestate = "I";
			out.println("200 ������ȷ ת BINARY ģʽ");
		} else{
			out.println("504 �����ִ�����ֲ���");
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
