import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class APITests {

    @Test
    void test1(){
        RestAssured.useRelaxedHTTPSValidation();
        Response response = RestAssured.get("https://data.weather.gov.hk/weatherAPI/opendata/weather.php?dataType=flw&lang=en");
        response.getStatusCode();
        response.getBody();
        System.out.println("Response :" + response.asString());
        System.out.println("status code: "+response.getStatusCode());
        System.out.println("body: " + response.getBody().asString());
        System.out.println("Time taken: " + response.getTime());
        System.out.println("header: " + response.getHeader("content-type"));

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);

    }

}
