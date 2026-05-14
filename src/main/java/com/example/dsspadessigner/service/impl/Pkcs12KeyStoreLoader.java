package com.example.dsspadessigner.service.impl;

import com.example.dsspadessigner.service.KeyStoreLoader;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.Pkcs12SignatureToken;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;

@Component
public class Pkcs12KeyStoreLoader implements KeyStoreLoader {

    @Override
    public Pkcs12SignatureToken loadToken(String keystorePath, String keystorePassword) throws IOException {
        if (keystorePath == null || keystorePath.isBlank()) {
            throw new IOException("Missing keystorePath");
        }

        Path path = Path.of(keystorePath).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            throw new IOException("Keystore file not found: " + path);
        }
        if (!Files.isRegularFile(path)) {
            throw new IOException("Keystore path is not a file: " + path);
        }
        if (!Files.isReadable(path)) {
            throw new IOException("Keystore file is not readable: " + path);
        }

        KeyStore.PasswordProtection storeProtection = new KeyStore.PasswordProtection(keystorePassword.toCharArray());
        return new Pkcs12SignatureToken(path.toFile(), storeProtection);
    }

    @Override
    public DSSPrivateKeyEntry loadPrivateKey(String keystorePath, String keystorePassword, String keyAlias, String keyPassword) throws IOException {
        KeyStore.PasswordProtection keyProtection = new KeyStore.PasswordProtection(keyPassword.toCharArray());
        try (Pkcs12SignatureToken token = loadToken(keystorePath, keystorePassword)) {
            return token.getKey(keyAlias, keyProtection);
        }
    }
}