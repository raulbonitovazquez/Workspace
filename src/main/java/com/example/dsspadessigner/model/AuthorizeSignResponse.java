package com.example.dsspadessigner.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class AuthorizeSignResponse {

    @JsonAlias({"IsSuccess", "isSuccess"})
    private Boolean isSuccess;

    @JsonAlias({"AuthorizeSignResponse", "authorizeSignResponse", "Result", "result"})
    private AuthorizeSignResult authorizeSignResponse;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public AuthorizeSignResult getAuthorizeSignResponse() {
        return authorizeSignResponse;
    }

    public void setAuthorizeSignResponse(AuthorizeSignResult authorizeSignResponse) {
        this.authorizeSignResponse = authorizeSignResponse;
    }

    public boolean isAuthorized() {
        if (authorizeSignResponse == null || !Boolean.TRUE.equals(authorizeSignResponse.getSuccess())) {
            return false;
        }
        return isSuccess == null || Boolean.TRUE.equals(isSuccess);
    }

    public String getResolvedMessage() {
        if (authorizeSignResponse != null && authorizeSignResponse.getMessage() != null) {
            return authorizeSignResponse.getMessage();
        }
        return Boolean.FALSE.equals(isSuccess) ? "Authorization rejected" : "Authorized";
    }
}