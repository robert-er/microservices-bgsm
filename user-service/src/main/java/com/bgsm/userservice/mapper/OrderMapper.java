package com.bgsm.userservice.mapper;

import com.bgsm.userservice.dto.OfferDto;
import com.bgsm.userservice.dto.OrderDto;
import com.bgsm.userservice.model.Offer;
import com.bgsm.userservice.model.Order;
import com.bgsm.userservice.service.CartService;
import com.bgsm.userservice.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OfferService offerService;
    private final CartService cartService;

    public Order mapToOrder(OrderDto orderDto) {
        return Order.builder()
                .offer(offerService.findById(orderDto.getOfferId()))
                .cart(cartService.createOrGetCart(orderDto.getPurchaserUserId()))
                .build();
    }

    public OrderDto mapToOrderDto(Order order) {
        return OrderDto.builder()
                .offerId(order.getOffer().getId())
                .purchaserUserId(order.getCart().getUser().getId())
                .build();
    }

    public List<OrderDto> mapToOrderDtoList(List<Order> orderList) {
        return orderList.stream()
                .map(this::mapToOrderDto)
                .collect(Collectors.toList());
    }
}
