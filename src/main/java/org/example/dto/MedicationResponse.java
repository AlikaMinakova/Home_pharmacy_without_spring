package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicationResponse {
    private Long id;
    private Long pharmacyId;
    private String name;
    private String description;
    private String image;
    private Integer quantity;
    private LocalDate expirationDate;
    private LocalDate purchaseDate;
    private List<DiseaseResponse> diseaseResponses;
}
