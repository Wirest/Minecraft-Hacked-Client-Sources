// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLiving;

public class EntityAIBreakDoor extends EntityAIDoorInteract
{
    private int breakingTime;
    private int previousBreakProgress;
    
    public EntityAIBreakDoor(final EntityLiving entityIn) {
        super(entityIn);
        this.previousBreakProgress = -1;
    }
    
    @Override
    public boolean shouldExecute() {
        if (!super.shouldExecute()) {
            return false;
        }
        if (!this.theEntity.worldObj.getGameRules().getBoolean("mobGriefing")) {
            return false;
        }
        final BlockDoor blockdoor = this.doorBlock;
        return !BlockDoor.isOpen(this.theEntity.worldObj, this.doorPosition);
    }
    
    @Override
    public void startExecuting() {
        super.startExecuting();
        this.breakingTime = 0;
    }
    
    @Override
    public boolean continueExecuting() {
        final double d0 = this.theEntity.getDistanceSq(this.doorPosition);
        if (this.breakingTime <= 240) {
            final BlockDoor blockdoor = this.doorBlock;
            if (!BlockDoor.isOpen(this.theEntity.worldObj, this.doorPosition) && d0 < 4.0) {
                final boolean flag = true;
                return flag;
            }
        }
        final boolean flag = false;
        return flag;
    }
    
    @Override
    public void resetTask() {
        super.resetTask();
        this.theEntity.worldObj.sendBlockBreakProgress(this.theEntity.getEntityId(), this.doorPosition, -1);
    }
    
    @Override
    public void updateTask() {
        super.updateTask();
        if (this.theEntity.getRNG().nextInt(20) == 0) {
            this.theEntity.worldObj.playAuxSFX(1010, this.doorPosition, 0);
        }
        ++this.breakingTime;
        final int i = (int)(this.breakingTime / 240.0f * 10.0f);
        if (i != this.previousBreakProgress) {
            this.theEntity.worldObj.sendBlockBreakProgress(this.theEntity.getEntityId(), this.doorPosition, i);
            this.previousBreakProgress = i;
        }
        if (this.breakingTime == 240 && this.theEntity.worldObj.getDifficulty() == EnumDifficulty.HARD) {
            this.theEntity.worldObj.setBlockToAir(this.doorPosition);
            this.theEntity.worldObj.playAuxSFX(1012, this.doorPosition, 0);
            this.theEntity.worldObj.playAuxSFX(2001, this.doorPosition, Block.getIdFromBlock(this.doorBlock));
        }
    }
}
