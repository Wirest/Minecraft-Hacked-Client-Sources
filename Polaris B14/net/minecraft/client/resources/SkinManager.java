/*     */ package net.minecraft.client.resources;
/*     */ 
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.minecraft.InsecureTextureException;
/*     */ import com.mojang.authlib.minecraft.MinecraftProfileTexture;
/*     */ import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
/*     */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.IImageBuffer;
/*     */ import net.minecraft.client.renderer.ThreadDownloadImageData;
/*     */ import net.minecraft.client.renderer.texture.ITextureObject;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Session;
/*     */ 
/*     */ public class SkinManager
/*     */ {
/*  29 */   private static final ExecutorService THREAD_POOL = new java.util.concurrent.ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue());
/*     */   private final TextureManager textureManager;
/*     */   private final File skinCacheDir;
/*     */   private final MinecraftSessionService sessionService;
/*     */   private final LoadingCache<GameProfile, Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>> skinCacheLoader;
/*     */   
/*     */   public SkinManager(TextureManager textureManagerInstance, File skinCacheDirectory, MinecraftSessionService sessionService)
/*     */   {
/*  37 */     this.textureManager = textureManagerInstance;
/*  38 */     this.skinCacheDir = skinCacheDirectory;
/*  39 */     this.sessionService = sessionService;
/*  40 */     this.skinCacheLoader = CacheBuilder.newBuilder().expireAfterAccess(15L, TimeUnit.SECONDS).build(new CacheLoader()
/*     */     {
/*     */       public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> load(GameProfile p_load_1_) throws Exception
/*     */       {
/*  44 */         return Minecraft.getMinecraft().getSessionService().getTextures(p_load_1_, false);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceLocation loadSkin(MinecraftProfileTexture profileTexture, MinecraftProfileTexture.Type p_152792_2_)
/*     */   {
/*  54 */     return loadSkin(profileTexture, p_152792_2_, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceLocation loadSkin(final MinecraftProfileTexture profileTexture, final MinecraftProfileTexture.Type p_152789_2_, final SkinAvailableCallback skinAvailableCallback)
/*     */   {
/*  62 */     final ResourceLocation resourcelocation = new ResourceLocation("skins/" + profileTexture.getHash());
/*  63 */     ITextureObject itextureobject = this.textureManager.getTexture(resourcelocation);
/*     */     
/*  65 */     if (itextureobject != null)
/*     */     {
/*  67 */       if (skinAvailableCallback != null)
/*     */       {
/*  69 */         skinAvailableCallback.skinAvailable(p_152789_2_, resourcelocation, profileTexture);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  74 */       File file1 = new File(this.skinCacheDir, profileTexture.getHash().length() > 2 ? profileTexture.getHash().substring(0, 2) : "xx");
/*  75 */       File file2 = new File(file1, profileTexture.getHash());
/*  76 */       final IImageBuffer iimagebuffer = p_152789_2_ == MinecraftProfileTexture.Type.SKIN ? new net.minecraft.client.renderer.ImageBufferDownload() : null;
/*  77 */       ThreadDownloadImageData threaddownloadimagedata = new ThreadDownloadImageData(file2, profileTexture.getUrl(), DefaultPlayerSkin.getDefaultSkinLegacy(), new IImageBuffer()
/*     */       {
/*     */         public BufferedImage parseUserSkin(BufferedImage image)
/*     */         {
/*  81 */           if (iimagebuffer != null)
/*     */           {
/*  83 */             image = iimagebuffer.parseUserSkin(image);
/*     */           }
/*     */           
/*  86 */           return image;
/*     */         }
/*     */         
/*     */         public void skinAvailable() {
/*  90 */           if (iimagebuffer != null)
/*     */           {
/*  92 */             iimagebuffer.skinAvailable();
/*     */           }
/*     */           
/*  95 */           if (skinAvailableCallback != null)
/*     */           {
/*  97 */             skinAvailableCallback.skinAvailable(p_152789_2_, resourcelocation, profileTexture);
/*     */           }
/*     */         }
/* 100 */       });
/* 101 */       this.textureManager.loadTexture(resourcelocation, threaddownloadimagedata);
/*     */     }
/*     */     
/* 104 */     return resourcelocation;
/*     */   }
/*     */   
/*     */   public void loadProfileTextures(final GameProfile profile, final SkinAvailableCallback skinAvailableCallback, final boolean requireSecure)
/*     */   {
/* 109 */     THREAD_POOL.submit(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 113 */         final Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = Maps.newHashMap();
/*     */         
/*     */         try
/*     */         {
/* 117 */           map.putAll(SkinManager.this.sessionService.getTextures(profile, requireSecure));
/*     */         }
/*     */         catch (InsecureTextureException localInsecureTextureException) {}
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 124 */         if ((map.isEmpty()) && (profile.getId().equals(Minecraft.getMinecraft().getSession().getProfile().getId())))
/*     */         {
/* 126 */           profile.getProperties().clear();
/* 127 */           profile.getProperties().putAll(Minecraft.getMinecraft().func_181037_M());
/* 128 */           map.putAll(SkinManager.this.sessionService.getTextures(profile, false));
/*     */         }
/*     */         
/* 131 */         Minecraft.getMinecraft().addScheduledTask(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 135 */             if (map.containsKey(MinecraftProfileTexture.Type.SKIN))
/*     */             {
/* 137 */               SkinManager.this.loadSkin((MinecraftProfileTexture)map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN, this.val$skinAvailableCallback);
/*     */             }
/*     */             
/* 140 */             if (map.containsKey(MinecraftProfileTexture.Type.CAPE))
/*     */             {
/* 142 */               SkinManager.this.loadSkin((MinecraftProfileTexture)map.get(MinecraftProfileTexture.Type.CAPE), MinecraftProfileTexture.Type.CAPE, this.val$skinAvailableCallback);
/*     */             }
/*     */           }
/*     */         });
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> loadSkinFromCache(GameProfile profile)
/*     */   {
/* 152 */     return (Map)this.skinCacheLoader.getUnchecked(profile);
/*     */   }
/*     */   
/*     */   public static abstract interface SkinAvailableCallback
/*     */   {
/*     */     public abstract void skinAvailable(MinecraftProfileTexture.Type paramType, ResourceLocation paramResourceLocation, MinecraftProfileTexture paramMinecraftProfileTexture);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\SkinManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */