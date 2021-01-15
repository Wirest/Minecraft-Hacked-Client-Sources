/*    */ package rip.jutting.polaris.utils;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.io.Writer;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ public final class FileUtils
/*    */ {
/*    */   public static List<String> read(File inputFile)
/*    */   {
/* 19 */     List<String> readContent = new ArrayList();
/*    */     try {
/* 21 */       BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF8"));
/*    */       String str;
/* 23 */       while ((str = in.readLine()) != null) { String str;
/* 24 */         readContent.add(str);
/*    */       }
/* 26 */       in.close();
/*    */     }
/*    */     catch (Exception e) {
/* 29 */       e.printStackTrace();
/*    */     }
/* 31 */     return readContent;
/*    */   }
/*    */   
/*    */   public static void write(File outputFile, List<String> writeContent, boolean overrideContent) {
/*    */     try {
/* 36 */       Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
/* 37 */       for (String outputLine : writeContent) {
/* 38 */         out.write(String.valueOf(outputLine) + System.getProperty("line.separator"));
/*    */       }
/* 40 */       out.close();
/*    */     }
/*    */     catch (Exception e) {
/* 43 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */   
/*    */   public static File getConfigDir() {
/* 48 */     File file = new File(Minecraft.getMinecraft().mcDataDir, "Polaris");
/* 49 */     if (!file.exists()) {
/* 50 */       file.mkdir();
/*    */     }
/* 52 */     return file;
/*    */   }
/*    */   
/*    */   public static File getConfigFile(String name) {
/* 56 */     File file = new File(getConfigDir(), String.format("%s.txt", new Object[] { name }));
/* 57 */     if (!file.exists()) {
/*    */       try {
/* 59 */         file.createNewFile();
/*    */       }
/*    */       catch (Exception e) {
/* 62 */         e.printStackTrace();
/*    */       }
/*    */     }
/* 65 */     return file;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\FileUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */