package org.example.controller.symptom;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.DiseaseService;
import org.example.dto.SymptomRequest;
import org.example.service.SymptomService;

import java.io.IOException;

public class SymptomAddServlet extends HttpServlet {

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

    // показать форму
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/symptom/create.jsp").forward(req, resp);
    }

    // сохранить
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");

        SymptomRequest symptomRequest = new SymptomRequest();
        symptomRequest.setName(name);

        symptomService.save(symptomRequest);

        resp.sendRedirect(req.getContextPath() + "/symptoms");
    }
}
