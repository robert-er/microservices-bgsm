package com.bgsm.userservice.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;
    private String name;
    private String description;
    private double minPlayers;
    private double maxPlayers;
    private String categoryName;
    private String userName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDto itemDto = (ItemDto) o;
        return Double.compare(itemDto.minPlayers, minPlayers) == 0 &&
                Double.compare(itemDto.maxPlayers, maxPlayers) == 0 &&
                Objects.equals(name, itemDto.name) && Objects.equals(description, itemDto.description) &&
                Objects.equals(categoryName, itemDto.categoryName) &&
                Objects.equals(userName, itemDto.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, minPlayers, maxPlayers, categoryName, userName);
    }
}
