/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTUtil;
/*     */ import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.PlayerProfileCache;
/*     */ import net.minecraft.util.StringUtils;
/*     */ 
/*     */ public class TileEntitySkull extends TileEntity
/*     */ {
/*     */   private int skullType;
/*     */   private int skullRotation;
/*  18 */   private GameProfile playerProfile = null;
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound)
/*     */   {
/*  22 */     super.writeToNBT(compound);
/*  23 */     compound.setByte("SkullType", (byte)(this.skullType & 0xFF));
/*  24 */     compound.setByte("Rot", (byte)(this.skullRotation & 0xFF));
/*     */     
/*  26 */     if (this.playerProfile != null)
/*     */     {
/*  28 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  29 */       NBTUtil.writeGameProfile(nbttagcompound, this.playerProfile);
/*  30 */       compound.setTag("Owner", nbttagcompound);
/*     */     }
/*     */   }
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound)
/*     */   {
/*  36 */     super.readFromNBT(compound);
/*  37 */     this.skullType = compound.getByte("SkullType");
/*  38 */     this.skullRotation = compound.getByte("Rot");
/*     */     
/*  40 */     if (this.skullType == 3)
/*     */     {
/*  42 */       if (compound.hasKey("Owner", 10))
/*     */       {
/*  44 */         this.playerProfile = NBTUtil.readGameProfileFromNBT(compound.getCompoundTag("Owner"));
/*     */       }
/*  46 */       else if (compound.hasKey("ExtraType", 8))
/*     */       {
/*  48 */         String s = compound.getString("ExtraType");
/*     */         
/*  50 */         if (!StringUtils.isNullOrEmpty(s))
/*     */         {
/*  52 */           this.playerProfile = new GameProfile(null, s);
/*  53 */           updatePlayerProfile();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public GameProfile getPlayerProfile()
/*     */   {
/*  61 */     return this.playerProfile;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public net.minecraft.network.Packet getDescriptionPacket()
/*     */   {
/*  70 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  71 */     writeToNBT(nbttagcompound);
/*  72 */     return new S35PacketUpdateTileEntity(this.pos, 4, nbttagcompound);
/*     */   }
/*     */   
/*     */   public void setType(int type)
/*     */   {
/*  77 */     this.skullType = type;
/*  78 */     this.playerProfile = null;
/*     */   }
/*     */   
/*     */   public void setPlayerProfile(GameProfile playerProfile)
/*     */   {
/*  83 */     this.skullType = 3;
/*  84 */     this.playerProfile = playerProfile;
/*  85 */     updatePlayerProfile();
/*     */   }
/*     */   
/*     */   private void updatePlayerProfile()
/*     */   {
/*  90 */     this.playerProfile = updateGameprofile(this.playerProfile);
/*  91 */     markDirty();
/*     */   }
/*     */   
/*     */   public static GameProfile updateGameprofile(GameProfile input)
/*     */   {
/*  96 */     if ((input != null) && (!StringUtils.isNullOrEmpty(input.getName())))
/*     */     {
/*  98 */       if ((input.isComplete()) && (input.getProperties().containsKey("textures")))
/*     */       {
/* 100 */         return input;
/*     */       }
/* 102 */       if (MinecraftServer.getServer() == null)
/*     */       {
/* 104 */         return input;
/*     */       }
/*     */       
/*     */ 
/* 108 */       GameProfile gameprofile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(input.getName());
/*     */       
/* 110 */       if (gameprofile == null)
/*     */       {
/* 112 */         return input;
/*     */       }
/*     */       
/*     */ 
/* 116 */       Property property = (Property)com.google.common.collect.Iterables.getFirst(gameprofile.getProperties().get("textures"), null);
/*     */       
/* 118 */       if (property == null)
/*     */       {
/* 120 */         gameprofile = MinecraftServer.getServer().getMinecraftSessionService().fillProfileProperties(gameprofile, true);
/*     */       }
/*     */       
/* 123 */       return gameprofile;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 129 */     return input;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getSkullType()
/*     */   {
/* 135 */     return this.skullType;
/*     */   }
/*     */   
/*     */   public int getSkullRotation()
/*     */   {
/* 140 */     return this.skullRotation;
/*     */   }
/*     */   
/*     */   public void setSkullRotation(int rotation)
/*     */   {
/* 145 */     this.skullRotation = rotation;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\tileentity\TileEntitySkull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */