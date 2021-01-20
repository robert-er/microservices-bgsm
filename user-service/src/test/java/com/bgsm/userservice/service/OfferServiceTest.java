package com.bgsm.userservice.service;

import com.bgsm.userservice.model.*;
import com.bgsm.userservice.repository.OfferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.constraints.AssertTrue;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class OfferServiceTest {

    @Autowired
    private RoleService roleService;

    @Autowired
    private AppUserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemCategoryService categoryService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private OfferRepository offerRepository;

    @Test
    public void shouldFindById() {
        //Given
        AppUser user = createUser("UserOfferServiceTestFindById222");
        Long userId = userService.save(user).getId();
        String categoryName = "CategoryOfferServiceTestFindById222";
        Item item = createItem("ItemOfferServiceTestFindById222", categoryName, user);
        Long itemId = itemService.save(item).getId();
        Offer offer = createOffer(item);
        Long offerId = offerService.save(offer).getId();
        //When
        Offer returnedOffer = offerService.findById(offerId);
        //Then
        assertEquals(returnedOffer.getId(), offer.getId());
        //CleanUp
        deleteOffer(offerId);
        deleteItem(itemId);
        deleteCategory(categoryService.findByName(categoryName).getId());
        deleteUser(userId);
    }

    @Test
    public void shouldSave() {
        //Given
        AppUser user = createUser("UserOfferServiceTestSave");
        Long userId = userService.save(user).getId();
        String categoryName = "CategoryOfferServiceTestSave";
        Item item = createItem("ItemOfferServiceTestSave", categoryName, user);
        Long itemId = itemService.save(item).getId();
        Offer offer = createOffer(item);
        //When
        Long offerId = offerService.save(offer).getId();
        //Then
        assertNotEquals(0l, offerId);
        //CleanUp
        deleteOffer(offerId);
        deleteItem(itemId);
        deleteCategory(categoryService.findByName(categoryName).getId());
        deleteUser(userId);
    }

    @Test
    public void shouldDeleteById() {
        //Given
        AppUser user = createUser("UserOfferServiceTestDelete");
        Long userId = userService.save(user).getId();
        String categoryName = "CategoryOfferServiceTestDelete";
        Item item = createItem("ItemOfferServiceTestDelete", categoryName, user);
        Long itemId = itemService.save(item).getId();
        Offer offer = createOffer(item);
        Long offerId = offerService.save(offer).getId();
        //When
        deleteOffer(offerId);
        //Then
        assertEquals(Optional.empty(), offerRepository.findById(offerId));
        //CleanUp
        deleteItem(itemId);
        deleteCategory(categoryService.findByName(categoryName).getId());
        deleteUser(userId);
    }

    @Test
    public void shouldGetAll() {
        //Given
        AppUser user1 = createUser("UserOfferServiceTestGetAll111");
        Long userId1 = userService.save(user1).getId();
        String categoryName1 = "CategoryOfferServiceTestGetAll111";
        Item item1 = createItem("ItemOfferServiceTestGetAll111", categoryName1, user1);
        Long itemId1 = itemService.save(item1).getId();
        Offer offer1 = createOffer(item1);
        Offer savedOffer1 = offerService.save(offer1);
        Long offerId1 = savedOffer1.getId();

        AppUser user2 = createUser("UserOfferServiceTestGetAll222");
        Long userId2 = userService.save(user2).getId();
        String categoryName2 = "CategoryOfferServiceTestGetAll222";
        Item item2 = createItem("ItemOfferServiceTestGetAll222", categoryName2, user2);
        Long itemId2 = itemService.save(item2).getId();
        Offer offer2 = createOffer(item2);
        Offer savedOffer2 = offerService.save(offer2);
        Long offerId2 = savedOffer2.getId();

        AppUser user3 = createUser("UserOfferServiceTestGetAll333");
        Long userId3 = userService.save(user3).getId();
        String categoryName3 = "CategoryOfferServiceTestGetAll333";
        Item item3 = createItem("ItemOfferServiceTestGetAll333", categoryName3, user3);
        Long itemId3 = itemService.save(item3).getId();
        Offer offer3 = createOffer(item3);
        Offer savedOffer3 = offerService.save(offer3);
        Long offerId3 = savedOffer3.getId();
        //When
        List<Offer> returnedOffers = offerService.getAll();
        //Then
        assertEquals(returnedOffers.stream().filter(offer -> offer.getId().equals(offerId1))
        .collect(Collectors.toList()).stream().findFirst().get().getId(), offerId1);
        assertEquals(returnedOffers.stream().filter(offer -> offer.getId().equals(offerId2))
                .collect(Collectors.toList()).stream().findFirst().get().getId(), offerId2);
        assertEquals(returnedOffers.stream().filter(offer -> offer.getId().equals(offerId3))
                .collect(Collectors.toList()).stream().findFirst().get().getId(), offerId3);
        //CleanUp
        deleteOffer(offerId1);
        deleteItem(itemId1);
        deleteCategory(categoryService.findByName(categoryName1).getId());
        deleteUser(userId1);

        deleteOffer(offerId2);
        deleteItem(itemId2);
        deleteCategory(categoryService.findByName(categoryName2).getId());
        deleteUser(userId2);

        deleteOffer(offerId3);
        deleteItem(itemId3);
        deleteCategory(categoryService.findByName(categoryName3).getId());
        deleteUser(userId3);
    }

    @Test
    public void shouldFindByCategoryId() {
        //Given
        AppUser user = createUser("UserOfferServiceTestFindByCategoryId");
        Long userId = userService.save(user).getId();
        String categoryName = "CategoryOfferServiceTestFindByCategoryId";
        Item item = createItem("ItemOfferServiceTestFindByCategoryId", categoryName, user);
        Long itemId = itemService.save(item).getId();
        Offer offer = createOffer(item);
        Long offerId = offerService.save(offer).getId();
        Long categoryId = categoryService.findByName(categoryName).getId();
        //When
        List<Offer> returnedOffers = offerService.findByCategoryId(categoryId);
        //Then
        assertEquals(returnedOffers.stream().filter(offer1 -> offer1.getId().equals(offerId))
                .collect(Collectors.toList()).stream().findFirst().get().getId(), offerId);
        //CleanUp
        deleteOffer(offerId);
        deleteItem(itemId);
        deleteCategory(categoryId);
        deleteUser(userId);
    }

    @Test
    public void shouldFindByCategoryName() {
        //Given
        AppUser user = createUser("UserOfferServiceTestFindByCategoryName");
        Long userId = userService.save(user).getId();
        String categoryName = "CategoryOfferServiceTestFindByCategoryName";
        Item item = createItem("ItemOfferServiceTestFindByCategoryName", categoryName, user);
        Long itemId = itemService.save(item).getId();
        Offer offer = createOffer(item);
        Long offerId = offerService.save(offer).getId();
        Long categoryId = categoryService.findByName(categoryName).getId();
        //When
        List<Offer> returnedOffers = offerService.findByCategoryName(categoryName);
        //Then
        assertEquals(returnedOffers.stream().filter(offer1 -> offer1.getId().equals(offerId))
                .collect(Collectors.toList()).stream().findFirst().get().getId(), offerId);
        //CleanUp
        deleteOffer(offerId);
        deleteItem(itemId);
        deleteCategory(categoryId);
        deleteUser(userId);
    }

    @Test
    public void shouldFindByUserId() {
        //Given
        AppUser user = createUser("UserOfferServiceTestFindByUserId");
        Long userId = userService.save(user).getId();
        String categoryName = "CategoryOfferServiceTestFindByUserId";
        Item item = createItem("ItemOfferServiceTestFindByUserId", categoryName, user);
        Long itemId = itemService.save(item).getId();
        Offer offer = createOffer(item);
        Long offerId = offerService.save(offer).getId();
        Long categoryId = categoryService.findByName(categoryName).getId();
        //When
        List<Offer> returnedOffers = offerService.findByUserId(userId);
        //Then
        assertEquals(returnedOffers.stream().filter(offer1 -> offer1.getId().equals(offerId))
                .collect(Collectors.toList()).stream().findFirst().get().getId(), offerId);
        //CleanUp
        deleteOffer(offerId);
        deleteItem(itemId);
        deleteCategory(categoryId);
        deleteUser(userId);
    }

    @Test
    public void shouldFindByItem() {
        //Given
        AppUser user = createUser("UserOfferServiceTestFindByItem");
        Long userId = userService.save(user).getId();
        String categoryName = "CategoryOfferServiceTestFindByItem";
        Item item = createItem("ItemOfferServiceTestFindByItem", categoryName, user);
        Long itemId = itemService.save(item).getId();
        Offer offer = createOffer(item);
        Long offerId = offerService.save(offer).getId();
        Long categoryId = categoryService.findByName(categoryName).getId();
        //When
        Offer returnedOffer = offerService.findByItem(item);
        //Then
        assertEquals(returnedOffer.getId(), offerId);
        //CleanUp
        deleteOffer(offerId);
        deleteItem(itemId);
        deleteCategory(categoryId);
        deleteUser(userId);
    }

    @Test
    public void shouldChangeOfferStatus() {
        //Given
        AppUser user = createUser("UserOfferServiceTestChangeOfferStatus");
        Long userId = userService.save(user).getId();
        String categoryName = "CategoryOfferServiceTestChangeOfferStatus";
        Item item = createItem("ItemOfferServiceTestChangeOfferStatus", categoryName, user);
        Long itemId = itemService.save(item).getId();
        Offer offer = createOffer(item);
        Offer savedOffer = offerService.save(offer);
        Long offerId = savedOffer.getId();
        EOfferStatus status = savedOffer.getStatus();
        Long categoryId = categoryService.findByName(categoryName).getId();
        //When
        offerService.changeOfferStatus(offerId, EOfferStatus.NOT_ACTIVE);
        //Then
        assertEquals(EOfferStatus.NOT_ACTIVE, offerService.findById(offerId).getStatus());
        //CleanUp
        deleteOffer(offerId);
        deleteItem(itemId);
        deleteCategory(categoryId);
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

    private Offer createOffer(Item item) {
        return Offer.builder()
                .item(item)
                .dateFrom(LocalDate.of(1999, 9, 9))
                .dateTo(LocalDate.of(1999, 10, 19))
                .location("RandomTestLocation")
                .price(new BigDecimal(100.00))
                .status(EOfferStatus.ACTIVE)
                .build();
    }

    private void deleteOffer(Long offerId) {
        offerService.deleteById(offerId);
    }
}