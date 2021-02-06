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
    private final ItemCategoryService categoryService;

    public Item findById(Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("item not found, id: " + id));
    }

    public Item save(Item item) {
        return repository.save(item);
    }

    public Item update(Item item) {
        Item itemFromDB = findById(item.getId());
        itemFromDB.setName(item.getName());
        itemFromDB.setDescription(item.getDescription());
        itemFromDB.setMinPlayers(item.getMinPlayers());
        itemFromDB.setMaxPlayers(item.getMaxPlayers());
        itemFromDB.setCategory(item.getCategory());
        itemFromDB.setUser(item.getUser());
        return save(itemFromDB);
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

    public Item findByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Item with name " + name + " not found"));
    }

    public List<Item> findByCategory(String category) {
        return repository.findByCategory(categoryService.findByName(category)).orElse(new ArrayList<>());
    }
}
