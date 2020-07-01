package com.springframework.spring5webfluxrest.controllers;

import com.springframework.spring5webfluxrest.domain.Vendor;
import com.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @BeforeEach
    void setUp() {
        vendorRepository= Mockito.mock(VendorRepository.class);
        vendorController=new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void list() {
        given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("Joe").build(),
                        Vendor.builder().firstName("Bob").build()));

        webTestClient.get().uri("/api/v1/vendors/")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("Joe").build()));

        webTestClient.get().uri("/api/v1/vendors/1")
                .exchange()
                .expectBody(Vendor.class);
    }
    @Test
    void testCreateVendor(){
        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendToSaveMono = Mono.just(Vendor.builder().firstName("Joe").build());

        webTestClient.post()
                .uri(vendorController.BASE_URL)
                .body(vendToSaveMono,Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void testUpdateCategory(){
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(new Vendor()));

        Mono<Vendor> vendToUpdateMono = Mono.just(Vendor.builder().firstName("Joe").build());

        webTestClient.put()
                .uri(vendorController.BASE_URL+"/4551")
                .body(vendToUpdateMono,Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
    @Test
    void testPatchCategory(){
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("John").lastName("Doe").build()));
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(new Vendor()));

        Mono<Vendor> vendToUpdateMono = Mono.just(Vendor.builder().firstName("Joe").lastName("Doe").build());

        webTestClient.patch()
                .uri(vendorController.BASE_URL+"/4551")
                .body(vendToUpdateMono,Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
        verify(vendorRepository,Mockito.times(1)).save(any());
    }
    @Test
    void testPatchCategoryNoChanges(){
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("Joe").lastName("Doe").build()));
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(new Vendor()));

        Mono<Vendor> vendToUpdateMono = Mono.just(Vendor.builder().firstName("Joe").lastName("Doe").build());

        webTestClient.patch()
                .uri(vendorController.BASE_URL+"/4551")
                .body(vendToUpdateMono,Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
        verify(vendorRepository,never()).save(any());
    }
}