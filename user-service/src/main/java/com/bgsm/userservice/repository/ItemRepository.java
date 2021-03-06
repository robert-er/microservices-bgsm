package com.bgsm.userservice.repository;

import com.bgsm.userservice.model.AppUser;
import com.bgsm.userservice.model.Item;
import com.bgsm.userservice.model.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<List<Item>> findByUser(AppUser user);
    Optional<Item> findByName(String name);
    Optional<List<Item>> findByCategory(ItemCategory category);
}
