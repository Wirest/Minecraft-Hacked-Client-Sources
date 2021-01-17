// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import java.util.Iterator;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.util.Vec3;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.pathfinding.PathNavigateGround;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.entity.EntityCreature;

public class EntityAIMoveThroughVillage extends EntityAIBase
{
    private EntityCreature theEntity;
    private double movementSpeed;
    private PathEntity entityPathNavigate;
    private VillageDoorInfo doorInfo;
    private boolean isNocturnal;
    private List<VillageDoorInfo> doorList;
    
    public EntityAIMoveThroughVillage(final EntityCreature theEntityIn, final double movementSpeedIn, final boolean isNocturnalIn) {
        this.doorList = (List<VillageDoorInfo>)Lists.newArrayList();
        this.theEntity = theEntityIn;
        this.movementSpeed = movementSpeedIn;
        this.isNocturnal = isNocturnalIn;
        this.setMutexBits(1);
        if (!(theEntityIn.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob for MoveThroughVillageGoal");
        }
    }
    
    @Override
    public boolean shouldExecute() {
        this.resizeDoorList();
        if (this.isNocturnal && this.theEntity.worldObj.isDaytime()) {
            return false;
        }
        final Village village = this.theEntity.worldObj.getVillageCollection().getNearestVillage(new BlockPos(this.theEntity), 0);
        if (village == null) {
            return false;
        }
        this.doorInfo = this.findNearestDoor(village);
        if (this.doorInfo == null) {
            return false;
        }
        final PathNavigateGround pathnavigateground = (PathNavigateGround)this.theEntity.getNavigator();
        final boolean flag = pathnavigateground.getEnterDoors();
        pathnavigateground.setBreakDoors(false);
        this.entityPathNavigate = pathnavigateground.getPathToPos(this.doorInfo.getDoorBlockPos());
        pathnavigateground.setBreakDoors(flag);
        if (this.entityPathNavigate != null) {
            return true;
        }
        final Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 10, 7, new Vec3(this.doorInfo.getDoorBlockPos().getX(), this.doorInfo.getDoorBlockPos().getY(), this.doorInfo.getDoorBlockPos().getZ()));
        if (vec3 == null) {
            return false;
        }
        pathnavigateground.setBreakDoors(false);
        this.entityPathNavigate = this.theEntity.getNavigator().getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord);
        pathnavigateground.setBreakDoors(flag);
        return this.entityPathNavigate != null;
    }
    
    @Override
    public boolean continueExecuting() {
        if (this.theEntity.getNavigator().noPath()) {
            return false;
        }
        final float f = this.theEntity.width + 4.0f;
        return this.theEntity.getDistanceSq(this.doorInfo.getDoorBlockPos()) > f * f;
    }
    
    @Override
    public void startExecuting() {
        this.theEntity.getNavigator().setPath(this.entityPathNavigate, this.movementSpeed);
    }
    
    @Override
    public void resetTask() {
        if (this.theEntity.getNavigator().noPath() || this.theEntity.getDistanceSq(this.doorInfo.getDoorBlockPos()) < 16.0) {
            this.doorList.add(this.doorInfo);
        }
    }
    
    private VillageDoorInfo findNearestDoor(final Village villageIn) {
        VillageDoorInfo villagedoorinfo = null;
        int i = Integer.MAX_VALUE;
        for (final VillageDoorInfo villagedoorinfo2 : villageIn.getVillageDoorInfoList()) {
            final int j = villagedoorinfo2.getDistanceSquared(MathHelper.floor_double(this.theEntity.posX), MathHelper.floor_double(this.theEntity.posY), MathHelper.floor_double(this.theEntity.posZ));
            if (j < i && !this.doesDoorListContain(villagedoorinfo2)) {
                villagedoorinfo = villagedoorinfo2;
                i = j;
            }
        }
        return villagedoorinfo;
    }
    
    private boolean doesDoorListContain(final VillageDoorInfo doorInfoIn) {
        for (final VillageDoorInfo villagedoorinfo : this.doorList) {
            if (doorInfoIn.getDoorBlockPos().equals(villagedoorinfo.getDoorBlockPos())) {
                return true;
            }
        }
        return false;
    }
    
    private void resizeDoorList() {
        if (this.doorList.size() > 15) {
            this.doorList.remove(0);
        }
    }
}
