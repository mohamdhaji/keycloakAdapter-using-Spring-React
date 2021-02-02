package com.dress.shop.services;

public final class View {
    // show only public data
    public interface Public {}

    // show public and internal data
    public interface Internal extends Public {}
}