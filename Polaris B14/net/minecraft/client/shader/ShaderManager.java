/*     */ package net.minecraft.client.shader;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.texture.ITextureObject;
/*     */ import net.minecraft.client.util.JsonBlendingMode;
/*     */ import net.minecraft.client.util.JsonException;
/*     */ import net.minecraft.util.JsonUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShaderManager
/*     */ {
/*  28 */   private static final Logger logger = ;
/*  29 */   private static final ShaderDefault defaultShaderUniform = new ShaderDefault();
/*  30 */   private static ShaderManager staticShaderManager = null;
/*  31 */   private static int currentProgram = -1;
/*  32 */   private static boolean field_148000_e = true;
/*     */   private final Map<String, Object> shaderSamplers;
/*     */   private final List<String> samplerNames;
/*     */   private final List<Integer> shaderSamplerLocations;
/*     */   private final List<ShaderUniform> shaderUniforms;
/*     */   private final List<Integer> shaderUniformLocations;
/*     */   private final Map<String, ShaderUniform> mappedShaderUniforms;
/*     */   private final int program;
/*     */   private final String programFilename;
/*     */   private final boolean useFaceCulling;
/*     */   private boolean isDirty;
/*     */   private final JsonBlendingMode field_148016_p;
/*     */   private final List<Integer> attribLocations;
/*     */   private final List<String> attributes;
/*     */   private final ShaderLoader vertexShaderLoader;
/*     */   private final ShaderLoader fragmentShaderLoader;
/*     */   
/*     */   /* Error */
/*     */   public ShaderManager(net.minecraft.client.resources.IResourceManager resourceManager, String programName)
/*     */     throws JsonException, java.io.IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokespecial 76	java/lang/Object:<init>	()V
/*     */     //   4: aload_0
/*     */     //   5: invokestatic 82	com/google/common/collect/Maps:newHashMap	()Ljava/util/HashMap;
/*     */     //   8: putfield 84	net/minecraft/client/shader/ShaderManager:shaderSamplers	Ljava/util/Map;
/*     */     //   11: aload_0
/*     */     //   12: invokestatic 90	com/google/common/collect/Lists:newArrayList	()Ljava/util/ArrayList;
/*     */     //   15: putfield 92	net/minecraft/client/shader/ShaderManager:samplerNames	Ljava/util/List;
/*     */     //   18: aload_0
/*     */     //   19: invokestatic 90	com/google/common/collect/Lists:newArrayList	()Ljava/util/ArrayList;
/*     */     //   22: putfield 94	net/minecraft/client/shader/ShaderManager:shaderSamplerLocations	Ljava/util/List;
/*     */     //   25: aload_0
/*     */     //   26: invokestatic 90	com/google/common/collect/Lists:newArrayList	()Ljava/util/ArrayList;
/*     */     //   29: putfield 96	net/minecraft/client/shader/ShaderManager:shaderUniforms	Ljava/util/List;
/*     */     //   32: aload_0
/*     */     //   33: invokestatic 90	com/google/common/collect/Lists:newArrayList	()Ljava/util/ArrayList;
/*     */     //   36: putfield 98	net/minecraft/client/shader/ShaderManager:shaderUniformLocations	Ljava/util/List;
/*     */     //   39: aload_0
/*     */     //   40: invokestatic 82	com/google/common/collect/Maps:newHashMap	()Ljava/util/HashMap;
/*     */     //   43: putfield 100	net/minecraft/client/shader/ShaderManager:mappedShaderUniforms	Ljava/util/Map;
/*     */     //   46: new 102	com/google/gson/JsonParser
/*     */     //   49: dup
/*     */     //   50: invokespecial 103	com/google/gson/JsonParser:<init>	()V
/*     */     //   53: astore_3
/*     */     //   54: new 105	net/minecraft/util/ResourceLocation
/*     */     //   57: dup
/*     */     //   58: new 107	java/lang/StringBuilder
/*     */     //   61: dup
/*     */     //   62: ldc 109
/*     */     //   64: invokespecial 112	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   67: aload_2
/*     */     //   68: invokevirtual 116	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   71: ldc 118
/*     */     //   73: invokevirtual 116	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   76: invokevirtual 122	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   79: invokespecial 123	net/minecraft/util/ResourceLocation:<init>	(Ljava/lang/String;)V
/*     */     //   82: astore 4
/*     */     //   84: aload_0
/*     */     //   85: aload_2
/*     */     //   86: putfield 125	net/minecraft/client/shader/ShaderManager:programFilename	Ljava/lang/String;
/*     */     //   89: aconst_null
/*     */     //   90: astore 5
/*     */     //   92: aload_1
/*     */     //   93: aload 4
/*     */     //   95: invokeinterface 131 2 0
/*     */     //   100: invokeinterface 137 1 0
/*     */     //   105: astore 5
/*     */     //   107: aload_3
/*     */     //   108: aload 5
/*     */     //   110: getstatic 143	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
/*     */     //   113: invokestatic 148	org/apache/commons/io/IOUtils:toString	(Ljava/io/InputStream;Ljava/nio/charset/Charset;)Ljava/lang/String;
/*     */     //   116: invokevirtual 152	com/google/gson/JsonParser:parse	(Ljava/lang/String;)Lcom/google/gson/JsonElement;
/*     */     //   119: invokevirtual 158	com/google/gson/JsonElement:getAsJsonObject	()Lcom/google/gson/JsonObject;
/*     */     //   122: astore 6
/*     */     //   124: aload 6
/*     */     //   126: ldc -96
/*     */     //   128: invokestatic 166	net/minecraft/util/JsonUtils:getString	(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   131: astore 7
/*     */     //   133: aload 6
/*     */     //   135: ldc -88
/*     */     //   137: invokestatic 166	net/minecraft/util/JsonUtils:getString	(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   140: astore 8
/*     */     //   142: aload 6
/*     */     //   144: ldc -86
/*     */     //   146: aconst_null
/*     */     //   147: invokestatic 174	net/minecraft/util/JsonUtils:getJsonArray	(Lcom/google/gson/JsonObject;Ljava/lang/String;Lcom/google/gson/JsonArray;)Lcom/google/gson/JsonArray;
/*     */     //   150: astore 9
/*     */     //   152: aload 9
/*     */     //   154: ifnull +89 -> 243
/*     */     //   157: iconst_0
/*     */     //   158: istore 10
/*     */     //   160: aload 9
/*     */     //   162: invokevirtual 180	com/google/gson/JsonArray:iterator	()Ljava/util/Iterator;
/*     */     //   165: astore 12
/*     */     //   167: goto +66 -> 233
/*     */     //   170: aload 12
/*     */     //   172: invokeinterface 192 1 0
/*     */     //   177: checkcast 154	com/google/gson/JsonElement
/*     */     //   180: astore 11
/*     */     //   182: aload_0
/*     */     //   183: aload 11
/*     */     //   185: invokespecial 196	net/minecraft/client/shader/ShaderManager:parseSampler	(Lcom/google/gson/JsonElement;)V
/*     */     //   188: goto +42 -> 230
/*     */     //   191: astore 13
/*     */     //   193: aload 13
/*     */     //   195: invokestatic 200	net/minecraft/client/util/JsonException:func_151379_a	(Ljava/lang/Exception;)Lnet/minecraft/client/util/JsonException;
/*     */     //   198: astore 14
/*     */     //   200: aload 14
/*     */     //   202: new 107	java/lang/StringBuilder
/*     */     //   205: dup
/*     */     //   206: ldc -54
/*     */     //   208: invokespecial 112	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   211: iload 10
/*     */     //   213: invokevirtual 205	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */     //   216: ldc -49
/*     */     //   218: invokevirtual 116	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   221: invokevirtual 122	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   224: invokevirtual 210	net/minecraft/client/util/JsonException:func_151380_a	(Ljava/lang/String;)V
/*     */     //   227: aload 14
/*     */     //   229: athrow
/*     */     //   230: iinc 10 1
/*     */     //   233: aload 12
/*     */     //   235: invokeinterface 214 1 0
/*     */     //   240: ifne -70 -> 170
/*     */     //   243: aload 6
/*     */     //   245: ldc -41
/*     */     //   247: aconst_null
/*     */     //   248: invokestatic 174	net/minecraft/util/JsonUtils:getJsonArray	(Lcom/google/gson/JsonObject;Ljava/lang/String;Lcom/google/gson/JsonArray;)Lcom/google/gson/JsonArray;
/*     */     //   251: astore 10
/*     */     //   253: aload 10
/*     */     //   255: ifnull +127 -> 382
/*     */     //   258: iconst_0
/*     */     //   259: istore 11
/*     */     //   261: aload_0
/*     */     //   262: aload 10
/*     */     //   264: invokevirtual 219	com/google/gson/JsonArray:size	()I
/*     */     //   267: invokestatic 223	com/google/common/collect/Lists:newArrayListWithCapacity	(I)Ljava/util/ArrayList;
/*     */     //   270: putfield 225	net/minecraft/client/shader/ShaderManager:attribLocations	Ljava/util/List;
/*     */     //   273: aload_0
/*     */     //   274: aload 10
/*     */     //   276: invokevirtual 219	com/google/gson/JsonArray:size	()I
/*     */     //   279: invokestatic 223	com/google/common/collect/Lists:newArrayListWithCapacity	(I)Ljava/util/ArrayList;
/*     */     //   282: putfield 227	net/minecraft/client/shader/ShaderManager:attributes	Ljava/util/List;
/*     */     //   285: aload 10
/*     */     //   287: invokevirtual 180	com/google/gson/JsonArray:iterator	()Ljava/util/Iterator;
/*     */     //   290: astore 13
/*     */     //   292: goto +77 -> 369
/*     */     //   295: aload 13
/*     */     //   297: invokeinterface 192 1 0
/*     */     //   302: checkcast 154	com/google/gson/JsonElement
/*     */     //   305: astore 12
/*     */     //   307: aload_0
/*     */     //   308: getfield 227	net/minecraft/client/shader/ShaderManager:attributes	Ljava/util/List;
/*     */     //   311: aload 12
/*     */     //   313: ldc -27
/*     */     //   315: invokestatic 232	net/minecraft/util/JsonUtils:getString	(Lcom/google/gson/JsonElement;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   318: invokeinterface 238 2 0
/*     */     //   323: pop
/*     */     //   324: goto +42 -> 366
/*     */     //   327: astore 14
/*     */     //   329: aload 14
/*     */     //   331: invokestatic 200	net/minecraft/client/util/JsonException:func_151379_a	(Ljava/lang/Exception;)Lnet/minecraft/client/util/JsonException;
/*     */     //   334: astore 15
/*     */     //   336: aload 15
/*     */     //   338: new 107	java/lang/StringBuilder
/*     */     //   341: dup
/*     */     //   342: ldc -16
/*     */     //   344: invokespecial 112	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   347: iload 11
/*     */     //   349: invokevirtual 205	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */     //   352: ldc -49
/*     */     //   354: invokevirtual 116	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   357: invokevirtual 122	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   360: invokevirtual 210	net/minecraft/client/util/JsonException:func_151380_a	(Ljava/lang/String;)V
/*     */     //   363: aload 15
/*     */     //   365: athrow
/*     */     //   366: iinc 11 1
/*     */     //   369: aload 13
/*     */     //   371: invokeinterface 214 1 0
/*     */     //   376: ifne -81 -> 295
/*     */     //   379: goto +13 -> 392
/*     */     //   382: aload_0
/*     */     //   383: aconst_null
/*     */     //   384: putfield 225	net/minecraft/client/shader/ShaderManager:attribLocations	Ljava/util/List;
/*     */     //   387: aload_0
/*     */     //   388: aconst_null
/*     */     //   389: putfield 227	net/minecraft/client/shader/ShaderManager:attributes	Ljava/util/List;
/*     */     //   392: aload 6
/*     */     //   394: ldc -14
/*     */     //   396: aconst_null
/*     */     //   397: invokestatic 174	net/minecraft/util/JsonUtils:getJsonArray	(Lcom/google/gson/JsonObject;Ljava/lang/String;Lcom/google/gson/JsonArray;)Lcom/google/gson/JsonArray;
/*     */     //   400: astore 11
/*     */     //   402: aload 11
/*     */     //   404: ifnull +89 -> 493
/*     */     //   407: iconst_0
/*     */     //   408: istore 12
/*     */     //   410: aload 11
/*     */     //   412: invokevirtual 180	com/google/gson/JsonArray:iterator	()Ljava/util/Iterator;
/*     */     //   415: astore 14
/*     */     //   417: goto +66 -> 483
/*     */     //   420: aload 14
/*     */     //   422: invokeinterface 192 1 0
/*     */     //   427: checkcast 154	com/google/gson/JsonElement
/*     */     //   430: astore 13
/*     */     //   432: aload_0
/*     */     //   433: aload 13
/*     */     //   435: invokespecial 245	net/minecraft/client/shader/ShaderManager:parseUniform	(Lcom/google/gson/JsonElement;)V
/*     */     //   438: goto +42 -> 480
/*     */     //   441: astore 15
/*     */     //   443: aload 15
/*     */     //   445: invokestatic 200	net/minecraft/client/util/JsonException:func_151379_a	(Ljava/lang/Exception;)Lnet/minecraft/client/util/JsonException;
/*     */     //   448: astore 16
/*     */     //   450: aload 16
/*     */     //   452: new 107	java/lang/StringBuilder
/*     */     //   455: dup
/*     */     //   456: ldc -9
/*     */     //   458: invokespecial 112	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   461: iload 12
/*     */     //   463: invokevirtual 205	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */     //   466: ldc -49
/*     */     //   468: invokevirtual 116	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   471: invokevirtual 122	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   474: invokevirtual 210	net/minecraft/client/util/JsonException:func_151380_a	(Ljava/lang/String;)V
/*     */     //   477: aload 16
/*     */     //   479: athrow
/*     */     //   480: iinc 12 1
/*     */     //   483: aload 14
/*     */     //   485: invokeinterface 214 1 0
/*     */     //   490: ifne -70 -> 420
/*     */     //   493: aload_0
/*     */     //   494: aload 6
/*     */     //   496: ldc -7
/*     */     //   498: aconst_null
/*     */     //   499: invokestatic 253	net/minecraft/util/JsonUtils:getJsonObject	(Lcom/google/gson/JsonObject;Ljava/lang/String;Lcom/google/gson/JsonObject;)Lcom/google/gson/JsonObject;
/*     */     //   502: invokestatic 259	net/minecraft/client/util/JsonBlendingMode:func_148110_a	(Lcom/google/gson/JsonObject;)Lnet/minecraft/client/util/JsonBlendingMode;
/*     */     //   505: putfield 261	net/minecraft/client/shader/ShaderManager:field_148016_p	Lnet/minecraft/client/util/JsonBlendingMode;
/*     */     //   508: aload_0
/*     */     //   509: aload 6
/*     */     //   511: ldc_w 263
/*     */     //   514: iconst_1
/*     */     //   515: invokestatic 267	net/minecraft/util/JsonUtils:getBoolean	(Lcom/google/gson/JsonObject;Ljava/lang/String;Z)Z
/*     */     //   518: putfield 269	net/minecraft/client/shader/ShaderManager:useFaceCulling	Z
/*     */     //   521: aload_0
/*     */     //   522: aload_1
/*     */     //   523: getstatic 273	net/minecraft/client/shader/ShaderLoader$ShaderType:VERTEX	Lnet/minecraft/client/shader/ShaderLoader$ShaderType;
/*     */     //   526: aload 7
/*     */     //   528: invokestatic 277	net/minecraft/client/shader/ShaderLoader:loadShader	(Lnet/minecraft/client/resources/IResourceManager;Lnet/minecraft/client/shader/ShaderLoader$ShaderType;Ljava/lang/String;)Lnet/minecraft/client/shader/ShaderLoader;
/*     */     //   531: putfield 279	net/minecraft/client/shader/ShaderManager:vertexShaderLoader	Lnet/minecraft/client/shader/ShaderLoader;
/*     */     //   534: aload_0
/*     */     //   535: aload_1
/*     */     //   536: getstatic 282	net/minecraft/client/shader/ShaderLoader$ShaderType:FRAGMENT	Lnet/minecraft/client/shader/ShaderLoader$ShaderType;
/*     */     //   539: aload 8
/*     */     //   541: invokestatic 277	net/minecraft/client/shader/ShaderLoader:loadShader	(Lnet/minecraft/client/resources/IResourceManager;Lnet/minecraft/client/shader/ShaderLoader$ShaderType;Ljava/lang/String;)Lnet/minecraft/client/shader/ShaderLoader;
/*     */     //   544: putfield 284	net/minecraft/client/shader/ShaderManager:fragmentShaderLoader	Lnet/minecraft/client/shader/ShaderLoader;
/*     */     //   547: aload_0
/*     */     //   548: invokestatic 290	net/minecraft/client/shader/ShaderLinkHelper:getStaticShaderLinkHelper	()Lnet/minecraft/client/shader/ShaderLinkHelper;
/*     */     //   551: invokevirtual 293	net/minecraft/client/shader/ShaderLinkHelper:createProgram	()I
/*     */     //   554: putfield 295	net/minecraft/client/shader/ShaderManager:program	I
/*     */     //   557: invokestatic 290	net/minecraft/client/shader/ShaderLinkHelper:getStaticShaderLinkHelper	()Lnet/minecraft/client/shader/ShaderLinkHelper;
/*     */     //   560: aload_0
/*     */     //   561: invokevirtual 299	net/minecraft/client/shader/ShaderLinkHelper:linkProgram	(Lnet/minecraft/client/shader/ShaderManager;)V
/*     */     //   564: aload_0
/*     */     //   565: invokespecial 302	net/minecraft/client/shader/ShaderManager:setupUniforms	()V
/*     */     //   568: aload_0
/*     */     //   569: getfield 227	net/minecraft/client/shader/ShaderManager:attributes	Ljava/util/List;
/*     */     //   572: ifnull +100 -> 672
/*     */     //   575: aload_0
/*     */     //   576: getfield 227	net/minecraft/client/shader/ShaderManager:attributes	Ljava/util/List;
/*     */     //   579: invokeinterface 303 1 0
/*     */     //   584: astore 13
/*     */     //   586: goto +41 -> 627
/*     */     //   589: aload 13
/*     */     //   591: invokeinterface 192 1 0
/*     */     //   596: checkcast 182	java/lang/String
/*     */     //   599: astore 12
/*     */     //   601: aload_0
/*     */     //   602: getfield 295	net/minecraft/client/shader/ShaderManager:program	I
/*     */     //   605: aload 12
/*     */     //   607: invokestatic 309	net/minecraft/client/renderer/OpenGlHelper:glGetAttribLocation	(ILjava/lang/CharSequence;)I
/*     */     //   610: istore 14
/*     */     //   612: aload_0
/*     */     //   613: getfield 225	net/minecraft/client/shader/ShaderManager:attribLocations	Ljava/util/List;
/*     */     //   616: iload 14
/*     */     //   618: invokestatic 315	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   621: invokeinterface 238 2 0
/*     */     //   626: pop
/*     */     //   627: aload 13
/*     */     //   629: invokeinterface 214 1 0
/*     */     //   634: ifne -45 -> 589
/*     */     //   637: goto +35 -> 672
/*     */     //   640: astore 6
/*     */     //   642: aload 6
/*     */     //   644: invokestatic 200	net/minecraft/client/util/JsonException:func_151379_a	(Ljava/lang/Exception;)Lnet/minecraft/client/util/JsonException;
/*     */     //   647: astore 7
/*     */     //   649: aload 7
/*     */     //   651: aload 4
/*     */     //   653: invokevirtual 318	net/minecraft/util/ResourceLocation:getResourcePath	()Ljava/lang/String;
/*     */     //   656: invokevirtual 321	net/minecraft/client/util/JsonException:func_151381_b	(Ljava/lang/String;)V
/*     */     //   659: aload 7
/*     */     //   661: athrow
/*     */     //   662: astore 17
/*     */     //   664: aload 5
/*     */     //   666: invokestatic 327	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*     */     //   669: aload 17
/*     */     //   671: athrow
/*     */     //   672: aload 5
/*     */     //   674: invokestatic 327	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*     */     //   677: aload_0
/*     */     //   678: invokevirtual 330	net/minecraft/client/shader/ShaderManager:markDirty	()V
/*     */     //   681: return
/*     */     // Line number table:
/*     */     //   Java source line #49	-> byte code offset #0
/*     */     //   Java source line #33	-> byte code offset #4
/*     */     //   Java source line #34	-> byte code offset #11
/*     */     //   Java source line #35	-> byte code offset #18
/*     */     //   Java source line #36	-> byte code offset #25
/*     */     //   Java source line #37	-> byte code offset #32
/*     */     //   Java source line #38	-> byte code offset #39
/*     */     //   Java source line #51	-> byte code offset #46
/*     */     //   Java source line #52	-> byte code offset #54
/*     */     //   Java source line #53	-> byte code offset #84
/*     */     //   Java source line #54	-> byte code offset #89
/*     */     //   Java source line #58	-> byte code offset #92
/*     */     //   Java source line #59	-> byte code offset #107
/*     */     //   Java source line #60	-> byte code offset #124
/*     */     //   Java source line #61	-> byte code offset #133
/*     */     //   Java source line #62	-> byte code offset #142
/*     */     //   Java source line #64	-> byte code offset #152
/*     */     //   Java source line #66	-> byte code offset #157
/*     */     //   Java source line #68	-> byte code offset #160
/*     */     //   Java source line #72	-> byte code offset #182
/*     */     //   Java source line #73	-> byte code offset #188
/*     */     //   Java source line #74	-> byte code offset #191
/*     */     //   Java source line #76	-> byte code offset #193
/*     */     //   Java source line #77	-> byte code offset #200
/*     */     //   Java source line #78	-> byte code offset #227
/*     */     //   Java source line #81	-> byte code offset #230
/*     */     //   Java source line #68	-> byte code offset #233
/*     */     //   Java source line #85	-> byte code offset #243
/*     */     //   Java source line #87	-> byte code offset #253
/*     */     //   Java source line #89	-> byte code offset #258
/*     */     //   Java source line #90	-> byte code offset #261
/*     */     //   Java source line #91	-> byte code offset #273
/*     */     //   Java source line #93	-> byte code offset #285
/*     */     //   Java source line #97	-> byte code offset #307
/*     */     //   Java source line #98	-> byte code offset #324
/*     */     //   Java source line #99	-> byte code offset #327
/*     */     //   Java source line #101	-> byte code offset #329
/*     */     //   Java source line #102	-> byte code offset #336
/*     */     //   Java source line #103	-> byte code offset #363
/*     */     //   Java source line #106	-> byte code offset #366
/*     */     //   Java source line #93	-> byte code offset #369
/*     */     //   Java source line #108	-> byte code offset #379
/*     */     //   Java source line #111	-> byte code offset #382
/*     */     //   Java source line #112	-> byte code offset #387
/*     */     //   Java source line #115	-> byte code offset #392
/*     */     //   Java source line #117	-> byte code offset #402
/*     */     //   Java source line #119	-> byte code offset #407
/*     */     //   Java source line #121	-> byte code offset #410
/*     */     //   Java source line #125	-> byte code offset #432
/*     */     //   Java source line #126	-> byte code offset #438
/*     */     //   Java source line #127	-> byte code offset #441
/*     */     //   Java source line #129	-> byte code offset #443
/*     */     //   Java source line #130	-> byte code offset #450
/*     */     //   Java source line #131	-> byte code offset #477
/*     */     //   Java source line #134	-> byte code offset #480
/*     */     //   Java source line #121	-> byte code offset #483
/*     */     //   Java source line #138	-> byte code offset #493
/*     */     //   Java source line #139	-> byte code offset #508
/*     */     //   Java source line #140	-> byte code offset #521
/*     */     //   Java source line #141	-> byte code offset #534
/*     */     //   Java source line #142	-> byte code offset #547
/*     */     //   Java source line #143	-> byte code offset #557
/*     */     //   Java source line #144	-> byte code offset #564
/*     */     //   Java source line #146	-> byte code offset #568
/*     */     //   Java source line #148	-> byte code offset #575
/*     */     //   Java source line #150	-> byte code offset #601
/*     */     //   Java source line #151	-> byte code offset #612
/*     */     //   Java source line #148	-> byte code offset #627
/*     */     //   Java source line #154	-> byte code offset #637
/*     */     //   Java source line #155	-> byte code offset #640
/*     */     //   Java source line #157	-> byte code offset #642
/*     */     //   Java source line #158	-> byte code offset #649
/*     */     //   Java source line #159	-> byte code offset #659
/*     */     //   Java source line #162	-> byte code offset #662
/*     */     //   Java source line #163	-> byte code offset #664
/*     */     //   Java source line #164	-> byte code offset #669
/*     */     //   Java source line #163	-> byte code offset #672
/*     */     //   Java source line #166	-> byte code offset #677
/*     */     //   Java source line #167	-> byte code offset #681
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	682	0	this	ShaderManager
/*     */     //   0	682	1	resourceManager	net.minecraft.client.resources.IResourceManager
/*     */     //   0	682	2	programName	String
/*     */     //   53	55	3	jsonparser	com.google.gson.JsonParser
/*     */     //   82	570	4	resourcelocation	net.minecraft.util.ResourceLocation
/*     */     //   90	583	5	inputstream	java.io.InputStream
/*     */     //   122	388	6	jsonobject	JsonObject
/*     */     //   640	3	6	exception3	Exception
/*     */     //   131	396	7	s	String
/*     */     //   647	13	7	jsonexception	JsonException
/*     */     //   140	400	8	s1	String
/*     */     //   150	11	9	jsonarray	JsonArray
/*     */     //   158	73	10	i	int
/*     */     //   251	35	10	jsonarray1	JsonArray
/*     */     //   180	4	11	jsonelement	JsonElement
/*     */     //   259	108	11	j	int
/*     */     //   400	11	11	jsonarray2	JsonArray
/*     */     //   165	69	12	localIterator	Iterator
/*     */     //   305	7	12	jsonelement1	JsonElement
/*     */     //   408	73	12	k	int
/*     */     //   599	7	12	s2	String
/*     */     //   191	179	13	exception2	Exception
/*     */     //   430	198	13	jsonelement2	JsonElement
/*     */     //   198	30	14	jsonexception1	JsonException
/*     */     //   327	157	14	exception1	Exception
/*     */     //   610	7	14	l	int
/*     */     //   334	30	15	jsonexception2	JsonException
/*     */     //   441	3	15	exception	Exception
/*     */     //   448	30	16	jsonexception3	JsonException
/*     */     //   662	8	17	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   182	188	191	java/lang/Exception
/*     */     //   307	324	327	java/lang/Exception
/*     */     //   432	438	441	java/lang/Exception
/*     */     //   92	637	640	java/lang/Exception
/*     */     //   92	662	662	finally
/*     */   }
/*     */   
/*     */   public void deleteShader()
/*     */   {
/* 171 */     ShaderLinkHelper.getStaticShaderLinkHelper().deleteShader(this);
/*     */   }
/*     */   
/*     */   public void endShader()
/*     */   {
/* 176 */     OpenGlHelper.glUseProgram(0);
/* 177 */     currentProgram = -1;
/* 178 */     staticShaderManager = null;
/* 179 */     field_148000_e = true;
/*     */     
/* 181 */     for (int i = 0; i < this.shaderSamplerLocations.size(); i++)
/*     */     {
/* 183 */       if (this.shaderSamplers.get(this.samplerNames.get(i)) != null)
/*     */       {
/* 185 */         GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + i);
/* 186 */         GlStateManager.bindTexture(0);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void useShader()
/*     */   {
/* 193 */     this.isDirty = false;
/* 194 */     staticShaderManager = this;
/* 195 */     this.field_148016_p.func_148109_a();
/*     */     
/* 197 */     if (this.program != currentProgram)
/*     */     {
/* 199 */       OpenGlHelper.glUseProgram(this.program);
/* 200 */       currentProgram = this.program;
/*     */     }
/*     */     
/* 203 */     if (this.useFaceCulling)
/*     */     {
/* 205 */       GlStateManager.enableCull();
/*     */     }
/*     */     else
/*     */     {
/* 209 */       GlStateManager.disableCull();
/*     */     }
/*     */     
/* 212 */     for (int i = 0; i < this.shaderSamplerLocations.size(); i++)
/*     */     {
/* 214 */       if (this.shaderSamplers.get(this.samplerNames.get(i)) != null)
/*     */       {
/* 216 */         GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + i);
/* 217 */         GlStateManager.enableTexture2D();
/* 218 */         object = this.shaderSamplers.get(this.samplerNames.get(i));
/* 219 */         int j = -1;
/*     */         
/* 221 */         if ((object instanceof Framebuffer))
/*     */         {
/* 223 */           j = ((Framebuffer)object).framebufferTexture;
/*     */         }
/* 225 */         else if ((object instanceof ITextureObject))
/*     */         {
/* 227 */           j = ((ITextureObject)object).getGlTextureId();
/*     */         }
/* 229 */         else if ((object instanceof Integer))
/*     */         {
/* 231 */           j = ((Integer)object).intValue();
/*     */         }
/*     */         
/* 234 */         if (j != -1)
/*     */         {
/* 236 */           GlStateManager.bindTexture(j);
/* 237 */           OpenGlHelper.glUniform1i(OpenGlHelper.glGetUniformLocation(this.program, (CharSequence)this.samplerNames.get(i)), i);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 242 */     for (Object object = this.shaderUniforms.iterator(); ((Iterator)object).hasNext();) { ShaderUniform shaderuniform = (ShaderUniform)((Iterator)object).next();
/*     */       
/* 244 */       shaderuniform.upload();
/*     */     }
/*     */   }
/*     */   
/*     */   public void markDirty()
/*     */   {
/* 250 */     this.isDirty = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ShaderUniform getShaderUniform(String p_147991_1_)
/*     */   {
/* 258 */     return this.mappedShaderUniforms.containsKey(p_147991_1_) ? (ShaderUniform)this.mappedShaderUniforms.get(p_147991_1_) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ShaderUniform getShaderUniformOrDefault(String p_147984_1_)
/*     */   {
/* 266 */     return this.mappedShaderUniforms.containsKey(p_147984_1_) ? (ShaderUniform)this.mappedShaderUniforms.get(p_147984_1_) : defaultShaderUniform;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setupUniforms()
/*     */   {
/* 274 */     int i = 0;
/*     */     String s;
/* 276 */     for (int j = 0; i < this.samplerNames.size(); j++)
/*     */     {
/* 278 */       s = (String)this.samplerNames.get(i);
/* 279 */       int k = OpenGlHelper.glGetUniformLocation(this.program, s);
/*     */       
/* 281 */       if (k == -1)
/*     */       {
/* 283 */         logger.warn("Shader " + this.programFilename + "could not find sampler named " + s + " in the specified shader program.");
/* 284 */         this.shaderSamplers.remove(s);
/* 285 */         this.samplerNames.remove(j);
/* 286 */         j--;
/*     */       }
/*     */       else
/*     */       {
/* 290 */         this.shaderSamplerLocations.add(Integer.valueOf(k));
/*     */       }
/*     */       
/* 293 */       i++;
/*     */     }
/*     */     
/* 296 */     for (ShaderUniform shaderuniform : this.shaderUniforms)
/*     */     {
/* 298 */       String s1 = shaderuniform.getShaderName();
/* 299 */       int l = OpenGlHelper.glGetUniformLocation(this.program, s1);
/*     */       
/* 301 */       if (l == -1)
/*     */       {
/* 303 */         logger.warn("Could not find uniform named " + s1 + " in the specified" + " shader program.");
/*     */       }
/*     */       else
/*     */       {
/* 307 */         this.shaderUniformLocations.add(Integer.valueOf(l));
/* 308 */         shaderuniform.setUniformLocation(l);
/* 309 */         this.mappedShaderUniforms.put(s1, shaderuniform);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void parseSampler(JsonElement p_147996_1_) throws JsonException
/*     */   {
/* 316 */     JsonObject jsonobject = JsonUtils.getJsonObject(p_147996_1_, "sampler");
/* 317 */     String s = JsonUtils.getString(jsonobject, "name");
/*     */     
/* 319 */     if (!JsonUtils.isString(jsonobject, "file"))
/*     */     {
/* 321 */       this.shaderSamplers.put(s, null);
/* 322 */       this.samplerNames.add(s);
/*     */     }
/*     */     else
/*     */     {
/* 326 */       this.samplerNames.add(s);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSamplerTexture(String p_147992_1_, Object p_147992_2_)
/*     */   {
/* 335 */     if (this.shaderSamplers.containsKey(p_147992_1_))
/*     */     {
/* 337 */       this.shaderSamplers.remove(p_147992_1_);
/*     */     }
/*     */     
/* 340 */     this.shaderSamplers.put(p_147992_1_, p_147992_2_);
/* 341 */     markDirty();
/*     */   }
/*     */   
/*     */   private void parseUniform(JsonElement p_147987_1_) throws JsonException
/*     */   {
/* 346 */     JsonObject jsonobject = JsonUtils.getJsonObject(p_147987_1_, "uniform");
/* 347 */     String s = JsonUtils.getString(jsonobject, "name");
/* 348 */     int i = ShaderUniform.parseType(JsonUtils.getString(jsonobject, "type"));
/* 349 */     int j = JsonUtils.getInt(jsonobject, "count");
/* 350 */     float[] afloat = new float[Math.max(j, 16)];
/* 351 */     JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "values");
/*     */     
/* 353 */     if ((jsonarray.size() != j) && (jsonarray.size() > 1))
/*     */     {
/* 355 */       throw new JsonException("Invalid amount of values specified (expected " + j + ", found " + jsonarray.size() + ")");
/*     */     }
/*     */     
/*     */ 
/* 359 */     int k = 0;
/*     */     
/* 361 */     for (JsonElement jsonelement : jsonarray)
/*     */     {
/*     */       try
/*     */       {
/* 365 */         afloat[k] = JsonUtils.getFloat(jsonelement, "value");
/*     */       }
/*     */       catch (Exception exception)
/*     */       {
/* 369 */         JsonException jsonexception = JsonException.func_151379_a(exception);
/* 370 */         jsonexception.func_151380_a("values[" + k + "]");
/* 371 */         throw jsonexception;
/*     */       }
/*     */       
/* 374 */       k++;
/*     */     }
/*     */     
/* 377 */     if ((j > 1) && (jsonarray.size() == 1))
/*     */     {
/* 379 */       while (k < j)
/*     */       {
/* 381 */         afloat[k] = afloat[0];
/* 382 */         k++;
/*     */       }
/*     */     }
/*     */     
/* 386 */     int l = (j > 1) && (j <= 4) && (i < 8) ? j - 1 : 0;
/* 387 */     ShaderUniform shaderuniform = new ShaderUniform(s, i + l, j, this);
/*     */     
/* 389 */     if (i <= 3)
/*     */     {
/* 391 */       shaderuniform.set((int)afloat[0], (int)afloat[1], (int)afloat[2], (int)afloat[3]);
/*     */     }
/* 393 */     else if (i <= 7)
/*     */     {
/* 395 */       shaderuniform.func_148092_b(afloat[0], afloat[1], afloat[2], afloat[3]);
/*     */     }
/*     */     else
/*     */     {
/* 399 */       shaderuniform.set(afloat);
/*     */     }
/*     */     
/* 402 */     this.shaderUniforms.add(shaderuniform);
/*     */   }
/*     */   
/*     */ 
/*     */   public ShaderLoader getVertexShaderLoader()
/*     */   {
/* 408 */     return this.vertexShaderLoader;
/*     */   }
/*     */   
/*     */   public ShaderLoader getFragmentShaderLoader()
/*     */   {
/* 413 */     return this.fragmentShaderLoader;
/*     */   }
/*     */   
/*     */   public int getProgram()
/*     */   {
/* 418 */     return this.program;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\shader\ShaderManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */