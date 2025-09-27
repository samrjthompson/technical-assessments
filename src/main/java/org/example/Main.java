package org.example;

import org.example.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class Main {

    public static final String NAMESPACE = "my-application";

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}