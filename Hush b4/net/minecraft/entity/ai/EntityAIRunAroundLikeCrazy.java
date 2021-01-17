// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.EntityHorse;

public class EntityAIRunAroundLikeCrazy extends EntityAIBase
{
    private EntityHorse horseHost;
    private double speed;
    private double targetX;
    private double targetY;
    private double targetZ;
    
    public EntityAIRunAroundLikeCrazy(final EntityHorse horse, final double speedIn) {
        this.horseHost = horse;
        this.speed = speedIn;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.horseHost.isTame() || this.horseHost.riddenByEntity == null) {
            return false;
        }
        final Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.horseHost, 5, 4);
        if (vec3 == null) {
            return false;
        }
        this.targetX = vec3.xCoord;
        this.targetY = vec3.yCoord;
        this.targetZ = vec3.zCoord;
        return true;
    }
    
    @Override
    public void startExecuting() {
        this.horseHost.getNavigator().tryMoveToXYZ(this.targetX, this.targetY, this.targetZ, this.speed);
    }
    
    @Override
    public boolean continueExecuting() {
        return !this.horseHost.getNavigator().noPath() && this.horseHost.riddenByEntity != null;
    }
    
    @Override
    public void updateTask() {
        if (this.horseHost.getRNG().nextInt(50) == 0) {
            if (this.horseHost.riddenByEntity instanceof EntityPlayer) {
                final int i = this.horseHost.getTemper();
                final int j = this.horseHost.getMaxTemper();
                if (j > 0 && this.horseHost.getRNG().nextInt(j) < i) {
                    this.horseHost.setTamedBy((EntityPlayer)this.horseHost.riddenByEntity);
                    this.horseHost.worldObj.setEntityState(this.horseHost, (byte)7);
                    return;
                }
                this.horseHost.increaseTemper(5);
            }
            this.horseHost.riddenByEntity.mountEntity(null);
            this.horseHost.riddenByEntity = null;
            this.horseHost.makeHorseRearWithSound();
            this.horseHost.worldObj.setEntityState(this.horseHost, (byte)6);
        }
    }
}
