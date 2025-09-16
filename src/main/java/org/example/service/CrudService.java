package org.example.service;

import java.sql.SQLException;
import java.util.List;

public interface CrudService<T, ID> {
    T findById(ID id);
    List<T> findAll() throws SQLException;
    T save(T entity);
    void delete(ID id);
    long count();
}
