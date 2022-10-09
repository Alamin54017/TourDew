package com.example.tourdew.model;

public class homemodel {

    String placeName,agency,price,rating;
    Integer imageUrl;

    public homemodel(String placeName, String agency, String price, String rating,Integer imageUrl) {
        this.placeName = placeName;
        this.agency = agency;
        this.price = price;
        this.rating = rating;
        this.imageUrl=imageUrl;
    }

    public Integer getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Integer imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
