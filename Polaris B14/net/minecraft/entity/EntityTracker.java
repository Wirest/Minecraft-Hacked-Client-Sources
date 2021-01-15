/*     */ package net.minecraft.entity;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.entity.boss.EntityDragon;
/*     */ import net.minecraft.entity.boss.EntityWither;
/*     */ import net.minecraft.entity.item.EntityArmorStand;
/*     */ import net.minecraft.entity.item.EntityBoat;
/*     */ import net.minecraft.entity.item.EntityEnderCrystal;
/*     */ import net.minecraft.entity.item.EntityEnderEye;
/*     */ import net.minecraft.entity.item.EntityEnderPearl;
/*     */ import net.minecraft.entity.item.EntityExpBottle;
/*     */ import net.minecraft.entity.item.EntityFallingBlock;
/*     */ import net.minecraft.entity.item.EntityFireworkRocket;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.item.EntityMinecart;
/*     */ import net.minecraft.entity.item.EntityTNTPrimed;
/*     */ import net.minecraft.entity.item.EntityXPOrb;
/*     */ import net.minecraft.entity.passive.EntityBat;
/*     */ import net.minecraft.entity.passive.EntitySquid;
/*     */ import net.minecraft.entity.passive.IAnimals;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.entity.projectile.EntityFireball;
/*     */ import net.minecraft.entity.projectile.EntityFishHook;
/*     */ import net.minecraft.entity.projectile.EntityPotion;
/*     */ import net.minecraft.entity.projectile.EntitySmallFireball;
/*     */ import net.minecraft.entity.projectile.EntitySnowball;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.ServerConfigurationManager;
/*     */ import net.minecraft.util.IntHashMap;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.world.WorldServer;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class EntityTracker
/*     */ {
/*  45 */   private static final Logger logger = ;
/*     */   private final WorldServer theWorld;
/*  47 */   private Set<EntityTrackerEntry> trackedEntities = Sets.newHashSet();
/*  48 */   private IntHashMap<EntityTrackerEntry> trackedEntityHashTable = new IntHashMap();
/*     */   private int maxTrackingDistanceThreshold;
/*     */   
/*     */   public EntityTracker(WorldServer theWorldIn)
/*     */   {
/*  53 */     this.theWorld = theWorldIn;
/*  54 */     this.maxTrackingDistanceThreshold = theWorldIn.getMinecraftServer().getConfigurationManager().getEntityViewDistance();
/*     */   }
/*     */   
/*     */   public void trackEntity(Entity p_72786_1_)
/*     */   {
/*  59 */     if ((p_72786_1_ instanceof EntityPlayerMP))
/*     */     {
/*  61 */       trackEntity(p_72786_1_, 512, 2);
/*  62 */       EntityPlayerMP entityplayermp = (EntityPlayerMP)p_72786_1_;
/*     */       
/*  64 */       for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
/*     */       {
/*  66 */         if (entitytrackerentry.trackedEntity != entityplayermp)
/*     */         {
/*  68 */           entitytrackerentry.updatePlayerEntity(entityplayermp);
/*     */         }
/*     */       }
/*     */     }
/*  72 */     else if ((p_72786_1_ instanceof EntityFishHook))
/*     */     {
/*  74 */       addEntityToTracker(p_72786_1_, 64, 5, true);
/*     */     }
/*  76 */     else if ((p_72786_1_ instanceof EntityArrow))
/*     */     {
/*  78 */       addEntityToTracker(p_72786_1_, 64, 20, false);
/*     */     }
/*  80 */     else if ((p_72786_1_ instanceof EntitySmallFireball))
/*     */     {
/*  82 */       addEntityToTracker(p_72786_1_, 64, 10, false);
/*     */     }
/*  84 */     else if ((p_72786_1_ instanceof EntityFireball))
/*     */     {
/*  86 */       addEntityToTracker(p_72786_1_, 64, 10, false);
/*     */     }
/*  88 */     else if ((p_72786_1_ instanceof EntitySnowball))
/*     */     {
/*  90 */       addEntityToTracker(p_72786_1_, 64, 10, true);
/*     */     }
/*  92 */     else if ((p_72786_1_ instanceof EntityEnderPearl))
/*     */     {
/*  94 */       addEntityToTracker(p_72786_1_, 64, 10, true);
/*     */     }
/*  96 */     else if ((p_72786_1_ instanceof EntityEnderEye))
/*     */     {
/*  98 */       addEntityToTracker(p_72786_1_, 64, 4, true);
/*     */     }
/* 100 */     else if ((p_72786_1_ instanceof net.minecraft.entity.projectile.EntityEgg))
/*     */     {
/* 102 */       addEntityToTracker(p_72786_1_, 64, 10, true);
/*     */     }
/* 104 */     else if ((p_72786_1_ instanceof EntityPotion))
/*     */     {
/* 106 */       addEntityToTracker(p_72786_1_, 64, 10, true);
/*     */     }
/* 108 */     else if ((p_72786_1_ instanceof EntityExpBottle))
/*     */     {
/* 110 */       addEntityToTracker(p_72786_1_, 64, 10, true);
/*     */     }
/* 112 */     else if ((p_72786_1_ instanceof EntityFireworkRocket))
/*     */     {
/* 114 */       addEntityToTracker(p_72786_1_, 64, 10, true);
/*     */     }
/* 116 */     else if ((p_72786_1_ instanceof EntityItem))
/*     */     {
/* 118 */       addEntityToTracker(p_72786_1_, 64, 20, true);
/*     */     }
/* 120 */     else if ((p_72786_1_ instanceof EntityMinecart))
/*     */     {
/* 122 */       addEntityToTracker(p_72786_1_, 80, 3, true);
/*     */     }
/* 124 */     else if ((p_72786_1_ instanceof EntityBoat))
/*     */     {
/* 126 */       addEntityToTracker(p_72786_1_, 80, 3, true);
/*     */     }
/* 128 */     else if ((p_72786_1_ instanceof EntitySquid))
/*     */     {
/* 130 */       addEntityToTracker(p_72786_1_, 64, 3, true);
/*     */     }
/* 132 */     else if ((p_72786_1_ instanceof EntityWither))
/*     */     {
/* 134 */       addEntityToTracker(p_72786_1_, 80, 3, false);
/*     */     }
/* 136 */     else if ((p_72786_1_ instanceof EntityBat))
/*     */     {
/* 138 */       addEntityToTracker(p_72786_1_, 80, 3, false);
/*     */     }
/* 140 */     else if ((p_72786_1_ instanceof EntityDragon))
/*     */     {
/* 142 */       addEntityToTracker(p_72786_1_, 160, 3, true);
/*     */     }
/* 144 */     else if ((p_72786_1_ instanceof IAnimals))
/*     */     {
/* 146 */       addEntityToTracker(p_72786_1_, 80, 3, true);
/*     */     }
/* 148 */     else if ((p_72786_1_ instanceof EntityTNTPrimed))
/*     */     {
/* 150 */       addEntityToTracker(p_72786_1_, 160, 10, true);
/*     */     }
/* 152 */     else if ((p_72786_1_ instanceof EntityFallingBlock))
/*     */     {
/* 154 */       addEntityToTracker(p_72786_1_, 160, 20, true);
/*     */     }
/* 156 */     else if ((p_72786_1_ instanceof EntityHanging))
/*     */     {
/* 158 */       addEntityToTracker(p_72786_1_, 160, Integer.MAX_VALUE, false);
/*     */     }
/* 160 */     else if ((p_72786_1_ instanceof EntityArmorStand))
/*     */     {
/* 162 */       addEntityToTracker(p_72786_1_, 160, 3, true);
/*     */     }
/* 164 */     else if ((p_72786_1_ instanceof EntityXPOrb))
/*     */     {
/* 166 */       addEntityToTracker(p_72786_1_, 160, 20, true);
/*     */     }
/* 168 */     else if ((p_72786_1_ instanceof EntityEnderCrystal))
/*     */     {
/* 170 */       addEntityToTracker(p_72786_1_, 256, Integer.MAX_VALUE, false);
/*     */     }
/*     */   }
/*     */   
/*     */   public void trackEntity(Entity entityIn, int trackingRange, int updateFrequency)
/*     */   {
/* 176 */     addEntityToTracker(entityIn, trackingRange, updateFrequency, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addEntityToTracker(Entity entityIn, int trackingRange, final int updateFrequency, boolean sendVelocityUpdates)
/*     */   {
/* 184 */     if (trackingRange > this.maxTrackingDistanceThreshold)
/*     */     {
/* 186 */       trackingRange = this.maxTrackingDistanceThreshold;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 191 */       if (this.trackedEntityHashTable.containsItem(entityIn.getEntityId()))
/*     */       {
/* 193 */         throw new IllegalStateException("Entity is already tracked!");
/*     */       }
/*     */       
/* 196 */       EntityTrackerEntry entitytrackerentry = new EntityTrackerEntry(entityIn, trackingRange, updateFrequency, sendVelocityUpdates);
/* 197 */       this.trackedEntities.add(entitytrackerentry);
/* 198 */       this.trackedEntityHashTable.addKey(entityIn.getEntityId(), entitytrackerentry);
/* 199 */       entitytrackerentry.updatePlayerEntities(this.theWorld.playerEntities);
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/* 203 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding entity to track");
/* 204 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity To Track");
/* 205 */       crashreportcategory.addCrashSection("Tracking range", trackingRange + " blocks");
/* 206 */       crashreportcategory.addCrashSectionCallable("Update interval", new Callable()
/*     */       {
/*     */         public String call() throws Exception
/*     */         {
/* 210 */           String s = "Once per " + updateFrequency + " ticks";
/*     */           
/* 212 */           if (updateFrequency == Integer.MAX_VALUE)
/*     */           {
/* 214 */             s = "Maximum (" + s + ")";
/*     */           }
/*     */           
/* 217 */           return s;
/*     */         }
/* 219 */       });
/* 220 */       entityIn.addEntityCrashInfo(crashreportcategory);
/* 221 */       CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Entity That Is Already Tracked");
/* 222 */       ((EntityTrackerEntry)this.trackedEntityHashTable.lookup(entityIn.getEntityId())).trackedEntity.addEntityCrashInfo(crashreportcategory1);
/*     */       
/*     */       try
/*     */       {
/* 226 */         throw new ReportedException(crashreport);
/*     */       }
/*     */       catch (ReportedException reportedexception)
/*     */       {
/* 230 */         logger.error("\"Silently\" catching entity tracking error.", reportedexception);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void untrackEntity(Entity entityIn)
/*     */   {
/* 237 */     if ((entityIn instanceof EntityPlayerMP))
/*     */     {
/* 239 */       EntityPlayerMP entityplayermp = (EntityPlayerMP)entityIn;
/*     */       
/* 241 */       for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
/*     */       {
/* 243 */         entitytrackerentry.removeFromTrackedPlayers(entityplayermp);
/*     */       }
/*     */     }
/*     */     
/* 247 */     EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry)this.trackedEntityHashTable.removeObject(entityIn.getEntityId());
/*     */     
/* 249 */     if (entitytrackerentry1 != null)
/*     */     {
/* 251 */       this.trackedEntities.remove(entitytrackerentry1);
/* 252 */       entitytrackerentry1.sendDestroyEntityPacketToTrackedPlayers();
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateTrackedEntities()
/*     */   {
/* 258 */     List<EntityPlayerMP> list = com.google.common.collect.Lists.newArrayList();
/*     */     
/* 260 */     for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
/*     */     {
/* 262 */       entitytrackerentry.updatePlayerList(this.theWorld.playerEntities);
/*     */       
/* 264 */       if ((entitytrackerentry.playerEntitiesUpdated) && ((entitytrackerentry.trackedEntity instanceof EntityPlayerMP)))
/*     */       {
/* 266 */         list.add((EntityPlayerMP)entitytrackerentry.trackedEntity);
/*     */       }
/*     */     }
/*     */     
/* 270 */     for (int i = 0; i < list.size(); i++)
/*     */     {
/* 272 */       EntityPlayerMP entityplayermp = (EntityPlayerMP)list.get(i);
/*     */       
/* 274 */       for (EntityTrackerEntry entitytrackerentry1 : this.trackedEntities)
/*     */       {
/* 276 */         if (entitytrackerentry1.trackedEntity != entityplayermp)
/*     */         {
/* 278 */           entitytrackerentry1.updatePlayerEntity(entityplayermp);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_180245_a(EntityPlayerMP p_180245_1_)
/*     */   {
/* 286 */     for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
/*     */     {
/* 288 */       if (entitytrackerentry.trackedEntity == p_180245_1_)
/*     */       {
/* 290 */         entitytrackerentry.updatePlayerEntities(this.theWorld.playerEntities);
/*     */       }
/*     */       else
/*     */       {
/* 294 */         entitytrackerentry.updatePlayerEntity(p_180245_1_);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendToAllTrackingEntity(Entity entityIn, Packet p_151247_2_)
/*     */   {
/* 301 */     EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)this.trackedEntityHashTable.lookup(entityIn.getEntityId());
/*     */     
/* 303 */     if (entitytrackerentry != null)
/*     */     {
/* 305 */       entitytrackerentry.sendPacketToTrackedPlayers(p_151247_2_);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_151248_b(Entity entityIn, Packet p_151248_2_)
/*     */   {
/* 311 */     EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)this.trackedEntityHashTable.lookup(entityIn.getEntityId());
/*     */     
/* 313 */     if (entitytrackerentry != null)
/*     */     {
/* 315 */       entitytrackerentry.func_151261_b(p_151248_2_);
/*     */     }
/*     */   }
/*     */   
/*     */   public void removePlayerFromTrackers(EntityPlayerMP p_72787_1_)
/*     */   {
/* 321 */     for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
/*     */     {
/* 323 */       entitytrackerentry.removeTrackedPlayerSymmetric(p_72787_1_);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_85172_a(EntityPlayerMP p_85172_1_, Chunk p_85172_2_)
/*     */   {
/* 329 */     for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
/*     */     {
/* 331 */       if ((entitytrackerentry.trackedEntity != p_85172_1_) && (entitytrackerentry.trackedEntity.chunkCoordX == p_85172_2_.xPosition) && (entitytrackerentry.trackedEntity.chunkCoordZ == p_85172_2_.zPosition))
/*     */       {
/* 333 */         entitytrackerentry.updatePlayerEntity(p_85172_1_);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\EntityTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */