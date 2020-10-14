import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

class utility{
    public static JSONObject getJsonFromURL(URL url) throws IOException, JSONException {
        return new JSONObject(IOUtils.toString(url, Charset.forName("UTF-8")));
    }

    public static JSONObject getJsonFromURL(String url) throws IOException, JSONException {
        return new JSONObject(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
    }
}
