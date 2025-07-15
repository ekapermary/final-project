package api.steps;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import java.time.Duration;
import java.util.UUID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class APIStepDev {

    private static final String BASE_URL = "https://dummyapi.io/data/v1";
    private static final String APP_ID = "YOUR_APP_ID_HERE"; // ganti dengan APP ID asli
    private static String userID;
    private Response response;

    @Given("saya memiliki user dengan detail firstName {string}, lastName {string}, and a unique email")
    public void sayaMemilikiUserDenganDetail(String firstName, String lastName) {
        String uniqueEmail = firstName.toLowerCase() + "." + lastName.toLowerCase() + System.currentTimeMillis() + "@example.com";
        String requestBody = "{\"firstName\": \"" + firstName + "\", \"lastName\": \"" + lastName + "\", \"email\": \"" + uniqueEmail + "\"}";
        System.out.println("[DEBUG] Request Body: " + requestBody);

        response = given()
                .header("app-id", APP_ID)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(BASE_URL + "/user/create");

        System.out.println("[DEBUG] Response: " + response.asString());

        if (response.getStatusCode() != 200 && response.getStatusCode() != 201) {
            throw new RuntimeException("Failed to create user: " + response.asString());
        }

        userID = response.jsonPath().getString("id");
        Assert.assertNotNull("User ID should not be null", userID);
    }

    @Then("menampilkan response dengan status created successfully")
    public void menampilkanResponseDenganStatusCreatedSuccessfully() {
        response.then().statusCode(anyOf(equalTo(200), equalTo(201)));
        Assert.assertNotNull("User ID should not be null", userID);
    }
}
