package ru.stepchenkov.api._base.students.payload.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Accessors(chain = true)
public class ErrorResponseStudentDto {

    @JsonProperty("type")
    private String type;

    @JsonProperty("title")
    private String title;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("detail")
    private String detail;

    @JsonProperty("instance")
    private String instance;

}
