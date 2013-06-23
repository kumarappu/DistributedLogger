package com.innovation.cassandra;

import me.prettyprint.hector.api.Keyspace;

public class SimpleCassandraDao {
	
	Keyspace keyspace;
    String columnFamilyName;
    
    
    
      public Keyspace getKeyspace() {
		return keyspace;
	}
	public void setKeyspace(Keyspace keyspace) {
		this.keyspace = keyspace;
	}
	public String getColumnFamilyName() {
		return columnFamilyName;
	}
	public void setColumnFamilyName(String columnFamilyName) {
		this.columnFamilyName = columnFamilyName;
	}


}
