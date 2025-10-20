package ru.stepchenkov.api;

import lombok.experimental.UtilityClass;
import ru.stepchenkov.api._base.students.StudentsApi;

@UtilityClass
public class ApiRepository {
    public static final StudentsApi studentsApi = new StudentsApi();
}
