package com.example.dsspadessigner.service.impl;

import com.example.dsspadessigner.service.DocumentSigner;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.spi.x509.tsp.TSPSource;
import eu.europa.esig.dss.spi.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.SignatureTokenConnection;
import org.springframework.stereotype.Component;

@Component
public class PadesDocumentSigner implements DocumentSigner {

    @Override
    public DSSDocument signDocument(DSSDocument document, PAdESSignatureParameters parameters,
                                    SignatureTokenConnection token, DSSPrivateKeyEntry privateKey,
                                    TSPSource tspSource) {
        CommonCertificateVerifier certificateVerifier = new CommonCertificateVerifier();
        PAdESService service = new PAdESService(certificateVerifier);

        if (tspSource != null) {
            service.setTspSource(tspSource);
        }

        ToBeSigned dataToSign = service.getDataToSign(document, parameters);
        SignatureValue signatureValue = token.sign(dataToSign, parameters.getDigestAlgorithm(), privateKey);
        return service.signDocument(document, parameters, signatureValue);
    }
}