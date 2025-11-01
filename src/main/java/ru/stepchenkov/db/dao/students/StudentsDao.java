package ru.stepchenkov.db.dao.students;

import lombok.extern.slf4j.Slf4j;
import ru.stepchenkov.db.dao._base._BaseMainDao;
import ru.stepchenkov.db.dao.students.entity.StudentEntity;

import java.util.List;

@Slf4j
public class StudentsDao extends _BaseMainDao {

    public List<StudentEntity> findAllStudents() {
        final String query = """
                select *
                from students;
                """;
        return jdbi.withHandle(
                handle -> handle.createQuery(query)
                        .mapToBean(StudentEntity.class)
                        .list()
        );
    }

    public StudentEntity findStudentByName(String name) {
        final String query = """
                select *
                from students
                where name = :name;
                """;
        return jdbi.withHandle(
                handle -> handle.createQuery(query)
                        .bind("name", name)
                        .mapToBean(StudentEntity.class)
                        .one()
        );
    }

    public StudentEntity findStudentById(int id) {
        final String query = """
                select *
                from students
                where id = :id;
                """;
        return jdbi.withHandle(
                handle -> handle.createQuery(query)
                        .bind("id", id)
                        .mapToBean(StudentEntity.class)
                        .one()
        );
    }
}