package com.bgsm.userservice.service;

import com.bgsm.userservice.model.AppUser;
import com.bgsm.userservice.model.ERole;
import com.bgsm.userservice.model.Item;
import com.bgsm.userservice.model.ItemCategory;
import com.bgsm.userservice.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ItemServiceTest {

    @Autowired
    private RoleService roleService;

    @Autowired
    private AppUserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemCategoryService categoryService;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void shouldFindById() {
        //Given
        String categoryName = "TestCategoryForItemServiceTest";
        AppUser user = createUser("UserItemFindById");
        Long userId = userService.save(user).getId();
        Item item = createItem("ItemForShouldFindById", categoryName, user);
        Long itemId = itemService.save(item).getId();
        //When
        Item returnedItem = itemService.findById(itemId);
        //Then
        assertEquals(returnedItem, item);
        //CleanUp
        deleteItem(itemId);
        deleteCategory(categoryService.findByName(categoryName).getId());
        deleteUser(userId);
    }

    @Test
    public void shouldSave() {
        String categoryName = "TestCategoryForSaveItemServiceTest";
        AppUser user = createUser("UserItemSave");
        Long userId = userService.save(user).getId();
        Item item = createItem("ItemForShouldSave", categoryName, user);
        //When
        Long itemId = itemService.save(item).getId();
        //Then
        assertNotEquals(0L, itemId);
        //CleanUp
        deleteItem(itemId);
        deleteCategory(categoryService.findByName(categoryName).getId());
        deleteUser(userId);
    }

    @Test
    public void shouldDeleteById() {
        //Given
        String categoryName = "TestCategoryForItemDelete";
        AppUser user = createUser("UserItemDelete");
        Long userId = userService.save(user).getId();
        Item item = createItem("ItemForShouldDelete", categoryName, user);
        Long itemId = itemService.save(item).getId();
        //When
        deleteItem(itemId);
        //Then
        assertEquals(Optional.empty(), itemRepository.findById(itemId));
        //CleanUp
        deleteCategory(categoryService.findByName(categoryName).getId());
        deleteUser(userId);
    }

    @Test
    public void shouldGetAll() {
        //Given
        String categoryName1 = "TestCategoryForItemGetAll11";
        AppUser user1 = createUser("UserItemGetAll11");
        Long userId1 = userService.save(user1).getId();
        Item item1 = createItem("ItemForShouldGetAll11", categoryName1, user1);
        Long itemId1 = itemService.save(item1).getId();

        String categoryName2 = "TestCategoryForItemGetAll22";
        AppUser user2 = createUser("UserItemGetAll22");
        Long userId2 = userService.save(user2).getId();
        Item item2 = createItem("ItemForShouldGetAll22", categoryName2, user2);
        Long itemId2 = itemService.save(item2).getId();

        String categoryName3 = "TestCategoryForItemGetAll33";
        AppUser user3 = createUser("UserItemGetAll33");
        Long userId3 = userService.save(user3).getId();
        Item item3 = createItem("ItemForShouldGetAll33", categoryName3, user3);
        Long itemId3 = itemService.save(item3).getId();
        //When
        List<Item> returnedItems = itemService.getAll();
        //Then
        assertTrue(returnedItems.contains(item1));
        assertTrue(returnedItems.contains(item2));
        assertTrue(returnedItems.contains(item3));
        //CleanUp
        deleteItem(itemId1);
        deleteCategory(categoryService.findByName(categoryName1).getId());
        deleteUser(userId1);

        deleteItem(itemId2);
        deleteCategory(categoryService.findByName(categoryName2).getId());
        deleteUser(userId2);

        deleteItem(itemId3);
        deleteCategory(categoryService.findByName(categoryName3).getId());
        deleteUser(userId3);
    }

    @Test
    public void shouldFindByUserId() {
        //Given
        String categoryName = "TestCategoryForItemFindByUserId";
        AppUser user = createUser("TestUserItemFindByUserId");
        Long userId = userService.save(user).getId();
        Item item = createItem("TestItemForFindByUserId", categoryName, user);
        Long itemId = itemService.save(item).getId();
        //When
        List<Item> returnedItems = itemService.findByUserId(userId);
        //Then
        assertTrue(returnedItems.contains(item));
        //CleanUp
        deleteItem(itemId);
        deleteCategory(categoryService.findByName(categoryName).getId());
        deleteUser(userId);
    }

    @Test
    public void shouldFindByName() {
        //Given
        String itemName = "TestItemForFindByName";
        String categoryName = "TestCategoryForItemFindByName";
        AppUser user = createUser("TestUserItemFindByName");
        Long userId = userService.save(user).getId();
        Item item = createItem(itemName, categoryName, user);
        Long itemId = itemService.save(item).getId();
        //When
        Item returnedItem = itemService.findByName(itemName);
        //Then
        assertEquals(returnedItem, item);
        //CleanUp
        deleteItem(itemId);
        deleteCategory(categoryService.findByName(categoryName).getId());
        deleteUser(userId);
    }

    @Test
    public void shouldFindByCategory() {
        //Given
        String itemName = "TestItemForFindByCategory";
        String categoryName = "TestCategoryForItemFindByCategory";
        AppUser user = createUser("TestUserItemFindByCategory");
        Long userId = userService.save(user).getId();
        Item item = createItem(itemName, categoryName, user);
        Long itemId = itemService.save(item).getId();
        //When
        List<Item> returnedItems = itemService.findByCategory(categoryName);
        //Then
        assertTrue(returnedItems.contains(item));
        //CleanUp
        deleteItem(itemId);
        deleteCategory(categoryService.findByName(categoryName).getId());
        deleteUser(userId);
    }

    private AppUser createUser(String name) {
        return AppUser.builder()
                .username(name)
                .password("TomPassword123")
                .email("tom@mail.com")
                .roles(Stream.of(ERole.ROLE_USER)
                        .map(roleService::getRole)
                        .collect(Collectors.toSet()))
                .build();
    }

    private void deleteUser(Long userId) {
        userService.deleteById(userId);
    }

    private Item createItem(String itemName, String categoryName, AppUser user) {
        return Item.builder()
                .name(itemName)
                .description("testDescription")
                .minPlayers(1.0)
                .maxPlayers(2.0)
                .category(createCategory(categoryName))
                .user(user)
                .build();
    }

    private void deleteItem(Long itemId) {
        itemService.deleteById(itemId);
    }

    private ItemCategory createCategory(String name) {
        return categoryService.save(ItemCategory.builder()
                .name(name)
                .build());
    }

    private void deleteCategory(Long categoryId) {
        categoryService.deleteById(categoryId);
    }
}