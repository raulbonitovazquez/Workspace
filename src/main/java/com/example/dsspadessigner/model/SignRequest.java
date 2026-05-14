package com.example.dsspadessigner.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class SignRequest {
    private String pdfBase64;

    @JsonAlias({"CurrentUsers", "currentUsers"})
    private Integer currentUsers;
    private String keystorePath;
    private String keystorePassword;
    private String keyPassword; // optional, defaults to keystorePassword
    private String tspUrl;
    private String tspUsername;
    private String tspPassword;
    private String reason;
    private String location;
    private boolean addVisualSignature;
    private String visualText;
    private int visualX;
    private int visualY;
    private int visualWidth;
    private int visualHeight;

    // Getters and setters
    public String getPdfBase64() { return pdfBase64; }
    public void setPdfBase64(String pdfBase64) { this.pdfBase64 = pdfBase64; }

    public Integer getCurrentUsers() { return currentUsers; }
    public void setCurrentUsers(Integer currentUsers) { this.currentUsers = currentUsers; }

    public String getKeystorePath() { return keystorePath; }
    public void setKeystorePath(String keystorePath) { this.keystorePath = keystorePath; }

    public String getKeystorePassword() { return keystorePassword; }
    public void setKeystorePassword(String keystorePassword) { this.keystorePassword = keystorePassword; }

    public String getKeyPassword() { return keyPassword; }
    public void setKeyPassword(String keyPassword) { this.keyPassword = keyPassword; }

    /** Returns keyPassword if set, otherwise falls back to keystorePassword */
    public String getEffectiveKeyPassword() {
        return (keyPassword != null && !keyPassword.isEmpty()) ? keyPassword : keystorePassword;
    }

    public String getTspUrl() { return tspUrl; }
    public void setTspUrl(String tspUrl) { this.tspUrl = tspUrl; }

    public String getTspUsername() { return tspUsername; }
    public void setTspUsername(String tspUsername) { this.tspUsername = tspUsername; }

    public String getTspPassword() { return tspPassword; }
    public void setTspPassword(String tspPassword) { this.tspPassword = tspPassword; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public boolean isAddVisualSignature() { return addVisualSignature; }
    public void setAddVisualSignature(boolean addVisualSignature) { this.addVisualSignature = addVisualSignature; }

    public String getVisualText() { return visualText; }
    public void setVisualText(String visualText) { this.visualText = visualText; }

    public int getVisualX() { return visualX; }
    public void setVisualX(int visualX) { this.visualX = visualX; }

    public int getVisualY() { return visualY; }
    public void setVisualY(int visualY) { this.visualY = visualY; }

    public int getVisualWidth() { return visualWidth; }
    public void setVisualWidth(int visualWidth) { this.visualWidth = visualWidth; }

    public int getVisualHeight() { return visualHeight; }
    public void setVisualHeight(int visualHeight) { this.visualHeight = visualHeight; }
}