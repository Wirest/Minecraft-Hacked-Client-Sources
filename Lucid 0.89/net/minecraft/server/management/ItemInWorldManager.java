package net.minecraft.server.management;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;

public class ItemInWorldManager
{
    /** The world object that this object is connected to. */
    public World theWorld;

    /** The EntityPlayerMP object that this object is connected to. */
    public EntityPlayerMP thisPlayerMP;
    private WorldSettings.GameType gameType;

    /** True if the player is destroying a block */
    private boolean isDestroyingBlock;
    private int initialDamage;
    private BlockPos field_180240_f;
    private int curblockDamage;

    /**
     * Set to true when the "finished destroying block" packet is received but the block wasn't fully damaged yet. The
     * block will not be destroyed while this is false.
     */
    private boolean receivedFinishDiggingPacket;
    private BlockPos field_180241_i;
    private int initialBlockDamage;
    private int durabilityRemainingOnBlock;

    public ItemInWorldManager(World worldIn)
    {
        this.gameType = WorldSettings.GameType.NOT_SET;
        this.field_180240_f = BlockPos.ORIGIN;
        this.field_180241_i = BlockPos.ORIGIN;
        this.durabilityRemainingOnBlock = -1;
        this.theWorld = worldIn;
    }

    public void setGameType(WorldSettings.GameType type)
    {
        this.gameType = type;
        type.configurePlayerCapabilities(this.thisPlayerMP.capabilities);
        this.thisPlayerMP.sendPlayerAbilities();
        this.thisPlayerMP.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.UPDATE_GAME_MODE, new EntityPlayerMP[] {this.thisPlayerMP}));
    }

    public WorldSettings.GameType getGameType()
    {
        return this.gameType;
    }

    public boolean survivalOrAdventure()
    {
        return this.gameType.isSurvivalOrAdventure();
    }

    /**
     * Get if we are in creative game mode.
     */
    public boolean isCreative()
    {
        return this.gameType.isCreative();
    }

    /**
     * if the gameType is currently NOT_SET then change it to par1
     */
    public void initializeGameType(WorldSettings.GameType type)
    {
        if (this.gameType == WorldSettings.GameType.NOT_SET)
        {
            this.gameType = type;
        }

        this.setGameType(this.gameType);
    }

    public void updateBlockRemoving()
    {
        ++this.curblockDamage;
        float var3;
        int var4;

        if (this.receivedFinishDiggingPacket)
        {
            int var1 = this.curblockDamage - this.initialBlockDamage;
            Block var2 = this.theWorld.getBlockState(this.field_180241_i).getBlock();

            if (var2.getMaterial() == Material.air)
            {
                this.receivedFinishDiggingPacket = false;
            }
            else
            {
                var3 = var2.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.field_180241_i) * (var1 + 1);
                var4 = (int)(var3 * 10.0F);

                if (var4 != this.durabilityRemainingOnBlock)
                {
                    this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180241_i, var4);
                    this.durabilityRemainingOnBlock = var4;
                }

                if (var3 >= 1.0F)
                {
                    this.receivedFinishDiggingPacket = false;
                    this.tryHarvestBlock(this.field_180241_i);
                }
            }
        }
        else if (this.isDestroyingBlock)
        {
            Block var5 = this.theWorld.getBlockState(this.field_180240_f).getBlock();

            if (var5.getMaterial() == Material.air)
            {
                this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, -1);
                this.durabilityRemainingOnBlock = -1;
                this.isDestroyingBlock = false;
            }
            else
            {
                int var6 = this.curblockDamage - this.initialDamage;
                var3 = var5.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.field_180241_i) * (var6 + 1);
                var4 = (int)(var3 * 10.0F);

                if (var4 != this.durabilityRemainingOnBlock)
                {
                    this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, var4);
                    this.durabilityRemainingOnBlock = var4;
                }
            }
        }
    }

    /**
     * If not creative, it calls sendBlockBreakProgress until the block is broken first. tryHarvestBlock can also be the
     * result of this call.
     *  
     * @param pos The block's coordinates
     * @param side The specific side that is being hit
     */
    public void onBlockClicked(BlockPos pos, EnumFacing side)
    {
        if (this.isCreative())
        {
            if (!this.theWorld.extinguishFire((EntityPlayer)null, pos, side))
            {
                this.tryHarvestBlock(pos);
            }
        }
        else
        {
            Block var3 = this.theWorld.getBlockState(pos).getBlock();

            if (this.gameType.isAdventure())
            {
                if (this.gameType == WorldSettings.GameType.SPECTATOR)
                {
                    return;
                }

                if (!this.thisPlayerMP.isAllowEdit())
                {
                    ItemStack var4 = this.thisPlayerMP.getCurrentEquippedItem();

                    if (var4 == null)
                    {
                        return;
                    }

                    if (!var4.canDestroy(var3))
                    {
                        return;
                    }
                }
            }

            this.theWorld.extinguishFire((EntityPlayer)null, pos, side);
            this.initialDamage = this.curblockDamage;
            float var6 = 1.0F;

            if (var3.getMaterial() != Material.air)
            {
                var3.onBlockClicked(this.theWorld, pos, this.thisPlayerMP);
                var6 = var3.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, pos);
            }

            if (var3.getMaterial() != Material.air && var6 >= 1.0F)
            {
                this.tryHarvestBlock(pos);
            }
            else
            {
                this.isDestroyingBlock = true;
                this.field_180240_f = pos;
                int var5 = (int)(var6 * 10.0F);
                this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), pos, var5);
                this.durabilityRemainingOnBlock = var5;
            }
        }
    }

    public void blockRemoving(BlockPos pos)
    {
        if (pos.equals(this.field_180240_f))
        {
            int var2 = this.curblockDamage - this.initialDamage;
            Block var3 = this.theWorld.getBlockState(pos).getBlock();

            if (var3.getMaterial() != Material.air)
            {
                float var4 = var3.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, pos) * (var2 + 1);

                if (var4 >= 0.7F)
                {
                    this.isDestroyingBlock = false;
                    this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), pos, -1);
                    this.tryHarvestBlock(pos);
                }
                else if (!this.receivedFinishDiggingPacket)
                {
                    this.isDestroyingBlock = false;
                    this.receivedFinishDiggingPacket = true;
                    this.field_180241_i = pos;
                    this.initialBlockDamage = this.initialDamage;
                }
            }
        }
    }

    /**
     * Stops the block breaking process
     */
    public void cancelDestroyingBlock()
    {
        this.isDestroyingBlock = false;
        this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, -1);
    }

    /**
     * Removes a block and triggers the appropriate events
     *  
     * @param pos The coordinates for the block to remove
     */
    private boolean removeBlock(BlockPos pos)
    {
        IBlockState var2 = this.theWorld.getBlockState(pos);
        var2.getBlock().onBlockHarvested(this.theWorld, pos, var2, this.thisPlayerMP);
        boolean var3 = this.theWorld.setBlockToAir(pos);

        if (var3)
        {
            var2.getBlock().onBlockDestroyedByPlayer(this.theWorld, pos, var2);
        }

        return var3;
    }

    /**
     * Attempts to harvest a block
     *  
     * @param pos The coordinates of the block
     */
    public boolean tryHarvestBlock(BlockPos pos)
    {
        if (this.gameType.isCreative() && this.thisPlayerMP.getHeldItem() != null && this.thisPlayerMP.getHeldItem().getItem() instanceof ItemSword)
        {
            return false;
        }
        else
        {
            IBlockState var2 = this.theWorld.getBlockState(pos);
            TileEntity var3 = this.theWorld.getTileEntity(pos);

            if (this.gameType.isAdventure())
            {
                if (this.gameType == WorldSettings.GameType.SPECTATOR)
                {
                    return false;
                }

                if (!this.thisPlayerMP.isAllowEdit())
                {
                    ItemStack var4 = this.thisPlayerMP.getCurrentEquippedItem();

                    if (var4 == null)
                    {
                        return false;
                    }

                    if (!var4.canDestroy(var2.getBlock()))
                    {
                        return false;
                    }
                }
            }

            this.theWorld.playAuxSFXAtEntity(this.thisPlayerMP, 2001, pos, Block.getStateId(var2));
            boolean var7 = this.removeBlock(pos);

            if (this.isCreative())
            {
                this.thisPlayerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(this.theWorld, pos));
            }
            else
            {
                ItemStack var5 = this.thisPlayerMP.getCurrentEquippedItem();
                boolean var6 = this.thisPlayerMP.canHarvestBlock(var2.getBlock());

                if (var5 != null)
                {
                    var5.onBlockDestroyed(this.theWorld, var2.getBlock(), pos, this.thisPlayerMP);

                    if (var5.stackSize == 0)
                    {
                        this.thisPlayerMP.destroyCurrentEquippedItem();
                    }
                }

                if (var7 && var6)
                {
                    var2.getBlock().harvestBlock(this.theWorld, this.thisPlayerMP, pos, var2, var3);
                }
            }

            return var7;
        }
    }

    /**
     * Attempts to right-click use an item by the given EntityPlayer in the given World
     */
    public boolean tryUseItem(EntityPlayer player, World worldIn, ItemStack stack)
    {
        if (this.gameType == WorldSettings.GameType.SPECTATOR)
        {
            return false;
        }
        else
        {
            int var4 = stack.stackSize;
            int var5 = stack.getMetadata();
            ItemStack var6 = stack.useItemRightClick(worldIn, player);

            if (var6 == stack && (var6 == null || var6.stackSize == var4 && var6.getMaxItemUseDuration() <= 0 && var6.getMetadata() == var5))
            {
                return false;
            }
            else
            {
                player.inventory.mainInventory[player.inventory.currentItem] = var6;

                if (this.isCreative())
                {
                    var6.stackSize = var4;

                    if (var6.isItemStackDamageable())
                    {
                        var6.setItemDamage(var5);
                    }
                }

                if (var6.stackSize == 0)
                {
                    player.inventory.mainInventory[player.inventory.currentItem] = null;
                }

                if (!player.isUsingItem())
                {
                    ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                }

                return true;
            }
        }
    }

    /**
     * Activate the clicked on block, otherwise use the held item.
     *  
     * @param pos The block's coordinates
     * @param side The side of the block that was clicked on
     */
    public boolean activateBlockOrUseItem(EntityPlayer player, World worldIn, ItemStack stack, BlockPos pos, EnumFacing side, float p_180236_6_, float p_180236_7_, float p_180236_8_)
    {
        if (this.gameType == WorldSettings.GameType.SPECTATOR)
        {
            TileEntity var13 = worldIn.getTileEntity(pos);

            if (var13 instanceof ILockableContainer)
            {
                Block var14 = worldIn.getBlockState(pos).getBlock();
                ILockableContainer var15 = (ILockableContainer)var13;

                if (var15 instanceof TileEntityChest && var14 instanceof BlockChest)
                {
                    var15 = ((BlockChest)var14).getLockableContainer(worldIn, pos);
                }

                if (var15 != null)
                {
                    player.displayGUIChest(var15);
                    return true;
                }
            }
            else if (var13 instanceof IInventory)
            {
                player.displayGUIChest((IInventory)var13);
                return true;
            }

            return false;
        }
        else
        {
            if (!player.isSneaking() || player.getHeldItem() == null)
            {
                IBlockState var9 = worldIn.getBlockState(pos);

                if (var9.getBlock().onBlockActivated(worldIn, pos, var9, player, side, p_180236_6_, p_180236_7_, p_180236_8_))
                {
                    return true;
                }
            }

            if (stack == null)
            {
                return false;
            }
            else if (this.isCreative())
            {
                int var12 = stack.getMetadata();
                int var10 = stack.stackSize;
                boolean var11 = stack.onItemUse(player, worldIn, pos, side, p_180236_6_, p_180236_7_, p_180236_8_);
                stack.setItemDamage(var12);
                stack.stackSize = var10;
                return var11;
            }
            else
            {
                return stack.onItemUse(player, worldIn, pos, side, p_180236_6_, p_180236_7_, p_180236_8_);
            }
        }
    }

    /**
     * Sets the world instance.
     */
    public void setWorld(WorldServer serverWorld)
    {
        this.theWorld = serverWorld;
    }
}
