package com.innovation.cassandra;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.innovation.logger.*;
import com.innovation.util.Utility;

public class CassandraDBWriter implements Runnable {

	
	  Keyspace keyspaceOperator;
	@Override
	public void run() {
		processJob();
		
		
	}

	
	
	public void processJob() {
		AbstractApplicationContext context=new ClassPathXmlApplicationContext("spring-cassandra.xml");
       SimpleCassandraDao dao= (SimpleCassandraDao) context.getBean("simpleCassandraDao");
        this.keyspaceOperator =dao.getKeyspace();
       
       HashMap<String,ArrayList<String>> payLoad;
		for(;;){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("CassandraDBWriter:JobQueueSize "+Utility.jobQueue.size());
			if(Utility.jobQueue.size()!=0){
			payLoad=Utility.jobQueue.poll();
			//System.out.println("cass:"+ payLoad);
			
			 
			 String jobId=null;
			 ArrayList<String> fileContent=null;
			 for (Map.Entry<String, ArrayList<String>> entry : payLoad.entrySet()) {  
			    	 jobId=entry.getKey(); // file name with time stamp is jobID
			    	 fileContent= entry.getValue();
			    	 writeToCassandra(jobId,fileContent);
			    	  
			 }
			 
			 
				
			
			}
			
		}
		
		
	}



	private void writeToCassandra(String jobId, ArrayList<String> fileContent) {

        try {
            Mutator<String> mutator = HFactory.createMutator(keyspaceOperator, StringSerializer.get());
            
          // jobId="100";
           String column="";
          
            int i=0;
            for(String s:fileContent){
            	//String column="col"+i++;
            	//System.out.println("Column:"+column);
            	String [] tokens=s.split("\\|");
            //	System.out.println("key"+tokens.length);
            	//for(int j=0;i<tokens.length;++i)System.out.println("token"+tokens[j]);
            	
            mutator.insert(tokens[0], "userinfo", HFactory.createColumn(tokens[2], s));
            jobId=tokens[0];
            column=tokens[2];
            }
            
            ColumnQuery<String, String,String> columnQuery = HFactory.createStringColumnQuery(keyspaceOperator);
            columnQuery.setColumnFamily("userinfo").setKey(jobId).setName(column);
            QueryResult<HColumn<String, String>> result = columnQuery.execute();
            
            System.out.println("Read a random record from cassandra: " + result.get());            
                        
        } catch (HectorException e) {
            e.printStackTrace();
        }
		
	}


}
