import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.stepchenkov.api.ApiRepository;
import ru.stepchenkov.api._base.students.payload.entity.StudentDto;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class Tests {

    private StudentDto response;

    @BeforeEach
    void init() {
        // удалить все существующие записи
        List<StudentDto> existing = ApiRepository.studentsApi.getAllStudents();
        existing.stream()
                .filter(s -> "fake@mail.com".equals(s.getEmail()))
                .forEach(s -> ApiRepository.studentsApi.deleteStudentById(s.getId()));

        StudentDto dto = new StudentDto()
                .setName("Piere Dun")
                .setEmail("fake@mail.com")
                .setTags(List.of("xxx")
                );
        response = ApiRepository.studentsApi.createStudent(dto);
    }

    @AfterEach
    void cleanup() {
        if (response != null && response.getId() != null) {
            try {
                ApiRepository.studentsApi.deleteStudentById(response.getId());
            } catch (Exception e) {
                log.error("Не удалось удалить студента: {}", e.getMessage());
            }
        }

    }

    @Test
    @DisplayName("Получение информации о студенте по id")
    void checkGetStudentById() {
        StudentDto dto = ApiRepository.studentsApi.getStudentById(response.getId());

        assertThat(dto).isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(response);
    }

    @Test
    @DisplayName("Изменение данных студента")
    void checkUpdateStudent() {
        StudentDto dto = ApiRepository.studentsApi.getStudentById(response.getId());
        Map<String, Object> mapParams = Map.of(
                "name", "Changed Name",
                "email", response.getEmail(),
                "tags", List.of("tag1", "tag2")
        );

        StudentDto responseDto = ApiRepository.studentsApi.updateStudent(dto, mapParams);

        assertThat(responseDto)
                .extracting(StudentDto::getName, StudentDto::getEmail, StudentDto::getTags)
                .containsExactly("Changed Name", response.getEmail(), List.of("tag1", "tag2"));
    }


}
