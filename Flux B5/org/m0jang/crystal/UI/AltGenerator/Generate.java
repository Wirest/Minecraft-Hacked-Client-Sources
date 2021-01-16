package org.m0jang.crystal.UI.AltGenerator;

import com.google.common.base.Charsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

public class Generate {
   private static CookieStore cookie;

   public static String[] GetAltFromFastAlts(String[] idents) {
      if (cookie == null) {
         String token = GETFAToken();
         System.out.println("Token Is " + token);
         if (!LoginFA(token, idents)) {
            System.out.println("Failed To Login");
            return null;
         }

         System.out.println("Logged In Successfully!");
      }

      System.out.println("Generating Alt...");
      String[] alt = GenerateAltFA(idents);
      System.out.println("Generated");
      return alt;
   }

   private static boolean LoginFA(String token, String[] idents) {
      try {
         RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
         List headers = new ArrayList();
         headers.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"));
         headers.add(new BasicHeader("Accept-Charset", "utf-8"));
         headers.add(new BasicHeader("Accept-Language", "en;q=0.8"));
         headers.add(new BasicHeader("Referer", "https://fastalts.com/login"));
         headers.add(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
         headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36"));
         HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setDefaultHeaders(headers).setDefaultCookieStore(cookie).build();
         HttpPost httpPost = new HttpPost("https://fastalts.com/login.php");
         httpPost.setEntity(new StringEntity("username=" + idents[0] + "&password=" + idents[1] + "&token=" + token + "&login=Login&remember=on", Charsets.UTF_8));
         HttpResponse response = httpClient.execute(httpPost);
         int responseStatus = response.getStatusLine().getStatusCode();
         System.out.println("Responce Code : " + responseStatus);
         return responseStatus == 302;
      } catch (Exception var8) {
         var8.printStackTrace();
         return false;
      }
   }

   private static String GETFAToken() {
      if (cookie == null) {
         cookie = new BasicCookieStore();
      }

      try {
         RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
         List headers = new ArrayList();
         headers.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"));
         headers.add(new BasicHeader("Accept-Charset", "utf-8"));
         headers.add(new BasicHeader("Accept-Language", "en;q=0.8"));
         headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36"));
         HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setDefaultHeaders(headers).setDefaultCookieStore(cookie).build();
         HttpGet httpGet = new HttpGet("https://fastalts.com/login");
         HttpResponse response = httpClient.execute(httpGet);
         int responseStatus = response.getStatusLine().getStatusCode();
         System.out.println("Responce Code : " + responseStatus);
         String body = EntityUtils.toString(response.getEntity(), "UTF-8");
         System.out.println("body: " + body);
         Iterator var8 = cookie.getCookies().iterator();

         while(var8.hasNext()) {
            Cookie cookie = (Cookie)var8.next();
            System.out.println("cookie : " + cookie.toString());
         }

         Pattern pattern = Pattern.compile("name=.token. value=.([^<]+)\"", 2);
         Matcher matcher = pattern.matcher(body);
         return matcher.find() ? matcher.group(1) : null;
      } catch (Exception var9) {
         var9.printStackTrace();
         return null;
      }
   }

   private static String[] GenerateAltFA(String[] UserPass) {
      try {
         RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
         List headers = new ArrayList();
         headers.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"));
         headers.add(new BasicHeader("Accept-Charset", "utf-8"));
         headers.add(new BasicHeader("Accept-Language", "en;q=0.8"));
         headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36"));
         headers.add(new BasicHeader("Referer", "https://fastalts.com/generator"));
         headers.add(new BasicHeader("X-Requested-With", "XMLHttpRequest"));
         HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setDefaultHeaders(headers).setDefaultCookieStore(cookie).build();
         HttpGet httpGet = new HttpGet("https://fastalts.com/cocksuckingmom/generate.php?generator=1&username=" + UserPass[0]);
         HttpResponse response = httpClient.execute(httpGet);
         int responseStatus = response.getStatusLine().getStatusCode();
         System.out.println("Responce Code : " + responseStatus);
         String body = EntityUtils.toString(response.getEntity(), "UTF-8");
         body = body.replaceAll("\n", "");
         body = body.replaceAll("\r", "");
         System.out.println(body);
         return new String[]{body.split(":")[0], body.split(":")[1]};
      } catch (Exception var8) {
         var8.printStackTrace();
         return null;
      }
   }
}
