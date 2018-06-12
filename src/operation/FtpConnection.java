package operation;

import java.io.*;
import java.net.*;
import java.util.*;

public class FtpConnection extends Thread {

	public static String rhost = "";
	public static int rport;
	public static String typestate = "A";

	private Command ListCMD, PortCMD, QuitCMD, TypeCMD, PwdCMD,
			RETRCMD, STORCMD, CWDCMD,CDUPCMD;
	/**
	 * 将用户信息从本地文件系统中调出存放到userinfo列表中
	 */
	public static ArrayList<User> userinfo = new ArrayList<User>();//在配置文件user.ini中
	/**
	 * 保存各种命令列表
	 */
	public Map<String, Command> commands = new HashMap<String, Command>();

	private Socket socket;
	public PrintWriter out;
	private BufferedReader in;

	public String username = null;
	String cmd;
	String param;
	public String dir;
	public String currentdir;
	boolean state = true;
	User user;
	FtpState ftpstate = new FtpState();

	public FtpConnection(Socket s, String d) {
		this.socket = s;
		dir = d;
		currentdir = d;
	}

	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			loadUserInfo();
			loadCommands();

			while (state) {
				String s = in.readLine();
				if (s.equals(null)) {
					state = false;
				} else {
					String[] str = s.split(" "); 

					// 解析命令
					if (str.length == 1) {
						cmd = str[0];
						param = null;
					} else {
						cmd = str[0];
						param = s.split(" ")[1];

					}

					// 执行相应的命令
					if (cmd.equals("USER")) {
						username = param;
						UserCMD user = new UserCMD();
						user.Excecute(param, out,ftpstate);
					} else if(cmd.equals("PASS")){
						PasswordCMD password = new PasswordCMD(username);
						password.Excecute(param, out, ftpstate);
						ftpstate = password.Excecute();
						state = ftpstate.GetState();
					}else{
						if (commands.containsKey(cmd)) {
							ftpstate.SetCurrentdir(currentdir);
							ftpstate.SetDir(dir);
							ftpstate.SetState(state);
							Command command = (Command) commands.get(cmd);
							command.Excecute(param, out,ftpstate);
							ftpstate = command.Excecute();				
							dir = ftpstate.GetDir();
							currentdir = ftpstate.GetCurrentdir();
							state = ftpstate.GetState();
						} else {
							out.println("500 语法错误");
							out.flush();
						}
					}
				}
			}

		} catch (IOException e) {
			state = false;
			System.out.println("socket close");
		}

	}

	public void loadUserInfo() {
		try {
			FileReader param = new FileReader("src/user.ini");
			BufferedReader in = new BufferedReader(param);
			boolean eof = false;
			do {
				String s = in.readLine();

				if (s == null) {
					eof = true;
				} else {
					user = new User();
					user.setName(s.split(" ")[0]);
					user.setPWD(s.split(" ")[1]);
					userinfo.add(user);
				}

			} while (!eof);
		} catch (IOException e) {
			System.out.println("文件不存在");
		}
	}

	public void loadCommands() {
		
		ListCMD = new ListCMD();
		PortCMD = new PortCMD();
		QuitCMD = new QuitCMD();
		TypeCMD = new TypeCMD();
		PwdCMD = new PwdCMD();
		RETRCMD = new GetFileCMD();
		STORCMD = new SendFileCMD();
		CWDCMD = new CwdCMD();
		CDUPCMD = new CdupCMD();
		commands.put("LIST", ListCMD);
		commands.put("PORT", PortCMD);
		commands.put("QUIT", QuitCMD);
		commands.put("TYPE", TypeCMD);
		commands.put("PWD", PwdCMD);
		commands.put("RETR", RETRCMD);
		commands.put("STOR", STORCMD);
		commands.put("CWD", CWDCMD);
		commands.put("CDUP", CDUPCMD);
	}
	
	public String GetDir(){
		return currentdir;
	}
	public void SaveDir(String s){
		currentdir =s;
	}
}
