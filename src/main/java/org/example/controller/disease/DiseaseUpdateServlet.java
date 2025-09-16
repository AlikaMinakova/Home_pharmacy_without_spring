package org.example.controller.disease;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.DiseaseRequest;
import org.example.entity.Symptom;
import org.example.service.DiseaseService;
import org.example.service.SymptomService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DiseaseUpdateServlet extends HttpServlet {

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo(); // например /5/edit

        if (pathInfo != null && pathInfo.matches("/\\d+/edit")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            DiseaseRequest disease = null;
            try {
                disease = diseaseService.findById(id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            List<Symptom> symptoms = null;
            try {
                symptoms = symptomService.findAll();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            req.setAttribute("disease", disease);
            req.setAttribute("symptoms", symptoms);

            req.getRequestDispatcher("/disease/update.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, IOException {
        req.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            // обновление болезни
            Long id = Long.parseLong(pathInfo.substring(1));

            String name = req.getParameter("name");
            String description = req.getParameter("description");

            String[] symptomIds = req.getParameterValues("symptomIds");
            List<Long> symptomList = symptomIds != null
                    ? Arrays.stream(symptomIds).map(Long::parseLong).collect(Collectors.toList())
                    : List.of();

            DiseaseRequest diseaseRequest = new DiseaseRequest();
            diseaseRequest.setName(name);
            diseaseRequest.setDescription(description);
            diseaseRequest.setSymptomIds(symptomList);

            try {
                diseaseService.update(id, diseaseRequest);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            resp.sendRedirect(req.getContextPath() + "/diseases");
        } else if (pathInfo != null && pathInfo.matches("/\\d+/delete")) {
            // удаление болезни
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            try {
                diseaseService.delete(id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            resp.sendRedirect(req.getContextPath() + "/diseases");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}