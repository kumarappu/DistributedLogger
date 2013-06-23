package com.innovation.logger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.io.*;

public class LogGeneratorApp  implements Runnable{
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	 DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
    Date date = new Date();
   String logfilename;
   String servername;
   public LogGeneratorApp(String logfilename,String servername){
	   this.logfilename=logfilename;
	   this.servername=servername;
	   
   }

	private  void generateLogs() {
		  
		 Random generator = new Random();
        
      		for(;;){
    			String []DATAPOINTS={"ASKPRICE","BIDPRICE","ASKYIELD","BIDYIELD","RATING","ISSUER_NAME"};
    			//USERNAME|DATAPOINT|STARTDATE|ENDDATE|RESPONSETIME|SERVERNAME
    			String USERNAME="CUSTOMER"+ generator.nextInt( 10 )+"@abc.com";
    			String DATAPOINT=DATAPOINTS[generator.nextInt( 6 )];
    			String STARTDATE= dateFormat.format(new Date());
    			String RESPONSETIME=""+(generator.nextInt(200 )+10);
    			String generatedLog=USERNAME+"|"+DATAPOINT+"|"+STARTDATE+"|"+RESPONSETIME+"|"+servername;
    			//System.out.println(generatedLog);
    			 writeToFile(generatedLog);
    		
    			try {
    				
    				Thread.sleep(generator.nextInt(750 )+300);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
     
		 

		
	}
	
	
    public  void writeToFile(String text) {
        try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
                		logfilename), true));
                bw.write(text);
                bw.newLine();
                bw.close();
        } catch (Exception e) {
        }
}

	@Override
	public void run() {
		
		generateLogs();
	}

public static void main(String[] args){
		
//new LogGeneratorApp().generateLogs();
		
	}

}
