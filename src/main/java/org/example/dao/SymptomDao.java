package org.example.dao;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.example.dto.SymptomResponse;
import org.example.entity.Symptom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SymptomDao {
    private final DataSource pool;

    public SymptomDao(DataSource pool) {
        this.pool = pool;
    }

    public List<Symptom> findAll() throws SQLException {
        String sql = "SELECT id, name FROM symptom ORDER BY id";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Symptom> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Symptom(rs.getLong("id"),
                        rs.getString("name")));
            }
            return list;
        }
    }

    public void save(SymptomResponse request) {
        String sql = "INSERT INTO symptom (name) VALUES (?)";

        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, request.getName());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении симптома", e);
        }
    }

    public Symptom findById(Long id) {
        String sql = "SELECT id, name FROM symptom WHERE id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Symptom s = new Symptom();
                    s.setId(rs.getLong("id"));
                    s.setName(rs.getString("name"));
                    return s;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске симптома", e);
        }
        return null;
    }

    public void update(Long id, SymptomResponse request) {
        String sql = "UPDATE symptom SET name = ? WHERE id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, request.getName());
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении симптома", e);
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM symptom WHERE id = ?";
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении симптома", e);
        }
    }
}
