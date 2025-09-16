package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PharmacyOverviewResponse {

    private List<PharmacyResponse> all;
    private List<PharmacyResponse> recentlyBought;
    private List<PharmacyResponse> expiringSoon;
}
