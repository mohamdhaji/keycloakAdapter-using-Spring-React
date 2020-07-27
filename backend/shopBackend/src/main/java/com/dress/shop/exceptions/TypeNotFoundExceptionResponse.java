package com.dress.shop.exceptions;

public class TypeNotFoundExceptionResponse {

    private String TypetNotFound;

    public TypeNotFoundExceptionResponse(String typeNotFound) {
        TypetNotFound = typeNotFound;
    }

    public String getProductNotFound() {
        return TypetNotFound;
    }

    public void setProductNotFound(String typeNotFound) {
        TypetNotFound = typeNotFound;
    }
}
