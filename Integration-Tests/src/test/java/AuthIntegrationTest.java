import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AuthIntegrationTest {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:8084"; // Gateway
    }

    @Test
    public void shouldReturnTokenWithValidCredentials() {
        String loginDTO = """
                {
                    "email": "testuser@test.com",
                    "password": "password123"
                }
                """;

        given()
                .contentType("application/json")
                .body(loginDTO)
                .when()
                    .post("/auth/login")
                .then()
                    .statusCode(200)
                    .body("token", notNullValue());
    }

    @Test
    public void shouldReturnUnauthorizedOnInvalidLogin() {
        String loginDTO = """
            {
                "email": "invalid_user@test.com",
                "password": "wrongPassword"
            }
            """;

        given()
                .contentType("application/json")
                .body(loginDTO)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }
}
