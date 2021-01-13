package com.bgsm.userservice.mapper;

import com.bgsm.userservice.dto.ItemDto;
import com.bgsm.userservice.model.Item;
import com.bgsm.userservice.service.AppUserService;
import com.bgsm.userservice.service.ItemCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    private final ItemCategoryService itemCategoryService;
    private final AppUserService appUserService;

    public Item mapToItem(ItemDto itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .minPlayers(itemDto.getMinPlayers())
                .maxPlayers(itemDto.getMaxPlayers())
                .category(itemCategoryService.findByName(itemDto.getCategoryName()))
                .user(appUserService.findByName(itemDto.getUserName()))
                .build();
    }

    public ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .minPlayers(item.getMinPlayers())
                .maxPlayers(item.getMaxPlayers())
                .categoryName(item.getCategory().getName())
                .userName(item.getUser().getUsername())
                .build();
    }

    public List<ItemDto> mapToItemDtoList(List<Item> itemList) {
        return itemList.stream()
                .map(this::mapToItemDto)
                .collect(Collectors.toList());
    }
}
