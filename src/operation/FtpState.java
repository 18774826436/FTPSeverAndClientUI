package operation;

public class FtpState {

	String dir;
	String currentdir;
	boolean state = true;
	public void SetDir(String s){
		this.dir = s;
	}
	public String GetDir(){
		return this.dir;
	}
	public void SetCurrentdir(String s){
		this.currentdir = s;
	}
	public String GetCurrentdir(){
		return this.currentdir;
	}
	public void SetState(boolean s){
		this.state =s;
	}
	public boolean GetState(){
		return this.state;
	}
}
