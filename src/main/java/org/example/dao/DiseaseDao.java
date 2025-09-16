package org.example.dao;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.example.dto.DiseaseResponse;
import org.example.dto.SymptomResponse;
import org.example.entity.Disease;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiseaseDao {
    private final DataSource pool;

    public DiseaseDao(DataSource pool) {
        this.pool = pool;
    }

    public List<Long> findDiseasesIdByMedicationId(Long id) throws SQLException {
        String sql = "SELECT disease_id FROM medication_disease WHERE medication_id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                List<Long> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(rs.getLong("disease_id"));
                }
                return list;
            }
        }
    }

    public List<Disease> findAll() throws SQLException {
        String sql = "SELECT id, name, description FROM disease ORDER BY id";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Disease> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Disease(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description")));
            }
            return list;
        }
    }

    public void save(DiseaseResponse request) {
        String sqlDisease = "INSERT INTO disease (name, description) VALUES (?, ?)";
        String sqlSymptomLink = "INSERT INTO disease_symptom (disease_id, symptom_id) VALUES (?, ?)";

        try (Connection conn = pool.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sqlDisease, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, request.getName());
                ps.setString(2, request.getDescription());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        long diseaseId = rs.getLong(1);

                        if (request.getSymptomIds() != null) {
                            try (PreparedStatement psLink = conn.prepareStatement(sqlSymptomLink)) {
                                for (Long symptomId : request.getSymptomIds()) {
                                    psLink.setLong(1, diseaseId);
                                    psLink.setLong(2, symptomId);
                                    psLink.addBatch();
                                }
                                psLink.executeBatch();
                            }
                        }
                    }
                }
            }
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении болезни", e);
        }
    }

    public DiseaseResponse findById(Long id) throws SQLException {
        String sql = "SELECT id, name, description FROM disease WHERE id = ?";
        String sqlSymptoms = "SELECT s.id, s.name FROM disease_symptom ds JOIN symptom s ON ds.symptom_id = s.id  WHERE ds.disease_id = ?";

        try (Connection conn = pool.getConnection()) {
            conn.setAutoCommit(false);

            DiseaseResponse d = null;
            // основная информация о болезни
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        d = new DiseaseResponse();
                        d.setId(rs.getLong("id"));
                        d.setName(rs.getString("name"));
                        d.setDescription(rs.getString("description"));
                    }
                }
            }
            if (d == null) {
                return null;
            }

            // симптомы
            try (PreparedStatement ps = conn.prepareStatement(sqlSymptoms)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    List<Long> symptomIds = new ArrayList<>();
                    List<SymptomResponse> symptoms = new ArrayList<>();
                    while (rs.next()) {
                        symptoms.add(new SymptomResponse(rs.getLong("id"), rs.getString("name")));
                        symptomIds.add(rs.getLong("id"));
                    }
                    d.setSymptomsResponses(symptoms);
                    d.setSymptomIds(symptomIds);
                }
            }

            conn.commit();
            return d;
        }
    }

    public void update(Long id, DiseaseResponse disease) throws SQLException {
        String sql = "UPDATE disease SET name = ?, description = ? WHERE id = ?";
        try (Connection conn = pool.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, disease.getName());
                ps.setString(2, disease.getDescription());
                ps.setLong(3, id);
                ps.executeUpdate();
            }

            // чистим старые связи
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM disease_symptom WHERE disease_id = ?")) {
                ps.setLong(1, id);
                ps.executeUpdate();
            }

            // добавляем новые связи
            if (disease.getSymptomIds() != null && !disease.getSymptomIds().isEmpty()) {
                String insert = "INSERT INTO disease_symptom (disease_id, symptom_id) VALUES (?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insert)) {
                    for (Long symptomId : disease.getSymptomIds()) {
                        ps.setLong(1, id);
                        ps.setLong(2, symptomId);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении болезни", e);
        }
    }

    public void delete(Long id) throws SQLException {
        try (Connection conn = pool.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM disease_symptom WHERE disease_id = ?")) {
                ps.setLong(1, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM disease WHERE id = ?")) {
                ps.setLong(1, id);
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении болезни", e);
        }
    }
}