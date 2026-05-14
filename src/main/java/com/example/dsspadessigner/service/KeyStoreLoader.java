package com.example.dsspadessigner.service;

import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.Pkcs12SignatureToken;

import java.io.IOException;

public interface KeyStoreLoader {
    DSSPrivateKeyEntry loadPrivateKey(String keystorePath, String keystorePassword, String keyAlias, String keyPassword) throws IOException;
    Pkcs12SignatureToken loadToken(String keystorePath, String keystorePassword) throws IOException;
}