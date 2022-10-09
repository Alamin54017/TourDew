package com.example.tourdew.model;

public class GuiderModel {

    String personName,places,price;
    int imageUrl;
    float rating;

    public GuiderModel(String personName, String places, String price, int imageUrl, float rating) {
        this.personName = personName;
        this.places = places;
        this.price = price;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
