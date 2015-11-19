db-scripts/setup.sql contains the SQL to create the db table with the data sent by Ralph. Run from a client or import (it's MySQL).

Endpoints:
localhost:8081
    /populate:
        create and populate the mapdb file 'testdb' with data read from db populated by db-scripts/setup.sql above. 
    /get/{id}:
        searches for a map from the db by given id
    /keyset:
        added for kicks, returns string representation of key set of the map.

Note: the id being used is an auto-increment one via MySQL. Talked to Ralph and apparently there is a composite key in there and no need to create a surrogate key (or rather, since the actual, existing, db does not have a surrogate key, it would be best not to assume we have one).

---

Still not sure about:

* sync-ing db (SQL server) and cache (in the form of mapdb).
  * Seems to be a non-issue from Ralph's response. I would imagine the approach is to update the mapdb cache when a batch job is not in progress. When it finishes, the batch job can start and act on the cache instead of the db.
  * Problem I'm not sure about is how to determine which rows are new / deleted from db and need to be reflected in cache.
* Is a transaction necessary while executing this batch job? i.e. is the cache effected by batch job, or read only?
  * From online doc: "You also can not modify an object fetched from the db." ... so I guess we're only reading from db?
