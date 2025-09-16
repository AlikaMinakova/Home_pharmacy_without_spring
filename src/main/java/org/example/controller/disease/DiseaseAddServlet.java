package org.example.controller.disease;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.DiseaseResponse;
import org.example.entity.Symptom;
import org.example.service.DiseaseService;
import org.example.service.SymptomService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class DiseaseAddServlet extends HttpServlet {


    private DiseaseService diseaseService;
    private SymptomService symptomService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Object disease = config.getServletContext().getAttribute("diseaseService");
        Object symptom = config.getServletContext().getAttribute("symptomService");
        if (!(disease instanceof DiseaseService)) {
            throw new ServletException("DiseaseService not initialized");
        }
        if (!(symptom instanceof SymptomService)) {
            throw new ServletException("SymptomService not initialized");
        }
        this.diseaseService = (DiseaseService) disease;
        this.symptomService = (SymptomService) symptom;
    }

    // показать форму
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Symptom> symptoms = null;
        try {
            symptoms = symptomService.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        req.setAttribute("symptoms", symptoms);
        req.getRequestDispatcher("/disease/create.jsp").forward(req, resp);
    }

    // обработать сохранение
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String[] selectedSymptomIds = req.getParameterValues("symptomIds");

        List<Long> symptomIds = new ArrayList<>();
        if (selectedSymptomIds != null) {
            for (String id : selectedSymptomIds) {
                symptomIds.add(Long.parseLong(id));
            }
        }

        DiseaseResponse diseaseResponse = new DiseaseResponse();
        diseaseResponse.setName(name);
        diseaseResponse.setDescription(description);
        diseaseResponse.setSymptomIds(symptomIds);

        diseaseService.save(diseaseResponse);

        resp.sendRedirect(req.getContextPath() + "/diseases");
    }
}

