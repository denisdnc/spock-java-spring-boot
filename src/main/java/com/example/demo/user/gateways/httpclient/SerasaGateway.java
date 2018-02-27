package com.example.demo.user.gateways.httpclient;

import com.example.demo.user.domains.SerasaWrapper;

public interface SerasaGateway {

    SerasaWrapper find(String document);
}
