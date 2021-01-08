package com.bgsm.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private String name;
    private String description;
    private int minPlayers;
    private int maxPlayers;
    private String categoryName;
    private String userName;
}
