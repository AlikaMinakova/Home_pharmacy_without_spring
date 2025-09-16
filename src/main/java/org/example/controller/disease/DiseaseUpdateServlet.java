package org.example.controller.disease;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.DiseaseResponse;
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
        this.diseaseService = (DiseaseService) config.getServletContext().getAttribute("diseaseService");
        this.symptomService = (SymptomService) config.getServletContext().getAttribute("symptomService");
        if (diseaseService == null || symptomService == null) {
            throw new ServletException("Services not initialized");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            try {
                DiseaseResponse disease = diseaseService.findById(id);
                List<Symptom> symptoms = symptomService.findAll();

                req.setAttribute("disease", disease);
                req.setAttribute("symptoms", symptoms);

                req.getRequestDispatcher("/disease/update.jsp").forward(req, resp);
            } catch (SQLException e) {
                throw new ServletException("Ошибка загрузки болезни", e);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);

            String name = req.getParameter("name");
            String description = req.getParameter("description");

            String[] symptomIds = req.getParameterValues("symptomIds");
            List<Long> symptomList = symptomIds != null
                    ? Arrays.stream(symptomIds).map(Long::parseLong).collect(Collectors.toList())
                    : List.of();

            DiseaseResponse diseaseRequest = new DiseaseResponse();
            diseaseRequest.setName(name);
            diseaseRequest.setDescription(description);
            diseaseRequest.setSymptomIds(symptomList);

            try {
                diseaseService.update(id, diseaseRequest);
            } catch (SQLException e) {
                throw new ServletException("Ошибка при обновлении болезни", e);
            }

            resp.sendRedirect(req.getContextPath() + "/diseases");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}