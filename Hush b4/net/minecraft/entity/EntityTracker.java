// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.network.Packet;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import java.util.Iterator;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.IntHashMap;
import java.util.Set;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.Logger;

public class EntityTracker
{
    private static final Logger logger;
    private final WorldServer theWorld;
    private Set<EntityTrackerEntry> trackedEntities;
    private IntHashMap<EntityTrackerEntry> trackedEntityHashTable;
    private int maxTrackingDistanceThreshold;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public EntityTracker(final WorldServer theWorldIn) {
        this.trackedEntities = (Set<EntityTrackerEntry>)Sets.newHashSet();
        this.trackedEntityHashTable = new IntHashMap<EntityTrackerEntry>();
        this.theWorld = theWorldIn;
        this.maxTrackingDistanceThreshold = theWorldIn.getMinecraftServer().getConfigurationManager().getEntityViewDistance();
    }
    
    public void trackEntity(final Entity p_72786_1_) {
        if (p_72786_1_ instanceof EntityPlayerMP) {
            this.trackEntity(p_72786_1_, 512, 2);
            final EntityPlayerMP entityplayermp = (EntityPlayerMP)p_72786_1_;
            for (final EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
                if (entitytrackerentry.trackedEntity != entityplayermp) {
                    entitytrackerentry.updatePlayerEntity(entityplayermp);
                }
            }
        }
        else if (p_72786_1_ instanceof EntityFishHook) {
            this.addEntityToTracker(p_72786_1_, 64, 5, true);
        }
        else if (p_72786_1_ instanceof EntityArrow) {
            this.addEntityToTracker(p_72786_1_, 64, 20, false);
        }
        else if (p_72786_1_ instanceof EntitySmallFireball) {
            this.addEntityToTracker(p_72786_1_, 64, 10, false);
        }
        else if (p_72786_1_ instanceof EntityFireball) {
            this.addEntityToTracker(p_72786_1_, 64, 10, false);
        }
        else if (p_72786_1_ instanceof EntitySnowball) {
            this.addEntityToTracker(p_72786_1_, 64, 10, true);
        }
        else if (p_72786_1_ instanceof EntityEnderPearl) {
            this.addEntityToTracker(p_72786_1_, 64, 10, true);
        }
        else if (p_72786_1_ instanceof EntityEnderEye) {
            this.addEntityToTracker(p_72786_1_, 64, 4, true);
        }
        else if (p_72786_1_ instanceof EntityEgg) {
            this.addEntityToTracker(p_72786_1_, 64, 10, true);
        }
        else if (p_72786_1_ instanceof EntityPotion) {
            this.addEntityToTracker(p_72786_1_, 64, 10, true);
        }
        else if (p_72786_1_ instanceof EntityExpBottle) {
            this.addEntityToTracker(p_72786_1_, 64, 10, true);
        }
        else if (p_72786_1_ instanceof EntityFireworkRocket) {
            this.addEntityToTracker(p_72786_1_, 64, 10, true);
        }
        else if (p_72786_1_ instanceof EntityItem) {
            this.addEntityToTracker(p_72786_1_, 64, 20, true);
        }
        else if (p_72786_1_ instanceof EntityMinecart) {
            this.addEntityToTracker(p_72786_1_, 80, 3, true);
        }
        else if (p_72786_1_ instanceof EntityBoat) {
            this.addEntityToTracker(p_72786_1_, 80, 3, true);
        }
        else if (p_72786_1_ instanceof EntitySquid) {
            this.addEntityToTracker(p_72786_1_, 64, 3, true);
        }
        else if (p_72786_1_ instanceof EntityWither) {
            this.addEntityToTracker(p_72786_1_, 80, 3, false);
        }
        else if (p_72786_1_ instanceof EntityBat) {
            this.addEntityToTracker(p_72786_1_, 80, 3, false);
        }
        else if (p_72786_1_ instanceof EntityDragon) {
            this.addEntityToTracker(p_72786_1_, 160, 3, true);
        }
        else if (p_72786_1_ instanceof IAnimals) {
            this.addEntityToTracker(p_72786_1_, 80, 3, true);
        }
        else if (p_72786_1_ instanceof EntityTNTPrimed) {
            this.addEntityToTracker(p_72786_1_, 160, 10, true);
        }
        else if (p_72786_1_ instanceof EntityFallingBlock) {
            this.addEntityToTracker(p_72786_1_, 160, 20, true);
        }
        else if (p_72786_1_ instanceof EntityHanging) {
            this.addEntityToTracker(p_72786_1_, 160, Integer.MAX_VALUE, false);
        }
        else if (p_72786_1_ instanceof EntityArmorStand) {
            this.addEntityToTracker(p_72786_1_, 160, 3, true);
        }
        else if (p_72786_1_ instanceof EntityXPOrb) {
            this.addEntityToTracker(p_72786_1_, 160, 20, true);
        }
        else if (p_72786_1_ instanceof EntityEnderCrystal) {
            this.addEntityToTracker(p_72786_1_, 256, Integer.MAX_VALUE, false);
        }
    }
    
    public void trackEntity(final Entity entityIn, final int trackingRange, final int updateFrequency) {
        this.addEntityToTracker(entityIn, trackingRange, updateFrequency, false);
    }
    
    public void addEntityToTracker(final Entity entityIn, int trackingRange, final int updateFrequency, final boolean sendVelocityUpdates) {
        if (trackingRange > this.maxTrackingDistanceThreshold) {
            trackingRange = this.maxTrackingDistanceThreshold;
        }
        try {
            if (this.trackedEntityHashTable.containsItem(entityIn.getEntityId())) {
                throw new IllegalStateException("Entity is already tracked!");
            }
            final EntityTrackerEntry entitytrackerentry = new EntityTrackerEntry(entityIn, trackingRange, updateFrequency, sendVelocityUpdates);
            this.trackedEntities.add(entitytrackerentry);
            this.trackedEntityHashTable.addKey(entityIn.getEntityId(), entitytrackerentry);
            entitytrackerentry.updatePlayerEntities(this.theWorld.playerEntities);
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding entity to track");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity To Track");
            crashreportcategory.addCrashSection("Tracking range", String.valueOf(trackingRange) + " blocks");
            crashreportcategory.addCrashSectionCallable("Update interval", new Callable<String>() {
                @Override
                public String call() throws Exception {
                    String s = "Once per " + updateFrequency + " ticks";
                    if (updateFrequency == Integer.MAX_VALUE) {
                        s = "Maximum (" + s + ")";
                    }
                    return s;
                }
            });
            entityIn.addEntityCrashInfo(crashreportcategory);
            final CrashReportCategory crashreportcategory2 = crashreport.makeCategory("Entity That Is Already Tracked");
            this.trackedEntityHashTable.lookup(entityIn.getEntityId()).trackedEntity.addEntityCrashInfo(crashreportcategory2);
            try {
                throw new ReportedException(crashreport);
            }
            catch (ReportedException reportedexception) {
                EntityTracker.logger.error("\"Silently\" catching entity tracking error.", reportedexception);
            }
        }
    }
    
    public void untrackEntity(final Entity entityIn) {
        if (entityIn instanceof EntityPlayerMP) {
            final EntityPlayerMP entityplayermp = (EntityPlayerMP)entityIn;
            for (final EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
                entitytrackerentry.removeFromTrackedPlayers(entityplayermp);
            }
        }
        final EntityTrackerEntry entitytrackerentry2 = this.trackedEntityHashTable.removeObject(entityIn.getEntityId());
        if (entitytrackerentry2 != null) {
            this.trackedEntities.remove(entitytrackerentry2);
            entitytrackerentry2.sendDestroyEntityPacketToTrackedPlayers();
        }
    }
    
    public void updateTrackedEntities() {
        final List<EntityPlayerMP> list = (List<EntityPlayerMP>)Lists.newArrayList();
        for (final EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
            entitytrackerentry.updatePlayerList(this.theWorld.playerEntities);
            if (entitytrackerentry.playerEntitiesUpdated && entitytrackerentry.trackedEntity instanceof EntityPlayerMP) {
                list.add((EntityPlayerMP)entitytrackerentry.trackedEntity);
            }
        }
        for (int i = 0; i < list.size(); ++i) {
            final EntityPlayerMP entityplayermp = list.get(i);
            for (final EntityTrackerEntry entitytrackerentry2 : this.trackedEntities) {
                if (entitytrackerentry2.trackedEntity != entityplayermp) {
                    entitytrackerentry2.updatePlayerEntity(entityplayermp);
                }
            }
        }
    }
    
    public void func_180245_a(final EntityPlayerMP p_180245_1_) {
        for (final EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
            if (entitytrackerentry.trackedEntity == p_180245_1_) {
                entitytrackerentry.updatePlayerEntities(this.theWorld.playerEntities);
            }
            else {
                entitytrackerentry.updatePlayerEntity(p_180245_1_);
            }
        }
    }
    
    public void sendToAllTrackingEntity(final Entity entityIn, final Packet p_151247_2_) {
        final EntityTrackerEntry entitytrackerentry = this.trackedEntityHashTable.lookup(entityIn.getEntityId());
        if (entitytrackerentry != null) {
            entitytrackerentry.sendPacketToTrackedPlayers(p_151247_2_);
        }
    }
    
    public void func_151248_b(final Entity entityIn, final Packet p_151248_2_) {
        final EntityTrackerEntry entitytrackerentry = this.trackedEntityHashTable.lookup(entityIn.getEntityId());
        if (entitytrackerentry != null) {
            entitytrackerentry.func_151261_b(p_151248_2_);
        }
    }
    
    public void removePlayerFromTrackers(final EntityPlayerMP p_72787_1_) {
        for (final EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
            entitytrackerentry.removeTrackedPlayerSymmetric(p_72787_1_);
        }
    }
    
    public void func_85172_a(final EntityPlayerMP p_85172_1_, final Chunk p_85172_2_) {
        for (final EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
            if (entitytrackerentry.trackedEntity != p_85172_1_ && entitytrackerentry.trackedEntity.chunkCoordX == p_85172_2_.xPosition && entitytrackerentry.trackedEntity.chunkCoordZ == p_85172_2_.zPosition) {
                entitytrackerentry.updatePlayerEntity(p_85172_1_);
            }
        }
    }
}
