// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.multiplayer;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.entity.Entity;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.Vec3;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemSword;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.Minecraft;

public class PlayerControllerMP
{
    private final Minecraft mc;
    private final NetHandlerPlayClient netClientHandler;
    private BlockPos currentBlock;
    private ItemStack currentItemHittingBlock;
    private float curBlockDamageMP;
    private float stepSoundTickCounter;
    private int blockHitDelay;
    private boolean isHittingBlock;
    private WorldSettings.GameType currentGameType;
    private int currentPlayerItem;
    
    public PlayerControllerMP(final Minecraft mcIn, final NetHandlerPlayClient p_i45062_2_) {
        this.currentBlock = new BlockPos(-1, -1, -1);
        this.currentGameType = WorldSettings.GameType.SURVIVAL;
        this.mc = mcIn;
        this.netClientHandler = p_i45062_2_;
    }
    
    public static void clickBlockCreative(final Minecraft mcIn, final PlayerControllerMP p_178891_1_, final BlockPos p_178891_2_, final EnumFacing p_178891_3_) {
        if (!mcIn.theWorld.extinguishFire(Minecraft.thePlayer, p_178891_2_, p_178891_3_)) {
            p_178891_1_.onPlayerDestroyBlock(p_178891_2_, p_178891_3_);
        }
    }
    
    public void setPlayerCapabilities(final EntityPlayer p_78748_1_) {
        this.currentGameType.configurePlayerCapabilities(p_78748_1_.capabilities);
    }
    
    public boolean isSpectator() {
        return this.currentGameType == WorldSettings.GameType.SPECTATOR;
    }
    
    public void setGameType(final WorldSettings.GameType p_78746_1_) {
        (this.currentGameType = p_78746_1_).configurePlayerCapabilities(Minecraft.thePlayer.capabilities);
    }
    
    public void flipPlayer(final EntityPlayer playerIn) {
        playerIn.rotationYaw = -180.0f;
    }
    
    public boolean shouldDrawHUD() {
        return this.currentGameType.isSurvivalOrAdventure();
    }
    
    public boolean onPlayerDestroyBlock(final BlockPos pos, final EnumFacing side) {
        if (this.currentGameType.isAdventure()) {
            if (this.currentGameType == WorldSettings.GameType.SPECTATOR) {
                return false;
            }
            if (!Minecraft.thePlayer.isAllowEdit()) {
                final Block block = this.mc.theWorld.getBlockState(pos).getBlock();
                final ItemStack itemstack = Minecraft.thePlayer.getCurrentEquippedItem();
                if (itemstack == null) {
                    return false;
                }
                if (!itemstack.canDestroy(block)) {
                    return false;
                }
            }
        }
        if (this.currentGameType.isCreative() && Minecraft.thePlayer.getHeldItem() != null && Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            return false;
        }
        final World world = this.mc.theWorld;
        final IBlockState iblockstate = world.getBlockState(pos);
        final Block block2 = iblockstate.getBlock();
        if (block2.getMaterial() == Material.air) {
            return false;
        }
        world.playAuxSFX(2001, pos, Block.getStateId(iblockstate));
        final boolean flag = world.setBlockToAir(pos);
        if (flag) {
            block2.onBlockDestroyedByPlayer(world, pos, iblockstate);
        }
        this.currentBlock = new BlockPos(this.currentBlock.getX(), -1, this.currentBlock.getZ());
        if (!this.currentGameType.isCreative()) {
            final ItemStack itemstack2 = Minecraft.thePlayer.getCurrentEquippedItem();
            if (itemstack2 != null) {
                itemstack2.onBlockDestroyed(world, block2, pos, Minecraft.thePlayer);
                if (itemstack2.stackSize == 0) {
                    Minecraft.thePlayer.destroyCurrentEquippedItem();
                }
            }
        }
        return flag;
    }
    
    public boolean clickBlock(final BlockPos loc, final EnumFacing face) {
        if (this.currentGameType.isAdventure()) {
            if (this.currentGameType == WorldSettings.GameType.SPECTATOR) {
                return false;
            }
            if (!Minecraft.thePlayer.isAllowEdit()) {
                final Block block = this.mc.theWorld.getBlockState(loc).getBlock();
                final ItemStack itemstack = Minecraft.thePlayer.getCurrentEquippedItem();
                if (itemstack == null) {
                    return false;
                }
                if (!itemstack.canDestroy(block)) {
                    return false;
                }
            }
        }
        if (!this.mc.theWorld.getWorldBorder().contains(loc)) {
            return false;
        }
        if (this.currentGameType.isCreative()) {
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, loc, face));
            clickBlockCreative(this.mc, this, loc, face);
            this.blockHitDelay = 5;
        }
        else if (!this.isHittingBlock || !this.isHittingPosition(loc)) {
            if (this.isHittingBlock) {
                this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, face));
            }
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, loc, face));
            final Block block2 = this.mc.theWorld.getBlockState(loc).getBlock();
            final boolean flag = block2.getMaterial() != Material.air;
            if (flag && this.curBlockDamageMP == 0.0f) {
                block2.onBlockClicked(this.mc.theWorld, loc, Minecraft.thePlayer);
            }
            if (flag && block2.getPlayerRelativeBlockHardness(Minecraft.thePlayer, Minecraft.thePlayer.worldObj, loc) >= 1.0f) {
                this.onPlayerDestroyBlock(loc, face);
            }
            else {
                this.isHittingBlock = true;
                this.currentBlock = loc;
                this.currentItemHittingBlock = Minecraft.thePlayer.getHeldItem();
                this.curBlockDamageMP = 0.0f;
                this.stepSoundTickCounter = 0.0f;
                this.mc.theWorld.sendBlockBreakProgress(Minecraft.thePlayer.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0f) - 1);
            }
        }
        return true;
    }
    
    public void resetBlockRemoving() {
        if (this.isHittingBlock) {
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, EnumFacing.DOWN));
            this.isHittingBlock = false;
            this.curBlockDamageMP = 0.0f;
            this.mc.theWorld.sendBlockBreakProgress(Minecraft.thePlayer.getEntityId(), this.currentBlock, -1);
        }
    }
    
    public boolean onPlayerDamageBlock(final BlockPos posBlock, final EnumFacing directionFacing) {
        this.syncCurrentPlayItem();
        if (this.blockHitDelay > 0) {
            --this.blockHitDelay;
            return true;
        }
        if (this.currentGameType.isCreative() && this.mc.theWorld.getWorldBorder().contains(posBlock)) {
            this.blockHitDelay = 5;
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, posBlock, directionFacing));
            clickBlockCreative(this.mc, this, posBlock, directionFacing);
            return true;
        }
        if (!this.isHittingPosition(posBlock)) {
            return this.clickBlock(posBlock, directionFacing);
        }
        final Block block = this.mc.theWorld.getBlockState(posBlock).getBlock();
        if (block.getMaterial() == Material.air) {
            return this.isHittingBlock = false;
        }
        this.curBlockDamageMP += block.getPlayerRelativeBlockHardness(Minecraft.thePlayer, Minecraft.thePlayer.worldObj, posBlock);
        if (this.stepSoundTickCounter % 4.0f == 0.0f) {
            this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(block.stepSound.getStepSound()), (block.stepSound.getVolume() + 1.0f) / 8.0f, block.stepSound.getFrequency() * 0.5f, posBlock.getX() + 0.5f, posBlock.getY() + 0.5f, posBlock.getZ() + 0.5f));
        }
        ++this.stepSoundTickCounter;
        if (this.curBlockDamageMP >= 1.0f) {
            this.isHittingBlock = false;
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, posBlock, directionFacing));
            this.onPlayerDestroyBlock(posBlock, directionFacing);
            this.curBlockDamageMP = 0.0f;
            this.stepSoundTickCounter = 0.0f;
            this.blockHitDelay = 5;
        }
        this.mc.theWorld.sendBlockBreakProgress(Minecraft.thePlayer.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0f) - 1);
        return true;
    }
    
    public float getBlockReachDistance() {
        return this.currentGameType.isCreative() ? 5.0f : 4.5f;
    }
    
    public void updateController() {
        this.syncCurrentPlayItem();
        if (this.netClientHandler.getNetworkManager().isChannelOpen()) {
            this.netClientHandler.getNetworkManager().processReceivedPackets();
        }
        else {
            this.netClientHandler.getNetworkManager().checkDisconnected();
        }
    }
    
    private boolean isHittingPosition(final BlockPos pos) {
        final ItemStack itemstack = Minecraft.thePlayer.getHeldItem();
        boolean flag = this.currentItemHittingBlock == null && itemstack == null;
        if (this.currentItemHittingBlock != null && itemstack != null) {
            flag = (itemstack.getItem() == this.currentItemHittingBlock.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.currentItemHittingBlock) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.currentItemHittingBlock.getMetadata()));
        }
        return pos.equals(this.currentBlock) && flag;
    }
    
    public void syncCurrentPlayItem() {
        final int i = Minecraft.thePlayer.inventory.currentItem;
        if (i != this.currentPlayerItem) {
            this.currentPlayerItem = i;
            this.netClientHandler.addToSendQueue(new C09PacketHeldItemChange(this.currentPlayerItem));
        }
    }
    
    public boolean onPlayerRightClick(final EntityPlayerSP player, final WorldClient worldIn, final ItemStack heldStack, final BlockPos hitPos, final EnumFacing side, final Vec3 hitVec) {
        this.syncCurrentPlayItem();
        final float f = (float)(hitVec.xCoord - hitPos.getX());
        final float f2 = (float)(hitVec.yCoord - hitPos.getY());
        final float f3 = (float)(hitVec.zCoord - hitPos.getZ());
        boolean flag = false;
        if (!this.mc.theWorld.getWorldBorder().contains(hitPos)) {
            return false;
        }
        if (this.currentGameType != WorldSettings.GameType.SPECTATOR) {
            final IBlockState iblockstate = worldIn.getBlockState(hitPos);
            if ((!player.isSneaking() || player.getHeldItem() == null) && iblockstate.getBlock().onBlockActivated(worldIn, hitPos, iblockstate, player, side, f, f2, f3)) {
                flag = true;
            }
            if (!flag && heldStack != null && heldStack.getItem() instanceof ItemBlock) {
                final ItemBlock itemblock = (ItemBlock)heldStack.getItem();
                if (!itemblock.canPlaceBlockOnSide(worldIn, hitPos, side, player, heldStack)) {
                    return false;
                }
            }
        }
        this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(hitPos, side.getIndex(), player.inventory.getCurrentItem(), f, f2, f3));
        if (flag || this.currentGameType == WorldSettings.GameType.SPECTATOR) {
            return true;
        }
        if (heldStack == null) {
            return false;
        }
        if (this.currentGameType.isCreative()) {
            final int i = heldStack.getMetadata();
            final int j = heldStack.stackSize;
            final boolean flag2 = heldStack.onItemUse(player, worldIn, hitPos, side, f, f2, f3);
            heldStack.setItemDamage(i);
            heldStack.stackSize = j;
            return flag2;
        }
        return heldStack.onItemUse(player, worldIn, hitPos, side, f, f2, f3);
    }
    
    public boolean sendUseItem(final EntityPlayer playerIn, final World worldIn, final ItemStack itemStackIn) {
        if (this.currentGameType == WorldSettings.GameType.SPECTATOR) {
            return false;
        }
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(playerIn.inventory.getCurrentItem()));
        final int i = itemStackIn.stackSize;
        final ItemStack itemstack = itemStackIn.useItemRightClick(worldIn, playerIn);
        if (itemstack != itemStackIn || (itemstack != null && itemstack.stackSize != i)) {
            playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = itemstack;
            if (itemstack.stackSize == 0) {
                playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = null;
            }
            return true;
        }
        return false;
    }
    
    public EntityPlayerSP func_178892_a(final World worldIn, final StatFileWriter p_178892_2_) {
        return new EntityPlayerSP(this.mc, worldIn, this.netClientHandler, p_178892_2_);
    }
    
    public void attackEntity(final EntityPlayer playerIn, final Entity targetEntity) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.ATTACK));
        if (this.currentGameType != WorldSettings.GameType.SPECTATOR) {
            playerIn.attackTargetEntityWithCurrentItem(targetEntity);
        }
    }
    
    public boolean interactWithEntitySendPacket(final EntityPlayer playerIn, final Entity targetEntity) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.INTERACT));
        return this.currentGameType != WorldSettings.GameType.SPECTATOR && playerIn.interactWith(targetEntity);
    }
    
    public boolean func_178894_a(final EntityPlayer p_178894_1_, final Entity p_178894_2_, final MovingObjectPosition p_178894_3_) {
        this.syncCurrentPlayItem();
        final Vec3 vec3 = new Vec3(p_178894_3_.hitVec.xCoord - p_178894_2_.posX, p_178894_3_.hitVec.yCoord - p_178894_2_.posY, p_178894_3_.hitVec.zCoord - p_178894_2_.posZ);
        this.netClientHandler.addToSendQueue(new C02PacketUseEntity(p_178894_2_, vec3));
        return this.currentGameType != WorldSettings.GameType.SPECTATOR && p_178894_2_.interactAt(p_178894_1_, vec3);
    }
    
    public ItemStack windowClick(final int windowId, final int slotId, final int mouseButtonClicked, final int mode, final EntityPlayer playerIn) {
        final short short1 = playerIn.openContainer.getNextTransactionID(playerIn.inventory);
        final ItemStack itemstack = playerIn.openContainer.slotClick(slotId, mouseButtonClicked, mode, playerIn);
        this.netClientHandler.addToSendQueue(new C0EPacketClickWindow(windowId, slotId, mouseButtonClicked, mode, itemstack, short1));
        return itemstack;
    }
    
    public void sendEnchantPacket(final int p_78756_1_, final int p_78756_2_) {
        this.netClientHandler.addToSendQueue(new C11PacketEnchantItem(p_78756_1_, p_78756_2_));
    }
    
    public void sendSlotPacket(final ItemStack itemStackIn, final int slotId) {
        if (this.currentGameType.isCreative()) {
            this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(slotId, itemStackIn));
        }
    }
    
    public void sendPacketDropItem(final ItemStack itemStackIn) {
        if (this.currentGameType.isCreative() && itemStackIn != null) {
            this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(-1, itemStackIn));
        }
    }
    
    public void onStoppedUsingItem(final EntityPlayer playerIn) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        playerIn.stopUsingItem();
    }
    
    public boolean gameIsSurvivalOrAdventure() {
        return this.currentGameType.isSurvivalOrAdventure();
    }
    
    public boolean isNotCreative() {
        return !this.currentGameType.isCreative();
    }
    
    public boolean isInCreativeMode() {
        return this.currentGameType.isCreative();
    }
    
    public boolean extendedReach() {
        return this.currentGameType.isCreative();
    }
    
    public boolean isRidingHorse() {
        return Minecraft.thePlayer.isRiding() && Minecraft.thePlayer.ridingEntity instanceof EntityHorse;
    }
    
    public boolean isSpectatorMode() {
        return this.currentGameType == WorldSettings.GameType.SPECTATOR;
    }
    
    public WorldSettings.GameType getCurrentGameType() {
        return this.currentGameType;
    }
    
    public boolean func_181040_m() {
        return this.isHittingBlock;
    }
}
