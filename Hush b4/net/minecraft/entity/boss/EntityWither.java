// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.boss;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.PotionEffect;
import java.util.Iterator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import net.minecraft.init.Items;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import java.util.List;
import net.minecraft.block.material.Material;
import com.google.common.base.Predicates;
import net.minecraft.util.EntitySelectors;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import com.google.common.base.Predicate;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.monster.EntityMob;

public class EntityWither extends EntityMob implements IBossDisplayData, IRangedAttackMob
{
    private float[] field_82220_d;
    private float[] field_82221_e;
    private float[] field_82217_f;
    private float[] field_82218_g;
    private int[] field_82223_h;
    private int[] field_82224_i;
    private int blockBreakCounter;
    private static final Predicate<Entity> attackEntitySelector;
    
    static {
        attackEntitySelector = new Predicate<Entity>() {
            @Override
            public boolean apply(final Entity p_apply_1_) {
                return p_apply_1_ instanceof EntityLivingBase && ((EntityLivingBase)p_apply_1_).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD;
            }
        };
    }
    
    public EntityWither(final World worldIn) {
        super(worldIn);
        this.field_82220_d = new float[2];
        this.field_82221_e = new float[2];
        this.field_82217_f = new float[2];
        this.field_82218_g = new float[2];
        this.field_82223_h = new int[2];
        this.field_82224_i = new int[2];
        this.setHealth(this.getMaxHealth());
        this.setSize(0.9f, 3.5f);
        this.isImmuneToFire = true;
        ((PathNavigateGround)this.getNavigator()).setCanSwim(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0, 40, 20.0f));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Object>(this, EntityLiving.class, 0, false, false, EntityWither.attackEntitySelector));
        this.experienceValue = 50;
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(0));
        this.dataWatcher.addObject(19, new Integer(0));
        this.dataWatcher.addObject(20, new Integer(0));
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Invul", this.getInvulTime());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setInvulTime(tagCompund.getInteger("Invul"));
    }
    
    @Override
    protected String getLivingSound() {
        return "mob.wither.idle";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.wither.hurt";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.wither.death";
    }
    
    @Override
    public void onLivingUpdate() {
        this.motionY *= 0.6000000238418579;
        if (!this.worldObj.isRemote && this.getWatchedTargetId(0) > 0) {
            final Entity entity = this.worldObj.getEntityByID(this.getWatchedTargetId(0));
            if (entity != null) {
                if (this.posY < entity.posY || (!this.isArmored() && this.posY < entity.posY + 5.0)) {
                    if (this.motionY < 0.0) {
                        this.motionY = 0.0;
                    }
                    this.motionY += (0.5 - this.motionY) * 0.6000000238418579;
                }
                final double d0 = entity.posX - this.posX;
                final double d2 = entity.posZ - this.posZ;
                final double d3 = d0 * d0 + d2 * d2;
                if (d3 > 9.0) {
                    final double d4 = MathHelper.sqrt_double(d3);
                    this.motionX += (d0 / d4 * 0.5 - this.motionX) * 0.6000000238418579;
                    this.motionZ += (d2 / d4 * 0.5 - this.motionZ) * 0.6000000238418579;
                }
            }
        }
        if (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.05000000074505806) {
            this.rotationYaw = (float)MathHelper.func_181159_b(this.motionZ, this.motionX) * 57.295776f - 90.0f;
        }
        super.onLivingUpdate();
        for (int i = 0; i < 2; ++i) {
            this.field_82218_g[i] = this.field_82221_e[i];
            this.field_82217_f[i] = this.field_82220_d[i];
        }
        for (int j = 0; j < 2; ++j) {
            final int k = this.getWatchedTargetId(j + 1);
            Entity entity2 = null;
            if (k > 0) {
                entity2 = this.worldObj.getEntityByID(k);
            }
            if (entity2 != null) {
                final double d5 = this.func_82214_u(j + 1);
                final double d6 = this.func_82208_v(j + 1);
                final double d7 = this.func_82213_w(j + 1);
                final double d8 = entity2.posX - d5;
                final double d9 = entity2.posY + entity2.getEyeHeight() - d6;
                final double d10 = entity2.posZ - d7;
                final double d11 = MathHelper.sqrt_double(d8 * d8 + d10 * d10);
                final float f = (float)(MathHelper.func_181159_b(d10, d8) * 180.0 / 3.141592653589793) - 90.0f;
                final float f2 = (float)(-(MathHelper.func_181159_b(d9, d11) * 180.0 / 3.141592653589793));
                this.field_82220_d[j] = this.func_82204_b(this.field_82220_d[j], f2, 40.0f);
                this.field_82221_e[j] = this.func_82204_b(this.field_82221_e[j], f, 10.0f);
            }
            else {
                this.field_82221_e[j] = this.func_82204_b(this.field_82221_e[j], this.renderYawOffset, 10.0f);
            }
        }
        final boolean flag = this.isArmored();
        for (int l = 0; l < 3; ++l) {
            final double d12 = this.func_82214_u(l);
            final double d13 = this.func_82208_v(l);
            final double d14 = this.func_82213_w(l);
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d12 + this.rand.nextGaussian() * 0.30000001192092896, d13 + this.rand.nextGaussian() * 0.30000001192092896, d14 + this.rand.nextGaussian() * 0.30000001192092896, 0.0, 0.0, 0.0, new int[0]);
            if (flag && this.worldObj.rand.nextInt(4) == 0) {
                this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, d12 + this.rand.nextGaussian() * 0.30000001192092896, d13 + this.rand.nextGaussian() * 0.30000001192092896, d14 + this.rand.nextGaussian() * 0.30000001192092896, 0.699999988079071, 0.699999988079071, 0.5, new int[0]);
            }
        }
        if (this.getInvulTime() > 0) {
            for (int i2 = 0; i2 < 3; ++i2) {
                this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + this.rand.nextGaussian() * 1.0, this.posY + this.rand.nextFloat() * 3.3f, this.posZ + this.rand.nextGaussian() * 1.0, 0.699999988079071, 0.699999988079071, 0.8999999761581421, new int[0]);
            }
        }
    }
    
    @Override
    protected void updateAITasks() {
        if (this.getInvulTime() > 0) {
            final int j1 = this.getInvulTime() - 1;
            if (j1 <= 0) {
                this.worldObj.newExplosion(this, this.posX, this.posY + this.getEyeHeight(), this.posZ, 7.0f, false, this.worldObj.getGameRules().getBoolean("mobGriefing"));
                this.worldObj.playBroadcastSound(1013, new BlockPos(this), 0);
            }
            this.setInvulTime(j1);
            if (this.ticksExisted % 10 == 0) {
                this.heal(10.0f);
            }
        }
        else {
            super.updateAITasks();
            for (int i = 1; i < 3; ++i) {
                if (this.ticksExisted >= this.field_82223_h[i - 1]) {
                    this.field_82223_h[i - 1] = this.ticksExisted + 10 + this.rand.nextInt(10);
                    if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL || this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                        final int j2 = i - 1;
                        final int k3 = this.field_82224_i[i - 1];
                        this.field_82224_i[j2] = this.field_82224_i[i - 1] + 1;
                        if (k3 > 15) {
                            final float f = 10.0f;
                            final float f2 = 5.0f;
                            final double d0 = MathHelper.getRandomDoubleInRange(this.rand, this.posX - f, this.posX + f);
                            final double d2 = MathHelper.getRandomDoubleInRange(this.rand, this.posY - f2, this.posY + f2);
                            final double d3 = MathHelper.getRandomDoubleInRange(this.rand, this.posZ - f, this.posZ + f);
                            this.launchWitherSkullToCoords(i + 1, d0, d2, d3, true);
                            this.field_82224_i[i - 1] = 0;
                        }
                    }
                    final int k4 = this.getWatchedTargetId(i);
                    if (k4 > 0) {
                        final Entity entity = this.worldObj.getEntityByID(k4);
                        if (entity != null && entity.isEntityAlive() && this.getDistanceSqToEntity(entity) <= 900.0 && this.canEntityBeSeen(entity)) {
                            if (entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.disableDamage) {
                                this.updateWatchedTargetId(i, 0);
                            }
                            else {
                                this.launchWitherSkullToEntity(i + 1, (EntityLivingBase)entity);
                                this.field_82223_h[i - 1] = this.ticksExisted + 40 + this.rand.nextInt(20);
                                this.field_82224_i[i - 1] = 0;
                            }
                        }
                        else {
                            this.updateWatchedTargetId(i, 0);
                        }
                    }
                    else {
                        final List<EntityLivingBase> list = this.worldObj.getEntitiesWithinAABB((Class<? extends EntityLivingBase>)EntityLivingBase.class, this.getEntityBoundingBox().expand(20.0, 8.0, 20.0), Predicates.and((Predicate<? super EntityLivingBase>)EntityWither.attackEntitySelector, (Predicate<? super EntityLivingBase>)EntitySelectors.NOT_SPECTATING));
                        int j3 = 0;
                        while (j3 < 10 && !list.isEmpty()) {
                            final EntityLivingBase entitylivingbase = list.get(this.rand.nextInt(list.size()));
                            if (entitylivingbase != this && entitylivingbase.isEntityAlive() && this.canEntityBeSeen(entitylivingbase)) {
                                if (!(entitylivingbase instanceof EntityPlayer)) {
                                    this.updateWatchedTargetId(i, entitylivingbase.getEntityId());
                                    break;
                                }
                                if (!((EntityPlayer)entitylivingbase).capabilities.disableDamage) {
                                    this.updateWatchedTargetId(i, entitylivingbase.getEntityId());
                                    break;
                                }
                                break;
                            }
                            else {
                                list.remove(entitylivingbase);
                                ++j3;
                            }
                        }
                    }
                }
            }
            if (this.getAttackTarget() != null) {
                this.updateWatchedTargetId(0, this.getAttackTarget().getEntityId());
            }
            else {
                this.updateWatchedTargetId(0, 0);
            }
            if (this.blockBreakCounter > 0) {
                --this.blockBreakCounter;
                if (this.blockBreakCounter == 0 && this.worldObj.getGameRules().getBoolean("mobGriefing")) {
                    final int i2 = MathHelper.floor_double(this.posY);
                    final int l1 = MathHelper.floor_double(this.posX);
                    final int i3 = MathHelper.floor_double(this.posZ);
                    boolean flag = false;
                    for (int k5 = -1; k5 <= 1; ++k5) {
                        for (int l2 = -1; l2 <= 1; ++l2) {
                            for (int m = 0; m <= 3; ++m) {
                                final int i4 = l1 + k5;
                                final int k6 = i2 + m;
                                final int l3 = i3 + l2;
                                final BlockPos blockpos = new BlockPos(i4, k6, l3);
                                final Block block = this.worldObj.getBlockState(blockpos).getBlock();
                                if (block.getMaterial() != Material.air && func_181033_a(block)) {
                                    flag = (this.worldObj.destroyBlock(blockpos, true) || flag);
                                }
                            }
                        }
                    }
                    if (flag) {
                        this.worldObj.playAuxSFXAtEntity(null, 1012, new BlockPos(this), 0);
                    }
                }
            }
            if (this.ticksExisted % 20 == 0) {
                this.heal(1.0f);
            }
        }
    }
    
    public static boolean func_181033_a(final Block p_181033_0_) {
        return p_181033_0_ != Blocks.bedrock && p_181033_0_ != Blocks.end_portal && p_181033_0_ != Blocks.end_portal_frame && p_181033_0_ != Blocks.command_block && p_181033_0_ != Blocks.barrier;
    }
    
    public void func_82206_m() {
        this.setInvulTime(220);
        this.setHealth(this.getMaxHealth() / 3.0f);
    }
    
    @Override
    public void setInWeb() {
    }
    
    @Override
    public int getTotalArmorValue() {
        return 4;
    }
    
    private double func_82214_u(final int p_82214_1_) {
        if (p_82214_1_ <= 0) {
            return this.posX;
        }
        final float f = (this.renderYawOffset + 180 * (p_82214_1_ - 1)) / 180.0f * 3.1415927f;
        final float f2 = MathHelper.cos(f);
        return this.posX + f2 * 1.3;
    }
    
    private double func_82208_v(final int p_82208_1_) {
        return (p_82208_1_ <= 0) ? (this.posY + 3.0) : (this.posY + 2.2);
    }
    
    private double func_82213_w(final int p_82213_1_) {
        if (p_82213_1_ <= 0) {
            return this.posZ;
        }
        final float f = (this.renderYawOffset + 180 * (p_82213_1_ - 1)) / 180.0f * 3.1415927f;
        final float f2 = MathHelper.sin(f);
        return this.posZ + f2 * 1.3;
    }
    
    private float func_82204_b(final float p_82204_1_, final float p_82204_2_, final float p_82204_3_) {
        float f = MathHelper.wrapAngleTo180_float(p_82204_2_ - p_82204_1_);
        if (f > p_82204_3_) {
            f = p_82204_3_;
        }
        if (f < -p_82204_3_) {
            f = -p_82204_3_;
        }
        return p_82204_1_ + f;
    }
    
    private void launchWitherSkullToEntity(final int p_82216_1_, final EntityLivingBase p_82216_2_) {
        this.launchWitherSkullToCoords(p_82216_1_, p_82216_2_.posX, p_82216_2_.posY + p_82216_2_.getEyeHeight() * 0.5, p_82216_2_.posZ, p_82216_1_ == 0 && this.rand.nextFloat() < 0.001f);
    }
    
    private void launchWitherSkullToCoords(final int p_82209_1_, final double x, final double y, final double z, final boolean invulnerable) {
        this.worldObj.playAuxSFXAtEntity(null, 1014, new BlockPos(this), 0);
        final double d0 = this.func_82214_u(p_82209_1_);
        final double d2 = this.func_82208_v(p_82209_1_);
        final double d3 = this.func_82213_w(p_82209_1_);
        final double d4 = x - d0;
        final double d5 = y - d2;
        final double d6 = z - d3;
        final EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.worldObj, this, d4, d5, d6);
        if (invulnerable) {
            entitywitherskull.setInvulnerable(true);
        }
        entitywitherskull.posY = d2;
        entitywitherskull.posX = d0;
        entitywitherskull.posZ = d3;
        this.worldObj.spawnEntityInWorld(entitywitherskull);
    }
    
    @Override
    public void attackEntityWithRangedAttack(final EntityLivingBase p_82196_1_, final float p_82196_2_) {
        this.launchWitherSkullToEntity(0, p_82196_1_);
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (source == DamageSource.drown || source.getEntity() instanceof EntityWither) {
            return false;
        }
        if (this.getInvulTime() > 0 && source != DamageSource.outOfWorld) {
            return false;
        }
        if (this.isArmored()) {
            final Entity entity = source.getSourceOfDamage();
            if (entity instanceof EntityArrow) {
                return false;
            }
        }
        final Entity entity2 = source.getEntity();
        if (entity2 != null && !(entity2 instanceof EntityPlayer) && entity2 instanceof EntityLivingBase && ((EntityLivingBase)entity2).getCreatureAttribute() == this.getCreatureAttribute()) {
            return false;
        }
        if (this.blockBreakCounter <= 0) {
            this.blockBreakCounter = 20;
        }
        for (int i = 0; i < this.field_82224_i.length; ++i) {
            final int[] field_82224_i = this.field_82224_i;
            final int n = i;
            field_82224_i[n] += 3;
        }
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        final EntityItem entityitem = this.dropItem(Items.nether_star, 1);
        if (entityitem != null) {
            entityitem.setNoDespawn();
        }
        if (!this.worldObj.isRemote) {
            for (final EntityPlayer entityplayer : this.worldObj.getEntitiesWithinAABB((Class<? extends EntityPlayer>)EntityPlayer.class, this.getEntityBoundingBox().expand(50.0, 100.0, 50.0))) {
                entityplayer.triggerAchievement(AchievementList.killWither);
            }
        }
    }
    
    @Override
    protected void despawnEntity() {
        this.entityAge = 0;
    }
    
    @Override
    public int getBrightnessForRender(final float partialTicks) {
        return 15728880;
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
    }
    
    @Override
    public void addPotionEffect(final PotionEffect potioneffectIn) {
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(300.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.6000000238418579);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0);
    }
    
    public float func_82207_a(final int p_82207_1_) {
        return this.field_82221_e[p_82207_1_];
    }
    
    public float func_82210_r(final int p_82210_1_) {
        return this.field_82220_d[p_82210_1_];
    }
    
    public int getInvulTime() {
        return this.dataWatcher.getWatchableObjectInt(20);
    }
    
    public void setInvulTime(final int p_82215_1_) {
        this.dataWatcher.updateObject(20, p_82215_1_);
    }
    
    public int getWatchedTargetId(final int p_82203_1_) {
        return this.dataWatcher.getWatchableObjectInt(17 + p_82203_1_);
    }
    
    public void updateWatchedTargetId(final int targetOffset, final int newId) {
        this.dataWatcher.updateObject(17 + targetOffset, newId);
    }
    
    public boolean isArmored() {
        return this.getHealth() <= this.getMaxHealth() / 2.0f;
    }
    
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }
    
    @Override
    public void mountEntity(final Entity entityIn) {
        this.ridingEntity = null;
    }
}
