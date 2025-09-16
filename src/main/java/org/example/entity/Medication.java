package org.example.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medication {
    private Long id;
    private String name;
    private String description;
    private String image;
}
