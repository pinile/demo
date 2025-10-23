package ru.stepchenkov.api._base.tags;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import ru.stepchenkov.api._base._BaseApi;
import ru.stepchenkov.api._base.tags.payload.entity.TagDto;
import ru.stepchenkov.env.Env;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class TagsApi extends _BaseApi {

    public TagsApi() {
        super(Env.Api.API_CONFIG);
    }

    public List<TagDto> getAllTags() {
        log.info("getAllTags");
        Response response = jsonAutoAuth()
                .basePath("/api/tags")
                .get();

        response.then().statusCode(200);
        return Arrays.asList(response.as(TagDto[].class));
    }

    public TagDto addTag(TagDto tag) {
        log.info("addTag");
        Response response = jsonAutoAuth()
                .basePath("/api/tags")
                .body(tag)
                .post();

        return response.as(TagDto.class);
    }
}
