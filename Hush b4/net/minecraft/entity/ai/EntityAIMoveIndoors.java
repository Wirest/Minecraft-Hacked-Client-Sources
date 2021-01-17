// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.util.Vec3;
import net.minecraft.village.Village;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.entity.EntityCreature;

public class EntityAIMoveIndoors extends EntityAIBase
{
    private EntityCreature entityObj;
    private VillageDoorInfo doorInfo;
    private int insidePosX;
    private int insidePosZ;
    
    public EntityAIMoveIndoors(final EntityCreature entityObjIn) {
        this.insidePosX = -1;
        this.insidePosZ = -1;
        this.entityObj = entityObjIn;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        final BlockPos blockpos = new BlockPos(this.entityObj);
        if ((this.entityObj.worldObj.isDaytime() && (!this.entityObj.worldObj.isRaining() || this.entityObj.worldObj.getBiomeGenForCoords(blockpos).canSpawnLightningBolt())) || this.entityObj.worldObj.provider.getHasNoSky()) {
            return false;
        }
        if (this.entityObj.getRNG().nextInt(50) != 0) {
            return false;
        }
        if (this.insidePosX != -1 && this.entityObj.getDistanceSq(this.insidePosX, this.entityObj.posY, this.insidePosZ) < 4.0) {
            return false;
        }
        final Village village = this.entityObj.worldObj.getVillageCollection().getNearestVillage(blockpos, 14);
        if (village == null) {
            return false;
        }
        this.doorInfo = village.getDoorInfo(blockpos);
        return this.doorInfo != null;
    }
    
    @Override
    public boolean continueExecuting() {
        return !this.entityObj.getNavigator().noPath();
    }
    
    @Override
    public void startExecuting() {
        this.insidePosX = -1;
        final BlockPos blockpos = this.doorInfo.getInsideBlockPos();
        final int i = blockpos.getX();
        final int j = blockpos.getY();
        final int k = blockpos.getZ();
        if (this.entityObj.getDistanceSq(blockpos) > 256.0) {
            final Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityObj, 14, 3, new Vec3(i + 0.5, j, k + 0.5));
            if (vec3 != null) {
                this.entityObj.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, 1.0);
            }
        }
        else {
            this.entityObj.getNavigator().tryMoveToXYZ(i + 0.5, j, k + 0.5, 1.0);
        }
    }
    
    @Override
    public void resetTask() {
        this.insidePosX = this.doorInfo.getInsideBlockPos().getX();
        this.insidePosZ = this.doorInfo.getInsideBlockPos().getZ();
        this.doorInfo = null;
    }
}
