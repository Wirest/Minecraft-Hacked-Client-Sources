/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NativeLibraryLoader
/*     */ {
/*  35 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NativeLibraryLoader.class);
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String NATIVE_RESOURCE_HOME = "META-INF/native/";
/*     */   
/*     */ 
/*  42 */   private static final String OSNAME = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
/*     */   
/*  44 */   static { String workdir = SystemPropertyUtil.get("io.netty.native.workdir");
/*  45 */     if (workdir != null) {
/*  46 */       File f = new File(workdir);
/*  47 */       f.mkdirs();
/*     */       try
/*     */       {
/*  50 */         f = f.getAbsoluteFile();
/*     */       }
/*     */       catch (Exception ignored) {}
/*     */       
/*     */ 
/*  55 */       WORKDIR = f;
/*  56 */       logger.debug("-Dio.netty.netty.workdir: " + WORKDIR);
/*     */     } else {
/*  58 */       WORKDIR = tmpdir();
/*  59 */       logger.debug("-Dio.netty.netty.workdir: " + WORKDIR + " (io.netty.tmpdir)");
/*     */     }
/*     */   }
/*     */   
/*     */   private static File tmpdir() {
/*     */     File f;
/*     */     try {
/*  66 */       f = toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
/*  67 */       if (f != null) {
/*  68 */         logger.debug("-Dio.netty.tmpdir: " + f);
/*  69 */         return f;
/*     */       }
/*     */       
/*  72 */       f = toDirectory(SystemPropertyUtil.get("java.io.tmpdir"));
/*  73 */       if (f != null) {
/*  74 */         logger.debug("-Dio.netty.tmpdir: " + f + " (java.io.tmpdir)");
/*  75 */         return f;
/*     */       }
/*     */       
/*     */ 
/*  79 */       if (isWindows()) {
/*  80 */         f = toDirectory(System.getenv("TEMP"));
/*  81 */         if (f != null) {
/*  82 */           logger.debug("-Dio.netty.tmpdir: " + f + " (%TEMP%)");
/*  83 */           return f;
/*     */         }
/*     */         
/*  86 */         String userprofile = System.getenv("USERPROFILE");
/*  87 */         if (userprofile != null) {
/*  88 */           f = toDirectory(userprofile + "\\AppData\\Local\\Temp");
/*  89 */           if (f != null) {
/*  90 */             logger.debug("-Dio.netty.tmpdir: " + f + " (%USERPROFILE%\\AppData\\Local\\Temp)");
/*  91 */             return f;
/*     */           }
/*     */           
/*  94 */           f = toDirectory(userprofile + "\\Local Settings\\Temp");
/*  95 */           if (f != null) {
/*  96 */             logger.debug("-Dio.netty.tmpdir: " + f + " (%USERPROFILE%\\Local Settings\\Temp)");
/*  97 */             return f;
/*     */           }
/*     */         }
/*     */       } else {
/* 101 */         f = toDirectory(System.getenv("TMPDIR"));
/* 102 */         if (f != null) {
/* 103 */           logger.debug("-Dio.netty.tmpdir: " + f + " ($TMPDIR)");
/* 104 */           return f;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception ignored) {}
/*     */     
/*     */     File f;
/*     */     
/* 112 */     if (isWindows()) {
/* 113 */       f = new File("C:\\Windows\\Temp");
/*     */     } else {
/* 115 */       f = new File("/tmp");
/*     */     }
/*     */     
/* 118 */     logger.warn("Failed to get the temporary directory; falling back to: " + f);
/* 119 */     return f;
/*     */   }
/*     */   
/*     */   private static File toDirectory(String path)
/*     */   {
/* 124 */     if (path == null) {
/* 125 */       return null;
/*     */     }
/*     */     
/* 128 */     File f = new File(path);
/* 129 */     f.mkdirs();
/*     */     
/* 131 */     if (!f.isDirectory()) {
/* 132 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 136 */       return f.getAbsoluteFile();
/*     */     } catch (Exception ignored) {}
/* 138 */     return f;
/*     */   }
/*     */   
/*     */   private static boolean isWindows()
/*     */   {
/* 143 */     return OSNAME.startsWith("windows");
/*     */   }
/*     */   
/*     */   private static boolean isOSX() {
/* 147 */     return (OSNAME.startsWith("macosx")) || (OSNAME.startsWith("osx"));
/*     */   }
/*     */   
/*     */ 
/*     */   private static final File WORKDIR;
/*     */   public static void load(String name, ClassLoader loader)
/*     */   {
/* 154 */     String libname = System.mapLibraryName(name);
/* 155 */     String path = "META-INF/native/" + libname;
/*     */     
/* 157 */     URL url = loader.getResource(path);
/* 158 */     if ((url == null) && (isOSX())) {
/* 159 */       if (path.endsWith(".jnilib")) {
/* 160 */         url = loader.getResource("META-INF/native/lib" + name + ".dynlib");
/*     */       } else {
/* 162 */         url = loader.getResource("META-INF/native/lib" + name + ".jnilib");
/*     */       }
/*     */     }
/*     */     
/* 166 */     if (url == null)
/*     */     {
/* 168 */       System.loadLibrary(name);
/* 169 */       return;
/*     */     }
/*     */     
/* 172 */     int index = libname.lastIndexOf('.');
/* 173 */     String prefix = libname.substring(0, index);
/* 174 */     String suffix = libname.substring(index, libname.length());
/* 175 */     InputStream in = null;
/* 176 */     OutputStream out = null;
/* 177 */     File tmpFile = null;
/* 178 */     boolean loaded = false;
/*     */     try {
/* 180 */       tmpFile = File.createTempFile(prefix, suffix, WORKDIR);
/* 181 */       in = url.openStream();
/* 182 */       out = new FileOutputStream(tmpFile);
/*     */       
/* 184 */       byte[] buffer = new byte[' '];
/*     */       int length;
/* 186 */       while ((length = in.read(buffer)) > 0) {
/* 187 */         out.write(buffer, 0, length);
/*     */       }
/* 189 */       out.flush();
/* 190 */       out.close();
/* 191 */       out = null;
/*     */       
/* 193 */       System.load(tmpFile.getPath());
/* 194 */       loaded = true;
/*     */     } catch (Exception e) {
/* 196 */       throw ((UnsatisfiedLinkError)new UnsatisfiedLinkError("could not load a native library: " + name).initCause(e));
/*     */     }
/*     */     finally {
/* 199 */       if (in != null) {
/*     */         try {
/* 201 */           in.close();
/*     */         }
/*     */         catch (IOException ignore) {}
/*     */       }
/*     */       
/* 206 */       if (out != null) {
/*     */         try {
/* 208 */           out.close();
/*     */         }
/*     */         catch (IOException ignore) {}
/*     */       }
/*     */       
/* 213 */       if (tmpFile != null) {
/* 214 */         if (loaded) {
/* 215 */           tmpFile.deleteOnExit();
/*     */         }
/* 217 */         else if (!tmpFile.delete()) {
/* 218 */           tmpFile.deleteOnExit();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\NativeLibraryLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */