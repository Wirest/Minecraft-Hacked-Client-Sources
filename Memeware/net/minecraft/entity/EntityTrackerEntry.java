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
     * every 400 ticks a  full teleport packet is sent, rather than just a "move me +x" command, so that position
     * remains fully synced.
     */
    private int ticksSinceLastForcedTeleport;
    private Entity field_85178_v;
    private boolean ridingEntity;
    private boolean field_180234_y;
    public boolean playerEntitiesUpdated;

    /**
     * Holds references to all the players that are currently receiving position updates for this entity.
     */
    public Set trackingPlayers = Sets.newHashSet();
    private static final String __OBFID = "CL_00001443";

    public EntityTrackerEntry(Entity p_i1525_1_, int p_i1525_2_, int p_i1525_3_, boolean p_i1525_4_) {
        this.trackedEntity = p_i1525_1_;
        this.trackingDistanceThreshold = p_i1525_2_;
        this.updateFrequency = p_i1525_3_;
        this.sendVelocityUpdates = p_i1525_4_;
        this.encodedPosX = MathHelper.floor_double(p_i1525_1_.posX * 32.0D);
        this.encodedPosY = MathHelper.floor_double(p_i1525_1_.posY * 32.0D);
        this.encodedPosZ = MathHelper.floor_double(p_i1525_1_.posZ * 32.0D);
        this.encodedRotationYaw = MathHelper.floor_float(p_i1525_1_.rotationYaw * 256.0F / 360.0F);
        this.encodedRotationPitch = MathHelper.floor_float(p_i1525_1_.rotationPitch * 256.0F / 360.0F);
        this.lastHeadMotion = MathHelper.floor_float(p_i1525_1_.getRotationYawHead() * 256.0F / 360.0F);
        this.field_180234_y = p_i1525_1_.onGround;
    }

    public boolean equals(Object p_equals_1_) {
        return p_equals_1_ instanceof EntityTrackerEntry ? ((EntityTrackerEntry) p_equals_1_).trackedEntity.getEntityId() == this.trackedEntity.getEntityId() : false;
    }

    public int hashCode() {
        return this.trackedEntity.getEntityId();
    }

    public void updatePlayerList(List p_73122_1_) {
        this.playerEntitiesUpdated = false;

        if (!this.firstUpdateDone || this.trackedEntity.getDistanceSq(this.lastTrackedEntityPosX, this.lastTrackedEntityPosY, this.lastTrackedEntityPosZ) > 16.0D) {
            this.lastTrackedEntityPosX = this.trackedEntity.posX;
            this.lastTrackedEntityPosY = this.trackedEntity.posY;
            this.lastTrackedEntityPosZ = this.trackedEntity.posZ;
            this.firstUpdateDone = true;
            this.playerEntitiesUpdated = true;
            this.updatePlayerEntities(p_73122_1_);
        }

        if (this.field_85178_v != this.trackedEntity.ridingEntity || this.trackedEntity.ridingEntity != null && this.updateCounter % 60 == 0) {
            this.field_85178_v = this.trackedEntity.ridingEntity;
            this.func_151259_a(new S1BPacketEntityAttach(0, this.trackedEntity, this.trackedEntity.ridingEntity));
        }

        if (this.trackedEntity instanceof EntityItemFrame && this.updateCounter % 10 == 0) {
            EntityItemFrame var2 = (EntityItemFrame) this.trackedEntity;
            ItemStack var3 = var2.getDisplayedItem();

            if (var3 != null && var3.getItem() instanceof ItemMap) {
                MapData var4 = Items.filled_map.getMapData(var3, this.trackedEntity.worldObj);
                Iterator var5 = p_73122_1_.iterator();

                while (var5.hasNext()) {
                    EntityPlayer var6 = (EntityPlayer) var5.next();
                    EntityPlayerMP var7 = (EntityPlayerMP) var6;
                    var4.updateVisiblePlayers(var7, var3);
                    Packet var8 = Items.filled_map.createMapDataPacket(var3, this.trackedEntity.worldObj, var7);

                    if (var8 != null) {
                        var7.playerNetServerHandler.sendPacket(var8);
                    }
                }
            }

            this.sendMetadataToAllAssociatedPlayers();
        }

        if (this.updateCounter % this.updateFrequency == 0 || this.trackedEntity.isAirBorne || this.trackedEntity.getDataWatcher().hasObjectChanged()) {
            int var23;
            int var24;

            if (this.trackedEntity.ridingEntity == null) {
                ++this.ticksSinceLastForcedTeleport;
                var23 = MathHelper.floor_double(this.trackedEntity.posX * 32.0D);
                var24 = MathHelper.floor_double(this.trackedEntity.posY * 32.0D);
                int var25 = MathHelper.floor_double(this.trackedEntity.posZ * 32.0D);
                int var27 = MathHelper.floor_float(this.trackedEntity.rotationYaw * 256.0F / 360.0F);
                int var28 = MathHelper.floor_float(this.trackedEntity.rotationPitch * 256.0F / 360.0F);
                int var29 = var23 - this.encodedPosX;
                int var30 = var24 - this.encodedPosY;
                int var9 = var25 - this.encodedPosZ;
                Object var10 = null;
                boolean var11 = Math.abs(var29) >= 4 || Math.abs(var30) >= 4 || Math.abs(var9) >= 4 || this.updateCounter % 60 == 0;
                boolean var12 = Math.abs(var27 - this.encodedRotationYaw) >= 4 || Math.abs(var28 - this.encodedRotationPitch) >= 4;

                if (this.updateCounter > 0 || this.trackedEntity instanceof EntityArrow) {
                    if (var29 >= -128 && var29 < 128 && var30 >= -128 && var30 < 128 && var9 >= -128 && var9 < 128 && this.ticksSinceLastForcedTeleport <= 400 && !this.ridingEntity && this.field_180234_y == this.trackedEntity.onGround) {
                        if (var11 && var12) {
                            var10 = new S14PacketEntity.S17PacketEntityLookMove(this.trackedEntity.getEntityId(), (byte) var29, (byte) var30, (byte) var9, (byte) var27, (byte) var28, this.trackedEntity.onGround);
                        } else if (var11) {
                            var10 = new S14PacketEntity.S15PacketEntityRelMove(this.trackedEntity.getEntityId(), (byte) var29, (byte) var30, (byte) var9, this.trackedEntity.onGround);
                        } else if (var12) {
                            var10 = new S14PacketEntity.S16PacketEntityLook(this.trackedEntity.getEntityId(), (byte) var27, (byte) var28, this.trackedEntity.onGround);
                        }
                    } else {
                        this.field_180234_y = this.trackedEntity.onGround;
                        this.ticksSinceLastForcedTeleport = 0;
                        var10 = new S18PacketEntityTeleport(this.trackedEntity.getEntityId(), var23, var24, var25, (byte) var27, (byte) var28, this.trackedEntity.onGround);
                    }
                }

                if (this.sendVelocityUpdates) {
                    double var13 = this.trackedEntity.motionX - this.lastTrackedEntityMotionX;
                    double var15 = this.trackedEntity.motionY - this.lastTrackedEntityMotionY;
                    double var17 = this.trackedEntity.motionZ - this.motionZ;
                    double var19 = 0.02D;
                    double var21 = var13 * var13 + var15 * var15 + var17 * var17;

                    if (var21 > var19 * var19 || var21 > 0.0D && this.trackedEntity.motionX == 0.0D && this.trackedEntity.motionY == 0.0D && this.trackedEntity.motionZ == 0.0D) {
                        this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
                        this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
                        this.motionZ = this.trackedEntity.motionZ;
                        this.func_151259_a(new S12PacketEntityVelocity(this.trackedEntity.getEntityId(), this.lastTrackedEntityMotionX, this.lastTrackedEntityMotionY, this.motionZ));
                    }
                }

                if (var10 != null) {
                    this.func_151259_a((Packet) var10);
                }

                this.sendMetadataToAllAssociatedPlayers();

                if (var11) {
                    this.encodedPosX = var23;
                    this.encodedPosY = var24;
                    this.encodedPosZ = var25;
                }

                if (var12) {
                    this.encodedRotationYaw = var27;
                    this.encodedRotationPitch = var28;
                }

                this.ridingEntity = false;
            } else {
                var23 = MathHelper.floor_float(this.trackedEntity.rotationYaw * 256.0F / 360.0F);
                var24 = MathHelper.floor_float(this.trackedEntity.rotationPitch * 256.0F / 360.0F);
                boolean var26 = Math.abs(var23 - this.encodedRotationYaw) >= 4 || Math.abs(var24 - this.encodedRotationPitch) >= 4;

                if (var26) {
                    this.func_151259_a(new S14PacketEntity.S16PacketEntityLook(this.trackedEntity.getEntityId(), (byte) var23, (byte) var24, this.trackedEntity.onGround));
                    this.encodedRotationYaw = var23;
                    this.encodedRotationPitch = var24;
                }

                this.encodedPosX = MathHelper.floor_double(this.trackedEntity.posX * 32.0D);
                this.encodedPosY = MathHelper.floor_double(this.trackedEntity.posY * 32.0D);
                this.encodedPosZ = MathHelper.floor_double(this.trackedEntity.posZ * 32.0D);
                this.sendMetadataToAllAssociatedPlayers();
                this.ridingEntity = true;
            }

            var23 = MathHelper.floor_float(this.trackedEntity.getRotationYawHead() * 256.0F / 360.0F);

            if (Math.abs(var23 - this.lastHeadMotion) >= 4) {
                this.func_151259_a(new S19PacketEntityHeadLook(this.trackedEntity, (byte) var23));
                this.lastHeadMotion = var23;
            }

            this.trackedEntity.isAirBorne = false;
        }

        ++this.updateCounter;

        if (this.trackedEntity.velocityChanged) {
            this.func_151261_b(new S12PacketEntityVelocity(this.trackedEntity));
            this.trackedEntity.velocityChanged = false;
        }
    }

    /**
     * Sends the entity metadata (DataWatcher) and attributes to all players tracking this entity, including the entity
     * itself if a player.
     */
    private void sendMetadataToAllAssociatedPlayers() {
        DataWatcher var1 = this.trackedEntity.getDataWatcher();

        if (var1.hasObjectChanged()) {
            this.func_151261_b(new S1CPacketEntityMetadata(this.trackedEntity.getEntityId(), var1, false));
        }

        if (this.trackedEntity instanceof EntityLivingBase) {
            ServersideAttributeMap var2 = (ServersideAttributeMap) ((EntityLivingBase) this.trackedEntity).getAttributeMap();
            Set var3 = var2.getAttributeInstanceSet();

            if (!var3.isEmpty()) {
                this.func_151261_b(new S20PacketEntityProperties(this.trackedEntity.getEntityId(), var3));
            }

            var3.clear();
        }
    }

    public void func_151259_a(Packet p_151259_1_) {
        Iterator var2 = this.trackingPlayers.iterator();

        while (var2.hasNext()) {
            EntityPlayerMP var3 = (EntityPlayerMP) var2.next();
            var3.playerNetServerHandler.sendPacket(p_151259_1_);
        }
    }

    public void func_151261_b(Packet p_151261_1_) {
        this.func_151259_a(p_151261_1_);

        if (this.trackedEntity instanceof EntityPlayerMP) {
            ((EntityPlayerMP) this.trackedEntity).playerNetServerHandler.sendPacket(p_151261_1_);
        }
    }

    public void sendDestroyEntityPacketToTrackedPlayers() {
        Iterator var1 = this.trackingPlayers.iterator();

        while (var1.hasNext()) {
            EntityPlayerMP var2 = (EntityPlayerMP) var1.next();
            var2.func_152339_d(this.trackedEntity);
        }
    }

    public void removeFromTrackedPlayers(EntityPlayerMP p_73118_1_) {
        if (this.trackingPlayers.contains(p_73118_1_)) {
            p_73118_1_.func_152339_d(this.trackedEntity);
            this.trackingPlayers.remove(p_73118_1_);
        }
    }

    public void updatePlayerEntity(EntityPlayerMP p_73117_1_) {
        if (p_73117_1_ != this.trackedEntity) {
            if (this.func_180233_c(p_73117_1_)) {
                if (!this.trackingPlayers.contains(p_73117_1_) && (this.isPlayerWatchingThisChunk(p_73117_1_) || this.trackedEntity.forceSpawn)) {
                    this.trackingPlayers.add(p_73117_1_);
                    Packet var2 = this.func_151260_c();
                    p_73117_1_.playerNetServerHandler.sendPacket(var2);

                    if (!this.trackedEntity.getDataWatcher().getIsBlank()) {
                        p_73117_1_.playerNetServerHandler.sendPacket(new S1CPacketEntityMetadata(this.trackedEntity.getEntityId(), this.trackedEntity.getDataWatcher(), true));
                    }

                    NBTTagCompound var3 = this.trackedEntity.func_174819_aU();

                    if (var3 != null) {
                        p_73117_1_.playerNetServerHandler.sendPacket(new S49PacketUpdateEntityNBT(this.trackedEntity.getEntityId(), var3));
                    }

                    if (this.trackedEntity instanceof EntityLivingBase) {
                        ServersideAttributeMap var4 = (ServersideAttributeMap) ((EntityLivingBase) this.trackedEntity).getAttributeMap();
                        Collection var5 = var4.getWatchedAttributes();

                        if (!var5.isEmpty()) {
                            p_73117_1_.playerNetServerHandler.sendPacket(new S20PacketEntityProperties(this.trackedEntity.getEntityId(), var5));
                        }
                    }

                    this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
                    this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
                    this.motionZ = this.trackedEntity.motionZ;

                    if (this.sendVelocityUpdates && !(var2 instanceof S0FPacketSpawnMob)) {
                        p_73117_1_.playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(this.trackedEntity.getEntityId(), this.trackedEntity.motionX, this.trackedEntity.motionY, this.trackedEntity.motionZ));
                    }

                    if (this.trackedEntity.ridingEntity != null) {
                        p_73117_1_.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(0, this.trackedEntity, this.trackedEntity.ridingEntity));
                    }

                    if (this.trackedEntity instanceof EntityLiving && ((EntityLiving) this.trackedEntity).getLeashedToEntity() != null) {
                        p_73117_1_.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(1, this.trackedEntity, ((EntityLiving) this.trackedEntity).getLeashedToEntity()));
                    }

                    if (this.trackedEntity instanceof EntityLivingBase) {
                        for (int var7 = 0; var7 < 5; ++var7) {
                            ItemStack var10 = ((EntityLivingBase) this.trackedEntity).getEquipmentInSlot(var7);

                            if (var10 != null) {
                                p_73117_1_.playerNetServerHandler.sendPacket(new S04PacketEntityEquipment(this.trackedEntity.getEntityId(), var7, var10));
                            }
                        }
                    }

                    if (this.trackedEntity instanceof EntityPlayer) {
                        EntityPlayer var8 = (EntityPlayer) this.trackedEntity;

                        if (var8.isPlayerSleeping()) {
                            p_73117_1_.playerNetServerHandler.sendPacket(new S0APacketUseBed(var8, new BlockPos(this.trackedEntity)));
                        }
                    }

                    if (this.trackedEntity instanceof EntityLivingBase) {
                        EntityLivingBase var9 = (EntityLivingBase) this.trackedEntity;
                        Iterator var11 = var9.getActivePotionEffects().iterator();

                        while (var11.hasNext()) {
                            PotionEffect var6 = (PotionEffect) var11.next();
                            p_73117_1_.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(this.trackedEntity.getEntityId(), var6));
                        }
                    }
                }
            } else if (this.trackingPlayers.contains(p_73117_1_)) {
                this.trackingPlayers.remove(p_73117_1_);
                p_73117_1_.func_152339_d(this.trackedEntity);
            }
        }
    }

    public boolean func_180233_c(EntityPlayerMP p_180233_1_) {
        double var2 = p_180233_1_.posX - (double) (this.encodedPosX / 32);
        double var4 = p_180233_1_.posZ - (double) (this.encodedPosZ / 32);
        return var2 >= (double) (-this.trackingDistanceThreshold) && var2 <= (double) this.trackingDistanceThreshold && var4 >= (double) (-this.trackingDistanceThreshold) && var4 <= (double) this.trackingDistanceThreshold && this.trackedEntity.func_174827_a(p_180233_1_);
    }

    private boolean isPlayerWatchingThisChunk(EntityPlayerMP p_73121_1_) {
        return p_73121_1_.getServerForPlayer().getPlayerManager().isPlayerWatchingChunk(p_73121_1_, this.trackedEntity.chunkCoordX, this.trackedEntity.chunkCoordZ);
    }

    public void updatePlayerEntities(List p_73125_1_) {
        for (int var2 = 0; var2 < p_73125_1_.size(); ++var2) {
            this.updatePlayerEntity((EntityPlayerMP) p_73125_1_.get(var2));
        }
    }

    private Packet func_151260_c() {
        if (this.trackedEntity.isDead) {
            logger.warn("Fetching addPacket for removed entity");
        }

        if (this.trackedEntity instanceof EntityItem) {
            return new S0EPacketSpawnObject(this.trackedEntity, 2, 1);
        } else if (this.trackedEntity instanceof EntityPlayerMP) {
            return new S0CPacketSpawnPlayer((EntityPlayer) this.trackedEntity);
        } else if (this.trackedEntity instanceof EntityMinecart) {
            EntityMinecart var9 = (EntityMinecart) this.trackedEntity;
            return new S0EPacketSpawnObject(this.trackedEntity, 10, var9.func_180456_s().func_180039_a());
        } else if (this.trackedEntity instanceof EntityBoat) {
            return new S0EPacketSpawnObject(this.trackedEntity, 1);
        } else if (this.trackedEntity instanceof IAnimals) {
            this.lastHeadMotion = MathHelper.floor_float(this.trackedEntity.getRotationYawHead() * 256.0F / 360.0F);
            return new S0FPacketSpawnMob((EntityLivingBase) this.trackedEntity);
        } else if (this.trackedEntity instanceof EntityFishHook) {
            EntityPlayer var8 = ((EntityFishHook) this.trackedEntity).angler;
            return new S0EPacketSpawnObject(this.trackedEntity, 90, var8 != null ? var8.getEntityId() : this.trackedEntity.getEntityId());
        } else if (this.trackedEntity instanceof EntityArrow) {
            Entity var7 = ((EntityArrow) this.trackedEntity).shootingEntity;
            return new S0EPacketSpawnObject(this.trackedEntity, 60, var7 != null ? var7.getEntityId() : this.trackedEntity.getEntityId());
        } else if (this.trackedEntity instanceof EntitySnowball) {
            return new S0EPacketSpawnObject(this.trackedEntity, 61);
        } else if (this.trackedEntity instanceof EntityPotion) {
            return new S0EPacketSpawnObject(this.trackedEntity, 73, ((EntityPotion) this.trackedEntity).getPotionDamage());
        } else if (this.trackedEntity instanceof EntityExpBottle) {
            return new S0EPacketSpawnObject(this.trackedEntity, 75);
        } else if (this.trackedEntity instanceof EntityEnderPearl) {
            return new S0EPacketSpawnObject(this.trackedEntity, 65);
        } else if (this.trackedEntity instanceof EntityEnderEye) {
            return new S0EPacketSpawnObject(this.trackedEntity, 72);
        } else if (this.trackedEntity instanceof EntityFireworkRocket) {
            return new S0EPacketSpawnObject(this.trackedEntity, 76);
        } else {
            S0EPacketSpawnObject var2;

            if (this.trackedEntity instanceof EntityFireball) {
                EntityFireball var6 = (EntityFireball) this.trackedEntity;
                var2 = null;
                byte var10 = 63;

                if (this.trackedEntity instanceof EntitySmallFireball) {
                    var10 = 64;
                } else if (this.trackedEntity instanceof EntityWitherSkull) {
                    var10 = 66;
                }

                if (var6.shootingEntity != null) {
                    var2 = new S0EPacketSpawnObject(this.trackedEntity, var10, ((EntityFireball) this.trackedEntity).shootingEntity.getEntityId());
                } else {
                    var2 = new S0EPacketSpawnObject(this.trackedEntity, var10, 0);
                }

                var2.func_149003_d((int) (var6.accelerationX * 8000.0D));
                var2.func_149000_e((int) (var6.accelerationY * 8000.0D));
                var2.func_149007_f((int) (var6.accelerationZ * 8000.0D));
                return var2;
            } else if (this.trackedEntity instanceof EntityEgg) {
                return new S0EPacketSpawnObject(this.trackedEntity, 62);
            } else if (this.trackedEntity instanceof EntityTNTPrimed) {
                return new S0EPacketSpawnObject(this.trackedEntity, 50);
            } else if (this.trackedEntity instanceof EntityEnderCrystal) {
                return new S0EPacketSpawnObject(this.trackedEntity, 51);
            } else if (this.trackedEntity instanceof EntityFallingBlock) {
                EntityFallingBlock var5 = (EntityFallingBlock) this.trackedEntity;
                return new S0EPacketSpawnObject(this.trackedEntity, 70, Block.getStateId(var5.getBlock()));
            } else if (this.trackedEntity instanceof EntityArmorStand) {
                return new S0EPacketSpawnObject(this.trackedEntity, 78);
            } else if (this.trackedEntity instanceof EntityPainting) {
                return new S10PacketSpawnPainting((EntityPainting) this.trackedEntity);
            } else {
                BlockPos var3;

                if (this.trackedEntity instanceof EntityItemFrame) {
                    EntityItemFrame var4 = (EntityItemFrame) this.trackedEntity;
                    var2 = new S0EPacketSpawnObject(this.trackedEntity, 71, var4.field_174860_b.getHorizontalIndex());
                    var3 = var4.func_174857_n();
                    var2.func_148996_a(MathHelper.floor_float((float) (var3.getX() * 32)));
                    var2.func_148995_b(MathHelper.floor_float((float) (var3.getY() * 32)));
                    var2.func_149005_c(MathHelper.floor_float((float) (var3.getZ() * 32)));
                    return var2;
                } else if (this.trackedEntity instanceof EntityLeashKnot) {
                    EntityLeashKnot var1 = (EntityLeashKnot) this.trackedEntity;
                    var2 = new S0EPacketSpawnObject(this.trackedEntity, 77);
                    var3 = var1.func_174857_n();
                    var2.func_148996_a(MathHelper.floor_float((float) (var3.getX() * 32)));
                    var2.func_148995_b(MathHelper.floor_float((float) (var3.getY() * 32)));
                    var2.func_149005_c(MathHelper.floor_float((float) (var3.getZ() * 32)));
                    return var2;
                } else if (this.trackedEntity instanceof EntityXPOrb) {
                    return new S11PacketSpawnExperienceOrb((EntityXPOrb) this.trackedEntity);
                } else {
                    throw new IllegalArgumentException("Don\'t know how to add " + this.trackedEntity.getClass() + "!");
                }
            }
        }
    }

    /**
     * Remove a tracked player from our list and tell the tracked player to destroy us from their world.
     */
    public void removeTrackedPlayerSymmetric(EntityPlayerMP p_73123_1_) {
        if (this.trackingPlayers.contains(p_73123_1_)) {
            this.trackingPlayers.remove(p_73123_1_);
            p_73123_1_.func_152339_d(this.trackedEntity);
        }
    }
}
