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
			if (param.equals("..") || param.equals("..\\")) { // 用..代表上一层目录
				if (currentdir.compareToIgnoreCase(dir) == 0) {
					out.println("550 此路径不存在");

				} else {
					s = new File(currentdir).getParent();
					if (s != null) {
						currentdir = s;
						out.println("250 请求的文件处理结束, 当前目录变为: " + currentdir);
					} else
						out.println("550 此路径不存在");
				}
			} else {
				currentdir = param;
				out.println("250 请求的文件处理结束, 工作路径变为 " + currentdir);
			}
		} else if (f1.isDirectory() && f1.exists()) {
			currentdir = tempdir;
			out.println("250 请求的文件处理结束, 工作路径变为 " + currentdir);
		} else {
			out.println("501 参数语法错误");
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
