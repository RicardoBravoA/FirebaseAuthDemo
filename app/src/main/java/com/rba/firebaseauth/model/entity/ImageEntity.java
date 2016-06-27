package com.rba.firebaseauth.model.entity;

/**
 * Created by Ricardo Bravo on 26/06/16.
 */

public class ImageEntity {

    private String image, description;

    public ImageEntity(String image, String description) {
        this.image = image;
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

}
