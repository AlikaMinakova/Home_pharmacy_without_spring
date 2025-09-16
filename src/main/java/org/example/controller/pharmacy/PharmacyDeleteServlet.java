package org.example.controller.pharmacy;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.PharmacyService;

import java.io.IOException;
import java.sql.SQLException;

public class PharmacyDeleteServlet extends HttpServlet {

    private PharmacyService pharmacyService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Object pharmacy = config.getServletContext().getAttribute("pharmacyService");
        if (!(pharmacy instanceof PharmacyService)) {
            throw new ServletException("PharmacyService not initialized");
        }
        this.pharmacyService = (PharmacyService) pharmacy;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.matches("/\\d+")) {


            Long id = Long.parseLong(pathInfo.substring(1));

            try {
                pharmacyService.delete(id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            resp.sendRedirect(req.getContextPath() + "/pharmacies");
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный id");
        }
    }
}
