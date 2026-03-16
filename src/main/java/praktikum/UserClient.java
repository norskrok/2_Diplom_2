package praktikum;

import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String REGISTER_PATH = "api/auth/register";
    private static final String LOGIN_PATH = "api/auth/login";
    private static final String USER_PATH = "api/auth/user";

    public ValidatableResponse create(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(REGISTER_PATH)
                .then();
    }

    public ValidatableResponse login(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    public ValidatableResponse delete(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(USER_PATH)
                .then();
    }
}