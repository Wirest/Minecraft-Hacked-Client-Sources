package com.thealtening.api.retriever;

import com.google.gson.Gson;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public interface DataRetriever {
   Logger LOGGER = Logger.getLogger("The Altening");
   Gson gson = new Gson();
   String BASE_URL = "http://api.thealtening.com/v1/";
   String LICENCE_URL = "http://api.thealtening.com/v1/license?token=";
   String GENERATE_URL = "http://api.thealtening.com/v1/generate?info=true&token=";
   String PRIVATE_ACC_URL = "http://api.thealtening.com/v1/private?acctoken=";
   String FAVORITE_ACC_URL = "http://api.thealtening.com/v1/favorite?acctoken=";

   License getLicense();

   Account getAccount();

   boolean isPrivate(String var1) throws TheAlteningException;

   boolean isFavorite(String var1) throws TheAlteningException;

   void updateKey(String var1);

   default JsonObject retrieveData(String url) throws TheAlteningException {
      String response;
      JsonObject jsonObject;
      try {
         response = this.connect(url);
         jsonObject = (JsonObject)gson.fromJson(response, JsonObject.class);
      } catch (IOException var5) {
         LOGGER.log(Level.SEVERE, "Error while reading retrieved data from the website");
         throw new TheAlteningException("IO", var5.getCause());
      }

      if (jsonObject == null) {
         LOGGER.log(Level.SEVERE, "Error while parsing website's response");
         throw new TheAlteningException("JSON", "Parsing error: \n" + response);
      } else if (jsonObject.has("error") && jsonObject.has("errorMessage")) {
         LOGGER.log(Level.SEVERE, "The website returned, type: " + jsonObject.get("error").getAsString() + ". Details:" + jsonObject.get("errorMessage").getAsString());
         throw new TheAlteningException("Connection", "Bad response");
      } else {
         return jsonObject;
      }
   }

   default boolean isSuccess(JsonObject jsonObject) {
      return jsonObject.has("success") && jsonObject.get("success").getAsBoolean();
   }

   default String connect(String link) throws IOException {
      StringBuilder stringBuilder = new StringBuilder();
      URLConnection connection = (new URL(link)).openConnection();
      InputStream connectionStream = connection.getInputStream();
      String encodingId = connection.getContentEncoding();

      Charset encoding;
      try {
         encoding = encodingId == null ? StandardCharsets.UTF_8 : Charset.forName(encodingId);
      } catch (UnsupportedCharsetException var18) {
         encoding = StandardCharsets.UTF_8;
      }

      BufferedReader reader = new BufferedReader(new InputStreamReader(connectionStream, encoding));
      Throwable var8 = null;

      try {
         String line;
         try {
            while((line = reader.readLine()) != null) {
               stringBuilder.append(line).append("\n");
            }
         } catch (Throwable var19) {
            var8 = var19;
            throw var19;
         }
      } finally {
         if (reader != null) {
            if (var8 != null) {
               try {
                  reader.close();
               } catch (Throwable var17) {
                  var8.addSuppressed(var17);
               }
            } else {
               reader.close();
            }
         }

      }

      return stringBuilder.toString();
   }
}
