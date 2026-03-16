package praktikum;

import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String ORDER_PATH = "api/orders";

    public ValidatableResponse create(Order order, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken == null ? "" : accessToken)
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    public ValidatableResponse getUserOrders(String accessToken) {
        return given()
                .header("Authorization", accessToken == null ? "" : accessToken)
                .when()
                .get(ORDER_PATH)
                .then();
    }
}