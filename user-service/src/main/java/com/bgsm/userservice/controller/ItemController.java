package com.bgsm.userservice.controller;

import com.bgsm.userservice.dto.ItemDto;
import com.bgsm.userservice.mapper.ItemMapper;
import com.bgsm.userservice.model.Item;
import com.bgsm.userservice.service.AppUserService;
import com.bgsm.userservice.service.ItemCategoryService;
import com.bgsm.userservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("item")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final ItemCategoryService itemCategoryService;
    private final AppUserService appUserService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ItemDto getItem(@PathVariable Long id) {
        return itemMapper.mapToItemDto(itemService.findById(id));
    }

    @GetMapping(")/useritems/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<ItemDto> getUserItems(@PathVariable Long userId) {
        return itemMapper.mapToItemDtoList(itemService.findByUserId(userId));
    }

    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<ItemDto> getAll() {
        return itemMapper.mapToItemDtoList(itemService.getAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ItemDto updateItem(@PathVariable Long id, @RequestBody ItemDto itemDto) {
        Item item = itemService.findById(id);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setMinPlayers(itemDto.getMinPlayers());
        item.setMaxPlayers(itemDto.getMaxPlayers());
        item.setCategory(itemCategoryService.findByName(itemDto.getCategoryName()));
        item.setUser(appUserService.findByName(itemDto.getUserName()));
        return itemMapper.mapToItemDto(itemService.save(item));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ItemDto createItem(@RequestBody ItemDto itemDto) {
        return itemMapper.mapToItemDto(itemService.save(itemMapper.mapToItem(itemDto)));
    }
}
