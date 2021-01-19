package com.bgsm.userservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="cart_id")
    private Cart cart;
}
