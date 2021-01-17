// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.village.Village;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.entity.EntityCreature;

public class EntityAIRestrictOpenDoor extends EntityAIBase
{
    private EntityCreature entityObj;
    private VillageDoorInfo frontDoor;
    
    public EntityAIRestrictOpenDoor(final EntityCreature creatureIn) {
        this.entityObj = creatureIn;
        if (!(creatureIn.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob type for RestrictOpenDoorGoal");
        }
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.entityObj.worldObj.isDaytime()) {
            return false;
        }
        final BlockPos blockpos = new BlockPos(this.entityObj);
        final Village village = this.entityObj.worldObj.getVillageCollection().getNearestVillage(blockpos, 16);
        if (village == null) {
            return false;
        }
        this.frontDoor = village.getNearestDoor(blockpos);
        return this.frontDoor != null && this.frontDoor.getDistanceToInsideBlockSq(blockpos) < 2.25;
    }
    
    @Override
    public boolean continueExecuting() {
        return !this.entityObj.worldObj.isDaytime() && (!this.frontDoor.getIsDetachedFromVillageFlag() && this.frontDoor.func_179850_c(new BlockPos(this.entityObj)));
    }
    
    @Override
    public void startExecuting() {
        ((PathNavigateGround)this.entityObj.getNavigator()).setBreakDoors(false);
        ((PathNavigateGround)this.entityObj.getNavigator()).setEnterDoors(false);
    }
    
    @Override
    public void resetTask() {
        ((PathNavigateGround)this.entityObj.getNavigator()).setBreakDoors(true);
        ((PathNavigateGround)this.entityObj.getNavigator()).setEnterDoors(true);
        this.frontDoor = null;
    }
    
    @Override
    public void updateTask() {
        this.frontDoor.incrementDoorOpeningRestrictionCounter();
    }
}
