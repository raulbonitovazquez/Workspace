package com.example.dsspadessigner.service;

import eu.europa.esig.dss.spi.x509.tsp.TSPSource;

public interface TspSourceProvider {
    TSPSource getTspSource(String url, String username, String password);
}