package org.example.controller.symptom;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.SymptomService;

import java.io.IOException;

public class SymptomDeleteServlet extends HttpServlet {

    private SymptomService symptomService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.symptomService = (SymptomService) config.getServletContext().getAttribute("symptomService");
        if (symptomService == null) {
            throw new ServletException("SymptomService not initialized");
        }
    }

    // Удаление симптома
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            Long id = Long.parseLong(pathInfo.split("/")[1]);

            symptomService.deleteSymptom(id);

            resp.sendRedirect(req.getContextPath() + "/symptoms");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
