// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.stats.StatList;
import net.minecraft.entity.EntityAgeable;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.entity.passive.EntityAnimal;

public class EntityAIMate extends EntityAIBase
{
    private EntityAnimal theAnimal;
    World theWorld;
    private EntityAnimal targetMate;
    int spawnBabyDelay;
    double moveSpeed;
    
    public EntityAIMate(final EntityAnimal animal, final double speedIn) {
        this.theAnimal = animal;
        this.theWorld = animal.worldObj;
        this.moveSpeed = speedIn;
        this.setMutexBits(3);
    }
    
    @Override
    public boolean shouldExecute() {
        if (!this.theAnimal.isInLove()) {
            return false;
        }
        this.targetMate = this.getNearbyMate();
        return this.targetMate != null;
    }
    
    @Override
    public boolean continueExecuting() {
        return this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
    }
    
    @Override
    public void resetTask() {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }
    
    @Override
    public void updateTask() {
        this.theAnimal.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0f, (float)this.theAnimal.getVerticalFaceSpeed());
        this.theAnimal.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
        ++this.spawnBabyDelay;
        if (this.spawnBabyDelay >= 60 && this.theAnimal.getDistanceSqToEntity(this.targetMate) < 9.0) {
            this.spawnBaby();
        }
    }
    
    private EntityAnimal getNearbyMate() {
        final float f = 8.0f;
        final List<EntityAnimal> list = this.theWorld.getEntitiesWithinAABB(this.theAnimal.getClass(), this.theAnimal.getEntityBoundingBox().expand(f, f, f));
        double d0 = Double.MAX_VALUE;
        EntityAnimal entityanimal = null;
        for (final EntityAnimal entityanimal2 : list) {
            if (this.theAnimal.canMateWith(entityanimal2) && this.theAnimal.getDistanceSqToEntity(entityanimal2) < d0) {
                entityanimal = entityanimal2;
                d0 = this.theAnimal.getDistanceSqToEntity(entityanimal2);
            }
        }
        return entityanimal;
    }
    
    private void spawnBaby() {
        final EntityAgeable entityageable = this.theAnimal.createChild(this.targetMate);
        if (entityageable != null) {
            EntityPlayer entityplayer = this.theAnimal.getPlayerInLove();
            if (entityplayer == null && this.targetMate.getPlayerInLove() != null) {
                entityplayer = this.targetMate.getPlayerInLove();
            }
            if (entityplayer != null) {
                entityplayer.triggerAchievement(StatList.animalsBredStat);
                if (this.theAnimal instanceof EntityCow) {
                    entityplayer.triggerAchievement(AchievementList.breedCow);
                }
            }
            this.theAnimal.setGrowingAge(6000);
            this.targetMate.setGrowingAge(6000);
            this.theAnimal.resetInLove();
            this.targetMate.resetInLove();
            entityageable.setGrowingAge(-24000);
            entityageable.setLocationAndAngles(this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, 0.0f, 0.0f);
            this.theWorld.spawnEntityInWorld(entityageable);
            final Random random = this.theAnimal.getRNG();
            for (int i = 0; i < 7; ++i) {
                final double d0 = random.nextGaussian() * 0.02;
                final double d2 = random.nextGaussian() * 0.02;
                final double d3 = random.nextGaussian() * 0.02;
                final double d4 = random.nextDouble() * this.theAnimal.width * 2.0 - this.theAnimal.width;
                final double d5 = 0.5 + random.nextDouble() * this.theAnimal.height;
                final double d6 = random.nextDouble() * this.theAnimal.width * 2.0 - this.theAnimal.width;
                this.theWorld.spawnParticle(EnumParticleTypes.HEART, this.theAnimal.posX + d4, this.theAnimal.posY + d5, this.theAnimal.posZ + d6, d0, d2, d3, new int[0]);
            }
            if (this.theWorld.getGameRules().getBoolean("doMobLoot")) {
                this.theWorld.spawnEntityInWorld(new EntityXPOrb(this.theWorld, this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, random.nextInt(7) + 1));
            }
        }
    }
}
