package api.steps;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class APIStepDev {

    private static final Logger logger = LoggerFactory.getLogger(APIStepDev.class);
    private static final String BASE_URL = "https://dummyapi.io/data/v1";
    private static final String APP_ID = "624c9429450430b574dcf17c"; // ganti sesuai APP ID kamu
    private static String userId;
    private Response response;

    private Map<String, Object> createUserBody(String firstName, String lastName, String email) {
        Map<String, Object> body = new HashMap<>();
        body.put("firstName", firstName);
        body.put("lastName", lastName);
        body.put("email", email);
        return body;
    }

    @Given("saya memiliki user dengan detail firstName {string}, lastName {string}, and a unique email {string}")
    public void saya_memiliki_user_dengan_detail(String firstName, String lastName, String email) {
        String uniqueEmail = firstName.toLowerCase() + "." + lastName.toLowerCase() + System.currentTimeMillis() + "@example.com";
        Map<String, Object> requestBody = createUserBody(firstName, lastName, uniqueEmail);

        logger.debug("Request Body: {}", requestBody);

        response = given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(BASE_URL + "/user/create");

        logger.debug("Response: {}", response.asString());

        Assert.assertEquals(200, response.getStatusCode());
        userId = response.jsonPath().getString("id");
        assertNotNull("User ID should not be null after creation", userId);
        logger.info("User created with ID: {}", userId);
    }

    @When("saya mengirimkan request untuk membuat user")
    public void saya_mengirimkan_request_untuk_membuat_user() {
        String email = "eka.permatasari" + System.currentTimeMillis() + "@example.com";
        Map<String, Object> requestBody = createUserBody("eka", "permatasari", email);

        response = given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(BASE_URL + "/user/create");

        userId = response.jsonPath().getString("id");
        logger.debug("Create user response: {}", response.asString());
    }

    @Then("menampilkan response dengan status created successfully")
    public void menampilkan_response_dengan_status_created_successfully() {
        response.then().statusCode(200);
        assertNotNull("User ID should not be null after creation", userId);
        logger.info("User created successfully with ID: {}", userId);
    }

    @Given("saya memiliki ID user")
    public void saya_memiliki_id_user() {
        assertNotNull("User ID must be set", userId);
        logger.info("Using user ID: {}", userId);
    }

    @When("saya mengirim request POST untuk {string}")
    public void saya_mengirim_request_post_untuk(String endpoint) {
        response = given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .post(BASE_URL + endpoint);

        logger.info("POST to {}{}", BASE_URL, endpoint);
        logger.debug("Response: {}", response.asString());
    }

    @When("saya mengirim request GET untuk {string}")
    public void sayaMengirimRequestGETUntuk(String endpoint) {
        String finalEndpoint = BASE_URL + endpoint.replace("{id}", userId);
        response = given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .get(finalEndpoint);

        logger.info("GET from {}", finalEndpoint);
        logger.debug("Response: {}", response.asString());
    }

    @Then("saya akan menerima response dengan status be {int}")
    public void saya_akan_menerima_response_dengan_status_be(int statusCode) {
        response.then().statusCode(statusCode)
                .body("id", equalTo(userId))
                .body("firstName", notNullValue())
                .body("email", notNullValue());

        logger.info("Verified response with status {}", statusCode);
    }

    @And("saya akan mendapatkan detail user")
    public void saya_akan_mendapatkan_detail_user() {
        response.then().statusCode(200)
                .body("id", equalTo(userId))
                .body("firstName", notNullValue())
                .body("email", notNullValue());
    }

    @And("saya melakukan update dengan detail name {string}, dan email {string}")
    public void saya_melakukan_update_dengan_detail(String name, String email) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("firstName", name);
        requestBody.put("email", email);

        response = given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .put(BASE_URL + "/user/" + userId);

        logger.info("PUT request with update body: {}", requestBody);
        logger.debug("Response: {}", response.asString());
    }

    @When("saya mengirim request PUT untuk {string}")
    public void saya_mengirim_request_put_untuk(String endpoint) {
        String finalEndpoint = BASE_URL + endpoint.replace("{id}", userId);
        String newEmail = "updated_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";

        Map<String, Object> updateBody = new HashMap<>();
        updateBody.put("firstName", "Updated Name");
        updateBody.put("email", newEmail);

        response = given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .body(updateBody)
                .put(finalEndpoint);

        logger.info("PUT to {}", finalEndpoint);
        logger.debug("Update Response: {}", response.asString());
    }

    @And("saya akan mendapatkan detail user yang telah diperbarui")
    public void saya_akan_mendapatkan_detail_user_yang_telah_diperbarui() {
        response.then().statusCode(200)
                .body("firstName", notNullValue())
                .body("email", notNullValue());
    }

    @When("saya mengirim request DELETE untuk {string}")
    public void saya_mengirim_request_delete_untuk(String endpoint) {
        String finalEndpoint = BASE_URL + endpoint.replace("{id}", userId);

        response = given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .delete(finalEndpoint);

        logger.info("DELETE request to {}", finalEndpoint);
        logger.debug("Delete Response: {}", response.asString());
    }

    @And("user tidak lagi ditemukan")
    public void user_tidak_lagi_ditemukan() throws InterruptedException {
        Thread.sleep(2000); // delay untuk propagasi penghapusan

        given()
                .header("app-id", APP_ID)
                .contentType(ContentType.JSON)
                .get(BASE_URL + "/user/" + userId)
                .then()
                .statusCode(404);

        logger.info("User {} confirmed deleted", userId);
    }
}
