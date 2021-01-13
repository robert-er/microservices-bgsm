package com.bgsm.userservice.controller;

import com.bgsm.userservice.dto.ItemCategoryDto;
import com.bgsm.userservice.mapper.ItemCategoryMapper;
import com.bgsm.userservice.model.ItemCategory;
import com.bgsm.userservice.service.ItemCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("itemcategory")
public class ItemCategoryController {

    private final ItemCategoryService itemCategoryService;
    private final ItemCategoryMapper itemCategoryMapper;

    @GetMapping
    public List<String> getCategories() {
        return itemCategoryService.getCategoryNames();
    }

    @GetMapping("/{id}")
    public ItemCategoryDto getCategory(@PathVariable Long id) {
        return itemCategoryMapper.mapToItemCategoryDto(itemCategoryService.findById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public void deleteCategory(@PathVariable Long id) {
        itemCategoryService.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ItemCategoryDto updateCategory(@PathVariable Long id, @RequestBody ItemCategoryDto itemCategoryDto) {
        ItemCategory itemCategory = itemCategoryService.findById(id);
        itemCategory.setName(itemCategoryDto.getName());
        return itemCategoryMapper.mapToItemCategoryDto(itemCategoryService.save(itemCategory));
    }

    @PostMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ItemCategoryDto createCategory(@RequestBody ItemCategoryDto itemCategoryDto) {
        return itemCategoryMapper
                .mapToItemCategoryDto(itemCategoryService.save(itemCategoryMapper.mapToItemCategory(itemCategoryDto)));
    }
}
