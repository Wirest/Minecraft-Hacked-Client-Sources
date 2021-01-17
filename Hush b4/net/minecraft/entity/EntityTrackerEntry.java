// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import java.util.Collection;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import java.util.Iterator;
import net.minecraft.world.storage.MapData;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.util.MathHelper;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.Set;
import org.apache.logging.log4j.Logger;

public class EntityTrackerEntry
{
    private static final Logger logger;
    public Entity trackedEntity;
    public int trackingDistanceThreshold;
    public int updateFrequency;
    public int encodedPosX;
    public int encodedPosY;
    public int encodedPosZ;
    public int encodedRotationYaw;
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
    private int ticksSinceLastForcedTeleport;
    private Entity field_85178_v;
    private boolean ridingEntity;
    private boolean onGround;
    public boolean playerEntitiesUpdated;
    public Set<EntityPlayerMP> trackingPlayers;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public EntityTrackerEntry(final Entity trackedEntityIn, final int trackingDistanceThresholdIn, final int updateFrequencyIn, final boolean sendVelocityUpdatesIn) {
        this.trackingPlayers = (Set<EntityPlayerMP>)Sets.newHashSet();
        this.trackedEntity = trackedEntityIn;
        this.trackingDistanceThreshold = trackingDistanceThresholdIn;
        this.updateFrequency = updateFrequencyIn;
        this.sendVelocityUpdates = sendVelocityUpdatesIn;
        this.encodedPosX = MathHelper.floor_double(trackedEntityIn.posX * 32.0);
        this.encodedPosY = MathHelper.floor_double(trackedEntityIn.posY * 32.0);
        this.encodedPosZ = MathHelper.floor_double(trackedEntityIn.posZ * 32.0);
        this.encodedRotationYaw = MathHelper.floor_float(trackedEntityIn.rotationYaw * 256.0f / 360.0f);
        this.encodedRotationPitch = MathHelper.floor_float(trackedEntityIn.rotationPitch * 256.0f / 360.0f);
        this.lastHeadMotion = MathHelper.floor_float(trackedEntityIn.getRotationYawHead() * 256.0f / 360.0f);
        this.onGround = trackedEntityIn.onGround;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        return p_equals_1_ instanceof EntityTrackerEntry && ((EntityTrackerEntry)p_equals_1_).trackedEntity.getEntityId() == this.trackedEntity.getEntityId();
    }
    
    @Override
    public int hashCode() {
        return this.trackedEntity.getEntityId();
    }
    
    public void updatePlayerList(final List<EntityPlayer> p_73122_1_) {
        this.playerEntitiesUpdated = false;
        if (!this.firstUpdateDone || this.trackedEntity.getDistanceSq(this.lastTrackedEntityPosX, this.lastTrackedEntityPosY, this.lastTrackedEntityPosZ) > 16.0) {
            this.lastTrackedEntityPosX = this.trackedEntity.posX;
            this.lastTrackedEntityPosY = this.trackedEntity.posY;
            this.lastTrackedEntityPosZ = this.trackedEntity.posZ;
            this.firstUpdateDone = true;
            this.playerEntitiesUpdated = true;
            this.updatePlayerEntities(p_73122_1_);
        }
        if (this.field_85178_v != this.trackedEntity.ridingEntity || (this.trackedEntity.ridingEntity != null && this.updateCounter % 60 == 0)) {
            this.field_85178_v = this.trackedEntity.ridingEntity;
            this.sendPacketToTrackedPlayers(new S1BPacketEntityAttach(0, this.trackedEntity, this.trackedEntity.ridingEntity));
        }
        if (this.trackedEntity instanceof EntityItemFrame && this.updateCounter % 10 == 0) {
            final EntityItemFrame entityitemframe = (EntityItemFrame)this.trackedEntity;
            final ItemStack itemstack = entityitemframe.getDisplayedItem();
            if (itemstack != null && itemstack.getItem() instanceof ItemMap) {
                final MapData mapdata = Items.filled_map.getMapData(itemstack, this.trackedEntity.worldObj);
                for (final EntityPlayer entityplayer : p_73122_1_) {
                    final EntityPlayerMP entityplayermp = (EntityPlayerMP)entityplayer;
                    mapdata.updateVisiblePlayers(entityplayermp, itemstack);
                    final Packet packet = Items.filled_map.createMapDataPacket(itemstack, this.trackedEntity.worldObj, entityplayermp);
                    if (packet != null) {
                        entityplayermp.playerNetServerHandler.sendPacket(packet);
                    }
                }
            }
            this.sendMetadataToAllAssociatedPlayers();
        }
        if (this.updateCounter % this.updateFrequency == 0 || this.trackedEntity.isAirBorne || this.trackedEntity.getDataWatcher().hasObjectChanged()) {
            if (this.trackedEntity.ridingEntity == null) {
                ++this.ticksSinceLastForcedTeleport;
                final int k = MathHelper.floor_double(this.trackedEntity.posX * 32.0);
                final int j1 = MathHelper.floor_double(this.trackedEntity.posY * 32.0);
                final int k2 = MathHelper.floor_double(this.trackedEntity.posZ * 32.0);
                final int l1 = MathHelper.floor_float(this.trackedEntity.rotationYaw * 256.0f / 360.0f);
                final int i2 = MathHelper.floor_float(this.trackedEntity.rotationPitch * 256.0f / 360.0f);
                final int j2 = k - this.encodedPosX;
                final int k3 = j1 - this.encodedPosY;
                final int m = k2 - this.encodedPosZ;
                Packet packet2 = null;
                final boolean flag = Math.abs(j2) >= 4 || Math.abs(k3) >= 4 || Math.abs(m) >= 4 || this.updateCounter % 60 == 0;
                final boolean flag2 = Math.abs(l1 - this.encodedRotationYaw) >= 4 || Math.abs(i2 - this.encodedRotationPitch) >= 4;
                if (this.updateCounter > 0 || this.trackedEntity instanceof EntityArrow) {
                    if (j2 >= -128 && j2 < 128 && k3 >= -128 && k3 < 128 && m >= -128 && m < 128 && this.ticksSinceLastForcedTeleport <= 400 && !this.ridingEntity && this.onGround == this.trackedEntity.onGround) {
                        if ((!flag || !flag2) && !(this.trackedEntity instanceof EntityArrow)) {
                            if (flag) {
                                packet2 = new S14PacketEntity.S15PacketEntityRelMove(this.trackedEntity.getEntityId(), (byte)j2, (byte)k3, (byte)m, this.trackedEntity.onGround);
                            }
                            else if (flag2) {
                                packet2 = new S14PacketEntity.S16PacketEntityLook(this.trackedEntity.getEntityId(), (byte)l1, (byte)i2, this.trackedEntity.onGround);
                            }
                        }
                        else {
                            packet2 = new S14PacketEntity.S17PacketEntityLookMove(this.trackedEntity.getEntityId(), (byte)j2, (byte)k3, (byte)m, (byte)l1, (byte)i2, this.trackedEntity.onGround);
                        }
                    }
                    else {
                        this.onGround = this.trackedEntity.onGround;
                        this.ticksSinceLastForcedTeleport = 0;
                        packet2 = new S18PacketEntityTeleport(this.trackedEntity.getEntityId(), k, j1, k2, (byte)l1, (byte)i2, this.trackedEntity.onGround);
                    }
                }
                if (this.sendVelocityUpdates) {
                    final double d0 = this.trackedEntity.motionX - this.lastTrackedEntityMotionX;
                    final double d2 = this.trackedEntity.motionY - this.lastTrackedEntityMotionY;
                    final double d3 = this.trackedEntity.motionZ - this.motionZ;
                    final double d4 = 0.02;
                    final double d5 = d0 * d0 + d2 * d2 + d3 * d3;
                    if (d5 > d4 * d4 || (d5 > 0.0 && this.trackedEntity.motionX == 0.0 && this.trackedEntity.motionY == 0.0 && this.trackedEntity.motionZ == 0.0)) {
                        this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
                        this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
                        this.motionZ = this.trackedEntity.motionZ;
                        this.sendPacketToTrackedPlayers(new S12PacketEntityVelocity(this.trackedEntity.getEntityId(), this.lastTrackedEntityMotionX, this.lastTrackedEntityMotionY, this.motionZ));
                    }
                }
                if (packet2 != null) {
                    this.sendPacketToTrackedPlayers(packet2);
                }
                this.sendMetadataToAllAssociatedPlayers();
                if (flag) {
                    this.encodedPosX = k;
                    this.encodedPosY = j1;
                    this.encodedPosZ = k2;
                }
                if (flag2) {
                    this.encodedRotationYaw = l1;
                    this.encodedRotationPitch = i2;
                }
                this.ridingEntity = false;
            }
            else {
                final int j3 = MathHelper.floor_float(this.trackedEntity.rotationYaw * 256.0f / 360.0f);
                final int i3 = MathHelper.floor_float(this.trackedEntity.rotationPitch * 256.0f / 360.0f);
                final boolean flag3 = Math.abs(j3 - this.encodedRotationYaw) >= 4 || Math.abs(i3 - this.encodedRotationPitch) >= 4;
                if (flag3) {
                    this.sendPacketToTrackedPlayers(new S14PacketEntity.S16PacketEntityLook(this.trackedEntity.getEntityId(), (byte)j3, (byte)i3, this.trackedEntity.onGround));
                    this.encodedRotationYaw = j3;
                    this.encodedRotationPitch = i3;
                }
                this.encodedPosX = MathHelper.floor_double(this.trackedEntity.posX * 32.0);
                this.encodedPosY = MathHelper.floor_double(this.trackedEntity.posY * 32.0);
                this.encodedPosZ = MathHelper.floor_double(this.trackedEntity.posZ * 32.0);
                this.sendMetadataToAllAssociatedPlayers();
                this.ridingEntity = true;
            }
            final int l2 = MathHelper.floor_float(this.trackedEntity.getRotationYawHead() * 256.0f / 360.0f);
            if (Math.abs(l2 - this.lastHeadMotion) >= 4) {
                this.sendPacketToTrackedPlayers(new S19PacketEntityHeadLook(this.trackedEntity, (byte)l2));
                this.lastHeadMotion = l2;
            }
            this.trackedEntity.isAirBorne = false;
        }
        ++this.updateCounter;
        if (this.trackedEntity.velocityChanged) {
            this.func_151261_b(new S12PacketEntityVelocity(this.trackedEntity));
            this.trackedEntity.velocityChanged = false;
        }
    }
    
    private void sendMetadataToAllAssociatedPlayers() {
        final DataWatcher datawatcher = this.trackedEntity.getDataWatcher();
        if (datawatcher.hasObjectChanged()) {
            this.func_151261_b(new S1CPacketEntityMetadata(this.trackedEntity.getEntityId(), datawatcher, false));
        }
        if (this.trackedEntity instanceof EntityLivingBase) {
            final ServersideAttributeMap serversideattributemap = (ServersideAttributeMap)((EntityLivingBase)this.trackedEntity).getAttributeMap();
            final Set<IAttributeInstance> set = serversideattributemap.getAttributeInstanceSet();
            if (!set.isEmpty()) {
                this.func_151261_b(new S20PacketEntityProperties(this.trackedEntity.getEntityId(), set));
            }
            set.clear();
        }
    }
    
    public void sendPacketToTrackedPlayers(final Packet packetIn) {
        for (final EntityPlayerMP entityplayermp : this.trackingPlayers) {
            entityplayermp.playerNetServerHandler.sendPacket(packetIn);
        }
    }
    
    public void func_151261_b(final Packet packetIn) {
        this.sendPacketToTrackedPlayers(packetIn);
        if (this.trackedEntity instanceof EntityPlayerMP) {
            ((EntityPlayerMP)this.trackedEntity).playerNetServerHandler.sendPacket(packetIn);
        }
    }
    
    public void sendDestroyEntityPacketToTrackedPlayers() {
        for (final EntityPlayerMP entityplayermp : this.trackingPlayers) {
            entityplayermp.removeEntity(this.trackedEntity);
        }
    }
    
    public void removeFromTrackedPlayers(final EntityPlayerMP playerMP) {
        if (this.trackingPlayers.contains(playerMP)) {
            playerMP.removeEntity(this.trackedEntity);
            this.trackingPlayers.remove(playerMP);
        }
    }
    
    public void updatePlayerEntity(final EntityPlayerMP playerMP) {
        if (playerMP != this.trackedEntity) {
            if (this.func_180233_c(playerMP)) {
                if (!this.trackingPlayers.contains(playerMP) && (this.isPlayerWatchingThisChunk(playerMP) || this.trackedEntity.forceSpawn)) {
                    this.trackingPlayers.add(playerMP);
                    final Packet packet = this.func_151260_c();
                    playerMP.playerNetServerHandler.sendPacket(packet);
                    if (!this.trackedEntity.getDataWatcher().getIsBlank()) {
                        playerMP.playerNetServerHandler.sendPacket(new S1CPacketEntityMetadata(this.trackedEntity.getEntityId(), this.trackedEntity.getDataWatcher(), true));
                    }
                    final NBTTagCompound nbttagcompound = this.trackedEntity.getNBTTagCompound();
                    if (nbttagcompound != null) {
                        playerMP.playerNetServerHandler.sendPacket(new S49PacketUpdateEntityNBT(this.trackedEntity.getEntityId(), nbttagcompound));
                    }
                    if (this.trackedEntity instanceof EntityLivingBase) {
                        final ServersideAttributeMap serversideattributemap = (ServersideAttributeMap)((EntityLivingBase)this.trackedEntity).getAttributeMap();
                        final Collection<IAttributeInstance> collection = serversideattributemap.getWatchedAttributes();
                        if (!collection.isEmpty()) {
                            playerMP.playerNetServerHandler.sendPacket(new S20PacketEntityProperties(this.trackedEntity.getEntityId(), collection));
                        }
                    }
                    this.lastTrackedEntityMotionX = this.trackedEntity.motionX;
                    this.lastTrackedEntityMotionY = this.trackedEntity.motionY;
                    this.motionZ = this.trackedEntity.motionZ;
                    if (this.sendVelocityUpdates && !(packet instanceof S0FPacketSpawnMob)) {
                        playerMP.playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(this.trackedEntity.getEntityId(), this.trackedEntity.motionX, this.trackedEntity.motionY, this.trackedEntity.motionZ));
                    }
                    if (this.trackedEntity.ridingEntity != null) {
                        playerMP.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(0, this.trackedEntity, this.trackedEntity.ridingEntity));
                    }
                    if (this.trackedEntity instanceof EntityLiving && ((EntityLiving)this.trackedEntity).getLeashedToEntity() != null) {
                        playerMP.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(1, this.trackedEntity, ((EntityLiving)this.trackedEntity).getLeashedToEntity()));
                    }
                    if (this.trackedEntity instanceof EntityLivingBase) {
                        for (int i = 0; i < 5; ++i) {
                            final ItemStack itemstack = ((EntityLivingBase)this.trackedEntity).getEquipmentInSlot(i);
                            if (itemstack != null) {
                                playerMP.playerNetServerHandler.sendPacket(new S04PacketEntityEquipment(this.trackedEntity.getEntityId(), i, itemstack));
                            }
                        }
                    }
                    if (this.trackedEntity instanceof EntityPlayer) {
                        final EntityPlayer entityplayer = (EntityPlayer)this.trackedEntity;
                        if (entityplayer.isPlayerSleeping()) {
                            playerMP.playerNetServerHandler.sendPacket(new S0APacketUseBed(entityplayer, new BlockPos(this.trackedEntity)));
                        }
                    }
                    if (this.trackedEntity instanceof EntityLivingBase) {
                        final EntityLivingBase entitylivingbase = (EntityLivingBase)this.trackedEntity;
                        for (final PotionEffect potioneffect : entitylivingbase.getActivePotionEffects()) {
                            playerMP.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(this.trackedEntity.getEntityId(), potioneffect));
                        }
                    }
                }
            }
            else if (this.trackingPlayers.contains(playerMP)) {
                this.trackingPlayers.remove(playerMP);
                playerMP.removeEntity(this.trackedEntity);
            }
        }
    }
    
    public boolean func_180233_c(final EntityPlayerMP playerMP) {
        final double d0 = playerMP.posX - this.encodedPosX / 32;
        final double d2 = playerMP.posZ - this.encodedPosZ / 32;
        return d0 >= -this.trackingDistanceThreshold && d0 <= this.trackingDistanceThreshold && d2 >= -this.trackingDistanceThreshold && d2 <= this.trackingDistanceThreshold && this.trackedEntity.isSpectatedByPlayer(playerMP);
    }
    
    private boolean isPlayerWatchingThisChunk(final EntityPlayerMP playerMP) {
        return playerMP.getServerForPlayer().getPlayerManager().isPlayerWatchingChunk(playerMP, this.trackedEntity.chunkCoordX, this.trackedEntity.chunkCoordZ);
    }
    
    public void updatePlayerEntities(final List<EntityPlayer> p_73125_1_) {
        for (int i = 0; i < p_73125_1_.size(); ++i) {
            this.updatePlayerEntity(p_73125_1_.get(i));
        }
    }
    
    private Packet func_151260_c() {
        if (this.trackedEntity.isDead) {
            EntityTrackerEntry.logger.warn("Fetching addPacket for removed entity");
        }
        if (this.trackedEntity instanceof EntityItem) {
            return new S0EPacketSpawnObject(this.trackedEntity, 2, 1);
        }
        if (this.trackedEntity instanceof EntityPlayerMP) {
            return new S0CPacketSpawnPlayer((EntityPlayer)this.trackedEntity);
        }
        if (this.trackedEntity instanceof EntityMinecart) {
            final EntityMinecart entityminecart = (EntityMinecart)this.trackedEntity;
            return new S0EPacketSpawnObject(this.trackedEntity, 10, entityminecart.getMinecartType().getNetworkID());
        }
        if (this.trackedEntity instanceof EntityBoat) {
            return new S0EPacketSpawnObject(this.trackedEntity, 1);
        }
        if (this.trackedEntity instanceof IAnimals) {
            this.lastHeadMotion = MathHelper.floor_float(this.trackedEntity.getRotationYawHead() * 256.0f / 360.0f);
            return new S0FPacketSpawnMob((EntityLivingBase)this.trackedEntity);
        }
        if (this.trackedEntity instanceof EntityFishHook) {
            final Entity entity1 = ((EntityFishHook)this.trackedEntity).angler;
            return new S0EPacketSpawnObject(this.trackedEntity, 90, (entity1 != null) ? entity1.getEntityId() : this.trackedEntity.getEntityId());
        }
        if (this.trackedEntity instanceof EntityArrow) {
            final Entity entity2 = ((EntityArrow)this.trackedEntity).shootingEntity;
            return new S0EPacketSpawnObject(this.trackedEntity, 60, (entity2 != null) ? entity2.getEntityId() : this.trackedEntity.getEntityId());
        }
        if (this.trackedEntity instanceof EntitySnowball) {
            return new S0EPacketSpawnObject(this.trackedEntity, 61);
        }
        if (this.trackedEntity instanceof EntityPotion) {
            return new S0EPacketSpawnObject(this.trackedEntity, 73, ((EntityPotion)this.trackedEntity).getPotionDamage());
        }
        if (this.trackedEntity instanceof EntityExpBottle) {
            return new S0EPacketSpawnObject(this.trackedEntity, 75);
        }
        if (this.trackedEntity instanceof EntityEnderPearl) {
            return new S0EPacketSpawnObject(this.trackedEntity, 65);
        }
        if (this.trackedEntity instanceof EntityEnderEye) {
            return new S0EPacketSpawnObject(this.trackedEntity, 72);
        }
        if (this.trackedEntity instanceof EntityFireworkRocket) {
            return new S0EPacketSpawnObject(this.trackedEntity, 76);
        }
        if (this.trackedEntity instanceof EntityFireball) {
            final EntityFireball entityfireball = (EntityFireball)this.trackedEntity;
            S0EPacketSpawnObject s0epacketspawnobject2 = null;
            int i = 63;
            if (this.trackedEntity instanceof EntitySmallFireball) {
                i = 64;
            }
            else if (this.trackedEntity instanceof EntityWitherSkull) {
                i = 66;
            }
            if (entityfireball.shootingEntity != null) {
                s0epacketspawnobject2 = new S0EPacketSpawnObject(this.trackedEntity, i, ((EntityFireball)this.trackedEntity).shootingEntity.getEntityId());
            }
            else {
                s0epacketspawnobject2 = new S0EPacketSpawnObject(this.trackedEntity, i, 0);
            }
            s0epacketspawnobject2.setSpeedX((int)(entityfireball.accelerationX * 8000.0));
            s0epacketspawnobject2.setSpeedY((int)(entityfireball.accelerationY * 8000.0));
            s0epacketspawnobject2.setSpeedZ((int)(entityfireball.accelerationZ * 8000.0));
            return s0epacketspawnobject2;
        }
        if (this.trackedEntity instanceof EntityEgg) {
            return new S0EPacketSpawnObject(this.trackedEntity, 62);
        }
        if (this.trackedEntity instanceof EntityTNTPrimed) {
            return new S0EPacketSpawnObject(this.trackedEntity, 50);
        }
        if (this.trackedEntity instanceof EntityEnderCrystal) {
            return new S0EPacketSpawnObject(this.trackedEntity, 51);
        }
        if (this.trackedEntity instanceof EntityFallingBlock) {
            final EntityFallingBlock entityfallingblock = (EntityFallingBlock)this.trackedEntity;
            return new S0EPacketSpawnObject(this.trackedEntity, 70, Block.getStateId(entityfallingblock.getBlock()));
        }
        if (this.trackedEntity instanceof EntityArmorStand) {
            return new S0EPacketSpawnObject(this.trackedEntity, 78);
        }
        if (this.trackedEntity instanceof EntityPainting) {
            return new S10PacketSpawnPainting((EntityPainting)this.trackedEntity);
        }
        if (this.trackedEntity instanceof EntityItemFrame) {
            final EntityItemFrame entityitemframe = (EntityItemFrame)this.trackedEntity;
            final S0EPacketSpawnObject s0epacketspawnobject3 = new S0EPacketSpawnObject(this.trackedEntity, 71, entityitemframe.facingDirection.getHorizontalIndex());
            final BlockPos blockpos1 = entityitemframe.getHangingPosition();
            s0epacketspawnobject3.setX(MathHelper.floor_float((float)(blockpos1.getX() * 32)));
            s0epacketspawnobject3.setY(MathHelper.floor_float((float)(blockpos1.getY() * 32)));
            s0epacketspawnobject3.setZ(MathHelper.floor_float((float)(blockpos1.getZ() * 32)));
            return s0epacketspawnobject3;
        }
        if (this.trackedEntity instanceof EntityLeashKnot) {
            final EntityLeashKnot entityleashknot = (EntityLeashKnot)this.trackedEntity;
            final S0EPacketSpawnObject s0epacketspawnobject4 = new S0EPacketSpawnObject(this.trackedEntity, 77);
            final BlockPos blockpos2 = entityleashknot.getHangingPosition();
            s0epacketspawnobject4.setX(MathHelper.floor_float((float)(blockpos2.getX() * 32)));
            s0epacketspawnobject4.setY(MathHelper.floor_float((float)(blockpos2.getY() * 32)));
            s0epacketspawnobject4.setZ(MathHelper.floor_float((float)(blockpos2.getZ() * 32)));
            return s0epacketspawnobject4;
        }
        if (this.trackedEntity instanceof EntityXPOrb) {
            return new S11PacketSpawnExperienceOrb((EntityXPOrb)this.trackedEntity);
        }
        throw new IllegalArgumentException("Don't know how to add " + this.trackedEntity.getClass() + "!");
    }
    
    public void removeTrackedPlayerSymmetric(final EntityPlayerMP playerMP) {
        if (this.trackingPlayers.contains(playerMP)) {
            this.trackingPlayers.remove(playerMP);
            playerMP.removeEntity(this.trackedEntity);
        }
    }
}
