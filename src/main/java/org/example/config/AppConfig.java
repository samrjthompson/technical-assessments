package org.example.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.text.SimpleDateFormat;
import java.util.function.Supplier;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    private final AppProperties appProperties;
    private final String basePath;

    public AppConfig(AppProperties appProperties, @Value("${api.base-path}") String basePath) {
        this.appProperties = appProperties;
        this.basePath = basePath;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean
    public Supplier<WebClient> webClientSupplier() {
        return () -> WebClient.builder()
                .baseUrl(basePath)
                .defaultHeaders(httpHeaders -> httpHeaders.addAll(createHeaders()))
                .build();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-request-id", MDC.get("request_id"));

        return headers;
    }
}
