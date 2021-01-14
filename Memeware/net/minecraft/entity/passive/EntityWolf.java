package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBeg;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityWolf extends EntityTameable {
    /**
     * Float used to smooth the rotation of the wolf head
     */
    private float headRotationCourse;
    private float headRotationCourseOld;

    /**
     * true is the wolf is wet else false
     */
    private boolean isWet;

    /**
     * True if the wolf is shaking else False
     */
    private boolean isShaking;

    /**
     * This time increases while wolf is shaking and emitting water particles.
     */
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;
    private static final String __OBFID = "CL_00001654";

    public EntityWolf(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 0.8F);
        ((PathNavigateGround) this.getNavigator()).func_179690_a(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, true));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(6, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIBeg(this, 8.0F));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntityAnimal.class, false, new Predicate() {
            private static final String __OBFID = "CL_00002229";

            public boolean func_180094_a(Entity p_180094_1_) {
                return p_180094_1_ instanceof EntitySheep || p_180094_1_ instanceof EntityRabbit;
            }

            public boolean apply(Object p_apply_1_) {
                return this.func_180094_a((Entity) p_apply_1_);
            }
        }));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntitySkeleton.class, false));
        this.setTamed(false);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);

        if (this.isTamed()) {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
        }

        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0D);
    }

    /**
     * Sets the active target the Task system uses for tracking
     */
    public void setAttackTarget(EntityLivingBase p_70624_1_) {
        super.setAttackTarget(p_70624_1_);

        if (p_70624_1_ == null) {
            this.setAngry(false);
        } else if (!this.isTamed()) {
            this.setAngry(true);
        }
    }

    protected void updateAITasks() {
        this.dataWatcher.updateObject(18, Float.valueOf(this.getHealth()));
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(18, new Float(this.getHealth()));
        this.dataWatcher.addObject(19, new Byte((byte) 0));
        this.dataWatcher.addObject(20, new Byte((byte) EnumDyeColor.RED.func_176765_a()));
    }

    protected void func_180429_a(BlockPos p_180429_1_, Block p_180429_2_) {
        this.playSound("mob.wolf.step", 0.15F, 1.0F);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("Angry", this.isAngry());
        tagCompound.setByte("CollarColor", (byte) this.func_175546_cu().getDyeColorDamage());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setAngry(tagCompund.getBoolean("Angry"));

        if (tagCompund.hasKey("CollarColor", 99)) {
            this.func_175547_a(EnumDyeColor.func_176766_a(tagCompund.getByte("CollarColor")));
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound() {
        return this.isAngry() ? "mob.wolf.growl" : (this.rand.nextInt(3) == 0 ? (this.isTamed() && this.dataWatcher.getWatchableObjectFloat(18) < 10.0F ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound() {
        return "mob.wolf.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound() {
        return "mob.wolf.death";
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume() {
        return 0.4F;
    }

    protected Item getDropItem() {
        return Item.getItemById(-1);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!this.worldObj.isRemote && this.isWet && !this.isShaking && !this.hasPath() && this.onGround) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
            this.worldObj.setEntityState(this, (byte) 8);
        }

        if (!this.worldObj.isRemote && this.getAttackTarget() == null && this.isAngry()) {
            this.setAngry(false);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        super.onUpdate();
        this.headRotationCourseOld = this.headRotationCourse;

        if (this.func_70922_bv()) {
            this.headRotationCourse += (1.0F - this.headRotationCourse) * 0.4F;
        } else {
            this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;
        }

        if (this.isWet()) {
            this.isWet = true;
            this.isShaking = false;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        } else if ((this.isWet || this.isShaking) && this.isShaking) {
            if (this.timeWolfIsShaking == 0.0F) {
                this.playSound("mob.wolf.shake", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }

            this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
            this.timeWolfIsShaking += 0.05F;

            if (this.prevTimeWolfIsShaking >= 2.0F) {
                this.isWet = false;
                this.isShaking = false;
                this.prevTimeWolfIsShaking = 0.0F;
                this.timeWolfIsShaking = 0.0F;
            }

            if (this.timeWolfIsShaking > 0.4F) {
                float var1 = (float) this.getEntityBoundingBox().minY;
                int var2 = (int) (MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float) Math.PI) * 7.0F);

                for (int var3 = 0; var3 < var2; ++var3) {
                    float var4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float var5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + (double) var4, (double) (var1 + 0.8F), this.posZ + (double) var5, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
            }
        }
    }

    /**
     * True if the wolf is wet
     */
    public boolean isWolfWet() {
        return this.isWet;
    }

    /**
     * Used when calculating the amount of shading to apply while the wolf is wet.
     */
    public float getShadingWhileWet(float p_70915_1_) {
        return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70915_1_) / 2.0F * 0.25F;
    }

    public float getShakeAngle(float p_70923_1_, float p_70923_2_) {
        float var3 = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70923_1_ + p_70923_2_) / 1.8F;

        if (var3 < 0.0F) {
            var3 = 0.0F;
        } else if (var3 > 1.0F) {
            var3 = 1.0F;
        }

        return MathHelper.sin(var3 * (float) Math.PI) * MathHelper.sin(var3 * (float) Math.PI * 11.0F) * 0.15F * (float) Math.PI;
    }

    public float getInterestedAngle(float p_70917_1_) {
        return (this.headRotationCourseOld + (this.headRotationCourse - this.headRotationCourseOld) * p_70917_1_) * 0.15F * (float) Math.PI;
    }

    public float getEyeHeight() {
        return this.height * 0.8F;
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed() {
        return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.func_180431_b(source)) {
            return false;
        } else {
            Entity var3 = source.getEntity();
            this.aiSit.setSitting(false);

            if (var3 != null && !(var3 instanceof EntityPlayer) && !(var3 instanceof EntityArrow)) {
                amount = (amount + 1.0F) / 2.0F;
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    public boolean attackEntityAsMob(Entity p_70652_1_) {
        boolean var2 = p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), (float) ((int) this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue()));

        if (var2) {
            this.func_174815_a(this, p_70652_1_);
        }

        return var2;
    }

    public void setTamed(boolean p_70903_1_) {
        super.setTamed(p_70903_1_);

        if (p_70903_1_) {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
        }

        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer p_70085_1_) {
        ItemStack var2 = p_70085_1_.inventory.getCurrentItem();

        if (this.isTamed()) {
            if (var2 != null) {
                if (var2.getItem() instanceof ItemFood) {
                    ItemFood var3 = (ItemFood) var2.getItem();

                    if (var3.isWolfsFavoriteMeat() && this.dataWatcher.getWatchableObjectFloat(18) < 20.0F) {
                        if (!p_70085_1_.capabilities.isCreativeMode) {
                            --var2.stackSize;
                        }

                        this.heal((float) var3.getHealAmount(var2));

                        if (var2.stackSize <= 0) {
                            p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem, (ItemStack) null);
                        }

                        return true;
                    }
                } else if (var2.getItem() == Items.dye) {
                    EnumDyeColor var4 = EnumDyeColor.func_176766_a(var2.getMetadata());

                    if (var4 != this.func_175546_cu()) {
                        this.func_175547_a(var4);

                        if (!p_70085_1_.capabilities.isCreativeMode && --var2.stackSize <= 0) {
                            p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem, (ItemStack) null);
                        }

                        return true;
                    }
                }
            }

            if (this.func_152114_e(p_70085_1_) && !this.worldObj.isRemote && !this.isBreedingItem(var2)) {
                this.aiSit.setSitting(!this.isSitting());
                this.isJumping = false;
                this.navigator.clearPathEntity();
                this.setAttackTarget((EntityLivingBase) null);
            }
        } else if (var2 != null && var2.getItem() == Items.bone && !this.isAngry()) {
            if (!p_70085_1_.capabilities.isCreativeMode) {
                --var2.stackSize;
            }

            if (var2.stackSize <= 0) {
                p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem, (ItemStack) null);
            }

            if (!this.worldObj.isRemote) {
                if (this.rand.nextInt(3) == 0) {
                    this.setTamed(true);
                    this.navigator.clearPathEntity();
                    this.setAttackTarget((EntityLivingBase) null);
                    this.aiSit.setSitting(true);
                    this.setHealth(20.0F);
                    this.func_152115_b(p_70085_1_.getUniqueID().toString());
                    this.playTameEffect(true);
                    this.worldObj.setEntityState(this, (byte) 7);
                } else {
                    this.playTameEffect(false);
                    this.worldObj.setEntityState(this, (byte) 6);
                }
            }

            return true;
        }

        return super.interact(p_70085_1_);
    }

    public void handleHealthUpdate(byte p_70103_1_) {
        if (p_70103_1_ == 8) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        } else {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

    public float getTailRotation() {
        return this.isAngry() ? 1.5393804F : (this.isTamed() ? (0.55F - (20.0F - this.dataWatcher.getWatchableObjectFloat(18)) * 0.02F) * (float) Math.PI : ((float) Math.PI / 5F));
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack p_70877_1_) {
        return p_70877_1_ == null ? false : (!(p_70877_1_.getItem() instanceof ItemFood) ? false : ((ItemFood) p_70877_1_.getItem()).isWolfsFavoriteMeat());
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk() {
        return 8;
    }

    /**
     * Determines whether this wolf is angry or not.
     */
    public boolean isAngry() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
    }

    /**
     * Sets whether this wolf is angry or not.
     */
    public void setAngry(boolean p_70916_1_) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);

        if (p_70916_1_) {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 | 2)));
        } else {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 & -3)));
        }
    }

    public EnumDyeColor func_175546_cu() {
        return EnumDyeColor.func_176766_a(this.dataWatcher.getWatchableObjectByte(20) & 15);
    }

    public void func_175547_a(EnumDyeColor p_175547_1_) {
        this.dataWatcher.updateObject(20, Byte.valueOf((byte) (p_175547_1_.getDyeColorDamage() & 15)));
    }

    public EntityWolf createChild(EntityAgeable p_90011_1_) {
        EntityWolf var2 = new EntityWolf(this.worldObj);
        String var3 = this.func_152113_b();

        if (var3 != null && var3.trim().length() > 0) {
            var2.func_152115_b(var3);
            var2.setTamed(true);
        }

        return var2;
    }

    public void func_70918_i(boolean p_70918_1_) {
        if (p_70918_1_) {
            this.dataWatcher.updateObject(19, Byte.valueOf((byte) 1));
        } else {
            this.dataWatcher.updateObject(19, Byte.valueOf((byte) 0));
        }
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    public boolean canMateWith(EntityAnimal p_70878_1_) {
        if (p_70878_1_ == this) {
            return false;
        } else if (!this.isTamed()) {
            return false;
        } else if (!(p_70878_1_ instanceof EntityWolf)) {
            return false;
        } else {
            EntityWolf var2 = (EntityWolf) p_70878_1_;
            return !var2.isTamed() ? false : (var2.isSitting() ? false : this.isInLove() && var2.isInLove());
        }
    }

    public boolean func_70922_bv() {
        return this.dataWatcher.getWatchableObjectByte(19) == 1;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn() {
        return !this.isTamed() && this.ticksExisted > 2400;
    }

    public boolean func_142018_a(EntityLivingBase p_142018_1_, EntityLivingBase p_142018_2_) {
        if (!(p_142018_1_ instanceof EntityCreeper) && !(p_142018_1_ instanceof EntityGhast)) {
            if (p_142018_1_ instanceof EntityWolf) {
                EntityWolf var3 = (EntityWolf) p_142018_1_;

                if (var3.isTamed() && var3.func_180492_cm() == p_142018_2_) {
                    return false;
                }
            }

            return p_142018_1_ instanceof EntityPlayer && p_142018_2_ instanceof EntityPlayer && !((EntityPlayer) p_142018_2_).canAttackPlayer((EntityPlayer) p_142018_1_) ? false : !(p_142018_1_ instanceof EntityHorse) || !((EntityHorse) p_142018_1_).isTame();
        } else {
            return false;
        }
    }

    public boolean allowLeashing() {
        return !this.isAngry() && super.allowLeashing();
    }


}
