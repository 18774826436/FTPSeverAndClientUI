package operation;

import java.io.File;
import java.io.PrintWriter;

public class CwdCMD implements Command {

	String currentdir;
	String dir;
	FtpState ftpstate;
	
	public void Excecute(String param, PrintWriter out,FtpState ftpstate) {
		currentdir = ftpstate.GetCurrentdir();
		dir = ftpstate.GetDir();
		File f = new File(param);
		File f1;
		String s = "";
		String tempdir = "";
		if (currentdir.endsWith("/")||currentdir.startsWith("/")) {
			f1 = new File(currentdir + param);
			tempdir = currentdir + param;
		} else {
			f1 = new File(currentdir + "/" + param+"/");
			tempdir = currentdir + "/" + param+"/";
		}

		if (f.isDirectory() && f.exists()) {
			if (param.equals("..") || param.equals("..\\")) { // ��..������һ��Ŀ¼
				if (currentdir.compareToIgnoreCase(dir) == 0) {
					out.println("550 ��·��������");

				} else {
					s = new File(currentdir).getParent();
					if (s != null) {
						currentdir = s;
						out.println("250 ������ļ��������, ��ǰĿ¼��Ϊ: " + currentdir);
					} else
						out.println("550 ��·��������");
				}
			} else {
				currentdir = param;
				out.println("250 ������ļ��������, ����·����Ϊ " + currentdir);
			}
		} else if (f1.isDirectory() && f1.exists()) {
			currentdir = tempdir;
			out.println("250 ������ļ��������, ����·����Ϊ " + currentdir);
		} else {
			out.println("501 �����﷨����");
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
