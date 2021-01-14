/*
 * Copyright (C) 2019 TheAltening
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.thealtening.api.retriever;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.thealtening.api.TheAlteningException;
import com.thealtening.api.response.Account;
import com.thealtening.api.response.License;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface DataRetriever {

    Logger LOGGER = Logger.getLogger("The Altening");
    Gson gson = new Gson();

    String BASE_URL = "http://api.thealtening.com/v2/";
    String LICENCE_URL = BASE_URL + "license?key=";
    String GENERATE_URL = BASE_URL + "generate?info=true&key=";
    String PRIVATE_ACC_URL = BASE_URL + "private?token=";
    String FAVORITE_ACC_URL = BASE_URL + "favorite?token=";
    String PRIVATES_URL = BASE_URL + "privates?key=";
    String FAVORITES_URL = BASE_URL + "favorites?key=";

    License getLicense();

    Account getAccount();

    boolean isPrivate(String token) throws TheAlteningException;

    boolean isFavorite(String token) throws TheAlteningException;

    List<Account> getPrivatedAccounts();

    List<Account> getFavoriteAccounts();

    void updateKey(String newApiKey);

    default JsonElement retrieveData(String url) throws TheAlteningException {
        String response;
        JsonElement jsonElement;
        try {
            response = connect(url);
            jsonElement = gson.fromJson(response, JsonElement.class);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while reading retrieved data from the website");
            throw new TheAlteningException("IO", e.getCause());
        }

        if (jsonElement == null) {
            LOGGER.log(Level.SEVERE, "Error while parsing website's response");
            throw new TheAlteningException("JSON", "Parsing error: \n" + response);
        }

        if (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("error") && jsonElement.getAsJsonObject().has("errorMessage")) {
            LOGGER.log(Level.SEVERE, "The website returned, type: " + jsonElement.getAsJsonObject().get("error").getAsString() + ". Details:" + jsonElement.getAsJsonObject().get("errorMessage").getAsString());
            throw new TheAlteningException("Connection", "Bad response");
        }

        return jsonElement;
    }

    default boolean isSuccess(JsonObject jsonObject) {
        return jsonObject.has("success")
                && jsonObject.get("success").getAsBoolean();
    }

    default String connect(String link) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        URLConnection connection = new URL(link).openConnection();
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
