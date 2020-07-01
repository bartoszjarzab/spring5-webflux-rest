package com.springframework.spring5webfluxrest.controllers;

import com.springframework.spring5webfluxrest.domain.Category;
import com.springframework.spring5webfluxrest.repositories.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public final String BASE_URL = "/api/v1/categories";

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping(BASE_URL)
    Flux<Category> list(){
        return categoryRepository.findAll();
    }

    @GetMapping(BASE_URL+"/{id}")
    Mono<Category> getById(@PathVariable String id){
        return categoryRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(BASE_URL)
    Mono<Void> create(@RequestBody Publisher<Category> categoryPublisher){
        return categoryRepository.saveAll(categoryPublisher).then();
    }

    @PutMapping(BASE_URL+"/{id}")
    Mono<Category> update(@PathVariable String id, @RequestBody Category category){
        category.setId(id);
        return categoryRepository.save(category);
    }
    @PatchMapping(BASE_URL+"/{id}")
    Mono<Category> patch(@PathVariable String id, @RequestBody Category category){
        Category foundCategory = categoryRepository.findById(id).block();
        if(!foundCategory.getDescription().equals(category.getDescription())){
            foundCategory.setDescription(category.getDescription());
            return categoryRepository.save(foundCategory);
        }

        return Mono.just(foundCategory);
    }
}
