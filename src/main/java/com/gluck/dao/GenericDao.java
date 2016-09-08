package com.gluck.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T, K extends Serializable> {

    T findById(K id);

    List<T> findAll();

    K create(T entity);

    void update(T entity);

}
