package com.example.dsspadessigner.service.impl;

import com.example.dsspadessigner.model.SignRequest;
import com.example.dsspadessigner.model.SignResponse;
import com.example.dsspadessigner.service.*;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.spi.x509.tsp.TSPSource;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.Pkcs12SignatureToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class PadesSigningServiceImpl implements SigningService {

    @Autowired
    private KeyStoreLoader keyStoreLoader;

    @Autowired
    private SignatureParametersBuilder parametersBuilder;

    @Autowired
    private DocumentSigner documentSigner;

    @Autowired
    private TspSourceProvider tspSourceProvider;

    @Override
    public SignResponse sign(SignRequest request, SignatureLevel level) {
        try {
            byte[] pdfBytes = Base64.getDecoder().decode(request.getPdfBase64());
            DSSDocument toSignDocument = new InMemoryDocument(pdfBytes);

            try (Pkcs12SignatureToken token = keyStoreLoader.loadToken(
                    request.getKeystorePath(), request.getKeystorePassword())) {

                // Always use first available key — no alias needed
                DSSPrivateKeyEntry privateKey = token.getKeys().get(0);

                PAdESSignatureParameters parameters = parametersBuilder.build(request, privateKey, level);

                TSPSource tspSource = null;
                if (request.getTspUrl() != null && !request.getTspUrl().isEmpty()) {
                    tspSource = tspSourceProvider.getTspSource(
                            request.getTspUrl(), request.getTspUsername(), request.getTspPassword());
                }

                DSSDocument signedDocument = documentSigner.signDocument(
                        toSignDocument, parameters, token, privateKey, tspSource);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                signedDocument.writeTo(baos);
                String signedPdfBase64 = Base64.getEncoder().encodeToString(baos.toByteArray());

                return new SignResponse("SUCCESS", "Document signed successfully", signedPdfBase64);
            }
        } catch (Exception e) {
            return new SignResponse("ERROR", e.getMessage(), null);
        }
    }
}