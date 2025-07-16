package api.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class DeleteTest extends BaseTest{
    @Test
    public void deleteUser() {
        //ganti dengan ID user yg valid
        String userId = "";

        //set base url untuk restAssured
        RestAssured.baseURI = "https://dummyapi.io/data/v1"; // Ganti dengan base URI yang sesuai

        //langkah 1 : periksa apakah user ID masih ada sebelum menghapusnya
        Response checkUser = given()
                .header("app-id", "624c9429450430b574dcf17c") //Ganti dengan APP ID asli
                .when()
                .get("/user/" + userId)
                .then()
                .extract().response();

        System.out.println("Check user response: " + checkUser.getBody().asPrettyString());

        //jika user tidak ditemukan, tidak perlu lanjut req delete
        if (checkUser.statusCode() == 404) {
            System.out.println("User with ID " + userId + " not found.");
            return;
        }

        //langkah 2 : mengirim request DELETE untuk menghapus user jika masih ada
        Response deleteResponse = given()
                .header("app-id", "624c9429450430b574dcf17c") //Ganti dengan APP ID asli
                .when()
                .delete("/user/" + userId)
                .then()
                .log().all() // log semua request dan response untuk debugging
                .statusCode(200) // pastikan status code adalah 200 OK
                .extract().response();


        //langkah 3 : validasi hasil delete
        System.out.println("Delete user response: " + deleteResponse.getBody().asPrettyString());

        if (deleteResponse.statusCode() == 200) {
            deleteResponse.then().body("message", containsString("User deleted successfully"));
        } else {
            throw new AssertionError("Unexpected response : " + deleteResponse.statusCode());
        }
    }
}


