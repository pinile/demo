import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.stepchenkov.api.ApiRepository;
import ru.stepchenkov.api.ApiResponse;
import ru.stepchenkov.api._base.students.payload.entity.ErrorResponseStudentDto;
import ru.stepchenkov.api._base.students.payload.entity.StudentDto;
import ru.stepchenkov.api._base.tags.payload.entity.ErrorResponseTagDto;
import ru.stepchenkov.api._base.tags.payload.entity.TagDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("API Тесты")
public class Tests {

    private static ApiResponse<StudentDto, ErrorResponseStudentDto> prepareResponse;

    @BeforeEach
    void init() {
        // удалить все существующие записи
        List<StudentDto> existing = ApiRepository.studentsApi.getAllStudents();
        existing.stream()
                .filter(s -> "fake@mail.com".equals(s.getEmail()))
                .forEach(s -> ApiRepository.studentsApi.deleteStudentById(s.getId()));

        // создать нового студента
        StudentDto dto = new StudentDto()
                .setName("Piere Dun")
                .setEmail("fake@mail.com")
                .setTags(List.of("xxx")
                );
        prepareResponse = ApiRepository.studentsApi.createStudent(dto);

        // проверить наличие тегов
        List<TagDto> existingTags = ApiRepository.tagsApi.getAllTags();

        if (existingTags.isEmpty()) {
            for (String tagName : prepareResponse.data().getTags()) {
                TagDto tagDto = new TagDto().setName(tagName);
                ApiRepository.tagsApi.addTag(tagDto);
            }
        }
    }

    @AfterEach
    void cleanup() {
        if (prepareResponse != null && prepareResponse.data().getId() != null) {
            try {
                ApiRepository.studentsApi.deleteStudentById(prepareResponse.data().getId());
            } catch (Exception e) {
                log.error("Не удалось удалить студента: {}", e.getMessage());
            }
        }
    }

    private void assertSuccessResponse(ApiResponse<?, ?> response) {
        assertThat(response.data()).isNotNull();
        assertThat(response.status()).isIn(200, 201, 204);
        assertThat(response.error()).isNull();
    }

    private void assertErrorResponse(ApiResponse<?, ?> response, int expectedStatus) {
        assertThat(response.status()).isEqualTo(expectedStatus);
        assertThat(response.data()).isNull();
        assertThat(response.error()).isNotNull();
    }

    @Nested
    @DisplayName("Тесты для студентов")
    class StudentTests {
        private static Stream<Map<String, Object>> provider_checkBadRequestUpdateStudent() {
            return Stream.of(
                    Map.of("name", 123),
                    Map.of("name", List.of("invalid", "array")),
                    Map.of("name", Map.of("invalid", "object")),
                    Map.of("email", "email.ru"),
                    Map.of("email", List.of("invalid", "array")),
                    Map.of("email", 123),
                    Map.of("tags", List.of("tag1", "tag2"))
            );
        }

        private static Stream<Map<String, Object>> provider_checkValidUpdateStudent() {
            return Stream.of(
                    Map.of("name", "Valid Case",
                            "email", "valid@mail.ru",
                            "tags", List.of("tag1", "tag2")),

                    Map.of("email", "valid@mail.ru",
                            "tags", List.of("tag1", "tag2")),
                    Map.of("name", "Valid Case",
                            "tags", List.of("tag1", "tag2")),
                    Map.of("name", "Valid Case",
                            "email", "valid@mail.ru"),

                    Map.of("name", "Valid Case"),
                    Map.of("email", "valid@mail.ru"),
                    Map.of("tags", List.of("tag1", "tag2"))
            );
        }

        private static Stream<Map<String, Object>> provider_checkValidCreateStudent() {
            return Stream.of(
                    Map.of("name", "Valid Case",
                            "email", "valid@mail.ru",
                            "tags", List.of("tag1", "tag2")),

                    Map.of("name", "Valid Case",
                            "email", "valid@mail.ru")
            );
        }

        @Test
        @DisplayName("Позитивные. Получение информации о студенте по id")
        void checkGetStudentById() {
            ApiResponse<StudentDto, ?> dto = ApiRepository.studentsApi.getStudentById(prepareResponse.data().getId());

            assertSuccessResponse(dto);
            assertThat(dto.data())
                    .usingRecursiveComparison()
                    .ignoringFields("createdAt")
                    .isEqualTo(prepareResponse.data());
        }

        @Test
        @DisplayName("Негативный. Получение информации о студенте по id")
        void checkBadRequestGetStudentById() {
            int randomId = ThreadLocalRandom.current().nextInt(9999, 999999);
            ApiResponse<StudentDto, ErrorResponseStudentDto> response = ApiRepository.studentsApi.getStudentById(randomId);

            assertErrorResponse(response, 404);
            assertThat(response.error())
                    .extracting(ErrorResponseStudentDto::getTitle)
                    .asString()
                    .contains("Студент не найден");
        }

        @ParameterizedTest(name = "[{index}]. Создание студента. Тело запроса = {0}")
        @DisplayName("Негативные. Создание студента")
        @MethodSource("provider_checkBadRequestUpdateStudent")
        void checkBadRequestCreateStudent(Map<String, Object> mapParams) {
            ApiResponse<StudentDto, ErrorResponseStudentDto> responseDto = ApiRepository.studentsApi.updateStudent(prepareResponse.data(), mapParams);
            assertErrorResponse(responseDto, 400);
        }

        @ParameterizedTest(name = "[{index}]. Создание студента. Тело запроса = {0}")
        @DisplayName("Позитивные. Создание студента")
        @MethodSource("provider_checkValidCreateStudent")
        void checkValidCreateStudent(Map<String, Object> mapParams) {
            ApiResponse<StudentDto, ErrorResponseStudentDto> responseDto = ApiRepository.studentsApi.updateStudent(prepareResponse.data(), mapParams);
            assertErrorResponse(responseDto, 201);
        }

        @Test
        @DisplayName("Позитивные. Изменение данных студента")
        void checkUpdateStudent() {
            ApiResponse<StudentDto, ErrorResponseStudentDto> dto = ApiRepository.studentsApi.getStudentById(prepareResponse.data().getId());
            Map<String, Object> mapParams = Map.of(
                    "name", "Changed Name",
                    "email", prepareResponse.data().getEmail(),
                    "tags", List.of("tag1", "tag2")
            );

            ApiResponse<StudentDto, ErrorResponseStudentDto> responseDto = ApiRepository.studentsApi.updateStudent(dto.data(), mapParams);

            assertSuccessResponse(responseDto);
            assertThat(responseDto.data())
                    .extracting(StudentDto::getName, StudentDto::getEmail, StudentDto::getTags)
                    .containsExactly("Changed Name", prepareResponse.data().getEmail(), List.of("tag1", "tag2"));
        }

        @ParameterizedTest(name = "[{index}]. Изменение данных студента. Тело запроса = {0}")
        @DisplayName("Позитивные. Изменение данных студента")
        @MethodSource("provider_checkValidUpdateStudent")
        void checkValidUpdateStudent(Map<String, Object> mapParams) {
            ApiResponse<StudentDto, ErrorResponseStudentDto> dto = ApiRepository.studentsApi.getStudentById(prepareResponse.data().getId());
            ApiResponse<StudentDto, ErrorResponseStudentDto> responseDto = ApiRepository.studentsApi.updateStudent(dto.data(), mapParams);

            assertSuccessResponse(responseDto);
            assertThat(responseDto.data())
                    .extracting(StudentDto::getName, StudentDto::getEmail, StudentDto::getTags)
                    .containsExactly(mapParams.get("name"),
                            mapParams.get("email"),
                            mapParams.getOrDefault("tags", List.of()));
        }

        @Test
        @DisplayName("Негативный. Изменение данных студента. Пустое тело запроса")
        void checkUpdateIsBlankStudent() {
            ApiResponse<StudentDto, ErrorResponseStudentDto> dto = ApiRepository.studentsApi.getStudentById(prepareResponse.data().getId());
            Map<String, Object> mapParams = Map.of();

            ApiResponse<StudentDto, ErrorResponseStudentDto> responseDto = ApiRepository.studentsApi.updateStudent(dto.data(), mapParams);

            assertErrorResponse(responseDto, 400);
            assertThat(responseDto.error())
                    .extracting(ErrorResponseStudentDto::getDetail)
                    .asString()
                    .contains("не должно быть пустым");
        }

        @ParameterizedTest(name = "[{index}]. Изменение данных студента. Тело запроса = {0}")
        @DisplayName("Негативные. Изменение данных студента")
        @MethodSource("provider_checkBadRequestUpdateStudent")
        void checkBadRequestUpdateStudent(Map<String, Object> mapParams) {
            ApiResponse<StudentDto, ErrorResponseStudentDto> responseDto = ApiRepository.studentsApi.updateStudent(prepareResponse.data(), mapParams);
            assertErrorResponse(responseDto, 400);
        }

        @Test
        @DisplayName("Негативный. Изменение данных студента. Студент не найден по id")
        void checkUpdateUnknownStudent() {
            int randomId = ThreadLocalRandom.current().nextInt(9999, 999999);
            ApiResponse<StudentDto, ErrorResponseStudentDto> dto = ApiRepository.studentsApi.getStudentById(prepareResponse.data().getId());
            dto.data().setId(randomId);
            Map<String, Object> mapParams = Map.of(
                    "name", "Changed Name",
                    "email", prepareResponse.data().getEmail(),
                    "tags", List.of("tag1", "tag2")
            );

            ApiResponse<StudentDto, ErrorResponseStudentDto> responseDto = ApiRepository.studentsApi.updateStudent(dto.data(), mapParams);

            assertErrorResponse(responseDto, 404);
            assertThat(responseDto.error())
                    .extracting(ErrorResponseStudentDto::getTitle)
                    .asString()
                    .contains("Студент не найден");
        }

        @Test
        @DisplayName("Негативный. Удаление несуществующего студента по id")
        void checkBadRequestDeleteStudentById() {
            int randomId = ThreadLocalRandom.current().nextInt(9999, 999999);
            ApiResponse<StudentDto, ErrorResponseStudentDto> response = ApiRepository.studentsApi.deleteStudentById(randomId);

            assertErrorResponse(response, 404);
            assertThat(response.error())
                    .extracting(ErrorResponseStudentDto::getTitle)
                    .asString()
                    .contains("Студент не найден");
        }
    }

    @Nested
    @DisplayName("Позитивные. Тесты тегов")
    class TagTests {
        private static Stream<Object> provider_checkBadRequestAddTag() {
            return Stream.of(
                    123,
                    List.of("invalid", "array"),
                    Map.of("name", Map.of("nested", 1)),
                    "",
                    new TagDto().setName(null),
                    new TagDto().setName("")
            );
        }

        @Test
        @DisplayName("Позитивные. Получить все теги")
        void checkGetAllTags() {
            List<TagDto> allTags = ApiRepository.tagsApi.getAllTags();

            assertThat(allTags)
                    .isNotNull()
                    .isNotEmpty();
        }

        @Test
        @DisplayName("Позитивные. Добавление тега")
        void checkAddTag() {
            TagDto tagDto = new TagDto().setName(UUID.randomUUID().toString());
            ApiResponse<TagDto, ErrorResponseTagDto> response = ApiRepository.tagsApi.addTag(tagDto);

            assertSuccessResponse(response);
            assertThat(response.data())
                    .isNotNull()
                    .extracting(TagDto::getName)
                    .isEqualTo(tagDto.getName());

            List<TagDto> tagList = ApiRepository.tagsApi.getAllTags();

            assertThat(tagList)
                    .extracting(TagDto::getName)
                    .contains(tagDto.getName());
        }

        @ParameterizedTest(name = "[{index}]. Добавление тега. Тело запроса = {0}")
        @MethodSource("provider_checkBadRequestAddTag")
        @DisplayName("Негативные. Добавление тега.")
        void checkBadRequestAddTag(Object invalidBody) {
            ApiResponse<TagDto, ErrorResponseTagDto> response = ApiRepository.tagsApi.addTag(invalidBody);

            assertErrorResponse(response, 400);
            assertThat(response.error())
                    .extracting(ErrorResponseTagDto::getError)
                    .asString()
                    .contains("Bad Request");
        }
    }
}
