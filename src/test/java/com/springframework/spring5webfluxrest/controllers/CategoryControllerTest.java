package com.springframework.spring5webfluxrest.controllers;

import com.springframework.spring5webfluxrest.domain.Category;
import com.springframework.spring5webfluxrest.repositories.CategoryRepository;
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

class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryRepository categoryRepository;
    CategoryController categoryController;

    @BeforeEach
    void setUp() {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void list() {
        given(categoryRepository.findAll())
                .willReturn(Flux.just(Category.builder().description("category description").build(),
                        Category.builder().description("category description2").build()));

        webTestClient.get().uri(categoryController.BASE_URL)
            .exchange()
            .expectBodyList(Category.class)
            .hasSize(2);

    }

    @Test
    void getById() {
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("category description").build()));

        webTestClient
                .get()
                .uri(categoryController.BASE_URL+"/1")
                .exchange()
                .expectBody(Category.class);

    }

    @Test
    void testCreateCategory(){
        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> catToSaveMono = Mono.just(Category.builder().description("Some category").build());

        webTestClient.post()
                .uri(categoryController.BASE_URL)
                .body(catToSaveMono,Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void testUpdateCategory(){
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(new Category()));

        Mono<Category> catToUpdateMono = Mono.just(Category.builder().description("Some category").build());

        webTestClient.put()
                .uri(categoryController.BASE_URL+"/4551")
                .body(catToUpdateMono,Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void testPatchCategory(){

        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("Old description").build()));
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(new Category()));

        Mono<Category> catToPatcherMono = Mono.just(Category.builder().description("New description").build());

        webTestClient.patch()
                .uri(categoryController.BASE_URL+"/4551")
                .body(catToPatcherMono,Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository,Mockito.times(1)).save(any());
    }
    @Test
    void testPatchCategoryNoChanges(){

        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("Same old description").build()));
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(new Category()));
        Mono<Category> catToPatcherMono = Mono.just(Category.builder().description("Same old description").build());

        webTestClient.patch()
                .uri(categoryController.BASE_URL+"/4551")
                .body(catToPatcherMono,Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository,never()).save(any());
    }
}