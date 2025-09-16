package org.example.dto;

import org.example.entity.Medication;

import java.time.LocalDate;
import java.util.List;

public class PharmacyResponse {
    private Long id;
    private Long medicationId;
    private Medication medication;
    private String medicationDescription;
    private List<String> diseaseNames;
    private Integer quantity;
    private LocalDate expirationDate;
    private LocalDate purchaseDate;
    private String image;
}
