package com.example.demo.config;

import com.github.fakemongo.Fongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.web.WebAppConfiguration;

@Configuration
@EnableMongoRepositories({"com.example"})
@WebAppConfiguration
@ComponentScan(basePackages = {"com.example"})
public class ApplicationComponentTestConfig {

    private static final String DATABASE_NAME = "integration-tests";

    @Bean
    public MongoTemplate mongoTemplate() {
        final MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }

    public MongoDbFactory mongoDbFactory() {
        final Fongo fongo = new Fongo("mongodb server in memory");
        return new SimpleMongoDbFactory(fongo.getMongo(), DATABASE_NAME);
    }
}
