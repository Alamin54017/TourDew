package com.example.tourdew.model;

public class AgentModel1 {
    String PlaceName,StartDate,BookedSeats,Payment;
    int imageUrl;

    public AgentModel1(String placeName, String startDate, String bookedSeats, String payment, int imageUrl) {
        PlaceName = placeName;
        StartDate = startDate;
        BookedSeats = bookedSeats;
        Payment = payment;
        this.imageUrl = imageUrl;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getBookedSeats() {
        return BookedSeats;
    }

    public void setBookedSeats(String bookedSeats) {
        BookedSeats = bookedSeats;
    }

    public String getPayment() {
        return Payment;
    }

    public void setPayment(String payment) {
        Payment = payment;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }
}
