package org.example.service;

import org.example.dao.DiseaseDao;
import org.example.dto.DiseaseRequest;
import org.example.entity.Disease;

import java.sql.SQLException;
import java.util.List;


public class DiseaseService {

    private final DiseaseDao diseaseDao;

    public DiseaseService(DiseaseDao dao) {
        this.diseaseDao = dao;
    }


    public List<Disease> findAll() throws SQLException {
        return diseaseDao.findAll();
    }

    public void save(DiseaseRequest entity) {
        diseaseDao.save(entity);
    }

    public DiseaseRequest findById(Long id) throws SQLException {
        return diseaseDao.findById(id);
    }


    public void update(Long id, DiseaseRequest request) throws SQLException {
        diseaseDao.update(id, request);
    }

    public void delete(Long id) throws SQLException {
        diseaseDao.delete(id);
    }
}
