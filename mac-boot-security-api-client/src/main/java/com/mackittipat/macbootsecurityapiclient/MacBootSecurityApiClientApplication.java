package com.mackittipat.macbootsecurityapiclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.client.RestTemplate;

@Slf4j
@SpringBootApplication
public class MacBootSecurityApiClientApplication implements CommandLineRunner {

    @Autowired
    private OAuth2AuthorizedClientManager authorizedClientManager;

    @Autowired
    private RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(MacBootSecurityApiClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("keycloak")
                .principal("mac-realm")
                .build();
        OAuth2AuthorizedClient authorizedClient = this.authorizedClientManager.authorize(authorizeRequest);

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        log.info("Access Token = {}", accessToken.getTokenValue());

        restTemplate.getInterceptors().add(
                (HttpRequest request, byte[] body, ClientHttpRequestExecution execution) -> {
                    request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getTokenValue());
                    return execution.execute(request, body);
                });
        String response = restTemplate.getForObject("http://localhost:8081/products", String.class);
        log.info("Response = {}", response);
    }
}
