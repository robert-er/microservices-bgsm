package com.bgsm.userservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private Long userId;
    private List<OrderDto> orders;
}
