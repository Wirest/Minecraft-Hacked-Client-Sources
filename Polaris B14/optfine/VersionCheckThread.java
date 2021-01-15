/*    */ package optfine;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.URL;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ 
/*    */ public class VersionCheckThread extends Thread
/*    */ {
/*    */   public void run()
/*    */   {
/* 12 */     HttpURLConnection httpurlconnection = null;
/*    */     
/*    */     try
/*    */     {
/* 16 */       Config.dbg("Checking for new version");
/* 17 */       URL url = new URL("http://optifine.net/version/1.8.8/HD_U.txt");
/* 18 */       httpurlconnection = (HttpURLConnection)url.openConnection();
/*    */       
/* 20 */       if (Config.getGameSettings().snooperEnabled)
/*    */       {
/* 22 */         httpurlconnection.setRequestProperty("OF-MC-Version", "1.8.8");
/* 23 */         httpurlconnection.setRequestProperty("OF-MC-Brand", net.minecraft.client.ClientBrandRetriever.getClientModName());
/* 24 */         httpurlconnection.setRequestProperty("OF-Edition", "HD_U");
/* 25 */         httpurlconnection.setRequestProperty("OF-Release", "E1");
/* 26 */         httpurlconnection.setRequestProperty("OF-Java-Version", System.getProperty("java.version"));
/* 27 */         httpurlconnection.setRequestProperty("OF-CpuCount", Config.getAvailableProcessors());
/* 28 */         httpurlconnection.setRequestProperty("OF-OpenGL-Version", Config.openGlVersion);
/* 29 */         httpurlconnection.setRequestProperty("OF-OpenGL-Vendor", Config.openGlVendor);
/*    */       }
/*    */       
/* 32 */       httpurlconnection.setDoInput(true);
/* 33 */       httpurlconnection.setDoOutput(false);
/* 34 */       httpurlconnection.connect();
/*    */       
/*    */       try
/*    */       {
/* 38 */         InputStream inputstream = httpurlconnection.getInputStream();
/* 39 */         String s = Config.readInputStream(inputstream);
/* 40 */         inputstream.close();
/* 41 */         String[] astring = Config.tokenize(s, "\n\r");
/*    */         
/* 43 */         if (astring.length >= 1)
/*    */         {
/* 45 */           String s1 = astring[0];
/* 46 */           Config.dbg("Version found: " + s1);
/*    */           
/* 48 */           if (Config.compareRelease(s1, "E1") <= 0)
/*    */           {
/* 50 */             return;
/*    */           }
/*    */           
/* 53 */           Config.setNewRelease(s1);
/* 54 */           return;
/*    */         }
/*    */       }
/*    */       finally
/*    */       {
/* 59 */         if (httpurlconnection != null)
/*    */         {
/* 61 */           httpurlconnection.disconnect();
/*    */         }
/*    */       }
/* 59 */       if (httpurlconnection != null)
/*    */       {
/* 61 */         httpurlconnection.disconnect();
/*    */       }
/*    */       
/*    */     }
/*    */     catch (Exception exception)
/*    */     {
/* 67 */       Config.dbg(exception.getClass().getName() + ": " + exception.getMessage());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\VersionCheckThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */