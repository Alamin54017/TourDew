package com.example.tourdew.model;

public class UserDetails {
    String Name,Phone,Gender,ProfilePic,Balance,Address,imageUrl,UserId;
    Boolean agentVerified,guiderVerified;

    public UserDetails() {
    }

    public UserDetails(String name, String phone, String gender, Boolean agentVerified, Boolean guiderVerified) {
        this.Name = name;
        this.Phone = phone;
        this.Gender = gender;
        this.agentVerified = agentVerified;
        this.guiderVerified = guiderVerified;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public Boolean getAgentVerified() {
        return agentVerified;
    }

    public void setAgentVerified(Boolean agentVerified) {
        this.agentVerified = agentVerified;
    }

    public Boolean getGuiderVerified() {
        return guiderVerified;
    }

    public void setGuiderVerified(Boolean guiderVerified) {
        this.guiderVerified = guiderVerified;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }
}
