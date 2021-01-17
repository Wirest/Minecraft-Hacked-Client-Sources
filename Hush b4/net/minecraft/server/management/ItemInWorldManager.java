// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import net.minecraft.world.WorldServer;
import net.minecraft.inventory.IInventory;
import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.ILockableContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.item.ItemSword;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.material.Material;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldSettings;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class ItemInWorldManager
{
    public World theWorld;
    public EntityPlayerMP thisPlayerMP;
    private WorldSettings.GameType gameType;
    private boolean isDestroyingBlock;
    private int initialDamage;
    private BlockPos field_180240_f;
    private int curblockDamage;
    private boolean receivedFinishDiggingPacket;
    private BlockPos field_180241_i;
    private int initialBlockDamage;
    private int durabilityRemainingOnBlock;
    
    public ItemInWorldManager(final World worldIn) {
        this.gameType = WorldSettings.GameType.NOT_SET;
        this.field_180240_f = BlockPos.ORIGIN;
        this.field_180241_i = BlockPos.ORIGIN;
        this.durabilityRemainingOnBlock = -1;
        this.theWorld = worldIn;
    }
    
    public void setGameType(final WorldSettings.GameType type) {
        (this.gameType = type).configurePlayerCapabilities(this.thisPlayerMP.capabilities);
        this.thisPlayerMP.sendPlayerAbilities();
        this.thisPlayerMP.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.UPDATE_GAME_MODE, new EntityPlayerMP[] { this.thisPlayerMP }));
    }
    
    public WorldSettings.GameType getGameType() {
        return this.gameType;
    }
    
    public boolean survivalOrAdventure() {
        return this.gameType.isSurvivalOrAdventure();
    }
    
    public boolean isCreative() {
        return this.gameType.isCreative();
    }
    
    public void initializeGameType(final WorldSettings.GameType type) {
        if (this.gameType == WorldSettings.GameType.NOT_SET) {
            this.gameType = type;
        }
        this.setGameType(this.gameType);
    }
    
    public void updateBlockRemoving() {
        ++this.curblockDamage;
        if (this.receivedFinishDiggingPacket) {
            final int i = this.curblockDamage - this.initialBlockDamage;
            final Block block = this.theWorld.getBlockState(this.field_180241_i).getBlock();
            if (block.getMaterial() == Material.air) {
                this.receivedFinishDiggingPacket = false;
            }
            else {
                final float f = block.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.field_180241_i) * (i + 1);
                final int j = (int)(f * 10.0f);
                if (j != this.durabilityRemainingOnBlock) {
                    this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180241_i, j);
                    this.durabilityRemainingOnBlock = j;
                }
                if (f >= 1.0f) {
                    this.receivedFinishDiggingPacket = false;
                    this.tryHarvestBlock(this.field_180241_i);
                }
            }
        }
        else if (this.isDestroyingBlock) {
            final Block block2 = this.theWorld.getBlockState(this.field_180240_f).getBlock();
            if (block2.getMaterial() == Material.air) {
                this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, -1);
                this.durabilityRemainingOnBlock = -1;
                this.isDestroyingBlock = false;
            }
            else {
                final int k = this.curblockDamage - this.initialDamage;
                final float f2 = block2.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.field_180241_i) * (k + 1);
                final int l = (int)(f2 * 10.0f);
                if (l != this.durabilityRemainingOnBlock) {
                    this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, l);
                    this.durabilityRemainingOnBlock = l;
                }
            }
        }
    }
    
    public void onBlockClicked(final BlockPos pos, final EnumFacing side) {
        if (this.isCreative()) {
            if (!this.theWorld.extinguishFire(null, pos, side)) {
                this.tryHarvestBlock(pos);
            }
        }
        else {
            final Block block = this.theWorld.getBlockState(pos).getBlock();
            if (this.gameType.isAdventure()) {
                if (this.gameType == WorldSettings.GameType.SPECTATOR) {
                    return;
                }
                if (!this.thisPlayerMP.isAllowEdit()) {
                    final ItemStack itemstack = this.thisPlayerMP.getCurrentEquippedItem();
                    if (itemstack == null) {
                        return;
                    }
                    if (!itemstack.canDestroy(block)) {
                        return;
                    }
                }
            }
            this.theWorld.extinguishFire(null, pos, side);
            this.initialDamage = this.curblockDamage;
            float f = 1.0f;
            if (block.getMaterial() != Material.air) {
                block.onBlockClicked(this.theWorld, pos, this.thisPlayerMP);
                f = block.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, pos);
            }
            if (block.getMaterial() != Material.air && f >= 1.0f) {
                this.tryHarvestBlock(pos);
            }
            else {
                this.isDestroyingBlock = true;
                this.field_180240_f = pos;
                final int i = (int)(f * 10.0f);
                this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), pos, i);
                this.durabilityRemainingOnBlock = i;
            }
        }
    }
    
    public void blockRemoving(final BlockPos pos) {
        if (pos.equals(this.field_180240_f)) {
            final int i = this.curblockDamage - this.initialDamage;
            final Block block = this.theWorld.getBlockState(pos).getBlock();
            if (block.getMaterial() != Material.air) {
                final float f = block.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, pos) * (i + 1);
                if (f >= 0.7f) {
                    this.isDestroyingBlock = false;
                    this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), pos, -1);
                    this.tryHarvestBlock(pos);
                }
                else if (!this.receivedFinishDiggingPacket) {
                    this.isDestroyingBlock = false;
                    this.receivedFinishDiggingPacket = true;
                    this.field_180241_i = pos;
                    this.initialBlockDamage = this.initialDamage;
                }
            }
        }
    }
    
    public void cancelDestroyingBlock() {
        this.isDestroyingBlock = false;
        this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, -1);
    }
    
    private boolean removeBlock(final BlockPos pos) {
        final IBlockState iblockstate = this.theWorld.getBlockState(pos);
        iblockstate.getBlock().onBlockHarvested(this.theWorld, pos, iblockstate, this.thisPlayerMP);
        final boolean flag = this.theWorld.setBlockToAir(pos);
        if (flag) {
            iblockstate.getBlock().onBlockDestroyedByPlayer(this.theWorld, pos, iblockstate);
        }
        return flag;
    }
    
    public boolean tryHarvestBlock(final BlockPos pos) {
        if (this.gameType.isCreative() && this.thisPlayerMP.getHeldItem() != null && this.thisPlayerMP.getHeldItem().getItem() instanceof ItemSword) {
            return false;
        }
        final IBlockState iblockstate = this.theWorld.getBlockState(pos);
        final TileEntity tileentity = this.theWorld.getTileEntity(pos);
        if (this.gameType.isAdventure()) {
            if (this.gameType == WorldSettings.GameType.SPECTATOR) {
                return false;
            }
            if (!this.thisPlayerMP.isAllowEdit()) {
                final ItemStack itemstack = this.thisPlayerMP.getCurrentEquippedItem();
                if (itemstack == null) {
                    return false;
                }
                if (!itemstack.canDestroy(iblockstate.getBlock())) {
                    return false;
                }
            }
        }
        this.theWorld.playAuxSFXAtEntity(this.thisPlayerMP, 2001, pos, Block.getStateId(iblockstate));
        final boolean flag1 = this.removeBlock(pos);
        if (this.isCreative()) {
            this.thisPlayerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(this.theWorld, pos));
        }
        else {
            final ItemStack itemstack2 = this.thisPlayerMP.getCurrentEquippedItem();
            final boolean flag2 = this.thisPlayerMP.canHarvestBlock(iblockstate.getBlock());
            if (itemstack2 != null) {
                itemstack2.onBlockDestroyed(this.theWorld, iblockstate.getBlock(), pos, this.thisPlayerMP);
                if (itemstack2.stackSize == 0) {
                    this.thisPlayerMP.destroyCurrentEquippedItem();
                }
            }
            if (flag1 && flag2) {
                iblockstate.getBlock().harvestBlock(this.theWorld, this.thisPlayerMP, pos, iblockstate, tileentity);
            }
        }
        return flag1;
    }
    
    public boolean tryUseItem(final EntityPlayer player, final World worldIn, final ItemStack stack) {
        if (this.gameType == WorldSettings.GameType.SPECTATOR) {
            return false;
        }
        final int i = stack.stackSize;
        final int j = stack.getMetadata();
        final ItemStack itemstack = stack.useItemRightClick(worldIn, player);
        if (itemstack != stack || (itemstack != null && (itemstack.stackSize != i || itemstack.getMaxItemUseDuration() > 0 || itemstack.getMetadata() != j))) {
            player.inventory.mainInventory[player.inventory.currentItem] = itemstack;
            if (this.isCreative()) {
                itemstack.stackSize = i;
                if (itemstack.isItemStackDamageable()) {
                    itemstack.setItemDamage(j);
                }
            }
            if (itemstack.stackSize == 0) {
                player.inventory.mainInventory[player.inventory.currentItem] = null;
            }
            if (!player.isUsingItem()) {
                ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
            }
            return true;
        }
        return false;
    }
    
    public boolean activateBlockOrUseItem(final EntityPlayer player, final World worldIn, final ItemStack stack, final BlockPos pos, final EnumFacing side, final float offsetX, final float offsetY, final float offsetZ) {
        if (this.gameType == WorldSettings.GameType.SPECTATOR) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof ILockableContainer) {
                final Block block = worldIn.getBlockState(pos).getBlock();
                ILockableContainer ilockablecontainer = (ILockableContainer)tileentity;
                if (ilockablecontainer instanceof TileEntityChest && block instanceof BlockChest) {
                    ilockablecontainer = ((BlockChest)block).getLockableContainer(worldIn, pos);
                }
                if (ilockablecontainer != null) {
                    player.displayGUIChest(ilockablecontainer);
                    return true;
                }
            }
            else if (tileentity instanceof IInventory) {
                player.displayGUIChest((IInventory)tileentity);
                return true;
            }
            return false;
        }
        if (!player.isSneaking() || player.getHeldItem() == null) {
            final IBlockState iblockstate = worldIn.getBlockState(pos);
            if (iblockstate.getBlock().onBlockActivated(worldIn, pos, iblockstate, player, side, offsetX, offsetY, offsetZ)) {
                return true;
            }
        }
        if (stack == null) {
            return false;
        }
        if (this.isCreative()) {
            final int j = stack.getMetadata();
            final int i = stack.stackSize;
            final boolean flag = stack.onItemUse(player, worldIn, pos, side, offsetX, offsetY, offsetZ);
            stack.setItemDamage(j);
            stack.stackSize = i;
            return flag;
        }
        return stack.onItemUse(player, worldIn, pos, side, offsetX, offsetY, offsetZ);
    }
    
    public void setWorld(final WorldServer serverWorld) {
        this.theWorld = serverWorld;
    }
}
