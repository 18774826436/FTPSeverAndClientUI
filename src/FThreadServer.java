
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

		Scanner input = new Scanner(System.in);
		System.out.println("����ѡ����Ҫ���ŵĶ˿ںţ���������");
		String port = "4500";
		int portNumber;
		port = input.nextLine();
		while (true) {
			try {
				portNumber = Integer.parseInt(port);
				break;// ����ǿת�ɹ�������ֹѭ��
			} catch (Exception e) {
				System.out.println("�����" + port + "�������֣���������");
				port = input.next();// ǿתʧ�ܣ���������
			}
		}
		System.out.println("��ӭʹ��Ftp���̷߳�����^_^����������Ϊ��������" +port+ "�˿ںţ�������ͨ������˿ںţ����е�¼�����ݵĹ���");
		try {

			serversocket = new ServerSocket(Integer.parseInt(port));//����˿ں�
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
