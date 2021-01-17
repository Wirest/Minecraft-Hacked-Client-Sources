// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Iterator;
import net.minecraft.util.AxisAlignedBB;
import java.util.Collection;
import net.minecraft.init.Blocks;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ITickable;

public class TileEntityPiston extends TileEntity implements ITickable
{
    private IBlockState pistonState;
    private EnumFacing pistonFacing;
    private boolean extending;
    private boolean shouldHeadBeRendered;
    private float progress;
    private float lastProgress;
    private List<Entity> field_174933_k;
    
    public TileEntityPiston() {
        this.field_174933_k = (List<Entity>)Lists.newArrayList();
    }
    
    public TileEntityPiston(final IBlockState pistonStateIn, final EnumFacing pistonFacingIn, final boolean extendingIn, final boolean shouldHeadBeRenderedIn) {
        this.field_174933_k = (List<Entity>)Lists.newArrayList();
        this.pistonState = pistonStateIn;
        this.pistonFacing = pistonFacingIn;
        this.extending = extendingIn;
        this.shouldHeadBeRendered = shouldHeadBeRenderedIn;
    }
    
    public IBlockState getPistonState() {
        return this.pistonState;
    }
    
    @Override
    public int getBlockMetadata() {
        return 0;
    }
    
    public boolean isExtending() {
        return this.extending;
    }
    
    public EnumFacing getFacing() {
        return this.pistonFacing;
    }
    
    public boolean shouldPistonHeadBeRendered() {
        return this.shouldHeadBeRendered;
    }
    
    public float getProgress(float ticks) {
        if (ticks > 1.0f) {
            ticks = 1.0f;
        }
        return this.lastProgress + (this.progress - this.lastProgress) * ticks;
    }
    
    public float getOffsetX(final float ticks) {
        return this.extending ? ((this.getProgress(ticks) - 1.0f) * this.pistonFacing.getFrontOffsetX()) : ((1.0f - this.getProgress(ticks)) * this.pistonFacing.getFrontOffsetX());
    }
    
    public float getOffsetY(final float ticks) {
        return this.extending ? ((this.getProgress(ticks) - 1.0f) * this.pistonFacing.getFrontOffsetY()) : ((1.0f - this.getProgress(ticks)) * this.pistonFacing.getFrontOffsetY());
    }
    
    public float getOffsetZ(final float ticks) {
        return this.extending ? ((this.getProgress(ticks) - 1.0f) * this.pistonFacing.getFrontOffsetZ()) : ((1.0f - this.getProgress(ticks)) * this.pistonFacing.getFrontOffsetZ());
    }
    
    private void launchWithSlimeBlock(float p_145863_1_, final float p_145863_2_) {
        if (this.extending) {
            p_145863_1_ = 1.0f - p_145863_1_;
        }
        else {
            --p_145863_1_;
        }
        final AxisAlignedBB axisalignedbb = Blocks.piston_extension.getBoundingBox(this.worldObj, this.pos, this.pistonState, p_145863_1_, this.pistonFacing);
        if (axisalignedbb != null) {
            final List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);
            if (!list.isEmpty()) {
                this.field_174933_k.addAll(list);
                for (final Entity entity : this.field_174933_k) {
                    if (this.pistonState.getBlock() == Blocks.slime_block && this.extending) {
                        switch (this.pistonFacing.getAxis()) {
                            case X: {
                                entity.motionX = this.pistonFacing.getFrontOffsetX();
                                continue;
                            }
                            case Y: {
                                entity.motionY = this.pistonFacing.getFrontOffsetY();
                                continue;
                            }
                            case Z: {
                                entity.motionZ = this.pistonFacing.getFrontOffsetZ();
                                continue;
                            }
                        }
                    }
                    else {
                        entity.moveEntity(p_145863_2_ * this.pistonFacing.getFrontOffsetX(), p_145863_2_ * this.pistonFacing.getFrontOffsetY(), p_145863_2_ * this.pistonFacing.getFrontOffsetZ());
                    }
                }
                this.field_174933_k.clear();
            }
        }
    }
    
    public void clearPistonTileEntity() {
        if (this.lastProgress < 1.0f && this.worldObj != null) {
            final float n = 1.0f;
            this.progress = n;
            this.lastProgress = n;
            this.worldObj.removeTileEntity(this.pos);
            this.invalidate();
            if (this.worldObj.getBlockState(this.pos).getBlock() == Blocks.piston_extension) {
                this.worldObj.setBlockState(this.pos, this.pistonState, 3);
                this.worldObj.notifyBlockOfStateChange(this.pos, this.pistonState.getBlock());
            }
        }
    }
    
    @Override
    public void update() {
        this.lastProgress = this.progress;
        if (this.lastProgress >= 1.0f) {
            this.launchWithSlimeBlock(1.0f, 0.25f);
            this.worldObj.removeTileEntity(this.pos);
            this.invalidate();
            if (this.worldObj.getBlockState(this.pos).getBlock() == Blocks.piston_extension) {
                this.worldObj.setBlockState(this.pos, this.pistonState, 3);
                this.worldObj.notifyBlockOfStateChange(this.pos, this.pistonState.getBlock());
            }
        }
        else {
            this.progress += 0.5f;
            if (this.progress >= 1.0f) {
                this.progress = 1.0f;
            }
            if (this.extending) {
                this.launchWithSlimeBlock(this.progress, this.progress - this.lastProgress + 0.0625f);
            }
        }
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.pistonState = Block.getBlockById(compound.getInteger("blockId")).getStateFromMeta(compound.getInteger("blockData"));
        this.pistonFacing = EnumFacing.getFront(compound.getInteger("facing"));
        final float float1 = compound.getFloat("progress");
        this.progress = float1;
        this.lastProgress = float1;
        this.extending = compound.getBoolean("extending");
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("blockId", Block.getIdFromBlock(this.pistonState.getBlock()));
        compound.setInteger("blockData", this.pistonState.getBlock().getMetaFromState(this.pistonState));
        compound.setInteger("facing", this.pistonFacing.getIndex());
        compound.setFloat("progress", this.lastProgress);
        compound.setBoolean("extending", this.extending);
    }
}
