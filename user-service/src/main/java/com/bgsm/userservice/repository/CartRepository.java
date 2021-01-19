package com.bgsm.userservice.repository;

import com.bgsm.userservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
