package com.dress.shop.controllers;


import com.dress.shop.domain.Product;
import com.dress.shop.domain.ShopFilters;
import com.dress.shop.services.*;
import com.fasterxml.jackson.annotation.JsonView;
import org.keycloak.adapters.spi.AuthOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(value = "/api/product")
@CrossOrigin
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    MapValidationErrorService mapValidationErrorService;

    @Autowired
    KeyCloakService keyCloakService;

    @Autowired
    TypeService typeService;


    @PostMapping("")
    public ResponseEntity<?> createNewProduct(@Valid @RequestBody Product product, BindingResult result, Principal principal, HttpServletRequest request){

            ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
            if (errorMap != null) return errorMap;

        String role="admin";
        if (keyCloakService.authenticate(request,role).equals(AuthOutcome.AUTHENTICATED)) {


            Product p = productService.saveOrUpdateProduct(product);
            p.setSuccess(true);

            return new ResponseEntity<Product>(p, HttpStatus.CREATED);
        }else {
            return new ResponseEntity("Hi!, you are NOT auhorized !", HttpStatus.UNAUTHORIZED);

        }
    }

    @GetMapping("/findBySold")
    public ResponseEntity<?> getProductsBySold(){
        return new ResponseEntity(productService.getProductBySalary(), HttpStatus.OK);
    }

    @GetMapping("/findByArrival")
    public ResponseEntity<?> getProductsByArrival(){
        return new ResponseEntity(productService.getProductByArrival(), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProducts(){

        return new ResponseEntity<>(productService.getAllProducts(),HttpStatus.OK);
    }

    @PostMapping("/shop")
    public ResponseEntity<?> shop(@RequestBody ShopFilters shopFilters){

    return new ResponseEntity<>(productService.shop(shopFilters),HttpStatus.OK);


    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") String id){
        return new ResponseEntity<>(productService.getProductById(id),HttpStatus.OK);
    }



}
