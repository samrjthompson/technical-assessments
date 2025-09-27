package org.example.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Disabled // Disabled due to lack of working mongo config
@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
class ControllerIT {

    @Value("${spring.data.mongodb.database}")
    private String collectionName;
    private static final String HEALTHCHECK_ENDPOINT = "/healthcheck";

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0.3");

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(collectionName);
        mongoTemplate.createCollection(collectionName);
    }

    @Test
    void healthcheck() throws Exception {
        // given

        // when
        ResultActions result = mockMvc.perform(get(HEALTHCHECK_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
