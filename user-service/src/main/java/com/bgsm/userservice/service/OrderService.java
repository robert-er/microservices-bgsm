package com.bgsm.userservice.service;

import com.bgsm.userservice.exception.NotFoundException;
import com.bgsm.userservice.model.Cart;
import com.bgsm.userservice.model.EOfferStatus;
import com.bgsm.userservice.model.Offer;
import com.bgsm.userservice.model.Order;
import com.bgsm.userservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OfferService offerService;
    private final CartService cartService;

    public Order findById(Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("order not found, id: " + id));
    }

    public Order save(Order order) {
        return repository.save(order);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<Order> getAll() {
        return repository.findAll();
    }

    public Order createOrderFromOffer(Long offerId, Long userId) {
        Offer offer = offerService.changeOfferStatus(offerId, EOfferStatus.NOT_ACTIVE);
        Cart cart = cartService.createOrGetCart(userId);
        Order order = Order.builder()
                .offer(offer)
                .cart(cart)
                .build();
        return repository.save(order);
    }
}
