package chp.datareceiving;

import java.util.*;

public class FileIntakeDirectory {
	//TODO add the date received on entries.
	private List<FileEntry> entries;

	public FileIntakeDirectory(){
		super();
		entries = new ArrayList<FileEntry>();
	}
	
	public void loadEntriesFromFile(){
		
	}
	
	public void add(FileEntry entry) {
		entries.add(entry);
		
	}
	public List<FileEntry> allEntries(){
		return this.entries;
	}
	public void print(){
		System.out.println(this.toPrettyString());

	}

	public String toPrettyString() {
		StringBuffer buf = new StringBuffer();
		//buf.append("------------------------------------------------------------------------------------------------------------------\n");
		//buf.append("\nFile Log\n");

		String[] header = {"File Name","File Type","Current Status","Report","Scan Start","Scan End","Scan Duration"};
		List<String[]> recs = new ArrayList<String[]>();
		Collections.sort(entries);
		for(FileEntry entry:entries){//TODO use the formatted output report from my utils.
			//System.out.println("  "+entry.toString());
			recs.add(entry.toArray());
		}
		String[][] table = new String[recs.size()+1][header.length];
		table[0] = header;
		int i=1;
		for(String[] r:recs){
			table[i] = r;
			i++;
		}
		
		Table t = new Table(table);
		buf.append(t.toHtmlTable());
		//buf.append("------------------------------------------------------------------------------------------------------------------");
		return buf.toString();
	}
	
}
