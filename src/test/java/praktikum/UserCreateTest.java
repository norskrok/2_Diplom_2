package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;

public class UserCreateTest extends BaseTest {
    private final UserClient userClient = new UserClient();

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешного создания нового пользователя с валидными данными")
    public void createUniqueUserSuccessfully() {
        User user = new User("unique_user_" + System.currentTimeMillis() + "ivan@yandex.ru", "1234", "Ivan");
        ValidatableResponse response = userClient.create(user);

        accessToken = response.extract().path("accessToken");

        response.assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    @Description("Проверка ошибки при попытке регистрации пользователя с уже занятым email")
    public void createExistingUserReturnsError() {
        User user = new User("ivan@yandex.ru", "1234", "Ivan");

        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");

        userClient.create(user)
                .assertThat().statusCode(403)
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Проверка получения ошибки 403 при попытке регистрации без указания обязательного поля name")
    public void createUserWithoutNameReturnsError() {
        User user = new User("ivan@yandex.ru", "password123", "");
        userClient.create(user)
                .assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Проверка ошибки 403 при отсутствии поля email")
    public void createUserWithoutEmailReturnsError() {
        User user = new User(null, "123456", "Ivan");
        userClient.create(user)
                .assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверка ошибки 403 при отсутствии поля password")
    public void createUserWithoutPasswordReturnsError() {
        User user = new User("ivan_" + System.currentTimeMillis() + "@yandex.ru", null, "Ivan");
        userClient.create(user)
                .assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}