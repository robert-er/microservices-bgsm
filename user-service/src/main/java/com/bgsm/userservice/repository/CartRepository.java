package com.bgsm.userservice.repository;

import com.bgsm.userservice.model.AppUser;
import com.bgsm.userservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(AppUser user);
}
