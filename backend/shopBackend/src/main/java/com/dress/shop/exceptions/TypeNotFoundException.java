package com.dress.shop.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TypeNotFoundException extends RuntimeException {
    public TypeNotFoundException(String message) {
        super(message);
    }
}
