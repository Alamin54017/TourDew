package com.example.tourdew.model;

public class Guide {
    String GuideId,GuideFor,Description,GuideNID;
    int TripCompleted,Balance,price;
    float Rating;

    public Guide() {
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public String getGuideNID() {
        return GuideNID;
    }

    public void setGuideNID(String guideNID) {
        GuideNID = guideNID;
    }

    public String getGuideId() {
        return GuideId;
    }

    public void setGuideId(String guideId) {
        GuideId = guideId;
    }

    public String getGuideFor() {
        return GuideFor;
    }

    public void setGuideFor(String guideFor) {
        GuideFor = guideFor;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getTripCompleted() {
        return TripCompleted;
    }

    public void setTripCompleted(int tripCompleted) {
        TripCompleted = tripCompleted;
    }

    public int getBalance() {
        return Balance;
    }

    public void setBalance(int balance) {
        Balance = balance;
    }
}
