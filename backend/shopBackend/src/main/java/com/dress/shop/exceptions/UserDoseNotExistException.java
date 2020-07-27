package com.dress.shop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserDoseNotExistException extends RuntimeException {

    public UserDoseNotExistException(String message) {
        super(message);
    }
}
