// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMinecartChest extends EntityMinecartContainer
{
    public EntityMinecartChest(final World worldIn) {
        super(worldIn);
    }
    
    public EntityMinecartChest(final World worldIn, final double p_i1715_2_, final double p_i1715_4_, final double p_i1715_6_) {
        super(worldIn, p_i1715_2_, p_i1715_4_, p_i1715_6_);
    }
    
    @Override
    public void killMinecart(final DamageSource p_94095_1_) {
        super.killMinecart(p_94095_1_);
        if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
            this.dropItemWithOffset(Item.getItemFromBlock(Blocks.chest), 1, 0.0f);
        }
    }
    
    @Override
    public int getSizeInventory() {
        return 27;
    }
    
    @Override
    public EnumMinecartType getMinecartType() {
        return EnumMinecartType.CHEST;
    }
    
    @Override
    public IBlockState getDefaultDisplayTile() {
        return Blocks.chest.getDefaultState().withProperty((IProperty<Comparable>)BlockChest.FACING, EnumFacing.NORTH);
    }
    
    @Override
    public int getDefaultDisplayTileOffset() {
        return 8;
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:chest";
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        return new ContainerChest(playerInventory, this, playerIn);
    }
}
