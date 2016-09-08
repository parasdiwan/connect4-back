package com.gluck.dao.impl;

import static org.springframework.util.SerializationUtils.deserialize;
import static org.springframework.util.SerializationUtils.serialize;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gluck.dao.RedisDao;
import com.gluck.model.GameState;

import redis.clients.jedis.Jedis;

@Repository
public class RedisDaoImpl implements RedisDao {

    private @Autowired Jedis jedis;

    @Override
    public GameState findById(Long id) {
        byte[] data = jedis.get(serialize(generateId(id)));
        return (GameState) deserialize(data);
    }

    @Override
    public List<GameState> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long create(GameState entity) {
        byte[] key = serialize(generateId(entity.getGameId()));
        byte[] data = serialize(entity);
        jedis.set(key, data);
        return entity.getGameId();
    }

    @Override
    public void update(GameState entity) {
        create(entity);
    }

    private String generateId(Serializable id) {
        return "Game::" + id.toString();
    }

}
