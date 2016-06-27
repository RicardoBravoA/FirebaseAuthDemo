package com.rba.firebaseauth.model.entity;

/**
 * Created by Ricardo Bravo on 26/06/16.
 */

public class ProductEntity {

    private String description;
    private String price;
    private String image;

    public ProductEntity() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
