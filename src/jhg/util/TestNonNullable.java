package jhg.util;

import org.eclipse.jdt.annotation.NonNull;



public class TestNonNullable implements Validated{

	private boolean isDefault;
	private String name;
	
	private TestNonNullable(){
		super();
		name = "";
		isDefault = true;
	}
	
	public TestNonNullable(String s){
		super();
		this.name = s;
		this.isDefault = false;
	}
	public String getName(){
		return this.name;
	}
	
	
	@Override
	public String toString() {
		return "TestNonNullable [name=" + name + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestNonNullable other = (TestNonNullable) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	public static void main(String[] args){
		
		/*
		TestNonNullable x = null;
		boolean a = false;
		if(a){
			x = new TestNonNullable("hey");
		}
		
		TestNonNullable y;
		*/
		TestNonNullable x = TestNonNullable.NULL;
		TestNonNullable y = TestNonNullable.NULL;
		TestNonNullable z = new TestNonNullable("a");
		
		StringBuilder sb = new StringBuilder();
		sb.append("x:"+x.getName());
		sb.append(" y:"+y.getName());
		sb.append(" z:"+z.getName());
		System.out.println(sb.toString());
	}
	
	public static final TestNonNullable NULL = new TestNonNullable();
	
	@Override
	public boolean isDefault() {
		return this.isDefault;
	}

	@Override
	public boolean isValid() {
		return !isDefault;
	}

	@Override
	public void initialize(Object... args) {
		// TODO Auto-generated method stub
		
	}


}
