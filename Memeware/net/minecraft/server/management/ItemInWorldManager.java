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

public class ItemInWorldManager {
    /**
     * The world object that this object is connected to.
     */
    public World theWorld;

    /**
     * The EntityPlayerMP object that this object is connected to.
     */
    public EntityPlayerMP thisPlayerMP;
    private WorldSettings.GameType gameType;

    /**
     * True if the player is destroying a block
     */
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
    private static final String __OBFID = "CL_00001442";

    public ItemInWorldManager(World worldIn) {
        this.gameType = WorldSettings.GameType.NOT_SET;
        this.field_180240_f = BlockPos.ORIGIN;
        this.field_180241_i = BlockPos.ORIGIN;
        this.durabilityRemainingOnBlock = -1;
        this.theWorld = worldIn;
    }

    public void setGameType(WorldSettings.GameType p_73076_1_) {
        this.gameType = p_73076_1_;
        p_73076_1_.configurePlayerCapabilities(this.thisPlayerMP.capabilities);
        this.thisPlayerMP.sendPlayerAbilities();
        this.thisPlayerMP.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.UPDATE_GAME_MODE, new EntityPlayerMP[]{this.thisPlayerMP}));
    }

    public WorldSettings.GameType getGameType() {
        return this.gameType;
    }

    public boolean func_180239_c() {
        return this.gameType.isSurvivalOrAdventure();
    }

    /**
     * Get if we are in creative game mode.
     */
    public boolean isCreative() {
        return this.gameType.isCreative();
    }

    /**
     * if the gameType is currently NOT_SET then change it to par1
     */
    public void initializeGameType(WorldSettings.GameType p_73077_1_) {
        if (this.gameType == WorldSettings.GameType.NOT_SET) {
            this.gameType = p_73077_1_;
        }

        this.setGameType(this.gameType);
    }

    public void updateBlockRemoving() {
        ++this.curblockDamage;
        float var3;
        int var4;

        if (this.receivedFinishDiggingPacket) {
            int var1 = this.curblockDamage - this.initialBlockDamage;
            Block var2 = this.theWorld.getBlockState(this.field_180241_i).getBlock();

            if (var2.getMaterial() == Material.air) {
                this.receivedFinishDiggingPacket = false;
            } else {
                var3 = var2.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.field_180241_i) * (float) (var1 + 1);
                var4 = (int) (var3 * 10.0F);

                if (var4 != this.durabilityRemainingOnBlock) {
                    this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180241_i, var4);
                    this.durabilityRemainingOnBlock = var4;
                }

                if (var3 >= 1.0F) {
                    this.receivedFinishDiggingPacket = false;
                    this.func_180237_b(this.field_180241_i);
                }
            }
        } else if (this.isDestroyingBlock) {
            Block var5 = this.theWorld.getBlockState(this.field_180240_f).getBlock();

            if (var5.getMaterial() == Material.air) {
                this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, -1);
                this.durabilityRemainingOnBlock = -1;
                this.isDestroyingBlock = false;
            } else {
                int var6 = this.curblockDamage - this.initialDamage;
                var3 = var5.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.field_180241_i) * (float) (var6 + 1);
                var4 = (int) (var3 * 10.0F);

                if (var4 != this.durabilityRemainingOnBlock) {
                    this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, var4);
                    this.durabilityRemainingOnBlock = var4;
                }
            }
        }
    }

    public void func_180784_a(BlockPos p_180784_1_, EnumFacing p_180784_2_) {
        if (this.isCreative()) {
            if (!this.theWorld.func_175719_a((EntityPlayer) null, p_180784_1_, p_180784_2_)) {
                this.func_180237_b(p_180784_1_);
            }
        } else {
            Block var3 = this.theWorld.getBlockState(p_180784_1_).getBlock();

            if (this.gameType.isAdventure()) {
                if (this.gameType == WorldSettings.GameType.SPECTATOR) {
                    return;
                }

                if (!this.thisPlayerMP.func_175142_cm()) {
                    ItemStack var4 = this.thisPlayerMP.getCurrentEquippedItem();

                    if (var4 == null) {
                        return;
                    }

                    if (!var4.canDestroy(var3)) {
                        return;
                    }
                }
            }

            this.theWorld.func_175719_a((EntityPlayer) null, p_180784_1_, p_180784_2_);
            this.initialDamage = this.curblockDamage;
            float var6 = 1.0F;

            if (var3.getMaterial() != Material.air) {
                var3.onBlockClicked(this.theWorld, p_180784_1_, this.thisPlayerMP);
                var6 = var3.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, p_180784_1_);
            }

            if (var3.getMaterial() != Material.air && var6 >= 1.0F) {
                this.func_180237_b(p_180784_1_);
            } else {
                this.isDestroyingBlock = true;
                this.field_180240_f = p_180784_1_;
                int var5 = (int) (var6 * 10.0F);
                this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), p_180784_1_, var5);
                this.durabilityRemainingOnBlock = var5;
            }
        }
    }

    public void func_180785_a(BlockPos p_180785_1_) {
        if (p_180785_1_.equals(this.field_180240_f)) {
            int var2 = this.curblockDamage - this.initialDamage;
            Block var3 = this.theWorld.getBlockState(p_180785_1_).getBlock();

            if (var3.getMaterial() != Material.air) {
                float var4 = var3.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, p_180785_1_) * (float) (var2 + 1);

                if (var4 >= 0.7F) {
                    this.isDestroyingBlock = false;
                    this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), p_180785_1_, -1);
                    this.func_180237_b(p_180785_1_);
                } else if (!this.receivedFinishDiggingPacket) {
                    this.isDestroyingBlock = false;
                    this.receivedFinishDiggingPacket = true;
                    this.field_180241_i = p_180785_1_;
                    this.initialBlockDamage = this.initialDamage;
                }
            }
        }
    }

    public void func_180238_e() {
        this.isDestroyingBlock = false;
        this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.field_180240_f, -1);
    }

    private boolean func_180235_c(BlockPos p_180235_1_) {
        IBlockState var2 = this.theWorld.getBlockState(p_180235_1_);
        var2.getBlock().onBlockHarvested(this.theWorld, p_180235_1_, var2, this.thisPlayerMP);
        boolean var3 = this.theWorld.setBlockToAir(p_180235_1_);

        if (var3) {
            var2.getBlock().onBlockDestroyedByPlayer(this.theWorld, p_180235_1_, var2);
        }

        return var3;
    }

    public boolean func_180237_b(BlockPos p_180237_1_) {
        if (this.gameType.isCreative() && this.thisPlayerMP.getHeldItem() != null && this.thisPlayerMP.getHeldItem().getItem() instanceof ItemSword) {
            return false;
        } else {
            IBlockState var2 = this.theWorld.getBlockState(p_180237_1_);
            TileEntity var3 = this.theWorld.getTileEntity(p_180237_1_);

            if (this.gameType.isAdventure()) {
                if (this.gameType == WorldSettings.GameType.SPECTATOR) {
                    return false;
                }

                if (!this.thisPlayerMP.func_175142_cm()) {
                    ItemStack var4 = this.thisPlayerMP.getCurrentEquippedItem();

                    if (var4 == null) {
                        return false;
                    }

                    if (!var4.canDestroy(var2.getBlock())) {
                        return false;
                    }
                }
            }

            this.theWorld.playAuxSFXAtEntity(this.thisPlayerMP, 2001, p_180237_1_, Block.getStateId(var2));
            boolean var7 = this.func_180235_c(p_180237_1_);

            if (this.isCreative()) {
                this.thisPlayerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(this.theWorld, p_180237_1_));
            } else {
                ItemStack var5 = this.thisPlayerMP.getCurrentEquippedItem();
                boolean var6 = this.thisPlayerMP.canHarvestBlock(var2.getBlock());

                if (var5 != null) {
                    var5.onBlockDestroyed(this.theWorld, var2.getBlock(), p_180237_1_, this.thisPlayerMP);

                    if (var5.stackSize == 0) {
                        this.thisPlayerMP.destroyCurrentEquippedItem();
                    }
                }

                if (var7 && var6) {
                    var2.getBlock().harvestBlock(this.theWorld, this.thisPlayerMP, p_180237_1_, var2, var3);
                }
            }

            return var7;
        }
    }

    /**
     * Attempts to right-click use an item by the given EntityPlayer in the given World
     */
    public boolean tryUseItem(EntityPlayer p_73085_1_, World worldIn, ItemStack p_73085_3_) {
        if (this.gameType == WorldSettings.GameType.SPECTATOR) {
            return false;
        } else {
            int var4 = p_73085_3_.stackSize;
            int var5 = p_73085_3_.getMetadata();
            ItemStack var6 = p_73085_3_.useItemRightClick(worldIn, p_73085_1_);

            if (var6 == p_73085_3_ && (var6 == null || var6.stackSize == var4 && var6.getMaxItemUseDuration() <= 0 && var6.getMetadata() == var5)) {
                return false;
            } else {
                p_73085_1_.inventory.mainInventory[p_73085_1_.inventory.currentItem] = var6;

                if (this.isCreative()) {
                    var6.stackSize = var4;

                    if (var6.isItemStackDamageable()) {
                        var6.setItemDamage(var5);
                    }
                }

                if (var6.stackSize == 0) {
                    p_73085_1_.inventory.mainInventory[p_73085_1_.inventory.currentItem] = null;
                }

                if (!p_73085_1_.isUsingItem()) {
                    ((EntityPlayerMP) p_73085_1_).sendContainerToPlayer(p_73085_1_.inventoryContainer);
                }

                return true;
            }
        }
    }

    public boolean func_180236_a(EntityPlayer p_180236_1_, World worldIn, ItemStack p_180236_3_, BlockPos p_180236_4_, EnumFacing p_180236_5_, float p_180236_6_, float p_180236_7_, float p_180236_8_) {
        if (this.gameType == WorldSettings.GameType.SPECTATOR) {
            TileEntity var13 = worldIn.getTileEntity(p_180236_4_);

            if (var13 instanceof ILockableContainer) {
                Block var14 = worldIn.getBlockState(p_180236_4_).getBlock();
                ILockableContainer var15 = (ILockableContainer) var13;

                if (var15 instanceof TileEntityChest && var14 instanceof BlockChest) {
                    var15 = ((BlockChest) var14).getLockableContainer(worldIn, p_180236_4_);
                }

                if (var15 != null) {
                    p_180236_1_.displayGUIChest(var15);
                    return true;
                }
            } else if (var13 instanceof IInventory) {
                p_180236_1_.displayGUIChest((IInventory) var13);
                return true;
            }

            return false;
        } else {
            if (!p_180236_1_.isSneaking() || p_180236_1_.getHeldItem() == null) {
                IBlockState var9 = worldIn.getBlockState(p_180236_4_);

                if (var9.getBlock().onBlockActivated(worldIn, p_180236_4_, var9, p_180236_1_, p_180236_5_, p_180236_6_, p_180236_7_, p_180236_8_)) {
                    return true;
                }
            }

            if (p_180236_3_ == null) {
                return false;
            } else if (this.isCreative()) {
                int var12 = p_180236_3_.getMetadata();
                int var10 = p_180236_3_.stackSize;
                boolean var11 = p_180236_3_.onItemUse(p_180236_1_, worldIn, p_180236_4_, p_180236_5_, p_180236_6_, p_180236_7_, p_180236_8_);
                p_180236_3_.setItemDamage(var12);
                p_180236_3_.stackSize = var10;
                return var11;
            } else {
                return p_180236_3_.onItemUse(p_180236_1_, worldIn, p_180236_4_, p_180236_5_, p_180236_6_, p_180236_7_, p_180236_8_);
            }
        }
    }

    /**
     * Sets the world instance.
     */
    public void setWorld(WorldServer p_73080_1_) {
        this.theWorld = p_73080_1_;
    }
}
