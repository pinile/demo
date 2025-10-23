package ru.stepchenkov.api._base.students;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import ru.stepchenkov.api._base._BaseApi;
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

    public StudentDto getStudentById(int id) {
        log.info("Получить студента по id");

        Response response = jsonAutoAuth()
                .basePath("/api/students/" + id)
                .get();

        response.then().statusCode(200);
        return response.as(StudentDto.class);
    }

    public void deleteStudentById(int id) {
        log.info("Удалить студента по id");

        jsonAutoAuth()
                .basePath("/api/students/" + id)
                .delete()
                .then()
                .statusCode(204);
    }

    public StudentDto createStudent(StudentDto student) {
        log.info("Создать студента с именем - {}", student.getName());

        Response response = jsonAutoAuth()
                .basePath("/api/students")
                .body(student)
                .post();

        response.then().statusCode(201);
        return response.as(StudentDto.class);
    }

    public StudentDto updateStudent(StudentDto student, Map<String, Object> mapParams) {
        log.info("Обновить студента с именем - {}", student.getName());

        Response response = jsonAutoAuth()
                .basePath("/api/students/" + student.getId())
                .body(mapParams)
                .put();

        return response.as(StudentDto.class);
    }
}


