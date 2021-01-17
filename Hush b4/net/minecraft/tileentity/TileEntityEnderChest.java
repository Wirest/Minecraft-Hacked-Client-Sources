// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ITickable;

public class TileEntityEnderChest extends TileEntity implements ITickable
{
    public float lidAngle;
    public float prevLidAngle;
    public int numPlayersUsing;
    private int ticksSinceSync;
    
    @Override
    public void update() {
        if (++this.ticksSinceSync % 20 * 4 == 0) {
            this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.numPlayersUsing);
        }
        this.prevLidAngle = this.lidAngle;
        final int i = this.pos.getX();
        final int j = this.pos.getY();
        final int k = this.pos.getZ();
        final float f = 0.1f;
        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0f) {
            final double d0 = i + 0.5;
            final double d2 = k + 0.5;
            this.worldObj.playSoundEffect(d0, j + 0.5, d2, "random.chestopen", 0.5f, this.worldObj.rand.nextFloat() * 0.1f + 0.9f);
        }
        if ((this.numPlayersUsing == 0 && this.lidAngle > 0.0f) || (this.numPlayersUsing > 0 && this.lidAngle < 1.0f)) {
            final float f2 = this.lidAngle;
            if (this.numPlayersUsing > 0) {
                this.lidAngle += f;
            }
            else {
                this.lidAngle -= f;
            }
            if (this.lidAngle > 1.0f) {
                this.lidAngle = 1.0f;
            }
            final float f3 = 0.5f;
            if (this.lidAngle < f3 && f2 >= f3) {
                final double d3 = i + 0.5;
                final double d4 = k + 0.5;
                this.worldObj.playSoundEffect(d3, j + 0.5, d4, "random.chestclosed", 0.5f, this.worldObj.rand.nextFloat() * 0.1f + 0.9f);
            }
            if (this.lidAngle < 0.0f) {
                this.lidAngle = 0.0f;
            }
        }
    }
    
    @Override
    public boolean receiveClientEvent(final int id, final int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        }
        return super.receiveClientEvent(id, type);
    }
    
    @Override
    public void invalidate() {
        this.updateContainingBlockInfo();
        super.invalidate();
    }
    
    public void openChest() {
        ++this.numPlayersUsing;
        this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.numPlayersUsing);
    }
    
    public void closeChest() {
        --this.numPlayersUsing;
        this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.numPlayersUsing);
    }
    
    public boolean canBeUsed(final EntityPlayer p_145971_1_) {
        return this.worldObj.getTileEntity(this.pos) == this && p_145971_1_.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64.0;
    }
}
