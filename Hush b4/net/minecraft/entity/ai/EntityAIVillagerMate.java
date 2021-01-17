// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIVillagerMate extends EntityAIBase
{
    private EntityVillager villagerObj;
    private EntityVillager mate;
    private World worldObj;
    private int matingTimeout;
    Village villageObj;
    
    public EntityAIVillagerMate(final EntityVillager villagerIn) {
        this.villagerObj = villagerIn;
        this.worldObj = villagerIn.worldObj;
        this.setMutexBits(3);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.villagerObj.getGrowingAge() != 0) {
            return false;
        }
        if (this.villagerObj.getRNG().nextInt(500) != 0) {
            return false;
        }
        this.villageObj = this.worldObj.getVillageCollection().getNearestVillage(new BlockPos(this.villagerObj), 0);
        if (this.villageObj == null) {
            return false;
        }
        if (!this.checkSufficientDoorsPresentForNewVillager() || !this.villagerObj.getIsWillingToMate(true)) {
            return false;
        }
        final Entity entity = this.worldObj.findNearestEntityWithinAABB(EntityVillager.class, this.villagerObj.getEntityBoundingBox().expand(8.0, 3.0, 8.0), this.villagerObj);
        if (entity == null) {
            return false;
        }
        this.mate = (EntityVillager)entity;
        return this.mate.getGrowingAge() == 0 && this.mate.getIsWillingToMate(true);
    }
    
    @Override
    public void startExecuting() {
        this.matingTimeout = 300;
        this.villagerObj.setMating(true);
    }
    
    @Override
    public void resetTask() {
        this.villageObj = null;
        this.mate = null;
        this.villagerObj.setMating(false);
    }
    
    @Override
    public boolean continueExecuting() {
        return this.matingTimeout >= 0 && this.checkSufficientDoorsPresentForNewVillager() && this.villagerObj.getGrowingAge() == 0 && this.villagerObj.getIsWillingToMate(false);
    }
    
    @Override
    public void updateTask() {
        --this.matingTimeout;
        this.villagerObj.getLookHelper().setLookPositionWithEntity(this.mate, 10.0f, 30.0f);
        if (this.villagerObj.getDistanceSqToEntity(this.mate) > 2.25) {
            this.villagerObj.getNavigator().tryMoveToEntityLiving(this.mate, 0.25);
        }
        else if (this.matingTimeout == 0 && this.mate.isMating()) {
            this.giveBirth();
        }
        if (this.villagerObj.getRNG().nextInt(35) == 0) {
            this.worldObj.setEntityState(this.villagerObj, (byte)12);
        }
    }
    
    private boolean checkSufficientDoorsPresentForNewVillager() {
        if (!this.villageObj.isMatingSeason()) {
            return false;
        }
        final int i = (int)((float)this.villageObj.getNumVillageDoors() * 0.35);
        return this.villageObj.getNumVillagers() < i;
    }
    
    private void giveBirth() {
        final EntityVillager entityvillager = this.villagerObj.createChild(this.mate);
        this.mate.setGrowingAge(6000);
        this.villagerObj.setGrowingAge(6000);
        this.mate.setIsWillingToMate(false);
        this.villagerObj.setIsWillingToMate(false);
        entityvillager.setGrowingAge(-24000);
        entityvillager.setLocationAndAngles(this.villagerObj.posX, this.villagerObj.posY, this.villagerObj.posZ, 0.0f, 0.0f);
        this.worldObj.spawnEntityInWorld(entityvillager);
        this.worldObj.setEntityState(entityvillager, (byte)12);
    }
}
