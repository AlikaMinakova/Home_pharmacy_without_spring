package org.example.controller.pharmacy;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.MedicationResponse;
import org.example.service.PharmacyService;

import java.io.IOException;
import java.sql.SQLException;

public class PharmacyDetailServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Long id = Long.parseLong(pathInfo.substring(1));

        try {
            MedicationResponse medication = pharmacyService.findMedicationDetailByPharmacyId(id);
            if (medication == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            req.setAttribute("medication", medication);
            req.getRequestDispatcher("/pharmacy/detail.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Ошибка загрузки лекарства id=" + id, e);
        }
    }
}
