import static io.restassured.RestAssured.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.*;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
//import io.restassured.mapper.ObjectMapper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherAPITests {

    final static String url="https://data.weather.gov.hk/weatherAPI/opendata/weather.php?dataType=flw&lang=en";

    @Test
    public void DailyWeatherReportTests(){
    APISetup();
        getBodyDetails();
        getStatusLine(200);
        getHeaders("application/json; charset=utf-8", "Apache", null);
        //allHeaderDetails();

            }



    //Reusable Method for API Setup with Relaxed HTTPS Validation
    public static void APISetup(){
        //Address the SSL Handshake error due to unrecognised certificate
        useRelaxedHTTPSValidation();
        // Specify the base URL to the RESTful web service
        baseURI = "https://data.weather.gov.hk/weatherAPI/opendata/weather.php";
    }

    public static void getBodyDetails()
    {
        APISetup();
        // Get the RequestSpecification of the request that you want to sent
        // to the server. The server is specified by the BaseURI that we have
        // specified in the above step.
        RequestSpecification httpRequest = given();
        // Make a request to the server by specifying the method Type and the method URL.
        // This will return the Response from the server. Store the response in a variable.
        Response response = httpRequest.request(Method.GET, "?dataType=rhrread&lang=en");
        // Now let us print the body of the message to see what response
        // we have received from the server
        String responseBody = response.getBody().asString();
        System.out.println("Response Body is =>  " + responseBody);
    }
    public static void getStatusLine(int expectedValue)
    {
        APISetup();
        RequestSpecification httpRequest = given();
        Response response = httpRequest.request(Method.GET, "?dataType=rhrread&lang=en");

        // Get the status code from the Response. A successful interaction with the webservice will return status code: 200
        int statusCode = response.getStatusCode();
        // Assert that correct status code is returned.
        Assert.assertEquals(statusCode /*actual value*/, expectedValue /*expected value*/, "Correct status code returned");
        System.out.println("Status Code: " + statusCode);
    }
    public static void getHeaders(String expectedContentType,String expectedServerType, String expectedContentEncoding)
    {
        APISetup();
        RequestSpecification httpRequest = given();
        Response response = httpRequest.request(Method.GET, "?dataType=rhrread&lang=en");

        // Reader header of a give name. In this line we will get Header named Content-Type
        String contentType = response.header("Content-Type");
        Assert.assertEquals(contentType /* actual value */, expectedContentType /* expected value */);
        System.out.println("Content-Type value: " + contentType);

        // Reader header of a give name. In this line we will get Header named Server
        String serverType =  response.header("Server");
        Assert.assertEquals(serverType /* actual value */, expectedServerType /* expected value */);
        System.out.println("Server value: " + serverType);

        // Reader header of a give name. In this line we will get Header named Content-Encoding
        String contentEncoding = response.header("Content-Encoding");
        Assert.assertEquals(contentEncoding /* actual value */, expectedContentEncoding /* expected value */);
        System.out.println("Content-Encoding: " + contentEncoding);
    }
    public void allHeaderDetails()
    {
        APISetup();
        RequestSpecification httpRequest = given();
        Response response = httpRequest.request(Method.GET, "?dataType=rhrread&lang=en");

        // Get all the headers. Return value is of type Headers.
        // Headers class implements Iterable interface, hence we
        // can apply an advance for loop to go through all Headers
        // as shown in the code below
        Headers allHeaders = response.headers();

        // Iterate over all the Headers
        for(Header header : allHeaders)
        {
            System.out.println("Key: " + header.getName() + " Value: " + header.getValue());
        }
    }


}
