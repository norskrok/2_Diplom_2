package praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetUserOrdersTest extends BaseTest {
    private final UserClient userClient = new UserClient();
    private final OrderClient orderClient = new OrderClient();

    @Test
    public void getOrdersWithAuthorizationSuccess() {

        User user = new User("list_test_" + System.currentTimeMillis() + "ivan@yandex.ru", "1234", "Ivan");
        userClient.create(user);
        ValidatableResponse loginResponse = userClient.login(new User(user.getEmail(), user.getPassword()));
        accessToken = loginResponse.extract().path("accessToken");

        orderClient.getUserOrders(accessToken)
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }

    @Test
    public void getOrdersWithoutAuthorizationReturnsError() {

        orderClient.getUserOrders(null)
                .assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}