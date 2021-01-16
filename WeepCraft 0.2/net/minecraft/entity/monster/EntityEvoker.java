package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityEvoker extends EntitySpellcasterIllager
{
    private EntitySheep field_190763_bw;

    public EntityEvoker(World p_i47287_1_)
    {
        super(p_i47287_1_);
        this.setSize(0.6F, 1.95F);
        this.experienceValue = 10;
    }

    protected void initEntityAI()
    {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityEvoker.AICastingSpell());
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 8.0F, 0.6D, 1.0D));
        this.tasks.addTask(4, new EntityEvoker.AISummonSpell());
        this.tasks.addTask(5, new EntityEvoker.AIAttackSpell());
        this.tasks.addTask(6, new EntityEvoker.AIWololoSpell());
        this.tasks.addTask(8, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityEvoker.class}));
        this.targetTasks.addTask(2, (new EntityAINearestAttackableTarget(this, EntityPlayer.class, true)).func_190882_b(300));
        this.targetTasks.addTask(3, (new EntityAINearestAttackableTarget(this, EntityVillager.class, false)).func_190882_b(300));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, false));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(12.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0D);
    }

    protected void entityInit()
    {
        super.entityInit();
    }

    public static void func_190759_b(DataFixer p_190759_0_)
    {
        EntityLiving.registerFixesMob(p_190759_0_, EntityEvoker.class);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
    }

    protected ResourceLocation getLootTable()
    {
        return LootTableList.field_191185_au;
    }

    protected void updateAITasks()
    {
        super.updateAITasks();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isOnSameTeam(Entity entityIn)
    {
        if (entityIn == null)
        {
            return false;
        }
        else if (entityIn == this)
        {
            return true;
        }
        else if (super.isOnSameTeam(entityIn))
        {
            return true;
        }
        else if (entityIn instanceof EntityVex)
        {
            return this.isOnSameTeam(((EntityVex)entityIn).func_190645_o());
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
        return SoundEvents.field_191243_bm;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.field_191245_bo;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.field_191246_bp;
    }

    private void func_190748_a(@Nullable EntitySheep p_190748_1_)
    {
        this.field_190763_bw = p_190748_1_;
    }

    @Nullable
    private EntitySheep func_190751_dj()
    {
        return this.field_190763_bw;
    }

    protected SoundEvent func_193086_dk()
    {
        return SoundEvents.field_191244_bn;
    }

    class AIAttackSpell extends EntitySpellcasterIllager.AIUseSpell
    {
        private AIAttackSpell()
        {
        }

        protected int func_190869_f()
        {
            return 40;
        }

        protected int func_190872_i()
        {
            return 100;
        }

        protected void func_190868_j()
        {
            EntityLivingBase entitylivingbase = EntityEvoker.this.getAttackTarget();
            double d0 = Math.min(entitylivingbase.posY, EntityEvoker.this.posY);
            double d1 = Math.max(entitylivingbase.posY, EntityEvoker.this.posY) + 1.0D;
            float f = (float)MathHelper.atan2(entitylivingbase.posZ - EntityEvoker.this.posZ, entitylivingbase.posX - EntityEvoker.this.posX);

            if (EntityEvoker.this.getDistanceSqToEntity(entitylivingbase) < 9.0D)
            {
                for (int i = 0; i < 5; ++i)
                {
                    float f1 = f + (float)i * (float)Math.PI * 0.4F;
                    this.func_190876_a(EntityEvoker.this.posX + (double)MathHelper.cos(f1) * 1.5D, EntityEvoker.this.posZ + (double)MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
                }

                for (int k = 0; k < 8; ++k)
                {
                    float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + ((float)Math.PI * 2F / 5F);
                    this.func_190876_a(EntityEvoker.this.posX + (double)MathHelper.cos(f2) * 2.5D, EntityEvoker.this.posZ + (double)MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
                }
            }
            else
            {
                for (int l = 0; l < 16; ++l)
                {
                    double d2 = 1.25D * (double)(l + 1);
                    int j = 1 * l;
                    this.func_190876_a(EntityEvoker.this.posX + (double)MathHelper.cos(f) * d2, EntityEvoker.this.posZ + (double)MathHelper.sin(f) * d2, d0, d1, f, j);
                }
            }
        }

        private void func_190876_a(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_)
        {
            BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
            boolean flag = false;
            double d0 = 0.0D;

            while (true)
            {
                if (!EntityEvoker.this.world.isBlockNormalCube(blockpos, true) && EntityEvoker.this.world.isBlockNormalCube(blockpos.down(), true))
                {
                    if (!EntityEvoker.this.world.isAirBlock(blockpos))
                    {
                        IBlockState iblockstate = EntityEvoker.this.world.getBlockState(blockpos);
                        AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(EntityEvoker.this.world, blockpos);

                        if (axisalignedbb != null)
                        {
                            d0 = axisalignedbb.maxY;
                        }
                    }

                    flag = true;
                    break;
                }

                blockpos = blockpos.down();

                if (blockpos.getY() < MathHelper.floor(p_190876_5_) - 1)
                {
                    break;
                }
            }

            if (flag)
            {
                EntityEvokerFangs entityevokerfangs = new EntityEvokerFangs(EntityEvoker.this.world, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, EntityEvoker.this);
                EntityEvoker.this.world.spawnEntityInWorld(entityevokerfangs);
            }
        }

        protected SoundEvent func_190871_k()
        {
            return SoundEvents.field_191247_bq;
        }

        protected EntitySpellcasterIllager.SpellType func_193320_l()
        {
            return EntitySpellcasterIllager.SpellType.FANGS;
        }
    }

    class AICastingSpell extends EntitySpellcasterIllager.AICastingApell
    {
        private AICastingSpell()
        {
        }

        public void updateTask()
        {
            if (EntityEvoker.this.getAttackTarget() != null)
            {
                EntityEvoker.this.getLookHelper().setLookPositionWithEntity(EntityEvoker.this.getAttackTarget(), (float)EntityEvoker.this.getHorizontalFaceSpeed(), (float)EntityEvoker.this.getVerticalFaceSpeed());
            }
            else if (EntityEvoker.this.func_190751_dj() != null)
            {
                EntityEvoker.this.getLookHelper().setLookPositionWithEntity(EntityEvoker.this.func_190751_dj(), (float)EntityEvoker.this.getHorizontalFaceSpeed(), (float)EntityEvoker.this.getVerticalFaceSpeed());
            }
        }
    }

    class AISummonSpell extends EntitySpellcasterIllager.AIUseSpell
    {
        private AISummonSpell()
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
                int i = EntityEvoker.this.world.getEntitiesWithinAABB(EntityVex.class, EntityEvoker.this.getEntityBoundingBox().expandXyz(16.0D)).size();
                return EntityEvoker.this.rand.nextInt(8) + 1 > i;
            }
        }

        protected int func_190869_f()
        {
            return 100;
        }

        protected int func_190872_i()
        {
            return 340;
        }

        protected void func_190868_j()
        {
            for (int i = 0; i < 3; ++i)
            {
                BlockPos blockpos = (new BlockPos(EntityEvoker.this)).add(-2 + EntityEvoker.this.rand.nextInt(5), 1, -2 + EntityEvoker.this.rand.nextInt(5));
                EntityVex entityvex = new EntityVex(EntityEvoker.this.world);
                entityvex.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
                entityvex.onInitialSpawn(EntityEvoker.this.world.getDifficultyForLocation(blockpos), (IEntityLivingData)null);
                entityvex.func_190658_a(EntityEvoker.this);
                entityvex.func_190651_g(blockpos);
                entityvex.func_190653_a(20 * (30 + EntityEvoker.this.rand.nextInt(90)));
                EntityEvoker.this.world.spawnEntityInWorld(entityvex);
            }
        }

        protected SoundEvent func_190871_k()
        {
            return SoundEvents.field_191248_br;
        }

        protected EntitySpellcasterIllager.SpellType func_193320_l()
        {
            return EntitySpellcasterIllager.SpellType.SUMMON_VEX;
        }
    }

    public class AIWololoSpell extends EntitySpellcasterIllager.AIUseSpell
    {
        final Predicate<EntitySheep> field_190879_a = new Predicate<EntitySheep>()
        {
            public boolean apply(EntitySheep p_apply_1_)
            {
                return p_apply_1_.getFleeceColor() == EnumDyeColor.BLUE;
            }
        };

        public AIWololoSpell()
        {
        }

        public boolean shouldExecute()
        {
            if (EntityEvoker.this.getAttackTarget() != null)
            {
                return false;
            }
            else if (EntityEvoker.this.func_193082_dl())
            {
                return false;
            }
            else if (EntityEvoker.this.ticksExisted < this.field_193322_d)
            {
                return false;
            }
            else if (!EntityEvoker.this.world.getGameRules().getBoolean("mobGriefing"))
            {
                return false;
            }
            else
            {
                List<EntitySheep> list = EntityEvoker.this.world.<EntitySheep>getEntitiesWithinAABB(EntitySheep.class, EntityEvoker.this.getEntityBoundingBox().expand(16.0D, 4.0D, 16.0D), this.field_190879_a);

                if (list.isEmpty())
                {
                    return false;
                }
                else
                {
                    EntityEvoker.this.func_190748_a(list.get(EntityEvoker.this.rand.nextInt(list.size())));
                    return true;
                }
            }
        }

        public boolean continueExecuting()
        {
            return EntityEvoker.this.func_190751_dj() != null && this.field_193321_c > 0;
        }

        public void resetTask()
        {
            super.resetTask();
            EntityEvoker.this.func_190748_a((EntitySheep)null);
        }

        protected void func_190868_j()
        {
            EntitySheep entitysheep = EntityEvoker.this.func_190751_dj();

            if (entitysheep != null && entitysheep.isEntityAlive())
            {
                entitysheep.setFleeceColor(EnumDyeColor.RED);
            }
        }

        protected int func_190867_m()
        {
            return 40;
        }

        protected int func_190869_f()
        {
            return 60;
        }

        protected int func_190872_i()
        {
            return 140;
        }

        protected SoundEvent func_190871_k()
        {
            return SoundEvents.field_191249_bs;
        }

        protected EntitySpellcasterIllager.SpellType func_193320_l()
        {
            return EntitySpellcasterIllager.SpellType.WOLOLO;
        }
    }
}
