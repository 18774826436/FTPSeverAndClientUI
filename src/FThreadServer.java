
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
		System.out.println("��ӭʹ��Ftp���̷߳�����^_^");
		try {
			serversocket = new ServerSocket(4500);//����˿ں�
			while (true) {
				socket = serversocket.accept();

				out = new PrintWriter(socket.getOutputStream(), true);

				out.println("220 ����������..."); // ֪ͨ�ͻ��˷������Ѿ�����

				FtpConnection newThread = new FtpConnection(socket,dir); // Ϊ��¼�û��½������߳�
				newThread.start();

			}
		} catch (IOException e) {
			System.out.println("��֪����ô�ķ�����������û��׼��������������");
		}
	}
	

	
	public static void main(String[] arg) {
		InputStreamReader is = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(is);
		System.out.println("���������Ŀ¼ �粻��Ҫ���ûس������ɡ�Ĭ��Ϊ"+"c:/ftp/"+":");
		String sdir;
		try {
			sdir = br.readLine();
			if (sdir.equals("")) {
				dir = "c:/ftp/";
			} else {
				dir = sdir;
			}
		} catch (IOException e) {
			e.printStackTrace();//ѡ��Ŀ¼������Ĭ��Ϊc:/ftp/��
		}	
		new FThreadServer();
	}
}
