package api.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GetUserTest {
    @Test
    public void testGetUser() {
        RestAssured.baseURI = "https://dummyapi.io/data/v1"; // Ganti dengan base URI yang sesuai

        Response response = given()
                .header("app-id", "624c9429450430b574dcf17c") // Ganti dengan APP ID asli
                .when()
                .get("/user/{userId}"); // Ganti "someUserId" dengan ID pengguna yang valid
                .then()
                .statusCode(200) // Pastikan status code adalah 200 OK
                .extract().response();

        //validasi    respons
        assertNotNull("Response should not be null", response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.jsonPath().getString("id")); // Pastikan ID pengguna ada dalam respons

    }

}
