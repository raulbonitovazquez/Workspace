package com.example.dsspadessigner.service;

import com.example.dsspadessigner.model.SignResponse;

public interface SignAuthorizationService {
    SignResponse authorize(String apiKey, Integer currentUsers);
}