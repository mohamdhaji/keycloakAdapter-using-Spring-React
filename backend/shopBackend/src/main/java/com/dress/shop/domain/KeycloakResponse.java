package com.dress.shop.domain;

public class KeycloakResponse {
   private boolean success;

   private String des;

    public KeycloakResponse(boolean success, String des) {
        this.success = success;
        this.des = des;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
