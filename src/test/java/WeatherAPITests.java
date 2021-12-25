import static io.restassured.RestAssured.*;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class WeatherAPITests {

    final static String url="https://data.weather.gov.hk/weatherAPI/opendata/weather.php?dataType=rhrread&lang=en";
    final static String baseURI = "https://data.weather.gov.hk/weatherAPI/opendata/weather.php";
    private static SoftAssert softAssert = new SoftAssert();

    //Main test class. Variables are reusable and easily scalable within the API.
    //Aim is to provide a generic API test, validating response, status, headers.
    @Test
    public void DailyWeatherReportTests(){
        getResponseTime();
        getResponseBody("rhrread","en");
        getValidateResponseStatus("rhrread","en");
        getValidateResponseHeaders("application/json; charset=utf-8","Apache");
        validateResponseNotNull("rainfall");
        validateResponseNotNull("warningMessage.data");
        validateExtractedValue("rainfall.data", "place", "Kwai Tsing");
        validateExtractedValue("temperature.data","unit","C");

    }

    //Get response time
    public static void getResponseTime(){
        System.out.println("The time taken to fetch the response: "+get(url)
                .timeIn(TimeUnit.MILLISECONDS) + " milliseconds");
    }

            //Get JSON Response body - This returns the entire body of the API response.
            // There isn't a validation here but will include some for the nested queries to ensure that the values
            // don't have any issues.
    public static void getResponseBody(String dataType, String lang){
        System.out.println(dataType +" Response Body is => ");
        //Specify query parameters for the API. In this case, dataType and lang are the variables: rhrread, en.
        // Changing these variables return different response bodies from the API
        //Fetch all the response fetched including log response, headers etc.
        ValidatableResponse responseBody = given().queryParam("dataType",dataType)
                .queryParam("lang",lang)
                .when().get(baseURI).then().log()
                .body();
        //Validates that the Response Body is not null
        Assert.assertNotNull(responseBody, " Response body is null");

    }

        //Get the response status. The most basic validation is to check if the status code of the request is in the
        // 2XX format
    public static void getValidateResponseStatus(String dataType, String lang){
        // Get the status code from the Response. A successful interaction with the webservice will
        // return status code: 200
        int statusCode= given().queryParam("dataType",dataType)
                .queryParam("lang",lang) .when().get(baseURI).getStatusCode();
        System.out.println("The response status is "+statusCode);
        // Assert that correct status code is returned. If returns another status code it will have an error.
        given().when().get(url).then().assertThat().statusCode(200);
    }

        // Get and validate the response headers. For this test case, I've chosen the Content Type and Server Type.
        // If there are discrepancies, it can indicate that the HTTP is has threatening content.
        // Ensures that only properly configured name-value pairs appear in the headers
        //There is a lot of flexibility in the code to add more headers if needed.
    public static void getValidateResponseHeaders(String expectedContentType, String expectedServerType){
        //print all headers in the response
        System.out.println("The headers in the response "+
                get(url).then().extract().headers());

        //Extract the content type and validate it to the expected value. Deviation should raise an error that needs
        // further attention. Future header validations can follow the same format.
        String contentType = get(url).then().extract().contentType();
        Assert.assertEquals(contentType /* actual value */, expectedContentType /* expected value */);

        String serverType = get(url).then().extract().header("Server");
        Assert.assertEquals(serverType /* actual value */, expectedServerType /* expected value */);

    }

    //Check response is not null with a given parameter. Flexible adjustments can be
    // made at the test method
    public static void validateResponseNotNull(String parameter)throws IllegalArgumentException{
        try {
            get(url)
                    .then()
                    .body(parameter, notNullValue()
                    );
        }catch(Exception e){
            softAssert.fail("Parameter not defined");
            System.out.println(e);
        }
    }

    //@Test
    public void testGetStringValue() {
        ArrayList<String> test =
        get(url)
                .then()
                .extract()
                .path("uvindex");
        System.out.println(test);

    }

    //Specific validation for nested queries. Used when the tester wants to verify a value.
    public void validateExtractedValue(String path, String data, String valueToFind) {
        //Extract specific value from path using method variables that can be adjusted in the main
        //test method. As the process finds the extracted value
        String extractedValue =
                get(url)
                        .then()
                        .extract()
                        .response()
                        .path( path+".find { it."+data+" == '"+valueToFind+"' }."+data);
        if(extractedValue != null) {
            System.out.println("Found extracted value from " + path + "-" + data + ": " + extractedValue);
        }
        Assert.assertNotNull(extractedValue,"Extracted value not found");
    }

}
