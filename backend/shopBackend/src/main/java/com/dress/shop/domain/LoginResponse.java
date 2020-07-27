package com.dress.shop.domain;

public class LoginResponse {

    String access_token;
    String refresh_token;
    Long expires_in;
    Long refresh_expires_in;
    String token_type;
    String role;
    String isAuth;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public Long getRefresh_expires_in() {
        return refresh_expires_in;
    }

    public void setRefresh_expires_in(Long refresh_expires_in) {
        this.refresh_expires_in = refresh_expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(String isAuth) {
        this.isAuth = isAuth;
    }
}
