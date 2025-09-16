package org.example.controller.pharmacy;


import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import lombok.RequiredArgsConstructor;
import org.example.service.DiseaseService;
import org.example.service.PharmacyService;

@RequiredArgsConstructor
public class PharmacyServlet extends HttpServlet {

    private PharmacyService pharmacyService;
    private DiseaseService diseaseService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Object pharmacy = config.getServletContext().getAttribute("pharmacyService");
        Object disease = config.getServletContext().getAttribute("diseaseService");
        if (!(pharmacy instanceof PharmacyService)) {
            throw new ServletException("PharmacyService not initialized");
        }
        if (!(disease instanceof DiseaseService)) {
            throw new ServletException("DiseaseService not initialized");
        }
        this.pharmacyService = (PharmacyService) pharmacy;
        this.diseaseService = (DiseaseService) disease;
    }


}
