package ru.stepchenkov.api._base;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.Filter;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import ru.stepchenkov.env.config.ApiConfig;

import java.util.List;

@RequiredArgsConstructor
public class _BaseApi {
    protected final ApiConfig CONFIG;

    protected RequestSpecification jsonAutoAuth() {
        return buildRequest(ContentType.JSON);
    }

    protected RequestSpecification buildRequest(ContentType contentType) {
        return RestAssured.given()
                .config(createConfig())
                .contentType(contentType)
                .baseUri(CONFIG.url())
                .relaxedHTTPSValidation()
                .filters(getFilters());
    }

    private RestAssuredConfig createConfig() {
        return RestAssured.config()
                .logConfig(LogConfig.logConfig()
                        .enableLoggingOfRequestAndResponseIfValidationFails())
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", 5000));
    }

    private List<Filter> getFilters() {
        return List.of(
//                new RequestLoggingFilter(),
//                new ResponseLoggingFilter(),
                new ErrorLoggingFilter()
        );
    }
}
