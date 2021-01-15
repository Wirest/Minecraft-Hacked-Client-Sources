/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.client.renderer.texture.TextureUtil;
/*    */ import net.minecraft.client.resources.data.IMetadataSection;
/*    */ import net.minecraft.client.resources.data.IMetadataSerializer;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class DefaultResourcePack implements IResourcePack
/*    */ {
/* 19 */   public static final Set<String> defaultResourceDomains = ImmutableSet.of("minecraft", "realms");
/*    */   private final Map<String, File> mapAssets;
/*    */   
/*    */   public DefaultResourcePack(Map<String, File> mapAssetsIn)
/*    */   {
/* 24 */     this.mapAssets = mapAssetsIn;
/*    */   }
/*    */   
/*    */   public InputStream getInputStream(ResourceLocation location) throws IOException
/*    */   {
/* 29 */     InputStream inputstream = getResourceStream(location);
/*    */     
/* 31 */     if (inputstream != null)
/*    */     {
/* 33 */       return inputstream;
/*    */     }
/*    */     
/*    */ 
/* 37 */     InputStream inputstream1 = getInputStreamAssets(location);
/*    */     
/* 39 */     if (inputstream1 != null)
/*    */     {
/* 41 */       return inputstream1;
/*    */     }
/*    */     
/*    */ 
/* 45 */     throw new FileNotFoundException(location.getResourcePath());
/*    */   }
/*    */   
/*    */ 
/*    */   public InputStream getInputStreamAssets(ResourceLocation location)
/*    */     throws IOException, FileNotFoundException
/*    */   {
/* 52 */     File file1 = (File)this.mapAssets.get(location.toString());
/* 53 */     return (file1 != null) && (file1.isFile()) ? new FileInputStream(file1) : null;
/*    */   }
/*    */   
/*    */   private InputStream getResourceStream(ResourceLocation location)
/*    */   {
/* 58 */     return DefaultResourcePack.class.getResourceAsStream("/assets/" + location.getResourceDomain() + "/" + location.getResourcePath());
/*    */   }
/*    */   
/*    */   public boolean resourceExists(ResourceLocation location)
/*    */   {
/* 63 */     return (getResourceStream(location) != null) || (this.mapAssets.containsKey(location.toString()));
/*    */   }
/*    */   
/*    */   public Set<String> getResourceDomains()
/*    */   {
/* 68 */     return defaultResourceDomains;
/*    */   }
/*    */   
/*    */   public <T extends IMetadataSection> T getPackMetadata(IMetadataSerializer p_135058_1_, String p_135058_2_) throws IOException
/*    */   {
/*    */     try
/*    */     {
/* 75 */       InputStream inputstream = new FileInputStream((File)this.mapAssets.get("pack.mcmeta"));
/* 76 */       return AbstractResourcePack.readMetadata(p_135058_1_, inputstream, p_135058_2_);
/*    */     }
/*    */     catch (RuntimeException var4)
/*    */     {
/* 80 */       return null;
/*    */     }
/*    */     catch (FileNotFoundException var5) {}
/*    */     
/* 84 */     return null;
/*    */   }
/*    */   
/*    */   public BufferedImage getPackImage()
/*    */     throws IOException
/*    */   {
/* 90 */     return TextureUtil.readBufferedImage(DefaultResourcePack.class.getResourceAsStream("/" + new ResourceLocation("pack.png").getResourcePath()));
/*    */   }
/*    */   
/*    */   public String getPackName()
/*    */   {
/* 95 */     return "Default";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\DefaultResourcePack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */