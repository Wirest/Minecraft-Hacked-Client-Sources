package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityIllusionIllager extends EntitySpellcasterIllager implements IRangedAttackMob
{
    private int field_193099_c;
    private final Vec3d[][] field_193100_bx;

    public EntityIllusionIllager(World p_i47507_1_)
    {
        super(p_i47507_1_);
        this.setSize(0.6F, 1.95F);
        this.experienceValue = 5;
        this.field_193100_bx = new Vec3d[2][4];

        for (int i = 0; i < 4; ++i)
        {
            this.field_193100_bx[0][i] = new Vec3d(0.0D, 0.0D, 0.0D);
            this.field_193100_bx[1][i] = new Vec3d(0.0D, 0.0D, 0.0D);
        }
    }

    protected void initEntityAI()
    {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntitySpellcasterIllager.AICastingApell());
        this.tasks.addTask(4, new EntityIllusionIllager.AIMirriorSpell());
        this.tasks.addTask(5, new EntityIllusionIllager.AIBlindnessSpell());
        this.tasks.addTask(6, new EntityAIAttackRangedBow(this, 0.5D, 20, 15.0F));
        this.tasks.addTask(8, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityIllusionIllager.class}));
        this.targetTasks.addTask(2, (new EntityAINearestAttackableTarget(this, EntityPlayer.class, true)).func_190882_b(300));
        this.targetTasks.addTask(3, (new EntityAINearestAttackableTarget(this, EntityVillager.class, false)).func_190882_b(300));
        this.targetTasks.addTask(3, (new EntityAINearestAttackableTarget(this, EntityIronGolem.class, false)).func_190882_b(300));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(18.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(32.0D);
    }

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
    {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        return super.onInitialSpawn(difficulty, livingdata);
    }

    protected void entityInit()
    {
        super.entityInit();
    }

    protected ResourceLocation getLootTable()
    {
        return LootTableList.EMPTY;
    }

    /**
     * Gets the bounding box of this Entity, adjusted to take auxiliary entities into account (e.g. the tile contained
     * by a minecart, such as a command block).
     */
    public AxisAlignedBB getRenderBoundingBox()
    {
        return this.getEntityBoundingBox().expand(3.0D, 0.0D, 3.0D);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.world.isRemote && this.isInvisible())
        {
            --this.field_193099_c;

            if (this.field_193099_c < 0)
            {
                this.field_193099_c = 0;
            }

            if (this.hurtTime != 1 && this.ticksExisted % 1200 != 0)
            {
                if (this.hurtTime == this.maxHurtTime - 1)
                {
                    this.field_193099_c = 3;

                    for (int k = 0; k < 4; ++k)
                    {
                        this.field_193100_bx[0][k] = this.field_193100_bx[1][k];
                        this.field_193100_bx[1][k] = new Vec3d(0.0D, 0.0D, 0.0D);
                    }
                }
            }
            else
            {
                this.field_193099_c = 3;
                float f = -6.0F;
                int i = 13;

                for (int j = 0; j < 4; ++j)
                {
                    this.field_193100_bx[0][j] = this.field_193100_bx[1][j];
                    this.field_193100_bx[1][j] = new Vec3d((double)(-6.0F + (float)this.rand.nextInt(13)) * 0.5D, (double)Math.max(0, this.rand.nextInt(6) - 4), (double)(-6.0F + (float)this.rand.nextInt(13)) * 0.5D);
                }

                for (int l = 0; l < 16; ++l)
                {
                    this.world.spawnParticle(EnumParticleTypes.CLOUD, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, 0.0D, 0.0D);
                }

                this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.field_193788_dg, this.getSoundCategory(), 1.0F, 1.0F, false);
            }
        }
    }

    public Vec3d[] func_193098_a(float p_193098_1_)
    {
        if (this.field_193099_c <= 0)
        {
            return this.field_193100_bx[1];
        }
        else
        {
            double d0 = (double)(((float)this.field_193099_c - p_193098_1_) / 3.0F);
            d0 = Math.pow(d0, 0.25D);
            Vec3d[] avec3d = new Vec3d[4];

            for (int i = 0; i < 4; ++i)
            {
                avec3d[i] = this.field_193100_bx[1][i].scale(1.0D - d0).add(this.field_193100_bx[0][i].scale(d0));
            }

            return avec3d;
        }
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isOnSameTeam(Entity entityIn)
    {
        if (super.isOnSameTeam(entityIn))
        {
            return true;
        }
        else if (entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).getCreatureAttribute() == EnumCreatureAttribute.ILLAGER)
        {
            return this.getTeam() == null && entityIn.getTeam() == null;
        }
        else
        {
            return false;
        }
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.field_193783_dc;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.field_193786_de;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.field_193787_df;
    }

    protected SoundEvent func_193086_dk()
    {
        return SoundEvents.field_193784_dd;
    }

    /**
     * Attack the specified entity using a ranged attack.
     *  
     * @param distanceFactor How far the target is, normalized and clamped between 0.1 and 1.0
     */
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
    {
        EntityArrow entityarrow = this.func_193097_t(distanceFactor);
        double d0 = target.posX - this.posX;
        double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - entityarrow.posY;
        double d2 = target.posZ - this.posZ;
        double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
        entityarrow.setThrowableHeading(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.world.getDifficulty().getDifficultyId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntityInWorld(entityarrow);
    }

    protected EntityArrow func_193097_t(float p_193097_1_)
    {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.world, this);
        entitytippedarrow.func_190547_a(this, p_193097_1_);
        return entitytippedarrow;
    }

    public boolean func_193096_dj()
    {
        return this.func_193078_a(1);
    }

    public void setSwingingArms(boolean swingingArms)
    {
        this.func_193079_a(1, swingingArms);
    }

    public AbstractIllager.IllagerArmPose func_193077_p()
    {
        if (this.func_193082_dl())
        {
            return AbstractIllager.IllagerArmPose.SPELLCASTING;
        }
        else
        {
            return this.func_193096_dj() ? AbstractIllager.IllagerArmPose.BOW_AND_ARROW : AbstractIllager.IllagerArmPose.CROSSED;
        }
    }

    class AIBlindnessSpell extends EntitySpellcasterIllager.AIUseSpell
    {
        private int field_193325_b;

        private AIBlindnessSpell()
        {
        }

        public boolean shouldExecute()
        {
            if (!super.shouldExecute())
            {
                return false;
            }
            else if (EntityIllusionIllager.this.getAttackTarget() == null)
            {
                return false;
            }
            else if (EntityIllusionIllager.this.getAttackTarget().getEntityId() == this.field_193325_b)
            {
                return false;
            }
            else
            {
                return EntityIllusionIllager.this.world.getDifficultyForLocation(new BlockPos(EntityIllusionIllager.this)).func_193845_a((float)EnumDifficulty.NORMAL.ordinal());
            }
        }

        public void startExecuting()
        {
            super.startExecuting();
            this.field_193325_b = EntityIllusionIllager.this.getAttackTarget().getEntityId();
        }

        protected int func_190869_f()
        {
            return 20;
        }

        protected int func_190872_i()
        {
            return 180;
        }

        protected void func_190868_j()
        {
            EntityIllusionIllager.this.getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 400));
        }

        protected SoundEvent func_190871_k()
        {
            return SoundEvents.field_193789_dh;
        }

        protected EntitySpellcasterIllager.SpellType func_193320_l()
        {
            return EntitySpellcasterIllager.SpellType.BLINDNESS;
        }
    }

    class AIMirriorSpell extends EntitySpellcasterIllager.AIUseSpell
    {
        private AIMirriorSpell()
        {
        }

        public boolean shouldExecute()
        {
            if (!super.shouldExecute())
            {
                return false;
            }
            else
            {
                return !EntityIllusionIllager.this.isPotionActive(MobEffects.INVISIBILITY);
            }
        }

        protected int func_190869_f()
        {
            return 20;
        }

        protected int func_190872_i()
        {
            return 340;
        }

        protected void func_190868_j()
        {
            EntityIllusionIllager.this.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 1200));
        }

        @Nullable
        protected SoundEvent func_190871_k()
        {
            return SoundEvents.field_193790_di;
        }

        protected EntitySpellcasterIllager.SpellType func_193320_l()
        {
            return EntitySpellcasterIllager.SpellType.DISAPPEAR;
        }
    }
}
