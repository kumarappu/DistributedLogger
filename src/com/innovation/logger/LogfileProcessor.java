package com.innovation.logger;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import  com.innovation.util.*;

public class LogfileProcessor implements Runnable{

	   @Override
		public void run() {
		   
		  processLogfile();   
	   }
	public void processLogfile(){
		
		//int i=0;
		for(;;){
			
	
			try {
				//System.out.println("sleep buddy");
				Thread.sleep(4500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//System.out.println("fileTracker's size:"+Utility.fileTracker.size());
		    for (Map.Entry<String, String> entry : Utility.fileTracker.entrySet()) {  
		    	String fileName=entry.getKey(); 
		    	String fileCurrentUpdateTime= entry.getValue();
		    	 // System.out.println(fileName + "=" + fileCurrentUpdateTime); 
		    	  
		    	  if(!Utility.fileLineNumtracker.containsKey(fileName)){
		    		  
		    		 // System.out.println("not there");
		    		  
		    		  //found new log file,loading its information in fileLineNumtracker
		    		  String lastLineRead="0";
		    		  String lastUpdatedInfo[]={fileCurrentUpdateTime,lastLineRead};
		    		  Utility.fileLineNumtracker.put(fileName,lastUpdatedInfo );
		    		  readLogfile(fileName,lastUpdatedInfo);
		    	  }
		    	  else{
		    		  String[] lastUpdatedInfo=Utility.fileLineNumtracker.get(fileName);
		    		  String lastUpdatedTime=lastUpdatedInfo[0];
		    		  String lastLineRead=lastUpdatedInfo[1];
		    				  
		    		  try {
						if(Utility.isDateGreater(fileCurrentUpdateTime,lastUpdatedTime))
						{
							//System.out.println("file have been updated again");
							
							readLogfile(fileName,lastUpdatedInfo);
							
							
							
							
						}
						
						else {
							
							//System.out.println("file is same");
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		  
		    	  }
		    
		    } 
		}
		
	}
	
	
	private void readLogfile(String fileName, String[] lastUpdatedInfo) {
		
		String fileUpdateTime=Utility.fileTracker.get(fileName);// Get with latest update time
		String lastLineRead=lastUpdatedInfo[1];
		ArrayList<String> fileContent=new  ArrayList<String>();
		HashMap<String,ArrayList<String>> payLoad=new HashMap<String,ArrayList<String>>();
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			Integer lines =0  ;
			String lineContent;
		    while ((lineContent=reader.readLine()) != null){
		    	
		    	if(lines < Integer.parseInt(lastLineRead)){
		    		lines++;
		    		continue;
		    	}
		    	fileContent.add(lineContent);
		    	//System.out.println(lines+":"+lineContent);
		    	lines++;
		    	
		    }
			
		    // Updating the latest update time and line number of content read in file ..
		    String UpdatedInfo[]={fileUpdateTime,lines.toString()};
  		    Utility.fileLineNumtracker.put(fileName,UpdatedInfo );
  		    
  		    //Update the job Queue with the payload.
  		    String jobId=fileName+"|"+fileUpdateTime;
  		     payLoad.put(jobId, fileContent);
  		     Utility.jobQueue.add(payLoad);
  		     
  		     System.out.println("LogFileProcessor:JobQueueSize "+Utility.jobQueue.size());
			reader.close();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}catch (IOException e) {
		
			e.printStackTrace();
		}
		
		
		
	}
	public static void main(String args[]){
		
		//new LogfileProcessor().processLogfile();
		AbstractApplicationContext context=new ClassPathXmlApplicationContext("spring.xml");
		
	}
}
