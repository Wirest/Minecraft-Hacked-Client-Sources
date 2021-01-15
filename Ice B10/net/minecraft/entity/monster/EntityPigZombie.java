package net.minecraft.entity.monster;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityPigZombie extends EntityZombie
{
    private static final UUID field_110189_bq = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
    private static final AttributeModifier field_110190_br = (new AttributeModifier(field_110189_bq, "Attacking speed boost", 0.05D, 0)).setSaved(false);

    /** Above zero if this PigZombie is Angry. */
    private int angerLevel;

    /** A random delay until this PigZombie next makes a sound. */
    private int randomSoundDelay;
    private UUID field_175459_bn;
    private static final String __OBFID = "CL_00001693";

    public EntityPigZombie(World worldIn)
    {
        super(worldIn);
        this.isImmuneToFire = true;
    }

    public void setRevengeTarget(EntityLivingBase p_70604_1_)
    {
        super.setRevengeTarget(p_70604_1_);

        if (p_70604_1_ != null)
        {
            this.field_175459_bn = p_70604_1_.getUniqueID();
        }
    }

    protected void func_175456_n()
    {
        this.targetTasks.addTask(1, new EntityPigZombie.AIHurtByAggressor());
        this.targetTasks.addTask(2, new EntityPigZombie.AITargetAggressor());
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(field_110186_bp).setBaseValue(0.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0D);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
    }

    protected void updateAITasks()
    {
        IAttributeInstance var1 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);

        if (this.func_175457_ck())
        {
            if (!this.isChild() && !var1.func_180374_a(field_110190_br))
            {
                var1.applyModifier(field_110190_br);
            }

            --this.angerLevel;
        }
        else if (var1.func_180374_a(field_110190_br))
        {
            var1.removeModifier(field_110190_br);
        }

        if (this.randomSoundDelay > 0 && --this.randomSoundDelay == 0)
        {
            this.playSound("mob.zombiepig.zpigangry", this.getSoundVolume() * 2.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        }

        if (this.angerLevel > 0 && this.field_175459_bn != null && this.getAITarget() == null)
        {
            EntityPlayer var2 = this.worldObj.getPlayerEntityByUUID(this.field_175459_bn);
            this.setRevengeTarget(var2);
            this.attackingPlayer = var2;
            this.recentlyHit = this.getRevengeTimer();
        }

        super.updateAITasks();
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    /**
     * Whether or not the current entity is in lava
     */
    public boolean handleLavaMovement()
    {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox());
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setShort("Anger", (short)this.angerLevel);

        if (this.field_175459_bn != null)
        {
            tagCompound.setString("HurtBy", this.field_175459_bn.toString());
        }
        else
        {
            tagCompound.setString("HurtBy", "");
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.angerLevel = tagCompund.getShort("Anger");
        String var2 = tagCompund.getString("HurtBy");

        if (var2.length() > 0)
        {
            this.field_175459_bn = UUID.fromString(var2);
            EntityPlayer var3 = this.worldObj.getPlayerEntityByUUID(this.field_175459_bn);
            this.setRevengeTarget(var3);

            if (var3 != null)
            {
                this.attackingPlayer = var3;
                this.recentlyHit = this.getRevengeTimer();
            }
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.func_180431_b(source))
        {
            return false;
        }
        else
        {
            Entity var3 = source.getEntity();

            if (var3 instanceof EntityPlayer)
            {
                this.becomeAngryAt(var3);
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    /**
     * Causes this PigZombie to become angry at the supplied Entity (which will be a player).
     */
    private void becomeAngryAt(Entity p_70835_1_)
    {
        this.angerLevel = 400 + this.rand.nextInt(400);
        this.randomSoundDelay = this.rand.nextInt(40);

        if (p_70835_1_ instanceof EntityLivingBase)
        {
            this.setRevengeTarget((EntityLivingBase)p_70835_1_);
        }
    }

    public boolean func_175457_ck()
    {
        return this.angerLevel > 0;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.zombiepig.zpig";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.zombiepig.zpighurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.zombiepig.zpigdeath";
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
        int var3 = this.rand.nextInt(2 + p_70628_2_);
        int var4;

        for (var4 = 0; var4 < var3; ++var4)
        {
            this.dropItem(Items.rotten_flesh, 1);
        }

        var3 = this.rand.nextInt(2 + p_70628_2_);

        for (var4 = 0; var4 < var3; ++var4)
        {
            this.dropItem(Items.gold_nugget, 1);
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer p_70085_1_)
    {
        return false;
    }

    /**
     * Makes entity wear random armor based on difficulty
     */
    protected void addRandomArmor()
    {
        this.dropItem(Items.gold_ingot, 1);
    }

    protected void func_180481_a(DifficultyInstance p_180481_1_)
    {
        this.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
    }

    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_)
    {
        super.func_180482_a(p_180482_1_, p_180482_2_);
        this.setVillager(false);
        return p_180482_2_;
    }

    class AIHurtByAggressor extends EntityAIHurtByTarget
    {
        private static final String __OBFID = "CL_00002206";

        public AIHurtByAggressor()
        {
            super(EntityPigZombie.this, true, new Class[0]);
        }

        protected void func_179446_a(EntityCreature p_179446_1_, EntityLivingBase p_179446_2_)
        {
            super.func_179446_a(p_179446_1_, p_179446_2_);

            if (p_179446_1_ instanceof EntityPigZombie)
            {
                ((EntityPigZombie)p_179446_1_).becomeAngryAt(p_179446_2_);
            }
        }
    }

    class AITargetAggressor extends EntityAINearestAttackableTarget
    {
        private static final String __OBFID = "CL_00002207";

        public AITargetAggressor()
        {
            super(EntityPigZombie.this, EntityPlayer.class, true);
        }

        public boolean shouldExecute()
        {
            return ((EntityPigZombie)this.taskOwner).func_175457_ck() && super.shouldExecute();
        }
    }
}
