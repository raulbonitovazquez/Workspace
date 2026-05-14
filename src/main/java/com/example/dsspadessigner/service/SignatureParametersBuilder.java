package com.example.dsspadessigner.service;

import com.example.dsspadessigner.model.SignRequest;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;

public interface SignatureParametersBuilder {
    PAdESSignatureParameters build(SignRequest request, DSSPrivateKeyEntry privateKey, SignatureLevel level);
}