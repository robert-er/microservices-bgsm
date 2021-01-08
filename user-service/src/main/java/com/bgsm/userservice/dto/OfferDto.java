package com.bgsm.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferDto {

    private Long itemId;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String location;
    private BigDecimal price;
}
