package com.example.booklistingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public final class QueryUtils {

    public static List<Book> fetchBookData(String requestUrl) {
        if (requestUrl == null)
            return null;
        URL url = createUrl(requestUrl);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractFeaturesFromJson(jsonResponse);
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = null;
        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(1000);
            urlConnection.setConnectTimeout(1500);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else
                Log.e("Query Utils", "Error response code: " + urlConnection.getResponseCode());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                // Closing the input stream could throw an IOException, which is why the makeHttpRequest(URL url) method signature specifies than an IOException could be thrown.
                inputStream.close();
        }
        return jsonResponse;
    }

    //Convert the InputStream into a String which contains the whole JSON response from the server.
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Book> extractFeaturesFromJson(String JsonResponse) {
        String title = null, authors = null, description = null;
        Bitmap image = null;
        List<Book> book = new ArrayList<>();
        if (JsonResponse == null)
            return book;

        try {
            JSONObject root = new JSONObject(JsonResponse);

            JSONArray bookArray = root.getJSONArray("items");

            for (int i = 0; i < bookArray.length(); i++) {

                JSONObject items = bookArray.optJSONObject(i);
                JSONObject volumeInfo = items.optJSONObject("volumeInfo");

                if (volumeInfo.has("title"))
                    title = "Tittle : " + volumeInfo.getString("title");

                if (volumeInfo.has("authors")) {
                    JSONArray author = volumeInfo.getJSONArray("authors");
                    authors = "Author : " + author.optString(0);
                }

                if (volumeInfo.has("imageLinks")) {
                    JSONObject imageObject = volumeInfo.optJSONObject("imageLinks");
                    String imageLink = imageObject.optString("thumbnail");
                    imageLink = imageLink.replace("http", "https");
                    URL imageUrl = new URL(imageLink);
                    image = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                }

                if (volumeInfo.has("description"))
                    description = "Des : " + volumeInfo.optString("description");

                JSONObject accessInfo = items.optJSONObject("accessInfo");
                String url = accessInfo.optString("webReaderLink");

                //Adding the value in ArrayList
                book.add(new Book(image, title, authors, description, url));
            }
        } catch (Exception e) {
            Log.e("QueryUtils", "Problem parsing the Book JSON results", e);
        }
        return book;
    }

}
