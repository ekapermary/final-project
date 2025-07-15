package api.test;

import com.fasterxml.jackson.databind.util.JSONPObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UpdateTest extends BaseTest {
    @Test
    public void updateUser() {
        String userId = " "; // Ganti dengan ID pengguna yang sesuai

        //set base url untuk restAssured
        RestAssured.baseURI = "https://dummyapi.io/data/api"; // Ganti dengan base URI yang sesuai

        // Membuat JSON payload untuk request body
        JSONPObject updateData = new JSONPObject();
        updateData.put("firstName", "eka");
        updateData.put("lastName", "permatasari");

        //kirim request PUT untuk memperbarui user
        Response response = given()
                .header("app-id", "YOUR_APP_ID_HERE") // Ganti dengan APP ID asli
                .contentType("application/json")
                .body(updateData.toString())
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
