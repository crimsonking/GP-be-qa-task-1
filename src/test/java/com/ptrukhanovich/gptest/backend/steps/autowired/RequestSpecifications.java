package com.ptrukhanovich.gptest.backend.steps.autowired;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TestProperties.class})
public class RequestSpecifications {

    private static final String ACCEPT_CUSTOM_HEADER = "application/vnd.who.Sportsbook+json;v=1;charset=utf-8";
    private static final String HEADER_KEY = "who-apiKey";
    private static final String HEADER_SECRET = "who-secret";

    @Bean
    public RequestSpecification sessionApiSpec(@Value("${urlToSessionApi}") String urlToApi,
                                        @Value("${apiKeyValue}") String apiKey,
                                        @Value("${apiSecretValue}") String apiSecret,
                                        @Value("${apiUsernameValue}") String apiUserName,
                                        @Value("${apiPasswordValue}") String apiUserPassword) {

        return new RequestSpecBuilder()
                .setContentType(ContentType.URLENC)
                .addHeader(HEADER_KEY, apiKey)
                .addHeader(HEADER_SECRET, apiSecret)
                .setAccept(ContentType.XML)
                .setBaseUri(urlToApi)
                .setBody(String.format("username=%s&password=%s", apiUserName, apiUserPassword))
                .build();
    }

    @Bean
    public RequestSpecification accountsApiSpec(@Value("${urlToAccountApi}") String urlToApi,
                                                @Value("${apiKeyValue}") String apiKey,
                                                @Value("${apiSecretValue}") String apiSecret) {
        return new RequestSpecBuilder()
                .setAccept(ContentType.XML)
                .addHeader(HEADER_KEY, apiKey)
                .addHeader(HEADER_SECRET, apiSecret)
                .setBaseUri(urlToApi)
                .build();
    }
    @Bean
    public RequestSpecification balanceApiSpec(@Value("${urlToAccountBalanceApi}") String urlToApi,
                                               @Value("${apiKeyValue}") String apiKey,
                                               @Value("${apiSecretValue}") String apiSecret) {
        return new RequestSpecBuilder()
                .setAccept(ContentType.XML)
                .addHeader(HEADER_KEY, apiKey)
                .addHeader(HEADER_SECRET, apiSecret)
                .setBaseUri(urlToApi)
                .build();
    }

    @Bean
    RequestSpecification competitionsApiSpec(@Value("${urlToFootballEvents}") String urlToApi,
                                             @Value("${apiKeyValue}") String apiKey) {
        return new RequestSpecBuilder()
                .setAccept(ACCEPT_CUSTOM_HEADER)
                .addHeader(HEADER_KEY, apiKey)
                .setBaseUri(urlToApi)
                .build();
    }

    @Bean
    RequestSpecification eventOutcomesApiSpec(@Value("${apiKeyValue}") String apiKey) {
        return new RequestSpecBuilder()
                .setAccept(ACCEPT_CUSTOM_HEADER)
                .addHeader(HEADER_KEY, apiKey)
                .build();
    }

    @Bean
    RequestSpecification betslipApiSpec(@Value("${urlToBetSlip}") String urlToApi,
                                        @Value("${apiKeyValue}") String apiKey,
                                        @Value("${apiSecretValue}") String apiSecret) {
        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .addHeader(HEADER_KEY, apiKey)
                .addHeader(HEADER_SECRET, apiSecret)
                .setBaseUri(urlToApi)
                .build();
    }

    @Bean
    RequestSpecification betHistoryApiSpec(@Value("${urlToBetSlip}") String urlToApi,
                                        @Value("${apiKeyValue}") String apiKey,
                                        @Value("${apiSecretValue}") String apiSecret) {
        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .addHeader(HEADER_KEY, apiKey)
                .addHeader(HEADER_SECRET, apiSecret)
                .setBaseUri(urlToApi)
                .build();
    }

    @Bean
    RequestSpecification betsApiSpec(@Value("{urlToSessionApi}") String urlToApi) {
        return new RequestSpecBuilder()
                .setContentType(ContentType.URLENC)
                .setBaseUri(urlToApi)
                .build();
    }
}
