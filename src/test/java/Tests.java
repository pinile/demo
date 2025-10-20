import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.stepchenkov.api.ApiRepository;
import ru.stepchenkov.api._base.students.payload.entity.StudentDto;

import java.util.List;

public class Tests {

    private StudentDto response;

    @BeforeEach
    void init() {
        StudentDto dto = new StudentDto()
                .setId(2)
                .setName("Piere Dun")
                .setEmail("pp@p.com")
                .setTags(List.of("xxx")
                );
        response = ApiRepository.studentsApi.createStudent(dto);
    }

    @AfterEach
    void cleanup() {
        ApiRepository.studentsApi.deleteStudentById(response.getId());
    }

    @Test
    void checkSomething() {
         System.out.println("do something");
    }


}
