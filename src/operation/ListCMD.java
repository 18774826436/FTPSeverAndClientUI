package operation;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ListCMD implements Command {

	String currentdir;
	String dir;
	FtpState ftpstate;
	Socket datasocket;

	public void Excecute(String param, PrintWriter out, FtpState ftpstate) {
		currentdir = ftpstate.GetCurrentdir();
		dir = ftpstate.GetDir();
		try {
			datasocket = new Socket(FtpConnection.rhost, FtpConnection.rport,
					InetAddress.getLocalHost(), 20);
			PrintWriter dataout = new PrintWriter(datasocket.getOutputStream(),
					true);

			out.println("请求成功，以文本模式传输");
			out.flush();
			File f = new File(currentdir);
			String dateStr;
			File[] files = f.listFiles();
			for (int i = 0; i < files.length; i++) {
				dateStr = new SimpleDateFormat("MMM dd hh:mm",Locale.ENGLISH).format(new Date(files[i].lastModified()));
				if (files[i].isDirectory()) {
					dataout.println("drw-rw-rw-  1 kenaky       0 "
				            + dateStr + " " + files[i].getName());

				} else {
					dataout.println("-rw-rw-rw-  1 kenaky  "
				            + files[i].length() + " " + dateStr + " " + files[i].getName());
				}

			}
			dataout.close();
			datasocket.close();
			out.println("传输结束");
		} catch (Exception e) {
			e.printStackTrace();
			out.println("传输时发生错误");
		}
	}

	public FtpState Excecute() {
		ftpstate = new FtpState();
		ftpstate.SetDir(dir);
		ftpstate.SetCurrentdir(currentdir);
		return ftpstate;
	}

}
