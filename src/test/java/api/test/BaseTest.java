package api.test;

import io.cucumber.java.BeforeAll;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class BaseTest {
    protected static RequestSpecification request;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://dummyapi.io/data/v1"; // Ganti dengan base URI yang sesuai
        request = RestAssured.given()
                .header("app-id", "624c9429450430b574dcf17c") // Ganti dengan APP ID asli
                .contentType("application/json")
                .accept("application/json");
    }
}
