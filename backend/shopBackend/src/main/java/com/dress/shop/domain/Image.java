package com.dress.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Image {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "publicId is required")
    private String public_id;

    @NotBlank(message = "url is required")
    private String url;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="product_id", updatable = false, nullable = false)
    @JsonIgnore
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublic_id() {
        return public_id;
    }

    public void setPublic_id(String public_id) {
        this.public_id = public_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
