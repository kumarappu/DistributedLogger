package com.innovation.test;


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

import com.innovation.cassandra.SimpleCassandraDao;


public class TestCassandraHectorClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

AbstractApplicationContext context=new ClassPathXmlApplicationContext("spring-cassandra.xml");
   
         SimpleCassandraDao dao= (SimpleCassandraDao) context.getBean("simpleCassandraDao");  
         if(dao==null)System.out.println("failed");
         else System.out.println("passed");
         
         Keyspace keyspaceOperator =dao.getKeyspace();
         try {
             Mutator<String> mutator = HFactory.createMutator(keyspaceOperator, StringSerializer.get());
             mutator.insert("jsmith", "userinfo", HFactory.createStringColumn("first1", "John"));
             mutator.insert("jsmith", "userinfo", HFactory.createStringColumn("last", "Appu"));
             ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(keyspaceOperator);
             columnQuery.setColumnFamily("userinfo").setKey("jsmith").setName("first1");
             QueryResult<HColumn<String, String>> result = columnQuery.execute();
             
             System.out.println("Read HColumn from cassandra: " + result.get());            
             System.out.println("Verify on CLI with:  get Keyspace1.Standard1['jsmith'] ");
             
         } catch (HectorException e) {
             e.printStackTrace();
         }
         
	}

}
