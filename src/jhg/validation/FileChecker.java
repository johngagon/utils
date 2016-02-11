package jhg.validation;

public class FileChecker {

	
	
	public static void main(String[] args){
		testSampleData();
	}
	
	public static void testSampleData(){
		
		String[][] data = {
				{"John","5/31/1971"},
				{"Ray","11/15/1967"},
				{"Mom",""}
		};
		String[] fields = {"name","bdate"};
		int count = 3;
		String dataName = "contacts";
		
		
		
		Preprocessor processor = new ContactPreprocessor(dataName,count,fields,data);
		
		processor.execute();
		
		PreprocessResult report = processor.getReport();
		
		
		System.out.println(report);
		

	}


}
