package com.thealtening.api.retriever;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.thealtening.api.TheAlteningException;
import com.thealtening.api.response.Account;
import com.thealtening.api.response.License;
import org.lwjgl.Sys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface DataRetriever {

    Logger LOGGER = Logger.getLogger("The Altening");
    Gson gson = new Gson();

    String BASE_URL = "http://api.thealtening.com/v1/";
    String LICENCE_URL = BASE_URL + "license?token=";
    String GENERATE_URL = BASE_URL + "generate?info=true&token=";
    String PRIVATE_ACC_URL = BASE_URL + "private?acctoken=";
    String FAVORITE_ACC_URL = BASE_URL + "favorite?acctoken=";

    License getLicence();

    Account getAccount();

    boolean isPrivate(String token) throws TheAlteningException;

    boolean isFavorite(String token) throws TheAlteningException;

    void updateKey(String newApiKey);

    default JsonObject retrieveData(String url) throws TheAlteningException {
        String response;
        JsonObject jsonObject;
        try {
            response = connect(url);
            jsonObject = gson.fromJson(response, JsonObject.class);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while reading retrieved data from the website");
            throw new TheAlteningException("IO", e.getCause());
        }

        if (jsonObject == null) {
            LOGGER.log(Level.SEVERE, "Error while parsing website's response");
            throw new TheAlteningException("JSON", "Parsing error: \n" + response);
        }

        if (jsonObject.has("error") && jsonObject.has("errorMessage")) {
            LOGGER.log(Level.SEVERE, "The website returned, type: " + jsonObject.get("error").getAsString() + ". Details:" + jsonObject.get("errorMessage").getAsString());
            throw new TheAlteningException("Connection", "Bad response");
        }

        return jsonObject;
    }

    default boolean isSuccess(JsonObject jsonObject) {
        return jsonObject.has("success")
                && jsonObject.get("success").getAsBoolean();
    }

    default String connect(String link) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        URLConnection connection = new URL(link).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        InputStream connectionStream = connection.getInputStream();

        String encodingId = connection.getContentEncoding();
        Charset encoding;
        try {
            encoding = encodingId == null ? StandardCharsets.UTF_8 : Charset.forName(encodingId);
        } catch (UnsupportedCharsetException ex) {
            encoding = StandardCharsets.UTF_8;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connectionStream, encoding))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder
                        .append(line)
                        .append("\n");
            }
        }

        return stringBuilder.toString();
    }
}
