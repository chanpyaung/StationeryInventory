package team5.ad.sa40.stationeryinventory;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class JSONParser {

    static String readStream(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            is.close();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return(sb.toString());
    }

    public static String getStream(String url) {
        InputStream is = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readStream(is);
    }

    public static String postStream(String url, String data) {
        InputStream is = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(data));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            Log.i("Httpresponse:",httpResponse.toString());
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("JSON post error:", e.toString());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.e("JSON post error:", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("JSON post error:", e.toString());
        }
        Log.e("is:", is.toString());
        return readStream(is);
    }

    public static String postStream2(String url, String data) {
        InputStream is = null;
        String flag = "";
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(data));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            Log.i("Httpresponse:",httpResponse.toString());
            Log.i("Http Response:", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
            if(String.valueOf(httpResponse.getStatusLine().getStatusCode()).trim().equals("404")){
                flag = "false";
            }
            else{
                flag = "true";
            }
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("JSON post error:", e.toString());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.e("JSON post error:", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("JSON post error:", e.toString());
        }
        Log.e("is:", is.toString());
        return flag;
    }

    public static JSONObject getJSONFromUrl(String url) {
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(getStream(url));
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jObj;
    }
    public static String getJSONFromUrl2(String url) {
        String jObj = "";
        try {
            Log.e("getStreamValue", getStream(url));
            jObj = getStream(url).trim();
        } catch (Exception e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jObj;
    }

    public static JSONObject getJSONFromUrlPOST(String url, String data) {
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(postStream(url,data));
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jObj;
    }

    public static String getJSONFromUrlPOST2(String url, String data) {
        String jObj = "";
        try {
            jObj = postStream2(url,data).trim();
        } catch (Exception e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jObj;
    }

    public static JSONArray getJSONArrayFromUrl(String url) {
        Log.i("URL", url);
        JSONArray jArray = null;
        try {
            jArray = new JSONArray(getStream(url));
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing array " + e.toString());
        }
        return jArray;
    }
}
