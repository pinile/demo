package ru.stepchenkov.db;

import lombok.experimental.UtilityClass;
import ru.stepchenkov.db.dao.students.StudentsDao;

@UtilityClass
public class DaoRepository {

    public static final StudentsDao studentsDao = new StudentsDao();
}
