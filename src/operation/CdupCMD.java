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
		if (f.getParent() != null && (!currentdir.equals(dir)))// �и�·�� && ���Ǹ�·��
		{
			currentdir = f.getParent();
			out.println("200 ������ȷ");
		} else {
			out.println("550 ��ǰĿ¼�޸�·��");
		}
		
	}

	
	public FtpState Excecute() {
		ftpstate = new FtpState();
		ftpstate.SetDir(dir);
		ftpstate.SetCurrentdir(currentdir);
		return ftpstate;
	}

}
