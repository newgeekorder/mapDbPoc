package com.umg;

import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mapdb {
	private static final Logger LOG = LoggerFactory.getLogger(Mapdb.class);
	protected DB db;
	protected ConcurrentNavigableMap<Integer, Object> clientsMap;

	public void init() throws Exception {
		LOG.info("Initializing Memdb...");

		db = DBMaker.fileDB(new File("testdb")).closeOnJvmShutdown()
				.encryptionEnable("password").make();
		this.clientsMap = db.treeMap("clients");
		LOG.info("Done initializing Memdb...");
	}

	public void cleanUp() throws Exception {
		LOG.info("Cleaning up Memdb...");
		db.close();
		LOG.info("Done cleaning up Memdb...");
	}

	public DB getDb() {
		return db;
	}

	public void setDb(DB db) {
		this.db = db;
	}

	public ConcurrentNavigableMap<Integer, Object> getClientsMap() {
		return clientsMap;
	}

	public void setClientsMap(ConcurrentNavigableMap<Integer, Object> clientsMap) {
		this.clientsMap = clientsMap;
	}

}
