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
}