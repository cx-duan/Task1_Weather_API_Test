import static io.restassured.RestAssured.*;


import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.util.*;

import java.io.IOException;

public class APITests {

    @Test
    void test1(){
        useRelaxedHTTPSValidation();
        Response response = get("https://data.weather.gov.hk/weatherAPI/opendata/weather.php?dataType=flw&lang=en");
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
    @Test
    void test2 (){
        given().
                get("https://data.weather.gov.hk/weatherAPI/opendata/weather.php?dataType=flw&lang=en").
                then().
                statusCode(200);
    }

    @Test
    void extractJSON (){

        baseURI = "https://data.weather.gov.hk/weatherAPI/opendata/weather.php?dataType=rhrread&lang=en";
        RequestSpecification httpRequest = given();
        Response response = httpRequest.request(Method.GET,"/Eastern District");
        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
//      String rainfall = given().contentType(ContentType.JSON).log().all()
//              .get("/99").then().extract().path("rainfall");
//        System.out.println(rainfall);

    }

    //Reusable Method for API Setup with Relaxed HTTPS Validation
    public void APISetup(){
        //Address the SSL Handshake error due to unrecognised certificate
        useRelaxedHTTPSValidation();
        // Specify the base URL to the RESTful web service
        baseURI = "https://data.weather.gov.hk/weatherAPI/opendata/weather.php";
    }

    @Test
    public void GetWeatherDetails()
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
        // we have recieved from the server
        String responseBody = response.getBody().asString();
        System.out.println("Response Body is =>  " + responseBody);
    }

    @Test
    public void GetWeatherStatusLine()
    {
        APISetup();
        RequestSpecification httpRequest = given();
        Response response = httpRequest.request(Method.GET, "?dataType=rhrread&lang=en");

        // Get the status code from the Response. In case of
        // a successful interaction with the web service, we
        // should get a status code of 200.
        int statusCode = response.getStatusCode();

        // Assert that correct status code is returned.
        Assert.assertEquals(statusCode /*actual value*/, 200 /*expected value*/, "Correct status code returned");
        System.out.println("Status Code: " + statusCode);
    }
    @Test
    public void GetWeatherHeaders()
    {
        APISetup();
        RequestSpecification httpRequest = given();
        Response response = httpRequest.request(Method.GET, "?dataType=rhrread&lang=en");


        // Reader header of a give name. In this line we will get
        // Header named Content-Type
        String contentType = response.header("Content-Type");
        Assert.assertEquals(contentType /* actual value */, "application/json; charset=utf-8" /* expected value */);
        System.out.println("Content-Type value: " + contentType);

        // Reader header of a give name. In this line we will get
        // Header named Server
        String serverType =  response.header("Server");
        Assert.assertEquals(serverType /* actual value */, "Apache" /* expected value */);
        System.out.println("Server value: " + serverType);

        // Reader header of a give name. In this line we will get
        // Header named Content-Encoding
        String contentEncoding = response.header("Content-Encoding");
        Assert.assertEquals(contentEncoding /* actual value */, null /* expected value */);
        System.out.println("Content-Encoding: " + contentEncoding);

    }
    @Test
    public void IteratingOverHeaders()
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

    @Test
    public void WeatherMessageBody()
    {
        APISetup();
        RequestSpecification httpRequest = given();
        Response response = httpRequest.request(Method.GET, "?dataType=rhrread&lang=en");

        // Retrieve the body of the Response
        ResponseBody body = response.getBody();

        // By using the ResponseBody.asString() method, we can convert the  body
        // into the string representation.
        System.out.println("Response Body is: " + body.asString());
        // To check for sub string presence get the Response body as a String.
        // Do a String.contains
        String bodyAsString = body.asString();
        Assert.assertEquals(bodyAsString.contains("rainfall") /*Expected value*/,
                true /*Actual Value*/, "Response body contains rainfall");
    }

    @Test
    public void VerifyRainfallInJsonResponse()
    {
        APISetup();
        RequestSpecification httpRequest = given();
        Response response = httpRequest.request(Method.GET, "?dataType=rhrread&lang=en");

        // First get the JsonPath object instance from the Response interface
        JsonPath jsonPathEvaluator = response.jsonPath();

        // Then simply query the JsonPath object to get a String value of the node
        // specified by JsonPath: City (Note: You should not put $. in the Java code)
        LinkedHashMap rainfall = jsonPathEvaluator.get("rainfall");

        // Let us print the city variable to see what we got
        System.out.println("Rainfall received from Response " + rainfall);



        // Validate the response
        //Assert.assertEquals(rainfall, "data", "Correct dataset received in the Response");

    }

    @Test
    public void DisplayAllNodesInWeatherAPI()
    {
        APISetup();
        RequestSpecification httpRequest = given();
        Response response = httpRequest.request(Method.GET, "?dataType=rhrread&lang=en");

        // First get the JsonPath object instance from the Response interface
        JsonPath jsonPathEvaluator = response.jsonPath();

        // Let us print the city variable to see what we got
        System.out.println("Rainfall received from Response " + jsonPathEvaluator.get("rainfall"));

        // Print the temperature node
        System.out.println("Icon received from Response " + jsonPathEvaluator.get("icon"));

        // Print the humidity node
        System.out.println("iconUpdateTime received from Response " + jsonPathEvaluator.get("iconUpdateTime"));

        // Print weather description
        System.out.println("updateTime description received from Response " + jsonPathEvaluator.get("updateTime"));

        // Print Wind Speed
        System.out.println("temperature received from Response " + jsonPathEvaluator.get("temperature"));

        // Print Wind Direction Degree
        System.out.println("warningMessage received from Response " + jsonPathEvaluator.get("warningMessage"));
    }

    @Test
    public void queryParameter() {
        useRelaxedHTTPSValidation();
        baseURI ="https://data.weather.gov.hk/weatherAPI/opendata.php";
        RequestSpecification request = given();

        Response response = request.queryParam("dataType", "rhrread")
                .queryParam("lang", "en")
                .get("/weather");

        String jsonString = response.asString();
        System.out.println(response.getStatusCode());
        Assert.assertEquals(jsonString.contains("rainfall"), true);

    }

    @Test
    public void JsonPathUsage() throws MalformedURLException
    {
        APISetup();
        RequestSpecification httpRequest = given();
        Response response = httpRequest.request(Method.GET, "?dataType=rhrread&lang=en");

        // First get the JsonPath object instance from the Response interface
        JsonPath jsonPathEvaluator = response.jsonPath();

        // Read all the books as a List of String. Each item in the list
        // represent a book node in the REST service Response

        Map<Object, Object> allRainfall = jsonPathEvaluator.getMap("rainfall.data");
        System.out.println(allRainfall);
//        for(Map.Entry<String,List<String>> entry : allRainfall.entrySet()) {
//            String key = entry.getKey();
//            List<String> value = entry.getValue();
//            System.out.println(value);
//        }
        for (Map.Entry<Object, Object> entry : allRainfall.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }

//        List<String> allRainfall = jsonPathEvaluator.getList("rainfall.data.place");
//        for(String place : allRainfall){
//            System.out.println(place);
//        }
    }

    public static void parseObject(JSONObject json, String key){
        System.out.println(json.get(key));
    }
    public static void getKey(JSONObject json, String key){
        boolean exists = json.has(key);
        Iterator<?>keys;
        String nextKeys;

        if(!exists){
            keys=json.keys();
            while(keys.hasNext()){
                nextKeys = (String) keys.next();
                try{
                    if(json.get(nextKeys) instanceof JSONObject){
                       if(exists == false){
                           getKey(json.getJSONObject(nextKeys), key);
                       }
                    }else if (json.get(nextKeys) instanceof JSONArray){
                        JSONArray jsonArray = json.getJSONArray(nextKeys);
                        for(int i = 0; i<jsonArray.length();i++){
                            String jsonarrayString = jsonArray.get(i).toString();
                            JSONObject innerJSON = new JSONObject(jsonarrayString);
                            if(exists == false){
                                getKey(innerJSON, nextKeys);
                            }
                        }
                    }
                }catch (Exception e){

                }
            }
        }else{
            parseObject(json,key);
        }


    }



@Test
    public void getValues() throws IOException {
        APISetup();
        RequestSpecification httpRequest = given();
        Response response = httpRequest.request(Method.GET, "?dataType=rhrread&lang=en");
        JsonPath jsonPathEvaluator = response.jsonPath();
        String url = "https://data.weather.gov.hk/weatherAPI/opendata/weather.php?dataType=rhrread&lang=en";
        useRelaxedHTTPSValidation();

        JSONObject json = JsonReader.readJsonFromUrl(url);

//    JSONObject allRainfall = new JSONObject((JSONObject) jsonPathEvaluator.getJsonObject("rainfall"));
        getKey(json,"place");
    }
@Test
    public static void getSpecificPartOfResponseBody(){
        String url = "https://data.weather.gov.hk/weatherAPI/opendata/weather.php?dataType=rhrread&lang=en";
        ArrayList<String> amounts = when().get(url).then().extract().path("rainfall.data.place") ;
        for(String a:amounts){

            System.out.println(a);
        }

    }
}

