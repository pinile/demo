package ru.stepchenkov.db.dao.students;

import ru.stepchenkov.db.dao._base._BaseMainDao;
import ru.stepchenkov.db.dao.students.entity.StudentEntity;

import java.util.List;

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
}