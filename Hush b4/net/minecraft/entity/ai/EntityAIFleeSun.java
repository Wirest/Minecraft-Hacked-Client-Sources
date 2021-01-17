// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import java.util.Random;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.EntityCreature;

public class EntityAIFleeSun extends EntityAIBase
{
    private EntityCreature theCreature;
    private double shelterX;
    private double shelterY;
    private double shelterZ;
    private double movementSpeed;
    private World theWorld;
    
    public EntityAIFleeSun(final EntityCreature theCreatureIn, final double movementSpeedIn) {
        this.theCreature = theCreatureIn;
        this.movementSpeed = movementSpeedIn;
        this.theWorld = theCreatureIn.worldObj;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (!this.theWorld.isDaytime()) {
            return false;
        }
        if (!this.theCreature.isBurning()) {
            return false;
        }
        if (!this.theWorld.canSeeSky(new BlockPos(this.theCreature.posX, this.theCreature.getEntityBoundingBox().minY, this.theCreature.posZ))) {
            return false;
        }
        final Vec3 vec3 = this.findPossibleShelter();
        if (vec3 == null) {
            return false;
        }
        this.shelterX = vec3.xCoord;
        this.shelterY = vec3.yCoord;
        this.shelterZ = vec3.zCoord;
        return true;
    }
    
    @Override
    public boolean continueExecuting() {
        return !this.theCreature.getNavigator().noPath();
    }
    
    @Override
    public void startExecuting() {
        this.theCreature.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }
    
    private Vec3 findPossibleShelter() {
        final Random random = this.theCreature.getRNG();
        final BlockPos blockpos = new BlockPos(this.theCreature.posX, this.theCreature.getEntityBoundingBox().minY, this.theCreature.posZ);
        for (int i = 0; i < 10; ++i) {
            final BlockPos blockpos2 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
            if (!this.theWorld.canSeeSky(blockpos2) && this.theCreature.getBlockPathWeight(blockpos2) < 0.0f) {
                return new Vec3(blockpos2.getX(), blockpos2.getY(), blockpos2.getZ());
            }
        }
        return null;
    }
}
