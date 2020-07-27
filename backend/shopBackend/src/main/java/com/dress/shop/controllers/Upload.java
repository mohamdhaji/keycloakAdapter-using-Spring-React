package com.dress.shop.controllers;

import com.dress.shop.domain.CloudinaryResponse;
import com.dress.shop.services.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/uploadimage")
@CrossOrigin
public class Upload {

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {

        return new ResponseEntity<>( cloudinaryService.uploadFile(file), HttpStatus.OK);
    }
}
