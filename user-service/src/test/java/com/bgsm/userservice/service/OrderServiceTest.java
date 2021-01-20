package com.bgsm.userservice.service;

import com.bgsm.userservice.model.*;
import com.bgsm.userservice.repository.OfferRepository;
import com.bgsm.userservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class OrderServiceTest {

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
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void shouldFindById() {
        //Given
        AppUser user = createUser("UserOrderTestFindById");
        Long userId = userService.save(user).getId();
        String categoryName = "CategoryOrderTestFindById";
        Item item = createItem("ItemOrderTestFindById", categoryName, user);
        Long itemId = itemService.save(item).getId();
        Offer offer = createOffer(item);
        Long offerId = offerService.save(offer).getId();

        AppUser rentierUser = createUser("RentierUserTestFindById");
        Long rentierUserId = userService.save(rentierUser).getId();
        Order order = createOrder(offerId, rentierUserId);
        Long cartId = order.getCart().getId();
        //When
        Order returnedOrder = orderService.findById(order.getId());
        //Then
        assertEquals(returnedOrder.getId(), order.getId());
        //CleanUp
        deleteOrder(order.getId());
        deleteOffer(offerId);
        deleteItem(itemId);
        deleteCategory(categoryService.findByName(categoryName).getId());
        deleteUser(userId);
        deleteCart(cartId);
        deleteUser(rentierUserId);
    }

    @Test
    public void shouldSave() {
        //Given
        AppUser user = createUser("UserOrderTestSave");
        Long userId = userService.save(user).getId();
        String categoryName = "CategoryOrderTestSave";
        Item item = createItem("ItemOrderTestSave", categoryName, user);
        Long itemId = itemService.save(item).getId();
        Offer offer = createOffer(item);
        Long offerId = offerService.save(offer).getId();

        AppUser rentierUser = createUser("RentierUserTestSave");
        Long rentierUserId = userService.save(rentierUser).getId();
        //When
        Order order = createOrder(offerId, rentierUserId);
        Long cartId = order.getCart().getId();
        //Then
        assertNotEquals(0L, order.getId());
        //CleanUp
        deleteOrder(order.getId());
        deleteOffer(offerId);
        deleteItem(itemId);
        deleteCategory(categoryService.findByName(categoryName).getId());
        deleteUser(userId);
        deleteCart(cartId);
        deleteUser(rentierUserId);
    }

    @Test
    public void shouldDeleteById() {
        //Given
        AppUser user = createUser("UserOrderTestDelete");
        Long userId = userService.save(user).getId();
        String categoryName = "CategoryOrderTestDelete";
        Item item = createItem("ItemOrderTestDelete", categoryName, user);
        Long itemId = itemService.save(item).getId();
        Offer offer = createOffer(item);
        Long offerId = offerService.save(offer).getId();
        AppUser rentierUser = createUser("RentierUserTestDelete");
        Long rentierUserId = userService.save(rentierUser).getId();
        Order order = createOrder(offerId, rentierUserId);
        Long cartId = order.getCart().getId();
        //When
        deleteOrder(order.getId());
        //Then
        assertEquals(Optional.empty(), orderRepository.findById(offerId));
        //CleanUp
        deleteOffer(offerId);
        deleteItem(itemId);
        deleteCategory(categoryService.findByName(categoryName).getId());
        deleteUser(userId);
        deleteCart(cartId);
        deleteUser(rentierUserId);
    }

    @Test
    public void shouldGetAll() {
        //Given
        AppUser user = createUser("UserOrderTestGetAll1");
        Long userId = userService.save(user).getId();
        AppUser rentierUser = createUser("RentierUserTestGetAll1");
        Long rentierUserId = userService.save(rentierUser).getId();
        String categoryName1 = "CategoryOrderTestGetAll1";
        Item item1 = createItem("ItemOrderTestGetAll1", categoryName1, user);
        Long itemId1 = itemService.save(item1).getId();
        Offer offer1 = createOffer(item1);
        Long offerId1 = offerService.save(offer1).getId();
        Order order1 = createOrder(offerId1, rentierUserId);
        Long cartId1 = order1.getCart().getId();

        String categoryName2 = "CategoryOrderTestGetAll2";
        Item item2 = createItem("ItemOrderTestGetAll2", categoryName2, user);
        Long itemId2 = itemService.save(item2).getId();
        Offer offer2 = createOffer(item2);
        Long offerId2 = offerService.save(offer2).getId();
        Order order2 = createOrder(offerId2, rentierUserId);
        Long cartId2 = order1.getCart().getId();
        //When
        List<Order> returnedOrders = orderService.getAll();
        //Then
        assertEquals(returnedOrders.stream().filter(order -> order.getId().equals(order1.getId()))
                .collect(Collectors.toList()).stream().findFirst().get().getId(), order1.getId());
        assertEquals(returnedOrders.stream().filter(order -> order.getId().equals(order2.getId()))
                .collect(Collectors.toList()).stream().findFirst().get().getId(), order2.getId());
        //CleanUp
        deleteOrder(order1.getId());
        deleteOffer(offerId1);
        deleteItem(itemId1);
        deleteOrder(order2.getId());
        deleteOffer(offerId2);
        deleteItem(itemId2);
        deleteCategory(categoryService.findByName(categoryName1).getId());
        deleteCategory(categoryService.findByName(categoryName2).getId());
        deleteUser(userId);
        deleteCart(cartId1);
        deleteUser(rentierUserId);
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

    private Order createOrder(Long offerId, Long userId) {
        return orderService.createOrderFromOffer(offerId, userId);
    }

    private void deleteOrder(Long orderId) {
        orderService.deleteById(orderId);
    }

    private void deleteCart(Long cartId) {
        cartService.deleteById(cartId);
    }
}