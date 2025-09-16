package org.example.dao;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.example.dto.DiseaseResponse;
import org.example.dto.MedicationResponse;
import org.example.dto.PharmacyOverviewResponse;
import org.example.dto.PharmacyResponse;
import org.example.entity.Medication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PharmacyDao {
    private final DataSource pool;

    public PharmacyDao(DataSource pool) {
        this.pool = pool;
    }

    public Medication findMedicationById(Long id) throws SQLException {
        String sqlMedication = "SELECT * FROM medication WHERE id = ?";
        try (Connection connMed = pool.getConnection();
             PreparedStatement psMedication = connMed.prepareStatement(sqlMedication)) {
            psMedication.setLong(1, id);

            Medication medication = null;

            try (ResultSet rsMedication = psMedication.executeQuery()) {
                if (rsMedication.next()) {
                    medication = new Medication();
                    medication.setId(rsMedication.getLong("id"));
                    medication.setDescription(rsMedication.getString("description"));
                    medication.setImage(rsMedication.getString("image"));
                    medication.setName(rsMedication.getString("name"));
                }
            }
            return medication;
        }
    }

    public List<PharmacyResponse> findAll(int page, int size, String sort) throws SQLException {
        String sql = "SELECT * FROM pharmacy ORDER BY " + sort + " LIMIT ? OFFSET ?";
        List<PharmacyResponse> pharmacyResponses = new ArrayList<>();
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);

            PharmacyResponse pharmacyResponse = null;

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pharmacyResponse = new PharmacyResponse();
                    pharmacyResponse.setId(rs.getLong("id"));
                    pharmacyResponse.setQuantity(rs.getInt("quantity"));
                    pharmacyResponse.setExpirationDate(LocalDate.parse(rs.getString("expiration_date")));
                    pharmacyResponse.setPurchaseDate(LocalDate.parse(rs.getString("purchase_date")));
                    pharmacyResponse.setMedication(findMedicationById(rs.getLong("medication_id")));
                    pharmacyResponses.add(pharmacyResponse);
                }
            }
        }
        return pharmacyResponses;
    }

    public PharmacyOverviewResponse findOverview(int page, int size, String sort, String keyword) throws SQLException {
        List<PharmacyResponse> all = findAll(page, size, sort);
        List<PharmacyResponse> recentlyBought = findAll(page, size, "purchase_date");
        List<PharmacyResponse> expiringSoon = findAll(page, size, "expiration_date");

        return new PharmacyOverviewResponse(all, recentlyBought, expiringSoon);
    }

    public MedicationResponse findMedicationDetailByPharmacyId(Long id) throws SQLException {
        String sql = "SELECT * FROM pharmacy WHERE id = ?";
        try (Connection conn = pool.getConnection()) {
            MedicationResponse medicationResponse = null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, id);
                System.out.println(id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        medicationResponse = new MedicationResponse();
                        medicationResponse.setPharmacyId(rs.getLong("id"));
                        medicationResponse.setQuantity(rs.getInt("quantity"));
                        medicationResponse.setExpirationDate(LocalDate.parse(rs.getString("expiration_date")));
                        medicationResponse.setPurchaseDate(LocalDate.parse(rs.getString("purchase_date")));
                        Medication med = findMedicationById(rs.getLong("medication_id"));
                        System.out.println(med);
                        medicationResponse.setId(med.getId());
                        medicationResponse.setName(med.getName());
                        medicationResponse.setDescription(med.getDescription());
                        medicationResponse.setImage(med.getImage());
                        medicationResponse.setDiseaseResponses(null);
                    }
                }
            }
            return medicationResponse;
        }
    }
}

