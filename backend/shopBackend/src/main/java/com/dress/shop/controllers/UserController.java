package com.dress.shop.controllers;

import com.dress.shop.domain.Product;
import com.dress.shop.domain.SuccessResponse;
import com.dress.shop.services.KeyCloakService;
import com.dress.shop.services.UserService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.spi.AuthOutcome;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/user")
@CrossOrigin
public class UserController {

    @Autowired
    KeyCloakService keyCloakService;

    @Autowired
    UserService userService;

//    @RequestMapping(value = "/logout", method = RequestMethod.GET)
//    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
//
//        request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//
//        AccessToken token = ((KeycloakPrincipal<?>) request.getUserPrincipal()).getKeycloakSecurityContext().getToken();
//
//        String userId = token.getSubject();
//
//        keyCloakService.logoutUser(userId);
//
//        return new ResponseEntity<>("Hi!, you have logged out successfully!", HttpStatus.OK);
//
//    }

    @RequestMapping(value = "/update/password", method = RequestMethod.GET)
    public ResponseEntity<?> updatePassword(HttpServletRequest request, String newPassword) {

        request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        AccessToken token = ((KeycloakPrincipal<?>) request.getUserPrincipal()).getKeycloakSecurityContext().getToken();

        String userId = token.getSubject();

        keyCloakService.resetPassword(newPassword, userId);

        return new ResponseEntity<>("Hi!, your password has been successfully updated!", HttpStatus.OK);

    }
    @GetMapping("/cart/{uemail}")
    public ResponseEntity<?> getUserCart(HttpServletRequest request,@PathVariable("uemail") String uemail){

        String role="user";
        if (keyCloakService.authenticate(request,role).equals(AuthOutcome.AUTHENTICATED)) {

            return new ResponseEntity<>(userService.getUserCart(uemail),HttpStatus.OK);
        }else {
            return new ResponseEntity("Hi!, you are NOT auhorized !", HttpStatus.UNAUTHORIZED);

        }

    }

    @PostMapping("/cart/{uemail}/{productid}")
    public ResponseEntity<?> addProductToCart(HttpServletRequest request,@PathVariable("uemail") String uemail,@PathVariable("productid") String productid){

        String role="user";
        if (keyCloakService.authenticate(request,role).equals(AuthOutcome.AUTHENTICATED)) {

        SuccessResponse successResponse=new SuccessResponse(false);

        if(userService.addProductToCart(productid,uemail) != null)
            successResponse.setSuccess(true);

        return new ResponseEntity<>(successResponse  ,HttpStatus.OK);
        }else {
            return new ResponseEntity("Hi!, you are NOT auhorized !", HttpStatus.UNAUTHORIZED);

        }
    }

    @DeleteMapping("/cart/{uemail}/{productid}")
    public ResponseEntity<?> deleteProductFromCart(HttpServletRequest request,@PathVariable("uemail") String uemail ,@PathVariable("productid") String productid){

        return new ResponseEntity<>(userService.deleteProductFromCart(uemail,productid),HttpStatus.OK);
    }

}
