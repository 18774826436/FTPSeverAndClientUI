import java.io.BufferedInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ListCommand implements Command {

	public void execute(String param, PrintWriter output) {
		
		try {
			int p0 = 0, p1;
			String s1, s2 = "", s3;
			byte[] buffer = new byte[1024];

			int n;
			
			Vector<String> listvector = new Vector<String>();
			Map<String,String> list = new HashMap<String,String>();		
			Socket listSocket = dataConnection("LIST", output);
			BufferedInputStream instr = new BufferedInputStream(listSocket
					.getInputStream());			
			FClientFrame.serverfilelist.removeAll();			
			while ((n = instr.read(buffer)) > 0) {
				s1 = new String(buffer, 0, n);
				s2 += s1;
				while ((p1 = s2.indexOf("\n", p0)) != -1) {
					s3 = s2.substring(p0, p1);// ·µ»Ø×Ó´®,0 ~ p-1
					p0 = p1 + 1;
					String[] str = s3.split(" ");
					listvector.add(str[str.length - 1]);
					list.put(str[str.length-1], s3);
				}
			}
			FClientFrame.listmap = list;
			FClientFrame.serverfilelist.setListData(listvector);
			FClientFrame.serverfilelist.validate();
			listSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public String getDir(String path) {
		String dirName;
		String[] str = path.split(" ");
		dirName = str[str.length - 1];
		return dirName;
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
