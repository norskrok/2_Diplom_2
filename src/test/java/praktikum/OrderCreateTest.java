package praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;

public class OrderCreateTest extends BaseTest {
    private final UserClient userClient = new UserClient();
    private final OrderClient orderClient = new OrderClient();

    @Test
    public void createOrderWithAuthorization() {

        User user = new User("order_final_" + System.currentTimeMillis() + "ivan@yandex.ru", "1234", "Ivan");
        userClient.create(user);
        ValidatableResponse loginResponse = userClient.login(new User(user.getEmail(), user.getPassword()));
        accessToken = loginResponse.extract().path("accessToken");

        List<String> allIngredients = io.restassured.RestAssured.given()
                .get("https://stellarburgers.education-services.ru/api/ingredients")
                .then()
                .extract()
                .path("data._id");

        List<String> ingredientsToOrder = List.of(allIngredients.get(0), allIngredients.get(1));
        Order order = new Order(ingredientsToOrder);

        orderClient.create(order, accessToken)
                .assertThat().statusCode(200)
                .body("success", org.hamcrest.Matchers.equalTo(true));
    }

    @Test
    public void createOrderWithoutIngredientsReturnsError() {

        Order order = new Order(List.of());

        orderClient.create(order, null)
                .assertThat().statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void createOrderWithInvalidHashReturnsError() {

        Order order = new Order(List.of("invalid_hash_123"));

        orderClient.create(order, null)
                .assertThat().statusCode(500);
    }
}