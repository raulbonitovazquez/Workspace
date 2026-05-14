package com.example.dsspadessigner.service.impl;

import com.example.dsspadessigner.service.TspSourceProvider;
import eu.europa.esig.dss.service.tsp.OnlineTSPSource;
import eu.europa.esig.dss.spi.x509.tsp.TSPSource;
import org.springframework.stereotype.Component;

@Component
public class OnlineTspSourceProvider implements TspSourceProvider {

    @Override
    public TSPSource getTspSource(String url, String username, String password) {
        OnlineTSPSource tspSource = new OnlineTSPSource(url);
        // Note: DSS 6.1 handles authentication differently
        // Basic authentication is now typically handled via URL or other means
        return tspSource;
    }
}