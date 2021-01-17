// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network;

import net.minecraft.server.management.UserList;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.init.Items;
import java.io.IOException;
import net.minecraft.item.ItemWritableBook;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C15PacketClientSettings;
import java.util.Iterator;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import java.util.List;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import com.google.common.collect.Lists;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import java.util.Date;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatAllowedCharacters;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import java.util.Set;
import java.util.Collections;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Doubles;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.network.play.client.C0CPacketInput;
import com.google.common.util.concurrent.Futures;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.IntHashMap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.ITickable;
import net.minecraft.network.play.INetHandlerPlayServer;

public class NetHandlerPlayServer implements INetHandlerPlayServer, ITickable
{
    private static final Logger logger;
    public final NetworkManager netManager;
    private final MinecraftServer serverController;
    public EntityPlayerMP playerEntity;
    private int networkTickCount;
    private int field_175090_f;
    private int floatingTickCount;
    private boolean field_147366_g;
    private int field_147378_h;
    private long lastPingTime;
    private long lastSentPingPacket;
    private int chatSpamThresholdCount;
    private int itemDropThreshold;
    private IntHashMap<Short> field_147372_n;
    private double lastPosX;
    private double lastPosY;
    private double lastPosZ;
    private boolean hasMoved;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public NetHandlerPlayServer(final MinecraftServer server, final NetworkManager networkManagerIn, final EntityPlayerMP playerIn) {
        this.field_147372_n = new IntHashMap<Short>();
        this.hasMoved = true;
        this.serverController = server;
        (this.netManager = networkManagerIn).setNetHandler(this);
        this.playerEntity = playerIn;
        playerIn.playerNetServerHandler = this;
    }
    
    @Override
    public void update() {
        this.field_147366_g = false;
        ++this.networkTickCount;
        this.serverController.theProfiler.startSection("keepAlive");
        if (this.networkTickCount - this.lastSentPingPacket > 40L) {
            this.lastSentPingPacket = this.networkTickCount;
            this.lastPingTime = this.currentTimeMillis();
            this.field_147378_h = (int)this.lastPingTime;
            this.sendPacket(new S00PacketKeepAlive(this.field_147378_h));
        }
        this.serverController.theProfiler.endSection();
        if (this.chatSpamThresholdCount > 0) {
            --this.chatSpamThresholdCount;
        }
        if (this.itemDropThreshold > 0) {
            --this.itemDropThreshold;
        }
        if (this.playerEntity.getLastActiveTime() > 0L && this.serverController.getMaxPlayerIdleMinutes() > 0 && MinecraftServer.getCurrentTimeMillis() - this.playerEntity.getLastActiveTime() > this.serverController.getMaxPlayerIdleMinutes() * 1000 * 60) {
            this.kickPlayerFromServer("You have been idle for too long!");
        }
    }
    
    public NetworkManager getNetworkManager() {
        return this.netManager;
    }
    
    public void kickPlayerFromServer(final String reason) {
        final ChatComponentText chatcomponenttext = new ChatComponentText(reason);
        this.netManager.sendPacket(new S40PacketDisconnect(chatcomponenttext), new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(final Future<? super Void> p_operationComplete_1_) throws Exception {
                NetHandlerPlayServer.this.netManager.closeChannel(chatcomponenttext);
            }
        }, (GenericFutureListener<? extends Future<? super Void>>[])new GenericFutureListener[0]);
        this.netManager.disableAutoRead();
        Futures.getUnchecked(this.serverController.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                NetHandlerPlayServer.this.netManager.checkDisconnected();
            }
        }));
    }
    
    @Override
    public void processInput(final C0CPacketInput packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.setEntityActionState(packetIn.getStrafeSpeed(), packetIn.getForwardSpeed(), packetIn.isJumping(), packetIn.isSneaking());
    }
    
    private boolean func_183006_b(final C03PacketPlayer p_183006_1_) {
        return !Doubles.isFinite(p_183006_1_.getPositionX()) || !Doubles.isFinite(p_183006_1_.getPositionY()) || !Doubles.isFinite(p_183006_1_.getPositionZ()) || !Floats.isFinite(p_183006_1_.getPitch()) || !Floats.isFinite(p_183006_1_.getYaw());
    }
    
    @Override
    public void processPlayer(final C03PacketPlayer packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        if (this.func_183006_b(packetIn)) {
            this.kickPlayerFromServer("Invalid move packet received");
        }
        else {
            final WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
            this.field_147366_g = true;
            if (!this.playerEntity.playerConqueredTheEnd) {
                final double d0 = this.playerEntity.posX;
                final double d2 = this.playerEntity.posY;
                final double d3 = this.playerEntity.posZ;
                double d4 = 0.0;
                final double d5 = packetIn.getPositionX() - this.lastPosX;
                final double d6 = packetIn.getPositionY() - this.lastPosY;
                final double d7 = packetIn.getPositionZ() - this.lastPosZ;
                if (packetIn.isMoving()) {
                    d4 = d5 * d5 + d6 * d6 + d7 * d7;
                    if (!this.hasMoved && d4 < 0.25) {
                        this.hasMoved = true;
                    }
                }
                if (this.hasMoved) {
                    this.field_175090_f = this.networkTickCount;
                    if (this.playerEntity.ridingEntity != null) {
                        float f4 = this.playerEntity.rotationYaw;
                        float f5 = this.playerEntity.rotationPitch;
                        this.playerEntity.ridingEntity.updateRiderPosition();
                        final double d8 = this.playerEntity.posX;
                        final double d9 = this.playerEntity.posY;
                        final double d10 = this.playerEntity.posZ;
                        if (packetIn.getRotating()) {
                            f4 = packetIn.getYaw();
                            f5 = packetIn.getPitch();
                        }
                        this.playerEntity.onGround = packetIn.isOnGround();
                        this.playerEntity.onUpdateEntity();
                        this.playerEntity.setPositionAndRotation(d8, d9, d10, f4, f5);
                        if (this.playerEntity.ridingEntity != null) {
                            this.playerEntity.ridingEntity.updateRiderPosition();
                        }
                        this.serverController.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
                        if (this.playerEntity.ridingEntity != null) {
                            if (d4 > 4.0) {
                                final Entity entity = this.playerEntity.ridingEntity;
                                this.playerEntity.playerNetServerHandler.sendPacket(new S18PacketEntityTeleport(entity));
                                this.setPlayerLocation(this.playerEntity.posX, this.playerEntity.posY, this.playerEntity.posZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                            }
                            this.playerEntity.ridingEntity.isAirBorne = true;
                        }
                        if (this.hasMoved) {
                            this.lastPosX = this.playerEntity.posX;
                            this.lastPosY = this.playerEntity.posY;
                            this.lastPosZ = this.playerEntity.posZ;
                        }
                        worldserver.updateEntity(this.playerEntity);
                        return;
                    }
                    if (this.playerEntity.isPlayerSleeping()) {
                        this.playerEntity.onUpdateEntity();
                        this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                        worldserver.updateEntity(this.playerEntity);
                        return;
                    }
                    final double d11 = this.playerEntity.posY;
                    this.lastPosX = this.playerEntity.posX;
                    this.lastPosY = this.playerEntity.posY;
                    this.lastPosZ = this.playerEntity.posZ;
                    double d12 = this.playerEntity.posX;
                    double d13 = this.playerEntity.posY;
                    double d14 = this.playerEntity.posZ;
                    float f6 = this.playerEntity.rotationYaw;
                    float f7 = this.playerEntity.rotationPitch;
                    if (packetIn.isMoving() && packetIn.getPositionY() == -999.0) {
                        packetIn.setMoving(false);
                    }
                    if (packetIn.isMoving()) {
                        d12 = packetIn.getPositionX();
                        d13 = packetIn.getPositionY();
                        d14 = packetIn.getPositionZ();
                        if (Math.abs(packetIn.getPositionX()) > 3.0E7 || Math.abs(packetIn.getPositionZ()) > 3.0E7) {
                            this.kickPlayerFromServer("Illegal position");
                            return;
                        }
                    }
                    if (packetIn.getRotating()) {
                        f6 = packetIn.getYaw();
                        f7 = packetIn.getPitch();
                    }
                    this.playerEntity.onUpdateEntity();
                    this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, f6, f7);
                    if (!this.hasMoved) {
                        return;
                    }
                    double d15 = d12 - this.playerEntity.posX;
                    double d16 = d13 - this.playerEntity.posY;
                    double d17 = d14 - this.playerEntity.posZ;
                    final double d18 = this.playerEntity.motionX * this.playerEntity.motionX + this.playerEntity.motionY * this.playerEntity.motionY + this.playerEntity.motionZ * this.playerEntity.motionZ;
                    double d19 = d15 * d15 + d16 * d16 + d17 * d17;
                    if (d19 - d18 > 100.0 && (!this.serverController.isSinglePlayer() || !this.serverController.getServerOwner().equals(this.playerEntity.getName()))) {
                        NetHandlerPlayServer.logger.warn(String.valueOf(this.playerEntity.getName()) + " moved too quickly! " + d15 + "," + d16 + "," + d17 + " (" + d15 + ", " + d16 + ", " + d17 + ")");
                        this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                        return;
                    }
                    final float f8 = 0.0625f;
                    final boolean flag = worldserver.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.getEntityBoundingBox().contract(f8, f8, f8)).isEmpty();
                    if (this.playerEntity.onGround && !packetIn.isOnGround() && d16 > 0.0) {
                        this.playerEntity.jump();
                    }
                    this.playerEntity.moveEntity(d15, d16, d17);
                    this.playerEntity.onGround = packetIn.isOnGround();
                    d15 = d12 - this.playerEntity.posX;
                    d16 = d13 - this.playerEntity.posY;
                    if (d16 > -0.5 || d16 < 0.5) {
                        d16 = 0.0;
                    }
                    d17 = d14 - this.playerEntity.posZ;
                    d19 = d15 * d15 + d16 * d16 + d17 * d17;
                    boolean flag2 = false;
                    if (d19 > 0.0625 && !this.playerEntity.isPlayerSleeping() && !this.playerEntity.theItemInWorldManager.isCreative()) {
                        flag2 = true;
                        NetHandlerPlayServer.logger.warn(String.valueOf(this.playerEntity.getName()) + " moved wrongly!");
                    }
                    this.playerEntity.setPositionAndRotation(d12, d13, d14, f6, f7);
                    this.playerEntity.addMovementStat(this.playerEntity.posX - d0, this.playerEntity.posY - d2, this.playerEntity.posZ - d3);
                    if (!this.playerEntity.noClip) {
                        final boolean flag3 = worldserver.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.getEntityBoundingBox().contract(f8, f8, f8)).isEmpty();
                        if (flag && (flag2 || !flag3) && !this.playerEntity.isPlayerSleeping()) {
                            this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, f6, f7);
                            return;
                        }
                    }
                    final AxisAlignedBB axisalignedbb = this.playerEntity.getEntityBoundingBox().expand(f8, f8, f8).addCoord(0.0, -0.55, 0.0);
                    if (!this.serverController.isFlightAllowed() && !this.playerEntity.capabilities.allowFlying && !worldserver.checkBlockCollision(axisalignedbb)) {
                        if (d16 >= -0.03125) {
                            ++this.floatingTickCount;
                            if (this.floatingTickCount > 80) {
                                NetHandlerPlayServer.logger.warn(String.valueOf(this.playerEntity.getName()) + " was kicked for floating too long!");
                                this.kickPlayerFromServer("Flying is not enabled on this server");
                                return;
                            }
                        }
                    }
                    else {
                        this.floatingTickCount = 0;
                    }
                    this.playerEntity.onGround = packetIn.isOnGround();
                    this.serverController.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
                    this.playerEntity.handleFalling(this.playerEntity.posY - d11, packetIn.isOnGround());
                }
                else if (this.networkTickCount - this.field_175090_f > 20) {
                    this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                }
            }
        }
    }
    
    public void setPlayerLocation(final double x, final double y, final double z, final float yaw, final float pitch) {
        this.setPlayerLocation(x, y, z, yaw, pitch, Collections.emptySet());
    }
    
    public void setPlayerLocation(final double x, final double y, final double z, final float yaw, final float pitch, final Set<S08PacketPlayerPosLook.EnumFlags> relativeSet) {
        this.hasMoved = false;
        this.lastPosX = x;
        this.lastPosY = y;
        this.lastPosZ = z;
        if (relativeSet.contains(S08PacketPlayerPosLook.EnumFlags.X)) {
            this.lastPosX += this.playerEntity.posX;
        }
        if (relativeSet.contains(S08PacketPlayerPosLook.EnumFlags.Y)) {
            this.lastPosY += this.playerEntity.posY;
        }
        if (relativeSet.contains(S08PacketPlayerPosLook.EnumFlags.Z)) {
            this.lastPosZ += this.playerEntity.posZ;
        }
        float f = yaw;
        float f2 = pitch;
        if (relativeSet.contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT)) {
            f = yaw + this.playerEntity.rotationYaw;
        }
        if (relativeSet.contains(S08PacketPlayerPosLook.EnumFlags.X_ROT)) {
            f2 = pitch + this.playerEntity.rotationPitch;
        }
        this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, f, f2);
        this.playerEntity.playerNetServerHandler.sendPacket(new S08PacketPlayerPosLook(x, y, z, yaw, pitch, relativeSet));
    }
    
    @Override
    public void processPlayerDigging(final C07PacketPlayerDigging packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        final WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        final BlockPos blockpos = packetIn.getPosition();
        this.playerEntity.markPlayerActive();
        switch (packetIn.getStatus()) {
            case DROP_ITEM: {
                if (!this.playerEntity.isSpectator()) {
                    this.playerEntity.dropOneItem(false);
                }
            }
            case DROP_ALL_ITEMS: {
                if (!this.playerEntity.isSpectator()) {
                    this.playerEntity.dropOneItem(true);
                }
            }
            case RELEASE_USE_ITEM: {
                this.playerEntity.stopUsingItem();
            }
            case START_DESTROY_BLOCK:
            case ABORT_DESTROY_BLOCK:
            case STOP_DESTROY_BLOCK: {
                final double d0 = this.playerEntity.posX - (blockpos.getX() + 0.5);
                final double d2 = this.playerEntity.posY - (blockpos.getY() + 0.5) + 1.5;
                final double d3 = this.playerEntity.posZ - (blockpos.getZ() + 0.5);
                final double d4 = d0 * d0 + d2 * d2 + d3 * d3;
                if (d4 > 36.0) {
                    return;
                }
                if (blockpos.getY() >= this.serverController.getBuildLimit()) {
                    return;
                }
                if (packetIn.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                    if (!this.serverController.isBlockProtected(worldserver, blockpos, this.playerEntity) && worldserver.getWorldBorder().contains(blockpos)) {
                        this.playerEntity.theItemInWorldManager.onBlockClicked(blockpos, packetIn.getFacing());
                    }
                    else {
                        this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldserver, blockpos));
                    }
                }
                else {
                    if (packetIn.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                        this.playerEntity.theItemInWorldManager.blockRemoving(blockpos);
                    }
                    else if (packetIn.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK) {
                        this.playerEntity.theItemInWorldManager.cancelDestroyingBlock();
                    }
                    if (worldserver.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
                        this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldserver, blockpos));
                    }
                }
            }
            default: {
                throw new IllegalArgumentException("Invalid player action");
            }
        }
    }
    
    @Override
    public void processPlayerBlockPlacement(final C08PacketPlayerBlockPlacement packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        final WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        ItemStack itemstack = this.playerEntity.inventory.getCurrentItem();
        boolean flag = false;
        final BlockPos blockpos = packetIn.getPosition();
        final EnumFacing enumfacing = EnumFacing.getFront(packetIn.getPlacedBlockDirection());
        this.playerEntity.markPlayerActive();
        if (packetIn.getPlacedBlockDirection() == 255) {
            if (itemstack == null) {
                return;
            }
            this.playerEntity.theItemInWorldManager.tryUseItem(this.playerEntity, worldserver, itemstack);
        }
        else if (blockpos.getY() < this.serverController.getBuildLimit() - 1 || (enumfacing != EnumFacing.UP && blockpos.getY() < this.serverController.getBuildLimit())) {
            if (this.hasMoved && this.playerEntity.getDistanceSq(blockpos.getX() + 0.5, blockpos.getY() + 0.5, blockpos.getZ() + 0.5) < 64.0 && !this.serverController.isBlockProtected(worldserver, blockpos, this.playerEntity) && worldserver.getWorldBorder().contains(blockpos)) {
                this.playerEntity.theItemInWorldManager.activateBlockOrUseItem(this.playerEntity, worldserver, itemstack, blockpos, enumfacing, packetIn.getPlacedBlockOffsetX(), packetIn.getPlacedBlockOffsetY(), packetIn.getPlacedBlockOffsetZ());
            }
            flag = true;
        }
        else {
            final ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("build.tooHigh", new Object[] { this.serverController.getBuildLimit() });
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            this.playerEntity.playerNetServerHandler.sendPacket(new S02PacketChat(chatcomponenttranslation));
            flag = true;
        }
        if (flag) {
            this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldserver, blockpos));
            this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldserver, blockpos.offset(enumfacing)));
        }
        itemstack = this.playerEntity.inventory.getCurrentItem();
        if (itemstack != null && itemstack.stackSize == 0) {
            this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = null;
            itemstack = null;
        }
        if (itemstack == null || itemstack.getMaxItemUseDuration() == 0) {
            this.playerEntity.isChangingQuantityOnly = true;
            this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = ItemStack.copyItemStack(this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem]);
            final Slot slot = this.playerEntity.openContainer.getSlotFromInventory(this.playerEntity.inventory, this.playerEntity.inventory.currentItem);
            this.playerEntity.openContainer.detectAndSendChanges();
            this.playerEntity.isChangingQuantityOnly = false;
            if (!ItemStack.areItemStacksEqual(this.playerEntity.inventory.getCurrentItem(), packetIn.getStack())) {
                this.sendPacket(new S2FPacketSetSlot(this.playerEntity.openContainer.windowId, slot.slotNumber, this.playerEntity.inventory.getCurrentItem()));
            }
        }
    }
    
    @Override
    public void handleSpectate(final C18PacketSpectate packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        if (this.playerEntity.isSpectator()) {
            Entity entity = null;
            WorldServer[] worldServers;
            for (int length = (worldServers = this.serverController.worldServers).length, i = 0; i < length; ++i) {
                final WorldServer worldserver = worldServers[i];
                if (worldserver != null) {
                    entity = packetIn.getEntity(worldserver);
                    if (entity != null) {
                        break;
                    }
                }
            }
            if (entity != null) {
                this.playerEntity.setSpectatingEntity(this.playerEntity);
                this.playerEntity.mountEntity(null);
                if (entity.worldObj != this.playerEntity.worldObj) {
                    final WorldServer worldserver2 = this.playerEntity.getServerForPlayer();
                    final WorldServer worldserver3 = (WorldServer)entity.worldObj;
                    this.playerEntity.dimension = entity.dimension;
                    this.sendPacket(new S07PacketRespawn(this.playerEntity.dimension, worldserver2.getDifficulty(), worldserver2.getWorldInfo().getTerrainType(), this.playerEntity.theItemInWorldManager.getGameType()));
                    worldserver2.removePlayerEntityDangerously(this.playerEntity);
                    this.playerEntity.isDead = false;
                    this.playerEntity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
                    if (this.playerEntity.isEntityAlive()) {
                        worldserver2.updateEntityWithOptionalForce(this.playerEntity, false);
                        worldserver3.spawnEntityInWorld(this.playerEntity);
                        worldserver3.updateEntityWithOptionalForce(this.playerEntity, false);
                    }
                    this.playerEntity.setWorld(worldserver3);
                    this.serverController.getConfigurationManager().preparePlayer(this.playerEntity, worldserver2);
                    this.playerEntity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
                    this.playerEntity.theItemInWorldManager.setWorld(worldserver3);
                    this.serverController.getConfigurationManager().updateTimeAndWeatherForPlayer(this.playerEntity, worldserver3);
                    this.serverController.getConfigurationManager().syncPlayerInventory(this.playerEntity);
                }
                else {
                    this.playerEntity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
                }
            }
        }
    }
    
    @Override
    public void handleResourcePackStatus(final C19PacketResourcePackStatus packetIn) {
    }
    
    @Override
    public void onDisconnect(final IChatComponent reason) {
        NetHandlerPlayServer.logger.info(String.valueOf(this.playerEntity.getName()) + " lost connection: " + reason);
        this.serverController.refreshStatusNextTick();
        final ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("multiplayer.player.left", new Object[] { this.playerEntity.getDisplayName() });
        chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.YELLOW);
        this.serverController.getConfigurationManager().sendChatMsg(chatcomponenttranslation);
        this.playerEntity.mountEntityAndWakeUp();
        this.serverController.getConfigurationManager().playerLoggedOut(this.playerEntity);
        if (this.serverController.isSinglePlayer() && this.playerEntity.getName().equals(this.serverController.getServerOwner())) {
            NetHandlerPlayServer.logger.info("Stopping singleplayer server as player logged out");
            this.serverController.initiateShutdown();
        }
    }
    
    public void sendPacket(final Packet packetIn) {
        if (packetIn instanceof S02PacketChat) {
            final S02PacketChat s02packetchat = (S02PacketChat)packetIn;
            final EntityPlayer.EnumChatVisibility entityplayer$enumchatvisibility = this.playerEntity.getChatVisibility();
            if (entityplayer$enumchatvisibility == EntityPlayer.EnumChatVisibility.HIDDEN) {
                return;
            }
            if (entityplayer$enumchatvisibility == EntityPlayer.EnumChatVisibility.SYSTEM && !s02packetchat.isChat()) {
                return;
            }
        }
        try {
            this.netManager.sendPacket(packetIn);
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Sending packet");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Packet being sent");
            crashreportcategory.addCrashSectionCallable("Packet class", new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return packetIn.getClass().getCanonicalName();
                }
            });
            throw new ReportedException(crashreport);
        }
    }
    
    @Override
    public void processHeldItemChange(final C09PacketHeldItemChange packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        if (packetIn.getSlotId() >= 0 && packetIn.getSlotId() < InventoryPlayer.getHotbarSize()) {
            this.playerEntity.inventory.currentItem = packetIn.getSlotId();
            this.playerEntity.markPlayerActive();
        }
        else {
            NetHandlerPlayServer.logger.warn(String.valueOf(this.playerEntity.getName()) + " tried to set an invalid carried item");
        }
    }
    
    @Override
    public void processChatMessage(final C01PacketChatMessage packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        if (this.playerEntity.getChatVisibility() == EntityPlayer.EnumChatVisibility.HIDDEN) {
            final ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("chat.cannotSend", new Object[0]);
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            this.sendPacket(new S02PacketChat(chatcomponenttranslation));
        }
        else {
            this.playerEntity.markPlayerActive();
            String s = packetIn.getMessage();
            s = StringUtils.normalizeSpace(s);
            for (int i = 0; i < s.length(); ++i) {
                if (!ChatAllowedCharacters.isAllowedCharacter(s.charAt(i))) {
                    this.kickPlayerFromServer("Illegal characters in chat");
                    return;
                }
            }
            if (s.startsWith("/")) {
                this.handleSlashCommand(s);
            }
            else {
                final IChatComponent ichatcomponent = new ChatComponentTranslation("chat.type.text", new Object[] { this.playerEntity.getDisplayName(), s });
                this.serverController.getConfigurationManager().sendChatMsgImpl(ichatcomponent, false);
            }
            this.chatSpamThresholdCount += 20;
            if (this.chatSpamThresholdCount > 200 && !this.serverController.getConfigurationManager().canSendCommands(this.playerEntity.getGameProfile())) {
                this.kickPlayerFromServer("disconnect.spam");
            }
        }
    }
    
    private void handleSlashCommand(final String command) {
        this.serverController.getCommandManager().executeCommand(this.playerEntity, command);
    }
    
    @Override
    public void handleAnimation(final C0APacketAnimation packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        this.playerEntity.swingItem();
    }
    
    @Override
    public void processEntityAction(final C0BPacketEntityAction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        switch (packetIn.getAction()) {
            case START_SNEAKING: {
                this.playerEntity.setSneaking(true);
                break;
            }
            case STOP_SNEAKING: {
                this.playerEntity.setSneaking(false);
                break;
            }
            case START_SPRINTING: {
                this.playerEntity.setSprinting(true);
                break;
            }
            case STOP_SPRINTING: {
                this.playerEntity.setSprinting(false);
                break;
            }
            case STOP_SLEEPING: {
                this.playerEntity.wakeUpPlayer(false, true, true);
                this.hasMoved = false;
                break;
            }
            case RIDING_JUMP: {
                if (this.playerEntity.ridingEntity instanceof EntityHorse) {
                    ((EntityHorse)this.playerEntity.ridingEntity).setJumpPower(packetIn.getAuxData());
                    break;
                }
                break;
            }
            case OPEN_INVENTORY: {
                if (this.playerEntity.ridingEntity instanceof EntityHorse) {
                    ((EntityHorse)this.playerEntity.ridingEntity).openGUI(this.playerEntity);
                    break;
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid client command!");
            }
        }
    }
    
    @Override
    public void processUseEntity(final C02PacketUseEntity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        final WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        final Entity entity = packetIn.getEntityFromWorld(worldserver);
        this.playerEntity.markPlayerActive();
        if (entity != null) {
            final boolean flag = this.playerEntity.canEntityBeSeen(entity);
            double d0 = 36.0;
            if (!flag) {
                d0 = 9.0;
            }
            if (this.playerEntity.getDistanceSqToEntity(entity) < d0) {
                if (packetIn.getAction() == C02PacketUseEntity.Action.INTERACT) {
                    this.playerEntity.interactWith(entity);
                }
                else if (packetIn.getAction() == C02PacketUseEntity.Action.INTERACT_AT) {
                    entity.interactAt(this.playerEntity, packetIn.getHitVec());
                }
                else if (packetIn.getAction() == C02PacketUseEntity.Action.ATTACK) {
                    if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow || entity == this.playerEntity) {
                        this.kickPlayerFromServer("Attempting to attack an invalid entity");
                        this.serverController.logWarning("Player " + this.playerEntity.getName() + " tried to attack an invalid entity");
                        return;
                    }
                    this.playerEntity.attackTargetEntityWithCurrentItem(entity);
                }
            }
        }
    }
    
    @Override
    public void processClientStatus(final C16PacketClientStatus packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        final C16PacketClientStatus.EnumState c16packetclientstatus$enumstate = packetIn.getStatus();
        switch (c16packetclientstatus$enumstate) {
            case PERFORM_RESPAWN: {
                if (this.playerEntity.playerConqueredTheEnd) {
                    this.playerEntity = this.serverController.getConfigurationManager().recreatePlayerEntity(this.playerEntity, 0, true);
                    break;
                }
                if (this.playerEntity.getServerForPlayer().getWorldInfo().isHardcoreModeEnabled()) {
                    if (this.serverController.isSinglePlayer() && this.playerEntity.getName().equals(this.serverController.getServerOwner())) {
                        this.playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it's game over!");
                        this.serverController.deleteWorldAndStopServer();
                        break;
                    }
                    final UserListBansEntry userlistbansentry = new UserListBansEntry(this.playerEntity.getGameProfile(), null, "(You just lost the game)", null, "Death in Hardcore");
                    ((UserList<K, UserListBansEntry>)this.serverController.getConfigurationManager().getBannedPlayers()).addEntry(userlistbansentry);
                    this.playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it's game over!");
                    break;
                }
                else {
                    if (this.playerEntity.getHealth() > 0.0f) {
                        return;
                    }
                    this.playerEntity = this.serverController.getConfigurationManager().recreatePlayerEntity(this.playerEntity, 0, false);
                    break;
                }
                break;
            }
            case REQUEST_STATS: {
                this.playerEntity.getStatFile().func_150876_a(this.playerEntity);
                break;
            }
            case OPEN_INVENTORY_ACHIEVEMENT: {
                this.playerEntity.triggerAchievement(AchievementList.openInventory);
                break;
            }
        }
    }
    
    @Override
    public void processCloseWindow(final C0DPacketCloseWindow packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.closeContainer();
    }
    
    @Override
    public void processClickWindow(final C0EPacketClickWindow packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        if (this.playerEntity.openContainer.windowId == packetIn.getWindowId() && this.playerEntity.openContainer.getCanCraft(this.playerEntity)) {
            if (this.playerEntity.isSpectator()) {
                final List<ItemStack> list = (List<ItemStack>)Lists.newArrayList();
                for (int i = 0; i < this.playerEntity.openContainer.inventorySlots.size(); ++i) {
                    list.add(this.playerEntity.openContainer.inventorySlots.get(i).getStack());
                }
                this.playerEntity.updateCraftingInventory(this.playerEntity.openContainer, list);
            }
            else {
                final ItemStack itemstack = this.playerEntity.openContainer.slotClick(packetIn.getSlotId(), packetIn.getUsedButton(), packetIn.getMode(), this.playerEntity);
                if (ItemStack.areItemStacksEqual(packetIn.getClickedItem(), itemstack)) {
                    this.playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
                    this.playerEntity.isChangingQuantityOnly = true;
                    this.playerEntity.openContainer.detectAndSendChanges();
                    this.playerEntity.updateHeldItem();
                    this.playerEntity.isChangingQuantityOnly = false;
                }
                else {
                    this.field_147372_n.addKey(this.playerEntity.openContainer.windowId, packetIn.getActionNumber());
                    this.playerEntity.playerNetServerHandler.sendPacket(new S32PacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), false));
                    this.playerEntity.openContainer.setCanCraft(this.playerEntity, false);
                    final List<ItemStack> list2 = (List<ItemStack>)Lists.newArrayList();
                    for (int j = 0; j < this.playerEntity.openContainer.inventorySlots.size(); ++j) {
                        list2.add(this.playerEntity.openContainer.inventorySlots.get(j).getStack());
                    }
                    this.playerEntity.updateCraftingInventory(this.playerEntity.openContainer, list2);
                }
            }
        }
    }
    
    @Override
    public void processEnchantItem(final C11PacketEnchantItem packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        if (this.playerEntity.openContainer.windowId == packetIn.getWindowId() && this.playerEntity.openContainer.getCanCraft(this.playerEntity) && !this.playerEntity.isSpectator()) {
            this.playerEntity.openContainer.enchantItem(this.playerEntity, packetIn.getButton());
            this.playerEntity.openContainer.detectAndSendChanges();
        }
    }
    
    @Override
    public void processCreativeInventoryAction(final C10PacketCreativeInventoryAction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        if (this.playerEntity.theItemInWorldManager.isCreative()) {
            final boolean flag = packetIn.getSlotId() < 0;
            final ItemStack itemstack = packetIn.getStack();
            if (itemstack != null && itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("BlockEntityTag", 10)) {
                final NBTTagCompound nbttagcompound = itemstack.getTagCompound().getCompoundTag("BlockEntityTag");
                if (nbttagcompound.hasKey("x") && nbttagcompound.hasKey("y") && nbttagcompound.hasKey("z")) {
                    final BlockPos blockpos = new BlockPos(nbttagcompound.getInteger("x"), nbttagcompound.getInteger("y"), nbttagcompound.getInteger("z"));
                    final TileEntity tileentity = this.playerEntity.worldObj.getTileEntity(blockpos);
                    if (tileentity != null) {
                        final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                        tileentity.writeToNBT(nbttagcompound2);
                        nbttagcompound2.removeTag("x");
                        nbttagcompound2.removeTag("y");
                        nbttagcompound2.removeTag("z");
                        itemstack.setTagInfo("BlockEntityTag", nbttagcompound2);
                    }
                }
            }
            final boolean flag2 = packetIn.getSlotId() >= 1 && packetIn.getSlotId() < 36 + InventoryPlayer.getHotbarSize();
            final boolean flag3 = itemstack == null || itemstack.getItem() != null;
            final boolean flag4 = itemstack == null || (itemstack.getMetadata() >= 0 && itemstack.stackSize <= 64 && itemstack.stackSize > 0);
            if (flag2 && flag3 && flag4) {
                if (itemstack == null) {
                    this.playerEntity.inventoryContainer.putStackInSlot(packetIn.getSlotId(), null);
                }
                else {
                    this.playerEntity.inventoryContainer.putStackInSlot(packetIn.getSlotId(), itemstack);
                }
                this.playerEntity.inventoryContainer.setCanCraft(this.playerEntity, true);
            }
            else if (flag && flag3 && flag4 && this.itemDropThreshold < 200) {
                this.itemDropThreshold += 20;
                final EntityItem entityitem = this.playerEntity.dropPlayerItemWithRandomChoice(itemstack, true);
                if (entityitem != null) {
                    entityitem.setAgeToCreativeDespawnTime();
                }
            }
        }
    }
    
    @Override
    public void processConfirmTransaction(final C0FPacketConfirmTransaction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        final Short oshort = this.field_147372_n.lookup(this.playerEntity.openContainer.windowId);
        if (oshort != null && packetIn.getUid() == oshort && this.playerEntity.openContainer.windowId == packetIn.getWindowId() && !this.playerEntity.openContainer.getCanCraft(this.playerEntity) && !this.playerEntity.isSpectator()) {
            this.playerEntity.openContainer.setCanCraft(this.playerEntity, true);
        }
    }
    
    @Override
    public void processUpdateSign(final C12PacketUpdateSign packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.markPlayerActive();
        final WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        final BlockPos blockpos = packetIn.getPosition();
        if (worldserver.isBlockLoaded(blockpos)) {
            final TileEntity tileentity = worldserver.getTileEntity(blockpos);
            if (!(tileentity instanceof TileEntitySign)) {
                return;
            }
            final TileEntitySign tileentitysign = (TileEntitySign)tileentity;
            if (!tileentitysign.getIsEditable() || tileentitysign.getPlayer() != this.playerEntity) {
                this.serverController.logWarning("Player " + this.playerEntity.getName() + " just tried to change non-editable sign");
                return;
            }
            final IChatComponent[] aichatcomponent = packetIn.getLines();
            for (int i = 0; i < aichatcomponent.length; ++i) {
                tileentitysign.signText[i] = new ChatComponentText(EnumChatFormatting.getTextWithoutFormattingCodes(aichatcomponent[i].getUnformattedText()));
            }
            tileentitysign.markDirty();
            worldserver.markBlockForUpdate(blockpos);
        }
    }
    
    @Override
    public void processKeepAlive(final C00PacketKeepAlive packetIn) {
        if (packetIn.getKey() == this.field_147378_h) {
            final int i = (int)(this.currentTimeMillis() - this.lastPingTime);
            this.playerEntity.ping = (this.playerEntity.ping * 3 + i) / 4;
        }
    }
    
    private long currentTimeMillis() {
        return System.nanoTime() / 1000000L;
    }
    
    @Override
    public void processPlayerAbilities(final C13PacketPlayerAbilities packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.capabilities.isFlying = (packetIn.isFlying() && this.playerEntity.capabilities.allowFlying);
    }
    
    @Override
    public void processTabComplete(final C14PacketTabComplete packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        final List<String> list = (List<String>)Lists.newArrayList();
        for (final String s : this.serverController.getTabCompletions(this.playerEntity, packetIn.getMessage(), packetIn.getTargetBlock())) {
            list.add(s);
        }
        this.playerEntity.playerNetServerHandler.sendPacket(new S3APacketTabComplete(list.toArray(new String[list.size()])));
    }
    
    @Override
    public void processClientSettings(final C15PacketClientSettings packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        this.playerEntity.handleClientSettings(packetIn);
    }
    
    @Override
    public void processVanilla250Packet(final C17PacketCustomPayload packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerForPlayer());
        if ("MC|BEdit".equals(packetIn.getChannelName())) {
            final PacketBuffer packetbuffer3 = new PacketBuffer(Unpooled.wrappedBuffer(packetIn.getBufferData()));
            try {
                final ItemStack itemstack1 = packetbuffer3.readItemStackFromBuffer();
                if (itemstack1 != null) {
                    if (!ItemWritableBook.isNBTValid(itemstack1.getTagCompound())) {
                        throw new IOException("Invalid book tag!");
                    }
                    final ItemStack itemstack2 = this.playerEntity.inventory.getCurrentItem();
                    if (itemstack2 == null) {
                        return;
                    }
                    if (itemstack1.getItem() == Items.writable_book && itemstack1.getItem() == itemstack2.getItem()) {
                        itemstack2.setTagInfo("pages", itemstack1.getTagCompound().getTagList("pages", 8));
                    }
                    return;
                }
            }
            catch (Exception exception3) {
                NetHandlerPlayServer.logger.error("Couldn't handle book info", exception3);
                return;
            }
            finally {
                packetbuffer3.release();
            }
            packetbuffer3.release();
            return;
        }
        if ("MC|BSign".equals(packetIn.getChannelName())) {
            final PacketBuffer packetbuffer4 = new PacketBuffer(Unpooled.wrappedBuffer(packetIn.getBufferData()));
            try {
                final ItemStack itemstack3 = packetbuffer4.readItemStackFromBuffer();
                if (itemstack3 != null) {
                    if (!ItemEditableBook.validBookTagContents(itemstack3.getTagCompound())) {
                        throw new IOException("Invalid book tag!");
                    }
                    final ItemStack itemstack4 = this.playerEntity.inventory.getCurrentItem();
                    if (itemstack4 == null) {
                        return;
                    }
                    if (itemstack3.getItem() == Items.written_book && itemstack4.getItem() == Items.writable_book) {
                        itemstack4.setTagInfo("author", new NBTTagString(this.playerEntity.getName()));
                        itemstack4.setTagInfo("title", new NBTTagString(itemstack3.getTagCompound().getString("title")));
                        itemstack4.setTagInfo("pages", itemstack3.getTagCompound().getTagList("pages", 8));
                        itemstack4.setItem(Items.written_book);
                    }
                    return;
                }
            }
            catch (Exception exception4) {
                NetHandlerPlayServer.logger.error("Couldn't sign book", exception4);
                return;
            }
            finally {
                packetbuffer4.release();
            }
            packetbuffer4.release();
            return;
        }
        if ("MC|TrSel".equals(packetIn.getChannelName())) {
            try {
                final int i = packetIn.getBufferData().readInt();
                final Container container = this.playerEntity.openContainer;
                if (container instanceof ContainerMerchant) {
                    ((ContainerMerchant)container).setCurrentRecipeIndex(i);
                }
            }
            catch (Exception exception5) {
                NetHandlerPlayServer.logger.error("Couldn't select trade", exception5);
            }
        }
        else if ("MC|AdvCdm".equals(packetIn.getChannelName())) {
            if (!this.serverController.isCommandBlockEnabled()) {
                this.playerEntity.addChatMessage(new ChatComponentTranslation("advMode.notEnabled", new Object[0]));
            }
            else if (this.playerEntity.canCommandSenderUseCommand(2, "") && this.playerEntity.capabilities.isCreativeMode) {
                final PacketBuffer packetbuffer5 = packetIn.getBufferData();
                try {
                    final int j = packetbuffer5.readByte();
                    CommandBlockLogic commandblocklogic = null;
                    if (j == 0) {
                        final TileEntity tileentity = this.playerEntity.worldObj.getTileEntity(new BlockPos(packetbuffer5.readInt(), packetbuffer5.readInt(), packetbuffer5.readInt()));
                        if (tileentity instanceof TileEntityCommandBlock) {
                            commandblocklogic = ((TileEntityCommandBlock)tileentity).getCommandBlockLogic();
                        }
                    }
                    else if (j == 1) {
                        final Entity entity = this.playerEntity.worldObj.getEntityByID(packetbuffer5.readInt());
                        if (entity instanceof EntityMinecartCommandBlock) {
                            commandblocklogic = ((EntityMinecartCommandBlock)entity).getCommandBlockLogic();
                        }
                    }
                    final String s1 = packetbuffer5.readStringFromBuffer(packetbuffer5.readableBytes());
                    final boolean flag = packetbuffer5.readBoolean();
                    if (commandblocklogic != null) {
                        commandblocklogic.setCommand(s1);
                        commandblocklogic.setTrackOutput(flag);
                        if (!flag) {
                            commandblocklogic.setLastOutput(null);
                        }
                        commandblocklogic.updateCommand();
                        this.playerEntity.addChatMessage(new ChatComponentTranslation("advMode.setCommand.success", new Object[] { s1 }));
                    }
                }
                catch (Exception exception6) {
                    NetHandlerPlayServer.logger.error("Couldn't set command block", exception6);
                    return;
                }
                finally {
                    packetbuffer5.release();
                }
                packetbuffer5.release();
            }
            else {
                this.playerEntity.addChatMessage(new ChatComponentTranslation("advMode.notAllowed", new Object[0]));
            }
        }
        else if ("MC|Beacon".equals(packetIn.getChannelName())) {
            if (this.playerEntity.openContainer instanceof ContainerBeacon) {
                try {
                    final PacketBuffer packetbuffer6 = packetIn.getBufferData();
                    final int k = packetbuffer6.readInt();
                    final int l = packetbuffer6.readInt();
                    final ContainerBeacon containerbeacon = (ContainerBeacon)this.playerEntity.openContainer;
                    final Slot slot = containerbeacon.getSlot(0);
                    if (slot.getHasStack()) {
                        slot.decrStackSize(1);
                        final IInventory iinventory = containerbeacon.func_180611_e();
                        iinventory.setField(1, k);
                        iinventory.setField(2, l);
                        iinventory.markDirty();
                    }
                }
                catch (Exception exception7) {
                    NetHandlerPlayServer.logger.error("Couldn't set beacon", exception7);
                }
            }
        }
        else if ("MC|ItemName".equals(packetIn.getChannelName()) && this.playerEntity.openContainer instanceof ContainerRepair) {
            final ContainerRepair containerrepair = (ContainerRepair)this.playerEntity.openContainer;
            if (packetIn.getBufferData() != null && packetIn.getBufferData().readableBytes() >= 1) {
                final String s2 = ChatAllowedCharacters.filterAllowedCharacters(packetIn.getBufferData().readStringFromBuffer(32767));
                if (s2.length() <= 30) {
                    containerrepair.updateItemName(s2);
                }
            }
            else {
                containerrepair.updateItemName("");
            }
        }
    }
}
