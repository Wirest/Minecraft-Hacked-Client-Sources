package net.minecraft.entity.ai;

import java.util.Random;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityAIFleeSun extends EntityAIBase {
    private EntityCreature theCreature;
    private double shelterX;
    private double shelterY;
    private double shelterZ;
    private double movementSpeed;
    private World theWorld;
    private static final String __OBFID = "CL_00001583";

    public EntityAIFleeSun(EntityCreature p_i1623_1_, double p_i1623_2_) {
        this.theCreature = p_i1623_1_;
        this.movementSpeed = p_i1623_2_;
        this.theWorld = p_i1623_1_.worldObj;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (!this.theWorld.isDaytime()) {
            return false;
        } else if (!this.theCreature.isBurning()) {
            return false;
        } else if (!this.theWorld.isAgainstSky(new BlockPos(this.theCreature.posX, this.theCreature.getEntityBoundingBox().minY, this.theCreature.posZ))) {
            return false;
        } else {
            Vec3 var1 = this.findPossibleShelter();

            if (var1 == null) {
                return false;
            } else {
                this.shelterX = var1.xCoord;
                this.shelterY = var1.yCoord;
                this.shelterZ = var1.zCoord;
                return true;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        return !this.theCreature.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.theCreature.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }

    private Vec3 findPossibleShelter() {
        Random var1 = this.theCreature.getRNG();
        BlockPos var2 = new BlockPos(this.theCreature.posX, this.theCreature.getEntityBoundingBox().minY, this.theCreature.posZ);

        for (int var3 = 0; var3 < 10; ++var3) {
            BlockPos var4 = var2.add(var1.nextInt(20) - 10, var1.nextInt(6) - 3, var1.nextInt(20) - 10);

            if (!this.theWorld.isAgainstSky(var4) && this.theCreature.func_180484_a(var4) < 0.0F) {
                return new Vec3((double) var4.getX(), (double) var4.getY(), (double) var4.getZ());
            }
        }

        return null;
    }
}
