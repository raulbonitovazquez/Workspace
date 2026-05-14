package com.example.dsspadessigner.service.impl;

import com.example.dsspadessigner.model.AuthorizeSignRequest;
import com.example.dsspadessigner.model.AuthorizeSignResponse;
import com.example.dsspadessigner.model.SignResponse;
import com.example.dsspadessigner.service.SignAuthorizationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class FirmaOneSignAuthorizationService implements SignAuthorizationService {

    private final RestClient restClient;

    public FirmaOneSignAuthorizationService(
            RestClient.Builder restClientBuilder,
            @Value("${firma-one.authorization-url}") String authorizationUrl) {
        this.restClient = restClientBuilder.baseUrl(authorizationUrl).build();
    }

    @Override
    public SignResponse authorize(String apiKey, Integer currentUsers) {
        try {
            AuthorizeSignResponse response = restClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-API-KEY", apiKey)
                    .body(new AuthorizeSignRequest(currentUsers))
                    .retrieve()
                    .body(AuthorizeSignResponse.class);

            if (response != null && response.isAuthorized()) {
                return new SignResponse("SUCCESS", response.getResolvedMessage(), null);
            }

            String message = response != null ? response.getResolvedMessage() : "Empty authorization response";
            return new SignResponse("ERROR", message, null);
        } catch (RestClientException e) {
            return new SignResponse("ERROR", "Authorization service error: " + e.getMessage(), null);
        }
    }
}