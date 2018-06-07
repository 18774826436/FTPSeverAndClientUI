


import java.io.*;
import java.net.*;
import java.util.Vector;

public class UpfileCommand implements Command {

	public void execute(String param, PrintWriter output) {
		FileInputStream file = null;
		byte[] buff = new byte[1024];
		Socket datasocket;
		int n;
		try {
			file = new FileInputStream(param);
			String[] str = param.split("/");
			String dirname = str[str.length-1];
			datasocket = dataConnection("STOR " + dirname, output);
			OutputStream out = datasocket.getOutputStream();
			while ((n = file.read(buff)) > 0) {
				out.write(buff, 0, n);
			}
			datasocket.close();
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
