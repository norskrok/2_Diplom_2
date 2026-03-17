package praktikum;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;

public class BaseTest {

    protected String accessToken;
    protected UserClient userClient = new UserClient();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru/";
    }

    @After
    public void teardown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }
}