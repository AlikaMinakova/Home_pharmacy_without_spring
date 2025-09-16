package org.example;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.example.config.DataSourceConfig;
import org.example.dao.DiseaseDao;
import org.example.dao.PharmacyDao;
import org.example.dao.SymptomDao;
import org.example.service.DiseaseService;
import org.example.service.PharmacyService;
import org.example.service.SymptomService;


public class AppContextListener implements ServletContextListener {

    private DataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        dataSource = DataSourceConfig.createDataSource();

        DiseaseDao diseaseDao = new DiseaseDao(dataSource);
        DiseaseService diseaseService = new DiseaseService(diseaseDao);
        SymptomDao symptomDao = new SymptomDao(dataSource);
        SymptomService symptomService = new SymptomService(symptomDao);
        PharmacyDao pharmacyDao = new PharmacyDao(dataSource);
        PharmacyService pharmacyService = new PharmacyService(pharmacyDao, diseaseDao);

        ServletContext context = sce.getServletContext();
        context.setAttribute("diseaseService", diseaseService);
        context.setAttribute("symptomService", symptomService);
        context.setAttribute("pharmacyService", pharmacyService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}

