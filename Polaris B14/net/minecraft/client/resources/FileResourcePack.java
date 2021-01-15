/*     */ package net.minecraft.client.resources;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ 
/*     */ public class FileResourcePack extends AbstractResourcePack implements Closeable
/*     */ {
/*  19 */   public static final Splitter entryNameSplitter = Splitter.on('/').omitEmptyStrings().limit(3);
/*     */   private ZipFile resourcePackZipFile;
/*     */   
/*     */   public FileResourcePack(File resourcePackFileIn)
/*     */   {
/*  24 */     super(resourcePackFileIn);
/*     */   }
/*     */   
/*     */   private ZipFile getResourcePackZipFile() throws IOException
/*     */   {
/*  29 */     if (this.resourcePackZipFile == null)
/*     */     {
/*  31 */       this.resourcePackZipFile = new ZipFile(this.resourcePackFile);
/*     */     }
/*     */     
/*  34 */     return this.resourcePackZipFile;
/*     */   }
/*     */   
/*     */   protected InputStream getInputStreamByName(String name) throws IOException
/*     */   {
/*  39 */     ZipFile zipfile = getResourcePackZipFile();
/*  40 */     ZipEntry zipentry = zipfile.getEntry(name);
/*     */     
/*  42 */     if (zipentry == null)
/*     */     {
/*  44 */       throw new ResourcePackFileNotFoundException(this.resourcePackFile, name);
/*     */     }
/*     */     
/*     */ 
/*  48 */     return zipfile.getInputStream(zipentry);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean hasResourceName(String name)
/*     */   {
/*     */     try
/*     */     {
/*  56 */       return getResourcePackZipFile().getEntry(name) != null;
/*     */     }
/*     */     catch (IOException var3) {}
/*     */     
/*  60 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<String> getResourceDomains()
/*     */   {
/*     */     try
/*     */     {
/*  70 */       zipfile = getResourcePackZipFile();
/*     */     }
/*     */     catch (IOException var8) {
/*     */       ZipFile zipfile;
/*  74 */       return Collections.emptySet();
/*     */     }
/*     */     ZipFile zipfile;
/*  77 */     Enumeration<? extends ZipEntry> enumeration = zipfile.entries();
/*  78 */     Set<String> set = Sets.newHashSet();
/*     */     
/*  80 */     while (enumeration.hasMoreElements())
/*     */     {
/*  82 */       ZipEntry zipentry = (ZipEntry)enumeration.nextElement();
/*  83 */       String s = zipentry.getName();
/*     */       
/*  85 */       if (s.startsWith("assets/"))
/*     */       {
/*  87 */         List<String> list = Lists.newArrayList(entryNameSplitter.split(s));
/*     */         
/*  89 */         if (list.size() > 1)
/*     */         {
/*  91 */           String s1 = (String)list.get(1);
/*     */           
/*  93 */           if (!s1.equals(s1.toLowerCase()))
/*     */           {
/*  95 */             logNameNotLowercase(s1);
/*     */           }
/*     */           else
/*     */           {
/*  99 */             set.add(s1);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 105 */     return set;
/*     */   }
/*     */   
/*     */   protected void finalize() throws Throwable
/*     */   {
/* 110 */     close();
/* 111 */     super.finalize();
/*     */   }
/*     */   
/*     */   public void close() throws IOException
/*     */   {
/* 116 */     if (this.resourcePackZipFile != null)
/*     */     {
/* 118 */       this.resourcePackZipFile.close();
/* 119 */       this.resourcePackZipFile = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\FileResourcePack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */