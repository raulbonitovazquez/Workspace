package com.example.dsspadessigner.util;

import eu.europa.esig.dss.token.PasswordInputCallback;

public class KeyStorePasswordCallback implements PasswordInputCallback {
    private final char[] password;

    public KeyStorePasswordCallback(String password) {
        this.password = password.toCharArray();
    }

    @Override
    public char[] getPassword() {
        return password;
    }
}