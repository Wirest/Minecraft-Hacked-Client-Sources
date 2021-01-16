package me.existdev.exist.gui.account.mcleaks;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import javax.net.ssl.HttpsURLConnection;
import me.existdev.exist.gui.account.mcleaks.Callback;
import me.existdev.exist.gui.account.mcleaks.RedeemResponse;

public class ModApi {
   // $FF: synthetic field
   public static final ExecutorService EXECUTOR_SERVICE = new ForkJoinPool();
   // $FF: synthetic field
   private static final String API_URL = "http://auth.mcleaks.net/v1/";
   // $FF: synthetic field
   private static final Gson gson = new Gson();

   // $FF: synthetic method
   public static void redeem(final String token, final Callback callback) {
      String url = "http://auth.mcleaks.net/v1/redeem";
      EXECUTOR_SERVICE.execute(new Runnable() {
         // $FF: synthetic method
         public void run() {
            URLConnection connection = ModApi.preparePostRequest("http://auth.mcleaks.net/v1/redeem", "{\"token\":\"" + token + "\"}");
            if(connection == null) {
               callback.done("An error occured! [R1]");
            } else {
               Object o = ModApi.getResult(connection);
               if(o instanceof String) {
                  callback.done(o);
               } else {
                  JsonObject jsonObject = (JsonObject)o;
                  if(jsonObject.has("mcname") && jsonObject.has("session")) {
                     RedeemResponse response = new RedeemResponse();
                     response.setMcName(jsonObject.get("mcname").getAsString());
                     response.setSession(jsonObject.get("session").getAsString());
                     callback.done(response);
                  } else {
                     callback.done("An error occured! [R2]");
                  }
               }
            }
         }
      });
   }

   // $FF: synthetic method
   private static URLConnection preparePostRequest(String url, String body) {
      try {
         Object e;
         if(url.toLowerCase().startsWith("https://")) {
            e = (HttpsURLConnection)(new URL(url)).openConnection();
         } else {
            e = (HttpURLConnection)(new URL(url)).openConnection();
         }

         ((HttpURLConnection)e).setConnectTimeout(10000);
         ((HttpURLConnection)e).setReadTimeout(10000);
         ((HttpURLConnection)e).setRequestMethod("POST");
         ((HttpURLConnection)e).setDoOutput(true);
         DataOutputStream wr = new DataOutputStream(((HttpURLConnection)e).getOutputStream());
         wr.write(body.getBytes("UTF-8"));
         wr.flush();
         wr.close();
         return (URLConnection)e;
      } catch (Exception var4) {
         var4.printStackTrace();
         return null;
      }
   }

   // $FF: synthetic method
   private static Object getResult(URLConnection urlConnection) {
      try {
         BufferedReader e = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
         StringBuilder result = new StringBuilder();

         String line;
         while((line = e.readLine()) != null) {
            result.append(line);
         }

         e.close();
         JsonElement jsonElement = (JsonElement)gson.fromJson(result.toString(), JsonElement.class);
         System.out.println(result.toString());
         return jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("success")?(!jsonElement.getAsJsonObject().get("success").getAsBoolean()?(jsonElement.getAsJsonObject().has("errorMessage")?jsonElement.getAsJsonObject().get("errorMessage").getAsString():"An error occured! [G4]"):(!jsonElement.getAsJsonObject().has("result")?"An error occured! [G3]":(jsonElement.getAsJsonObject().get("result").isJsonObject()?jsonElement.getAsJsonObject().get("result").getAsJsonObject():null))):"An error occured! [G1]";
      } catch (Exception var5) {
         var5.printStackTrace();
         return "An error occured! [G2]";
      }
   }
}
