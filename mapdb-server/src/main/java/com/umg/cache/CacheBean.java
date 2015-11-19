package com.umg.cache;

import java.io.File;

import javax.annotation.PostConstruct;

import org.mapdb.DB;

import com.umg.db2mapdb.CacheManager;

public class CacheBean {
	
	CacheManager cm = new CacheManager();
	
	String cacheFilePath;
	
	DB db;
	
	@PostConstruct
	public void init() throws Exception {
		File cacheFile = new File(cacheFilePath);
		if(cacheFile.exists()) {
			// load cache from file
			db = cm.loadCacheFromFile(cacheFilePath);
			
		} else {
			// load cache from db
//			db = cm.loadCacheFromDatabase(ds, databaseFileName, tableName, tableColumnNames, keyColumnNames);
		}
	}

}
