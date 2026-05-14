package com.example.dsspadessigner.service;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.spi.x509.tsp.TSPSource;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.SignatureTokenConnection;

public interface DocumentSigner {
    DSSDocument signDocument(DSSDocument document, PAdESSignatureParameters parameters,
                             SignatureTokenConnection token, DSSPrivateKeyEntry privateKey,
                             TSPSource tspSource);
}