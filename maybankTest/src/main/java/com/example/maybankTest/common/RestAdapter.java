package com.example.maybankTest.common;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RestAdapter {
    private final WebClient webClient;

    public RestAdapter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public WebClient.ResponseSpec getRequest(String url) {
        return webClient
                .get()
                .uri(url)
                .retrieve();
    }
}
