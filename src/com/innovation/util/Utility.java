package com.innovation.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;



public class Utility {

	
	public static HashMap<String,String> fileTracker=new HashMap<String,String>(); 
	
	//this keep track of line number in files which have been loaded to cassandra
	public static HashMap<String,String[]> fileLineNumtracker=new HashMap<String,String[]>(); 
	
	public static  Queue<HashMap<String,ArrayList<String>>> jobQueue=new LinkedList< HashMap<String,ArrayList<String>>>();
	
	public static boolean isDateGreater(String string1, String string2 ) throws ParseException{
		
		  String myFormatString = "yyyy/MM/dd HH:mm:ss"; 
		  SimpleDateFormat df = new SimpleDateFormat(myFormatString);
		  Date date1 = df.parse(string1);
		  Date date2 = df.parse(string2);
		  if (date1.after(date2)) return true;
		  else return false;
	
		
	}
	
	
}
