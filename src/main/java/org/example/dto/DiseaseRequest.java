package org.example.dto;

import lombok.Data;

import java.util.List;

@Data
public class DiseaseRequest {
    private Long id;
    private String name;
    private String description;
    private List<Long> symptomIds;
}
