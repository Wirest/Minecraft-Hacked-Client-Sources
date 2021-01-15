/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleTexture
/*    */   extends AbstractTexture
/*    */ {
/* 15 */   private static final Logger logger = ;
/*    */   protected final ResourceLocation textureLocation;
/*    */   
/*    */   public SimpleTexture(ResourceLocation textureResourceLocation)
/*    */   {
/* 20 */     this.textureLocation = textureResourceLocation;
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public void loadTexture(net.minecraft.client.resources.IResourceManager resourceManager)
/*    */     throws java.io.IOException
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: invokevirtual 37	net/minecraft/client/renderer/texture/SimpleTexture:deleteGlTexture	()V
/*    */     //   4: aconst_null
/*    */     //   5: astore_2
/*    */     //   6: aload_1
/*    */     //   7: aload_0
/*    */     //   8: getfield 25	net/minecraft/client/renderer/texture/SimpleTexture:textureLocation	Lnet/minecraft/util/ResourceLocation;
/*    */     //   11: invokeinterface 43 2 0
/*    */     //   16: astore_3
/*    */     //   17: aload_3
/*    */     //   18: invokeinterface 49 1 0
/*    */     //   23: astore_2
/*    */     //   24: aload_2
/*    */     //   25: invokestatic 55	net/minecraft/client/renderer/texture/TextureUtil:readBufferedImage	(Ljava/io/InputStream;)Ljava/awt/image/BufferedImage;
/*    */     //   28: astore 4
/*    */     //   30: iconst_0
/*    */     //   31: istore 5
/*    */     //   33: iconst_0
/*    */     //   34: istore 6
/*    */     //   36: aload_3
/*    */     //   37: invokeinterface 59 1 0
/*    */     //   42: ifeq +69 -> 111
/*    */     //   45: aload_3
/*    */     //   46: ldc 61
/*    */     //   48: invokeinterface 65 2 0
/*    */     //   53: checkcast 67	net/minecraft/client/resources/data/TextureMetadataSection
/*    */     //   56: astore 7
/*    */     //   58: aload 7
/*    */     //   60: ifnull +51 -> 111
/*    */     //   63: aload 7
/*    */     //   65: invokevirtual 70	net/minecraft/client/resources/data/TextureMetadataSection:getTextureBlur	()Z
/*    */     //   68: istore 5
/*    */     //   70: aload 7
/*    */     //   72: invokevirtual 73	net/minecraft/client/resources/data/TextureMetadataSection:getTextureClamp	()Z
/*    */     //   75: istore 6
/*    */     //   77: goto +34 -> 111
/*    */     //   80: astore 7
/*    */     //   82: getstatic 19	net/minecraft/client/renderer/texture/SimpleTexture:logger	Lorg/apache/logging/log4j/Logger;
/*    */     //   85: new 79	java/lang/StringBuilder
/*    */     //   88: dup
/*    */     //   89: ldc 81
/*    */     //   91: invokespecial 84	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*    */     //   94: aload_0
/*    */     //   95: getfield 25	net/minecraft/client/renderer/texture/SimpleTexture:textureLocation	Lnet/minecraft/util/ResourceLocation;
/*    */     //   98: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*    */     //   101: invokevirtual 92	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*    */     //   104: aload 7
/*    */     //   106: invokeinterface 98 3 0
/*    */     //   111: aload_0
/*    */     //   112: invokevirtual 102	net/minecraft/client/renderer/texture/SimpleTexture:getGlTextureId	()I
/*    */     //   115: aload 4
/*    */     //   117: iload 5
/*    */     //   119: iload 6
/*    */     //   121: invokestatic 106	net/minecraft/client/renderer/texture/TextureUtil:uploadTextureImageAllocate	(ILjava/awt/image/BufferedImage;ZZ)I
/*    */     //   124: pop
/*    */     //   125: goto +16 -> 141
/*    */     //   128: astore 8
/*    */     //   130: aload_2
/*    */     //   131: ifnull +7 -> 138
/*    */     //   134: aload_2
/*    */     //   135: invokevirtual 111	java/io/InputStream:close	()V
/*    */     //   138: aload 8
/*    */     //   140: athrow
/*    */     //   141: aload_2
/*    */     //   142: ifnull +7 -> 149
/*    */     //   145: aload_2
/*    */     //   146: invokevirtual 111	java/io/InputStream:close	()V
/*    */     //   149: return
/*    */     // Line number table:
/*    */     //   Java source line #25	-> byte code offset #0
/*    */     //   Java source line #26	-> byte code offset #4
/*    */     //   Java source line #30	-> byte code offset #6
/*    */     //   Java source line #31	-> byte code offset #17
/*    */     //   Java source line #32	-> byte code offset #24
/*    */     //   Java source line #33	-> byte code offset #30
/*    */     //   Java source line #34	-> byte code offset #33
/*    */     //   Java source line #36	-> byte code offset #36
/*    */     //   Java source line #40	-> byte code offset #45
/*    */     //   Java source line #42	-> byte code offset #58
/*    */     //   Java source line #44	-> byte code offset #63
/*    */     //   Java source line #45	-> byte code offset #70
/*    */     //   Java source line #47	-> byte code offset #77
/*    */     //   Java source line #48	-> byte code offset #80
/*    */     //   Java source line #50	-> byte code offset #82
/*    */     //   Java source line #54	-> byte code offset #111
/*    */     //   Java source line #55	-> byte code offset #125
/*    */     //   Java source line #57	-> byte code offset #128
/*    */     //   Java source line #58	-> byte code offset #130
/*    */     //   Java source line #60	-> byte code offset #134
/*    */     //   Java source line #62	-> byte code offset #138
/*    */     //   Java source line #58	-> byte code offset #141
/*    */     //   Java source line #60	-> byte code offset #145
/*    */     //   Java source line #63	-> byte code offset #149
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	150	0	this	SimpleTexture
/*    */     //   0	150	1	resourceManager	net.minecraft.client.resources.IResourceManager
/*    */     //   5	141	2	inputstream	java.io.InputStream
/*    */     //   16	30	3	iresource	net.minecraft.client.resources.IResource
/*    */     //   28	88	4	bufferedimage	java.awt.image.BufferedImage
/*    */     //   31	87	5	flag	boolean
/*    */     //   34	86	6	flag1	boolean
/*    */     //   56	15	7	texturemetadatasection	net.minecraft.client.resources.data.TextureMetadataSection
/*    */     //   80	25	7	runtimeexception	RuntimeException
/*    */     //   128	11	8	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   45	77	80	java/lang/RuntimeException
/*    */     //   6	128	128	finally
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\texture\SimpleTexture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */