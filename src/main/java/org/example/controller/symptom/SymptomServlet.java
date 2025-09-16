package org.example.controller.symptom;


import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.entity.Symptom;
import org.example.service.SymptomService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class SymptomServlet extends HttpServlet {

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // Получаем параметры страницы и размера
            int page = 0;
            int size = 5;

            String pageParam = req.getParameter("page");
            String sizeParam = req.getParameter("size");

            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
            if (sizeParam != null) {
                size = Integer.parseInt(sizeParam);
            }

            List<Symptom> symptoms = symptomService.findAll();

            // Если хочешь постранично:
            int fromIndex = Math.min(page * size, symptoms.size());
            int toIndex = Math.min(fromIndex + size, symptoms.size());
            List<Symptom> symptomsPage = symptoms.subList(fromIndex, toIndex);

            // Ставим атрибуты для JSP
            req.setAttribute("symptomsPage", symptomsPage);
            req.setAttribute("currentPage", page);
            req.setAttribute("pageSize", size);
            req.setAttribute("totalPages", (int) Math.ceil((double) symptoms.size() / size));

            // Передаём управление JSP
            req.getRequestDispatcher("/symptom/list.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
