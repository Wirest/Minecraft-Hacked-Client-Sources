// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.StatCollector;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.SharedMonsterAttributes;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIOcelotAttack;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.EntityCreature;
import net.minecraft.init.Items;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIAvoidEntity;

public class EntityOcelot extends EntityTameable
{
    private EntityAIAvoidEntity<EntityPlayer> avoidEntity;
    private EntityAITempt aiTempt;
    
    public EntityOcelot(final World worldIn) {
        super(worldIn);
        this.setSize(0.6f, 0.7f);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, this.aiTempt = new EntityAITempt(this, 0.6, Items.fish, true));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0, 10.0f, 5.0f));
        this.tasks.addTask(6, new EntityAIOcelotSit(this, 0.8));
        this.tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3f));
        this.tasks.addTask(8, new EntityAIOcelotAttack(this));
        this.tasks.addTask(9, new EntityAIMate(this, 0.8));
        this.tasks.addTask(10, new EntityAIWander(this, 0.8));
        this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0f));
        this.targetTasks.addTask(1, new EntityAITargetNonTamed<Object>(this, EntityChicken.class, false, null));
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(18, (Byte)0);
    }
    
    public void updateAITasks() {
        if (this.getMoveHelper().isUpdating()) {
            final double d0 = this.getMoveHelper().getSpeed();
            if (d0 == 0.6) {
                this.setSneaking(true);
                this.setSprinting(false);
            }
            else if (d0 == 1.33) {
                this.setSneaking(false);
                this.setSprinting(true);
            }
            else {
                this.setSneaking(false);
                this.setSprinting(false);
            }
        }
        else {
            this.setSneaking(false);
            this.setSprinting(false);
        }
    }
    
    @Override
    protected boolean canDespawn() {
        return !this.isTamed() && this.ticksExisted > 2400;
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896);
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("CatType", this.getTameSkin());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setTameSkin(tagCompund.getInteger("CatType"));
    }
    
    @Override
    protected String getLivingSound() {
        return this.isTamed() ? (this.isInLove() ? "mob.cat.purr" : ((this.rand.nextInt(4) == 0) ? "mob.cat.purreow" : "mob.cat.meow")) : "";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.cat.hitt";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.cat.hitt";
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }
    
    @Override
    protected Item getDropItem() {
        return Items.leather;
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0f);
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        this.aiSit.setSitting(false);
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
    }
    
    @Override
    public boolean interact(final EntityPlayer player) {
        final ItemStack itemstack = player.inventory.getCurrentItem();
        if (this.isTamed()) {
            if (this.isOwner(player) && !this.worldObj.isRemote && !this.isBreedingItem(itemstack)) {
                this.aiSit.setSitting(!this.isSitting());
            }
        }
        else if (this.aiTempt.isRunning() && itemstack != null && itemstack.getItem() == Items.fish && player.getDistanceSqToEntity(this) < 9.0) {
            if (!player.capabilities.isCreativeMode) {
                final ItemStack itemStack = itemstack;
                --itemStack.stackSize;
            }
            if (itemstack.stackSize <= 0) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }
            if (!this.worldObj.isRemote) {
                if (this.rand.nextInt(3) == 0) {
                    this.setTamed(true);
                    this.setTameSkin(1 + this.worldObj.rand.nextInt(3));
                    this.setOwnerId(player.getUniqueID().toString());
                    this.playTameEffect(true);
                    this.aiSit.setSitting(true);
                    this.worldObj.setEntityState(this, (byte)7);
                }
                else {
                    this.playTameEffect(false);
                    this.worldObj.setEntityState(this, (byte)6);
                }
            }
            return true;
        }
        return super.interact(player);
    }
    
    @Override
    public EntityOcelot createChild(final EntityAgeable ageable) {
        final EntityOcelot entityocelot = new EntityOcelot(this.worldObj);
        if (this.isTamed()) {
            entityocelot.setOwnerId(this.getOwnerId());
            entityocelot.setTamed(true);
            entityocelot.setTameSkin(this.getTameSkin());
        }
        return entityocelot;
    }
    
    @Override
    public boolean isBreedingItem(final ItemStack stack) {
        return stack != null && stack.getItem() == Items.fish;
    }
    
    @Override
    public boolean canMateWith(final EntityAnimal otherAnimal) {
        if (otherAnimal == this) {
            return false;
        }
        if (!this.isTamed()) {
            return false;
        }
        if (!(otherAnimal instanceof EntityOcelot)) {
            return false;
        }
        final EntityOcelot entityocelot = (EntityOcelot)otherAnimal;
        return entityocelot.isTamed() && (this.isInLove() && entityocelot.isInLove());
    }
    
    public int getTameSkin() {
        return this.dataWatcher.getWatchableObjectByte(18);
    }
    
    public void setTameSkin(final int skinId) {
        this.dataWatcher.updateObject(18, (byte)skinId);
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return this.worldObj.rand.nextInt(3) != 0;
    }
    
    @Override
    public boolean isNotColliding() {
        if (this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox())) {
            final BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
            if (blockpos.getY() < this.worldObj.func_181545_F()) {
                return false;
            }
            final Block block = this.worldObj.getBlockState(blockpos.down()).getBlock();
            if (block == Blocks.grass || block.getMaterial() == Material.leaves) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.getCustomNameTag() : (this.isTamed() ? StatCollector.translateToLocal("entity.Cat.name") : super.getName());
    }
    
    @Override
    public void setTamed(final boolean tamed) {
        super.setTamed(tamed);
    }
    
    @Override
    protected void setupTamedAI() {
        if (this.avoidEntity == null) {
            this.avoidEntity = new EntityAIAvoidEntity<EntityPlayer>(this, EntityPlayer.class, 16.0f, 0.8, 1.33);
        }
        this.tasks.removeTask(this.avoidEntity);
        if (!this.isTamed()) {
            this.tasks.addTask(4, this.avoidEntity);
        }
    }
    
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        if (this.worldObj.rand.nextInt(7) == 0) {
            for (int i = 0; i < 2; ++i) {
                final EntityOcelot entityocelot = new EntityOcelot(this.worldObj);
                entityocelot.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0f);
                entityocelot.setGrowingAge(-24000);
                this.worldObj.spawnEntityInWorld(entityocelot);
            }
        }
        return livingdata;
    }
}
