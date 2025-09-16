

package org.example.service;

import org.example.dao.DiseaseDao;
import org.example.dao.PharmacyDao;
import org.example.dto.DiseaseResponse;
import org.example.dto.MedicationResponse;
import org.example.dto.PharmacyOverviewResponse;
import org.example.dto.PharmacyResponse;
import org.example.entity.Disease;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PharmacyService {

    private final PharmacyDao pharmacyDao;
    private final DiseaseDao diseaseDao;

    public PharmacyService(PharmacyDao dao, DiseaseDao diseaseDao) {
        this.pharmacyDao = dao;
        this.diseaseDao = diseaseDao;
    }

    public PharmacyOverviewResponse getPharmacyOverview(String sort, String keyword) throws SQLException {
        return pharmacyDao.findOverview(sort, keyword);
    }

    public MedicationResponse findMedicationDetailByPharmacyId(Long id) throws SQLException {
        MedicationResponse medicationResponse = pharmacyDao.findMedicationDetailByPharmacyId(id);
        List<Long> ids = diseaseDao.findDiseasesIdByMedicationId(medicationResponse.getId());
        List<DiseaseResponse> diseaseResponses = new ArrayList<>();
        for (Long idDisease : ids){
            diseaseResponses.add(diseaseDao.findById(idDisease));
        }
        medicationResponse.setDiseaseResponses(diseaseResponses);
        medicationResponse.setDiseaseIds(ids);
        return medicationResponse;
    }


    public void save(MedicationResponse entity) {
        pharmacyDao.save(entity);
    }

    public void update(MedicationResponse entity) {
        pharmacyDao.update(entity);
    }

    public void delete(Long id) throws SQLException {
        pharmacyDao.delete(id);
    }
}
