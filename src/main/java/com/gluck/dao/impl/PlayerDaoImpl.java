package com.gluck.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gluck.dao.PlayerDao;
import com.gluck.model.Player;

@Repository
public class PlayerDaoImpl implements PlayerDao {

    private @Autowired SessionFactory sf;

    @Override
    public Player findById(Long id) {
        return (Player) sf.getCurrentSession().get(Player.class, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Player> findAll() {
        return sf.getCurrentSession().createQuery("from Player").list();
    }

    @Override
    public Long create(Player entity) {
        return (Long) sf.getCurrentSession().save(entity);
    }

    @Override
    public void update(Player entity) {
        sf.getCurrentSession().update(entity);
    }

}
