package com.bgsm.userservice.repository;

import com.bgsm.userservice.model.AppUser;
import com.bgsm.userservice.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<List<Item>> findByUser(AppUser user);
}
