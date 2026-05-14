package com.example.dsspadessigner.controller;

import com.example.dsspadessigner.model.SignRequest;
import com.example.dsspadessigner.model.SignResponse;
import com.example.dsspadessigner.service.SignAuthorizationService;
import com.example.dsspadessigner.service.SigningService;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PadesSigningController {

    @Autowired
    private SigningService signingService;

    @Autowired
    private SignAuthorizationService signAuthorizationService;

    @PostMapping("/sign-pades-b")
    public ResponseEntity<SignResponse> signPadesB(
            @RequestHeader(name = "X-API-KEY", required = false) String apiKey,
            @RequestBody SignRequest request) {
        return doSign(apiKey, request, SignatureLevel.PAdES_BASELINE_B);
    }

    @PostMapping("/sign-pades-t")
    public ResponseEntity<SignResponse> signPadesT(
            @RequestHeader(name = "X-API-KEY", required = false) String apiKey,
            @RequestBody SignRequest request) {
        return doSign(apiKey, request, SignatureLevel.PAdES_BASELINE_T);
    }

    @PostMapping("/sign-pades-lt")
    public ResponseEntity<SignResponse> signPadesLt(
            @RequestHeader(name = "X-API-KEY", required = false) String apiKey,
            @RequestBody SignRequest request) {
        return doSign(apiKey, request, SignatureLevel.PAdES_BASELINE_LT);
    }

    @PostMapping("/sign-pades-lta")
    public ResponseEntity<SignResponse> signPadesLta(
            @RequestHeader(name = "X-API-KEY", required = false) String apiKey,
            @RequestBody SignRequest request) {
        return doSign(apiKey, request, SignatureLevel.PAdES_BASELINE_LTA);
    }

    private ResponseEntity<SignResponse> doSign(String apiKey, SignRequest request, SignatureLevel level) {
        try {
            if (apiKey == null || apiKey.isBlank()) {
                return ResponseEntity.badRequest().body(new SignResponse("ERROR", "Missing X-API-KEY header", null));
            }
            if (request.getCurrentUsers() == null || request.getCurrentUsers() < 0) {
                return ResponseEntity.badRequest().body(new SignResponse("ERROR", "Missing currentUsers in request body", null));
            }

            SignResponse authorizationResponse = signAuthorizationService.authorize(apiKey, request.getCurrentUsers());
            if (!"SUCCESS".equalsIgnoreCase(authorizationResponse.getStatus())) {
                return ResponseEntity.status(403).body(authorizationResponse);
            }

            SignResponse response = signingService.sign(request, level);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new SignResponse("ERROR", e.getMessage(), null));
        }
    }
}