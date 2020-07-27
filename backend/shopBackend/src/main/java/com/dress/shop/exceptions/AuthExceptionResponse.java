package com.dress.shop.exceptions;

public class AuthExceptionResponse {

    private String atuhError;

    public AuthExceptionResponse(String atuhError) {
        this.atuhError = atuhError;
    }

    public String getAtuhError() {
        return atuhError;
    }

    public void setAtuhError(String atuhError) {
        this.atuhError = atuhError;
    }
}
