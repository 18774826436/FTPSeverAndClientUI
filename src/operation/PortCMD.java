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
			out.println("�����﷨����");
			out.flush();
		} else {
			int p1 = 0;
			int p2 = 0;
			int[] a = new int[6];// ���ip+port
			int i = 0;
			try {
				while ((p2 = param.indexOf(",", p1)) != -1)// һ���ֽڵö���ֱ������
				{
					a[i] = Integer.parseInt(param.substring(p1, p2));
					p2 = p2 + 1;
					p1 = p2;
					i++;
				}
				a[i] = Integer.parseInt(param.substring(p1, param.length()));// ���һλ
			} catch (NumberFormatException e) {
				out.println("�����﷨����");
				out.flush();
			}

			FtpConnection.rhost = a[0] + "." + a[1] + "." + a[2] + "." + a[3];
			FtpConnection.rport = a[4] * 256 + a[5];
			out.println("PORT����ɹ�"); // /����
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
