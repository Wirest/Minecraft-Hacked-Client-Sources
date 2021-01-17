// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.dispenser.IBehaviorDispenseItem;

public class ItemMinecart extends Item
{
    private static final IBehaviorDispenseItem dispenserMinecartBehavior;
    private final EntityMinecart.EnumMinecartType minecartType;
    
    static {
        dispenserMinecartBehavior = new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();
            
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
                final World world = source.getWorld();
                final double d0 = source.getX() + enumfacing.getFrontOffsetX() * 1.125;
                final double d2 = Math.floor(source.getY()) + enumfacing.getFrontOffsetY();
                final double d3 = source.getZ() + enumfacing.getFrontOffsetZ() * 1.125;
                final BlockPos blockpos = source.getBlockPos().offset(enumfacing);
                final IBlockState iblockstate = world.getBlockState(blockpos);
                final BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (iblockstate.getBlock() instanceof BlockRailBase) ? iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                double d4;
                if (BlockRailBase.isRailBlock(iblockstate)) {
                    if (blockrailbase$enumraildirection.isAscending()) {
                        d4 = 0.6;
                    }
                    else {
                        d4 = 0.1;
                    }
                }
                else {
                    if (iblockstate.getBlock().getMaterial() != Material.air || !BlockRailBase.isRailBlock(world.getBlockState(blockpos.down()))) {
                        return this.behaviourDefaultDispenseItem.dispense(source, stack);
                    }
                    final IBlockState iblockstate2 = world.getBlockState(blockpos.down());
                    final BlockRailBase.EnumRailDirection blockrailbase$enumraildirection2 = (iblockstate2.getBlock() instanceof BlockRailBase) ? iblockstate2.getValue(((BlockRailBase)iblockstate2.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                    if (enumfacing != EnumFacing.DOWN && blockrailbase$enumraildirection2.isAscending()) {
                        d4 = -0.4;
                    }
                    else {
                        d4 = -0.9;
                    }
                }
                final EntityMinecart entityminecart = EntityMinecart.func_180458_a(world, d0, d2 + d4, d3, ((ItemMinecart)stack.getItem()).minecartType);
                if (stack.hasDisplayName()) {
                    entityminecart.setCustomNameTag(stack.getDisplayName());
                }
                world.spawnEntityInWorld(entityminecart);
                stack.splitStack(1);
                return stack;
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
                source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
            }
        };
    }
    
    public ItemMinecart(final EntityMinecart.EnumMinecartType type) {
        this.maxStackSize = 1;
        this.minecartType = type;
        this.setCreativeTab(CreativeTabs.tabTransport);
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, ItemMinecart.dispenserMinecartBehavior);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (BlockRailBase.isRailBlock(iblockstate)) {
            if (!worldIn.isRemote) {
                final BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (iblockstate.getBlock() instanceof BlockRailBase) ? iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                double d0 = 0.0;
                if (blockrailbase$enumraildirection.isAscending()) {
                    d0 = 0.5;
                }
                final EntityMinecart entityminecart = EntityMinecart.func_180458_a(worldIn, pos.getX() + 0.5, pos.getY() + 0.0625 + d0, pos.getZ() + 0.5, this.minecartType);
                if (stack.hasDisplayName()) {
                    entityminecart.setCustomNameTag(stack.getDisplayName());
                }
                worldIn.spawnEntityInWorld(entityminecart);
            }
            --stack.stackSize;
            return true;
        }
        return false;
    }
}
