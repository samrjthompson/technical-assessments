package org.example.utils;

import com.github.tomakehurst.wiremock.http.RequestMethod;
import org.springframework.http.HttpMethod;

public class TestUtils {

    private TestUtils() {}

    public static HttpMethod getHttpMethodFromString(final String method) {
        return switch (method) {
            case "GET" -> HttpMethod.GET;
            case "PUT" -> HttpMethod.PUT;
            case "PATCH" -> HttpMethod.PATCH;
            case "POST" -> HttpMethod.POST;
            case "DELETE" -> HttpMethod.DELETE;
            default -> throw new RuntimeException("Http method: [%s] not allowed".formatted(method));
        };
    }

    public static RequestMethod getRequestMethodFromString(final String method) {
        return switch (method) {
            case "GET" -> RequestMethod.GET;
            case "POST" -> RequestMethod.POST;
            case "PATCH" -> RequestMethod.PATCH;
            case "PUT" -> RequestMethod.PUT;
            case "DELETE" -> RequestMethod.DELETE;
            default -> throw new RuntimeException("Request method: [%s] not allowed".formatted(method));
        };
    }
}
