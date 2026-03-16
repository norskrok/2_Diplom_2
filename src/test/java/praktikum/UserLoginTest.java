package praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserLoginTest extends BaseTest {
    private final UserClient userClient = new UserClient();

    @Test
    public void shouldLoginWithValidCredentials() {

        User user = new User("login_test_" + System.currentTimeMillis() + "ivan@yandex.ru", "1234", "Ivan");
        userClient.create(user);

        User credentials = new User(user.getEmail(), user.getPassword());
        ValidatableResponse response = userClient.login(credentials);

        response.assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());

        accessToken = response.extract().path("accessToken");
    }

    @Test
    public void shouldNotLoginWithIncorrectPassword() {

        User user = new User("login_fail_" + System.currentTimeMillis() + "ivan@yandex.ru", "1234", "Ivan");
        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");

        User wrongCredentials = new User(user.getEmail(), "wrong_password");
        ValidatableResponse response = userClient.login(wrongCredentials);

        response.assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}