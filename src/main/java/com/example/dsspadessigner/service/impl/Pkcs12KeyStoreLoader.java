package com.example.dsspadessigner.service.impl;

import com.example.dsspadessigner.service.KeyStoreLoader;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.Pkcs12SignatureToken;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;

@Component
public class Pkcs12KeyStoreLoader implements KeyStoreLoader {

    @Override
    public Pkcs12SignatureToken loadToken(String keystorePath, String keystorePassword) throws IOException {
        KeyStore.PasswordProtection storeProtection = new KeyStore.PasswordProtection(keystorePassword.toCharArray());
        return new Pkcs12SignatureToken(new File(keystorePath), storeProtection);
    }

    @Override
    public DSSPrivateKeyEntry loadPrivateKey(String keystorePath, String keystorePassword, String keyAlias, String keyPassword) throws IOException {
        KeyStore.PasswordProtection keyProtection = new KeyStore.PasswordProtection(keyPassword.toCharArray());
        try (Pkcs12SignatureToken token = loadToken(keystorePath, keystorePassword)) {
            return token.getKey(keyAlias, keyProtection);
        }
    }
}