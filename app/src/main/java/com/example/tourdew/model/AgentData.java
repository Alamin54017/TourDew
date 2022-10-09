package com.example.tourdew.model;

public class AgentData {
    String AgentName,AgentAddress,AgentPhone,Balance,Rating,AgentNID;

    public AgentData() {
    }

    public AgentData(String agentName, String agentAddress, String agentPhone, String balance, String rating, String agentNID) {
        AgentName = agentName;
        AgentAddress = agentAddress;
        AgentPhone = agentPhone;
        Balance = balance;
        Rating = rating;
        AgentNID = agentNID;
    }

    public String getAgentName() {
        return AgentName;
    }

    public void setAgentName(String agentName) {
        AgentName = agentName;
    }

    public String getAgentAddress() {
        return AgentAddress;
    }

    public void setAgentAddress(String agentAddress) {
        AgentAddress = agentAddress;
    }

    public String getAgentPhone() {
        return AgentPhone;
    }

    public void setAgentPhone(String agentPhone) {
        AgentPhone = agentPhone;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getAgentNID() {
        return AgentNID;
    }

    public void setAgentNID(String agentNID) {
        AgentNID = agentNID;
    }
}
