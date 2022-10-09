package com.example.tourdew.model;


public class PostTripData {

    String postId,postedBy,placeName,district,startDate,EndDate,totalSeats,price,visitplace,facility,excluded,description,imageUrl;
    long postedAt;
    int BookingCount;
    float Payment;

    public PostTripData() {

    }
    public PostTripData(String postedBy, String placeName, String district, String startDate, String endDate, String totalSeats, String price, String visitplace, String facility, String excluded, String description, String imageUrl, long postedAt) {
        this.postedBy= postedBy;
        this.placeName = placeName;
        this.district = district;
        this.startDate = startDate;
        EndDate = endDate;
        this.totalSeats = totalSeats;
        this.price = price;
        this.visitplace = visitplace;
        this.facility = facility;
        this.excluded = excluded;
        this.description = description;
        this.imageUrl = imageUrl;
        this.postedAt = postedAt;
    }

    public float getPayment() {
        return Payment;
    }

    public void setPayment(float Payment) {
        Payment = Payment;
    }

    public int getBookingCount() {
        return BookingCount;
    }

    public void setBookingCount(int BookingCount) {
        BookingCount = BookingCount;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(String totalSeats) {
        this.totalSeats = totalSeats;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVisitplace() {
        return visitplace;
    }

    public void setVisitplace(String visitplace) {
        this.visitplace = visitplace;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getExcluded() {
        return excluded;
    }

    public void setExcluded(String excluded) {
        this.excluded = excluded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
