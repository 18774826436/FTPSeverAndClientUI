package operation;

import java.io.PrintWriter;

public interface Command {
	public void Excecute(String param,PrintWriter out,FtpState ftpstate);
	public FtpState Excecute();
}
