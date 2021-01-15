/*     */ package optfine;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ import net.minecraft.client.resources.AbstractResourcePack;
/*     */ import net.minecraft.client.resources.DefaultResourcePack;
/*     */ import net.minecraft.client.resources.IResourcePack;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class ConnectedUtils
/*     */ {
/*     */   public static String[] collectFiles(IResourcePack p_collectFiles_0_, String p_collectFiles_1_, String p_collectFiles_2_, String[] p_collectFiles_3_)
/*     */   {
/*  19 */     if ((p_collectFiles_0_ instanceof DefaultResourcePack))
/*     */     {
/*  21 */       return collectFilesFixed(p_collectFiles_0_, p_collectFiles_3_);
/*     */     }
/*  23 */     if (!(p_collectFiles_0_ instanceof AbstractResourcePack))
/*     */     {
/*  25 */       return new String[0];
/*     */     }
/*     */     
/*     */ 
/*  29 */     AbstractResourcePack abstractresourcepack = (AbstractResourcePack)p_collectFiles_0_;
/*  30 */     File file1 = ResourceUtils.getResourcePackFile(abstractresourcepack);
/*  31 */     return file1.isFile() ? collectFilesZIP(file1, p_collectFiles_1_, p_collectFiles_2_) : file1.isDirectory() ? collectFilesFolder(file1, "", p_collectFiles_1_, p_collectFiles_2_) : file1 == null ? new String[0] : new String[0];
/*     */   }
/*     */   
/*     */ 
/*     */   private static String[] collectFilesFixed(IResourcePack p_collectFilesFixed_0_, String[] p_collectFilesFixed_1_)
/*     */   {
/*  37 */     if (p_collectFilesFixed_1_ == null)
/*     */     {
/*  39 */       return new String[0];
/*     */     }
/*     */     
/*     */ 
/*  43 */     List list = new ArrayList();
/*     */     
/*  45 */     for (int i = 0; i < p_collectFilesFixed_1_.length; i++)
/*     */     {
/*  47 */       String s = p_collectFilesFixed_1_[i];
/*  48 */       ResourceLocation resourcelocation = new ResourceLocation(s);
/*     */       
/*  50 */       if (p_collectFilesFixed_0_.resourceExists(resourcelocation))
/*     */       {
/*  52 */         list.add(s);
/*     */       }
/*     */     }
/*     */     
/*  56 */     String[] astring = (String[])list.toArray(new String[list.size()]);
/*  57 */     return astring;
/*     */   }
/*     */   
/*     */ 
/*     */   private static String[] collectFilesFolder(File p_collectFilesFolder_0_, String p_collectFilesFolder_1_, String p_collectFilesFolder_2_, String p_collectFilesFolder_3_)
/*     */   {
/*  63 */     List list = new ArrayList();
/*  64 */     String s = "assets/minecraft/";
/*  65 */     File[] afile = p_collectFilesFolder_0_.listFiles();
/*     */     
/*  67 */     if (afile == null)
/*     */     {
/*  69 */       return new String[0];
/*     */     }
/*     */     
/*     */ 
/*  73 */     for (int i = 0; i < afile.length; i++)
/*     */     {
/*  75 */       File file1 = afile[i];
/*     */       
/*  77 */       if (file1.isFile())
/*     */       {
/*  79 */         String s3 = p_collectFilesFolder_1_ + file1.getName();
/*     */         
/*  81 */         if (s3.startsWith(s))
/*     */         {
/*  83 */           s3 = s3.substring(s.length());
/*     */           
/*  85 */           if ((s3.startsWith(p_collectFilesFolder_2_)) && (s3.endsWith(p_collectFilesFolder_3_)))
/*     */           {
/*  87 */             list.add(s3);
/*     */           }
/*     */         }
/*     */       }
/*  91 */       else if (file1.isDirectory())
/*     */       {
/*  93 */         String s1 = p_collectFilesFolder_1_ + file1.getName() + "/";
/*  94 */         String[] astring = collectFilesFolder(file1, s1, p_collectFilesFolder_2_, p_collectFilesFolder_3_);
/*     */         
/*  96 */         for (int j = 0; j < astring.length; j++)
/*     */         {
/*  98 */           String s2 = astring[j];
/*  99 */           list.add(s2);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 104 */     String[] astring1 = (String[])list.toArray(new String[list.size()]);
/* 105 */     return astring1;
/*     */   }
/*     */   
/*     */ 
/*     */   private static String[] collectFilesZIP(File p_collectFilesZIP_0_, String p_collectFilesZIP_1_, String p_collectFilesZIP_2_)
/*     */   {
/* 111 */     List list = new ArrayList();
/* 112 */     String s = "assets/minecraft/";
/*     */     
/*     */     try
/*     */     {
/* 116 */       ZipFile zipfile = new ZipFile(p_collectFilesZIP_0_);
/* 117 */       Enumeration enumeration = zipfile.entries();
/*     */       
/* 119 */       while (enumeration.hasMoreElements())
/*     */       {
/* 121 */         ZipEntry zipentry = (ZipEntry)enumeration.nextElement();
/* 122 */         String s1 = zipentry.getName();
/*     */         
/* 124 */         if (s1.startsWith(s))
/*     */         {
/* 126 */           s1 = s1.substring(s.length());
/*     */           
/* 128 */           if ((s1.startsWith(p_collectFilesZIP_1_)) && (s1.endsWith(p_collectFilesZIP_2_)))
/*     */           {
/* 130 */             list.add(s1);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 135 */       zipfile.close();
/* 136 */       return (String[])list.toArray(new String[list.size()]);
/*     */ 
/*     */     }
/*     */     catch (IOException ioexception)
/*     */     {
/* 141 */       ioexception.printStackTrace(); }
/* 142 */     return new String[0];
/*     */   }
/*     */   
/*     */ 
/*     */   public static int getAverage(int[] p_getAverage_0_)
/*     */   {
/* 148 */     if (p_getAverage_0_.length <= 0)
/*     */     {
/* 150 */       return 0;
/*     */     }
/*     */     
/*     */ 
/* 154 */     int i = 0;
/*     */     
/* 156 */     for (int j = 0; j < p_getAverage_0_.length; j++)
/*     */     {
/* 158 */       int k = p_getAverage_0_[j];
/* 159 */       i += k;
/*     */     }
/*     */     
/* 162 */     int l = i / p_getAverage_0_.length;
/* 163 */     return l;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\ConnectedUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */