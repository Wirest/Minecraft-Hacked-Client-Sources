package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.network.Packet;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTracker {
    private static final Logger logger = LogManager.getLogger();
    private final WorldServer theWorld;

    /**
     * List of tracked entities, used for iteration operations on tracked entities.
     */
    private Set trackedEntities = Sets.newHashSet();

    /**
     * Used for identity lookup of tracked entities.
     */
    private IntHashMap trackedEntityHashTable = new IntHashMap();
    private int maxTrackingDistanceThreshold;
    private static final String __OBFID = "CL_00001431";

    public EntityTracker(WorldServer p_i1516_1_) {
        this.theWorld = p_i1516_1_;
        this.maxTrackingDistanceThreshold = p_i1516_1_.func_73046_m().getConfigurationManager().getEntityViewDistance();
    }

    public void trackEntity(Entity p_72786_1_) {
        if (p_72786_1_ instanceof EntityPlayerMP) {
            this.trackEntity(p_72786_1_, 512, 2);
            EntityPlayerMP var2 = (EntityPlayerMP) p_72786_1_;
            Iterator var3 = this.trackedEntities.iterator();

            while (var3.hasNext()) {
                EntityTrackerEntry var4 = (EntityTrackerEntry) var3.next();

                if (var4.trackedEntity != var2) {
                    var4.updatePlayerEntity(var2);
                }
            }
        } else if (p_72786_1_ instanceof EntityFishHook) {
            this.addEntityToTracker(p_72786_1_, 64, 5, true);
        } else if (p_72786_1_ instanceof EntityArrow) {
            this.addEntityToTracker(p_72786_1_, 64, 20, false);
        } else if (p_72786_1_ instanceof EntitySmallFireball) {
            this.addEntityToTracker(p_72786_1_, 64, 10, false);
        } else if (p_72786_1_ instanceof EntityFireball) {
            this.addEntityToTracker(p_72786_1_, 64, 10, false);
        } else if (p_72786_1_ instanceof EntitySnowball) {
            this.addEntityToTracker(p_72786_1_, 64, 10, true);
        } else if (p_72786_1_ instanceof EntityEnderPearl) {
            this.addEntityToTracker(p_72786_1_, 64, 10, true);
        } else if (p_72786_1_ instanceof EntityEnderEye) {
            this.addEntityToTracker(p_72786_1_, 64, 4, true);
        } else if (p_72786_1_ instanceof EntityEgg) {
            this.addEntityToTracker(p_72786_1_, 64, 10, true);
        } else if (p_72786_1_ instanceof EntityPotion) {
            this.addEntityToTracker(p_72786_1_, 64, 10, true);
        } else if (p_72786_1_ instanceof EntityExpBottle) {
            this.addEntityToTracker(p_72786_1_, 64, 10, true);
        } else if (p_72786_1_ instanceof EntityFireworkRocket) {
            this.addEntityToTracker(p_72786_1_, 64, 10, true);
        } else if (p_72786_1_ instanceof EntityItem) {
            this.addEntityToTracker(p_72786_1_, 64, 20, true);
        } else if (p_72786_1_ instanceof EntityMinecart) {
            this.addEntityToTracker(p_72786_1_, 80, 3, true);
        } else if (p_72786_1_ instanceof EntityBoat) {
            this.addEntityToTracker(p_72786_1_, 80, 3, true);
        } else if (p_72786_1_ instanceof EntitySquid) {
            this.addEntityToTracker(p_72786_1_, 64, 3, true);
        } else if (p_72786_1_ instanceof EntityWither) {
            this.addEntityToTracker(p_72786_1_, 80, 3, false);
        } else if (p_72786_1_ instanceof EntityBat) {
            this.addEntityToTracker(p_72786_1_, 80, 3, false);
        } else if (p_72786_1_ instanceof EntityDragon) {
            this.addEntityToTracker(p_72786_1_, 160, 3, true);
        } else if (p_72786_1_ instanceof IAnimals) {
            this.addEntityToTracker(p_72786_1_, 80, 3, true);
        } else if (p_72786_1_ instanceof EntityTNTPrimed) {
            this.addEntityToTracker(p_72786_1_, 160, 10, true);
        } else if (p_72786_1_ instanceof EntityFallingBlock) {
            this.addEntityToTracker(p_72786_1_, 160, 20, true);
        } else if (p_72786_1_ instanceof EntityHanging) {
            this.addEntityToTracker(p_72786_1_, 160, Integer.MAX_VALUE, false);
        } else if (p_72786_1_ instanceof EntityArmorStand) {
            this.addEntityToTracker(p_72786_1_, 160, 3, true);
        } else if (p_72786_1_ instanceof EntityXPOrb) {
            this.addEntityToTracker(p_72786_1_, 160, 20, true);
        } else if (p_72786_1_ instanceof EntityEnderCrystal) {
            this.addEntityToTracker(p_72786_1_, 256, Integer.MAX_VALUE, false);
        }
    }

    public void trackEntity(Entity p_72791_1_, int p_72791_2_, int p_72791_3_) {
        this.addEntityToTracker(p_72791_1_, p_72791_2_, p_72791_3_, false);
    }

    /**
     * Args : Entity, trackingRange, updateFrequency, sendVelocityUpdates
     */
    public void addEntityToTracker(Entity p_72785_1_, int p_72785_2_, final int p_72785_3_, boolean p_72785_4_) {
        if (p_72785_2_ > this.maxTrackingDistanceThreshold) {
            p_72785_2_ = this.maxTrackingDistanceThreshold;
        }

        try {
            if (this.trackedEntityHashTable.containsItem(p_72785_1_.getEntityId())) {
                throw new IllegalStateException("Entity is already tracked!");
            }

            EntityTrackerEntry var5 = new EntityTrackerEntry(p_72785_1_, p_72785_2_, p_72785_3_, p_72785_4_);
            this.trackedEntities.add(var5);
            this.trackedEntityHashTable.addKey(p_72785_1_.getEntityId(), var5);
            var5.updatePlayerEntities(this.theWorld.playerEntities);
        } catch (Throwable var11) {
            CrashReport var6 = CrashReport.makeCrashReport(var11, "Adding entity to track");
            CrashReportCategory var7 = var6.makeCategory("Entity To Track");
            var7.addCrashSection("Tracking range", p_72785_2_ + " blocks");
            var7.addCrashSectionCallable("Update interval", new Callable() {
                private static final String __OBFID = "CL_00001432";

                public String call() {
                    String var1 = "Once per " + p_72785_3_ + " ticks";

                    if (p_72785_3_ == Integer.MAX_VALUE) {
                        var1 = "Maximum (" + var1 + ")";
                    }

                    return var1;
                }
            });
            p_72785_1_.addEntityCrashInfo(var7);
            CrashReportCategory var8 = var6.makeCategory("Entity That Is Already Tracked");
            ((EntityTrackerEntry) this.trackedEntityHashTable.lookup(p_72785_1_.getEntityId())).trackedEntity.addEntityCrashInfo(var8);

            try {
                throw new ReportedException(var6);
            } catch (ReportedException var10) {
                logger.error("\"Silently\" catching entity tracking error.", var10);
            }
        }
    }

    public void untrackEntity(Entity p_72790_1_) {
        if (p_72790_1_ instanceof EntityPlayerMP) {
            EntityPlayerMP var2 = (EntityPlayerMP) p_72790_1_;
            Iterator var3 = this.trackedEntities.iterator();

            while (var3.hasNext()) {
                EntityTrackerEntry var4 = (EntityTrackerEntry) var3.next();
                var4.removeFromTrackedPlayers(var2);
            }
        }

        EntityTrackerEntry var5 = (EntityTrackerEntry) this.trackedEntityHashTable.removeObject(p_72790_1_.getEntityId());

        if (var5 != null) {
            this.trackedEntities.remove(var5);
            var5.sendDestroyEntityPacketToTrackedPlayers();
        }
    }

    public void updateTrackedEntities() {
        ArrayList var1 = Lists.newArrayList();
        Iterator var2 = this.trackedEntities.iterator();

        while (var2.hasNext()) {
            EntityTrackerEntry var3 = (EntityTrackerEntry) var2.next();
            var3.updatePlayerList(this.theWorld.playerEntities);

            if (var3.playerEntitiesUpdated && var3.trackedEntity instanceof EntityPlayerMP) {
                var1.add((EntityPlayerMP) var3.trackedEntity);
            }
        }

        for (int var6 = 0; var6 < var1.size(); ++var6) {
            EntityPlayerMP var7 = (EntityPlayerMP) var1.get(var6);
            Iterator var4 = this.trackedEntities.iterator();

            while (var4.hasNext()) {
                EntityTrackerEntry var5 = (EntityTrackerEntry) var4.next();

                if (var5.trackedEntity != var7) {
                    var5.updatePlayerEntity(var7);
                }
            }
        }
    }

    public void func_180245_a(EntityPlayerMP p_180245_1_) {
        Iterator var2 = this.trackedEntities.iterator();

        while (var2.hasNext()) {
            EntityTrackerEntry var3 = (EntityTrackerEntry) var2.next();

            if (var3.trackedEntity == p_180245_1_) {
                var3.updatePlayerEntities(this.theWorld.playerEntities);
            } else {
                var3.updatePlayerEntity(p_180245_1_);
            }
        }
    }

    public void sendToAllTrackingEntity(Entity p_151247_1_, Packet p_151247_2_) {
        EntityTrackerEntry var3 = (EntityTrackerEntry) this.trackedEntityHashTable.lookup(p_151247_1_.getEntityId());

        if (var3 != null) {
            var3.func_151259_a(p_151247_2_);
        }
    }

    public void func_151248_b(Entity p_151248_1_, Packet p_151248_2_) {
        EntityTrackerEntry var3 = (EntityTrackerEntry) this.trackedEntityHashTable.lookup(p_151248_1_.getEntityId());

        if (var3 != null) {
            var3.func_151261_b(p_151248_2_);
        }
    }

    public void removePlayerFromTrackers(EntityPlayerMP p_72787_1_) {
        Iterator var2 = this.trackedEntities.iterator();

        while (var2.hasNext()) {
            EntityTrackerEntry var3 = (EntityTrackerEntry) var2.next();
            var3.removeTrackedPlayerSymmetric(p_72787_1_);
        }
    }

    public void func_85172_a(EntityPlayerMP p_85172_1_, Chunk p_85172_2_) {
        Iterator var3 = this.trackedEntities.iterator();

        while (var3.hasNext()) {
            EntityTrackerEntry var4 = (EntityTrackerEntry) var3.next();

            if (var4.trackedEntity != p_85172_1_ && var4.trackedEntity.chunkCoordX == p_85172_2_.xPosition && var4.trackedEntity.chunkCoordZ == p_85172_2_.zPosition) {
                var4.updatePlayerEntity(p_85172_1_);
            }
        }
    }
}
