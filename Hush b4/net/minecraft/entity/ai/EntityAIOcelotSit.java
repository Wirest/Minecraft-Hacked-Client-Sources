// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockBed;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.EntityOcelot;

public class EntityAIOcelotSit extends EntityAIMoveToBlock
{
    private final EntityOcelot field_151493_a;
    
    public EntityAIOcelotSit(final EntityOcelot p_i45315_1_, final double p_i45315_2_) {
        super(p_i45315_1_, p_i45315_2_, 8);
        this.field_151493_a = p_i45315_1_;
    }
    
    @Override
    public boolean shouldExecute() {
        return this.field_151493_a.isTamed() && !this.field_151493_a.isSitting() && super.shouldExecute();
    }
    
    @Override
    public boolean continueExecuting() {
        return super.continueExecuting();
    }
    
    @Override
    public void startExecuting() {
        super.startExecuting();
        this.field_151493_a.getAISit().setSitting(false);
    }
    
    @Override
    public void resetTask() {
        super.resetTask();
        this.field_151493_a.setSitting(false);
    }
    
    @Override
    public void updateTask() {
        super.updateTask();
        this.field_151493_a.getAISit().setSitting(false);
        if (!this.getIsAboveDestination()) {
            this.field_151493_a.setSitting(false);
        }
        else if (!this.field_151493_a.isSitting()) {
            this.field_151493_a.setSitting(true);
        }
    }
    
    @Override
    protected boolean shouldMoveTo(final World worldIn, final BlockPos pos) {
        if (!worldIn.isAirBlock(pos.up())) {
            return false;
        }
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        if (block == Blocks.chest) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityChest && ((TileEntityChest)tileentity).numPlayersUsing < 1) {
                return true;
            }
        }
        else {
            if (block == Blocks.lit_furnace) {
                return true;
            }
            if (block == Blocks.bed && iblockstate.getValue(BlockBed.PART) != BlockBed.EnumPartType.HEAD) {
                return true;
            }
        }
        return false;
    }
}
