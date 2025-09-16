package org.example.controller.symptom;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.SymptomResponse;
import org.example.service.SymptomService;

import java.io.IOException;

public class SymptomCreateServlet extends HttpServlet {

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
        req.getRequestDispatcher("/symptom/create.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");

        SymptomResponse symptomResponse = new SymptomResponse();
        symptomResponse.setName(name);

        symptomService.save(symptomResponse);

        resp.sendRedirect(req.getContextPath() + "/symptoms");
    }
}
