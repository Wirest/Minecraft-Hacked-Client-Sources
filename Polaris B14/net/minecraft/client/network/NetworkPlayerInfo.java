/*     */ package net.minecraft.client.network;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.minecraft.MinecraftProfileTexture;
/*     */ import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.client.resources.DefaultPlayerSkin;
/*     */ import net.minecraft.client.resources.SkinManager;
/*     */ import net.minecraft.client.resources.SkinManager.SkinAvailableCallback;
/*     */ import net.minecraft.network.play.server.S38PacketPlayerListItem.AddPlayerData;
/*     */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*     */ import net.minecraft.scoreboard.Scoreboard;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.WorldSettings.GameType;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NetworkPlayerInfo
/*     */ {
/*     */   private final GameProfile gameProfile;
/*     */   private WorldSettings.GameType gameType;
/*     */   private int responseTime;
/*  26 */   private boolean playerTexturesLoaded = false;
/*     */   
/*     */   private ResourceLocation locationSkin;
/*     */   
/*     */   private ResourceLocation locationCape;
/*     */   
/*     */   private String skinType;
/*     */   
/*     */   private IChatComponent displayName;
/*  35 */   private int field_178873_i = 0;
/*  36 */   private int field_178870_j = 0;
/*  37 */   private long field_178871_k = 0L;
/*  38 */   private long field_178868_l = 0L;
/*  39 */   private long field_178869_m = 0L;
/*     */   
/*     */   public NetworkPlayerInfo(GameProfile p_i46294_1_)
/*     */   {
/*  43 */     this.gameProfile = p_i46294_1_;
/*     */   }
/*     */   
/*     */   public NetworkPlayerInfo(S38PacketPlayerListItem.AddPlayerData p_i46295_1_)
/*     */   {
/*  48 */     this.gameProfile = p_i46295_1_.getProfile();
/*  49 */     this.gameType = p_i46295_1_.getGameMode();
/*  50 */     this.responseTime = p_i46295_1_.getPing();
/*  51 */     this.displayName = p_i46295_1_.getDisplayName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GameProfile getGameProfile()
/*     */   {
/*  59 */     return this.gameProfile;
/*     */   }
/*     */   
/*     */   public WorldSettings.GameType getGameType()
/*     */   {
/*  64 */     return this.gameType;
/*     */   }
/*     */   
/*     */   public int getResponseTime()
/*     */   {
/*  69 */     return this.responseTime;
/*     */   }
/*     */   
/*     */   protected void setGameType(WorldSettings.GameType p_178839_1_)
/*     */   {
/*  74 */     this.gameType = p_178839_1_;
/*     */   }
/*     */   
/*     */   protected void setResponseTime(int p_178838_1_)
/*     */   {
/*  79 */     this.responseTime = p_178838_1_;
/*     */   }
/*     */   
/*     */   public boolean hasLocationSkin()
/*     */   {
/*  84 */     return this.locationSkin != null;
/*     */   }
/*     */   
/*     */   public String getSkinType()
/*     */   {
/*  89 */     return this.skinType == null ? DefaultPlayerSkin.getSkinType(this.gameProfile.getId()) : this.skinType;
/*     */   }
/*     */   
/*     */   public ResourceLocation getLocationSkin()
/*     */   {
/*  94 */     if (this.locationSkin == null)
/*     */     {
/*  96 */       loadPlayerTextures();
/*     */     }
/*     */     
/*  99 */     return (ResourceLocation)Objects.firstNonNull(this.locationSkin, DefaultPlayerSkin.getDefaultSkin(this.gameProfile.getId()));
/*     */   }
/*     */   
/*     */   public ResourceLocation getLocationCape()
/*     */   {
/* 104 */     if (this.locationCape == null)
/*     */     {
/* 106 */       loadPlayerTextures();
/*     */     }
/*     */     
/* 109 */     return this.locationCape;
/*     */   }
/*     */   
/*     */   public ScorePlayerTeam getPlayerTeam()
/*     */   {
/* 114 */     return Minecraft.getMinecraft().theWorld.getScoreboard().getPlayersTeam(getGameProfile().getName());
/*     */   }
/*     */   
/*     */   protected void loadPlayerTextures()
/*     */   {
/* 119 */     synchronized (this)
/*     */     {
/* 121 */       if (!this.playerTexturesLoaded)
/*     */       {
/* 123 */         this.playerTexturesLoaded = true;
/* 124 */         Minecraft.getMinecraft().getSkinManager().loadProfileTextures(this.gameProfile, new SkinManager.SkinAvailableCallback()
/*     */         {
/*     */           public void skinAvailable(MinecraftProfileTexture.Type p_180521_1_, ResourceLocation location, MinecraftProfileTexture profileTexture)
/*     */           {
/* 128 */             switch (p_180521_1_)
/*     */             {
/*     */             case CAPE: 
/* 131 */               NetworkPlayerInfo.this.locationSkin = location;
/* 132 */               NetworkPlayerInfo.this.skinType = profileTexture.getMetadata("model");
/*     */               
/* 134 */               if (NetworkPlayerInfo.this.skinType == null)
/*     */               {
/* 136 */                 NetworkPlayerInfo.this.skinType = "default";
/*     */               }
/*     */               
/* 139 */               break;
/*     */             
/*     */             case SKIN: 
/* 142 */               NetworkPlayerInfo.this.locationCape = location;
/*     */             }
/*     */           }
/* 145 */         }, true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setDisplayName(IChatComponent displayNameIn)
/*     */   {
/* 152 */     this.displayName = displayNameIn;
/*     */   }
/*     */   
/*     */   public IChatComponent getDisplayName()
/*     */   {
/* 157 */     return this.displayName;
/*     */   }
/*     */   
/*     */   public int func_178835_l()
/*     */   {
/* 162 */     return this.field_178873_i;
/*     */   }
/*     */   
/*     */   public void func_178836_b(int p_178836_1_)
/*     */   {
/* 167 */     this.field_178873_i = p_178836_1_;
/*     */   }
/*     */   
/*     */   public int func_178860_m()
/*     */   {
/* 172 */     return this.field_178870_j;
/*     */   }
/*     */   
/*     */   public void func_178857_c(int p_178857_1_)
/*     */   {
/* 177 */     this.field_178870_j = p_178857_1_;
/*     */   }
/*     */   
/*     */   public long func_178847_n()
/*     */   {
/* 182 */     return this.field_178871_k;
/*     */   }
/*     */   
/*     */   public void func_178846_a(long p_178846_1_)
/*     */   {
/* 187 */     this.field_178871_k = p_178846_1_;
/*     */   }
/*     */   
/*     */   public long func_178858_o()
/*     */   {
/* 192 */     return this.field_178868_l;
/*     */   }
/*     */   
/*     */   public void func_178844_b(long p_178844_1_)
/*     */   {
/* 197 */     this.field_178868_l = p_178844_1_;
/*     */   }
/*     */   
/*     */   public long func_178855_p()
/*     */   {
/* 202 */     return this.field_178869_m;
/*     */   }
/*     */   
/*     */   public void func_178843_c(long p_178843_1_)
/*     */   {
/* 207 */     this.field_178869_m = p_178843_1_;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\network\NetworkPlayerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */