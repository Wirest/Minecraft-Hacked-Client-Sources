/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResourceIndex
/*    */ {
/* 23 */   private static final Logger logger = ;
/*    */   private final Map<String, File> resourceMap;
/*    */   
/*    */   /* Error */
/*    */   public ResourceIndex(File p_i1047_1_, String p_i1047_2_)
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: invokespecial 33	java/lang/Object:<init>	()V
/*    */     //   4: aload_0
/*    */     //   5: invokestatic 39	com/google/common/collect/Maps:newHashMap	()Ljava/util/HashMap;
/*    */     //   8: putfield 41	net/minecraft/client/resources/ResourceIndex:resourceMap	Ljava/util/Map;
/*    */     //   11: aload_2
/*    */     //   12: ifnull +357 -> 369
/*    */     //   15: new 43	java/io/File
/*    */     //   18: dup
/*    */     //   19: aload_1
/*    */     //   20: ldc 45
/*    */     //   22: invokespecial 47	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
/*    */     //   25: astore_3
/*    */     //   26: new 43	java/io/File
/*    */     //   29: dup
/*    */     //   30: aload_1
/*    */     //   31: new 49	java/lang/StringBuilder
/*    */     //   34: dup
/*    */     //   35: ldc 51
/*    */     //   37: invokespecial 54	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*    */     //   40: aload_2
/*    */     //   41: invokevirtual 58	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*    */     //   44: ldc 60
/*    */     //   46: invokevirtual 58	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*    */     //   49: invokevirtual 64	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*    */     //   52: invokespecial 47	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
/*    */     //   55: astore 4
/*    */     //   57: aconst_null
/*    */     //   58: astore 5
/*    */     //   60: aload 4
/*    */     //   62: getstatic 70	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
/*    */     //   65: invokestatic 76	com/google/common/io/Files:newReader	(Ljava/io/File;Ljava/nio/charset/Charset;)Ljava/io/BufferedReader;
/*    */     //   68: astore 5
/*    */     //   70: new 78	com/google/gson/JsonParser
/*    */     //   73: dup
/*    */     //   74: invokespecial 79	com/google/gson/JsonParser:<init>	()V
/*    */     //   77: aload 5
/*    */     //   79: invokevirtual 83	com/google/gson/JsonParser:parse	(Ljava/io/Reader;)Lcom/google/gson/JsonElement;
/*    */     //   82: invokevirtual 89	com/google/gson/JsonElement:getAsJsonObject	()Lcom/google/gson/JsonObject;
/*    */     //   85: astore 6
/*    */     //   87: aload 6
/*    */     //   89: ldc 45
/*    */     //   91: aconst_null
/*    */     //   92: invokestatic 95	net/minecraft/util/JsonUtils:getJsonObject	(Lcom/google/gson/JsonObject;Ljava/lang/String;Lcom/google/gson/JsonObject;)Lcom/google/gson/JsonObject;
/*    */     //   95: astore 7
/*    */     //   97: aload 7
/*    */     //   99: ifnull +265 -> 364
/*    */     //   102: aload 7
/*    */     //   104: invokevirtual 101	com/google/gson/JsonObject:entrySet	()Ljava/util/Set;
/*    */     //   107: invokeinterface 107 1 0
/*    */     //   112: astore 9
/*    */     //   114: goto +157 -> 271
/*    */     //   117: aload 9
/*    */     //   119: invokeinterface 117 1 0
/*    */     //   124: checkcast 7	java/util/Map$Entry
/*    */     //   127: astore 8
/*    */     //   129: aload 8
/*    */     //   131: invokeinterface 120 1 0
/*    */     //   136: checkcast 97	com/google/gson/JsonObject
/*    */     //   139: astore 10
/*    */     //   141: aload 8
/*    */     //   143: invokeinterface 123 1 0
/*    */     //   148: checkcast 109	java/lang/String
/*    */     //   151: astore 11
/*    */     //   153: aload 11
/*    */     //   155: ldc 125
/*    */     //   157: iconst_2
/*    */     //   158: invokevirtual 129	java/lang/String:split	(Ljava/lang/String;I)[Ljava/lang/String;
/*    */     //   161: astore 12
/*    */     //   163: aload 12
/*    */     //   165: arraylength
/*    */     //   166: iconst_1
/*    */     //   167: if_icmpne +10 -> 177
/*    */     //   170: aload 12
/*    */     //   172: iconst_0
/*    */     //   173: aaload
/*    */     //   174: goto +32 -> 206
/*    */     //   177: new 49	java/lang/StringBuilder
/*    */     //   180: dup
/*    */     //   181: aload 12
/*    */     //   183: iconst_0
/*    */     //   184: aaload
/*    */     //   185: invokestatic 135	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*    */     //   188: invokespecial 54	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*    */     //   191: ldc -119
/*    */     //   193: invokevirtual 58	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*    */     //   196: aload 12
/*    */     //   198: iconst_1
/*    */     //   199: aaload
/*    */     //   200: invokevirtual 58	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*    */     //   203: invokevirtual 64	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*    */     //   206: astore 13
/*    */     //   208: aload 10
/*    */     //   210: ldc -117
/*    */     //   212: invokestatic 143	net/minecraft/util/JsonUtils:getString	(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/lang/String;
/*    */     //   215: astore 14
/*    */     //   217: new 43	java/io/File
/*    */     //   220: dup
/*    */     //   221: aload_3
/*    */     //   222: new 49	java/lang/StringBuilder
/*    */     //   225: dup
/*    */     //   226: aload 14
/*    */     //   228: iconst_0
/*    */     //   229: iconst_2
/*    */     //   230: invokevirtual 147	java/lang/String:substring	(II)Ljava/lang/String;
/*    */     //   233: invokestatic 135	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*    */     //   236: invokespecial 54	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*    */     //   239: ldc 125
/*    */     //   241: invokevirtual 58	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*    */     //   244: aload 14
/*    */     //   246: invokevirtual 58	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*    */     //   249: invokevirtual 64	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*    */     //   252: invokespecial 47	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
/*    */     //   255: astore 15
/*    */     //   257: aload_0
/*    */     //   258: getfield 41	net/minecraft/client/resources/ResourceIndex:resourceMap	Ljava/util/Map;
/*    */     //   261: aload 13
/*    */     //   263: aload 15
/*    */     //   265: invokeinterface 151 3 0
/*    */     //   270: pop
/*    */     //   271: aload 9
/*    */     //   273: invokeinterface 155 1 0
/*    */     //   278: ifne -161 -> 117
/*    */     //   281: goto +83 -> 364
/*    */     //   284: astore 6
/*    */     //   286: getstatic 25	net/minecraft/client/resources/ResourceIndex:logger	Lorg/apache/logging/log4j/Logger;
/*    */     //   289: new 49	java/lang/StringBuilder
/*    */     //   292: dup
/*    */     //   293: ldc -99
/*    */     //   295: invokespecial 54	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*    */     //   298: aload 4
/*    */     //   300: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*    */     //   303: invokevirtual 64	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*    */     //   306: invokeinterface 165 2 0
/*    */     //   311: aload 5
/*    */     //   313: invokestatic 171	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
/*    */     //   316: goto +53 -> 369
/*    */     //   319: astore 6
/*    */     //   321: getstatic 25	net/minecraft/client/resources/ResourceIndex:logger	Lorg/apache/logging/log4j/Logger;
/*    */     //   324: new 49	java/lang/StringBuilder
/*    */     //   327: dup
/*    */     //   328: ldc -83
/*    */     //   330: invokespecial 54	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*    */     //   333: aload 4
/*    */     //   335: invokevirtual 160	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*    */     //   338: invokevirtual 64	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*    */     //   341: invokeinterface 165 2 0
/*    */     //   346: aload 5
/*    */     //   348: invokestatic 171	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
/*    */     //   351: goto +18 -> 369
/*    */     //   354: astore 16
/*    */     //   356: aload 5
/*    */     //   358: invokestatic 171	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
/*    */     //   361: aload 16
/*    */     //   363: athrow
/*    */     //   364: aload 5
/*    */     //   366: invokestatic 171	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
/*    */     //   369: return
/*    */     // Line number table:
/*    */     //   Java source line #26	-> byte code offset #0
/*    */     //   Java source line #24	-> byte code offset #4
/*    */     //   Java source line #28	-> byte code offset #11
/*    */     //   Java source line #30	-> byte code offset #15
/*    */     //   Java source line #31	-> byte code offset #26
/*    */     //   Java source line #32	-> byte code offset #57
/*    */     //   Java source line #36	-> byte code offset #60
/*    */     //   Java source line #37	-> byte code offset #70
/*    */     //   Java source line #38	-> byte code offset #87
/*    */     //   Java source line #40	-> byte code offset #97
/*    */     //   Java source line #42	-> byte code offset #102
/*    */     //   Java source line #44	-> byte code offset #129
/*    */     //   Java source line #45	-> byte code offset #141
/*    */     //   Java source line #46	-> byte code offset #153
/*    */     //   Java source line #47	-> byte code offset #163
/*    */     //   Java source line #48	-> byte code offset #208
/*    */     //   Java source line #49	-> byte code offset #217
/*    */     //   Java source line #50	-> byte code offset #257
/*    */     //   Java source line #42	-> byte code offset #271
/*    */     //   Java source line #53	-> byte code offset #281
/*    */     //   Java source line #54	-> byte code offset #284
/*    */     //   Java source line #56	-> byte code offset #286
/*    */     //   Java source line #64	-> byte code offset #311
/*    */     //   Java source line #58	-> byte code offset #319
/*    */     //   Java source line #60	-> byte code offset #321
/*    */     //   Java source line #64	-> byte code offset #346
/*    */     //   Java source line #63	-> byte code offset #354
/*    */     //   Java source line #64	-> byte code offset #356
/*    */     //   Java source line #65	-> byte code offset #361
/*    */     //   Java source line #64	-> byte code offset #364
/*    */     //   Java source line #67	-> byte code offset #369
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	370	0	this	ResourceIndex
/*    */     //   0	370	1	p_i1047_1_	File
/*    */     //   0	370	2	p_i1047_2_	String
/*    */     //   25	197	3	file1	File
/*    */     //   55	279	4	file2	File
/*    */     //   58	307	5	bufferedreader	java.io.BufferedReader
/*    */     //   85	3	6	jsonobject	com.google.gson.JsonObject
/*    */     //   284	3	6	var20	com.google.gson.JsonParseException
/*    */     //   319	3	6	var21	java.io.FileNotFoundException
/*    */     //   95	8	7	jsonobject1	com.google.gson.JsonObject
/*    */     //   127	15	8	entry	java.util.Map.Entry<String, com.google.gson.JsonElement>
/*    */     //   112	160	9	localIterator	java.util.Iterator
/*    */     //   139	70	10	jsonobject2	com.google.gson.JsonObject
/*    */     //   151	3	11	s	String
/*    */     //   161	36	12	astring	String[]
/*    */     //   206	56	13	s1	String
/*    */     //   215	30	14	s2	String
/*    */     //   255	9	15	file3	File
/*    */     //   354	8	16	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   60	281	284	com/google/gson/JsonParseException
/*    */     //   60	281	319	java/io/FileNotFoundException
/*    */     //   60	311	354	finally
/*    */     //   319	346	354	finally
/*    */   }
/*    */   
/*    */   public Map<String, File> getResourceMap()
/*    */   {
/* 71 */     return this.resourceMap;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\ResourceIndex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */