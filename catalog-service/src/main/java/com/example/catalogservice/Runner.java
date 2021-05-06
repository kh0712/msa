package com.example.catalogservice;

import com.example.catalogservice.domain.Catalog;
import com.example.catalogservice.domain.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

//@Component
//public class Runner implements ApplicationRunner {
//
//    @Autowired
//    CatalogRepository catalogRepository;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        for (int i = 0; i <3; i++) {
//            Catalog product = Catalog.builder()
//                    .productId("CATALOG-00"+(i+1))
//                    .productName("PRODUCT"+(i+1))
//                    .stock(100*(i+1))
//                    .unitPrice(1000*(i+1))
//                    .build();
//            catalogRepository.save(product);
//        }
//
//
//    }
//}
