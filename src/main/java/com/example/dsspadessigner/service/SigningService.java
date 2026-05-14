package com.example.dsspadessigner.service;

import com.example.dsspadessigner.model.SignRequest;
import com.example.dsspadessigner.model.SignResponse;
import eu.europa.esig.dss.enumerations.SignatureLevel;

public interface SigningService {
    SignResponse sign(SignRequest request, SignatureLevel level);
}