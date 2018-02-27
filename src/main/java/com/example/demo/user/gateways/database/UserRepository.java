package com.example.demo.user.gateways.database;

import com.example.demo.user.domains.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
