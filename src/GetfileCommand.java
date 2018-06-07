


import java.io.*;
import java.net.*;
import java.util.Vector;

public class GetfileCommand implements Command{

	public void execute(String param,PrintWriter output) {

		FileOutputStream file = null;
		Socket getfilesocket;
		byte[] buffer = new byte[1024];
		try {
			String s = param.trim();
			file = new FileOutputStream(s);
			String[] str = s.split("/");
			String dirname = str[str.length-1];
			getfilesocket = dataConnection("RETR " + dirname,output);
			int n;
			while ((n = getfilesocket.getInputStream().read(buffer)) > 0) {
				file.write(buffer, 0, n);
			}
			getfilesocket.close();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public Socket dataConnection(String ctrlcmd, PrintWriter output) {
		Socket connectSocket = null;
		ServerSocket servSocket = null;
		try {
			servSocket = new ServerSocket(0, 1);
			byte[] buffer = InetAddress.getLocalHost().getAddress();
			int i;
			String cmd = "PORT ";
			for (i = 0; i < buffer.length; i++) {
				cmd = cmd + (buffer[i] & 0xff) + ",";
			}
			cmd = cmd
					+ (servSocket.getLocalPort() / 256 + "," + servSocket
							.getLocalPort() % 256);
			output.println(cmd);
			output.flush();
			output.println(ctrlcmd);
			output.flush();
			connectSocket = servSocket.accept();
			servSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return connectSocket;

	}
}
