package org.example;

import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@Suite
@SelectClasspathResource("features")
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RunnerIT {

    public static final int WIRE_MOCK_PORT = 8888;

    @DynamicPropertySource
    public static void props(DynamicPropertyRegistry registry) {
        // This is required to get WireMock working
        final String wireMockBasePath = "http://localhost:%d".formatted(WIRE_MOCK_PORT);
        registry.add("api.base-path", () -> wireMockBasePath);
    }
}
