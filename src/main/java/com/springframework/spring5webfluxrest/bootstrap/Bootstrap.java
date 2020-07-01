package com.springframework.spring5webfluxrest.bootstrap;

import com.springframework.spring5webfluxrest.domain.Category;
import com.springframework.spring5webfluxrest.domain.Vendor;
import com.springframework.spring5webfluxrest.repositories.CategoryRepository;
import com.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(categoryRepository.count().block()==0){
            loadCategories();
            loadVendors();
            System.out.println("Recipe data loaded into MongoDb...");

        }else{
            System.out.println("Recipe data was already in MongoDB...");
        }
        System.out.println("Vendors count: "+vendorRepository.count().block());
        System.out.println("Categories count: "+categoryRepository.count().block());

    }

    private void loadVendors(){
        Vendor ven1 = new Vendor();
        ven1.setFirstName("John");
        ven1.setLastName("Doe");
        vendorRepository.save(ven1).block();

        Vendor ven2 = new Vendor();
        ven2.setFirstName("Bon");
        ven2.setLastName("Joe");
        vendorRepository.save(ven2).block();

        Vendor ven3 = new Vendor();
        ven3.setFirstName("Tike");
        ven3.setLastName("Myson");
        vendorRepository.save(ven3).block();
    }
    private void loadCategories(){
        Category cat1 = new Category();
        cat1.setDescription("Nuts");
        categoryRepository.save(cat1).block();

        Category cat2 = new Category();
        cat2.setDescription("Breads");
        categoryRepository.save(cat2).block();

        Category cat3 = new Category();
        cat3.setDescription("Meats");
        categoryRepository.save(cat3).block();

        Category cat4 = new Category();
        cat4.setDescription("Fruits");
        categoryRepository.save(cat4).block();
    }
}
