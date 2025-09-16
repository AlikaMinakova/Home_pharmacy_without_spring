package org.example.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.DiseaseDao;
import org.example.dao.SymptomDao;
import org.example.dto.SymptomRequest;
import org.example.entity.Disease;
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

    public void save(SymptomRequest request) {
        symptomDao.save(request);
    }

    public Symptom getSymptom(Long id) {
        return symptomDao.findById(id);
    }

    public void updateSymptom(Long id, SymptomRequest request) {
        symptomDao.update(id, request);
    }

    public void deleteSymptom(Long id) {
        symptomDao.delete(id);
    }
}
