package org.example.controller.disease;


import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.DiseaseService;

import java.io.IOException;
import java.sql.SQLException;

public class DiseaseDeleteServlet extends HttpServlet {

    private DiseaseService diseaseService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.diseaseService = (DiseaseService) config.getServletContext().getAttribute("diseaseService");
        if (diseaseService == null) {
            throw new ServletException("DiseaseService not initialized");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo(); // /delete/5

        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            try {
                diseaseService.delete(id);
            } catch (SQLException e) {
                throw new ServletException("Ошибка при удалении болезни", e);
            }
            resp.sendRedirect(req.getContextPath() + "/diseases");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
