/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.util.concurrent.ListenableFuture;
/*     */ import com.google.common.util.concurrent.ListeningExecutorService;
/*     */ import com.google.common.util.concurrent.MoreExecutors;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class HttpUtil
/*     */ {
/*  33 */   public static final ListeningExecutorService field_180193_a = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool(new ThreadFactoryBuilder().setDaemon(true).setNameFormat("Downloader %d").build()));
/*     */   
/*     */ 
/*  36 */   private static final AtomicInteger downloadThreadsStarted = new AtomicInteger(0);
/*  37 */   private static final Logger logger = LogManager.getLogger();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String buildPostString(Map<String, Object> data)
/*     */   {
/*  44 */     StringBuilder stringbuilder = new StringBuilder();
/*     */     
/*  46 */     for (Map.Entry<String, Object> entry : data.entrySet())
/*     */     {
/*  48 */       if (stringbuilder.length() > 0)
/*     */       {
/*  50 */         stringbuilder.append('&');
/*     */       }
/*     */       
/*     */       try
/*     */       {
/*  55 */         stringbuilder.append(URLEncoder.encode((String)entry.getKey(), "UTF-8"));
/*     */       }
/*     */       catch (UnsupportedEncodingException unsupportedencodingexception1)
/*     */       {
/*  59 */         unsupportedencodingexception1.printStackTrace();
/*     */       }
/*     */       
/*  62 */       if (entry.getValue() != null)
/*     */       {
/*  64 */         stringbuilder.append('=');
/*     */         
/*     */         try
/*     */         {
/*  68 */           stringbuilder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
/*     */         }
/*     */         catch (UnsupportedEncodingException unsupportedencodingexception)
/*     */         {
/*  72 */           unsupportedencodingexception.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  77 */     return stringbuilder.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String postMap(URL url, Map<String, Object> data, boolean skipLoggingErrors)
/*     */   {
/*  85 */     return post(url, buildPostString(data), skipLoggingErrors);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String post(URL url, String content, boolean skipLoggingErrors)
/*     */   {
/*     */     try
/*     */     {
/*  95 */       Proxy proxy = MinecraftServer.getServer() == null ? null : MinecraftServer.getServer().getServerProxy();
/*     */       
/*  97 */       if (proxy == null)
/*     */       {
/*  99 */         proxy = Proxy.NO_PROXY;
/*     */       }
/*     */       
/* 102 */       HttpURLConnection httpurlconnection = (HttpURLConnection)url.openConnection(proxy);
/* 103 */       httpurlconnection.setRequestMethod("POST");
/* 104 */       httpurlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
/* 105 */       httpurlconnection.setRequestProperty("Content-Length", content.getBytes().length);
/* 106 */       httpurlconnection.setRequestProperty("Content-Language", "en-US");
/* 107 */       httpurlconnection.setUseCaches(false);
/* 108 */       httpurlconnection.setDoInput(true);
/* 109 */       httpurlconnection.setDoOutput(true);
/* 110 */       DataOutputStream dataoutputstream = new DataOutputStream(httpurlconnection.getOutputStream());
/* 111 */       dataoutputstream.writeBytes(content);
/* 112 */       dataoutputstream.flush();
/* 113 */       dataoutputstream.close();
/* 114 */       BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
/* 115 */       StringBuffer stringbuffer = new StringBuffer();
/*     */       
/*     */       String s;
/* 118 */       while ((s = bufferedreader.readLine()) != null) {
/*     */         String s;
/* 120 */         stringbuffer.append(s);
/* 121 */         stringbuffer.append('\r');
/*     */       }
/*     */       
/* 124 */       bufferedreader.close();
/* 125 */       return stringbuffer.toString();
/*     */     }
/*     */     catch (Exception exception)
/*     */     {
/* 129 */       if (!skipLoggingErrors)
/*     */       {
/* 131 */         logger.error("Could not post to " + url, exception);
/*     */       }
/*     */     }
/* 134 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */   public static ListenableFuture<Object> downloadResourcePack(final File saveFile, final String packUrl, final Map<String, String> p_180192_2_, final int maxSize, IProgressUpdate p_180192_4_, final Proxy p_180192_5_)
/*     */   {
/* 140 */     ListenableFuture<?> listenablefuture = field_180193_a.submit(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 144 */         HttpURLConnection httpurlconnection = null;
/* 145 */         InputStream inputstream = null;
/* 146 */         OutputStream outputstream = null;
/*     */         
/* 148 */         if (HttpUtil.this != null)
/*     */         {
/* 150 */           HttpUtil.this.resetProgressAndMessage("Downloading Resource Pack");
/* 151 */           HttpUtil.this.displayLoadingString("Making Request...");
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */         try
/*     */         {
/* 158 */           byte[] abyte = new byte['က'];
/* 159 */           URL url = new URL(packUrl);
/* 160 */           httpurlconnection = (HttpURLConnection)url.openConnection(p_180192_5_);
/* 161 */           float f = 0.0F;
/* 162 */           float f1 = p_180192_2_.entrySet().size();
/*     */           
/* 164 */           for (Map.Entry<String, String> entry : p_180192_2_.entrySet())
/*     */           {
/* 166 */             httpurlconnection.setRequestProperty((String)entry.getKey(), (String)entry.getValue());
/*     */             
/* 168 */             if (HttpUtil.this != null)
/*     */             {
/* 170 */               HttpUtil.this.setLoadingProgress((int)(++f / f1 * 100.0F));
/*     */             }
/*     */           }
/*     */           
/* 174 */           inputstream = httpurlconnection.getInputStream();
/* 175 */           f1 = httpurlconnection.getContentLength();
/* 176 */           int i = httpurlconnection.getContentLength();
/*     */           
/* 178 */           if (HttpUtil.this != null)
/*     */           {
/* 180 */             HttpUtil.this.displayLoadingString(String.format("Downloading file (%.2f MB)...", new Object[] { Float.valueOf(f1 / 1000.0F / 1000.0F) }));
/*     */           }
/*     */           
/* 183 */           if (saveFile.exists())
/*     */           {
/* 185 */             long j = saveFile.length();
/*     */             
/* 187 */             if (j == i)
/*     */             {
/* 189 */               if (HttpUtil.this != null)
/*     */               {
/* 191 */                 HttpUtil.this.setDoneWorking();
/*     */               }
/*     */               
/* 194 */               return;
/*     */             }
/*     */             
/* 197 */             HttpUtil.logger.warn("Deleting " + saveFile + " as it does not match what we currently have (" + i + " vs our " + j + ").");
/* 198 */             FileUtils.deleteQuietly(saveFile);
/*     */           }
/* 200 */           else if (saveFile.getParentFile() != null)
/*     */           {
/* 202 */             saveFile.getParentFile().mkdirs();
/*     */           }
/*     */           
/* 205 */           outputstream = new DataOutputStream(new FileOutputStream(saveFile));
/*     */           
/* 207 */           if ((maxSize > 0) && (f1 > maxSize))
/*     */           {
/* 209 */             if (HttpUtil.this != null)
/*     */             {
/* 211 */               HttpUtil.this.setDoneWorking();
/*     */             }
/*     */             
/* 214 */             throw new IOException("Filesize is bigger than maximum allowed (file is " + f + ", limit is " + maxSize + ")");
/*     */           }
/*     */           
/* 217 */           int k = 0;
/*     */           
/* 219 */           while ((k = inputstream.read(abyte)) >= 0)
/*     */           {
/* 221 */             f += k;
/*     */             
/* 223 */             if (HttpUtil.this != null)
/*     */             {
/* 225 */               HttpUtil.this.setLoadingProgress((int)(f / f1 * 100.0F));
/*     */             }
/*     */             
/* 228 */             if ((maxSize > 0) && (f > maxSize))
/*     */             {
/* 230 */               if (HttpUtil.this != null)
/*     */               {
/* 232 */                 HttpUtil.this.setDoneWorking();
/*     */               }
/*     */               
/* 235 */               throw new IOException("Filesize was bigger than maximum allowed (got >= " + f + ", limit was " + maxSize + ")");
/*     */             }
/*     */             
/* 238 */             if (Thread.interrupted())
/*     */             {
/* 240 */               HttpUtil.logger.error("INTERRUPTED");
/*     */               
/* 242 */               if (HttpUtil.this != null)
/*     */               {
/* 244 */                 HttpUtil.this.setDoneWorking();
/*     */               }
/*     */               
/* 247 */               return;
/*     */             }
/*     */             
/* 250 */             outputstream.write(abyte, 0, k);
/*     */           }
/*     */           
/* 253 */           if (HttpUtil.this != null)
/*     */           {
/* 255 */             HttpUtil.this.setDoneWorking();
/* 256 */             return;
/*     */           }
/*     */         }
/*     */         catch (Throwable throwable)
/*     */         {
/* 261 */           throwable.printStackTrace();
/*     */           
/* 263 */           if (httpurlconnection != null)
/*     */           {
/* 265 */             InputStream inputstream1 = httpurlconnection.getErrorStream();
/*     */             
/*     */             try
/*     */             {
/* 269 */               HttpUtil.logger.error(IOUtils.toString(inputstream1));
/*     */             }
/*     */             catch (IOException ioexception)
/*     */             {
/* 273 */               ioexception.printStackTrace();
/*     */             }
/*     */           }
/*     */           
/* 277 */           if (HttpUtil.this != null)
/*     */           {
/* 279 */             HttpUtil.this.setDoneWorking();
/* 280 */             return;
/*     */           }
/*     */           
/*     */         }
/*     */         finally
/*     */         {
/* 286 */           IOUtils.closeQuietly(inputstream);
/* 287 */           IOUtils.closeQuietly(outputstream);
/*     */         }
/* 286 */         IOUtils.closeQuietly(inputstream);
/* 287 */         IOUtils.closeQuietly(outputstream);
/*     */       }
/*     */       
/* 290 */     });
/* 291 */     return listenablefuture;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static int getSuitableLanPort()
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_0
/*     */     //   2: iconst_m1
/*     */     //   3: istore_1
/*     */     //   4: new 323	java/net/ServerSocket
/*     */     //   7: dup
/*     */     //   8: iconst_0
/*     */     //   9: invokespecial 324	java/net/ServerSocket:<init>	(I)V
/*     */     //   12: astore_0
/*     */     //   13: aload_0
/*     */     //   14: invokevirtual 327	java/net/ServerSocket:getLocalPort	()I
/*     */     //   17: istore_1
/*     */     //   18: goto +18 -> 36
/*     */     //   21: astore_2
/*     */     //   22: aload_0
/*     */     //   23: ifnull +11 -> 34
/*     */     //   26: aload_0
/*     */     //   27: invokevirtual 330	java/net/ServerSocket:close	()V
/*     */     //   30: goto +4 -> 34
/*     */     //   33: astore_3
/*     */     //   34: aload_2
/*     */     //   35: athrow
/*     */     //   36: aload_0
/*     */     //   37: ifnull +11 -> 48
/*     */     //   40: aload_0
/*     */     //   41: invokevirtual 330	java/net/ServerSocket:close	()V
/*     */     //   44: goto +4 -> 48
/*     */     //   47: astore_3
/*     */     //   48: iload_1
/*     */     //   49: ireturn
/*     */     // Line number table:
/*     */     //   Java source line #296	-> byte code offset #0
/*     */     //   Java source line #297	-> byte code offset #2
/*     */     //   Java source line #301	-> byte code offset #4
/*     */     //   Java source line #302	-> byte code offset #13
/*     */     //   Java source line #303	-> byte code offset #18
/*     */     //   Java source line #305	-> byte code offset #21
/*     */     //   Java source line #308	-> byte code offset #22
/*     */     //   Java source line #310	-> byte code offset #26
/*     */     //   Java source line #312	-> byte code offset #30
/*     */     //   Java source line #313	-> byte code offset #33
/*     */     //   Java source line #317	-> byte code offset #34
/*     */     //   Java source line #308	-> byte code offset #36
/*     */     //   Java source line #310	-> byte code offset #40
/*     */     //   Java source line #312	-> byte code offset #44
/*     */     //   Java source line #313	-> byte code offset #47
/*     */     //   Java source line #319	-> byte code offset #48
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   1	40	0	serversocket	java.net.ServerSocket
/*     */     //   3	46	1	i	int
/*     */     //   21	14	2	localObject	Object
/*     */     //   33	1	3	localIOException	IOException
/*     */     //   47	1	3	localIOException1	IOException
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	21	21	finally
/*     */     //   22	30	33	java/io/IOException
/*     */     //   36	44	47	java/io/IOException
/*     */   }
/*     */   
/*     */   public static String get(URL url)
/*     */     throws IOException
/*     */   {
/* 327 */     HttpURLConnection httpurlconnection = (HttpURLConnection)url.openConnection();
/* 328 */     httpurlconnection.setRequestMethod("GET");
/* 329 */     BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
/* 330 */     StringBuilder stringbuilder = new StringBuilder();
/*     */     
/*     */     String s;
/* 333 */     while ((s = bufferedreader.readLine()) != null) {
/*     */       String s;
/* 335 */       stringbuilder.append(s);
/* 336 */       stringbuilder.append('\r');
/*     */     }
/*     */     
/* 339 */     bufferedreader.close();
/* 340 */     return stringbuilder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\HttpUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */