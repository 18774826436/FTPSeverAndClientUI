package operation;

import java.io.PrintWriter;

public class UserCMD implements Command {
	User user;
	FtpState ftpstate = new FtpState();

	public void Excecute(String param,PrintWriter out,FtpState ftpstate) {
		if (param == null) {
			out.println("331 password required for Anonymous");
			out.flush();
		} else {
			out.println("331 password required for "+param);
			out.flush();
		}
	}

	public FtpState Excecute() {
		return ftpstate;
	}

}
