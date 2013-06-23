package com.innovation.test;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.innovation.cassandra.CassandraDBWriter;
import com.innovation.filewatcher.WatchDir;
import com.innovation.logger.*;

public class TestSimulater {
	public static void main(String[] args) throws IOException {
		Properties properties = new Properties();
		
		String logdirectory="";
		 String servername="";
		
		/*
		 try {
	    		//set the properties value
			 properties.setProperty("servername", "SERVER111");
			 properties.setProperty("logdirectory", "c:\\test");
	    		
	 
	    		//save properties to project root folder
			 properties.store(new FileOutputStream("application.properties"), null);
	 
	    	} catch (IOException ex) {
	    		ex.printStackTrace();
	        }
		 */
		try {
		    properties.load(new FileInputStream("application.properties"));
		 
		   servername=properties.getProperty("servername");
		   logdirectory=properties.getProperty("logdirectory");
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		  
		
		
		//String logdirectory="c:\\test";
		 //String serverName="SERVER111";
		DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
	   
	   
 
 String logfilename=logdirectory+"\\Usage_Logs_from_"+servername+"_"+ dateFormat2.format(new Date())+".txt";

Runnable watchDir = new WatchDir(logdirectory);
Thread watchDirThread = new Thread(watchDir);
watchDirThread.setName("watchDirThread");
System.out.println("starting watchDirThread...");
watchDirThread.start();

System.out.println("watchDirThread started ...");

Runnable logfileProcessor = new LogfileProcessor();
Thread logfileProcessorThread = new Thread(logfileProcessor);
logfileProcessorThread.setName("logfileProcessorThread");
System.out.println("starting logfileProcessorThread...");
logfileProcessorThread.start();
System.out.println("logfileProcessorThread started ...");

Runnable cassandraDBWriter = new CassandraDBWriter();
Thread cassandraDBWriterThread = new Thread(cassandraDBWriter);
cassandraDBWriterThread.setName("cassandraDBWriterThread");
System.out.println("starting cassandraDBWriterThread...");
cassandraDBWriterThread.start();
System.out.println("cassandraDBWriterThread started ...");

try {
	Thread.sleep(2000);
} catch (InterruptedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
Runnable logGenerator = new LogGeneratorApp(logfilename,servername);
Thread logGeneratorThread = new Thread(logGenerator);
logGeneratorThread.setName("logGeneratorThread");
System.out.println("starting logGeneratorThread...");
logGeneratorThread.start();
System.out.println("logGeneratorThread started ...");



	}
}
