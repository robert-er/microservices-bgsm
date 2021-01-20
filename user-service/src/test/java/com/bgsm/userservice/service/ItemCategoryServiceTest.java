package com.bgsm.userservice.service;

import com.bgsm.userservice.model.ItemCategory;
import com.bgsm.userservice.repository.ItemCategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ItemCategoryServiceTest {

    @Autowired
    private ItemCategoryService categoryService;

    @Autowired
    private ItemCategoryRepository categoryRepository;

    @Test
    public void shouldFindById() {
        //Given
        ItemCategory category = createCategory("TestCategoryFindById");
        Long categoryId = categoryService.save(category).getId();
        //When
        ItemCategory returnedCategory = categoryService.findById(categoryId);
        //Then
        assertEquals(category, returnedCategory);
        //CleanUp
        deleteCategory(categoryId);
    }

    @Test
    public void shouldFindByName() {
        //Given
        String categoryName = "TestCategoryFindByName";
        ItemCategory category = createCategory(categoryName);
        Long categoryId = categoryService.save(category).getId();
        //When
        ItemCategory returnedCategory = categoryService.findByName(categoryName);
        //Then
        assertEquals(category, returnedCategory);
        //CleanUp
        deleteCategory(categoryId);
    }

    @Test
    public void shouldSave() {
        //Given
        ItemCategory category = createCategory("TestCategorySave");
        //When
        Long categoryId = categoryService.save(category).getId();
        //Then
        assertNotEquals(0L, categoryId);
        //CleanUp
        deleteCategory(categoryId);
    }

    @Test
    public void shouldDeleteById() {
        //Given
        ItemCategory category = createCategory("TestCategoryDelete");
        Long categoryId = categoryService.save(category).getId();
        //When
        deleteCategory(categoryId);
        //Then
        assertEquals(Optional.empty(), categoryRepository.findById(categoryId));

    }

    @Test
    public void shouldGetAll() {
        //Given
        ItemCategory category1 = createCategory("TestCategory1GetAll");
        ItemCategory category2 = createCategory("TestCategory2GetAll");
        ItemCategory category3 = createCategory("TestCategory3GetAll");
        Long categoryId1 = categoryService.save(category1).getId();
        Long categoryId2 = categoryService.save(category2).getId();
        Long categoryId3 = categoryService.save(category3).getId();
        //When
        List<ItemCategory> returnedCategories = categoryService.getAll();
        //Then
        assertTrue(returnedCategories.contains(category1));
        assertTrue(returnedCategories.contains(category2));
        assertTrue(returnedCategories.contains(category3));
        //CleanUp
        deleteCategory(categoryId1);
        deleteCategory(categoryId2);
        deleteCategory(categoryId3);
    }

    @Test
    public void shouldGetCategoryNames() {
        //Given
        ItemCategory category1 = createCategory("TestCategory1GetNames");
        ItemCategory category2 = createCategory("TestCategory2GetNames");
        ItemCategory category3 = createCategory("TestCategory3GetNames");
        Long categoryId1 = categoryService.save(category1).getId();
        Long categoryId2 = categoryService.save(category2).getId();
        Long categoryId3 = categoryService.save(category3).getId();
        //When
        List<String> categoryNames = categoryService.getCategoryNames();
        //Then
        assertTrue(categoryNames.contains(category1.getName()));
        assertTrue(categoryNames.contains(category2.getName()));
        assertTrue(categoryNames.contains(category3.getName()));
        //CleanUp
        deleteCategory(categoryId1);
        deleteCategory(categoryId2);
        deleteCategory(categoryId3);
    }

    private ItemCategory createCategory(String name) {
        return ItemCategory.builder()
                .name(name)
                .build();
    }

    private void deleteCategory(Long categoryId) {
        categoryService.deleteById(categoryId);
    }
}