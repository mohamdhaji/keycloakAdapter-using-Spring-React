package com.dress.shop.controllers;


import com.dress.shop.domain.Site;
import com.dress.shop.repositories.SiteRepository;
import com.dress.shop.services.KeyCloakService;
import com.dress.shop.services.MapValidationErrorService;
import com.dress.shop.services.SiteService;
import org.keycloak.adapters.spi.AuthOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/site")
@CrossOrigin
public class SiteController {

    @Autowired
    SiteService siteService;

    @Autowired
    MapValidationErrorService mapValidationErrorService;

    @Autowired
    KeyCloakService keyCloakService;

    @GetMapping
    public ResponseEntity<?> getSiteData(){
        return new ResponseEntity<Site>(siteService.getSiteData(), HttpStatus.OK);

    }
    @PostMapping
    public ResponseEntity<?> updateSiteData(@Valid @RequestBody Site site, BindingResult result, HttpServletRequest request){

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if (errorMap != null) return errorMap;

        String role="admin";
        if (keyCloakService.authenticate(request,role).equals(AuthOutcome.AUTHENTICATED)) {

            return new ResponseEntity<>(siteService.updateSiteData(site),HttpStatus.OK);
        }else {
            return new ResponseEntity("Hi!, you are NOT auhorized !", HttpStatus.UNAUTHORIZED);

        }

    }
}
