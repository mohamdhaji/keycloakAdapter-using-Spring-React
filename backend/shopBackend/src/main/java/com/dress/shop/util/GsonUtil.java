package com.dress.shop.util;

import com.google.gson.Gson;

public class GsonUtil {
    public static String simpleJson(String s) {
        Gson gson = new Gson();
        String json = gson.toJson(s);
        System.out.println(json);
        return json;
    }
}