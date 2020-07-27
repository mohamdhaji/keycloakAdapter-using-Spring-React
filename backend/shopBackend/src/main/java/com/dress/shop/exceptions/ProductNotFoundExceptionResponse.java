package com.dress.shop.exceptions;

public class ProductNotFoundExceptionResponse {

    private String ProductNotFound;

    public ProductNotFoundExceptionResponse(String productNotFound) {
        ProductNotFound = productNotFound;
    }

    public String getProductNotFound() {
        return ProductNotFound;
    }

    public void setProductNotFound(String productNotFound) {
        ProductNotFound = productNotFound;
    }
}
