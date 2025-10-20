package ru.stepchenkov.db.dao.students.entity;

import lombok.*;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class StudentEntity {

    @ColumnName("ID")
    private Integer id;

    @ColumnName("NAME")
    private String name;

    @ColumnName("EMAIL")
    private String email;

    @ColumnName("CREATED_AT")
    private OffsetDateTime createdAt;

}