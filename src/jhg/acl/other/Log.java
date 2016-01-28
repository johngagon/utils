package jhg.acl.other;

public class Log {

	private String name;
	public Log(String n){
		this.name = n;
	}
	public void debug(String msg){
		System.out.println(name+" - "+msg);
	}
}
