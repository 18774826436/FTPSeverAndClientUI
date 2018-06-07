package operation;

import java.io.PrintWriter;

public class PasswordCMD implements Command {

	String name;
	User user;
	boolean state;
	String currentdir;
	String dir;
	FtpState ftpstate;

	public PasswordCMD(String username) {
		this.name = username;
	}

	public void Excecute(String param, PrintWriter out, FtpState ftpstate) {
		currentdir = ftpstate.GetCurrentdir();
		dir = ftpstate.GetDir();
		if ((name == null) || (name.toLowerCase().equals("anonymous"))) {
			state = true;
			out.println("USER Anonymous Logged in");

		} else {
			for (int i = 0; i < FtpConnection.userinfo.size(); i++) {
				user = FtpConnection.userinfo.get(i);
				if ((!user.getName().equals(name))
						|| (!user.getPWD().equals(param))) {
					state = false;
				}else{
					state = true;
					break;
				}
			}
			if (state) {
				out.println("USER" + name + "已经注册");
			} else {
				out.println("用户名或者密码不正确,拒绝连接");
			}
		}
		out.flush();
	}

	public FtpState Excecute() {
		ftpstate = new FtpState();
		ftpstate.SetCurrentdir(currentdir);
		ftpstate.SetDir(dir);
		ftpstate.SetState(state);
		return ftpstate;
	}

}
