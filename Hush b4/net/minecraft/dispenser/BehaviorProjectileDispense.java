// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.dispenser;

import net.minecraft.entity.IProjectile;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.block.BlockDispenser;
import net.minecraft.item.ItemStack;

public abstract class BehaviorProjectileDispense extends BehaviorDefaultDispenseItem
{
    public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
        final World world = source.getWorld();
        final IPosition iposition = BlockDispenser.getDispensePosition(source);
        final EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
        final IProjectile iprojectile = this.getProjectileEntity(world, iposition);
        iprojectile.setThrowableHeading(enumfacing.getFrontOffsetX(), enumfacing.getFrontOffsetY() + 0.1f, enumfacing.getFrontOffsetZ(), this.func_82500_b(), this.func_82498_a());
        world.spawnEntityInWorld((Entity)iprojectile);
        stack.splitStack(1);
        return stack;
    }
    
    @Override
    protected void playDispenseSound(final IBlockSource source) {
        source.getWorld().playAuxSFX(1002, source.getBlockPos(), 0);
    }
    
    protected abstract IProjectile getProjectileEntity(final World p0, final IPosition p1);
    
    protected float func_82498_a() {
        return 6.0f;
    }
    
    protected float func_82500_b() {
        return 1.1f;
    }
}
