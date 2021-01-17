// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.init.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.Entity;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.UUID;

public class EntityPigZombie extends EntityZombie
{
    private static final UUID ATTACK_SPEED_BOOST_MODIFIER_UUID;
    private static final AttributeModifier ATTACK_SPEED_BOOST_MODIFIER;
    private int angerLevel;
    private int randomSoundDelay;
    private UUID angerTargetUUID;
    
    static {
        ATTACK_SPEED_BOOST_MODIFIER_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
        ATTACK_SPEED_BOOST_MODIFIER = new AttributeModifier(EntityPigZombie.ATTACK_SPEED_BOOST_MODIFIER_UUID, "Attacking speed boost", 0.05, 0).setSaved(false);
    }
    
    public EntityPigZombie(final World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
    }
    
    @Override
    public void setRevengeTarget(final EntityLivingBase livingBase) {
        super.setRevengeTarget(livingBase);
        if (livingBase != null) {
            this.angerTargetUUID = livingBase.getUniqueID();
        }
    }
    
    @Override
    protected void applyEntityAI() {
        this.targetTasks.addTask(1, new AIHurtByAggressor(this));
        this.targetTasks.addTask(2, new AITargetAggressor(this));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(EntityPigZombie.reinforcementChance).setBaseValue(0.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0);
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
    }
    
    @Override
    protected void updateAITasks() {
        final IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        if (this.isAngry()) {
            if (!this.isChild() && !iattributeinstance.hasModifier(EntityPigZombie.ATTACK_SPEED_BOOST_MODIFIER)) {
                iattributeinstance.applyModifier(EntityPigZombie.ATTACK_SPEED_BOOST_MODIFIER);
            }
            --this.angerLevel;
        }
        else if (iattributeinstance.hasModifier(EntityPigZombie.ATTACK_SPEED_BOOST_MODIFIER)) {
            iattributeinstance.removeModifier(EntityPigZombie.ATTACK_SPEED_BOOST_MODIFIER);
        }
        if (this.randomSoundDelay > 0 && --this.randomSoundDelay == 0) {
            this.playSound("mob.zombiepig.zpigangry", this.getSoundVolume() * 2.0f, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f) * 1.8f);
        }
        if (this.angerLevel > 0 && this.angerTargetUUID != null && this.getAITarget() == null) {
            final EntityPlayer entityplayer = this.worldObj.getPlayerEntityByUUID(this.angerTargetUUID);
            this.setRevengeTarget(entityplayer);
            this.attackingPlayer = entityplayer;
            this.recentlyHit = this.getRevengeTimer();
        }
        super.updateAITasks();
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
    }
    
    @Override
    public boolean isNotColliding() {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox());
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setShort("Anger", (short)this.angerLevel);
        if (this.angerTargetUUID != null) {
            tagCompound.setString("HurtBy", this.angerTargetUUID.toString());
        }
        else {
            tagCompound.setString("HurtBy", "");
        }
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.angerLevel = tagCompund.getShort("Anger");
        final String s = tagCompund.getString("HurtBy");
        if (s.length() > 0) {
            this.angerTargetUUID = UUID.fromString(s);
            final EntityPlayer entityplayer = this.worldObj.getPlayerEntityByUUID(this.angerTargetUUID);
            this.setRevengeTarget(entityplayer);
            if (entityplayer != null) {
                this.attackingPlayer = entityplayer;
                this.recentlyHit = this.getRevengeTimer();
            }
        }
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        final Entity entity = source.getEntity();
        if (entity instanceof EntityPlayer) {
            this.becomeAngryAt(entity);
        }
        return super.attackEntityFrom(source, amount);
    }
    
    private void becomeAngryAt(final Entity p_70835_1_) {
        this.angerLevel = 400 + this.rand.nextInt(400);
        this.randomSoundDelay = this.rand.nextInt(40);
        if (p_70835_1_ instanceof EntityLivingBase) {
            this.setRevengeTarget((EntityLivingBase)p_70835_1_);
        }
    }
    
    public boolean isAngry() {
        return this.angerLevel > 0;
    }
    
    @Override
    protected String getLivingSound() {
        return "mob.zombiepig.zpig";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.zombiepig.zpighurt";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.zombiepig.zpigdeath";
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        for (int i = this.rand.nextInt(2 + p_70628_2_), j = 0; j < i; ++j) {
            this.dropItem(Items.rotten_flesh, 1);
        }
        for (int i = this.rand.nextInt(2 + p_70628_2_), k = 0; k < i; ++k) {
            this.dropItem(Items.gold_nugget, 1);
        }
    }
    
    @Override
    public boolean interact(final EntityPlayer player) {
        return false;
    }
    
    @Override
    protected void addRandomDrop() {
        this.dropItem(Items.gold_ingot, 1);
    }
    
    @Override
    protected void setEquipmentBasedOnDifficulty(final DifficultyInstance difficulty) {
        this.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
    }
    
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, final IEntityLivingData livingdata) {
        super.onInitialSpawn(difficulty, livingdata);
        this.setVillager(false);
        return livingdata;
    }
    
    static class AIHurtByAggressor extends EntityAIHurtByTarget
    {
        public AIHurtByAggressor(final EntityPigZombie p_i45828_1_) {
            super(p_i45828_1_, true, new Class[0]);
        }
        
        @Override
        protected void setEntityAttackTarget(final EntityCreature creatureIn, final EntityLivingBase entityLivingBaseIn) {
            super.setEntityAttackTarget(creatureIn, entityLivingBaseIn);
            if (creatureIn instanceof EntityPigZombie) {
                ((EntityPigZombie)creatureIn).becomeAngryAt(entityLivingBaseIn);
            }
        }
    }
    
    static class AITargetAggressor extends EntityAINearestAttackableTarget<EntityPlayer>
    {
        public AITargetAggressor(final EntityPigZombie p_i45829_1_) {
            super(p_i45829_1_, EntityPlayer.class, true);
        }
        
        @Override
        public boolean shouldExecute() {
            return ((EntityPigZombie)this.taskOwner).isAngry() && super.shouldExecute();
        }
    }
}
