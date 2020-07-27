package com.dress.shop.domain;

public class ErrorResponse {

    private boolean error;
    private String des;

    public ErrorResponse(boolean error, String des) {
        this.error = error;
        this.des = des;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
