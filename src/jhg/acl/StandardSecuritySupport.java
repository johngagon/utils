package jhg.acl;


public class StandardSecuritySupport {/*extends SecuritySupport {

	public static final String READ = "read";
	public static final String WRITE = "write";
	public static final String COUNT = "count";
	public static final String SHARE = "share";
	public static final String PUBLISH = "publish";		
	
	protected static String[] perms = new String[] {READ,WRITE,COUNT,SHARE,PUBLISH};
	
	
	
	protected StandardSecuritySupport(User user){
		super(user,perms);
		
	}
	
	
	protected List<StandardSecuritySupport> instances = new ArrayList<StandardSecuritySupport>();
	
	
	public MockSecuredObject(User user, String m){
		if(!policy.allowsOwner(user)){
			throw new IllegalAccessError("Policy does not allow this owner:"+user);
			//you could conceivably have a constructor that takes a user and allow other users besides owner to create.
			//here, only owners can create since they also set permissions.
		}else{
			this.msg = m;
			this.tag = new SecurityTag(user,this);
			tag.setPermission(TINKER,MockUser.find(MockUser.FRANK));//tinker substitute for count.
			instances.add(this);
		}
		
	}
	
	public String read(User user){
		return (tag.check(user,READ))?msg:"";
	}
	public void write(User user, String m){
		this.msg = tag.check(user, WRITE)?m:msg;
	}
	public void tinker(User user){
		if(!tag.check(user, TINKER)){return;}
	}
	
	public void share(User user){
		if(!tag.check(user, SHARE)){return;}
		tag.setPermission(TINKER,MockUser.find(MockUser.JOE));
		tag.setPermission(READ,MockUser.find(MockUser.JOE));
		
		tag.setPermission(TINKER,MockUser.find(MockUser.CHET));
		tag.setPermission(READ,MockUser.find(MockUser.CHET));
		tag.setPermission(WRITE,MockUser.find(MockUser.CHET));		
	}
	public void publish(User user){
		if(!tag.check(user, PUBLISH)){return;}
		tag.setPermission(TINKER,MockUser.find(MockUser.FENTON));
		tag.setPermission(READ,MockUser.find(MockUser.FENTON));
		tag.setPermission(READ,MockUser.find(MockUser.FRANK));
	}
	
	public void flushAccessLog(){
		
		tag.print();
		tag.clear();
		System.out.println("---------------------");
	}
/*
	private static User owner = new MockUser(JOHN);
	
	private static User blinded = new MockUser(FRANK);
	private static User sharedReader = new MockUser(JOE);
	private static User sharedContributer = new MockUser(CHET);
	private static User publishedReader = new MockUser(FENTON);//he could be anyone
	
 *
	
	@Override
	public int getId() {
		return hashCode();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 *
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((msg == null) ? 0 : msg.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 *
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MockSecuredObject other = (MockSecuredObject) obj;
		if (msg == null) {
			if (other.msg != null)
				return false;
		} else if (!msg.equals(other.msg))
			return false;
		return true;
	}

	@Override
	public SecurityTag getTag() {
		return this.tag;
	}

	@Override
	public SecurityPolicy getPolicy() {
		return policy;
	}

	public static void setPermissions(User u, List<Secure> securedList, User user, String perm){
		if(policy.getOwner().isSameIdentityAs(u)){
			for(Secure s:securedList){
				s.setPermission(perm,user);
			}
		}
	}

	@Override
	public void setPermission(String perm, User user) {
		tag.setPermission(perm, user);
	}
	
	public void printAclTable(){
		
		tag.printAclTable();
		System.out.println("---------------------");
	}
	*/
}
