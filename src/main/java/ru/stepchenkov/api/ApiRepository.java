package ru.stepchenkov.api;

import lombok.experimental.UtilityClass;
import ru.stepchenkov.api._base.students.StudentsApi;
import ru.stepchenkov.api._base.tags.TagsApi;

@UtilityClass
public class ApiRepository {
    public static final StudentsApi studentsApi = new StudentsApi();
    public static final TagsApi tagsApi = new TagsApi();
}
