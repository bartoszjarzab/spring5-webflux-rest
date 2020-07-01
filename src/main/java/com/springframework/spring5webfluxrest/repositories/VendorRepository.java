package com.springframework.spring5webfluxrest.repositories;

import com.springframework.spring5webfluxrest.domain.Vendor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends ReactiveMongoRepository<Vendor,String> {
}
