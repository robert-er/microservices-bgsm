package com.bgsm.userservice.service;

import com.bgsm.userservice.exception.NotFoundException;
import com.bgsm.userservice.model.Item;
import com.bgsm.userservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository repository;
    private final AppUserService appUserService;

    public Item findById(Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("item not found, id: " + id));
    }

    public Item save(Item item) {
        return repository.save(item);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<Item> getAll() {
        return repository.findAll();
    }

    public List<Item> findByUserId(Long id) {
        return repository.findByUser(appUserService.findById(id)).orElse(new ArrayList<>());
    }
}
