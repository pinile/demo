package ru.stepchenkov.api._base.students;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import ru.stepchenkov.api.ApiResponse;
import ru.stepchenkov.api._base._BaseApi;
import ru.stepchenkov.api._base.students.payload.entity.ErrorResponseStudentDto;
import ru.stepchenkov.api._base.students.payload.entity.StudentDto;
import ru.stepchenkov.env.Env;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public class StudentsApi extends _BaseApi {

    public StudentsApi() {
        super(Env.Api.API_CONFIG);
    }

    public List<StudentDto> getAllStudents() {
        log.info("Получить всех студентов");

        Response response = jsonAutoAuth()
                .basePath("/api/students")
                .get();

        response.then().statusCode(200);
        return Arrays.asList(response.as(StudentDto[].class));
    }

    public ApiResponse<StudentDto, ErrorResponseStudentDto> getStudentById(int id) {
        log.info("Получить студента по id");

        Response response = jsonAutoAuth()
                .basePath("/api/students/" + id)
                .get();

        int statusCode = response.getStatusCode();

        if (statusCode == 200) {
            return new ApiResponse<>(statusCode, response.as(StudentDto.class), null);
        } else return new ApiResponse<>(statusCode, null, response.as(ErrorResponseStudentDto.class));
    }

    public ApiResponse<StudentDto, ErrorResponseStudentDto> deleteStudentById(int id) {
        log.info("Удалить студента по id");

        Response response = jsonAutoAuth()
                .basePath("/api/students/" + id)
                .delete();

        int statusCode = response.getStatusCode();

        if (statusCode == 204) {
            return new ApiResponse<>(statusCode, null, null);
        } else return new ApiResponse<>(statusCode, null, response.as(ErrorResponseStudentDto.class));
    }

    public ApiResponse<StudentDto, ErrorResponseStudentDto> createStudent(StudentDto student) {
        log.info("Создать студента с именем - {}", student.getName());

        Response response = jsonAutoAuth()
                .basePath("/api/students")
                .body(student)
                .post();

        int statusCode = response.getStatusCode();

        if (statusCode == 201) {
            return new ApiResponse<>(statusCode, response.as(StudentDto.class), null);
        } else return new ApiResponse<>(statusCode, null, response.as(ErrorResponseStudentDto.class));
    }

    public ApiResponse<StudentDto, ErrorResponseStudentDto> updateStudent(StudentDto student, Map<String, Object> mapParams) {
        log.info("Обновить студента с id - {}", student.getId());

        Response response = jsonAutoAuth()
                .basePath("/api/students/" + student.getId())
                .body(mapParams)
                .put();

        int statusCode = response.getStatusCode();

        if (statusCode == 200) {
            return new ApiResponse<>(statusCode, response.as(StudentDto.class), null);
        } else return new ApiResponse<>(statusCode, null, response.as(ErrorResponseStudentDto.class));
    }
}


