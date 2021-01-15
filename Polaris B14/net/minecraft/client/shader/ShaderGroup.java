/*     */ package net.minecraft.client.shader;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.renderer.texture.ITextureObject;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.client.util.JsonException;
/*     */ import net.minecraft.util.JsonUtils;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.util.vector.Matrix4f;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShaderGroup
/*     */ {
/*     */   private Framebuffer mainFramebuffer;
/*     */   private IResourceManager resourceManager;
/*     */   private String shaderGroupName;
/*  32 */   private final List<Shader> listShaders = Lists.newArrayList();
/*  33 */   private final Map<String, Framebuffer> mapFramebuffers = Maps.newHashMap();
/*  34 */   private final List<Framebuffer> listFramebuffers = Lists.newArrayList();
/*     */   private Matrix4f projectionMatrix;
/*     */   private int mainFramebufferWidth;
/*     */   private int mainFramebufferHeight;
/*     */   private float field_148036_j;
/*     */   private float field_148037_k;
/*     */   
/*     */   public ShaderGroup(TextureManager p_i1050_1_, IResourceManager p_i1050_2_, Framebuffer p_i1050_3_, ResourceLocation p_i1050_4_) throws JsonException, IOException, JsonSyntaxException
/*     */   {
/*  43 */     this.resourceManager = p_i1050_2_;
/*  44 */     this.mainFramebuffer = p_i1050_3_;
/*  45 */     this.field_148036_j = 0.0F;
/*  46 */     this.field_148037_k = 0.0F;
/*  47 */     this.mainFramebufferWidth = p_i1050_3_.framebufferWidth;
/*  48 */     this.mainFramebufferHeight = p_i1050_3_.framebufferHeight;
/*  49 */     this.shaderGroupName = p_i1050_4_.toString();
/*  50 */     resetProjectionMatrix();
/*  51 */     parseGroup(p_i1050_1_, p_i1050_4_);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void parseGroup(TextureManager p_152765_1_, ResourceLocation p_152765_2_)
/*     */     throws JsonException, IOException, JsonSyntaxException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 103	com/google/gson/JsonParser
/*     */     //   3: dup
/*     */     //   4: invokespecial 104	com/google/gson/JsonParser:<init>	()V
/*     */     //   7: astore_3
/*     */     //   8: aconst_null
/*     */     //   9: astore 4
/*     */     //   11: aload_0
/*     */     //   12: getfield 58	net/minecraft/client/shader/ShaderGroup:resourceManager	Lnet/minecraft/client/resources/IResourceManager;
/*     */     //   15: aload_2
/*     */     //   16: invokeinterface 110 2 0
/*     */     //   21: astore 5
/*     */     //   23: aload 5
/*     */     //   25: invokeinterface 116 1 0
/*     */     //   30: astore 4
/*     */     //   32: aload_3
/*     */     //   33: aload 4
/*     */     //   35: getstatic 122	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
/*     */     //   38: invokestatic 127	org/apache/commons/io/IOUtils:toString	(Ljava/io/InputStream;Ljava/nio/charset/Charset;)Ljava/lang/String;
/*     */     //   41: invokevirtual 131	com/google/gson/JsonParser:parse	(Ljava/lang/String;)Lcom/google/gson/JsonElement;
/*     */     //   44: invokevirtual 137	com/google/gson/JsonElement:getAsJsonObject	()Lcom/google/gson/JsonObject;
/*     */     //   47: astore 6
/*     */     //   49: aload 6
/*     */     //   51: ldc -117
/*     */     //   53: invokestatic 145	net/minecraft/util/JsonUtils:isJsonArray	(Lcom/google/gson/JsonObject;Ljava/lang/String;)Z
/*     */     //   56: ifeq +98 -> 154
/*     */     //   59: aload 6
/*     */     //   61: ldc -117
/*     */     //   63: invokevirtual 151	com/google/gson/JsonObject:getAsJsonArray	(Ljava/lang/String;)Lcom/google/gson/JsonArray;
/*     */     //   66: astore 7
/*     */     //   68: iconst_0
/*     */     //   69: istore 8
/*     */     //   71: aload 7
/*     */     //   73: invokevirtual 157	com/google/gson/JsonArray:iterator	()Ljava/util/Iterator;
/*     */     //   76: astore 10
/*     */     //   78: goto +66 -> 144
/*     */     //   81: aload 10
/*     */     //   83: invokeinterface 167 1 0
/*     */     //   88: checkcast 133	com/google/gson/JsonElement
/*     */     //   91: astore 9
/*     */     //   93: aload_0
/*     */     //   94: aload 9
/*     */     //   96: invokespecial 171	net/minecraft/client/shader/ShaderGroup:initTarget	(Lcom/google/gson/JsonElement;)V
/*     */     //   99: goto +42 -> 141
/*     */     //   102: astore 11
/*     */     //   104: aload 11
/*     */     //   106: invokestatic 175	net/minecraft/client/util/JsonException:func_151379_a	(Ljava/lang/Exception;)Lnet/minecraft/client/util/JsonException;
/*     */     //   109: astore 12
/*     */     //   111: aload 12
/*     */     //   113: new 177	java/lang/StringBuilder
/*     */     //   116: dup
/*     */     //   117: ldc -77
/*     */     //   119: invokespecial 182	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   122: iload 8
/*     */     //   124: invokevirtual 186	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */     //   127: ldc -68
/*     */     //   129: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   132: invokevirtual 192	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   135: invokevirtual 195	net/minecraft/client/util/JsonException:func_151380_a	(Ljava/lang/String;)V
/*     */     //   138: aload 12
/*     */     //   140: athrow
/*     */     //   141: iinc 8 1
/*     */     //   144: aload 10
/*     */     //   146: invokeinterface 199 1 0
/*     */     //   151: ifne -70 -> 81
/*     */     //   154: aload 6
/*     */     //   156: ldc -55
/*     */     //   158: invokestatic 145	net/minecraft/util/JsonUtils:isJsonArray	(Lcom/google/gson/JsonObject;Ljava/lang/String;)Z
/*     */     //   161: ifeq +133 -> 294
/*     */     //   164: aload 6
/*     */     //   166: ldc -55
/*     */     //   168: invokevirtual 151	com/google/gson/JsonObject:getAsJsonArray	(Ljava/lang/String;)Lcom/google/gson/JsonArray;
/*     */     //   171: astore 7
/*     */     //   173: iconst_0
/*     */     //   174: istore 8
/*     */     //   176: aload 7
/*     */     //   178: invokevirtual 157	com/google/gson/JsonArray:iterator	()Ljava/util/Iterator;
/*     */     //   181: astore 10
/*     */     //   183: goto +67 -> 250
/*     */     //   186: aload 10
/*     */     //   188: invokeinterface 167 1 0
/*     */     //   193: checkcast 133	com/google/gson/JsonElement
/*     */     //   196: astore 9
/*     */     //   198: aload_0
/*     */     //   199: aload_1
/*     */     //   200: aload 9
/*     */     //   202: invokespecial 205	net/minecraft/client/shader/ShaderGroup:parsePass	(Lnet/minecraft/client/renderer/texture/TextureManager;Lcom/google/gson/JsonElement;)V
/*     */     //   205: goto +42 -> 247
/*     */     //   208: astore 11
/*     */     //   210: aload 11
/*     */     //   212: invokestatic 175	net/minecraft/client/util/JsonException:func_151379_a	(Ljava/lang/Exception;)Lnet/minecraft/client/util/JsonException;
/*     */     //   215: astore 12
/*     */     //   217: aload 12
/*     */     //   219: new 177	java/lang/StringBuilder
/*     */     //   222: dup
/*     */     //   223: ldc -49
/*     */     //   225: invokespecial 182	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   228: iload 8
/*     */     //   230: invokevirtual 186	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */     //   233: ldc -68
/*     */     //   235: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   238: invokevirtual 192	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   241: invokevirtual 195	net/minecraft/client/util/JsonException:func_151380_a	(Ljava/lang/String;)V
/*     */     //   244: aload 12
/*     */     //   246: athrow
/*     */     //   247: iinc 8 1
/*     */     //   250: aload 10
/*     */     //   252: invokeinterface 199 1 0
/*     */     //   257: ifne -71 -> 186
/*     */     //   260: goto +34 -> 294
/*     */     //   263: astore 5
/*     */     //   265: aload 5
/*     */     //   267: invokestatic 175	net/minecraft/client/util/JsonException:func_151379_a	(Ljava/lang/Exception;)Lnet/minecraft/client/util/JsonException;
/*     */     //   270: astore 6
/*     */     //   272: aload 6
/*     */     //   274: aload_2
/*     */     //   275: invokevirtual 210	net/minecraft/util/ResourceLocation:getResourcePath	()Ljava/lang/String;
/*     */     //   278: invokevirtual 213	net/minecraft/client/util/JsonException:func_151381_b	(Ljava/lang/String;)V
/*     */     //   281: aload 6
/*     */     //   283: athrow
/*     */     //   284: astore 13
/*     */     //   286: aload 4
/*     */     //   288: invokestatic 219	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*     */     //   291: aload 13
/*     */     //   293: athrow
/*     */     //   294: aload 4
/*     */     //   296: invokestatic 219	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*     */     //   299: return
/*     */     // Line number table:
/*     */     //   Java source line #56	-> byte code offset #0
/*     */     //   Java source line #57	-> byte code offset #8
/*     */     //   Java source line #61	-> byte code offset #11
/*     */     //   Java source line #62	-> byte code offset #23
/*     */     //   Java source line #63	-> byte code offset #32
/*     */     //   Java source line #65	-> byte code offset #49
/*     */     //   Java source line #67	-> byte code offset #59
/*     */     //   Java source line #68	-> byte code offset #68
/*     */     //   Java source line #70	-> byte code offset #71
/*     */     //   Java source line #74	-> byte code offset #93
/*     */     //   Java source line #75	-> byte code offset #99
/*     */     //   Java source line #76	-> byte code offset #102
/*     */     //   Java source line #78	-> byte code offset #104
/*     */     //   Java source line #79	-> byte code offset #111
/*     */     //   Java source line #80	-> byte code offset #138
/*     */     //   Java source line #83	-> byte code offset #141
/*     */     //   Java source line #70	-> byte code offset #144
/*     */     //   Java source line #87	-> byte code offset #154
/*     */     //   Java source line #89	-> byte code offset #164
/*     */     //   Java source line #90	-> byte code offset #173
/*     */     //   Java source line #92	-> byte code offset #176
/*     */     //   Java source line #96	-> byte code offset #198
/*     */     //   Java source line #97	-> byte code offset #205
/*     */     //   Java source line #98	-> byte code offset #208
/*     */     //   Java source line #100	-> byte code offset #210
/*     */     //   Java source line #101	-> byte code offset #217
/*     */     //   Java source line #102	-> byte code offset #244
/*     */     //   Java source line #105	-> byte code offset #247
/*     */     //   Java source line #92	-> byte code offset #250
/*     */     //   Java source line #108	-> byte code offset #260
/*     */     //   Java source line #109	-> byte code offset #263
/*     */     //   Java source line #111	-> byte code offset #265
/*     */     //   Java source line #112	-> byte code offset #272
/*     */     //   Java source line #113	-> byte code offset #281
/*     */     //   Java source line #116	-> byte code offset #284
/*     */     //   Java source line #117	-> byte code offset #286
/*     */     //   Java source line #118	-> byte code offset #291
/*     */     //   Java source line #117	-> byte code offset #294
/*     */     //   Java source line #119	-> byte code offset #299
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	300	0	this	ShaderGroup
/*     */     //   0	300	1	p_152765_1_	TextureManager
/*     */     //   0	300	2	p_152765_2_	ResourceLocation
/*     */     //   7	26	3	jsonparser	com.google.gson.JsonParser
/*     */     //   9	286	4	inputstream	java.io.InputStream
/*     */     //   21	3	5	iresource	net.minecraft.client.resources.IResource
/*     */     //   263	3	5	exception2	Exception
/*     */     //   47	118	6	jsonobject	JsonObject
/*     */     //   270	12	6	jsonexception	JsonException
/*     */     //   66	6	7	jsonarray	JsonArray
/*     */     //   171	6	7	jsonarray1	JsonArray
/*     */     //   69	73	8	i	int
/*     */     //   174	74	8	j	int
/*     */     //   91	4	9	jsonelement	JsonElement
/*     */     //   196	5	9	jsonelement1	JsonElement
/*     */     //   76	175	10	localIterator	java.util.Iterator
/*     */     //   102	3	11	exception1	Exception
/*     */     //   208	3	11	exception	Exception
/*     */     //   109	30	12	jsonexception1	JsonException
/*     */     //   215	30	12	jsonexception2	JsonException
/*     */     //   284	8	13	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   93	99	102	java/lang/Exception
/*     */     //   198	205	208	java/lang/Exception
/*     */     //   11	260	263	java/lang/Exception
/*     */     //   11	284	284	finally
/*     */   }
/*     */   
/*     */   private void initTarget(JsonElement p_148027_1_)
/*     */     throws JsonException
/*     */   {
/* 123 */     if (JsonUtils.isString(p_148027_1_))
/*     */     {
/* 125 */       addFramebuffer(p_148027_1_.getAsString(), this.mainFramebufferWidth, this.mainFramebufferHeight);
/*     */     }
/*     */     else
/*     */     {
/* 129 */       JsonObject jsonobject = JsonUtils.getJsonObject(p_148027_1_, "target");
/* 130 */       String s = JsonUtils.getString(jsonobject, "name");
/* 131 */       int i = JsonUtils.getInt(jsonobject, "width", this.mainFramebufferWidth);
/* 132 */       int j = JsonUtils.getInt(jsonobject, "height", this.mainFramebufferHeight);
/*     */       
/* 134 */       if (this.mapFramebuffers.containsKey(s))
/*     */       {
/* 136 */         throw new JsonException(s + " is already defined");
/*     */       }
/*     */       
/* 139 */       addFramebuffer(s, i, j);
/*     */     }
/*     */   }
/*     */   
/*     */   private void parsePass(TextureManager p_152764_1_, JsonElement p_152764_2_) throws JsonException, IOException
/*     */   {
/* 145 */     JsonObject jsonobject = JsonUtils.getJsonObject(p_152764_2_, "pass");
/* 146 */     String s = JsonUtils.getString(jsonobject, "name");
/* 147 */     String s1 = JsonUtils.getString(jsonobject, "intarget");
/* 148 */     String s2 = JsonUtils.getString(jsonobject, "outtarget");
/* 149 */     Framebuffer framebuffer = getFramebuffer(s1);
/* 150 */     Framebuffer framebuffer1 = getFramebuffer(s2);
/*     */     
/* 152 */     if (framebuffer == null)
/*     */     {
/* 154 */       throw new JsonException("Input target '" + s1 + "' does not exist");
/*     */     }
/* 156 */     if (framebuffer1 == null)
/*     */     {
/* 158 */       throw new JsonException("Output target '" + s2 + "' does not exist");
/*     */     }
/*     */     
/*     */ 
/* 162 */     Shader shader = addShader(s, framebuffer, framebuffer1);
/* 163 */     JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "auxtargets", null);
/*     */     
/* 165 */     if (jsonarray != null)
/*     */     {
/* 167 */       int i = 0;
/*     */       
/* 169 */       for (JsonElement jsonelement : jsonarray)
/*     */       {
/*     */         try
/*     */         {
/* 173 */           JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonelement, "auxtarget");
/* 174 */           String s4 = JsonUtils.getString(jsonobject1, "name");
/* 175 */           String s3 = JsonUtils.getString(jsonobject1, "id");
/* 176 */           Framebuffer framebuffer2 = getFramebuffer(s3);
/*     */           
/* 178 */           if (framebuffer2 == null)
/*     */           {
/* 180 */             ResourceLocation resourcelocation = new ResourceLocation("textures/effect/" + s3 + ".png");
/*     */             
/*     */             try
/*     */             {
/* 184 */               this.resourceManager.getResource(resourcelocation);
/*     */             }
/*     */             catch (FileNotFoundException var24)
/*     */             {
/* 188 */               throw new JsonException("Render target or texture '" + s3 + "' does not exist");
/*     */             }
/*     */             
/* 191 */             p_152764_1_.bindTexture(resourcelocation);
/* 192 */             ITextureObject itextureobject = p_152764_1_.getTexture(resourcelocation);
/* 193 */             int j = JsonUtils.getInt(jsonobject1, "width");
/* 194 */             int k = JsonUtils.getInt(jsonobject1, "height");
/* 195 */             boolean flag = JsonUtils.getBoolean(jsonobject1, "bilinear");
/*     */             
/* 197 */             if (flag)
/*     */             {
/* 199 */               GL11.glTexParameteri(3553, 10241, 9729);
/* 200 */               GL11.glTexParameteri(3553, 10240, 9729);
/*     */             }
/*     */             else
/*     */             {
/* 204 */               GL11.glTexParameteri(3553, 10241, 9728);
/* 205 */               GL11.glTexParameteri(3553, 10240, 9728);
/*     */             }
/*     */             
/* 208 */             shader.addAuxFramebuffer(s4, Integer.valueOf(itextureobject.getGlTextureId()), j, k);
/*     */           }
/*     */           else
/*     */           {
/* 212 */             shader.addAuxFramebuffer(s4, framebuffer2, framebuffer2.framebufferTextureWidth, framebuffer2.framebufferTextureHeight);
/*     */           }
/*     */         }
/*     */         catch (Exception exception1)
/*     */         {
/* 217 */           JsonException jsonexception = JsonException.func_151379_a(exception1);
/* 218 */           jsonexception.func_151380_a("auxtargets[" + i + "]");
/* 219 */           throw jsonexception;
/*     */         }
/*     */         
/* 222 */         i++;
/*     */       }
/*     */     }
/*     */     
/* 226 */     JsonArray jsonarray1 = JsonUtils.getJsonArray(jsonobject, "uniforms", null);
/*     */     
/* 228 */     if (jsonarray1 != null)
/*     */     {
/* 230 */       int l = 0;
/*     */       
/* 232 */       for (JsonElement jsonelement1 : jsonarray1)
/*     */       {
/*     */         try
/*     */         {
/* 236 */           initUniform(jsonelement1);
/*     */         }
/*     */         catch (Exception exception)
/*     */         {
/* 240 */           JsonException jsonexception1 = JsonException.func_151379_a(exception);
/* 241 */           jsonexception1.func_151380_a("uniforms[" + l + "]");
/* 242 */           throw jsonexception1;
/*     */         }
/*     */         
/* 245 */         l++;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void initUniform(JsonElement p_148028_1_)
/*     */     throws JsonException
/*     */   {
/* 253 */     JsonObject jsonobject = JsonUtils.getJsonObject(p_148028_1_, "uniform");
/* 254 */     String s = JsonUtils.getString(jsonobject, "name");
/* 255 */     ShaderUniform shaderuniform = ((Shader)this.listShaders.get(this.listShaders.size() - 1)).getShaderManager().getShaderUniform(s);
/*     */     
/* 257 */     if (shaderuniform == null)
/*     */     {
/* 259 */       throw new JsonException("Uniform '" + s + "' does not exist");
/*     */     }
/*     */     
/*     */ 
/* 263 */     float[] afloat = new float[4];
/* 264 */     int i = 0;
/*     */     
/* 266 */     for (JsonElement jsonelement : JsonUtils.getJsonArray(jsonobject, "values"))
/*     */     {
/*     */       try
/*     */       {
/* 270 */         afloat[i] = JsonUtils.getFloat(jsonelement, "value");
/*     */       }
/*     */       catch (Exception exception)
/*     */       {
/* 274 */         JsonException jsonexception = JsonException.func_151379_a(exception);
/* 275 */         jsonexception.func_151380_a("values[" + i + "]");
/* 276 */         throw jsonexception;
/*     */       }
/*     */       
/* 279 */       i++;
/*     */     }
/*     */     
/* 282 */     switch (i)
/*     */     {
/*     */     case 0: 
/*     */     default: 
/*     */       break;
/*     */     
/*     */     case 1: 
/* 289 */       shaderuniform.set(afloat[0]);
/* 290 */       break;
/*     */     
/*     */     case 2: 
/* 293 */       shaderuniform.set(afloat[0], afloat[1]);
/* 294 */       break;
/*     */     
/*     */     case 3: 
/* 297 */       shaderuniform.set(afloat[0], afloat[1], afloat[2]);
/* 298 */       break;
/*     */     
/*     */     case 4: 
/* 301 */       shaderuniform.set(afloat[0], afloat[1], afloat[2], afloat[3]);
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */   public Framebuffer getFramebufferRaw(String p_177066_1_)
/*     */   {
/* 308 */     return (Framebuffer)this.mapFramebuffers.get(p_177066_1_);
/*     */   }
/*     */   
/*     */   public void addFramebuffer(String p_148020_1_, int p_148020_2_, int p_148020_3_)
/*     */   {
/* 313 */     Framebuffer framebuffer = new Framebuffer(p_148020_2_, p_148020_3_, true);
/* 314 */     framebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
/* 315 */     this.mapFramebuffers.put(p_148020_1_, framebuffer);
/*     */     
/* 317 */     if ((p_148020_2_ == this.mainFramebufferWidth) && (p_148020_3_ == this.mainFramebufferHeight))
/*     */     {
/* 319 */       this.listFramebuffers.add(framebuffer);
/*     */     }
/*     */   }
/*     */   
/*     */   public void deleteShaderGroup()
/*     */   {
/* 325 */     for (Framebuffer framebuffer : this.mapFramebuffers.values())
/*     */     {
/* 327 */       framebuffer.deleteFramebuffer();
/*     */     }
/*     */     
/* 330 */     for (Shader shader : this.listShaders)
/*     */     {
/* 332 */       shader.deleteShader();
/*     */     }
/*     */     
/* 335 */     this.listShaders.clear();
/*     */   }
/*     */   
/*     */   public Shader addShader(String p_148023_1_, Framebuffer p_148023_2_, Framebuffer p_148023_3_) throws JsonException, IOException
/*     */   {
/* 340 */     Shader shader = new Shader(this.resourceManager, p_148023_1_, p_148023_2_, p_148023_3_);
/* 341 */     this.listShaders.add(this.listShaders.size(), shader);
/* 342 */     return shader;
/*     */   }
/*     */   
/*     */   private void resetProjectionMatrix()
/*     */   {
/* 347 */     this.projectionMatrix = new Matrix4f();
/* 348 */     this.projectionMatrix.setIdentity();
/* 349 */     this.projectionMatrix.m00 = (2.0F / this.mainFramebuffer.framebufferTextureWidth);
/* 350 */     this.projectionMatrix.m11 = (2.0F / -this.mainFramebuffer.framebufferTextureHeight);
/* 351 */     this.projectionMatrix.m22 = -0.0020001999F;
/* 352 */     this.projectionMatrix.m33 = 1.0F;
/* 353 */     this.projectionMatrix.m03 = -1.0F;
/* 354 */     this.projectionMatrix.m13 = 1.0F;
/* 355 */     this.projectionMatrix.m23 = -1.0001999F;
/*     */   }
/*     */   
/*     */   public void createBindFramebuffers(int width, int height)
/*     */   {
/* 360 */     this.mainFramebufferWidth = this.mainFramebuffer.framebufferTextureWidth;
/* 361 */     this.mainFramebufferHeight = this.mainFramebuffer.framebufferTextureHeight;
/* 362 */     resetProjectionMatrix();
/*     */     
/* 364 */     for (Shader shader : this.listShaders)
/*     */     {
/* 366 */       shader.setProjectionMatrix(this.projectionMatrix);
/*     */     }
/*     */     
/* 369 */     for (Framebuffer framebuffer : this.listFramebuffers)
/*     */     {
/* 371 */       framebuffer.createBindFramebuffer(width, height);
/*     */     }
/*     */   }
/*     */   
/*     */   public void loadShaderGroup(float partialTicks)
/*     */   {
/* 377 */     if (partialTicks < this.field_148037_k)
/*     */     {
/* 379 */       this.field_148036_j += 1.0F - this.field_148037_k;
/* 380 */       this.field_148036_j += partialTicks;
/*     */     }
/*     */     else
/*     */     {
/* 384 */       this.field_148036_j += partialTicks - this.field_148037_k;
/*     */     }
/*     */     
/* 387 */     for (this.field_148037_k = partialTicks; this.field_148036_j > 20.0F; this.field_148036_j -= 20.0F) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 392 */     for (Shader shader : this.listShaders)
/*     */     {
/* 394 */       shader.loadShader(this.field_148036_j / 20.0F);
/*     */     }
/*     */   }
/*     */   
/*     */   public final String getShaderGroupName()
/*     */   {
/* 400 */     return this.shaderGroupName;
/*     */   }
/*     */   
/*     */   private Framebuffer getFramebuffer(String p_148017_1_)
/*     */   {
/* 405 */     return p_148017_1_.equals("minecraft:main") ? this.mainFramebuffer : p_148017_1_ == null ? null : (Framebuffer)this.mapFramebuffers.get(p_148017_1_);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\shader\ShaderGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */