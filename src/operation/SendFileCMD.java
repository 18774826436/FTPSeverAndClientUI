package operation;

import java.io.*;
import java.net.*;

public class SendFileCMD implements Command {

	Socket datasocket;
	String currentdir;
	String dir;
	
	FtpState ftpstate;
	public void Excecute(String param, PrintWriter out,FtpState ftpstate) {
		currentdir = ftpstate.GetCurrentdir();
		dir = ftpstate.GetDir();
		if (param.equals(null)) {
			out.println("�����﷨����");
		}
		param = addTail(currentdir) + param;
		if (FtpConnection.typestate.equals("I")) {
			try {
				out.println("150 Opening Binary mode data connection for "
						+ param);
				datasocket = new Socket(FtpConnection.rhost,
						FtpConnection.rport, InetAddress.getLocalHost(), 20);
				BufferedOutputStream filewriter = new BufferedOutputStream(
						new FileOutputStream(param));
				BufferedInputStream dataInput = new BufferedInputStream(
						datasocket.getInputStream());
				byte[] buf = new byte[1024];
				int l = 0;
				while ((l = dataInput.read(buf, 0, 1024)) != -1) {
					filewriter.write(buf, 0, l);
				}
				dataInput.close();
				filewriter.close();
				datasocket.close();
				out.println("226 �����������ӽ���");
			} catch (Exception e) {
				e.printStackTrace();
				out.println("451 ����ʧ��: ���������");
			}
		}
		if (FtpConnection.typestate.equals("A"))// ascII
		{
			try {
				out.println("150 Opening ASCII mode data connection for "
						+ param);
				datasocket = new Socket(FtpConnection.rhost,
						FtpConnection.rport, InetAddress.getLocalHost(), 20);
				PrintWriter filewriter = new PrintWriter(new FileOutputStream(
						param));
				BufferedReader dataInput = new BufferedReader(
						new InputStreamReader(datasocket.getInputStream()));
				String s;
				while ((s = dataInput.readLine()) != null) {
					filewriter.println(s);
				}
				dataInput.close();
				filewriter.close();
				datasocket.close();
				out.println("226 �����������ӽ���");
			} catch (Exception e) {
				e.printStackTrace();
				out.println("451 ����ʧ��: ���������");
			}
		}

	}

	private String addTail(String dir) {
		if (!dir.endsWith("/")) {
			dir = dir + "/";
		}
		return dir;
	}

	public FtpState Excecute() {
		ftpstate = new FtpState();
		ftpstate.SetDir(dir);
		ftpstate.SetCurrentdir(currentdir);
		return ftpstate;
	}

}
