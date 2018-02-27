package com.example.demo.user.gateways.database;

import com.example.demo.user.domains.User;

public interface UserGateway {
    User save(User user);
}
