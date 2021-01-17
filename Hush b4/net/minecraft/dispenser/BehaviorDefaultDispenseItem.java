// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.dispenser;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockDispenser;
import net.minecraft.item.ItemStack;

public class BehaviorDefaultDispenseItem implements IBehaviorDispenseItem
{
    @Override
    public final ItemStack dispense(final IBlockSource source, final ItemStack stack) {
        final ItemStack itemstack = this.dispenseStack(source, stack);
        this.playDispenseSound(source);
        this.spawnDispenseParticles(source, BlockDispenser.getFacing(source.getBlockMetadata()));
        return itemstack;
    }
    
    protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
        final EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
        final IPosition iposition = BlockDispenser.getDispensePosition(source);
        final ItemStack itemstack = stack.splitStack(1);
        doDispense(source.getWorld(), itemstack, 6, enumfacing, iposition);
        return stack;
    }
    
    public static void doDispense(final World worldIn, final ItemStack stack, final int speed, final EnumFacing facing, final IPosition position) {
        final double d0 = position.getX();
        double d2 = position.getY();
        final double d3 = position.getZ();
        if (facing.getAxis() == EnumFacing.Axis.Y) {
            d2 -= 0.125;
        }
        else {
            d2 -= 0.15625;
        }
        final EntityItem entityitem = new EntityItem(worldIn, d0, d2, d3, stack);
        final double d4 = worldIn.rand.nextDouble() * 0.1 + 0.2;
        entityitem.motionX = facing.getFrontOffsetX() * d4;
        entityitem.motionY = 0.20000000298023224;
        entityitem.motionZ = facing.getFrontOffsetZ() * d4;
        final EntityItem entityItem = entityitem;
        entityItem.motionX += worldIn.rand.nextGaussian() * 0.007499999832361937 * speed;
        final EntityItem entityItem2 = entityitem;
        entityItem2.motionY += worldIn.rand.nextGaussian() * 0.007499999832361937 * speed;
        final EntityItem entityItem3 = entityitem;
        entityItem3.motionZ += worldIn.rand.nextGaussian() * 0.007499999832361937 * speed;
        worldIn.spawnEntityInWorld(entityitem);
    }
    
    protected void playDispenseSound(final IBlockSource source) {
        source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
    }
    
    protected void spawnDispenseParticles(final IBlockSource source, final EnumFacing facingIn) {
        source.getWorld().playAuxSFX(2000, source.getBlockPos(), this.func_82488_a(facingIn));
    }
    
    private int func_82488_a(final EnumFacing facingIn) {
        return facingIn.getFrontOffsetX() + 1 + (facingIn.getFrontOffsetZ() + 1) * 3;
    }
}
