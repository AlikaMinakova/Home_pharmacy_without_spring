package org.example.entity;


import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pharmacy {
    private Long id;
    private Medication medication;
    private Integer quantity;
    private LocalDate expirationDate;
    private LocalDate purchaseDate;
}
