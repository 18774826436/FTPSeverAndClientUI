package operation;

import java.io.*;
import java.net.*;

public class GetFileCMD implements Command {

	Socket datasocket;
	String currentdir;
	String dir;
	
	FtpState ftpstate;

	public void Excecute(String param, PrintWriter out,FtpState ftpstate) {
		currentdir = ftpstate.GetCurrentdir();
		dir = ftpstate.GetDir();
		File f = new File(param);
		if (!f.exists()) {
			f = new File(addTail(currentdir) + param);
			if (!f.exists()) {
				out.println("550 �����ļ�������");
			}
			param = addTail(currentdir) + param;
		}
		if (FtpConnection.typestate.equals("I")) 
		{
			try {
				out.println("�ļ�״̬����,�Զ����ƴ��ļ�:  " + param);
				datasocket = new Socket(FtpConnection.rhost,
						FtpConnection.rport, InetAddress.getLocalHost(), 20);
				BufferedInputStream filereader = new BufferedInputStream(
						new FileInputStream(param));
				PrintStream dataout = new PrintStream(datasocket
						.getOutputStream(), true);
				byte[] buf = new byte[1024]; // Ŀ�껺����
				int l = 0;
				while ((l = filereader.read(buf, 0, 1024)) != -1) // ������δ����
				{
					dataout.write(buf, 0, l); // д���׽���
				}
				filereader.close();
				dataout.close();
				datasocket.close();
				out.println("226 �����������ӽ���");

			} catch (Exception e) {
				e.printStackTrace();
				out.println("451 ����ʧ��: ���������");
			}

		}
		if (FtpConnection.typestate.equals("A"))
		{
			try {
				out.println("��ASCIIģʽ�������� ��"
								+ param);
				datasocket = new Socket(FtpConnection.rhost, FtpConnection.rport, InetAddress
						.getLocalHost(), 20);
				BufferedReader filereader = new BufferedReader(new FileReader(
						param));
				PrintWriter dataout = new PrintWriter(datasocket
						.getOutputStream(), true);
				String s;
				while ((s = filereader.readLine()) != null) {
					dataout.println(s); // /???
				}
				filereader.close();
				dataout.close();
				datasocket.close();
				out.println("�����������ӽ���");
			} catch (Exception e) {
				e.printStackTrace();
				out.println("����ʧ��: ���������");
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
