package com.pensionat.client;

import com.pensionat.exception.NotFoundException;
import com.pensionat.exception.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class CustomerClient {

    private final RestClient restClient;

    public CustomerClient(@Value("${customer.service.url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public boolean customerExists(Long customerId) {
        try {
            restClient.get()
                    .uri("/api/customers/{id}", customerId)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, resp) -> {
                        throw new NotFoundException("Customer not found");
                    })
                    .toBodilessEntity();
            return true;
        } catch (NotFoundException e) {
            return false;
        } catch (RestClientException e) {
            throw new ServiceUnavailableException(
                    "Could not reach customer service. Please try again later."
            );
        }
    }
}