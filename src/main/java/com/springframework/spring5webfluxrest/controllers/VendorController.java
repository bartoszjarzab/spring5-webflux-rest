package com.springframework.spring5webfluxrest.controllers;

import com.springframework.spring5webfluxrest.domain.Vendor;
import com.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class VendorController {
    private final VendorRepository vendorRepository;
    public final String BASE_URL = "/api/v1/vendors";
    @Autowired
    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping(BASE_URL)
    Flux<Vendor> list(){

        return vendorRepository.findAll();
    }

    @GetMapping(BASE_URL+"/{id}")
    Mono<Vendor> getById(@PathVariable String id){

        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(BASE_URL)
    Mono<Void> create(@RequestBody Publisher<Vendor> vendorPublisher){
        return vendorRepository.saveAll(vendorPublisher).then();
    }

    @PutMapping(BASE_URL+"/{id}")
    Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor){
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }
    @PatchMapping(BASE_URL+"/{id}")
    Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor){
        Vendor foundVendor = vendorRepository.findById(id).block();
        if(!foundVendor.getFirstName().equals(vendor.getFirstName())){
            foundVendor.setFirstName(vendor.getFirstName());
            vendorRepository.save(foundVendor);
        }
        if(!foundVendor.getLastName().equals(vendor.getLastName())){
            foundVendor.setLastName(vendor.getLastName());
            vendorRepository.save(foundVendor);
        }
        return Mono.just(foundVendor);
    }
}
