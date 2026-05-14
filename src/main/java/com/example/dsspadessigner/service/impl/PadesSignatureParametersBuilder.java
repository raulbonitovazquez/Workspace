package com.example.dsspadessigner.service.impl;

import com.example.dsspadessigner.model.SignRequest;
import com.example.dsspadessigner.service.SignatureParametersBuilder;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.SignatureFieldParameters;
import eu.europa.esig.dss.pades.SignatureImageParameters;
import eu.europa.esig.dss.pades.SignatureImageTextParameters;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import org.springframework.stereotype.Component;

@Component
public class PadesSignatureParametersBuilder implements SignatureParametersBuilder {

    @Override
    public PAdESSignatureParameters build(SignRequest request, DSSPrivateKeyEntry privateKey, SignatureLevel level) {
        PAdESSignatureParameters parameters = new PAdESSignatureParameters();
        parameters.setSignatureLevel(level);
        parameters.setSigningCertificate(privateKey.getCertificate());
        parameters.setCertificateChain(privateKey.getCertificateChain());

        if (request.getReason() != null) {
            parameters.setReason(request.getReason());
        }
        if (request.getLocation() != null) {
            parameters.setLocation(request.getLocation());
        }

        configureVisualSignature(request, parameters);

        return parameters;
    }

    private void configureVisualSignature(SignRequest request, PAdESSignatureParameters parameters) {
        if (request.isAddVisualSignature()) {
            SignatureImageParameters imageParameters = new SignatureImageParameters();
            SignatureImageTextParameters textParameters = new SignatureImageTextParameters();
            textParameters.setText(request.getVisualText() != null && !request.getVisualText().isEmpty() ? request.getVisualText() : "Digitally Signed");
            imageParameters.setTextParameters(textParameters);

            SignatureFieldParameters fieldParameters = new SignatureFieldParameters();
            fieldParameters.setOriginX(request.getVisualX() > 0 ? request.getVisualX() : 100);
            fieldParameters.setOriginY(request.getVisualY() > 0 ? request.getVisualY() : 100);
            fieldParameters.setWidth(request.getVisualWidth() > 0 ? request.getVisualWidth() : 200);
            fieldParameters.setHeight(request.getVisualHeight() > 0 ? request.getVisualHeight() : 50);
            imageParameters.setFieldParameters(fieldParameters);

            parameters.setImageParameters(imageParameters);
        }
    }
}