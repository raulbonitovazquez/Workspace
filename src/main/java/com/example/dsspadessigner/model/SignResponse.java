package com.example.dsspadessigner.model;

public class SignResponse {
    private String status;
    private String message;
    private String signedPdfBase64;

    public SignResponse(String status, String message, String signedPdfBase64) {
        this.status = status;
        this.message = message;
        this.signedPdfBase64 = signedPdfBase64;
    }

    // Getters and setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSignedPdfBase64() { return signedPdfBase64; }
    public void setSignedPdfBase64(String signedPdfBase64) { this.signedPdfBase64 = signedPdfBase64; }
}