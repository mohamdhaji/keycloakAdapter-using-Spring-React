package com.dress.shop.controllers;

import com.dress.shop.domain.Type;
import com.dress.shop.services.KeyCloakService;
import com.dress.shop.services.MapValidationErrorService;
import com.dress.shop.services.TypeService;
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
@RequestMapping(value = "/api/type")
@CrossOrigin
public class TypeController {

    @Autowired
    TypeService typeService;

    @Autowired
    MapValidationErrorService mapValidationErrorService;

    @Autowired
    KeyCloakService keyCloakService;

    @PostMapping("")
    public ResponseEntity<?> createNewType(HttpServletRequest request, @Valid @RequestBody Type type, BindingResult result){

            ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
            if (errorMap != null) return errorMap;
        String role="admin";

        if (keyCloakService.authenticate(request,role).equals(AuthOutcome.AUTHENTICATED)) {


            Type t = typeService.saveOrUpdateType(type);

            return new ResponseEntity<Type>(t, HttpStatus.CREATED);
        }else {
            return new ResponseEntity("Hi!, you are NOT auhorized !", HttpStatus.UNAUTHORIZED);

        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getTypes(){

        return new ResponseEntity(typeService.getTypes(),HttpStatus.OK);

    }
}
