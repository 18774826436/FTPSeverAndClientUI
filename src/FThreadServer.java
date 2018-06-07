
import java.io.*;
import java.net.*;
import java.util.*;

import operation.*;

public class FThreadServer {

	static String dir;
	private Socket socket;
	private ServerSocket serversocket;
		
	PrintWriter out;
	
	public FThreadServer() {
		System.out.println("欢迎使用Ftp多线程服务器^_^");
		try {
			serversocket = new ServerSocket(4500);//定义端口号
			while (true) {
				socket = serversocket.accept();

				out = new PrintWriter(socket.getOutputStream(), true);

				out.println("220 服务器就绪..."); // 通知客户端服务器已经就绪

				FtpConnection newThread = new FtpConnection(socket,dir); // 为登录用户新建连接线程
				newThread.start();

			}
		} catch (IOException e) {
			System.out.println("不知道怎么的服务器，好像没有准备就绪！！！！");
		}
	}
	

	
	public static void main(String[] arg) {
		InputStreamReader is = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(is);
		System.out.println("输入服务器目录 如不需要，敲回车键即可。默认为"+"c:/ftp/"+":");
		String sdir;
		try {
			sdir = br.readLine();
			if (sdir.equals("")) {
				dir = "c:/ftp/";
			} else {
				dir = sdir;
			}
		} catch (IOException e) {
			e.printStackTrace();//选择目录，否则默认为c:/ftp/；
		}	
		new FThreadServer();
	}
}
