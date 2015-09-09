package cache;

public class Block {

	private int recordCount;
	private byte[] bytes;
	private boolean isEmpty;
	
	public Block(int count) {
		super();
		this.recordCount = count;
		isEmpty = true;
	}
	public void setBytes(byte[] bytes){
		this.bytes = bytes;
		isEmpty = false;
	}
	public boolean isEmpty(){
		return this.isEmpty;
	}
	public byte[] getBytes(){
		return bytes;
	}
	public int getCount(){
		return this.recordCount;
	}

	
	
	public static void main(String[] args){
		//data    1, "Joe", "Blow"
		//data    2, "Jane", "Public"
		
	}



}
/*
	private int recordCount;
	private byte[] bytes;
	private boolean isEmpty;
	
	public Block(int count) {
		super();
		this.recordCount = count;
		isEmpty = true;
	}
	public void setBytes(byte[] bytes){
		this.bytes = bytes;
		isEmpty = false;
	}
	public boolean isEmpty(){
		return this.isEmpty;
	}
	public byte[] getBytes(){
		return bytes;
	}
	public int getCount(){
		return this.recordCount;
	}
*/