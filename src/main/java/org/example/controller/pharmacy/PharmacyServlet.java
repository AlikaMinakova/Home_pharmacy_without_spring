package org.example.controller.pharmacy;


import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.dto.PharmacyOverviewResponse;
import org.example.service.PharmacyService;

import java.io.IOException;
import java.sql.SQLException;

@RequiredArgsConstructor
public class PharmacyServlet extends HttpServlet {

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
        int page = parseIntOrDefault(req.getParameter("page"), 0);
        int size = parseIntOrDefault(req.getParameter("size"), 8);
        String keyword = req.getParameter("keyword");
        String sort = req.getParameter("sort") != null ? req.getParameter("sort") : "expiration_date";

        try {
            PharmacyOverviewResponse overview = pharmacyService.getPharmacyOverview(page, size, sort, keyword);

            req.setAttribute("pharmacyPage", overview);
            req.setAttribute("keyword", keyword);
            req.setAttribute("sort", sort);

            req.getRequestDispatcher("/pharmacy/list.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Ошибка загрузки списка аптечки", e);
        }
    }

    private int parseIntOrDefault(String param, int def) {
        try {
            return param != null ? Integer.parseInt(param) : def;
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
