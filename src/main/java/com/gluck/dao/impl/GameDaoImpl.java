package com.gluck.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gluck.dao.GameDao;
import com.gluck.model.Game;

@Repository
public class GameDaoImpl implements GameDao {

    private @Autowired SessionFactory sf;

    @Override
    public Game findById(Long id) {
        return (Game) sf.getCurrentSession().get(Game.class, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Game> findAll() {
        return sf.getCurrentSession().createQuery("from Game").list();
    }

    @Override
    public Long create(Game entity) {
        return (Long) sf.getCurrentSession().save(entity);
    }

    @Override
    public void update(Game entity) {
        sf.getCurrentSession().update(entity);
    }
}
