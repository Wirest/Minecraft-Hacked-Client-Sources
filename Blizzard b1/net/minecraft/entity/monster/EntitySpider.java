package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntitySpider extends EntityMob
{
    private static final String __OBFID = "CL_00001699";

    public EntitySpider(World worldIn)
    {
        super(worldIn);
        this.setSize(1.4F, 0.9F);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.field_175455_a);
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntitySpider.AISpiderAttack(EntityPlayer.class));
        this.tasks.addTask(4, new EntitySpider.AISpiderAttack(EntityIronGolem.class));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntitySpider.AISpiderTarget(EntityPlayer.class));
        this.targetTasks.addTask(3, new EntitySpider.AISpiderTarget(EntityIronGolem.class));
    }

    protected PathNavigate func_175447_b(World worldIn)
    {
        return new PathNavigateClimber(this, worldIn);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (!this.worldObj.isRemote)
        {
            this.setBesideClimbableBlock(this.isCollidedHorizontally);
        }
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.spider.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.spider.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.spider.death";
    }

    protected void func_180429_a(BlockPos p_180429_1_, Block p_180429_2_)
    {
        this.playSound("mob.spider.step", 0.15F, 1.0F);
    }

    protected Item getDropItem()
    {
        return Items.string;
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
        super.dropFewItems(p_70628_1_, p_70628_2_);

        if (p_70628_1_ && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + p_70628_2_) > 0))
        {
            this.dropItem(Items.spider_eye, 1);
        }
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    public boolean isOnLadder()
    {
        return this.isBesideClimbableBlock();
    }

    /**
     * Sets the Entity inside a web block.
     */
    public void setInWeb() {}

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    public boolean isPotionApplicable(PotionEffect p_70687_1_)
    {
        return p_70687_1_.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(p_70687_1_);
    }

    /**
     * Returns true if the WatchableObject (Byte) is 0x01 otherwise returns false. The WatchableObject is updated using
     * setBesideClimableBlock.
     */
    public boolean isBesideClimbableBlock()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    /**
     * Updates the WatchableObject (Byte) created in entityInit(), setting it to 0x01 if par1 is true or 0x00 if it is
     * false.
     */
    public void setBesideClimbableBlock(boolean p_70839_1_)
    {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);

        if (p_70839_1_)
        {
            var2 = (byte)(var2 | 1);
        }
        else
        {
            var2 &= -2;
        }

        this.dataWatcher.updateObject(16, Byte.valueOf(var2));
    }

    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_)
    {
        Object p_180482_2_1 = super.func_180482_a(p_180482_1_, p_180482_2_);

        if (this.worldObj.rand.nextInt(100) == 0)
        {
            EntitySkeleton var3 = new EntitySkeleton(this.worldObj);
            var3.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            var3.func_180482_a(p_180482_1_, (IEntityLivingData)null);
            this.worldObj.spawnEntityInWorld(var3);
            var3.mountEntity(this);
        }

        if (p_180482_2_1 == null)
        {
            p_180482_2_1 = new EntitySpider.GroupData();

            if (this.worldObj.getDifficulty() == EnumDifficulty.HARD && this.worldObj.rand.nextFloat() < 0.1F * p_180482_1_.func_180170_c())
            {
                ((EntitySpider.GroupData)p_180482_2_1).func_111104_a(this.worldObj.rand);
            }
        }

        if (p_180482_2_1 instanceof EntitySpider.GroupData)
        {
            int var5 = ((EntitySpider.GroupData)p_180482_2_1).field_111105_a;

            if (var5 > 0 && Potion.potionTypes[var5] != null)
            {
                this.addPotionEffect(new PotionEffect(var5, Integer.MAX_VALUE));
            }
        }

        return (IEntityLivingData)p_180482_2_1;
    }

    public float getEyeHeight()
    {
        return 0.65F;
    }

    class AISpiderAttack extends EntityAIAttackOnCollide
    {
        private static final String __OBFID = "CL_00002197";

        public AISpiderAttack(Class p_i45819_2_)
        {
            super(EntitySpider.this, p_i45819_2_, 1.0D, true);
        }

        public boolean continueExecuting()
        {
            float var1 = this.attacker.getBrightness(1.0F);

            if (var1 >= 0.5F && this.attacker.getRNG().nextInt(100) == 0)
            {
                this.attacker.setAttackTarget((EntityLivingBase)null);
                return false;
            }
            else
            {
                return super.continueExecuting();
            }
        }

        protected double func_179512_a(EntityLivingBase p_179512_1_)
        {
            return (double)(4.0F + p_179512_1_.width);
        }
    }

    class AISpiderTarget extends EntityAINearestAttackableTarget
    {
        private static final String __OBFID = "CL_00002196";

        public AISpiderTarget(Class p_i45818_2_)
        {
            super(EntitySpider.this, p_i45818_2_, true);
        }

        public boolean shouldExecute()
        {
            float var1 = this.taskOwner.getBrightness(1.0F);
            return var1 >= 0.5F ? false : super.shouldExecute();
        }
    }

    public static class GroupData implements IEntityLivingData
    {
        public int field_111105_a;
        private static final String __OBFID = "CL_00001700";

        public void func_111104_a(Random p_111104_1_)
        {
            int var2 = p_111104_1_.nextInt(5);

            if (var2 <= 1)
            {
                this.field_111105_a = Potion.moveSpeed.id;
            }
            else if (var2 <= 2)
            {
                this.field_111105_a = Potion.damageBoost.id;
            }
            else if (var2 <= 3)
            {
                this.field_111105_a = Potion.regeneration.id;
            }
            else if (var2 <= 4)
            {
                this.field_111105_a = Potion.invisibility.id;
            }
        }
    }
}
