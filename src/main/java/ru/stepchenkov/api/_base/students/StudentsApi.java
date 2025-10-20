package ru.stepchenkov.api._base.students;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import ru.stepchenkov.api._base._BaseApi;
import ru.stepchenkov.api._base.students.payload.entity.StudentDto;
import ru.stepchenkov.env.Env;

import java.util.Arrays;
import java.util.List;

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

}


