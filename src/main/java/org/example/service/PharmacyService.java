

package org.example.service;

import org.example.dao.PharmacyDao;
import org.example.dto.PharmacyResponse;
import org.example.entity.Disease;

import java.sql.SQLException;
import java.util.List;

public class PharmacyService implements PharmacyServiceInterface{

    private final PharmacyDao pharmacyDao;

    public PharmacyService(PharmacyDao dao) {
        this.pharmacyDao = dao;
    }

    @Override
    public PharmacyResponse findById(Long aLong) {
        return null;
    }

    @Override
    public List<PharmacyResponse> findAll() {
        return List.of();
    }

    @Override
    public PharmacyResponse save(PharmacyResponse entity) {
        return null;
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public long count() {
        return 0;
    }
}
