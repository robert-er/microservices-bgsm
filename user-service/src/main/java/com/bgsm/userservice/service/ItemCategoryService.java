package com.bgsm.userservice.service;

import com.bgsm.userservice.exception.ExistException;
import com.bgsm.userservice.exception.NotFoundException;
import com.bgsm.userservice.model.Item;
import com.bgsm.userservice.model.ItemCategory;
import com.bgsm.userservice.repository.ItemCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemCategoryService {

    private final ItemCategoryRepository repository;

    public ItemCategory findById(Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Category not found, id: " + id));
    }

    public ItemCategory findByName(String name) throws NotFoundException {
        return repository.findByName(name).orElseThrow(() -> new NotFoundException("Category not found, name: " + name));
    }

    public ItemCategory save(ItemCategory itemCategory) throws ExistException {
        if(repository.findByName(itemCategory.getName()) == null ||
                !repository.findByName(itemCategory.getName()).isPresent()) {
            return repository.save(itemCategory);
        } else {
            throw new ExistException("Category " + itemCategory.getName() + " already exist.");
        }
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<ItemCategory> getAll() {
        return repository.findAll();
    }

    public List<String> getCategoryNames() {
        return getAll().stream()
                .map(ItemCategory::getName)
                .collect(Collectors.toList());
    }
}
