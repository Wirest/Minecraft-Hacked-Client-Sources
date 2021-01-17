// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.world.World;

public class EntitySpider extends EntityMob
{
    public EntitySpider(final World worldIn) {
        super(worldIn);
        this.setSize(1.4f, 0.9f);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4f));
        this.tasks.addTask(4, new AISpiderAttack(this, EntityPlayer.class));
        this.tasks.addTask(4, new AISpiderAttack(this, EntityIronGolem.class));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new AISpiderTarget<Object>(this, EntityPlayer.class));
        this.targetTasks.addTask(3, new AISpiderTarget<Object>(this, EntityIronGolem.class));
    }
    
    @Override
    public double getMountedYOffset() {
        return this.height * 0.5f;
    }
    
    @Override
    protected PathNavigate getNewNavigator(final World worldIn) {
        return new PathNavigateClimber(this, worldIn);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote) {
            this.setBesideClimbableBlock(this.isCollidedHorizontally);
        }
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896);
    }
    
    @Override
    protected String getLivingSound() {
        return "mob.spider.say";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.spider.say";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.spider.death";
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound("mob.spider.step", 0.15f, 1.0f);
    }
    
    @Override
    protected Item getDropItem() {
        return Items.string;
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        super.dropFewItems(p_70628_1_, p_70628_2_);
        if (p_70628_1_ && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + p_70628_2_) > 0)) {
            this.dropItem(Items.spider_eye, 1);
        }
    }
    
    @Override
    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
    }
    
    @Override
    public void setInWeb() {
    }
    
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }
    
    @Override
    public boolean isPotionApplicable(final PotionEffect potioneffectIn) {
        return potioneffectIn.getPotionID() != Potion.poison.id && super.isPotionApplicable(potioneffectIn);
    }
    
    public boolean isBesideClimbableBlock() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0x0;
    }
    
    public void setBesideClimbableBlock(final boolean p_70839_1_) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);
        if (p_70839_1_) {
            b0 |= 0x1;
        }
        else {
            b0 &= 0xFFFFFFFE;
        }
        this.dataWatcher.updateObject(16, b0);
    }
    
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        if (this.worldObj.rand.nextInt(100) == 0) {
            final EntitySkeleton entityskeleton = new EntitySkeleton(this.worldObj);
            entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0f);
            entityskeleton.onInitialSpawn(difficulty, null);
            this.worldObj.spawnEntityInWorld(entityskeleton);
            entityskeleton.mountEntity(this);
        }
        if (livingdata == null) {
            livingdata = new GroupData();
            if (this.worldObj.getDifficulty() == EnumDifficulty.HARD && this.worldObj.rand.nextFloat() < 0.1f * difficulty.getClampedAdditionalDifficulty()) {
                ((GroupData)livingdata).func_111104_a(this.worldObj.rand);
            }
        }
        if (livingdata instanceof GroupData) {
            final int i = ((GroupData)livingdata).potionEffectId;
            if (i > 0 && Potion.potionTypes[i] != null) {
                this.addPotionEffect(new PotionEffect(i, Integer.MAX_VALUE));
            }
        }
        return livingdata;
    }
    
    @Override
    public float getEyeHeight() {
        return 0.65f;
    }
    
    static class AISpiderAttack extends EntityAIAttackOnCollide
    {
        public AISpiderAttack(final EntitySpider p_i45819_1_, final Class<? extends Entity> targetClass) {
            super(p_i45819_1_, targetClass, 1.0, true);
        }
        
        @Override
        public boolean continueExecuting() {
            final float f = this.attacker.getBrightness(1.0f);
            if (f >= 0.5f && this.attacker.getRNG().nextInt(100) == 0) {
                this.attacker.setAttackTarget(null);
                return false;
            }
            return super.continueExecuting();
        }
        
        @Override
        protected double func_179512_a(final EntityLivingBase attackTarget) {
            return 4.0f + attackTarget.width;
        }
    }
    
    static class AISpiderTarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget
    {
        public AISpiderTarget(final EntitySpider p_i45818_1_, final Class<T> classTarget) {
            super(p_i45818_1_, classTarget, true);
        }
        
        @Override
        public boolean shouldExecute() {
            final float f = this.taskOwner.getBrightness(1.0f);
            return f < 0.5f && super.shouldExecute();
        }
    }
    
    public static class GroupData implements IEntityLivingData
    {
        public int potionEffectId;
        
        public void func_111104_a(final Random rand) {
            final int i = rand.nextInt(5);
            if (i <= 1) {
                this.potionEffectId = Potion.moveSpeed.id;
            }
            else if (i <= 2) {
                this.potionEffectId = Potion.damageBoost.id;
            }
            else if (i <= 3) {
                this.potionEffectId = Potion.regeneration.id;
            }
            else if (i <= 4) {
                this.potionEffectId = Potion.invisibility.id;
            }
        }
    }
}
