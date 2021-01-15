/*     */ package net.minecraft.client.entity;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.image.BufferedImage;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.client.network.NetworkPlayerInfo;
/*     */ import net.minecraft.client.renderer.IImageBuffer;
/*     */ import net.minecraft.client.renderer.ImageBufferDownload;
/*     */ import net.minecraft.client.renderer.ThreadDownloadImageData;
/*     */ import net.minecraft.client.renderer.texture.ITextureObject;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.DefaultPlayerSkin;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.StringUtils;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldSettings.GameType;
/*     */ import optfine.PlayerConfigurations;
/*     */ import optfine.Reflector;
/*     */ import optfine.ReflectorMethod;
/*     */ import org.apache.commons.io.FilenameUtils;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ 
/*     */ public abstract class AbstractClientPlayer extends EntityPlayer
/*     */ {
/*     */   private NetworkPlayerInfo playerInfo;
/*  34 */   private ResourceLocation ofLocationCape = null;
/*     */   private static final String __OBFID = "CL_00000935";
/*     */   
/*     */   public AbstractClientPlayer(World worldIn, GameProfile playerProfile)
/*     */   {
/*  39 */     super(worldIn, playerProfile);
/*  40 */     String s = playerProfile.getName();
/*  41 */     downloadCape(s);
/*  42 */     PlayerConfigurations.getPlayerConfiguration(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSpectator()
/*     */   {
/*  50 */     NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(getGameProfile().getId());
/*  51 */     return (networkplayerinfo != null) && (networkplayerinfo.getGameType() == WorldSettings.GameType.SPECTATOR);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasPlayerInfo()
/*     */   {
/*  59 */     return getPlayerInfo() != null;
/*     */   }
/*     */   
/*     */   protected NetworkPlayerInfo getPlayerInfo()
/*     */   {
/*  64 */     if (this.playerInfo == null)
/*     */     {
/*  66 */       this.playerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(getUniqueID());
/*     */     }
/*     */     
/*  69 */     return this.playerInfo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasSkin()
/*     */   {
/*  77 */     NetworkPlayerInfo networkplayerinfo = getPlayerInfo();
/*  78 */     return (networkplayerinfo != null) && (networkplayerinfo.hasLocationSkin());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceLocation getLocationSkin()
/*     */   {
/*  86 */     NetworkPlayerInfo networkplayerinfo = getPlayerInfo();
/*  87 */     return networkplayerinfo == null ? DefaultPlayerSkin.getDefaultSkin(getUniqueID()) : networkplayerinfo.getLocationSkin();
/*     */   }
/*     */   
/*     */   public ResourceLocation getLocationCape()
/*     */   {
/*  92 */     if (!optfine.Config.isShowCapes())
/*     */     {
/*  94 */       return null;
/*     */     }
/*  96 */     if (this.ofLocationCape != null)
/*     */     {
/*  98 */       return this.ofLocationCape;
/*     */     }
/*     */     
/*     */ 
/* 102 */     NetworkPlayerInfo networkplayerinfo = getPlayerInfo();
/* 103 */     return networkplayerinfo == null ? null : networkplayerinfo.getLocationCape();
/*     */   }
/*     */   
/*     */ 
/*     */   public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocationIn, String username)
/*     */   {
/* 109 */     TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
/* 110 */     Object object = texturemanager.getTexture(resourceLocationIn);
/*     */     
/* 112 */     if (object == null)
/*     */     {
/* 114 */       object = new ThreadDownloadImageData(null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", new Object[] { StringUtils.stripControlCodes(username) }), DefaultPlayerSkin.getDefaultSkin(getOfflineUUID(username)), new ImageBufferDownload());
/* 115 */       texturemanager.loadTexture(resourceLocationIn, (ITextureObject)object);
/*     */     }
/*     */     
/* 118 */     return (ThreadDownloadImageData)object;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ResourceLocation getLocationSkin(String username)
/*     */   {
/* 126 */     return new ResourceLocation("skins/" + StringUtils.stripControlCodes(username));
/*     */   }
/*     */   
/*     */   public String getSkinType()
/*     */   {
/* 131 */     NetworkPlayerInfo networkplayerinfo = getPlayerInfo();
/* 132 */     return networkplayerinfo == null ? DefaultPlayerSkin.getSkinType(getUniqueID()) : networkplayerinfo.getSkinType();
/*     */   }
/*     */   
/*     */   public float getFovModifier()
/*     */   {
/* 137 */     float f = 1.0F;
/*     */     
/* 139 */     if (this.capabilities.isFlying)
/*     */     {
/* 141 */       f *= 1.1F;
/*     */     }
/*     */     
/* 144 */     IAttributeInstance iattributeinstance = getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.movementSpeed);
/* 145 */     f = (float)(f * ((iattributeinstance.getAttributeValue() / this.capabilities.getWalkSpeed() + 1.0D) / 2.0D));
/*     */     
/* 147 */     if ((this.capabilities.getWalkSpeed() == 0.0F) || (Float.isNaN(f)) || (Float.isInfinite(f)))
/*     */     {
/* 149 */       f = 1.0F;
/*     */     }
/*     */     
/* 152 */     if ((isUsingItem()) && (getItemInUse().getItem() == Items.bow))
/*     */     {
/* 154 */       int i = getItemInUseDuration();
/* 155 */       float f1 = i / 20.0F;
/*     */       
/* 157 */       if (f1 > 1.0F)
/*     */       {
/* 159 */         f1 = 1.0F;
/*     */       }
/*     */       else
/*     */       {
/* 163 */         f1 *= f1;
/*     */       }
/*     */       
/* 166 */       f *= (1.0F - f1 * 0.15F);
/*     */     }
/*     */     
/* 169 */     if (Polaris.instance.moduleManager.getModuleByName("NoFov").isToggled()) {
/* 170 */       return isSprinting() ? 1.15F : 1.0F;
/*     */     }
/*     */     
/* 173 */     return Reflector.ForgeHooksClient_getOffsetFOV.exists() ? Reflector.callFloat(Reflector.ForgeHooksClient_getOffsetFOV, new Object[] { this, Float.valueOf(f) }) : f;
/*     */   }
/*     */   
/*     */   private void downloadCape(String p_downloadCape_1_)
/*     */   {
/* 178 */     if ((p_downloadCape_1_ != null) && (!p_downloadCape_1_.isEmpty()))
/*     */     {
/* 180 */       p_downloadCape_1_ = StringUtils.stripControlCodes(p_downloadCape_1_);
/* 181 */       String s = "http://s.optifine.net/capes/" + p_downloadCape_1_ + ".png";
/* 182 */       String s1 = FilenameUtils.getBaseName(s);
/* 183 */       final ResourceLocation resourcelocation = new ResourceLocation("capeof/" + s1);
/* 184 */       TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
/* 185 */       ITextureObject itextureobject = texturemanager.getTexture(resourcelocation);
/*     */       
/* 187 */       if ((itextureobject != null) && ((itextureobject instanceof ThreadDownloadImageData)))
/*     */       {
/* 189 */         ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)itextureobject;
/*     */         
/* 191 */         if (threaddownloadimagedata.imageFound != null)
/*     */         {
/* 193 */           if (threaddownloadimagedata.imageFound.booleanValue())
/*     */           {
/* 195 */             this.ofLocationCape = resourcelocation;
/*     */           }
/*     */           
/* 198 */           return;
/*     */         }
/*     */       }
/*     */       
/* 202 */       IImageBuffer iimagebuffer = new IImageBuffer()
/*     */       {
/* 204 */         ImageBufferDownload ibd = new ImageBufferDownload();
/*     */         
/*     */         public BufferedImage parseUserSkin(BufferedImage image) {
/* 207 */           return AbstractClientPlayer.this.parseCape(image);
/*     */         }
/*     */         
/*     */         public void skinAvailable() {
/* 211 */           AbstractClientPlayer.this.ofLocationCape = resourcelocation;
/*     */         }
/* 213 */       };
/* 214 */       ThreadDownloadImageData threaddownloadimagedata1 = new ThreadDownloadImageData(null, s, null, iimagebuffer);
/* 215 */       texturemanager.loadTexture(resourcelocation, threaddownloadimagedata1);
/*     */     }
/*     */   }
/*     */   
/*     */   private BufferedImage parseCape(BufferedImage p_parseCape_1_)
/*     */   {
/* 221 */     int i = 64;
/* 222 */     int j = 32;
/* 223 */     int k = p_parseCape_1_.getWidth();
/*     */     
/* 225 */     for (int l = p_parseCape_1_.getHeight(); (i < k) || (j < l); j *= 2)
/*     */     {
/* 227 */       i *= 2;
/*     */     }
/*     */     
/* 230 */     BufferedImage bufferedimage = new BufferedImage(i, j, 2);
/* 231 */     Graphics graphics = bufferedimage.getGraphics();
/* 232 */     graphics.drawImage(p_parseCape_1_, 0, 0, null);
/* 233 */     graphics.dispose();
/* 234 */     return bufferedimage;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\entity\AbstractClientPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */