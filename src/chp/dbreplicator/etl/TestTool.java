package chp.dbreplicator.etl;

import chp.dbreplicator.Database;
import chp.dbreplicator.DatabaseManager;
import chp.dbreplicator.Log;

public class TestTool {

	
	
	
	public static void testProd(){
		Log.pl("\n\nTesting Prod Foundation.");
		Database db = Database.DMFPRD;
		DatabaseManager database = new DatabaseManager(db,new Log());
		database.connect();
		boolean connected = database.test();
		Log.pl("Connected to "+db.name()+" is connected: "+connected);
		if(!connected){
			Log.pl("!!! Connection failed: "+db.name());
		}
		database.close();
		
		Log.pl("Testing Prod New (Benchmarking).");
		db = Database.DMPRODNEW;
		database = new DatabaseManager(db,new Log());
		database.connect();
		connected = database.test();
		Log.pl("Connected to "+db.name()+" is connected: "+connected);
		if(!connected){
			Log.pl("!!! Connection failed: "+db.name());
		}		
		database.close();
		
	}
	
	public static void testUAT(){
		Log.pl("\n\nTesting UAT Foundation.");
		Database db = Database.DMFUAT;
		DatabaseManager database = new DatabaseManager(db,new Log());
		database.connect();
		boolean connected = database.test();
		Log.pl("Connected to "+db.name()+" is connected: "+connected);
		if(!connected){
			Log.pl("!!! Connection failed: "+db.name());
		}		
		database.close();
		
		Log.pl("Testing UAT New (Benchmarking).");
		db = Database.DMTESTNEW;
		database = new DatabaseManager(db,new Log());
		database.connect();
		connected = database.test();
		Log.pl("Connected to "+db.name()+" is connected: "+connected);
		if(!connected){
			Log.pl("!!! Connection failed: "+db.name());
		}		
		database.close();		
	}
	
	public static void testDev(){
		Log.pl("\n\nTesting Dev Foundation.");
		Database db = Database.DMFDEV;
		DatabaseManager database = new DatabaseManager(db,new Log());
		database.connect();
		boolean connected = database.test();
		Log.pl("Connected to "+db.name()+" is connected: "+connected);
		if(!connected){
			Log.pl("!!! Connection failed: "+db.name());
		}		
		database.close();
		
		Log.pl("Testing Dev New (Benchmarking).");
		db = Database.DMDEVNEW;
		database = new DatabaseManager(db,new Log());
		database.connect();
		connected = database.test();
		Log.pl("Connected to "+db.name()+" is connected: "+connected);
		if(!connected){
			Log.pl("!!! Connection failed: "+db.name());
		}		
		database.close();		
	}
	
	public static void test(){
		testDev();
		testUAT();
		testProd();
	}
	
	public static void main(String[] args){
		
		try{
			Log.pl("Starting all tests");
			test();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			Log.pl("End all tests.");
		}
	}
}
