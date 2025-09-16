package org.example.service;

import org.example.dao.SymptomDao;
import org.example.dto.SymptomResponse;
import org.example.entity.Symptom;

import java.sql.SQLException;
import java.util.List;

public class SymptomService {

    private final SymptomDao symptomDao;

    public SymptomService(SymptomDao dao) {
        this.symptomDao = dao;
    }

    public Symptom findById(Long id) throws SQLException {
        return symptomDao.findById(id);
    }

    public List<Symptom> findAll() throws SQLException {
        return symptomDao.findAll();
    }

    public void save(SymptomResponse request) {
        symptomDao.save(request);
    }

    public Symptom getSymptom(Long id) {
        return symptomDao.findById(id);
    }

    public void updateSymptom(Long id, SymptomResponse request) {
        symptomDao.update(id, request);
    }

    public void deleteSymptom(Long id) {
        symptomDao.delete(id);
    }
}
