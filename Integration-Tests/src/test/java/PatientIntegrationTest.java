import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PatientIntegrationTest {

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:8084";
    }

    @Test
    public void shouldReturnPatientsWithValidToken() {
        String loginDTO = """
                {
                    "email": "testuser@test.com",
                    "password": "password123"
                }
                """;

        // Log in and extract the JWT.
        String token = given()
                .contentType("application/json")
                .body(loginDTO)
                .when()
                    .post("/auth/login")
                .then()
                    .statusCode(200)
                    .body("token", notNullValue())
                    .extract()
                    .jsonPath()
                    .getString("token");

        // Request patients using the JWT.
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                    .get("/api/patients")
                .then()
                    .statusCode(200)
                    .body("$", instanceOf(List.class));
    }

    @Test
    public void shouldReturnUnauthorizedWithoutToken() {
        given()
                .when()
                    .get("/api/patients")
                .then()
                    .statusCode(401);
    }

    @Test
    public void shouldReturnUnauthorizedWithInvalidToken() {
        given()
                .header("Authorization", "Bearer invalid-token")
                .when()
                    .get("/api/patients")
                .then()
                    .statusCode(401);
    }
}
