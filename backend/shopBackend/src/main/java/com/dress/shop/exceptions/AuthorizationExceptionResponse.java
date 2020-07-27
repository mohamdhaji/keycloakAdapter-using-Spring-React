package com.dress.shop.exceptions;

public class AuthorizationExceptionResponse {

    private String authError;

    public AuthorizationExceptionResponse(String authError) {
        this.authError = authError;
    }

    public String getAuthError() {
        return authError;
    }

    public void setAuthError(String authError) {
        this.authError = authError;
    }
}
