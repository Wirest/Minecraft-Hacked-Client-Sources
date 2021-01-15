/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.URI;
/*    */ import net.minecraft.client.renderer.texture.TextureUtil;
/*    */ import net.minecraft.client.resources.data.IMetadataSection;
/*    */ import net.minecraft.client.resources.data.IMetadataSerializer;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractResourcePack
/*    */   implements IResourcePack
/*    */ {
/* 24 */   private static final Logger resourceLog = ;
/*    */   public final File resourcePackFile;
/*    */   
/*    */   public AbstractResourcePack(File resourcePackFileIn)
/*    */   {
/* 29 */     this.resourcePackFile = resourcePackFileIn;
/*    */   }
/*    */   
/*    */   private static String locationToName(ResourceLocation location)
/*    */   {
/* 34 */     return String.format("%s/%s/%s", new Object[] { "assets", location.getResourceDomain(), location.getResourcePath() });
/*    */   }
/*    */   
/*    */   protected static String getRelativeName(File p_110595_0_, File p_110595_1_)
/*    */   {
/* 39 */     return p_110595_0_.toURI().relativize(p_110595_1_.toURI()).getPath();
/*    */   }
/*    */   
/*    */   public InputStream getInputStream(ResourceLocation location) throws IOException
/*    */   {
/* 44 */     return getInputStreamByName(locationToName(location));
/*    */   }
/*    */   
/*    */   public boolean resourceExists(ResourceLocation location)
/*    */   {
/* 49 */     return hasResourceName(locationToName(location));
/*    */   }
/*    */   
/*    */   protected abstract InputStream getInputStreamByName(String paramString) throws IOException;
/*    */   
/*    */   protected abstract boolean hasResourceName(String paramString);
/*    */   
/*    */   protected void logNameNotLowercase(String p_110594_1_)
/*    */   {
/* 58 */     resourceLog.warn("ResourcePack: ignored non-lowercase namespace: %s in %s", new Object[] { p_110594_1_, this.resourcePackFile });
/*    */   }
/*    */   
/*    */   public <T extends IMetadataSection> T getPackMetadata(IMetadataSerializer p_135058_1_, String p_135058_2_) throws IOException
/*    */   {
/* 63 */     return readMetadata(p_135058_1_, getInputStreamByName("pack.mcmeta"), p_135058_2_);
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   static <T extends IMetadataSection> T readMetadata(IMetadataSerializer p_110596_0_, InputStream p_110596_1_, String p_110596_2_)
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aconst_null
/*    */     //   1: astore_3
/*    */     //   2: aconst_null
/*    */     //   3: astore 4
/*    */     //   5: new 115	java/io/BufferedReader
/*    */     //   8: dup
/*    */     //   9: new 117	java/io/InputStreamReader
/*    */     //   12: dup
/*    */     //   13: aload_1
/*    */     //   14: getstatic 123	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
/*    */     //   17: invokespecial 126	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/nio/charset/Charset;)V
/*    */     //   20: invokespecial 129	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
/*    */     //   23: astore 4
/*    */     //   25: new 131	com/google/gson/JsonParser
/*    */     //   28: dup
/*    */     //   29: invokespecial 132	com/google/gson/JsonParser:<init>	()V
/*    */     //   32: aload 4
/*    */     //   34: invokevirtual 136	com/google/gson/JsonParser:parse	(Ljava/io/Reader;)Lcom/google/gson/JsonElement;
/*    */     //   37: invokevirtual 142	com/google/gson/JsonElement:getAsJsonObject	()Lcom/google/gson/JsonObject;
/*    */     //   40: astore_3
/*    */     //   41: goto +25 -> 66
/*    */     //   44: astore 5
/*    */     //   46: new 150	com/google/gson/JsonParseException
/*    */     //   49: dup
/*    */     //   50: aload 5
/*    */     //   52: invokespecial 153	com/google/gson/JsonParseException:<init>	(Ljava/lang/Throwable;)V
/*    */     //   55: athrow
/*    */     //   56: astore 6
/*    */     //   58: aload 4
/*    */     //   60: invokestatic 160	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
/*    */     //   63: aload 6
/*    */     //   65: athrow
/*    */     //   66: aload 4
/*    */     //   68: invokestatic 160	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
/*    */     //   71: aload_0
/*    */     //   72: aload_2
/*    */     //   73: aload_3
/*    */     //   74: invokevirtual 164	net/minecraft/client/resources/data/IMetadataSerializer:parseMetadataSection	(Ljava/lang/String;Lcom/google/gson/JsonObject;)Lnet/minecraft/client/resources/data/IMetadataSection;
/*    */     //   77: areturn
/*    */     // Line number table:
/*    */     //   Java source line #68	-> byte code offset #0
/*    */     //   Java source line #69	-> byte code offset #2
/*    */     //   Java source line #73	-> byte code offset #5
/*    */     //   Java source line #74	-> byte code offset #25
/*    */     //   Java source line #75	-> byte code offset #41
/*    */     //   Java source line #76	-> byte code offset #44
/*    */     //   Java source line #78	-> byte code offset #46
/*    */     //   Java source line #81	-> byte code offset #56
/*    */     //   Java source line #82	-> byte code offset #58
/*    */     //   Java source line #83	-> byte code offset #63
/*    */     //   Java source line #82	-> byte code offset #66
/*    */     //   Java source line #85	-> byte code offset #71
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	78	0	p_110596_0_	IMetadataSerializer
/*    */     //   0	78	1	p_110596_1_	InputStream
/*    */     //   0	78	2	p_110596_2_	String
/*    */     //   1	73	3	jsonobject	com.google.gson.JsonObject
/*    */     //   3	64	4	bufferedreader	java.io.BufferedReader
/*    */     //   44	7	5	runtimeexception	RuntimeException
/*    */     //   56	8	6	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   5	41	44	java/lang/RuntimeException
/*    */     //   5	56	56	finally
/*    */   }
/*    */   
/*    */   public BufferedImage getPackImage()
/*    */     throws IOException
/*    */   {
/* 90 */     return TextureUtil.readBufferedImage(getInputStreamByName("pack.png"));
/*    */   }
/*    */   
/*    */   public String getPackName()
/*    */   {
/* 95 */     return this.resourcePackFile.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\AbstractResourcePack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */