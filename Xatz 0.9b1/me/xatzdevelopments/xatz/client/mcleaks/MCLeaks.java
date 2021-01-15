/*     */ package me.xatzdevelopments.xatz.client.mcleaks;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;

import net.minecraft.util.Session;

/*     */ import java.io.BufferedReader;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MCLeaks {
/*     */   private static Session session;
/*     */   public static Session savedSession = null;
/*  24 */   private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
/*     */   
/*  25 */   private static final Gson gson = new Gson();
/*     */   
/*     */   public static boolean isAltActive() {
/*  28 */     return (session != null);
/*     */   }
/*     */   
/*     */   public static Session getSession() {
/*  32 */     return session;
/*     */   }
/*     */   
/*     */   public static void refresh(Session session) {
/*  36 */     MCLeaks.session = session;
/*     */   }
/*     */   
/*     */   public static void remove() {
/*  40 */     session = null;
/*     */   }
/*     */   
/*     */   public static void redeem(String token, Callback<Object> callback) {
/*  44 */     EXECUTOR_SERVICE.execute(() -> {
/*     */           URLConnection connection = preparePostRequest("{\"token\":\"" + token + "\"}");
/*     */           if (connection == null) {
/*     */             callback.done("An error occured! [R1]");
/*     */             return;
/*     */           } 
/*     */           Object o = getResult(connection);
/*     */           if (o instanceof String) {
/*     */             callback.done(o);
/*     */             return;
/*     */           } 
/*     */           JsonObject jsonObject = (JsonObject)o;
/*     */           if (jsonObject == null)
/*     */             return; 
/*     */           if (!jsonObject.has("mcname") || !jsonObject.has("session")) {
/*     */             callback.done("An error occured! [R2]");
/*     */             return;
/*     */           } 
/*     */           callback.done(new RedeemResponse(jsonObject.get("mcname").getAsString(), jsonObject.get("session").getAsString()));
/*     */         });
/*     */   }
/*     */   
/*     */   private static URLConnection preparePostRequest(String body) {
/*     */     try {
/*  74 */       HttpsURLConnection connection = (HttpsURLConnection)(new URL("https://auth.mcleaks.net/v1/redeem")).openConnection();
/*  75 */       connection.setConnectTimeout(10000);
/*  76 */       connection.setReadTimeout(10000);
/*  77 */       connection.setRequestMethod("POST");
/*  78 */       connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; rv:2.2) Gecko/20110201");
/*  79 */       connection.setDoOutput(true);
/*  81 */       DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
/*  82 */       dataOutputStream.write(body.getBytes(StandardCharsets.UTF_8));
/*  83 */       dataOutputStream.flush();
/*  84 */       dataOutputStream.close();
/*  86 */       return connection;
/*  87 */     } catch (Exception e) {
/*  88 */       e.printStackTrace();
/*  89 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Object getResult(URLConnection urlConnection) {
/*     */     try {
/*  95 */       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
/*  96 */       StringBuilder stringBuilder = new StringBuilder();
/*     */       String line;
/*  99 */       while ((line = bufferedReader.readLine()) != null)
/* 100 */         stringBuilder.append(line); 
/* 102 */       bufferedReader.close();
/* 103 */       JsonElement jsonElement = (JsonElement)gson.fromJson(stringBuilder.toString(), JsonElement.class);
/* 105 */       if (!jsonElement.isJsonObject() || !jsonElement.getAsJsonObject().has("success"))
/* 106 */         return "An error occured! [G1]"; 
/* 108 */       if (!jsonElement.getAsJsonObject().get("success").getAsBoolean())
/* 109 */         return jsonElement.getAsJsonObject().has("errorMessage") ? jsonElement.getAsJsonObject().get("errorMessage").getAsString() : "An error occured! [G4]"; 
/* 111 */       if (!jsonElement.getAsJsonObject().has("result"))
/* 112 */         return "An error occured! [G3]"; 
/* 114 */       return jsonElement.getAsJsonObject().get("result").isJsonObject() ? jsonElement.getAsJsonObject().get("result").getAsJsonObject() : null;
/* 115 */     } catch (Exception e) {
/* 116 */       e.printStackTrace();
/* 117 */       return "An error occured! [G2]";
/*     */     } 
/*     */   }
/*     */ }


