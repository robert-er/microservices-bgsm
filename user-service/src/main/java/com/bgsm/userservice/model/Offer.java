package com.bgsm.userservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "offers")
@Getter
@Setter
@NoArgsConstructor
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String location;
    private BigDecimal price;

    @Builder
    public Offer(Item item, LocalDate dateFrom, LocalDate dateTo, String location, BigDecimal price) {
        this.item = item;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.location = location;
        this.price = price;
    }
}
