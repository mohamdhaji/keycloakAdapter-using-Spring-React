package com.dress.shop.domain;

import java.util.ArrayList;
import java.util.List;

public class Filters {

    List<Integer> type =new ArrayList<>();
    List<Integer> price=new ArrayList<>();

    public List<Integer> getType() {
        return type;
    }

    public void setType(List<Integer> type) {
        this.type = type;
    }

    public List<Integer> getPrice() {
        return price;
    }

    public void setPrice(List<Integer> price) {
        this.price = price;
    }
}
