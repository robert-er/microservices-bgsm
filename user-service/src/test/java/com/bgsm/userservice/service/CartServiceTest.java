package com.bgsm.userservice.service;

import com.bgsm.userservice.model.AppUser;
import com.bgsm.userservice.model.Cart;
import com.bgsm.userservice.model.ERole;
import com.bgsm.userservice.repository.CartRepository;
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
class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AppUserService userService;

    @Autowired
    private RoleService roleService;

    @Test
    public void shouldFindById() {
        //Given
        AppUser user = createUser("UserForFindById");
        Long userId = userService.save(user).getId();
        Cart cart = createCart(user);
        Long cartId = cartService.save(cart).getId();
        //When
        Cart returnedCart = cartService.findById(cartId);
        //Then
        assertNotEquals(0L, returnedCart.getId());
        //CleanUp
        deleteCart(cartId);
        deleteUser(userId);
    }

    @Test
    public void shouldSave() {
        //Given
        AppUser user = createUser("UserForSaveCart");
        Long userId = userService.save(user).getId();
        Cart cart = createCart(user);
        //When
        Long cartId = cartService.save(cart).getId();
        //Then
        assertNotEquals(0L, cartId);
        //CleanUp
        deleteCart(cartId);
        deleteUser(userId);
    }

    @Test
    public void shouldDeleteById() {
        //Given
        AppUser user = createUser("UserForDeleteById");
        Long userId = userService.save(user).getId();
        Cart cart = createCart(user);
        Long cartId = cartService.save(cart).getId();
        //When
        deleteCart(cartId);
        //Then
        assertEquals(Optional.empty(), cartRepository.findById(cartId));
        //CleanUp
        deleteUser(userId);
    }

    @Test
    public void shouldGetAll() {
        //Given
        AppUser user1 = createUser("UserForGetAllUser1");
        Long userId1 = userService.save(user1).getId();
        Cart cart1 = createCart(user1);
        Long cartId1 = cartService.save(cart1).getId();
        AppUser user2 = createUser("UserForGetAllUser2");
        Long userId2 = userService.save(user2).getId();
        Cart cart2 = createCart(user2);
        Long cartId2 = cartService.save(cart2).getId();
        //When
        List<Cart> returnedCarts = cartService.getAll();
        //Then
        assertTrue(returnedCarts.contains(cart1));
        assertTrue(returnedCarts.contains(cart2));
        //CleanUp
        deleteCart(cartId1);
        deleteUser(userId1);
        deleteCart(cartId2);
        deleteUser(userId2);
    }

    @Test
    public void shouldCreateCart() {
        //Given
        AppUser user = createUser("UserForCreateCart");
        Long userId = userService.save(user).getId();
        //When
        Cart createdNewCart = cartService.createOrGetCart(userId);
        //Then
        assertNotEquals(0L, createdNewCart.getId());
        //CleanUp
        deleteCart(createdNewCart.getId());
        deleteUser(userId);
    }

    @Test
    public void shouldGetCart() {
        //Given
        AppUser user = createUser("UserForGetCartService3");
        Long userId = userService.save(user).getId();
        Cart cart = createCart(user);
        cartService.save(cart);
        //When
        Cart getExistedCart = cartService.createOrGetCart(userId);
        //Then
        assertEquals(getExistedCart.getUser(), user);
        //CleanUp
        deleteCart(getExistedCart.getId());
        deleteUser(userId);
    }

    private Cart createCart(AppUser user) {
        return Cart.builder()
                .user(user)
                .build();
    }

    private void deleteCart(Long cartId) {
        cartService.deleteById(cartId);
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
}