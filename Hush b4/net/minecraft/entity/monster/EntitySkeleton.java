// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import java.util.Calendar;
import net.minecraft.init.Blocks;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.IRangedAttackMob;

public class EntitySkeleton extends EntityMob implements IRangedAttackMob
{
    private EntityAIArrowAttack aiArrowAttack;
    private EntityAIAttackOnCollide aiAttackOnCollide;
    
    public EntitySkeleton(final World worldIn) {
        super(worldIn);
        this.aiArrowAttack = new EntityAIArrowAttack(this, 1.0, 20, 60, 15.0f);
        this.aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2, false);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIRestrictSun(this));
        this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0));
        this.tasks.addTask(3, new EntityAIAvoidEntity<Object>(this, EntityWolf.class, 6.0f, 1.0, 1.2));
        this.tasks.addTask(4, new EntityAIWander(this, 1.0));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Object>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<Object>(this, EntityIronGolem.class, true));
        if (worldIn != null && !worldIn.isRemote) {
            this.setCombatTask();
        }
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(13, new Byte((byte)0));
    }
    
    @Override
    protected String getLivingSound() {
        return "mob.skeleton.say";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.skeleton.hurt";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.skeleton.death";
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound("mob.skeleton.step", 0.15f, 1.0f);
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        if (super.attackEntityAsMob(entityIn)) {
            if (this.getSkeletonType() == 1 && entityIn instanceof EntityLivingBase) {
                ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(Potion.wither.id, 200));
            }
            return true;
        }
        return false;
    }
    
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.worldObj.isDaytime() && !this.worldObj.isRemote) {
            final float f = this.getBrightness(1.0f);
            final BlockPos blockpos = new BlockPos(this.posX, (double)Math.round(this.posY), this.posZ);
            if (f > 0.5f && this.rand.nextFloat() * 30.0f < (f - 0.4f) * 2.0f && this.worldObj.canSeeSky(blockpos)) {
                boolean flag = true;
                final ItemStack itemstack = this.getEquipmentInSlot(4);
                if (itemstack != null) {
                    if (itemstack.isItemStackDamageable()) {
                        itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));
                        if (itemstack.getItemDamage() >= itemstack.getMaxDamage()) {
                            this.renderBrokenItemStack(itemstack);
                            this.setCurrentItemOrArmor(4, null);
                        }
                    }
                    flag = false;
                }
                if (flag) {
                    this.setFire(8);
                }
            }
        }
        if (this.worldObj.isRemote && this.getSkeletonType() == 1) {
            this.setSize(0.72f, 2.535f);
        }
        super.onLivingUpdate();
    }
    
    @Override
    public void updateRidden() {
        super.updateRidden();
        if (this.ridingEntity instanceof EntityCreature) {
            final EntityCreature entitycreature = (EntityCreature)this.ridingEntity;
            this.renderYawOffset = entitycreature.renderYawOffset;
        }
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        super.onDeath(cause);
        if (cause.getSourceOfDamage() instanceof EntityArrow && cause.getEntity() instanceof EntityPlayer) {
            final EntityPlayer entityplayer = (EntityPlayer)cause.getEntity();
            final double d0 = entityplayer.posX - this.posX;
            final double d2 = entityplayer.posZ - this.posZ;
            if (d0 * d0 + d2 * d2 >= 2500.0) {
                entityplayer.triggerAchievement(AchievementList.snipeSkeleton);
            }
        }
        else if (cause.getEntity() instanceof EntityCreeper && ((EntityCreeper)cause.getEntity()).getPowered() && ((EntityCreeper)cause.getEntity()).isAIEnabled()) {
            ((EntityCreeper)cause.getEntity()).func_175493_co();
            this.entityDropItem(new ItemStack(Items.skull, 1, (int)((this.getSkeletonType() == 1) ? 1 : 0)), 0.0f);
        }
    }
    
    @Override
    protected Item getDropItem() {
        return Items.arrow;
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        if (this.getSkeletonType() == 1) {
            for (int i = this.rand.nextInt(3 + p_70628_2_) - 1, j = 0; j < i; ++j) {
                this.dropItem(Items.coal, 1);
            }
        }
        else {
            for (int k = this.rand.nextInt(3 + p_70628_2_), i2 = 0; i2 < k; ++i2) {
                this.dropItem(Items.arrow, 1);
            }
        }
        for (int l = this.rand.nextInt(3 + p_70628_2_), j2 = 0; j2 < l; ++j2) {
            this.dropItem(Items.bone, 1);
        }
    }
    
    @Override
    protected void addRandomDrop() {
        if (this.getSkeletonType() == 1) {
            this.entityDropItem(new ItemStack(Items.skull, 1, 1), 0.0f);
        }
    }
    
    @Override
    protected void setEquipmentBasedOnDifficulty(final DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        this.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
    }
    
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        if (this.worldObj.provider instanceof WorldProviderHell && this.getRNG().nextInt(5) > 0) {
            this.tasks.addTask(4, this.aiAttackOnCollide);
            this.setSkeletonType(1);
            this.setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
            this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0);
        }
        else {
            this.tasks.addTask(4, this.aiArrowAttack);
            this.setEquipmentBasedOnDifficulty(difficulty);
            this.setEnchantmentBasedOnDifficulty(difficulty);
        }
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55f * difficulty.getClampedAdditionalDifficulty());
        if (this.getEquipmentInSlot(4) == null) {
            final Calendar calendar = this.worldObj.getCurrentDate();
            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25f) {
                this.setCurrentItemOrArmor(4, new ItemStack((this.rand.nextFloat() < 0.1f) ? Blocks.lit_pumpkin : Blocks.pumpkin));
                this.equipmentDropChances[4] = 0.0f;
            }
        }
        return livingdata;
    }
    
    public void setCombatTask() {
        this.tasks.removeTask(this.aiAttackOnCollide);
        this.tasks.removeTask(this.aiArrowAttack);
        final ItemStack itemstack = this.getHeldItem();
        if (itemstack != null && itemstack.getItem() == Items.bow) {
            this.tasks.addTask(4, this.aiArrowAttack);
        }
        else {
            this.tasks.addTask(4, this.aiAttackOnCollide);
        }
    }
    
    @Override
    public void attackEntityWithRangedAttack(final EntityLivingBase p_82196_1_, final float p_82196_2_) {
        final EntityArrow entityarrow = new EntityArrow(this.worldObj, this, p_82196_1_, 1.6f, (float)(14 - this.worldObj.getDifficulty().getDifficultyId() * 4));
        final int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
        final int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
        entityarrow.setDamage(p_82196_2_ * 2.0f + this.rand.nextGaussian() * 0.25 + this.worldObj.getDifficulty().getDifficultyId() * 0.11f);
        if (i > 0) {
            entityarrow.setDamage(entityarrow.getDamage() + i * 0.5 + 0.5);
        }
        if (j > 0) {
            entityarrow.setKnockbackStrength(j);
        }
        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0 || this.getSkeletonType() == 1) {
            entityarrow.setFire(100);
        }
        this.playSound("random.bow", 1.0f, 1.0f / (this.getRNG().nextFloat() * 0.4f + 0.8f));
        this.worldObj.spawnEntityInWorld(entityarrow);
    }
    
    public int getSkeletonType() {
        return this.dataWatcher.getWatchableObjectByte(13);
    }
    
    public void setSkeletonType(final int p_82201_1_) {
        this.dataWatcher.updateObject(13, (byte)p_82201_1_);
        this.isImmuneToFire = (p_82201_1_ == 1);
        if (p_82201_1_ == 1) {
            this.setSize(0.72f, 2.535f);
        }
        else {
            this.setSize(0.6f, 1.95f);
        }
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("SkeletonType", 99)) {
            final int i = tagCompund.getByte("SkeletonType");
            this.setSkeletonType(i);
        }
        this.setCombatTask();
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setByte("SkeletonType", (byte)this.getSkeletonType());
    }
    
    @Override
    public void setCurrentItemOrArmor(final int slotIn, final ItemStack stack) {
        super.setCurrentItemOrArmor(slotIn, stack);
        if (!this.worldObj.isRemote && slotIn == 0) {
            this.setCombatTask();
        }
    }
    
    @Override
    public float getEyeHeight() {
        return (this.getSkeletonType() == 1) ? super.getEyeHeight() : 1.74f;
    }
    
    @Override
    public double getYOffset() {
        return this.isChild() ? 0.0 : -0.35;
    }
}
