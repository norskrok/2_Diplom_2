package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;

public class OrderCreateTest extends BaseTest {
    private final UserClient userClient = new UserClient();
    private final OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    @Description("Проверка успешного создания заказа с ингредиентами под учетной записью пользователя")
    public void createOrderWithAuthorization() {

        User user = new User("order_final_" + System.currentTimeMillis() + "ivan@yandex.ru", "1234", "Ivan");
        userClient.create(user);
        ValidatableResponse loginResponse = userClient.login(new User(user.getEmail(), user.getPassword()));
        accessToken = loginResponse.extract().path("accessToken");

        List<String> allIngredients = orderClient.getIngredients()
                .extract()
                .path("data._id");

        List<String> ingredientsToOrder = List.of(allIngredients.get(0), allIngredients.get(1));
        Order order = new Order(ingredientsToOrder);

        orderClient.create(order, accessToken)
                .assertThat().statusCode(200)
                .body("success", org.hamcrest.Matchers.equalTo(true));
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
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка получения ошибки 400 при попытке создать заказ с пустым списком ингредиентов")
    public void createOrderWithoutIngredientsReturnsError() {

        Order order = new Order(List.of());

        orderClient.create(order, null)
                .assertThat().statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с невалидным хешем ингредиента")
    @Description("Проверка ответа сервера (500) при передаче несуществующего ID ингредиента")
    public void createOrderWithInvalidHashReturnsError() {

        Order order = new Order(List.of("invalid_hash_123"));

        orderClient.create(order, null)
                .assertThat().statusCode(500);
    }
}