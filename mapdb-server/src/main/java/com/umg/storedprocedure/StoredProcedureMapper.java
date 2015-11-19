package com.umg.storedprocedure;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.resource.spi.IllegalStateException;
import javax.sql.DataSource;

import org.mapdb.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umg.cache.InMemCacheDriver;
import com.umg.cache.storedprocedure.ports.Copa3DeriveLabel;
import com.umg.db2mapdb.TableInfo;

public class StoredProcedureMapper {
	
	public static final String COPA3_DERIVE_LABEL = "copa3DeriveLabel";

	private static final Logger LOG = LoggerFactory.getLogger(StoredProcedureMapper.class);
	
	InMemCacheDriver cacheDriver;
	DataSource dataSource;
	List<TableInfo> tableInfoList;
	String cacheDirectoryPath;
	String dbName;
	String cacheDumpDirectoryPath;

	protected Copa3DeriveLabel copa3DeriveLabel;
//	public StoredProcedureMapper(CacheDriver cacheDriver) {
//		this.cacheDriver = cacheDriver;
//	}
	
	public void init() throws Exception {
//		System.out.println("cacheDriver: " + cacheDriver);
//		System.out.println("dataSource: " + dataSource);
//		System.out.println("tableInfoList: " + tableInfoList);
//		System.out.println("cacheDirectoryPath: " + cacheDirectoryPath);
//		System.out.println("cacheDumpDirectoryPath: " + cacheDumpDirectoryPath);
		LOG.info("Initializing StoredProcedureMapper");
		for(TableInfo tableInfo : tableInfoList) {
			final String tableName = tableInfo.getTableName();
			if(!cacheDriver.isLoaded(tableName)) {
				DB db = cacheDriver.loadFromFile(cacheDirectoryPath + File.separator + tableName, tableName);
				if (db == null) {
					String selectQuery = "SELECT * FROM [" + dbName + "].[dbo].[" + tableName + "]";
					// am only loading from db and storing in memory here... am not saving to file
					db = cacheDriver.loadFromDb(selectQuery, dataSource, tableName, tableName, tableInfo.getAllTableColumnNames(), tableInfo.getKeyTableColumnNames());
					if (db == null) {
						throw new IllegalStateException("Could not load data from database with name " + dbName + " from table " + tableName);
					}
				}
			}
		}
		
		// check again for now (make sure that db was saved in memory in loading methods
		for(TableInfo tableInfo : tableInfoList) {
			final String tableName = tableInfo.getTableName();
			if(!cacheDriver.isLoaded(tableName)) {
				throw new IllegalStateException("cache with name " + tableName + " should have been loaded in memory but it is not....");
			}
		}
		copa3DeriveLabel = new Copa3DeriveLabel(cacheDriver);
//		cacheDriver.isLoaded(cacheName);
//		cacheManager.loadCachesDbFallback();

		writeDateFile(cacheDumpDirectoryPath, cacheDriver.csvNamesOfLoadedCaches());
		dumpLoadedCaches(cacheDumpDirectoryPath);
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getCacheDirectoryPath() {
		return cacheDirectoryPath;
	}

	public void setCacheDirectoryPath(String cacheDirectoryPath) {
		this.cacheDirectoryPath = cacheDirectoryPath;
	}

	public InMemCacheDriver getCacheDriver() {
		return cacheDriver;
	}

	public void setCacheDriver(InMemCacheDriver cacheDriver) {
		this.cacheDriver = cacheDriver;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public List<TableInfo> getTableInfoList() {
		return tableInfoList;
	}

	public void setTableInfoList(List<TableInfo> tableInfoList) {
		this.tableInfoList = tableInfoList;
	}

	public void destroy() throws IOException {
		LOG.info("Shutting down StoredProcedureMapper");
	}

	private void dumpLoadedCaches(String dumpDir) throws Exception {
		cacheDriver.dumpCachesInDirectory(dumpDir);
	}

	private void writeDateFile(String dir, String cacheNameAsCsv) throws IOException {
		Date date = new Date() ;
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
//		File file = new File(dateFormat.format(date)) ;
		File file = new File(dir + File.separator + "LAST_DUMP") ;
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
//		out.write("This file's name shows the last time files in this directory were dumped");
		out.write("Last dump of " + cacheNameAsCsv + " was at: " + date);
		out.close();
	}

	public String copa3DeriveLabel(String upc, String isrc, String sellingCountry, String globalIndicator, String interfaceType, String companyCodeIn, String globalRepertoire) {
		try {
			return copa3DeriveLabel.copa3DeriveLabel(upc, isrc, sellingCountry, globalIndicator, interfaceType, companyCodeIn, globalRepertoire);
		} catch (Exception e) {
			LOG.info("copa3DeriveLabel failed - exception message: {}", e.getMessage());
			e.printStackTrace();
		}
		
		return "";
	}

	public String getCacheDumpDirectoryPath() {
		return cacheDumpDirectoryPath;
	}

	public void setCacheDumpDirectoryPath(String cacheDumpDirectoryPath) {
		this.cacheDumpDirectoryPath = cacheDumpDirectoryPath;
	}

}
