package com.example.dsspadessigner.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizeSignRequest {

    @JsonProperty("CurrentUsers")
    private Integer currentUsers;

    public AuthorizeSignRequest() {
    }

    public AuthorizeSignRequest(Integer currentUsers) {
        this.currentUsers = currentUsers;
    }

    public Integer getCurrentUsers() {
        return currentUsers;
    }

    public void setCurrentUsers(Integer currentUsers) {
        this.currentUsers = currentUsers;
    }
}