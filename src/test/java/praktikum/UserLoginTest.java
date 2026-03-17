package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserLoginTest extends BaseTest {

    private User user;

    @Before
    @Override
    public void setUp() {
        super.setUp();

        user = new User("ivan" + System.currentTimeMillis() + "ivan@yandex.ru", "1234", "Ivan");
        ValidatableResponse response = userClient.create(user);

        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("Проверка успешной авторизации при вводе верного логина и пароля")
    public void shouldLoginWithValidCredentials() {

        ValidatableResponse response = userClient.login(user);

        accessToken = response.extract().path("accessToken");

        response.assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Проверка ошибки авторизации при использовании корректного email, но неверного пароля")
    public void shouldNotLoginWithIncorrectPassword() {

        User wrongPasswordUser = new User(user.getEmail(), "wrong_password");
        ValidatableResponse response = userClient.login(wrongPasswordUser);

        response.assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным логином")
    @Description("Проверка ошибки авторизации при использовании некорректного email, но верного пароля")
    public void shouldNotLoginWithIncorrectLogin() {

        User nonExistentUser = new User("ivanivanivan@yandex.ru", "password123");
        ValidatableResponse response = userClient.login(nonExistentUser);

        response.assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}