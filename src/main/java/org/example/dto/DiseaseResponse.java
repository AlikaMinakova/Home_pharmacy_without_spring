package org.example.dto;

import lombok.Data;

import java.util.List;

@Data
public class DiseaseResponse {
    private Long id;
    private String name;
    private String description;
    private List<SymptomResponse> symptomsResponses;
    private List<Long> symptomIds;
}
