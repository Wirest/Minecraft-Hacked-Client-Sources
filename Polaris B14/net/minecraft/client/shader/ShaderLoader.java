/*     */ package net.minecraft.client.shader;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.resources.IResource;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.client.util.JsonException;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.lwjgl.BufferUtils;
/*     */ 
/*     */ 
/*     */ public class ShaderLoader
/*     */ {
/*     */   private final ShaderType shaderType;
/*     */   private final String shaderFilename;
/*     */   private int shader;
/*  22 */   private int shaderAttachCount = 0;
/*     */   
/*     */   private ShaderLoader(ShaderType type, int shaderId, String filename)
/*     */   {
/*  26 */     this.shaderType = type;
/*  27 */     this.shader = shaderId;
/*  28 */     this.shaderFilename = filename;
/*     */   }
/*     */   
/*     */   public void attachShader(ShaderManager manager)
/*     */   {
/*  33 */     this.shaderAttachCount += 1;
/*  34 */     OpenGlHelper.glAttachShader(manager.getProgram(), this.shader);
/*     */   }
/*     */   
/*     */   public void deleteShader(ShaderManager manager)
/*     */   {
/*  39 */     this.shaderAttachCount -= 1;
/*     */     
/*  41 */     if (this.shaderAttachCount <= 0)
/*     */     {
/*  43 */       OpenGlHelper.glDeleteShader(this.shader);
/*  44 */       this.shaderType.getLoadedShaders().remove(this.shaderFilename);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getShaderFilename()
/*     */   {
/*  50 */     return this.shaderFilename;
/*     */   }
/*     */   
/*     */   public static ShaderLoader loadShader(IResourceManager resourceManager, ShaderType type, String filename) throws IOException
/*     */   {
/*  55 */     ShaderLoader shaderloader = (ShaderLoader)type.getLoadedShaders().get(filename);
/*     */     
/*  57 */     if (shaderloader == null)
/*     */     {
/*  59 */       ResourceLocation resourcelocation = new ResourceLocation("shaders/program/" + filename + type.getShaderExtension());
/*  60 */       BufferedInputStream bufferedinputstream = new BufferedInputStream(resourceManager.getResource(resourcelocation).getInputStream());
/*  61 */       byte[] abyte = toByteArray(bufferedinputstream);
/*  62 */       ByteBuffer bytebuffer = BufferUtils.createByteBuffer(abyte.length);
/*  63 */       bytebuffer.put(abyte);
/*  64 */       bytebuffer.position(0);
/*  65 */       int i = OpenGlHelper.glCreateShader(type.getShaderMode());
/*  66 */       OpenGlHelper.glShaderSource(i, bytebuffer);
/*  67 */       OpenGlHelper.glCompileShader(i);
/*     */       
/*  69 */       if (OpenGlHelper.glGetShaderi(i, OpenGlHelper.GL_COMPILE_STATUS) == 0)
/*     */       {
/*  71 */         String s = StringUtils.trim(OpenGlHelper.glGetShaderInfoLog(i, 32768));
/*  72 */         JsonException jsonexception = new JsonException("Couldn't compile " + type.getShaderName() + " program: " + s);
/*  73 */         jsonexception.func_151381_b(resourcelocation.getResourcePath());
/*  74 */         throw jsonexception;
/*     */       }
/*     */       
/*  77 */       shaderloader = new ShaderLoader(type, i, filename);
/*  78 */       type.getLoadedShaders().put(filename, shaderloader);
/*     */     }
/*     */     
/*  81 */     return shaderloader;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected static byte[] toByteArray(BufferedInputStream p_177064_0_)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokestatic 206	org/apache/commons/io/IOUtils:toByteArray	(Ljava/io/InputStream;)[B
/*     */     //   4: astore_1
/*     */     //   5: goto +10 -> 15
/*     */     //   8: astore_2
/*     */     //   9: aload_0
/*     */     //   10: invokevirtual 211	java/io/BufferedInputStream:close	()V
/*     */     //   13: aload_2
/*     */     //   14: athrow
/*     */     //   15: aload_0
/*     */     //   16: invokevirtual 211	java/io/BufferedInputStream:close	()V
/*     */     //   19: aload_1
/*     */     //   20: areturn
/*     */     // Line number table:
/*     */     //   Java source line #90	-> byte code offset #0
/*     */     //   Java source line #91	-> byte code offset #5
/*     */     //   Java source line #93	-> byte code offset #8
/*     */     //   Java source line #94	-> byte code offset #9
/*     */     //   Java source line #95	-> byte code offset #13
/*     */     //   Java source line #94	-> byte code offset #15
/*     */     //   Java source line #97	-> byte code offset #19
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	21	0	p_177064_0_	BufferedInputStream
/*     */     //   4	2	1	abyte	byte[]
/*     */     //   15	5	1	abyte	byte[]
/*     */     //   8	6	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	8	8	finally
/*     */   }
/*     */   
/*     */   public static enum ShaderType
/*     */   {
/* 102 */     VERTEX("vertex", ".vsh", OpenGlHelper.GL_VERTEX_SHADER), 
/* 103 */     FRAGMENT("fragment", ".fsh", OpenGlHelper.GL_FRAGMENT_SHADER);
/*     */     
/*     */     private final String shaderName;
/*     */     private final String shaderExtension;
/*     */     private final int shaderMode;
/* 108 */     private final Map<String, ShaderLoader> loadedShaders = Maps.newHashMap();
/*     */     
/*     */     private ShaderType(String p_i45090_3_, String p_i45090_4_, int p_i45090_5_)
/*     */     {
/* 112 */       this.shaderName = p_i45090_3_;
/* 113 */       this.shaderExtension = p_i45090_4_;
/* 114 */       this.shaderMode = p_i45090_5_;
/*     */     }
/*     */     
/*     */     public String getShaderName()
/*     */     {
/* 119 */       return this.shaderName;
/*     */     }
/*     */     
/*     */     protected String getShaderExtension()
/*     */     {
/* 124 */       return this.shaderExtension;
/*     */     }
/*     */     
/*     */     protected int getShaderMode()
/*     */     {
/* 129 */       return this.shaderMode;
/*     */     }
/*     */     
/*     */     protected Map<String, ShaderLoader> getLoadedShaders()
/*     */     {
/* 134 */       return this.loadedShaders;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\shader\ShaderLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */