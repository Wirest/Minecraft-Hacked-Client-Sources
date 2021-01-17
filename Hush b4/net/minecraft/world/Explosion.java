// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.Block;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.block.state.IBlockState;
import java.util.Set;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.util.DamageSource;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.block.material.Material;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import java.util.Collection;
import net.minecraft.util.Vec3;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Map;
import net.minecraft.util.BlockPos;
import java.util.List;
import net.minecraft.entity.Entity;
import java.util.Random;

public class Explosion
{
    private final boolean isFlaming;
    private final boolean isSmoking;
    private final Random explosionRNG;
    private final World worldObj;
    private final double explosionX;
    private final double explosionY;
    private final double explosionZ;
    private final Entity exploder;
    private final float explosionSize;
    private final List<BlockPos> affectedBlockPositions;
    private final Map<EntityPlayer, Vec3> playerKnockbackMap;
    
    public Explosion(final World worldIn, final Entity p_i45752_2_, final double p_i45752_3_, final double p_i45752_5_, final double p_i45752_7_, final float p_i45752_9_, final List<BlockPos> p_i45752_10_) {
        this(worldIn, p_i45752_2_, p_i45752_3_, p_i45752_5_, p_i45752_7_, p_i45752_9_, false, true, p_i45752_10_);
    }
    
    public Explosion(final World worldIn, final Entity p_i45753_2_, final double p_i45753_3_, final double p_i45753_5_, final double p_i45753_7_, final float p_i45753_9_, final boolean p_i45753_10_, final boolean p_i45753_11_, final List<BlockPos> p_i45753_12_) {
        this(worldIn, p_i45753_2_, p_i45753_3_, p_i45753_5_, p_i45753_7_, p_i45753_9_, p_i45753_10_, p_i45753_11_);
        this.affectedBlockPositions.addAll(p_i45753_12_);
    }
    
    public Explosion(final World worldIn, final Entity p_i45754_2_, final double p_i45754_3_, final double p_i45754_5_, final double p_i45754_7_, final float size, final boolean p_i45754_10_, final boolean p_i45754_11_) {
        this.explosionRNG = new Random();
        this.affectedBlockPositions = (List<BlockPos>)Lists.newArrayList();
        this.playerKnockbackMap = (Map<EntityPlayer, Vec3>)Maps.newHashMap();
        this.worldObj = worldIn;
        this.exploder = p_i45754_2_;
        this.explosionSize = size;
        this.explosionX = p_i45754_3_;
        this.explosionY = p_i45754_5_;
        this.explosionZ = p_i45754_7_;
        this.isFlaming = p_i45754_10_;
        this.isSmoking = p_i45754_11_;
    }
    
    public void doExplosionA() {
        final Set<BlockPos> set = (Set<BlockPos>)Sets.newHashSet();
        final int i = 16;
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                for (int l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d0 = j / 15.0f * 2.0f - 1.0f;
                        double d2 = k / 15.0f * 2.0f - 1.0f;
                        double d3 = l / 15.0f * 2.0f - 1.0f;
                        final double d4 = Math.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
                        d0 /= d4;
                        d2 /= d4;
                        d3 /= d4;
                        float f = this.explosionSize * (0.7f + this.worldObj.rand.nextFloat() * 0.6f);
                        double d5 = this.explosionX;
                        double d6 = this.explosionY;
                        double d7 = this.explosionZ;
                        final float f2 = 0.3f;
                        while (f > 0.0f) {
                            final BlockPos blockpos = new BlockPos(d5, d6, d7);
                            final IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
                            if (iblockstate.getBlock().getMaterial() != Material.air) {
                                final float f3 = (this.exploder != null) ? this.exploder.getExplosionResistance(this, this.worldObj, blockpos, iblockstate) : iblockstate.getBlock().getExplosionResistance(null);
                                f -= (f3 + 0.3f) * 0.3f;
                            }
                            if (f > 0.0f && (this.exploder == null || this.exploder.verifyExplosion(this, this.worldObj, blockpos, iblockstate, f))) {
                                set.add(blockpos);
                            }
                            d5 += d0 * 0.30000001192092896;
                            d6 += d2 * 0.30000001192092896;
                            d7 += d3 * 0.30000001192092896;
                            f -= 0.22500001f;
                        }
                    }
                }
            }
        }
        this.affectedBlockPositions.addAll(set);
        final float f4 = this.explosionSize * 2.0f;
        final int k2 = MathHelper.floor_double(this.explosionX - f4 - 1.0);
        final int l2 = MathHelper.floor_double(this.explosionX + f4 + 1.0);
        final int i2 = MathHelper.floor_double(this.explosionY - f4 - 1.0);
        final int i3 = MathHelper.floor_double(this.explosionY + f4 + 1.0);
        final int j2 = MathHelper.floor_double(this.explosionZ - f4 - 1.0);
        final int j3 = MathHelper.floor_double(this.explosionZ + f4 + 1.0);
        final List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB(k2, i2, j2, l2, i3, j3));
        final Vec3 vec3 = new Vec3(this.explosionX, this.explosionY, this.explosionZ);
        for (int k3 = 0; k3 < list.size(); ++k3) {
            final Entity entity = list.get(k3);
            if (!entity.isImmuneToExplosions()) {
                final double d8 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / f4;
                if (d8 <= 1.0) {
                    double d9 = entity.posX - this.explosionX;
                    double d10 = entity.posY + entity.getEyeHeight() - this.explosionY;
                    double d11 = entity.posZ - this.explosionZ;
                    final double d12 = MathHelper.sqrt_double(d9 * d9 + d10 * d10 + d11 * d11);
                    if (d12 != 0.0) {
                        d9 /= d12;
                        d10 /= d12;
                        d11 /= d12;
                        final double d13 = this.worldObj.getBlockDensity(vec3, entity.getEntityBoundingBox());
                        final double d14 = (1.0 - d8) * d13;
                        entity.attackEntityFrom(DamageSource.setExplosionSource(this), (float)(int)((d14 * d14 + d14) / 2.0 * 8.0 * f4 + 1.0));
                        final double d15 = EnchantmentProtection.func_92092_a(entity, d14);
                        final Entity entity2 = entity;
                        entity2.motionX += d9 * d15;
                        final Entity entity3 = entity;
                        entity3.motionY += d10 * d15;
                        final Entity entity4 = entity;
                        entity4.motionZ += d11 * d15;
                        if (entity instanceof EntityPlayer && !((EntityPlayer)entity).capabilities.disableDamage) {
                            this.playerKnockbackMap.put((EntityPlayer)entity, new Vec3(d9 * d14, d10 * d14, d11 * d14));
                        }
                    }
                }
            }
        }
    }
    
    public void doExplosionB(final boolean spawnParticles) {
        this.worldObj.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0f, (1.0f + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2f) * 0.7f);
        if (this.explosionSize >= 2.0f && this.isSmoking) {
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.explosionX, this.explosionY, this.explosionZ, 1.0, 0.0, 0.0, new int[0]);
        }
        else {
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.explosionX, this.explosionY, this.explosionZ, 1.0, 0.0, 0.0, new int[0]);
        }
        if (this.isSmoking) {
            for (final BlockPos blockpos : this.affectedBlockPositions) {
                final Block block = this.worldObj.getBlockState(blockpos).getBlock();
                if (spawnParticles) {
                    final double d0 = blockpos.getX() + this.worldObj.rand.nextFloat();
                    final double d2 = blockpos.getY() + this.worldObj.rand.nextFloat();
                    final double d3 = blockpos.getZ() + this.worldObj.rand.nextFloat();
                    double d4 = d0 - this.explosionX;
                    double d5 = d2 - this.explosionY;
                    double d6 = d3 - this.explosionZ;
                    final double d7 = MathHelper.sqrt_double(d4 * d4 + d5 * d5 + d6 * d6);
                    d4 /= d7;
                    d5 /= d7;
                    d6 /= d7;
                    double d8 = 0.5 / (d7 / this.explosionSize + 0.1);
                    d8 *= this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3f;
                    d4 *= d8;
                    d5 *= d8;
                    d6 *= d8;
                    this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + this.explosionX * 1.0) / 2.0, (d2 + this.explosionY * 1.0) / 2.0, (d3 + this.explosionZ * 1.0) / 2.0, d4, d5, d6, new int[0]);
                    this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d2, d3, d4, d5, d6, new int[0]);
                }
                if (block.getMaterial() != Material.air) {
                    if (block.canDropFromExplosion(this)) {
                        block.dropBlockAsItemWithChance(this.worldObj, blockpos, this.worldObj.getBlockState(blockpos), 1.0f / this.explosionSize, 0);
                    }
                    this.worldObj.setBlockState(blockpos, Blocks.air.getDefaultState(), 3);
                    block.onBlockDestroyedByExplosion(this.worldObj, blockpos, this);
                }
            }
        }
        if (this.isFlaming) {
            for (final BlockPos blockpos2 : this.affectedBlockPositions) {
                if (this.worldObj.getBlockState(blockpos2).getBlock().getMaterial() == Material.air && this.worldObj.getBlockState(blockpos2.down()).getBlock().isFullBlock() && this.explosionRNG.nextInt(3) == 0) {
                    this.worldObj.setBlockState(blockpos2, Blocks.fire.getDefaultState());
                }
            }
        }
    }
    
    public Map<EntityPlayer, Vec3> getPlayerKnockbackMap() {
        return this.playerKnockbackMap;
    }
    
    public EntityLivingBase getExplosivePlacedBy() {
        return (this.exploder == null) ? null : ((this.exploder instanceof EntityTNTPrimed) ? ((EntityTNTPrimed)this.exploder).getTntPlacedBy() : ((this.exploder instanceof EntityLivingBase) ? ((EntityLivingBase)this.exploder) : null));
    }
    
    public void func_180342_d() {
        this.affectedBlockPositions.clear();
    }
    
    public List<BlockPos> getAffectedBlockPositions() {
        return this.affectedBlockPositions;
    }
}
