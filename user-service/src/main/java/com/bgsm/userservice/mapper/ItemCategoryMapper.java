package com.bgsm.userservice.mapper;

import com.bgsm.userservice.dto.ItemCategoryDto;
import com.bgsm.userservice.model.ItemCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemCategoryMapper {

    public ItemCategory mapToItemCategory(ItemCategoryDto itemCategoryDto) {
        return ItemCategory.builder()
                .name(itemCategoryDto.getName())
                .build();
    }

    public ItemCategoryDto mapToItemCategoryDto(ItemCategory itemCategory) {
        return ItemCategoryDto.builder()
                .name(itemCategory.getName())
                .build();
    }
}
