import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.SSLHandshakeException;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.spec.ECField;

import static io.restassured.RestAssured.useRelaxedHTTPSValidation;

public class JsonReader {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException, SSLHandshakeException {
        InputStream is = new URL(url).openStream();
        useRelaxedHTTPSValidation();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

}
