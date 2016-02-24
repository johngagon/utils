package jhg.acl;

public interface Mock {
	
	
	public static final String READ = "read";
	public static final String WRITE = "write";
	public static final String TINKER = "tinker";
	public static final String SHARE = "share";
	public static final String PUBLISH = "publish";	
	
	public String read(User user);
	public void write(User user, String m);
	public void tinker(User user);
	public void share(User user);
	public void publish(User user);
}
