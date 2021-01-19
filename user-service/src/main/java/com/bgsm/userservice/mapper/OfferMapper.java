package com.bgsm.userservice.mapper;

import com.bgsm.userservice.dto.OfferDto;
import com.bgsm.userservice.model.Offer;
import com.bgsm.userservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OfferMapper {

    private final ItemService itemService;

    public Offer mapToOffer(OfferDto offerDto) {
        return Offer.builder()
                .item(itemService.findById(offerDto.getItemId()))
                .dateFrom(offerDto.getDateFrom())
                .dateTo(offerDto.getDateTo())
                .location(offerDto.getLocation())
                .price(offerDto.getPrice())
                .build();
    }

    public OfferDto mapToOfferDto(Offer offer) {
        return OfferDto.builder()
                .id(offer.getId())
                .itemId(offer.getItem().getId())
                .dateFrom(offer.getDateFrom())
                .dateTo(offer.getDateTo())
                .location(offer.getLocation())
                .price(offer.getPrice())
                .build();
    }

    public List<OfferDto> mapToOfferDtoList(List<Offer> offerList) {
        return offerList.stream()
                .map(this::mapToOfferDto)
                .collect(Collectors.toList());
    }
}

