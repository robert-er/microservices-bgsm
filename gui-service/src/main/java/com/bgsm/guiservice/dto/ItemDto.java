package com.bgsm.guiservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private String name;
    private String description;
    private double minPlayers;
    private double maxPlayers;
    private String categoryName;
    private String userName;
}
