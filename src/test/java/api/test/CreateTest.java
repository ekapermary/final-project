package api.test;

import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateTest extends BaseTest {
    @Test
    public void testCreate(){
        //set base url untuk restAssured
        RestAssured.baseURI = "https://dummyapi.io/data/v1"; // Ganti dengan base URI yang sesuai

        //membuat json payload untuk request body
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("firstName", "eka");
        requestBody.addProperty("lastName", "permatasari");
        requestBody.addProperty("email", "eka.permatasari" + System.currentTimeMillis() + "@example.com"); // membuat email unik

        //debug - cetak request body
        System.out.println("[DEBUG] Request Body: " + requestBody);

        //kirim request POST untuk membuat user baru
        Response response = given()
                .header("app-id", "624c9429450430b574dcf17c")
                .contentType("application/json")
                .body(requestBody.toString())
                .when()
                .post("/user/create")
                .then()
                .log().all()
                .statusCode(200) // pastikan status code adalah 201 Created
                .body ("firstName", equalTo("eka"))
                .body ("lastName", equalTo("permatasari"))
                .extract()
                .response();

        //System.out.println(response.asPrettyString());


        //debug - cetak response
        System.out.println("[DEBUG] Response status code: " + response.getStatusCode());
        System.out.println("[DEBUG] Response body: " + response.getBody().asPrettyString());
        System.out.println("[DEBUG] Create user response: " + response.getBody().asPrettyString());

    }


}
