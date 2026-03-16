package praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;

public class UserCreateTest extends BaseTest {
    private final UserClient userClient = new UserClient();

    @Test
    public void createUniqueUserSuccessfully() {
        User user = new User("unique_user_" + System.currentTimeMillis() + "ivan@yandex.ru", "1234", "Ivan");
        ValidatableResponse response = userClient.create(user);

        response.assertThat().statusCode(200).body("success", equalTo(true));

        accessToken = response.extract().path("accessToken");
    }

    @Test
    public void createExistingUserReturnsError() {
        User user = new User("ivan@yandex.ru", "1234", "Ivan");

        ValidatableResponse response1 = userClient.create(user);
        accessToken = response1.extract().path("accessToken");

        userClient.create(user)
                .assertThat().statusCode(403)
                .body("message", equalTo("User already exists"));
    }
}