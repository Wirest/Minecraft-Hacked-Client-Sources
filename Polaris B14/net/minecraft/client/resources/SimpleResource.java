/*     */ package net.minecraft.client.resources;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.JsonObject;
/*     */ import java.io.InputStream;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.resources.data.IMetadataSection;
/*     */ import net.minecraft.client.resources.data.IMetadataSerializer;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleResource
/*     */   implements IResource
/*     */ {
/*  18 */   private final Map<String, IMetadataSection> mapMetadataSections = Maps.newHashMap();
/*     */   private final String resourcePackName;
/*     */   private final ResourceLocation srResourceLocation;
/*     */   private final InputStream resourceInputStream;
/*     */   private final InputStream mcmetaInputStream;
/*     */   private final IMetadataSerializer srMetadataSerializer;
/*     */   private boolean mcmetaJsonChecked;
/*     */   private JsonObject mcmetaJson;
/*     */   
/*     */   public SimpleResource(String resourcePackNameIn, ResourceLocation srResourceLocationIn, InputStream resourceInputStreamIn, InputStream mcmetaInputStreamIn, IMetadataSerializer srMetadataSerializerIn)
/*     */   {
/*  29 */     this.resourcePackName = resourcePackNameIn;
/*  30 */     this.srResourceLocation = srResourceLocationIn;
/*  31 */     this.resourceInputStream = resourceInputStreamIn;
/*  32 */     this.mcmetaInputStream = mcmetaInputStreamIn;
/*  33 */     this.srMetadataSerializer = srMetadataSerializerIn;
/*     */   }
/*     */   
/*     */   public ResourceLocation getResourceLocation()
/*     */   {
/*  38 */     return this.srResourceLocation;
/*     */   }
/*     */   
/*     */   public InputStream getInputStream()
/*     */   {
/*  43 */     return this.resourceInputStream;
/*     */   }
/*     */   
/*     */   public boolean hasMetadata()
/*     */   {
/*  48 */     return this.mcmetaInputStream != null;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public <T extends IMetadataSection> T getMetadata(String p_110526_1_)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 63	net/minecraft/client/resources/SimpleResource:hasMetadata	()Z
/*     */     //   4: ifne +5 -> 9
/*     */     //   7: aconst_null
/*     */     //   8: areturn
/*     */     //   9: aload_0
/*     */     //   10: getfield 65	net/minecraft/client/resources/SimpleResource:mcmetaJson	Lcom/google/gson/JsonObject;
/*     */     //   13: ifnonnull +68 -> 81
/*     */     //   16: aload_0
/*     */     //   17: getfield 67	net/minecraft/client/resources/SimpleResource:mcmetaJsonChecked	Z
/*     */     //   20: ifne +61 -> 81
/*     */     //   23: aload_0
/*     */     //   24: iconst_1
/*     */     //   25: putfield 67	net/minecraft/client/resources/SimpleResource:mcmetaJsonChecked	Z
/*     */     //   28: aconst_null
/*     */     //   29: astore_2
/*     */     //   30: new 69	java/io/BufferedReader
/*     */     //   33: dup
/*     */     //   34: new 71	java/io/InputStreamReader
/*     */     //   37: dup
/*     */     //   38: aload_0
/*     */     //   39: getfield 44	net/minecraft/client/resources/SimpleResource:mcmetaInputStream	Ljava/io/InputStream;
/*     */     //   42: invokespecial 74	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
/*     */     //   45: invokespecial 77	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
/*     */     //   48: astore_2
/*     */     //   49: aload_0
/*     */     //   50: new 79	com/google/gson/JsonParser
/*     */     //   53: dup
/*     */     //   54: invokespecial 80	com/google/gson/JsonParser:<init>	()V
/*     */     //   57: aload_2
/*     */     //   58: invokevirtual 84	com/google/gson/JsonParser:parse	(Ljava/io/Reader;)Lcom/google/gson/JsonElement;
/*     */     //   61: invokevirtual 90	com/google/gson/JsonElement:getAsJsonObject	()Lcom/google/gson/JsonObject;
/*     */     //   64: putfield 65	net/minecraft/client/resources/SimpleResource:mcmetaJson	Lcom/google/gson/JsonObject;
/*     */     //   67: goto +10 -> 77
/*     */     //   70: astore_3
/*     */     //   71: aload_2
/*     */     //   72: invokestatic 99	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
/*     */     //   75: aload_3
/*     */     //   76: athrow
/*     */     //   77: aload_2
/*     */     //   78: invokestatic 99	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
/*     */     //   81: aload_0
/*     */     //   82: getfield 36	net/minecraft/client/resources/SimpleResource:mapMetadataSections	Ljava/util/Map;
/*     */     //   85: aload_1
/*     */     //   86: invokeinterface 105 2 0
/*     */     //   91: checkcast 107	net/minecraft/client/resources/data/IMetadataSection
/*     */     //   94: astore_2
/*     */     //   95: aload_2
/*     */     //   96: ifnonnull +16 -> 112
/*     */     //   99: aload_0
/*     */     //   100: getfield 46	net/minecraft/client/resources/SimpleResource:srMetadataSerializer	Lnet/minecraft/client/resources/data/IMetadataSerializer;
/*     */     //   103: aload_1
/*     */     //   104: aload_0
/*     */     //   105: getfield 65	net/minecraft/client/resources/SimpleResource:mcmetaJson	Lcom/google/gson/JsonObject;
/*     */     //   108: invokevirtual 113	net/minecraft/client/resources/data/IMetadataSerializer:parseMetadataSection	(Ljava/lang/String;Lcom/google/gson/JsonObject;)Lnet/minecraft/client/resources/data/IMetadataSection;
/*     */     //   111: astore_2
/*     */     //   112: aload_2
/*     */     //   113: areturn
/*     */     // Line number table:
/*     */     //   Java source line #53	-> byte code offset #0
/*     */     //   Java source line #55	-> byte code offset #7
/*     */     //   Java source line #59	-> byte code offset #9
/*     */     //   Java source line #61	-> byte code offset #23
/*     */     //   Java source line #62	-> byte code offset #28
/*     */     //   Java source line #66	-> byte code offset #30
/*     */     //   Java source line #67	-> byte code offset #49
/*     */     //   Java source line #68	-> byte code offset #67
/*     */     //   Java source line #70	-> byte code offset #70
/*     */     //   Java source line #71	-> byte code offset #71
/*     */     //   Java source line #72	-> byte code offset #75
/*     */     //   Java source line #71	-> byte code offset #77
/*     */     //   Java source line #75	-> byte code offset #81
/*     */     //   Java source line #77	-> byte code offset #95
/*     */     //   Java source line #79	-> byte code offset #99
/*     */     //   Java source line #82	-> byte code offset #112
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	114	0	this	SimpleResource
/*     */     //   0	114	1	p_110526_1_	String
/*     */     //   29	49	2	bufferedreader	java.io.BufferedReader
/*     */     //   94	19	2	t	T
/*     */     //   70	6	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   30	70	70	finally
/*     */   }
/*     */   
/*     */   public String getResourcePackName()
/*     */   {
/*  88 */     return this.resourcePackName;
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/*  93 */     if (this == p_equals_1_)
/*     */     {
/*  95 */       return true;
/*     */     }
/*  97 */     if (!(p_equals_1_ instanceof SimpleResource))
/*     */     {
/*  99 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 103 */     SimpleResource simpleresource = (SimpleResource)p_equals_1_;
/*     */     
/* 105 */     if (this.srResourceLocation != null)
/*     */     {
/* 107 */       if (!this.srResourceLocation.equals(simpleresource.srResourceLocation))
/*     */       {
/* 109 */         return false;
/*     */       }
/*     */     }
/* 112 */     else if (simpleresource.srResourceLocation != null)
/*     */     {
/* 114 */       return false;
/*     */     }
/*     */     
/* 117 */     if (this.resourcePackName != null)
/*     */     {
/* 119 */       if (!this.resourcePackName.equals(simpleresource.resourcePackName))
/*     */       {
/* 121 */         return false;
/*     */       }
/*     */     }
/* 124 */     else if (simpleresource.resourcePackName != null)
/*     */     {
/* 126 */       return false;
/*     */     }
/*     */     
/* 129 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 135 */     int i = this.resourcePackName != null ? this.resourcePackName.hashCode() : 0;
/* 136 */     i = 31 * i + (this.srResourceLocation != null ? this.srResourceLocation.hashCode() : 0);
/* 137 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\SimpleResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */