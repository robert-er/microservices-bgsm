package com.bgsm.userservice.service;

import com.bgsm.userservice.exception.NotFoundException;
import com.bgsm.userservice.model.Offer;
import com.bgsm.userservice.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository repository;

    public Offer findById(Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("item not found, id: " + id));
    }

    public Offer save(Offer offer) {
        return repository.save(offer);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<Offer> getAll() {
        return repository.findAll();
    }
}
