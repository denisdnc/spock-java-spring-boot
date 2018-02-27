package com.example.demo.user.gateways.httpclient;

import com.example.demo.user.domains.SerasaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SerasaGatewayImpl implements SerasaGateway {

    private SerasaHttpClient serasaHttpClient;

    @Autowired
    public SerasaGatewayImpl(SerasaHttpClient serasaHttpClient) {
        this.serasaHttpClient = serasaHttpClient;
    }

    public SerasaWrapper find(String document) {
        return serasaHttpClient.getStatus(document);
    }
}
