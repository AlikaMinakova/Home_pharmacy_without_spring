package org.example.dao;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.example.dto.DiseaseResponse;
import org.example.dto.MedicationResponse;
import org.example.dto.PharmacyOverviewResponse;
import org.example.dto.PharmacyResponse;
import org.example.entity.Medication;

import java.sql.*;
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

    public void save(MedicationResponse request) {
        String sqlMedication = "INSERT INTO medication (name, description, image) VALUES (?, ?, ?)";
        String sqlPharmacy = "INSERT INTO pharmacy (medication_id, quantity, expiration_date, purchase_date) VALUES (?, ?, ?, ?)";
        String sqlDiseaseLink = "INSERT INTO medication_disease (medication_id, disease_id) VALUES (?, ?)";

        try (Connection conn = pool.getConnection()) {
            conn.setAutoCommit(false);

            Long medicationId = null;

            // 1. Вставляем лекарство
            try (PreparedStatement ps = conn.prepareStatement(sqlMedication, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, request.getName());
                ps.setString(2, request.getDescription());
                ps.setString(3, request.getImage());

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        medicationId = rs.getLong(1);
                    }
                }
            }

            if (medicationId == null) {
                throw new SQLException("Не удалось получить id для medication");
            }

            // 2. Вставляем в аптечку (pharmacy)
            try (PreparedStatement ps = conn.prepareStatement(sqlPharmacy, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, medicationId);
                ps.setInt(2, request.getQuantity());
                ps.setObject(3, request.getExpirationDate()); // LocalDate → java.sql.Date
                ps.setObject(4, request.getPurchaseDate());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        request.setPharmacyId(rs.getLong(1)); // вернём pharmacyId в DTO
                    }
                }
            }

            // 3. Сохраняем связи medication ↔ disease
            if (request.getDiseaseIds() != null && !request.getDiseaseIds().isEmpty()) {
                try (PreparedStatement ps = conn.prepareStatement(sqlDiseaseLink)) {
                    for (Long diseaseId : request.getDiseaseIds()) {
                        ps.setLong(1, medicationId);
                        ps.setLong(2, diseaseId);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении лекарства", e);
        }
    }

    public void update(MedicationResponse request) {
        String sqlUpdateMedication;
        if (request.getImage() != null) {
            sqlUpdateMedication = "UPDATE medication SET name = ?, description = ?, image = ? WHERE id = ?";
        } else {
            sqlUpdateMedication = "UPDATE medication SET name = ?, description = ? WHERE id = ?";
        }
        String sqlUpdatePharmacy = "UPDATE pharmacy SET quantity = ?, expiration_date = ?, purchase_date = ? WHERE id = ?";
        String sqlDeleteDiseaseLinks = "DELETE FROM medication_disease WHERE medication_id = ?";
        String sqlInsertDiseaseLink = "INSERT INTO medication_disease (medication_id, disease_id) VALUES (?, ?)";

        try (Connection conn = pool.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // 1. Обновляем medication
                try (PreparedStatement ps = conn.prepareStatement(sqlUpdateMedication)) {
                    ps.setString(1, request.getName());
                    ps.setString(2, request.getDescription());
                    if (request.getImage() != null) {
                        ps.setString(3, request.getImage());
                        ps.setLong(4, request.getId());
                    }
                    else {
                        ps.setLong(3, request.getId());
                    }
                    ps.executeUpdate();
                }

                // 2. Обновляем pharmacy
                try (PreparedStatement ps = conn.prepareStatement(sqlUpdatePharmacy)) {
                    ps.setInt(1, request.getQuantity());
                    ps.setObject(2, request.getExpirationDate());
                    ps.setObject(3, request.getPurchaseDate());
                    ps.setLong(4, request.getPharmacyId());
                    ps.executeUpdate();
                }

                // 3. Перезаписываем связи болезнь ↔ лекарство
                try (PreparedStatement psDel = conn.prepareStatement(sqlDeleteDiseaseLinks)) {
                    psDel.setLong(1, request.getId());
                    psDel.executeUpdate();
                }

                if (request.getDiseaseIds() != null && !request.getDiseaseIds().isEmpty()) {
                    try (PreparedStatement ps = conn.prepareStatement(sqlInsertDiseaseLink)) {
                        for (Long diseaseId : request.getDiseaseIds()) {
                            ps.setLong(1, request.getId());
                            ps.setLong(2, diseaseId);
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                }

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException("Ошибка при обновлении лекарства", e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении лекарства", e);
        }
    }


    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM medication WHERE id = ?";

        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

}

