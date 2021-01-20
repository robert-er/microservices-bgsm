package com.bgsm.userservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

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
    private EOfferStatus status;

    @Builder
    public Offer(Item item, LocalDate dateFrom, LocalDate dateTo,
                 String location, BigDecimal price, EOfferStatus status) {
        this.item = item;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.location = location;
        this.price = price;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return item.equals(offer.item) &&
                dateFrom.equals(offer.dateFrom) &&
                dateTo.equals(offer.dateTo) &&
                location.equals(offer.location) &&
                price.equals(offer.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, dateFrom, dateTo, location, price);
    }
}
