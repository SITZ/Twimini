package com.directi.train.tweetapp.services.Auxillary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShardStore {
    @Autowired private RandomStore randomStore;

    @Autowired
    @Qualifier("simpleJdbcTemplate2")
    private SimpleJdbcTemplate db2;

    @Autowired
    @Qualifier("simpleJdbcTemplate1")
    private SimpleJdbcTemplate db1;

    @Autowired
    @Qualifier("shardTemplate")
    private SimpleJdbcTemplate shardDB;

    public SimpleJdbcTemplate getShardByUserId(Long userId) {
        return shardByIndex(shardDB.queryForInt("select shard from shards where userid = ?", userId));
    }

    public SimpleJdbcTemplate getShardByUserName(String userName) {
        return shardByIndex(shardDB.queryForInt("select shard from shards where username = ?", userName));
    }

    public SimpleJdbcTemplate getShardByUserEmail(String eMail) {
        return shardByIndex(shardDB.queryForInt("select shard from shards where email = ?", eMail));
    }

    public SimpleJdbcTemplate getAuthShard() {
        return shardDB;
    }

    public void insertNew(String eMail, String userName, String password) {
        SimpleJdbcTemplate db;

        int random = Math.random() <= 0.5 ? 1 : 2;
        db = shardByIndex(random);

        shardDB.update("insert into shards (username, email, shard) values(?, ?, ?)", userName, eMail, random);
        int userId = shardDB.queryForInt("select userid from shards where username = ?", userName);

        db.update("insert into users (id, email, username, password) values(?, ?, ?, ?)", userId, eMail, userName, password);

    }

    private SimpleJdbcTemplate shardByIndex(int random) {
        SimpleJdbcTemplate db;
        if (random == 1)
            db = db1;
        else
            db = db2;
        return db;
    }
}
