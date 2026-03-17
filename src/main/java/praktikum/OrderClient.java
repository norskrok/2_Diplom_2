package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String ORDER_PATH = "api/orders";
    private static final String INGREDIENTS_PATH = "api/ingredients";

    @Step("Создание заказа")
    public ValidatableResponse create(Order order, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken == null ? "" : accessToken)
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Получение заказов пользователя")
    public ValidatableResponse getUserOrders(String accessToken) {
        return given()
                .header("Authorization", accessToken == null ? "" : accessToken)
                .when()
                .get(ORDER_PATH)
                .then();
    }

    @Step("Получение списка ингредиентов")
    public ValidatableResponse getIngredients() {
        return given()
                .when()
                .get(INGREDIENTS_PATH)
                .then();
    }
}