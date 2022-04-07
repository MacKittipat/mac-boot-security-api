package com.mackittipat.macbootsecurityapiserver.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("products")
@RestController
public class ProductController {

    @PreAuthorize("hasAuthority('read:products')")
    @GetMapping
    public String getProduct() {
        return "Product";
    }

    @PreAuthorize("hasAuthority('create:products')")
    @PostMapping
    public String saveProduct() {
        return "Save Product";
    }

}
