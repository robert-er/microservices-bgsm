package com.bgsm.userservice.service;

import com.bgsm.userservice.exception.NotFoundException;
import com.bgsm.userservice.model.AppUser;
import com.bgsm.userservice.model.Cart;
import com.bgsm.userservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository repository;
    private final AppUserService userService;

    public Cart findById(Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("cart not found, id: " + id));
    }

    public Cart save(Cart cart) {
        return repository.save(cart);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<Cart> getAll() {
        return repository.findAll();
    }

    public Cart createOrGetCart(Long userId) {
        AppUser user = userService.findById(userId);
        return repository.findByUser(user)
                .orElse(save(Cart.builder()
                .user(user)
                .build()));
    }
}
