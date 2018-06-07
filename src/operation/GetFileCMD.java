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
				out.println("550 所求文件不存在");
			}
			param = addTail(currentdir) + param;
		}
		if (FtpConnection.typestate.equals("I")) 
		{
			try {
				out.println("文件状态正常,以二进制打开文件:  " + param);
				datasocket = new Socket(FtpConnection.rhost,
						FtpConnection.rport, InetAddress.getLocalHost(), 20);
				BufferedInputStream filereader = new BufferedInputStream(
						new FileInputStream(param));
				PrintStream dataout = new PrintStream(datasocket
						.getOutputStream(), true);
				byte[] buf = new byte[1024]; // 目标缓冲区
				int l = 0;
				while ((l = filereader.read(buf, 0, 1024)) != -1) // 缓冲区未读满
				{
					dataout.write(buf, 0, l); // 写入套接字
				}
				filereader.close();
				dataout.close();
				datasocket.close();
				out.println("226 传输数据连接结束");

			} catch (Exception e) {
				e.printStackTrace();
				out.println("451 请求失败: 传输出故障");
			}

		}
		if (FtpConnection.typestate.equals("A"))
		{
			try {
				out.println("打开ASCII模式数据连接 ："
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
				out.println("传输数据连接结束");
			} catch (Exception e) {
				e.printStackTrace();
				out.println("请求失败: 传输出故障");
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
