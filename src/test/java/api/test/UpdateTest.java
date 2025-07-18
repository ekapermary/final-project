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

        //set base url untuk restAssured
        RestAssured.baseURI = "https://dummyapi.io/data/v1"; // Ganti dengan base URI yang sesuai

        // Membuat JSON payload untuk request body
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("firstName", "eka");
        requestBody.addProperty("lastName", "permatasari");
        //updateData.addProperty("email", "eka.permatasari" + System.currentTimeMillis() + "@example.com"); // membuat email unik

        //kirim request PUT untuk memperbarui user
        Response response = given()
                .header("app-id", "624c9429450430b574dcf17c") // Ganti dengan APP ID asli
                .contentType("application/json")
                .body(requestBody.toString())
                .when()
                .put("/user/" + userId)
                .then()
                .statusCode(200) // pastikan status code adalah 200 OK
                .body("firstName", equalTo("eka"))
                .body("lastName", equalTo("permatasari"))
                .extract().response();

        //cetak response json untuk debugging
        System.out.println("User udate response: " + response.prettyPrint());
    }
}
