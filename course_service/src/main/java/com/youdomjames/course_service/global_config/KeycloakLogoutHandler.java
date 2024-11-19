package com.youdomjames.course_service.global_config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author youdomjames
 * @project smart_academy_api
 * @createdOn 2024-03-06
 * <p>
 * Copyright 2024 James Youdom
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakLogoutHandler implements LogoutHandler {
    private final RestTemplate restTemplate;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication auth) {
        logoutFromKeycloak((OidcUser) auth.getPrincipal());
    }

    private void logoutFromKeycloak(OidcUser user) {
        String endSessionEndpoint = user.getIssuer() + "/protocol/openid-connect/logout?id_token_hint="+user.getIdToken().getTokenValue();
//        UriBuilder builder = UriComponentsBuilder.fromHttpUrl(endSessionEndpoint).queryParam("id_token_hint", user.getIdToken().getTokenValue());
//        UriComponentsBuilder builder = UriComponentsBuilder
//                .fromUriString(endSessionEndpoint)
//                .queryParam("id_token_hint", user.getIdToken().getTokenValue());

        ResponseEntity<String> logoutResponse = restTemplate.getForEntity(
                endSessionEndpoint, String.class);
        if (logoutResponse.getStatusCode().is2xxSuccessful()) {
            log.info("Successfully logged out from Keycloak");
        } else {
            log.error("Could not propagate logout to Keycloak");
        }
    }
}
