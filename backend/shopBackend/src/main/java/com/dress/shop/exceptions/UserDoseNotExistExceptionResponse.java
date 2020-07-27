package com.dress.shop.exceptions;

public class UserDoseNotExistExceptionResponse {

    private String error;

    public UserDoseNotExistExceptionResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
