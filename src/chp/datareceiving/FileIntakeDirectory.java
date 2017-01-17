package chp.datareceiving;

import java.util.*;

public class FileIntakeDirectory {
	//TODO add the date received on entries.
	private List<FileEntry> entries;

	public FileIntakeDirectory(){
		super();
		entries = new ArrayList<FileEntry>();
	}
	public void add(FileEntry entry) {
		entries.add(entry);
		
	}
	public List<FileEntry> allEntries(){
		return this.entries;
	}
	public void print(){
		
		for(FileEntry entry:entries){//TODO use the formatted output report from my utils.
			System.out.println("------------------------------------------------------------------------------------------------------------------");
			System.out.println("\nFile Log");
			System.out.println("  "+entry.toString());
			System.out.println("------------------------------------------------------------------------------------------------------------------");
		}
	}
	
}
