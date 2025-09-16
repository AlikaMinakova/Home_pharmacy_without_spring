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
        this.symptomService = (SymptomService) config.getServletContext().getAttribute("symptomService");
        if (symptomService == null) {
            throw new ServletException("SymptomService not initialized");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            try {
                Symptom symptom = symptomService.findById(id);
                req.setAttribute("symptom", symptom);
                req.getRequestDispatcher("/symptom/update.jsp").forward(req, resp);
            } catch (SQLException e) {
                throw new ServletException("Ошибка загрузки симптома", e);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            String name = req.getParameter("name");

            SymptomResponse symptomResponse = new SymptomResponse();
            symptomResponse.setName(name);

            symptomService.updateSymptom(id, symptomResponse);

            resp.sendRedirect(req.getContextPath() + "/symptoms");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
