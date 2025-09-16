package org.example.dao;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.example.entity.Disease;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PharmacyDao {
    private final DataSource pool;

    public PharmacyDao(DataSource pool) {
        this.pool = pool;
    }
}
