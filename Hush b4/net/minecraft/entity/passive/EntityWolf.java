// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemFood;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIBeg;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;

public class EntityWolf extends EntityTameable
{
    private float headRotationCourse;
    private float headRotationCourseOld;
    private boolean isWet;
    private boolean isShaking;
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;
    
    public EntityWolf(final World worldIn) {
        super(worldIn);
        this.setSize(0.6f, 0.8f);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4f));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0, true));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0, 10.0f, 2.0f));
        this.tasks.addTask(6, new EntityAIMate(this, 1.0));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0));
        this.tasks.addTask(8, new EntityAIBeg(this, 8.0f));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(4, new EntityAITargetNonTamed<Object>(this, EntityAnimal.class, false, new Predicate<Entity>() {
            @Override
            public boolean apply(final Entity p_apply_1_) {
                return p_apply_1_ instanceof EntitySheep || p_apply_1_ instanceof EntityRabbit;
            }
        }));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<Object>(this, EntitySkeleton.class, false));
        this.setTamed(false);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896);
        if (this.isTamed()) {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0);
        }
        else {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0);
        }
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0);
    }
    
    @Override
    public void setAttackTarget(final EntityLivingBase entitylivingbaseIn) {
        super.setAttackTarget(entitylivingbaseIn);
        if (entitylivingbaseIn == null) {
            this.setAngry(false);
        }
        else if (!this.isTamed()) {
            this.setAngry(true);
        }
    }
    
    @Override
    protected void updateAITasks() {
        this.dataWatcher.updateObject(18, this.getHealth());
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(18, new Float(this.getHealth()));
        this.dataWatcher.addObject(19, new Byte((byte)0));
        this.dataWatcher.addObject(20, new Byte((byte)EnumDyeColor.RED.getMetadata()));
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound("mob.wolf.step", 0.15f, 1.0f);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("Angry", this.isAngry());
        tagCompound.setByte("CollarColor", (byte)this.getCollarColor().getDyeDamage());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setAngry(tagCompund.getBoolean("Angry"));
        if (tagCompund.hasKey("CollarColor", 99)) {
            this.setCollarColor(EnumDyeColor.byDyeDamage(tagCompund.getByte("CollarColor")));
        }
    }
    
    @Override
    protected String getLivingSound() {
        return this.isAngry() ? "mob.wolf.growl" : ((this.rand.nextInt(3) == 0) ? ((this.isTamed() && this.dataWatcher.getWatchableObjectFloat(18) < 10.0f) ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.wolf.hurt";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.wolf.death";
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }
    
    @Override
    protected Item getDropItem() {
        return Item.getItemById(-1);
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.worldObj.isRemote && this.isWet && !this.isShaking && !this.hasPath() && this.onGround) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0f;
            this.prevTimeWolfIsShaking = 0.0f;
            this.worldObj.setEntityState(this, (byte)8);
        }
        if (!this.worldObj.isRemote && this.getAttackTarget() == null && this.isAngry()) {
            this.setAngry(false);
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.headRotationCourseOld = this.headRotationCourse;
        if (this.isBegging()) {
            this.headRotationCourse += (1.0f - this.headRotationCourse) * 0.4f;
        }
        else {
            this.headRotationCourse += (0.0f - this.headRotationCourse) * 0.4f;
        }
        if (this.isWet()) {
            this.isWet = true;
            this.isShaking = false;
            this.timeWolfIsShaking = 0.0f;
            this.prevTimeWolfIsShaking = 0.0f;
        }
        else if ((this.isWet || this.isShaking) && this.isShaking) {
            if (this.timeWolfIsShaking == 0.0f) {
                this.playSound("mob.wolf.shake", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            }
            this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
            this.timeWolfIsShaking += 0.05f;
            if (this.prevTimeWolfIsShaking >= 2.0f) {
                this.isWet = false;
                this.isShaking = false;
                this.prevTimeWolfIsShaking = 0.0f;
                this.timeWolfIsShaking = 0.0f;
            }
            if (this.timeWolfIsShaking > 0.4f) {
                final float f = (float)this.getEntityBoundingBox().minY;
                for (int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4f) * 3.1415927f) * 7.0f), j = 0; j < i; ++j) {
                    final float f2 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width * 0.5f;
                    final float f3 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width * 0.5f;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + f2, f + 0.8f, this.posZ + f3, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
            }
        }
    }
    
    public boolean isWolfWet() {
        return this.isWet;
    }
    
    public float getShadingWhileWet(final float p_70915_1_) {
        return 0.75f + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70915_1_) / 2.0f * 0.25f;
    }
    
    public float getShakeAngle(final float p_70923_1_, final float p_70923_2_) {
        float f = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70923_1_ + p_70923_2_) / 1.8f;
        if (f < 0.0f) {
            f = 0.0f;
        }
        else if (f > 1.0f) {
            f = 1.0f;
        }
        return MathHelper.sin(f * 3.1415927f) * MathHelper.sin(f * 3.1415927f * 11.0f) * 0.15f * 3.1415927f;
    }
    
    public float getInterestedAngle(final float p_70917_1_) {
        return (this.headRotationCourseOld + (this.headRotationCourse - this.headRotationCourseOld) * p_70917_1_) * 0.15f * 3.1415927f;
    }
    
    @Override
    public float getEyeHeight() {
        return this.height * 0.8f;
    }
    
    @Override
    public int getVerticalFaceSpeed() {
        return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        final Entity entity = source.getEntity();
        this.aiSit.setSitting(false);
        if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow)) {
            amount = (amount + 1.0f) / 2.0f;
        }
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        final boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(int)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
        if (flag) {
            this.applyEnchantments(this, entityIn);
        }
        return flag;
    }
    
    @Override
    public void setTamed(final boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0);
        }
        else {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0);
        }
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0);
    }
    
    @Override
    public boolean interact(final EntityPlayer player) {
        final ItemStack itemstack = player.inventory.getCurrentItem();
        if (this.isTamed()) {
            if (itemstack != null) {
                if (itemstack.getItem() instanceof ItemFood) {
                    final ItemFood itemfood = (ItemFood)itemstack.getItem();
                    if (itemfood.isWolfsFavoriteMeat() && this.dataWatcher.getWatchableObjectFloat(18) < 20.0f) {
                        if (!player.capabilities.isCreativeMode) {
                            final ItemStack itemStack = itemstack;
                            --itemStack.stackSize;
                        }
                        this.heal((float)itemfood.getHealAmount(itemstack));
                        if (itemstack.stackSize <= 0) {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                        }
                        return true;
                    }
                }
                else if (itemstack.getItem() == Items.dye) {
                    final EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(itemstack.getMetadata());
                    if (enumdyecolor != this.getCollarColor()) {
                        this.setCollarColor(enumdyecolor);
                        if (!player.capabilities.isCreativeMode) {
                            final ItemStack itemStack2 = itemstack;
                            if (--itemStack2.stackSize <= 0) {
                                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                            }
                        }
                        return true;
                    }
                }
            }
            if (this.isOwner(player) && !this.worldObj.isRemote && !this.isBreedingItem(itemstack)) {
                this.aiSit.setSitting(!this.isSitting());
                this.isJumping = false;
                this.navigator.clearPathEntity();
                this.setAttackTarget(null);
            }
        }
        else if (itemstack != null && itemstack.getItem() == Items.bone && !this.isAngry()) {
            if (!player.capabilities.isCreativeMode) {
                final ItemStack itemStack3 = itemstack;
                --itemStack3.stackSize;
            }
            if (itemstack.stackSize <= 0) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }
            if (!this.worldObj.isRemote) {
                if (this.rand.nextInt(3) == 0) {
                    this.setTamed(true);
                    this.navigator.clearPathEntity();
                    this.setAttackTarget(null);
                    this.aiSit.setSitting(true);
                    this.setHealth(20.0f);
                    this.setOwnerId(player.getUniqueID().toString());
                    this.playTameEffect(true);
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
    public void handleStatusUpdate(final byte id) {
        if (id == 8) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0f;
            this.prevTimeWolfIsShaking = 0.0f;
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    public float getTailRotation() {
        return this.isAngry() ? 1.5393804f : (this.isTamed() ? ((0.55f - (20.0f - this.dataWatcher.getWatchableObjectFloat(18)) * 0.02f) * 3.1415927f) : 0.62831855f);
    }
    
    @Override
    public boolean isBreedingItem(final ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemFood && ((ItemFood)stack.getItem()).isWolfsFavoriteMeat();
    }
    
    @Override
    public int getMaxSpawnedInChunk() {
        return 8;
    }
    
    public boolean isAngry() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x2) != 0x0;
    }
    
    public void setAngry(final boolean angry) {
        final byte b0 = this.dataWatcher.getWatchableObjectByte(16);
        if (angry) {
            this.dataWatcher.updateObject(16, (byte)(b0 | 0x2));
        }
        else {
            this.dataWatcher.updateObject(16, (byte)(b0 & 0xFFFFFFFD));
        }
    }
    
    public EnumDyeColor getCollarColor() {
        return EnumDyeColor.byDyeDamage(this.dataWatcher.getWatchableObjectByte(20) & 0xF);
    }
    
    public void setCollarColor(final EnumDyeColor collarcolor) {
        this.dataWatcher.updateObject(20, (byte)(collarcolor.getDyeDamage() & 0xF));
    }
    
    @Override
    public EntityWolf createChild(final EntityAgeable ageable) {
        final EntityWolf entitywolf = new EntityWolf(this.worldObj);
        final String s = this.getOwnerId();
        if (s != null && s.trim().length() > 0) {
            entitywolf.setOwnerId(s);
            entitywolf.setTamed(true);
        }
        return entitywolf;
    }
    
    public void setBegging(final boolean beg) {
        if (beg) {
            this.dataWatcher.updateObject(19, (Byte)1);
        }
        else {
            this.dataWatcher.updateObject(19, (Byte)0);
        }
    }
    
    @Override
    public boolean canMateWith(final EntityAnimal otherAnimal) {
        if (otherAnimal == this) {
            return false;
        }
        if (!this.isTamed()) {
            return false;
        }
        if (!(otherAnimal instanceof EntityWolf)) {
            return false;
        }
        final EntityWolf entitywolf = (EntityWolf)otherAnimal;
        return entitywolf.isTamed() && !entitywolf.isSitting() && (this.isInLove() && entitywolf.isInLove());
    }
    
    public boolean isBegging() {
        return this.dataWatcher.getWatchableObjectByte(19) == 1;
    }
    
    @Override
    protected boolean canDespawn() {
        return !this.isTamed() && this.ticksExisted > 2400;
    }
    
    @Override
    public boolean shouldAttackEntity(final EntityLivingBase p_142018_1_, final EntityLivingBase p_142018_2_) {
        if (!(p_142018_1_ instanceof EntityCreeper) && !(p_142018_1_ instanceof EntityGhast)) {
            if (p_142018_1_ instanceof EntityWolf) {
                final EntityWolf entitywolf = (EntityWolf)p_142018_1_;
                if (entitywolf.isTamed() && entitywolf.getOwner() == p_142018_2_) {
                    return false;
                }
            }
            return (!(p_142018_1_ instanceof EntityPlayer) || !(p_142018_2_ instanceof EntityPlayer) || ((EntityPlayer)p_142018_2_).canAttackPlayer((EntityPlayer)p_142018_1_)) && (!(p_142018_1_ instanceof EntityHorse) || !((EntityHorse)p_142018_1_).isTame());
        }
        return false;
    }
    
    @Override
    public boolean allowLeashing() {
        return !this.isAngry() && super.allowLeashing();
    }
}
