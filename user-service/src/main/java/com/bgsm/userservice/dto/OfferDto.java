package com.bgsm.userservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferDto {

    private Long id;
    private Long itemId;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String location;
    private BigDecimal price;
}
