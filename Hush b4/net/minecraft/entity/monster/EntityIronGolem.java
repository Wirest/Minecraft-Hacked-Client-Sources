// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.init.Items;
import net.minecraft.block.BlockFlower;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.BlockPos;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIDefendVillage;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAILookAtVillager;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;
import net.minecraft.village.Village;

public class EntityIronGolem extends EntityGolem
{
    private int homeCheckTimer;
    Village villageObj;
    private int attackTimer;
    private int holdRoseTick;
    
    public EntityIronGolem(final World worldIn) {
        super(worldIn);
        this.setSize(1.4f, 2.9f);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, 1.0, true));
        this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9, 32.0f));
        this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6, true));
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0));
        this.tasks.addTask(5, new EntityAILookAtVillager(this));
        this.tasks.addTask(6, new EntityAIWander(this, 0.6));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIDefendVillage(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(3, new AINearestAttackableTargetNonCreeper<Object>(this, EntityLiving.class, 10, false, true, IMob.VISIBLE_MOB_SELECTOR));
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (Byte)0);
    }
    
    @Override
    protected void updateAITasks() {
        final int homeCheckTimer = this.homeCheckTimer - 1;
        this.homeCheckTimer = homeCheckTimer;
        if (homeCheckTimer <= 0) {
            this.homeCheckTimer = 70 + this.rand.nextInt(50);
            this.villageObj = this.worldObj.getVillageCollection().getNearestVillage(new BlockPos(this), 32);
            if (this.villageObj == null) {
                this.detachHome();
            }
            else {
                final BlockPos blockpos = this.villageObj.getCenter();
                this.setHomePosAndDistance(blockpos, (int)(this.villageObj.getVillageRadius() * 0.6f));
            }
        }
        super.updateAITasks();
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25);
    }
    
    @Override
    protected int decreaseAirSupply(final int p_70682_1_) {
        return p_70682_1_;
    }
    
    @Override
    protected void collideWithEntity(final Entity p_82167_1_) {
        if (p_82167_1_ instanceof IMob && !(p_82167_1_ instanceof EntityCreeper) && this.getRNG().nextInt(20) == 0) {
            this.setAttackTarget((EntityLivingBase)p_82167_1_);
        }
        super.collideWithEntity(p_82167_1_);
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.attackTimer > 0) {
            --this.attackTimer;
        }
        if (this.holdRoseTick > 0) {
            --this.holdRoseTick;
        }
        if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.500000277905201E-7 && this.rand.nextInt(5) == 0) {
            final int i = MathHelper.floor_double(this.posX);
            final int j = MathHelper.floor_double(this.posY - 0.20000000298023224);
            final int k = MathHelper.floor_double(this.posZ);
            final IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(i, j, k));
            final Block block = iblockstate.getBlock();
            if (block.getMaterial() != Material.air) {
                this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (this.rand.nextFloat() - 0.5) * this.width, this.getEntityBoundingBox().minY + 0.1, this.posZ + (this.rand.nextFloat() - 0.5) * this.width, 4.0 * (this.rand.nextFloat() - 0.5), 0.5, (this.rand.nextFloat() - 0.5) * 4.0, Block.getStateId(iblockstate));
            }
        }
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("PlayerCreated", this.isPlayerCreated());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setPlayerCreated(tagCompund.getBoolean("PlayerCreated"));
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        this.attackTimer = 10;
        this.worldObj.setEntityState(this, (byte)4);
        final boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(7 + this.rand.nextInt(15)));
        if (flag) {
            entityIn.motionY += 0.4000000059604645;
            this.applyEnchantments(this, entityIn);
        }
        this.playSound("mob.irongolem.throw", 1.0f, 1.0f);
        return flag;
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 4) {
            this.attackTimer = 10;
            this.playSound("mob.irongolem.throw", 1.0f, 1.0f);
        }
        else if (id == 11) {
            this.holdRoseTick = 400;
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    public Village getVillage() {
        return this.villageObj;
    }
    
    public int getAttackTimer() {
        return this.attackTimer;
    }
    
    public void setHoldingRose(final boolean p_70851_1_) {
        this.holdRoseTick = (p_70851_1_ ? 400 : 0);
        this.worldObj.setEntityState(this, (byte)11);
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.irongolem.hit";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.irongolem.death";
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound("mob.irongolem.walk", 1.0f, 1.0f);
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        for (int i = this.rand.nextInt(3), j = 0; j < i; ++j) {
            this.dropItemWithOffset(Item.getItemFromBlock(Blocks.red_flower), 1, (float)BlockFlower.EnumFlowerType.POPPY.getMeta());
        }
        for (int l = 3 + this.rand.nextInt(3), k = 0; k < l; ++k) {
            this.dropItem(Items.iron_ingot, 1);
        }
    }
    
    public int getHoldRoseTick() {
        return this.holdRoseTick;
    }
    
    public boolean isPlayerCreated() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0x0;
    }
    
    public void setPlayerCreated(final boolean p_70849_1_) {
        final byte b0 = this.dataWatcher.getWatchableObjectByte(16);
        if (p_70849_1_) {
            this.dataWatcher.updateObject(16, (byte)(b0 | 0x1));
        }
        else {
            this.dataWatcher.updateObject(16, (byte)(b0 & 0xFFFFFFFE));
        }
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        if (!this.isPlayerCreated() && this.attackingPlayer != null && this.villageObj != null) {
            this.villageObj.setReputationForPlayer(this.attackingPlayer.getName(), -5);
        }
        super.onDeath(cause);
    }
    
    static class AINearestAttackableTargetNonCreeper<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T>
    {
        public AINearestAttackableTargetNonCreeper(final EntityCreature creature, final Class<T> classTarget, final int chance, final boolean p_i45858_4_, final boolean p_i45858_5_, final Predicate<? super T> p_i45858_6_) {
            super(creature, classTarget, chance, p_i45858_4_, p_i45858_5_, p_i45858_6_);
            this.targetEntitySelector = new Predicate<T>() {
                @Override
                public boolean apply(final T p_apply_1_) {
                    if (p_i45858_6_ != null && !p_i45858_6_.apply(p_apply_1_)) {
                        return false;
                    }
                    if (p_apply_1_ instanceof EntityCreeper) {
                        return false;
                    }
                    if (p_apply_1_ instanceof EntityPlayer) {
                        double d0 = EntityAITarget.this.getTargetDistance();
                        if (p_apply_1_.isSneaking()) {
                            d0 *= 0.800000011920929;
                        }
                        if (p_apply_1_.isInvisible()) {
                            float f = ((EntityPlayer)p_apply_1_).getArmorVisibility();
                            if (f < 0.1f) {
                                f = 0.1f;
                            }
                            d0 *= 0.7f * f;
                        }
                        if (p_apply_1_.getDistanceToEntity(creature) > d0) {
                            return false;
                        }
                    }
                    return EntityAITarget.this.isSuitableTarget(p_apply_1_, false);
                }
            };
        }
    }
}
