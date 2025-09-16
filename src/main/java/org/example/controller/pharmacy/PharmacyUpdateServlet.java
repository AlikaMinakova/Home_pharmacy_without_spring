package org.example.controller.pharmacy;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.example.dto.MedicationResponse;
import org.example.entity.Disease;
import org.example.service.DiseaseService;
import org.example.service.PharmacyService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@MultipartConfig
public class PharmacyUpdateServlet extends HttpServlet {
    private PharmacyService pharmacyService;
    private DiseaseService diseaseService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Object pharmacy = config.getServletContext().getAttribute("pharmacyService");
        Object disease = config.getServletContext().getAttribute("diseaseService");
        if (!(pharmacy instanceof PharmacyService)) {
            throw new ServletException("PharmacyService not initialized");
        }
        if (!(disease instanceof DiseaseService)) {
            throw new ServletException("DiseaseService not initialized");
        }
        this.diseaseService = (DiseaseService) disease;
        this.pharmacyService = (PharmacyService) pharmacy;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            Long id = Long.parseLong(pathInfo.substring(1));
            MedicationResponse medicationResponse = null;
            try {
                medicationResponse = pharmacyService.findMedicationDetailByPharmacyId(id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            List<Disease> diseases = null;
            try {
                diseases = diseaseService.findAll();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            req.setAttribute("medication", medicationResponse);
            req.setAttribute("diseases", diseases);

            req.getRequestDispatcher("/pharmacy/update.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            Long id = Long.parseLong(pathInfo.substring(1));

            Long medicationId = Long.parseLong(req.getParameter("id"));
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            Part filePart = req.getPart("image");
            String fileName = filePart.getSubmittedFileName();
            if (fileName != null && !fileName.isEmpty()) {
                String uploadsDir = getServletContext().getRealPath("/uploads");
                File uploadsFolder = new File(uploadsDir);

                File file = new File(uploadsFolder, fileName);

                try (InputStream input = filePart.getInputStream();
                     FileOutputStream output = new FileOutputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = input.read(buffer)) > 0) {
                        output.write(buffer, 0, length);
                    }
                }
                fileName = "/" + fileName;
            }
            Integer quantity = Integer.valueOf(req.getParameter("quantity"));
            LocalDate purchaseDate = LocalDate.parse(req.getParameter("purchaseDate"));
            LocalDate expirationDate = LocalDate.parse(req.getParameter("expirationDate"));
            String[] selectedDiseaseIds = req.getParameterValues("diseaseIds");

            List<Long> diseaseIds = new ArrayList<>();
            if (selectedDiseaseIds != null) {
                for (String diseaseId : selectedDiseaseIds) {
                    diseaseIds.add(Long.parseLong(diseaseId));
                }
            }

            MedicationResponse medicationResponse = new MedicationResponse();
            medicationResponse.setId(medicationId);
            medicationResponse.setPharmacyId(id);
            medicationResponse.setName(name);
            medicationResponse.setDescription(description);
            if (fileName != null && !fileName.isEmpty()) {
                medicationResponse.setImage(fileName);
            }
            medicationResponse.setQuantity(quantity);
            medicationResponse.setExpirationDate(expirationDate);
            medicationResponse.setPurchaseDate(purchaseDate);
            medicationResponse.setDiseaseIds(diseaseIds);

            pharmacyService.update(medicationResponse);

            resp.sendRedirect(req.getContextPath() + "/pharmacies");
        }
    }
}
