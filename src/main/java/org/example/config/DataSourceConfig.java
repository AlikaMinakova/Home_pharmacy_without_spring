package org.example.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.io.InputStream;
import java.util.Properties;

public class DataSourceConfig {

    public static DataSource createDataSource() {
        PoolProperties p = new PoolProperties();

        try {
            Properties props = new Properties();
            // используем ClassLoader для статического метода
            try (InputStream is = DataSourceConfig.class.getClassLoader()
                    .getResourceAsStream("application.properties")) {
                if (is == null) throw new RuntimeException("application.properties not found in classpath");
                props.load(is);
            }

            int size = Integer.parseInt(props.getProperty("db.pool.size", "5"));

            p.setUrl(props.getProperty("db.url"));
            p.setDriverClassName(props.getProperty("db.driver"));
            p.setUsername(props.getProperty("db.user"));
            p.setPassword(props.getProperty("db.password"));
            p.setJmxEnabled(true);
            p.setTestOnBorrow(true);
            p.setValidationQuery("SELECT 1");
            p.setTestWhileIdle(true);
            p.setTimeBetweenEvictionRunsMillis(30000);
            p.setMinEvictableIdleTimeMillis(30000);
            p.setMaxActive(size);
            p.setInitialSize(size);
            p.setMaxIdle(size);
            p.setMaxWait(10000);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load database properties", e);
        }

        return new DataSource(p);
    }
}
