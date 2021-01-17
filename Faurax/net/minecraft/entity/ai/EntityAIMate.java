package net.minecraft.entity.ai;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityAIMate extends EntityAIBase
{
    private EntityAnimal theAnimal;
    World theWorld;
    private EntityAnimal targetMate;

    /**
     * Delay preventing a baby from spawning immediately when two mate-able animals find each other.
     */
    int spawnBabyDelay;

    /** The speed the creature moves at during mating behavior. */
    double moveSpeed;
    private static final String __OBFID = "CL_00001578";

    public EntityAIMate(EntityAnimal p_i1619_1_, double p_i1619_2_)
    {
        this.theAnimal = p_i1619_1_;
        this.theWorld = p_i1619_1_.worldObj;
        this.moveSpeed = p_i1619_2_;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!this.theAnimal.isInLove())
        {
            return false;
        }
        else
        {
            this.targetMate = this.getNearbyMate();
            return this.targetMate != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.theAnimal.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0F, (float)this.theAnimal.getVerticalFaceSpeed());
        this.theAnimal.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
        ++this.spawnBabyDelay;

        if (this.spawnBabyDelay >= 60 && this.theAnimal.getDistanceSqToEntity(this.targetMate) < 9.0D)
        {
            this.spawnBaby();
        }
    }

    /**
     * Loops through nearby animals and finds another animal of the same type that can be mated with. Returns the first
     * valid mate found.
     */
    private EntityAnimal getNearbyMate()
    {
        float var1 = 8.0F;
        List var2 = this.theWorld.getEntitiesWithinAABB(this.theAnimal.getClass(), this.theAnimal.getEntityBoundingBox().expand((double)var1, (double)var1, (double)var1));
        double var3 = Double.MAX_VALUE;
        EntityAnimal var5 = null;
        Iterator var6 = var2.iterator();

        while (var6.hasNext())
        {
            EntityAnimal var7 = (EntityAnimal)var6.next();

            if (this.theAnimal.canMateWith(var7) && this.theAnimal.getDistanceSqToEntity(var7) < var3)
            {
                var5 = var7;
                var3 = this.theAnimal.getDistanceSqToEntity(var7);
            }
        }

        return var5;
    }

    /**
     * Spawns a baby animal of the same type.
     */
    private void spawnBaby()
    {
        EntityAgeable var1 = this.theAnimal.createChild(this.targetMate);

        if (var1 != null)
        {
            EntityPlayer var2 = this.theAnimal.func_146083_cb();

            if (var2 == null && this.targetMate.func_146083_cb() != null)
            {
                var2 = this.targetMate.func_146083_cb();
            }

            if (var2 != null)
            {
                var2.triggerAchievement(StatList.animalsBredStat);

                if (this.theAnimal instanceof EntityCow)
                {
                    var2.triggerAchievement(AchievementList.breedCow);
                }
            }

            this.theAnimal.setGrowingAge(6000);
            this.targetMate.setGrowingAge(6000);
            this.theAnimal.resetInLove();
            this.targetMate.resetInLove();
            var1.setGrowingAge(-24000);
            var1.setLocationAndAngles(this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, 0.0F, 0.0F);
            this.theWorld.spawnEntityInWorld(var1);
            Random var3 = this.theAnimal.getRNG();

            for (int var4 = 0; var4 < 7; ++var4)
            {
                double var5 = var3.nextGaussian() * 0.02D;
                double var7 = var3.nextGaussian() * 0.02D;
                double var9 = var3.nextGaussian() * 0.02D;
                this.theWorld.spawnParticle(EnumParticleTypes.HEART, this.theAnimal.posX + (double)(var3.nextFloat() * this.theAnimal.width * 2.0F) - (double)this.theAnimal.width, this.theAnimal.posY + 0.5D + (double)(var3.nextFloat() * this.theAnimal.height), this.theAnimal.posZ + (double)(var3.nextFloat() * this.theAnimal.width * 2.0F) - (double)this.theAnimal.width, var5, var7, var9, new int[0]);
            }

            if (this.theWorld.getGameRules().getGameRuleBooleanValue("doMobLoot"))
            {
                this.theWorld.spawnEntityInWorld(new EntityXPOrb(this.theWorld, this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, var3.nextInt(7) + 1));
            }
        }
    }
}
