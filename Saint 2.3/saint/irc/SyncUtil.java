package saint.irc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.client.Minecraft;

public class SyncUtil {
   private static final String webURL = "";

   private static ArrayList parseDataSetsFromLatestList() {
      try {
         String url = "";
         HttpURLConnection urlConnection = (HttpURLConnection)(new URL(url)).openConnection();
         urlConnection.setRequestMethod("GET");
         InputStream is = urlConnection.getInputStream();
         BufferedReader rd = new BufferedReader(new InputStreamReader(is));
         ArrayList tempList = new ArrayList();

         String line;
         while((line = rd.readLine()) != null) {
            try {
               tempList.add(parseData(line));
            } catch (Exception var7) {
            }
         }

         return tempList;
      } catch (Exception var8) {
         var8.printStackTrace();
         return null;
      }
   }

   private static SyncData parseData(String raw) {
      String[] dataset = raw.split(";");
      Date date = new Date(Long.parseLong(dataset[0]));
      String var3 = dataset[1];
      String clientInfo = dataset[2];
      You Fuck = Integer.parseInt(dataset[3]);
      You Haze = Integer.parseInt(dataset[4]);
      You var7 = Integer.parseInt(dataset[5]);
      String var8 = null;
      if (dataset.length > 6) {
         var8 = dataset[6];
      }

      return new SyncData(date, var3, clientInfo, Fuck, Haze, var7, var8);
   }

   public static ArrayList getLatestDataForUsers() {
      ArrayList dataSet = parseDataSetsFromLatestList();
      if (dataSet == null) {
         return null;
      } else {
         HashMap usernameToData = new HashMap();
         Iterator var3 = dataSet.iterator();

         while(var3.hasNext()) {
            SyncData data = (SyncData)var3.next();
            usernameToData.put(data.getUser(), data);
         }

         return new ArrayList(usernameToData.values());
      }
   }

   public static void sendWebData(SyncData data) {
      try {
         String url = "";
         StringBuilder urlBuilder = new StringBuilder(url);
         urlBuilder.append("?user=");
         urlBuilder.append(data.getUser());
         urlBuilder.append("&data=");
         urlBuilder.append(data.getInfo());
         urlBuilder.append(";");
         urlBuilder.append(data.getX());
         urlBuilder.append(";");
         urlBuilder.append(data.getY());
         urlBuilder.append(";");
         urlBuilder.append(data.getZ());
         if (data.0() != null) {
            urlBuilder.append(";");
            urlBuilder.append(data.0());
         }

         HttpURLConnection urlConnection = (HttpURLConnection)(new URL(urlBuilder.toString())).openConnection();
         urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
         urlConnection.setConnectTimeout(5000);
         urlConnection.getResponseCode();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void wipeCurrentUser() {
      try {
         HttpURLConnection urlConnection = (HttpURLConnection)(new URL("" + Minecraft.getMinecraft().getSession().getUsername())).openConnection();
         urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
         urlConnection.setConnectTimeout(5000);
         urlConnection.getResponseCode();
      } catch (Exception var1) {
         var1.printStackTrace();
      }

   }
}
