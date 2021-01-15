/*     */ package net.minecraft.entity;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
/*     */ import net.minecraft.entity.item.EntityArmorStand;
/*     */ import net.minecraft.entity.item.EntityBoat;
/*     */ import net.minecraft.entity.item.EntityEnderCrystal;
/*     */ import net.minecraft.entity.item.EntityEnderEye;
/*     */ import net.minecraft.entity.item.EntityEnderPearl;
/*     */ import net.minecraft.entity.item.EntityExpBottle;
/*     */ import net.minecraft.entity.item.EntityFallingBlock;
/*     */ import net.minecraft.entity.item.EntityFireworkRocket;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.entity.item.EntityMinecart;
/*     */ import net.minecraft.entity.item.EntityMinecart.EnumMinecartType;
/*     */ import net.minecraft.entity.item.EntityPainting;
/*     */ import net.minecraft.entity.item.EntityXPOrb;
/*     */ import net.minecraft.entity.passive.IAnimals;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.entity.projectile.EntityFireball;
/*     */ import net.minecraft.entity.projectile.EntityFishHook;
/*     */ import net.minecraft.entity.projectile.EntityPotion;
/*     */ import net.minecraft.entity.projectile.EntitySmallFireball;
/*     */ import net.minecraft.entity.projectile.EntitySnowball;
/*     */ import net.minecraft.entity.projectile.EntityWitherSkull;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemMap;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.NetHandlerPlayServer;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S04PacketEntityEquipment;
/*     */ import net.minecraft.network.play.server.S0APacketUseBed;
/*     */ import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
/*     */ import net.minecraft.network.play.server.S0EPacketSpawnObject;
/*     */ import net.minecraft.network.play.server.S0FPacketSpawnMob;
/*     */ import net.minecraft.network.play.server.S10PacketSpawnPainting;
/*     */ import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
/*     */ import net.minecraft.network.play.server.S12PacketEntityVelocity;
/*     */ import net.minecraft.network.play.server.S14PacketEntity.S16PacketEntityLook;
/*     */ import net.minecraft.network.play.server.S14PacketEntity.S17PacketEntityLookMove;
/*     */ import net.minecraft.network.play.server.S18PacketEntityTeleport;
/*     */ import net.minecraft.network.play.server.S1BPacketEntityAttach;
/*     */ import net.minecraft.network.play.server.S1CPacketEntityMetadata;
/*     */ import net.minecraft.network.play.server.S1DPacketEntityEffect;
/*     */ import net.minecraft.network.play.server.S20PacketEntityProperties;
/*     */ import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.WorldServer;
/*     */ import net.minecraft.world.storage.MapData;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class EntityTrackerEntry
/*     */ {
/*  65 */   private static final Logger logger = ;
/*     */   
/*     */   public Entity trackedEntity;
/*     */   
/*     */   public int trackingDistanceThreshold;
/*     */   
/*     */   public int updateFrequency;
/*     */   
/*     */   public int encodedPosX;
/*     */   
/*     */   public int encodedPosY;
/*     */   
/*     */   public int encodedPosZ;
/*     */   
/*     */   public int encodedRotationYaw;
/*     */   
/*     */   public int encodedRotationPitch;
/*     */   
/*     */   public int lastHeadMotion;
/*     */   
/*     */   public double lastTrackedEntityMotionX;
/*     */   
/*     */   public double lastTrackedEntityMotionY;
/*     */   
/*     */   public double motionZ;
/*     */   
/*     */   public int updateCounter;
/*     */   
/*     */   private double lastTrackedEntityPosX;
/*     */   
/*     */   private double lastTrackedEntityPosY;
/*     */   
/*     */   private double lastTrackedEntityPosZ;
/*     */   
/*     */   private boolean firstUpdateDone;
/*     */   
/*     */   private boolean sendVelocityUpdates;
/*     */   
/*     */   private int ticksSinceLastForcedTeleport;
/*     */   private Entity field_85178_v;
/*     */   private boolean ridingEntity;
/*     */   private boolean onGround;
/*     */   public boolean playerEntitiesUpdated;
/* 108 */   public Set<EntityPlayerMP> trackingPlayers = com.google.common.collect.Sets.newHashSet();
/*     */   
/*     */   public EntityTrackerEntry(Entity trackedEntityIn, int trackingDistanceThresholdIn, int updateFrequencyIn, boolean sendVelocityUpdatesIn)
/*     */   {
/* 112 */     this.trackedEntity = trackedEntityIn;
/* 113 */     this.trackingDistanceThreshold = trackingDistanceThresholdIn;
/* 114 */     this.updateFrequency = updateFrequencyIn;
/* 115 */     this.sendVelocityUpdates = sendVelocityUpdatesIn;
/* 116 */     this.encodedPosX = MathHelper.floor_double(trackedEntityIn.posX * 32.0D);
/* 117 */     this.encodedPosY = MathHelper.floor_double(trackedEntityIn.posY * 32.0D);
/* 118 */     this.encodedPosZ = MathHelper.floor_double(trackedEntityIn.posZ * 32.0D);
/* 119 */     this.encodedRotationYaw = MathHelper.floor_float(trackedEntityIn.rotationYaw * 256.0F / 360.0F);
/* 120 */     this.encodedRotationPitch = MathHelper.floor_float(trackedEntityIn.rotationPitch * 256.0F / 360.0F);
/* 121 */     this.lastHeadMotion = MathHelper.floor_float(trackedEntityIn.getRotationYawHead() * 256.0F / 360.0F);
/* 122 */     this.onGround = trackedEntityIn.onGround;
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/* 127 */     return ((EntityTrackerEntry)p_equals_1_).trackedEntity.getEntityId() == this.trackedEntity.getEntityId();
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 132 */     return this.trackedEntity.getEntityId();
/*     */   }
/*     */   
/*     */   public void updatePlayerList(List<EntityPlayer> p_73122_1_)
/*     */   {
/* 137 */     this.playerEntitiesUpdated = false;
/*     */     
/* 139 */     if ((!this.firstUpdateDone) || (this.trackedEntity.getDistanceSq(this.lastTrackedEntityPosX, this.lastTrackedEntityPosY, this.lastTrackedEntityPosZ) > 16.0D))
/*     */     {
/* 141 */       this.lastTrackedEntityPosX = this.trackedEntity.posX;
/* 142 */       this.lastTrackedEntityPosY = this.trackedEntity.posY;
/* 143 */       this.lastTrackedEntityPosZ = this.trackedEntity.posZ;
/* 144 */       this.firstUpdateDone = true;
/* 145 */       this.playerEntitiesUpdated = true;
/* 146 */       updatePlayerEntities(p_73122_1_);
/*     */     }
/*     */     
/* 149 */     if ((this.field_85178_v != this.trackedEntity.ridingEntity) || ((this.trackedEntity.ridingEntity != null) && (this.updateCounter % 60 == 0)))
/*     */     {
/* 151 */       this.field_85178_v = this.trackedEntity.ridingEntity;
/* 152 */       sendPacketToTrackedPlayers(new S1BPacketEntityAttach(0, this.trackedEntity, this.trackedEntity.ridingEntity));
/*     */     }
/*     */     
/* 155 */     if (((this.trackedEntity instanceof EntityItemFrame)) && (this.updateCounter % 10 == 0))
/*     */     {
/* 157 */       EntityItemFrame entityitemframe = (EntityItemFrame)this.trackedEntity;
/* 158 */       ItemStack itemstack = entityitemframe.getDisplayedItem();
/*     */       
/* 160 */       if ((itemstack != null) && ((itemstack.getItem() instanceof ItemMap)))
/*     */       {
/* 162 */         MapData mapdata = Items.filled_map.getMapData(itemstack, this.trackedEntity.worldObj);
/*     */         
/* 164 */         for (EntityPlayer entityplayer : p_73122_1_)
/*     */         {
/* 166 */           EntityPlayerMP entityplayermp = (EntityPlayerMP)entityplayer;
/* 167 */           mapdata.updateVisiblePlayers(entityplayermp, itemstack);
/* 168 */           Packet packet = Items.filled_map.createMapDataPacket(itemstack, this.trackedEntity.worldObj, entityplayermp);
/*     */           
/* 170 */           if (packet != null)
/*     */           {
/* 172 */             entityplayermp.playerNetServerHandler.sendPacket(packet);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 177 */       sendMetadataToAllAssociatedPlayers();
/*     */     }
/*     */     
/* 180 */     if ((this.updateCounter % this.updateFrequency == 0) || (this.trackedEntity.isAirBorne) || (this.trackedEntity.getDataWatcher().hasObjectChanged()))
/*     */     {
/* 182 */       if (this.trackedEntity.ridingEntity == null)
/*     */       {
/* 184 */         this.ticksSinceLastForcedTeleport += 1;
/* 185 */         int k = MathHelper.floor_double(this.trackedEntity.posX * 32.0D);
/* 186 */         int j1 = MathHelper.floor_double(this.trackedEntity.posY * 32.0D);
/* 187 */         int k1 = MathHelper.floor_double(this.trackedEntity.posZ * 32.0D);
/* 188 */         int l1 = MathHelper.floor_float(this.trackedEntity.rotationYaw * 256.0F / 360.0F);
/* 189 */         int i2 = MathHelper.floor_float(this.trackedEntity.rotationPitch * 256.0F / 360.0F);
/* 190 */         int j2 = k - this.encodedPosX;
/* 191 */         int k2 = j1 - this.encodedPosY;
/* 192 */         int i = k1 - this.encodedPosZ;
/* 193 */         Packet packet1 = null;
/* 194 */         boolean flag = (Math.abs(j2) >= 4) || (Math.abs(k2) >= 4) || (Math.abs(i) >= 4) || (this.updateCounter % 60 == 0);
/* 195 */         boolean flag1 = (Math.abs(l1 - this.encodedRotationYaw) >= 4) || (Math.abs(i2 - this.encodedRotationPitch) >= 4);
/*     */         
/* 197 */         if ((this.updateCounter > 0) || ((this.trackedEntity instanceof EntityArrow)))
/*     */         {
/* 199 */           if ((j2 >= -128) && (j2 < 128) && (k2 >= -128) && (k2 < 128) && (i >= -128) && (i < 128) && (this.ticksSinceLastForcedTeleport <= 400) && (!this.ridingEntity) && (this.onGround == this.trackedEntity.onGround))
/*     */           {
/* 201 */             if (((!flag) || (!flag1)) && (!(this.trackedEntity instanceof EntityArrow)))
/*     */             {
/* 203 */               if (flag)
/*     */               {
/* 205 */                 packet1 = new net.minecraft.network.play.server.S14PacketEntity.S15PacketEntityRelMove(this.trackedEntity.getEntityId(), (byte)j2, (byte)k2, (byte)i, this.trackedEntity.onGround);
/*     */               }
/* 207 */               else if (flag1)
/*     */               {
/* 209 */                 packet1 = new S14PacketEntity.S16PacketEntityLook(this.trackedEntity.getEntityId(), (byte)l1, (byte)i2, this.trackedEntity.onGround);
/*     */               }
/*     */               
/*     */             }
/*     */             else {
/* 214 */               packet1 = new S14PacketEntity.S17PacketEntityLookMove(this.trackedEntity.getEntityId(), (byte)j2, (byte)k2, (byte)i, (byte)l1, (byte)i2, this.trackedEntity.onGround);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 219 */             this.onGround = this.trackedEntity.onGround;
/* 220 */             this.ticksSinceLastForcedTeleport = 0;
/* 221 */             packet1 = new S18PacketEntityTeleport(this.trackedEntity.getEntityId(), k, j1, k1, (byte)l1, (byte)i2, this.trackedEntity.onGround);
/*     */           }
/*     */         }
/*     */         
/* 225 */         if (this.sendVelocityUpdates)
/*     */         {
/* 227 */           double d0 = this.trackedEntity.motionX - this.lastTrackedEntityMotionX;
/* 228 */           double d1 = this.trackedEntity.motionY - this.lastTrackedEntityMotionY;
/* 229 */           double d2 = this.trackedEntity.motionZ - this.motionZ;
/* 230 */           double d3 = 0.02D;
/* 231 */           double d4 = d0 * d0 + d1 * d1 + d2 * d2;
/*     */           
/* 233 */           if ((d4 > d3 * d3) || ((d4 > 0.0D) && (this.trackedEntity.motionX == 0.0D) && (this.trackedEntity.motionY == 0.0D) && (this.trackedEntity.motionZ == 0.0D)))
/*     */           {
/* 235 */             this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
/* 236 */             this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
/* 237 */             this.motionZ = this.trackedEntity.motionZ;
/* 238 */             sendPacketToTrackedPlayers(new S12PacketEntityVelocity(this.trackedEntity.getEntityId(), this.lastTrackedEntityMotionX, this.lastTrackedEntityMotionY, this.motionZ));
/*     */           }
/*     */         }
/*     */         
/* 242 */         if (packet1 != null)
/*     */         {
/* 244 */           sendPacketToTrackedPlayers(packet1);
/*     */         }
/*     */         
/* 247 */         sendMetadataToAllAssociatedPlayers();
/*     */         
/* 249 */         if (flag)
/*     */         {
/* 251 */           this.encodedPosX = k;
/* 252 */           this.encodedPosY = j1;
/* 253 */           this.encodedPosZ = k1;
/*     */         }
/*     */         
/* 256 */         if (flag1)
/*     */         {
/* 258 */           this.encodedRotationYaw = l1;
/* 259 */           this.encodedRotationPitch = i2;
/*     */         }
/*     */         
/* 262 */         this.ridingEntity = false;
/*     */       }
/*     */       else
/*     */       {
/* 266 */         int j = MathHelper.floor_float(this.trackedEntity.rotationYaw * 256.0F / 360.0F);
/* 267 */         int i1 = MathHelper.floor_float(this.trackedEntity.rotationPitch * 256.0F / 360.0F);
/* 268 */         boolean flag2 = (Math.abs(j - this.encodedRotationYaw) >= 4) || (Math.abs(i1 - this.encodedRotationPitch) >= 4);
/*     */         
/* 270 */         if (flag2)
/*     */         {
/* 272 */           sendPacketToTrackedPlayers(new S14PacketEntity.S16PacketEntityLook(this.trackedEntity.getEntityId(), (byte)j, (byte)i1, this.trackedEntity.onGround));
/* 273 */           this.encodedRotationYaw = j;
/* 274 */           this.encodedRotationPitch = i1;
/*     */         }
/*     */         
/* 277 */         this.encodedPosX = MathHelper.floor_double(this.trackedEntity.posX * 32.0D);
/* 278 */         this.encodedPosY = MathHelper.floor_double(this.trackedEntity.posY * 32.0D);
/* 279 */         this.encodedPosZ = MathHelper.floor_double(this.trackedEntity.posZ * 32.0D);
/* 280 */         sendMetadataToAllAssociatedPlayers();
/* 281 */         this.ridingEntity = true;
/*     */       }
/*     */       
/* 284 */       int l = MathHelper.floor_float(this.trackedEntity.getRotationYawHead() * 256.0F / 360.0F);
/*     */       
/* 286 */       if (Math.abs(l - this.lastHeadMotion) >= 4)
/*     */       {
/* 288 */         sendPacketToTrackedPlayers(new net.minecraft.network.play.server.S19PacketEntityHeadLook(this.trackedEntity, (byte)l));
/* 289 */         this.lastHeadMotion = l;
/*     */       }
/*     */       
/* 292 */       this.trackedEntity.isAirBorne = false;
/*     */     }
/*     */     
/* 295 */     this.updateCounter += 1;
/*     */     
/* 297 */     if (this.trackedEntity.velocityChanged)
/*     */     {
/* 299 */       func_151261_b(new S12PacketEntityVelocity(this.trackedEntity));
/* 300 */       this.trackedEntity.velocityChanged = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void sendMetadataToAllAssociatedPlayers()
/*     */   {
/* 310 */     DataWatcher datawatcher = this.trackedEntity.getDataWatcher();
/*     */     
/* 312 */     if (datawatcher.hasObjectChanged())
/*     */     {
/* 314 */       func_151261_b(new S1CPacketEntityMetadata(this.trackedEntity.getEntityId(), datawatcher, false));
/*     */     }
/*     */     
/* 317 */     if ((this.trackedEntity instanceof EntityLivingBase))
/*     */     {
/* 319 */       ServersideAttributeMap serversideattributemap = (ServersideAttributeMap)((EntityLivingBase)this.trackedEntity).getAttributeMap();
/* 320 */       Set<IAttributeInstance> set = serversideattributemap.getAttributeInstanceSet();
/*     */       
/* 322 */       if (!set.isEmpty())
/*     */       {
/* 324 */         func_151261_b(new S20PacketEntityProperties(this.trackedEntity.getEntityId(), set));
/*     */       }
/*     */       
/* 327 */       set.clear();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendPacketToTrackedPlayers(Packet packetIn)
/*     */   {
/* 336 */     for (EntityPlayerMP entityplayermp : this.trackingPlayers)
/*     */     {
/* 338 */       entityplayermp.playerNetServerHandler.sendPacket(packetIn);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_151261_b(Packet packetIn)
/*     */   {
/* 344 */     sendPacketToTrackedPlayers(packetIn);
/*     */     
/* 346 */     if ((this.trackedEntity instanceof EntityPlayerMP))
/*     */     {
/* 348 */       ((EntityPlayerMP)this.trackedEntity).playerNetServerHandler.sendPacket(packetIn);
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendDestroyEntityPacketToTrackedPlayers()
/*     */   {
/* 354 */     for (EntityPlayerMP entityplayermp : this.trackingPlayers)
/*     */     {
/* 356 */       entityplayermp.removeEntity(this.trackedEntity);
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeFromTrackedPlayers(EntityPlayerMP playerMP)
/*     */   {
/* 362 */     if (this.trackingPlayers.contains(playerMP))
/*     */     {
/* 364 */       playerMP.removeEntity(this.trackedEntity);
/* 365 */       this.trackingPlayers.remove(playerMP);
/*     */     }
/*     */   }
/*     */   
/*     */   public void updatePlayerEntity(EntityPlayerMP playerMP)
/*     */   {
/* 371 */     if (playerMP != this.trackedEntity)
/*     */     {
/* 373 */       if (func_180233_c(playerMP))
/*     */       {
/* 375 */         if ((!this.trackingPlayers.contains(playerMP)) && ((isPlayerWatchingThisChunk(playerMP)) || (this.trackedEntity.forceSpawn)))
/*     */         {
/* 377 */           this.trackingPlayers.add(playerMP);
/* 378 */           Packet packet = func_151260_c();
/* 379 */           playerMP.playerNetServerHandler.sendPacket(packet);
/*     */           
/* 381 */           if (!this.trackedEntity.getDataWatcher().getIsBlank())
/*     */           {
/* 383 */             playerMP.playerNetServerHandler.sendPacket(new S1CPacketEntityMetadata(this.trackedEntity.getEntityId(), this.trackedEntity.getDataWatcher(), true));
/*     */           }
/*     */           
/* 386 */           net.minecraft.nbt.NBTTagCompound nbttagcompound = this.trackedEntity.getNBTTagCompound();
/*     */           
/* 388 */           if (nbttagcompound != null)
/*     */           {
/* 390 */             playerMP.playerNetServerHandler.sendPacket(new S49PacketUpdateEntityNBT(this.trackedEntity.getEntityId(), nbttagcompound));
/*     */           }
/*     */           
/* 393 */           if ((this.trackedEntity instanceof EntityLivingBase))
/*     */           {
/* 395 */             ServersideAttributeMap serversideattributemap = (ServersideAttributeMap)((EntityLivingBase)this.trackedEntity).getAttributeMap();
/* 396 */             Collection<IAttributeInstance> collection = serversideattributemap.getWatchedAttributes();
/*     */             
/* 398 */             if (!collection.isEmpty())
/*     */             {
/* 400 */               playerMP.playerNetServerHandler.sendPacket(new S20PacketEntityProperties(this.trackedEntity.getEntityId(), collection));
/*     */             }
/*     */           }
/*     */           
/* 404 */           this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
/* 405 */           this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
/* 406 */           this.motionZ = this.trackedEntity.motionZ;
/*     */           
/* 408 */           if ((this.sendVelocityUpdates) && (!(packet instanceof S0FPacketSpawnMob)))
/*     */           {
/* 410 */             playerMP.playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(this.trackedEntity.getEntityId(), this.trackedEntity.motionX, this.trackedEntity.motionY, this.trackedEntity.motionZ));
/*     */           }
/*     */           
/* 413 */           if (this.trackedEntity.ridingEntity != null)
/*     */           {
/* 415 */             playerMP.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(0, this.trackedEntity, this.trackedEntity.ridingEntity));
/*     */           }
/*     */           
/* 418 */           if (((this.trackedEntity instanceof EntityLiving)) && (((EntityLiving)this.trackedEntity).getLeashedToEntity() != null))
/*     */           {
/* 420 */             playerMP.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(1, this.trackedEntity, ((EntityLiving)this.trackedEntity).getLeashedToEntity()));
/*     */           }
/*     */           
/* 423 */           if ((this.trackedEntity instanceof EntityLivingBase))
/*     */           {
/* 425 */             for (int i = 0; i < 5; i++)
/*     */             {
/* 427 */               ItemStack itemstack = ((EntityLivingBase)this.trackedEntity).getEquipmentInSlot(i);
/*     */               
/* 429 */               if (itemstack != null)
/*     */               {
/* 431 */                 playerMP.playerNetServerHandler.sendPacket(new S04PacketEntityEquipment(this.trackedEntity.getEntityId(), i, itemstack));
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 436 */           if ((this.trackedEntity instanceof EntityPlayer))
/*     */           {
/* 438 */             EntityPlayer entityplayer = (EntityPlayer)this.trackedEntity;
/*     */             
/* 440 */             if (entityplayer.isPlayerSleeping())
/*     */             {
/* 442 */               playerMP.playerNetServerHandler.sendPacket(new S0APacketUseBed(entityplayer, new BlockPos(this.trackedEntity)));
/*     */             }
/*     */           }
/*     */           
/* 446 */           if ((this.trackedEntity instanceof EntityLivingBase))
/*     */           {
/* 448 */             EntityLivingBase entitylivingbase = (EntityLivingBase)this.trackedEntity;
/*     */             
/* 450 */             for (PotionEffect potioneffect : entitylivingbase.getActivePotionEffects())
/*     */             {
/* 452 */               playerMP.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(this.trackedEntity.getEntityId(), potioneffect));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 457 */       else if (this.trackingPlayers.contains(playerMP))
/*     */       {
/* 459 */         this.trackingPlayers.remove(playerMP);
/* 460 */         playerMP.removeEntity(this.trackedEntity);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean func_180233_c(EntityPlayerMP playerMP)
/*     */   {
/* 467 */     double d0 = playerMP.posX - this.encodedPosX / 32;
/* 468 */     double d1 = playerMP.posZ - this.encodedPosZ / 32;
/* 469 */     return (d0 >= -this.trackingDistanceThreshold) && (d0 <= this.trackingDistanceThreshold) && (d1 >= -this.trackingDistanceThreshold) && (d1 <= this.trackingDistanceThreshold) && (this.trackedEntity.isSpectatedByPlayer(playerMP));
/*     */   }
/*     */   
/*     */   private boolean isPlayerWatchingThisChunk(EntityPlayerMP playerMP)
/*     */   {
/* 474 */     return playerMP.getServerForPlayer().getPlayerManager().isPlayerWatchingChunk(playerMP, this.trackedEntity.chunkCoordX, this.trackedEntity.chunkCoordZ);
/*     */   }
/*     */   
/*     */   public void updatePlayerEntities(List<EntityPlayer> p_73125_1_)
/*     */   {
/* 479 */     for (int i = 0; i < p_73125_1_.size(); i++)
/*     */     {
/* 481 */       updatePlayerEntity((EntityPlayerMP)p_73125_1_.get(i));
/*     */     }
/*     */   }
/*     */   
/*     */   private Packet func_151260_c()
/*     */   {
/* 487 */     if (this.trackedEntity.isDead)
/*     */     {
/* 489 */       logger.warn("Fetching addPacket for removed entity");
/*     */     }
/*     */     
/* 492 */     if ((this.trackedEntity instanceof EntityItem))
/*     */     {
/* 494 */       return new S0EPacketSpawnObject(this.trackedEntity, 2, 1);
/*     */     }
/* 496 */     if ((this.trackedEntity instanceof EntityPlayerMP))
/*     */     {
/* 498 */       return new S0CPacketSpawnPlayer((EntityPlayer)this.trackedEntity);
/*     */     }
/* 500 */     if ((this.trackedEntity instanceof EntityMinecart))
/*     */     {
/* 502 */       EntityMinecart entityminecart = (EntityMinecart)this.trackedEntity;
/* 503 */       return new S0EPacketSpawnObject(this.trackedEntity, 10, entityminecart.getMinecartType().getNetworkID());
/*     */     }
/* 505 */     if ((this.trackedEntity instanceof EntityBoat))
/*     */     {
/* 507 */       return new S0EPacketSpawnObject(this.trackedEntity, 1);
/*     */     }
/* 509 */     if ((this.trackedEntity instanceof IAnimals))
/*     */     {
/* 511 */       this.lastHeadMotion = MathHelper.floor_float(this.trackedEntity.getRotationYawHead() * 256.0F / 360.0F);
/* 512 */       return new S0FPacketSpawnMob((EntityLivingBase)this.trackedEntity);
/*     */     }
/* 514 */     if ((this.trackedEntity instanceof EntityFishHook))
/*     */     {
/* 516 */       Entity entity1 = ((EntityFishHook)this.trackedEntity).angler;
/* 517 */       return new S0EPacketSpawnObject(this.trackedEntity, 90, entity1 != null ? entity1.getEntityId() : this.trackedEntity.getEntityId());
/*     */     }
/* 519 */     if ((this.trackedEntity instanceof EntityArrow))
/*     */     {
/* 521 */       Entity entity = ((EntityArrow)this.trackedEntity).shootingEntity;
/* 522 */       return new S0EPacketSpawnObject(this.trackedEntity, 60, entity != null ? entity.getEntityId() : this.trackedEntity.getEntityId());
/*     */     }
/* 524 */     if ((this.trackedEntity instanceof EntitySnowball))
/*     */     {
/* 526 */       return new S0EPacketSpawnObject(this.trackedEntity, 61);
/*     */     }
/* 528 */     if ((this.trackedEntity instanceof EntityPotion))
/*     */     {
/* 530 */       return new S0EPacketSpawnObject(this.trackedEntity, 73, ((EntityPotion)this.trackedEntity).getPotionDamage());
/*     */     }
/* 532 */     if ((this.trackedEntity instanceof EntityExpBottle))
/*     */     {
/* 534 */       return new S0EPacketSpawnObject(this.trackedEntity, 75);
/*     */     }
/* 536 */     if ((this.trackedEntity instanceof EntityEnderPearl))
/*     */     {
/* 538 */       return new S0EPacketSpawnObject(this.trackedEntity, 65);
/*     */     }
/* 540 */     if ((this.trackedEntity instanceof EntityEnderEye))
/*     */     {
/* 542 */       return new S0EPacketSpawnObject(this.trackedEntity, 72);
/*     */     }
/* 544 */     if ((this.trackedEntity instanceof EntityFireworkRocket))
/*     */     {
/* 546 */       return new S0EPacketSpawnObject(this.trackedEntity, 76);
/*     */     }
/* 548 */     if ((this.trackedEntity instanceof EntityFireball))
/*     */     {
/* 550 */       EntityFireball entityfireball = (EntityFireball)this.trackedEntity;
/* 551 */       S0EPacketSpawnObject s0epacketspawnobject2 = null;
/* 552 */       int i = 63;
/*     */       
/* 554 */       if ((this.trackedEntity instanceof EntitySmallFireball))
/*     */       {
/* 556 */         i = 64;
/*     */       }
/* 558 */       else if ((this.trackedEntity instanceof EntityWitherSkull))
/*     */       {
/* 560 */         i = 66;
/*     */       }
/*     */       
/* 563 */       if (entityfireball.shootingEntity != null)
/*     */       {
/* 565 */         s0epacketspawnobject2 = new S0EPacketSpawnObject(this.trackedEntity, i, ((EntityFireball)this.trackedEntity).shootingEntity.getEntityId());
/*     */       }
/*     */       else
/*     */       {
/* 569 */         s0epacketspawnobject2 = new S0EPacketSpawnObject(this.trackedEntity, i, 0);
/*     */       }
/*     */       
/* 572 */       s0epacketspawnobject2.setSpeedX((int)(entityfireball.accelerationX * 8000.0D));
/* 573 */       s0epacketspawnobject2.setSpeedY((int)(entityfireball.accelerationY * 8000.0D));
/* 574 */       s0epacketspawnobject2.setSpeedZ((int)(entityfireball.accelerationZ * 8000.0D));
/* 575 */       return s0epacketspawnobject2;
/*     */     }
/* 577 */     if ((this.trackedEntity instanceof net.minecraft.entity.projectile.EntityEgg))
/*     */     {
/* 579 */       return new S0EPacketSpawnObject(this.trackedEntity, 62);
/*     */     }
/* 581 */     if ((this.trackedEntity instanceof net.minecraft.entity.item.EntityTNTPrimed))
/*     */     {
/* 583 */       return new S0EPacketSpawnObject(this.trackedEntity, 50);
/*     */     }
/* 585 */     if ((this.trackedEntity instanceof EntityEnderCrystal))
/*     */     {
/* 587 */       return new S0EPacketSpawnObject(this.trackedEntity, 51);
/*     */     }
/* 589 */     if ((this.trackedEntity instanceof EntityFallingBlock))
/*     */     {
/* 591 */       EntityFallingBlock entityfallingblock = (EntityFallingBlock)this.trackedEntity;
/* 592 */       return new S0EPacketSpawnObject(this.trackedEntity, 70, Block.getStateId(entityfallingblock.getBlock()));
/*     */     }
/* 594 */     if ((this.trackedEntity instanceof EntityArmorStand))
/*     */     {
/* 596 */       return new S0EPacketSpawnObject(this.trackedEntity, 78);
/*     */     }
/* 598 */     if ((this.trackedEntity instanceof EntityPainting))
/*     */     {
/* 600 */       return new S10PacketSpawnPainting((EntityPainting)this.trackedEntity);
/*     */     }
/* 602 */     if ((this.trackedEntity instanceof EntityItemFrame))
/*     */     {
/* 604 */       EntityItemFrame entityitemframe = (EntityItemFrame)this.trackedEntity;
/* 605 */       S0EPacketSpawnObject s0epacketspawnobject1 = new S0EPacketSpawnObject(this.trackedEntity, 71, entityitemframe.facingDirection.getHorizontalIndex());
/* 606 */       BlockPos blockpos1 = entityitemframe.getHangingPosition();
/* 607 */       s0epacketspawnobject1.setX(MathHelper.floor_float(blockpos1.getX() * 32));
/* 608 */       s0epacketspawnobject1.setY(MathHelper.floor_float(blockpos1.getY() * 32));
/* 609 */       s0epacketspawnobject1.setZ(MathHelper.floor_float(blockpos1.getZ() * 32));
/* 610 */       return s0epacketspawnobject1;
/*     */     }
/* 612 */     if ((this.trackedEntity instanceof EntityLeashKnot))
/*     */     {
/* 614 */       EntityLeashKnot entityleashknot = (EntityLeashKnot)this.trackedEntity;
/* 615 */       S0EPacketSpawnObject s0epacketspawnobject = new S0EPacketSpawnObject(this.trackedEntity, 77);
/* 616 */       BlockPos blockpos = entityleashknot.getHangingPosition();
/* 617 */       s0epacketspawnobject.setX(MathHelper.floor_float(blockpos.getX() * 32));
/* 618 */       s0epacketspawnobject.setY(MathHelper.floor_float(blockpos.getY() * 32));
/* 619 */       s0epacketspawnobject.setZ(MathHelper.floor_float(blockpos.getZ() * 32));
/* 620 */       return s0epacketspawnobject;
/*     */     }
/* 622 */     if ((this.trackedEntity instanceof EntityXPOrb))
/*     */     {
/* 624 */       return new S11PacketSpawnExperienceOrb((EntityXPOrb)this.trackedEntity);
/*     */     }
/*     */     
/*     */ 
/* 628 */     throw new IllegalArgumentException("Don't know how to add " + this.trackedEntity.getClass() + "!");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeTrackedPlayerSymmetric(EntityPlayerMP playerMP)
/*     */   {
/* 637 */     if (this.trackingPlayers.contains(playerMP))
/*     */     {
/* 639 */       this.trackingPlayers.remove(playerMP);
/* 640 */       playerMP.removeEntity(this.trackedEntity);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\EntityTrackerEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */