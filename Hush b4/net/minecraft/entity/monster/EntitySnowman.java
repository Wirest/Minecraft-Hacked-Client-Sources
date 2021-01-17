// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;
import net.minecraft.entity.IRangedAttackMob;

public class EntitySnowman extends EntityGolem implements IRangedAttackMob
{
    public EntitySnowman(final World worldIn) {
        super(worldIn);
        this.setSize(0.7f, 1.9f);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAIArrowAttack(this, 1.25, 20, 10.0f));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<Object>(this, EntityLiving.class, 10, true, false, IMob.mobSelector));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224);
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.worldObj.isRemote) {
            int i = MathHelper.floor_double(this.posX);
            int j = MathHelper.floor_double(this.posY);
            int k = MathHelper.floor_double(this.posZ);
            if (this.isWet()) {
                this.attackEntityFrom(DamageSource.drown, 1.0f);
            }
            if (this.worldObj.getBiomeGenForCoords(new BlockPos(i, 0, k)).getFloatTemperature(new BlockPos(i, j, k)) > 1.0f) {
                this.attackEntityFrom(DamageSource.onFire, 1.0f);
            }
            for (int l = 0; l < 4; ++l) {
                i = MathHelper.floor_double(this.posX + (l % 2 * 2 - 1) * 0.25f);
                j = MathHelper.floor_double(this.posY);
                k = MathHelper.floor_double(this.posZ + (l / 2 % 2 * 2 - 1) * 0.25f);
                final BlockPos blockpos = new BlockPos(i, j, k);
                if (this.worldObj.getBlockState(blockpos).getBlock().getMaterial() == Material.air && this.worldObj.getBiomeGenForCoords(new BlockPos(i, 0, k)).getFloatTemperature(blockpos) < 0.8f && Blocks.snow_layer.canPlaceBlockAt(this.worldObj, blockpos)) {
                    this.worldObj.setBlockState(blockpos, Blocks.snow_layer.getDefaultState());
                }
            }
        }
    }
    
    @Override
    protected Item getDropItem() {
        return Items.snowball;
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        for (int i = this.rand.nextInt(16), j = 0; j < i; ++j) {
            this.dropItem(Items.snowball, 1);
        }
    }
    
    @Override
    public void attackEntityWithRangedAttack(final EntityLivingBase p_82196_1_, final float p_82196_2_) {
        final EntitySnowball entitysnowball = new EntitySnowball(this.worldObj, this);
        final double d0 = p_82196_1_.posY + p_82196_1_.getEyeHeight() - 1.100000023841858;
        final double d2 = p_82196_1_.posX - this.posX;
        final double d3 = d0 - entitysnowball.posY;
        final double d4 = p_82196_1_.posZ - this.posZ;
        final float f = MathHelper.sqrt_double(d2 * d2 + d4 * d4) * 0.2f;
        entitysnowball.setThrowableHeading(d2, d3 + f, d4, 1.6f, 12.0f);
        this.playSound("random.bow", 1.0f, 1.0f / (this.getRNG().nextFloat() * 0.4f + 0.8f));
        this.worldObj.spawnEntityInWorld(entitysnowball);
    }
    
    @Override
    public float getEyeHeight() {
        return 1.7f;
    }
}
