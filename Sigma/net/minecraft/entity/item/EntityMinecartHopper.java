package net.minecraft.entity.item;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMinecartHopper extends EntityMinecartContainer implements IHopper {
    /**
     * Whether this hopper minecart is being blocked by an activator rail.
     */
    private boolean isBlocked = true;
    private int transferTicker = -1;
    private BlockPos field_174900_c;
    private static final String __OBFID = "CL_00001676";

    public EntityMinecartHopper(World worldIn) {
        super(worldIn);
        this.field_174900_c = BlockPos.ORIGIN;
    }

    public EntityMinecartHopper(World worldIn, double p_i1721_2_, double p_i1721_4_, double p_i1721_6_) {
        super(worldIn, p_i1721_2_, p_i1721_4_, p_i1721_6_);
        this.field_174900_c = BlockPos.ORIGIN;
    }

    public EntityMinecart.EnumMinecartType func_180456_s() {
        return EntityMinecart.EnumMinecartType.HOPPER;
    }

    public IBlockState func_180457_u() {
        return Blocks.hopper.getDefaultState();
    }

    public int getDefaultDisplayTileOffset() {
        return 1;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory() {
        return 5;
    }

    /**
     * First layer of player interaction
     */
    public boolean interactFirst(EntityPlayer playerIn) {
        if (!this.worldObj.isRemote) {
            playerIn.displayGUIChest(this);
        }

        return true;
    }

    /**
     * Called every tick the minecart is on an activator rail. Args: x, y, z, is the rail receiving power
     */
    public void onActivatorRailPass(int p_96095_1_, int p_96095_2_, int p_96095_3_, boolean p_96095_4_) {
        boolean var5 = !p_96095_4_;

        if (var5 != this.getBlocked()) {
            this.setBlocked(var5);
        }
    }

    /**
     * Get whether this hopper minecart is being blocked by an activator rail.
     */
    public boolean getBlocked() {
        return this.isBlocked;
    }

    /**
     * Set whether this hopper minecart is being blocked by an activator rail.
     */
    public void setBlocked(boolean p_96110_1_) {
        this.isBlocked = p_96110_1_;
    }

    /**
     * Returns the worldObj for this tileEntity.
     */
    public World getWorld() {
        return this.worldObj;
    }

    /**
     * Gets the world X position for this hopper entity.
     */
    public double getXPos() {
        return this.posX;
    }

    /**
     * Gets the world Y position for this hopper entity.
     */
    public double getYPos() {
        return this.posY;
    }

    /**
     * Gets the world Z position for this hopper entity.
     */
    public double getZPos() {
        return this.posZ;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        super.onUpdate();

        if (!this.worldObj.isRemote && this.isEntityAlive() && this.getBlocked()) {
            BlockPos var1 = new BlockPos(this);

            if (var1.equals(this.field_174900_c)) {
                --this.transferTicker;
            } else {
                this.setTransferTicker(0);
            }

            if (!this.canTransfer()) {
                this.setTransferTicker(0);

                if (this.func_96112_aD()) {
                    this.setTransferTicker(4);
                    this.markDirty();
                }
            }
        }
    }

    public boolean func_96112_aD() {
        if (TileEntityHopper.func_145891_a(this)) {
            return true;
        } else {
            List var1 = this.worldObj.func_175647_a(EntityItem.class, this.getEntityBoundingBox().expand(0.25D, 0.0D, 0.25D), IEntitySelector.selectAnything);

            if (var1.size() > 0) {
                TileEntityHopper.func_145898_a(this, (EntityItem) var1.get(0));
            }

            return false;
        }
    }

    public void killMinecart(DamageSource p_94095_1_) {
        super.killMinecart(p_94095_1_);
        this.dropItemWithOffset(Item.getItemFromBlock(Blocks.hopper), 1, 0.0F);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("TransferCooldown", this.transferTicker);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.transferTicker = tagCompund.getInteger("TransferCooldown");
    }

    /**
     * Sets the transfer ticker, used to determine the delay between transfers.
     */
    public void setTransferTicker(int p_98042_1_) {
        this.transferTicker = p_98042_1_;
    }

    /**
     * Returns whether the hopper cart can currently transfer an item.
     */
    public boolean canTransfer() {
        return this.transferTicker > 0;
    }

    public String getGuiID() {
        return "minecraft:hopper";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerHopper(playerInventory, this, playerIn);
    }
}
