package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIMoveIndoors extends EntityAIBase
{
    private EntityCreature entityObj;
    private VillageDoorInfo doorInfo;
    private int insidePosX = -1;
    private int insidePosZ = -1;
    private static final String __OBFID = "CL_00001596";

    public EntityAIMoveIndoors(EntityCreature p_i1637_1_)
    {
        this.entityObj = p_i1637_1_;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        BlockPos var1 = new BlockPos(this.entityObj);

        if ((!this.entityObj.worldObj.isDaytime() || this.entityObj.worldObj.isRaining() && !this.entityObj.worldObj.getBiomeGenForCoords(var1).canSpawnLightningBolt()) && !this.entityObj.worldObj.provider.getHasNoSky())
        {
            if (this.entityObj.getRNG().nextInt(50) != 0)
            {
                return false;
            }
            else if (this.insidePosX != -1 && this.entityObj.getDistanceSq((double)this.insidePosX, this.entityObj.posY, (double)this.insidePosZ) < 4.0D)
            {
                return false;
            }
            else
            {
                Village var2 = this.entityObj.worldObj.getVillageCollection().func_176056_a(var1, 14);

                if (var2 == null)
                {
                    return false;
                }
                else
                {
                    this.doorInfo = var2.func_179863_c(var1);
                    return this.doorInfo != null;
                }
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !this.entityObj.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.insidePosX = -1;
        BlockPos var1 = this.doorInfo.func_179856_e();
        int var2 = var1.getX();
        int var3 = var1.getY();
        int var4 = var1.getZ();

        if (this.entityObj.getDistanceSq(var1) > 256.0D)
        {
            Vec3 var5 = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityObj, 14, 3, new Vec3((double)var2 + 0.5D, (double)var3, (double)var4 + 0.5D));

            if (var5 != null)
            {
                this.entityObj.getNavigator().tryMoveToXYZ(var5.xCoord, var5.yCoord, var5.zCoord, 1.0D);
            }
        }
        else
        {
            this.entityObj.getNavigator().tryMoveToXYZ((double)var2 + 0.5D, (double)var3, (double)var4 + 0.5D, 1.0D);
        }
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.insidePosX = this.doorInfo.func_179856_e().getX();
        this.insidePosZ = this.doorInfo.func_179856_e().getZ();
        this.doorInfo = null;
    }
}
