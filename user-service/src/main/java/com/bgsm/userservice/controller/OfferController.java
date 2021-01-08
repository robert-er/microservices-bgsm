package com.bgsm.userservice.controller;

import com.bgsm.userservice.dto.OfferDto;
import com.bgsm.userservice.mapper.OfferMapper;
import com.bgsm.userservice.model.Offer;
import com.bgsm.userservice.service.ItemService;
import com.bgsm.userservice.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("offer")
public class OfferController {

    private final OfferMapper offerMapper;
    private final OfferService offerService;
    private final ItemService itemService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public OfferDto getOffer(@PathVariable Long id) {
        return offerMapper.mapToOfferDto(offerService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<OfferDto> getAll() {
        return offerMapper.mapToOfferDtoList(offerService.getAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public void deleteOffer(@PathVariable Long id) {
        offerService.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public OfferDto updateOffer(@PathVariable Long id, @RequestBody OfferDto offerDto) {
        Offer offer = offerService.findById(id);
        offer.setItem(itemService.findById(offerDto.getItemId()));
        offer.setDateFrom(offerDto.getDateFrom());
        offer.setDateTo(offerDto.getDateTo());
        offer.setLocation(offerDto.getLocation());
        offer.setPrice(offerDto.getPrice());
        return offerMapper.mapToOfferDto(offerService.save(offer));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public OfferDto createOffer(@RequestBody OfferDto offerDto) {
        return offerMapper.mapToOfferDto(offerService.save(offerMapper.mapToOffer(offerDto)));
    }
}
