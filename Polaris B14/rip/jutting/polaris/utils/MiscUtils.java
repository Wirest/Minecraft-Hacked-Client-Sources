/*     */ package rip.jutting.polaris.utils;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Desktop;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.Proxy;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.swing.JOptionPane;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.Util.EnumOS;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.Sys;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MiscUtils
/*     */ {
/*  29 */   private static final Logger logger = ;
/*     */   
/*     */   public static boolean isInteger(String s)
/*     */   {
/*     */     try
/*     */     {
/*  35 */       Integer.parseInt(s);
/*     */     }
/*     */     catch (NumberFormatException e) {
/*  38 */       return false;
/*     */     }
/*  40 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isDouble(String s)
/*     */   {
/*     */     try
/*     */     {
/*  47 */       Double.parseDouble(s);
/*     */     }
/*     */     catch (NumberFormatException e) {
/*  50 */       return false;
/*     */     }
/*  52 */     return true;
/*     */   }
/*     */   
/*     */   public static int countMatches(String string, String regex)
/*     */   {
/*  57 */     Matcher matcher = Pattern.compile(regex).matcher(string);
/*  58 */     int count = 0;
/*  59 */     while (matcher.find())
/*  60 */       count++;
/*  61 */     return count;
/*     */   }
/*     */   
/*     */   public static boolean openLink(String url)
/*     */   {
/*     */     try
/*     */     {
/*  68 */       Desktop.getDesktop().browse(new URI(url));
/*  69 */       return true;
/*     */     }
/*     */     catch (Exception e) {
/*  72 */       logger.error("Failed to open link", e); }
/*  73 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void openFile(File file)
/*     */   {
/*  79 */     openFile(file.getPath());
/*     */   }
/*     */   
/*     */   public static void openFile(String path)
/*     */   {
/*  84 */     File file = new File(path);
/*  85 */     String apath = file.getAbsolutePath();
/*     */     
/*     */     try
/*     */     {
/*  89 */       Desktop.getDesktop().open(file);
/*     */     }
/*     */     catch (IOException e) {
/*  92 */       logger.error("Failed to open file via Desktop.", e);
/*     */     }
/*     */     
/*  95 */     if (Util.getOSType() == Util.EnumOS.WINDOWS)
/*     */     {
/*  97 */       String command = String.format(
/*  98 */         "cmd.exe /C start \"Open file\" \"%s\"", new Object[] { apath });
/*     */       
/*     */       try
/*     */       {
/* 102 */         Runtime.getRuntime().exec(command);
/* 103 */         return;
/*     */       }
/*     */       catch (IOException var8) {
/* 106 */         logger.error("Failed to open file", var8);
/*     */       }
/* 108 */     } else if (Util.getOSType() == Util.EnumOS.OSX)
/*     */     {
/*     */       try {
/* 111 */         logger.info(apath);
/* 112 */         Runtime.getRuntime().exec(new String[] { "/usr/bin/open", apath });
/* 113 */         return;
/*     */       }
/*     */       catch (IOException var9) {
/* 116 */         logger.error("Failed to open file", var9);
/*     */       }
/*     */     }
/* 119 */     boolean browserFailed = false;
/*     */     
/*     */     try
/*     */     {
/* 123 */       Desktop.getDesktop().browse(file.toURI());
/*     */     }
/*     */     catch (Throwable var7) {
/* 126 */       logger.error("Failed to open file", var7);
/* 127 */       browserFailed = true;
/*     */     }
/*     */     
/* 130 */     if (browserFailed)
/*     */     {
/* 132 */       logger.info("Opening via system class!");
/* 133 */       Sys.openURL("file://" + apath);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void simpleError(Exception e, Component parent)
/*     */   {
/* 139 */     StringWriter writer = new StringWriter();
/* 140 */     e.printStackTrace(new PrintWriter(writer));
/* 141 */     String message = writer.toString();
/* 142 */     JOptionPane.showMessageDialog(parent, message, "Error", 
/* 143 */       0);
/*     */   }
/*     */   
/*     */   public static String get(URL url) throws IOException
/*     */   {
/* 148 */     HttpURLConnection connection = (HttpURLConnection)url.openConnection();
/* 149 */     connection.setRequestMethod("GET");
/* 150 */     BufferedReader input = new BufferedReader(
/* 151 */       new InputStreamReader(connection.getInputStream()));
/* 152 */     StringBuilder buffer = new StringBuilder();
/* 153 */     String line; while ((line = input.readLine()) != null) {
/*     */       String line;
/* 155 */       buffer.append(line);
/* 156 */       buffer.append("\n");
/*     */     }
/* 158 */     input.close();
/* 159 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public static String post(URL url, String content) throws IOException
/*     */   {
/* 164 */     return post(url, content, "application/x-www-form-urlencoded");
/*     */   }
/*     */   
/*     */ 
/*     */   public static String post(URL url, String content, String contentType)
/*     */     throws IOException
/*     */   {
/* 171 */     HttpURLConnection connection = 
/* 172 */       (HttpURLConnection)url.openConnection(Proxy.NO_PROXY);
/* 173 */     connection.setRequestMethod("POST");
/* 174 */     connection.setRequestProperty("Content-Type", contentType);
/* 175 */     connection.setRequestProperty("Content-Length", 
/* 176 */       content.getBytes().length);
/* 177 */     connection.setRequestProperty("Content-Language", "en-US");
/* 178 */     connection.setUseCaches(false);
/* 179 */     connection.setDoInput(true);
/* 180 */     connection.setDoOutput(true);
/* 181 */     DataOutputStream output = 
/* 182 */       new DataOutputStream(connection.getOutputStream());
/* 183 */     output.writeBytes(content);
/* 184 */     output.flush();
/* 185 */     output.close();
/*     */     
/* 187 */     BufferedReader input = new BufferedReader(
/* 188 */       new InputStreamReader(connection.getInputStream()));
/* 189 */     StringBuffer buffer = new StringBuffer();
/* 190 */     String line; while ((line = input.readLine()) != null) {
/*     */       String line;
/* 192 */       buffer.append(line);
/* 193 */       buffer.append("\n");
/*     */     }
/* 195 */     input.close();
/* 196 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\MiscUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */