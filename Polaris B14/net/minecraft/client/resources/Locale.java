/*     */ package net.minecraft.client.resources;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.IllegalFormatException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.io.Charsets;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ public class Locale
/*     */ {
/*  19 */   private static final Splitter splitter = Splitter.on('=').limit(2);
/*  20 */   private static final Pattern pattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
/*  21 */   Map<String, String> properties = Maps.newHashMap();
/*     */   
/*     */ 
/*     */   private boolean unicode;
/*     */   
/*     */ 
/*     */   public synchronized void loadLocaleDataFiles(IResourceManager resourceManager, List<String> p_135022_2_)
/*     */   {
/*  29 */     this.properties.clear();
/*     */     Iterator localIterator2;
/*  31 */     for (Iterator localIterator1 = p_135022_2_.iterator(); localIterator1.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/*  35 */         localIterator2.hasNext())
/*     */     {
/*  31 */       String s = (String)localIterator1.next();
/*     */       
/*  33 */       String s1 = String.format("lang/%s.lang", new Object[] { s });
/*     */       
/*  35 */       localIterator2 = resourceManager.getResourceDomains().iterator(); continue;String s2 = (String)localIterator2.next();
/*     */       
/*     */       try
/*     */       {
/*  39 */         loadLocaleData(resourceManager.getAllResources(new net.minecraft.util.ResourceLocation(s2, s1)));
/*     */       }
/*     */       catch (IOException localIOException) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  48 */     checkUnicode();
/*     */   }
/*     */   
/*     */   public boolean isUnicode()
/*     */   {
/*  53 */     return this.unicode;
/*     */   }
/*     */   
/*     */   private void checkUnicode()
/*     */   {
/*  58 */     this.unicode = false;
/*  59 */     int i = 0;
/*  60 */     int j = 0;
/*     */     int k;
/*  62 */     int l; for (Iterator localIterator = this.properties.values().iterator(); localIterator.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*  67 */         l < k)
/*     */     {
/*  62 */       String s = (String)localIterator.next();
/*     */       
/*  64 */       k = s.length();
/*  65 */       j += k;
/*     */       
/*  67 */       l = 0; continue;
/*     */       
/*  69 */       if (s.charAt(l) >= 'Ā')
/*     */       {
/*  71 */         i++;
/*     */       }
/*  67 */       l++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  76 */     float f = i / j;
/*  77 */     this.unicode = (f > 0.1D);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private void loadLocaleData(List<IResource> p_135028_1_)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokeinterface 66 1 0
/*     */     //   6: astore_3
/*     */     //   7: goto +45 -> 52
/*     */     //   10: aload_3
/*     */     //   11: invokeinterface 74 1 0
/*     */     //   16: checkcast 147	net/minecraft/client/resources/IResource
/*     */     //   19: astore_2
/*     */     //   20: aload_2
/*     */     //   21: invokeinterface 151 1 0
/*     */     //   26: astore 4
/*     */     //   28: aload_0
/*     */     //   29: aload 4
/*     */     //   31: invokespecial 154	net/minecraft/client/resources/Locale:loadLocaleData	(Ljava/io/InputStream;)V
/*     */     //   34: goto +13 -> 47
/*     */     //   37: astore 5
/*     */     //   39: aload 4
/*     */     //   41: invokestatic 163	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*     */     //   44: aload 5
/*     */     //   46: athrow
/*     */     //   47: aload 4
/*     */     //   49: invokestatic 163	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*     */     //   52: aload_3
/*     */     //   53: invokeinterface 106 1 0
/*     */     //   58: ifne -48 -> 10
/*     */     //   61: return
/*     */     // Line number table:
/*     */     //   Java source line #85	-> byte code offset #0
/*     */     //   Java source line #87	-> byte code offset #20
/*     */     //   Java source line #91	-> byte code offset #28
/*     */     //   Java source line #92	-> byte code offset #34
/*     */     //   Java source line #94	-> byte code offset #37
/*     */     //   Java source line #95	-> byte code offset #39
/*     */     //   Java source line #96	-> byte code offset #44
/*     */     //   Java source line #95	-> byte code offset #47
/*     */     //   Java source line #85	-> byte code offset #52
/*     */     //   Java source line #98	-> byte code offset #61
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	62	0	this	Locale
/*     */     //   0	62	1	p_135028_1_	List<IResource>
/*     */     //   19	2	2	iresource	IResource
/*     */     //   6	47	3	localIterator	Iterator
/*     */     //   26	22	4	inputstream	InputStream
/*     */     //   37	8	5	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   28	37	37	finally
/*     */   }
/*     */   
/*     */   private void loadLocaleData(InputStream p_135021_1_)
/*     */     throws IOException
/*     */   {
/* 102 */     for (String s : IOUtils.readLines(p_135021_1_, Charsets.UTF_8))
/*     */     {
/* 104 */       if ((!s.isEmpty()) && (s.charAt(0) != '#'))
/*     */       {
/* 106 */         String[] astring = (String[])com.google.common.collect.Iterables.toArray(splitter.split(s), String.class);
/*     */         
/* 108 */         if ((astring != null) && (astring.length == 2))
/*     */         {
/* 110 */           String s1 = astring[0];
/* 111 */           String s2 = pattern.matcher(astring[1]).replaceAll("%$1s");
/* 112 */           this.properties.put(s1, s2);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String translateKeyPrivate(String p_135026_1_)
/*     */   {
/* 123 */     String s = (String)this.properties.get(p_135026_1_);
/* 124 */     return s == null ? p_135026_1_ : s;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String formatMessage(String translateKey, Object[] parameters)
/*     */   {
/* 132 */     String s = translateKeyPrivate(translateKey);
/*     */     
/*     */     try
/*     */     {
/* 136 */       return String.format(s, parameters);
/*     */     }
/*     */     catch (IllegalFormatException var5) {}
/*     */     
/* 140 */     return "Format error: " + s;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\Locale.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */