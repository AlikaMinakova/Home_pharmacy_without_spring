package org.example.controller.symptom;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.SymptomResponse;
import org.example.entity.Symptom;
import org.example.service.SymptomService;

import java.io.IOException;
import java.sql.SQLException;

public class SymptomUpdateServlet extends HttpServlet {
    private SymptomService symptomService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Object symptom = config.getServletContext().getAttribute("symptomService");
        if (!(symptom instanceof SymptomService)) {
            throw new ServletException("SymptomService not initialized");
        }
        this.symptomService = (SymptomService) symptom;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo(); // например /5/edit

        if (pathInfo != null && pathInfo.matches("/\\d+/edit")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            Symptom symptom = null;
            try {
                symptom = symptomService.findById(id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            req.setAttribute("symptom", symptom);
            req.getRequestDispatcher("/symptom/update.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            // обновление
            Long id = Long.parseLong(pathInfo.substring(1));

            String name = req.getParameter("name");
            SymptomResponse symptomResponse = new SymptomResponse();
            symptomResponse.setName(name);

            symptomService.updateSymptom(id, symptomResponse);
            resp.sendRedirect(req.getContextPath() + "/symptoms");
        } else if (pathInfo != null && pathInfo.matches("/\\d+/delete")) {
            // удаление
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            symptomService.deleteSymptom(id);
            resp.sendRedirect(req.getContextPath() + "/symptoms");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}