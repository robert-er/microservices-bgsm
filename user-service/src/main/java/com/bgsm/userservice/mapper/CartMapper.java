package com.bgsm.userservice.mapper;

import com.bgsm.userservice.dto.CartDto;
import com.bgsm.userservice.model.Cart;
import com.bgsm.userservice.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartMapper {

    private final AppUserService userService;

    public Cart mapToCart(CartDto cartDto) {
        return Cart.builder()
                .user(userService.findById(cartDto.getUserId()))
                .build();
    }

    public CartDto mapToCartDto(Cart cart) {
        return CartDto.builder()
                .userId(cart.getUser().getId())
                .build();
    }
}
