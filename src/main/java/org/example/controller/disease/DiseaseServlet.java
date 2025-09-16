package org.example.controller.disease;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.entity.Disease;
import org.example.service.DiseaseService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class DiseaseServlet extends HttpServlet {

    private DiseaseService diseaseService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Object disease = config.getServletContext().getAttribute("diseaseService");
        if (!(disease instanceof DiseaseService)) {
            throw new ServletException("DiseaseService not initialized");
        }
        this.diseaseService = (DiseaseService) disease;
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

            // Получаем список болезней (можно сделать постранично вручную)
            List<Disease> diseases = diseaseService.findAll();

            // Если хочешь постранично:
            int fromIndex = Math.min(page * size, diseases.size());
            int toIndex = Math.min(fromIndex + size, diseases.size());
            List<Disease> diseasesPage = diseases.subList(fromIndex, toIndex);

            // Ставим атрибуты для JSP
            req.setAttribute("diseasesPage", diseasesPage);
            req.setAttribute("currentPage", page);
            req.setAttribute("pageSize", size);
            req.setAttribute("totalPages", (int) Math.ceil((double) diseases.size() / size));

            // Передаём управление JSP
            req.getRequestDispatcher("/disease/list.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
