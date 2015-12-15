package jhg.security.test;

import jhg.security.*;

@SuppressWarnings("unused")
public class Test {

	
	public static void main(String[] args){
		DummySecured joeMedical = new DummySecured();
		
		SecurityTag tag = new SecurityTag();
		
		joeMedical.setTag(tag);
		
		Action read = new Action();
		Action write = new Action();
		
		DummyRole admin = new DummyRole();
		DummyRole viewer = new DummyRole();
		
		//data.setOwner(admin);
		/*
		 * create some data.
		 * create a security tag with 
		 *   role patient as owner and he can add roles with different combinations of access. 
		 *   doctor can write, 
		 *   counselor can read only, 
		 *   guest cannot read, 
		 *   public cannot count or exist.
		 * 
		 * data owner can always do everything
		 * data owner can open actions globally (share right) or close. workflow.
		 * 
		 * DummySecured joeMedicalRecord = new DummySecured();
		 * 
		 */
	}
}
