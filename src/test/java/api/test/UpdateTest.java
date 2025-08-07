package api.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import com.google.gson.JsonObject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UpdateTest extends BaseTest {

    @Test
    public void updateUser() {
        String userId = "6879fb344e447c9e781f3743"; // Ganti dengan ID pengguna yang sesuai
        String appId = "63a804408eb0cb069b57e43a"; // Ganti dengan APP ID kamu

        // Set base URI
        RestAssured.baseURI = "https://dummyapi.io/data/v1";

        // Buat request body
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("firstName", "eka");
        requestBody.addProperty("lastName", "permatasari");

        // Kirim PUT request
        Response response = given()
                .header("app-id", "63a804408eb0cb069b57e43a")
                .contentType("application/json")
                .body(requestBody.toString())
                .when()
                .put("/user/" + userId)
                .then()
                .statusCode(200)
                .body("firstName", equalTo("eka"))
                .body("lastName", equalTo("permatasari"))
                .extract().response();

        // Cetak isi response
        System.out.println("===== RESPONSE BODY =====");
        System.out.println(response.prettyPrint());

        // Validasi status dan isi field
        int statusCode = response.getStatusCode();
        if (statusCode != 200) {
            throw new AssertionError("Status code tidak sesuai. Diharapkan: 200, Tapi: " + statusCode);
        }

        String firstName = response.jsonPath().getString("firstName");
        String lastName = response.jsonPath().getString("lastName");

        if (!"eka".equals(firstName)) {
            throw new AssertionError("First name tidak sesuai. Diharapkan: eka, Tapi: " + firstName);
        }

        if (!"permatasari".equals(lastName)) {
            throw new AssertionError("Last name tidak sesuai. Diharapkan: permatasari, Tapi: " + lastName);
        }

        System.out.println("✅ Update user berhasil dan data sesuai.");
    }
}
