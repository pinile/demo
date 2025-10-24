package ru.stepchenkov.api._base.tags.payload.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Accessors(chain = true)
public class ErrorResponseTagDto {

    @JsonProperty("timestamp")
    private OffsetDateTime timestamp;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("error")
    private String error;

    @JsonProperty("path")
    private String path;
}
