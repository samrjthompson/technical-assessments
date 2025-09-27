package org.example.steps;

import static org.example.config.CucumberContext.CONTEXT;
import static org.example.config.WireMockTestConfig.getServeEvents;
import static org.example.config.WireMockTestConfig.setupWireMock;
import static org.example.config.WireMockTestConfig.stubGet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.example.utils.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class MainSteps {

    private static final String HEADERS_JSON_PATH = "/json/headers/%s.json";
    private static final String REQUEST_BODY_JSON_PATH = "/json/requests/%s.json";
    private static final String RESPONSE_BODY_JSON_PATH = "/json/responses/%s.json";
    private static final String STATUS_CODE_CONTEXT = "status_code";
    private static final String RESPONSE_BODY_CONTEXT = "response_body";
    private static final String RESPONSE_HEADERS_CONTEXT = "response_headers";

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void beforeEachScenario() {
        CONTEXT.clear();
        setupWireMock();
    }

    @Given("the app is running")
    public void theAppIsRunning() {
        assertNotNull(testRestTemplate);
    }

    @And("a {int} is returned from the external app")
    public void stub(final int statusCode) {
        stubGet("/external-endpoint", statusCode);
    }

    @When("a {string} request is sent to {string} with a request body of {string} with headers {string}")
    public void sendHttpRequest(final String requestType, final String endpoint, final String requestBodyFileName,
                                final String headersFileName)
            throws Exception {
        // Http Method
        HttpMethod httpMethod = TestUtils.getHttpMethodFromString(requestType);

        // Headers
        final String headersAsJson = IOUtils.resourceToString(HEADERS_JSON_PATH.formatted(headersFileName),
                StandardCharsets.UTF_8);
        Map<String, String> headersAsMap = objectMapper.readValue(headersAsJson, new TypeReference<>() {
        });
        HttpHeaders headers = new HttpHeaders();
        headersAsMap.forEach(headers::add);

        // Request Body
        String requestBody;
        if ("empty".equals(requestBodyFileName)) {
            requestBody = null;
        } else {
            requestBody = IOUtils.resourceToString(REQUEST_BODY_JSON_PATH.formatted(requestBodyFileName),
                    StandardCharsets.UTF_8);
        }
        HttpEntity<?> request = new HttpEntity<>(requestBody, headers);

        // Response
        ResponseEntity<?> response = testRestTemplate.exchange(endpoint, httpMethod, request, Object.class);
        CONTEXT.set(STATUS_CODE_CONTEXT, response.getStatusCode().value());
        CONTEXT.set(RESPONSE_BODY_CONTEXT, response.getBody());
        CONTEXT.set(RESPONSE_HEADERS_CONTEXT, response.getHeaders());
    }

    @Then("a {int} response is returned")
    public void assertResponseCode(final int code) {
        assertEquals(code, (int) CONTEXT.get(STATUS_CODE_CONTEXT));
    }

    @And("a {string} request was made to the external server")
    public void verifyRequestWasMade(final String httpMethod) {
        RequestMethod requestmethod = TestUtils.getRequestMethodFromString(httpMethod);

        ServeEvent serveEvent = getServeEvents().getFirst();
        assertEquals("http://localhost:8888/external-endpoint", serveEvent.getRequest().getAbsoluteUrl());
        assertEquals(requestmethod, serveEvent.getRequest().getMethod());
    }
}
