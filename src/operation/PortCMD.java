package operation;

import java.io.PrintWriter;

public class PortCMD implements Command {

	boolean state = true;
	String currentdir;
	String dir;
	FtpState ftpstate;

	public void Excecute(String param, PrintWriter out, FtpState ftpstate) {
		currentdir = ftpstate.GetCurrentdir();
		dir = ftpstate.GetDir();
		
		if (param == null) {
			out.println("参数语法错误");
			out.flush();
		} else {
			int p1 = 0;
			int p2 = 0;
			int[] a = new int[6];// 存放ip+port
			int i = 0;
			try {
				while ((p2 = param.indexOf(",", p1)) != -1)// 一个字节得读，直到读完
				{
					a[i] = Integer.parseInt(param.substring(p1, p2));
					p2 = p2 + 1;
					p1 = p2;
					i++;
				}
				a[i] = Integer.parseInt(param.substring(p1, param.length()));// 最后一位
			} catch (NumberFormatException e) {
				out.println("参数语法错误");
				out.flush();
			}

			FtpConnection.rhost = a[0] + "." + a[1] + "." + a[2] + "." + a[3];
			FtpConnection.rport = a[4] * 256 + a[5];
			out.println("PORT命令成功"); // /测试
			out.flush();
		}
	}

	public FtpState Excecute() {
		ftpstate = new FtpState();
		ftpstate.SetDir(dir);
		ftpstate.SetCurrentdir(currentdir);
		return ftpstate;
	}

}
