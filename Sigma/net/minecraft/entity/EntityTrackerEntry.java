package net.minecraft.entity;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.storage.MapData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTrackerEntry {
    private static final Logger logger = LogManager.getLogger();

    /**
     * The entity that this EntityTrackerEntry tracks.
     */
    public Entity trackedEntity;
    public int trackingDistanceThreshold;

    /**
     * check for sync when ticks % updateFrequency==0
     */
    public int updateFrequency;

    /**
     * The encoded entity X position.
     */
    public int encodedPosX;

    /**
     * The encoded entity Y position.
     */
    public int encodedPosY;

    /**
     * The encoded entity Z position.
     */
    public int encodedPosZ;

    /**
     * The encoded entity yaw rotation.
     */
    public int encodedRotationYaw;

    /**
     * The encoded entity pitch rotation.
     */
    public int encodedRotationPitch;
    public int lastHeadMotion;
    public double lastTrackedEntityMotionX;
    public double lastTrackedEntityMotionY;
    public double motionZ;
    public int updateCounter;
    private double lastTrackedEntityPosX;
    private double lastTrackedEntityPosY;
    private double lastTrackedEntityPosZ;
    private boolean firstUpdateDone;
    private boolean sendVelocityUpdates;

    /**
     * every 400 ticks a full teleport packet is sent, rather than just a
     * "move me +x" command, so that position remains fully synced.
     */
    private int ticksSinceLastForcedTeleport;
    private Entity field_85178_v;
    private boolean ridingEntity;
    private boolean field_180234_y;
    public boolean playerEntitiesUpdated;

    /**
     * Holds references to all the players that are currently receiving position
     * updates for this entity.
     */
    public Set trackingPlayers = Sets.newHashSet();
    private static final String __OBFID = "CL_00001443";

    public EntityTrackerEntry(Entity p_i1525_1_, int p_i1525_2_, int p_i1525_3_, boolean p_i1525_4_) {
        trackedEntity = p_i1525_1_;
        trackingDistanceThreshold = p_i1525_2_;
        updateFrequency = p_i1525_3_;
        sendVelocityUpdates = p_i1525_4_;
        encodedPosX = MathHelper.floor_double(p_i1525_1_.posX * 32.0D);
        encodedPosY = MathHelper.floor_double(p_i1525_1_.posY * 32.0D);
        encodedPosZ = MathHelper.floor_double(p_i1525_1_.posZ * 32.0D);
        encodedRotationYaw = MathHelper.floor_float(p_i1525_1_.rotationYaw * 256.0F / 360.0F);
        encodedRotationPitch = MathHelper.floor_float(p_i1525_1_.rotationPitch * 256.0F / 360.0F);
        lastHeadMotion = MathHelper.floor_float(p_i1525_1_.getRotationYawHead() * 256.0F / 360.0F);
        field_180234_y = p_i1525_1_.onGround;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        return p_equals_1_ instanceof EntityTrackerEntry ? ((EntityTrackerEntry) p_equals_1_).trackedEntity.getEntityId() == trackedEntity.getEntityId() : false;
    }

    @Override
    public int hashCode() {
        return trackedEntity.getEntityId();
    }

    public void updatePlayerList(List p_73122_1_) {
        playerEntitiesUpdated = false;

        if (!firstUpdateDone || trackedEntity.getDistanceSq(lastTrackedEntityPosX, lastTrackedEntityPosY, lastTrackedEntityPosZ) > 16.0D) {
            lastTrackedEntityPosX = trackedEntity.posX;
            lastTrackedEntityPosY = trackedEntity.posY;
            lastTrackedEntityPosZ = trackedEntity.posZ;
            firstUpdateDone = true;
            playerEntitiesUpdated = true;
            updatePlayerEntities(p_73122_1_);
        }

        if (field_85178_v != trackedEntity.ridingEntity || trackedEntity.ridingEntity != null && updateCounter % 60 == 0) {
            field_85178_v = trackedEntity.ridingEntity;
            func_151259_a(new S1BPacketEntityAttach(0, trackedEntity, trackedEntity.ridingEntity));
        }

        if (trackedEntity instanceof EntityItemFrame && updateCounter % 10 == 0) {
            EntityItemFrame var2 = (EntityItemFrame) trackedEntity;
            ItemStack var3 = var2.getDisplayedItem();

            if (var3 != null && var3.getItem() instanceof ItemMap) {
                MapData var4 = Items.filled_map.getMapData(var3, trackedEntity.worldObj);
                Iterator var5 = p_73122_1_.iterator();

                while (var5.hasNext()) {
                    EntityPlayer var6 = (EntityPlayer) var5.next();
                    EntityPlayerMP var7 = (EntityPlayerMP) var6;
                    var4.updateVisiblePlayers(var7, var3);
                    Packet var8 = Items.filled_map.createMapDataPacket(var3, trackedEntity.worldObj, var7);

                    if (var8 != null) {
                        var7.playerNetServerHandler.sendPacket(var8);
                    }
                }
            }

            sendMetadataToAllAssociatedPlayers();
        }

        if (updateCounter % updateFrequency == 0 || trackedEntity.isAirBorne || trackedEntity.getDataWatcher().hasObjectChanged()) {
            int var23;
            int var24;

            if (trackedEntity.ridingEntity == null) {
                ++ticksSinceLastForcedTeleport;
                var23 = MathHelper.floor_double(trackedEntity.posX * 32.0D);
                var24 = MathHelper.floor_double(trackedEntity.posY * 32.0D);
                int var25 = MathHelper.floor_double(trackedEntity.posZ * 32.0D);
                int var27 = MathHelper.floor_float(trackedEntity.rotationYaw * 256.0F / 360.0F);
                int var28 = MathHelper.floor_float(trackedEntity.rotationPitch * 256.0F / 360.0F);
                int var29 = var23 - encodedPosX;
                int var30 = var24 - encodedPosY;
                int var9 = var25 - encodedPosZ;
                Object var10 = null;
                boolean var11 = Math.abs(var29) >= 4 || Math.abs(var30) >= 4 || Math.abs(var9) >= 4 || updateCounter % 60 == 0;
                boolean var12 = Math.abs(var27 - encodedRotationYaw) >= 4 || Math.abs(var28 - encodedRotationPitch) >= 4;

                if (updateCounter > 0 || trackedEntity instanceof EntityArrow) {
                    if (var29 >= -128 && var29 < 128 && var30 >= -128 && var30 < 128 && var9 >= -128 && var9 < 128 && ticksSinceLastForcedTeleport <= 400 && !ridingEntity && field_180234_y == trackedEntity.onGround) {
                        if (var11 && var12) {
                            var10 = new S14PacketEntity.S17PacketEntityLookMove(trackedEntity.getEntityId(), (byte) var29, (byte) var30, (byte) var9, (byte) var27, (byte) var28, trackedEntity.onGround);
                        } else if (var11) {
                            var10 = new S14PacketEntity.S15PacketEntityRelMove(trackedEntity.getEntityId(), (byte) var29, (byte) var30, (byte) var9, trackedEntity.onGround);
                        } else if (var12) {
                            var10 = new S14PacketEntity.S16PacketEntityLook(trackedEntity.getEntityId(), (byte) var27, (byte) var28, trackedEntity.onGround);
                        }
                    } else {
                        field_180234_y = trackedEntity.onGround;
                        ticksSinceLastForcedTeleport = 0;
                        var10 = new S18PacketEntityTeleport(trackedEntity.getEntityId(), var23, var24, var25, (byte) var27, (byte) var28, trackedEntity.onGround);
                    }
                }

                if (sendVelocityUpdates) {
                    double var13 = trackedEntity.motionX - lastTrackedEntityMotionX;
                    double var15 = trackedEntity.motionY - lastTrackedEntityMotionY;
                    double var17 = trackedEntity.motionZ - motionZ;
                    double var19 = 0.02D;
                    double var21 = var13 * var13 + var15 * var15 + var17 * var17;

                    if (var21 > var19 * var19 || var21 > 0.0D && trackedEntity.motionX == 0.0D && trackedEntity.motionY == 0.0D && trackedEntity.motionZ == 0.0D) {
                        lastTrackedEntityMotionX = trackedEntity.motionX;
                        lastTrackedEntityMotionY = trackedEntity.motionY;
                        motionZ = trackedEntity.motionZ;
                        func_151259_a(new S12PacketEntityVelocity(trackedEntity.getEntityId(), lastTrackedEntityMotionX, lastTrackedEntityMotionY, motionZ));
                    }
                }

                if (var10 != null) {
                    func_151259_a((Packet) var10);
                }

                sendMetadataToAllAssociatedPlayers();

                if (var11) {
                    encodedPosX = var23;
                    encodedPosY = var24;
                    encodedPosZ = var25;
                }

                if (var12) {
                    encodedRotationYaw = var27;
                    encodedRotationPitch = var28;
                }

                ridingEntity = false;
            } else {
                var23 = MathHelper.floor_float(trackedEntity.rotationYaw * 256.0F / 360.0F);
                var24 = MathHelper.floor_float(trackedEntity.rotationPitch * 256.0F / 360.0F);
                boolean var26 = Math.abs(var23 - encodedRotationYaw) >= 4 || Math.abs(var24 - encodedRotationPitch) >= 4;

                if (var26) {
                    func_151259_a(new S14PacketEntity.S16PacketEntityLook(trackedEntity.getEntityId(), (byte) var23, (byte) var24, trackedEntity.onGround));
                    encodedRotationYaw = var23;
                    encodedRotationPitch = var24;
                }

                encodedPosX = MathHelper.floor_double(trackedEntity.posX * 32.0D);
                encodedPosY = MathHelper.floor_double(trackedEntity.posY * 32.0D);
                encodedPosZ = MathHelper.floor_double(trackedEntity.posZ * 32.0D);
                sendMetadataToAllAssociatedPlayers();
                ridingEntity = true;
            }

            var23 = MathHelper.floor_float(trackedEntity.getRotationYawHead() * 256.0F / 360.0F);

            if (Math.abs(var23 - lastHeadMotion) >= 4) {
                func_151259_a(new S19PacketEntityHeadLook(trackedEntity, (byte) var23));
                lastHeadMotion = var23;
            }

            trackedEntity.isAirBorne = false;
        }

        ++updateCounter;

        if (trackedEntity.velocityChanged) {
            func_151261_b(new S12PacketEntityVelocity(trackedEntity));
            trackedEntity.velocityChanged = false;
        }
    }

    /**
     * Sends the entity metadata (DataWatcher) and attributes to all players
     * tracking this entity, including the entity itself if a player.
     */
    private void sendMetadataToAllAssociatedPlayers() {
        DataWatcher var1 = trackedEntity.getDataWatcher();

        if (var1.hasObjectChanged()) {
            func_151261_b(new S1CPacketEntityMetadata(trackedEntity.getEntityId(), var1, false));
        }

        if (trackedEntity instanceof EntityLivingBase) {
            ServersideAttributeMap var2 = (ServersideAttributeMap) ((EntityLivingBase) trackedEntity).getAttributeMap();
            Set var3 = var2.getAttributeInstanceSet();

            if (!var3.isEmpty()) {
                func_151261_b(new S20PacketEntityProperties(trackedEntity.getEntityId(), var3));
            }

            var3.clear();
        }
    }

    public void func_151259_a(Packet p_151259_1_) {
        Iterator var2 = trackingPlayers.iterator();

        while (var2.hasNext()) {
            EntityPlayerMP var3 = (EntityPlayerMP) var2.next();
            var3.playerNetServerHandler.sendPacket(p_151259_1_);
        }
    }

    public void func_151261_b(Packet p_151261_1_) {
        func_151259_a(p_151261_1_);

        if (trackedEntity instanceof EntityPlayerMP) {
            ((EntityPlayerMP) trackedEntity).playerNetServerHandler.sendPacket(p_151261_1_);
        }
    }

    public void sendDestroyEntityPacketToTrackedPlayers() {
        Iterator var1 = trackingPlayers.iterator();

        while (var1.hasNext()) {
            EntityPlayerMP var2 = (EntityPlayerMP) var1.next();
            var2.func_152339_d(trackedEntity);
        }
    }

    public void removeFromTrackedPlayers(EntityPlayerMP p_73118_1_) {
        if (trackingPlayers.contains(p_73118_1_)) {
            p_73118_1_.func_152339_d(trackedEntity);
            trackingPlayers.remove(p_73118_1_);
        }
    }

    public void updatePlayerEntity(EntityPlayerMP p_73117_1_) {
        if (p_73117_1_ != trackedEntity) {
            if (func_180233_c(p_73117_1_)) {
                if (!trackingPlayers.contains(p_73117_1_) && (isPlayerWatchingThisChunk(p_73117_1_) || trackedEntity.forceSpawn)) {
                    trackingPlayers.add(p_73117_1_);
                    Packet var2 = func_151260_c();
                    p_73117_1_.playerNetServerHandler.sendPacket(var2);

                    if (!trackedEntity.getDataWatcher().getIsBlank()) {
                        p_73117_1_.playerNetServerHandler.sendPacket(new S1CPacketEntityMetadata(trackedEntity.getEntityId(), trackedEntity.getDataWatcher(), true));
                    }

                    NBTTagCompound var3 = trackedEntity.func_174819_aU();

                    if (var3 != null) {
                        p_73117_1_.playerNetServerHandler.sendPacket(new S49PacketUpdateEntityNBT(trackedEntity.getEntityId(), var3));
                    }

                    if (trackedEntity instanceof EntityLivingBase) {
                        ServersideAttributeMap var4 = (ServersideAttributeMap) ((EntityLivingBase) trackedEntity).getAttributeMap();
                        Collection var5 = var4.getWatchedAttributes();

                        if (!var5.isEmpty()) {
                            p_73117_1_.playerNetServerHandler.sendPacket(new S20PacketEntityProperties(trackedEntity.getEntityId(), var5));
                        }
                    }

                    lastTrackedEntityMotionX = trackedEntity.motionX;
                    lastTrackedEntityMotionY = trackedEntity.motionY;
                    motionZ = trackedEntity.motionZ;

                    if (sendVelocityUpdates && !(var2 instanceof S0FPacketSpawnMob)) {
                        p_73117_1_.playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(trackedEntity.getEntityId(), trackedEntity.motionX, trackedEntity.motionY, trackedEntity.motionZ));
                    }

                    if (trackedEntity.ridingEntity != null) {
                        p_73117_1_.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(0, trackedEntity, trackedEntity.ridingEntity));
                    }

                    if (trackedEntity instanceof EntityLiving && ((EntityLiving) trackedEntity).getLeashedToEntity() != null) {
                        p_73117_1_.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(1, trackedEntity, ((EntityLiving) trackedEntity).getLeashedToEntity()));
                    }

                    if (trackedEntity instanceof EntityLivingBase) {
                        for (int var7 = 0; var7 < 5; ++var7) {
                            ItemStack var10 = ((EntityLivingBase) trackedEntity).getEquipmentInSlot(var7);

                            if (var10 != null) {
                                p_73117_1_.playerNetServerHandler.sendPacket(new S04PacketEntityEquipment(trackedEntity.getEntityId(), var7, var10));
                            }
                        }
                    }

                    if (trackedEntity instanceof EntityPlayer) {
                        EntityPlayer var8 = (EntityPlayer) trackedEntity;

                        if (var8.isPlayerSleeping()) {
                            p_73117_1_.playerNetServerHandler.sendPacket(new S0APacketUseBed(var8, new BlockPos(trackedEntity)));
                        }
                    }

                    if (trackedEntity instanceof EntityLivingBase) {
                        EntityLivingBase var9 = (EntityLivingBase) trackedEntity;
                        Iterator var11 = var9.getActivePotionEffects().iterator();

                        while (var11.hasNext()) {
                            PotionEffect var6 = (PotionEffect) var11.next();
                            p_73117_1_.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(trackedEntity.getEntityId(), var6));
                        }
                    }
                }
            } else if (trackingPlayers.contains(p_73117_1_)) {
                trackingPlayers.remove(p_73117_1_);
                p_73117_1_.func_152339_d(trackedEntity);
            }
        }
    }

    public boolean func_180233_c(EntityPlayerMP p_180233_1_) {
        double var2 = p_180233_1_.posX - encodedPosX / 32;
        double var4 = p_180233_1_.posZ - encodedPosZ / 32;
        return var2 >= (-trackingDistanceThreshold) && var2 <= trackingDistanceThreshold && var4 >= (-trackingDistanceThreshold) && var4 <= trackingDistanceThreshold && trackedEntity.func_174827_a(p_180233_1_);
    }

    private boolean isPlayerWatchingThisChunk(EntityPlayerMP p_73121_1_) {
        return p_73121_1_.getServerForPlayer().getPlayerManager().isPlayerWatchingChunk(p_73121_1_, trackedEntity.chunkCoordX, trackedEntity.chunkCoordZ);
    }

    public void updatePlayerEntities(List p_73125_1_) {
        for (int var2 = 0; var2 < p_73125_1_.size(); ++var2) {
            updatePlayerEntity((EntityPlayerMP) p_73125_1_.get(var2));
        }
    }

    private Packet func_151260_c() {
        if (trackedEntity.isDead) {
            EntityTrackerEntry.logger.warn("Fetching addPacket for removed entity");
        }

        if (trackedEntity instanceof EntityItem) {
            return new S0EPacketSpawnObject(trackedEntity, 2, 1);
        } else if (trackedEntity instanceof EntityPlayerMP) {
            return new S0CPacketSpawnPlayer((EntityPlayer) trackedEntity);
        } else if (trackedEntity instanceof EntityMinecart) {
            EntityMinecart var9 = (EntityMinecart) trackedEntity;
            return new S0EPacketSpawnObject(trackedEntity, 10, var9.func_180456_s().func_180039_a());
        } else if (trackedEntity instanceof EntityBoat) {
            return new S0EPacketSpawnObject(trackedEntity, 1);
        } else if (trackedEntity instanceof IAnimals) {
            lastHeadMotion = MathHelper.floor_float(trackedEntity.getRotationYawHead() * 256.0F / 360.0F);
            return new S0FPacketSpawnMob((EntityLivingBase) trackedEntity);
        } else if (trackedEntity instanceof EntityFishHook) {
            EntityPlayer var8 = ((EntityFishHook) trackedEntity).angler;
            return new S0EPacketSpawnObject(trackedEntity, 90, var8 != null ? var8.getEntityId() : trackedEntity.getEntityId());
        } else if (trackedEntity instanceof EntityArrow) {
            Entity var7 = ((EntityArrow) trackedEntity).shootingEntity;
            return new S0EPacketSpawnObject(trackedEntity, 60, var7 != null ? var7.getEntityId() : trackedEntity.getEntityId());
        } else if (trackedEntity instanceof EntitySnowball) {
            return new S0EPacketSpawnObject(trackedEntity, 61);
        } else if (trackedEntity instanceof EntityPotion) {
            return new S0EPacketSpawnObject(trackedEntity, 73, ((EntityPotion) trackedEntity).getPotionDamage());
        } else if (trackedEntity instanceof EntityExpBottle) {
            return new S0EPacketSpawnObject(trackedEntity, 75);
        } else if (trackedEntity instanceof EntityEnderPearl) {
            return new S0EPacketSpawnObject(trackedEntity, 65);
        } else if (trackedEntity instanceof EntityEnderEye) {
            return new S0EPacketSpawnObject(trackedEntity, 72);
        } else if (trackedEntity instanceof EntityFireworkRocket) {
            return new S0EPacketSpawnObject(trackedEntity, 76);
        } else {
            S0EPacketSpawnObject var2;

            if (trackedEntity instanceof EntityFireball) {
                EntityFireball var6 = (EntityFireball) trackedEntity;
                var2 = null;
                byte var10 = 63;

                if (trackedEntity instanceof EntitySmallFireball) {
                    var10 = 64;
                } else if (trackedEntity instanceof EntityWitherSkull) {
                    var10 = 66;
                }

                if (var6.shootingEntity != null) {
                    var2 = new S0EPacketSpawnObject(trackedEntity, var10, ((EntityFireball) trackedEntity).shootingEntity.getEntityId());
                } else {
                    var2 = new S0EPacketSpawnObject(trackedEntity, var10, 0);
                }

                var2.func_149003_d((int) (var6.accelerationX * 8000.0D));
                var2.func_149000_e((int) (var6.accelerationY * 8000.0D));
                var2.func_149007_f((int) (var6.accelerationZ * 8000.0D));
                return var2;
            } else if (trackedEntity instanceof EntityEgg) {
                return new S0EPacketSpawnObject(trackedEntity, 62);
            } else if (trackedEntity instanceof EntityTNTPrimed) {
                return new S0EPacketSpawnObject(trackedEntity, 50);
            } else if (trackedEntity instanceof EntityEnderCrystal) {
                return new S0EPacketSpawnObject(trackedEntity, 51);
            } else if (trackedEntity instanceof EntityFallingBlock) {
                EntityFallingBlock var5 = (EntityFallingBlock) trackedEntity;
                return new S0EPacketSpawnObject(trackedEntity, 70, Block.getStateId(var5.getBlock()));
            } else if (trackedEntity instanceof EntityArmorStand) {
                return new S0EPacketSpawnObject(trackedEntity, 78);
            } else if (trackedEntity instanceof EntityPainting) {
                return new S10PacketSpawnPainting((EntityPainting) trackedEntity);
            } else {
                BlockPos var3;

                if (trackedEntity instanceof EntityItemFrame) {
                    EntityItemFrame var4 = (EntityItemFrame) trackedEntity;
                    var2 = new S0EPacketSpawnObject(trackedEntity, 71, var4.field_174860_b.getHorizontalIndex());
                    var3 = var4.func_174857_n();
                    var2.func_148996_a(MathHelper.floor_float(var3.getX() * 32));
                    var2.func_148995_b(MathHelper.floor_float(var3.getY() * 32));
                    var2.func_149005_c(MathHelper.floor_float(var3.getZ() * 32));
                    return var2;
                } else if (trackedEntity instanceof EntityLeashKnot) {
                    EntityLeashKnot var1 = (EntityLeashKnot) trackedEntity;
                    var2 = new S0EPacketSpawnObject(trackedEntity, 77);
                    var3 = var1.func_174857_n();
                    var2.func_148996_a(MathHelper.floor_float(var3.getX() * 32));
                    var2.func_148995_b(MathHelper.floor_float(var3.getY() * 32));
                    var2.func_149005_c(MathHelper.floor_float(var3.getZ() * 32));
                    return var2;
                } else if (trackedEntity instanceof EntityXPOrb) {
                    return new S11PacketSpawnExperienceOrb((EntityXPOrb) trackedEntity);
                } else {
                    throw new IllegalArgumentException("Don\'t know how to add " + trackedEntity.getClass() + "!");
                }
            }
        }
    }

    /**
     * Remove a tracked player from our list and tell the tracked player to
     * destroy us from their world.
     */
    public void removeTrackedPlayerSymmetric(EntityPlayerMP p_73123_1_) {
        if (trackingPlayers.contains(p_73123_1_)) {
            trackingPlayers.remove(p_73123_1_);
            p_73123_1_.func_152339_d(trackedEntity);
        }
    }
}
