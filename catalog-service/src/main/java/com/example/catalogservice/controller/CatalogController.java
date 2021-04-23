package com.example.catalogservice.controller;


import com.example.catalogservice.domain.Catalog;
import com.example.catalogservice.service.CatalogService;
import com.example.catalogservice.vo.ResponseCatalog;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog-service")
@RequiredArgsConstructor
public class CatalogController {

    private final Environment env;
    private final CatalogService catalogService;



    @GetMapping("/catalogs")
    public ResponseEntity<?> getCatalogs(){

        List<Catalog> catalogs = catalogService.getAllCatalogs();
        List<ResponseCatalog> result = catalogs.stream()
                                        .map(ResponseCatalog::of)
                                        .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }


    @GetMapping("/health-check")
    public String status(){
        return "It is working in catalog service PORT: " + env.getProperty("local.server.port");
    }

}
