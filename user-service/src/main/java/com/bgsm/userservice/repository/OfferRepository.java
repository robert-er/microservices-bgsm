package com.bgsm.userservice.repository;

import com.bgsm.userservice.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
}
