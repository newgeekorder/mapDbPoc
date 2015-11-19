package com.umg;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapDBOperations {

	private static final Logger LOG = LoggerFactory.getLogger(Mapdb.class);

	protected Mapdb mapdb;

	public Integer populate(List<Map<String, Object>> payload) {
		LOG.info("about to populate mapdb....");
		DB db = mapdb.getDb();

		ConcurrentNavigableMap<Integer, Object> map = db.treeMap("clients");
		for (Map<String, Object> row : payload) {
			map.put((Integer) row.get("ID"), row);
		}
		// persist changes into disk
		db.commit();

		return 0;
	}

	public Object get(String key) {
		return this.get(Integer.parseInt(key));
	}
	
	public Object get(Integer key) {
		ConcurrentNavigableMap<Integer, Object> clientsMap = mapdb.getClientsMap();
		return clientsMap.get(key);
	}
	
	public Set<Integer> keySet() {
		ConcurrentNavigableMap<Integer, Object> clientsMap = mapdb.getClientsMap();
		return clientsMap.keySet();
	}

	public Mapdb getMapdb() {
		return mapdb;
	}

	public void setMapdb(Mapdb mapdb) {
		this.mapdb = mapdb;
	}

}
