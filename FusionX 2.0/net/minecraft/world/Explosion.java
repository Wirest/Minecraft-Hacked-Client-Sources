package net.minecraft.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Explosion
{
    /** whether or not the explosion sets fire to blocks around it */
    private final boolean isFlaming;

    /** whether or not this explosion spawns smoke particles */
    private final boolean isSmoking;
    private final Random explosionRNG;
    private final World worldObj;
    private final double explosionX;
    private final double explosionY;
    private final double explosionZ;
    private final Entity exploder;
    private final float explosionSize;

    /** A list of ChunkPositions of blocks affected by this explosion */
    private final List affectedBlockPositions;
    private final Map field_77288_k;
    private static final String __OBFID = "CL_00000134";

    public Explosion(World worldIn, Entity p_i45752_2_, double p_i45752_3_, double p_i45752_5_, double p_i45752_7_, float p_i45752_9_, List p_i45752_10_)
    {
        this(worldIn, p_i45752_2_, p_i45752_3_, p_i45752_5_, p_i45752_7_, p_i45752_9_, false, true, p_i45752_10_);
    }

    public Explosion(World worldIn, Entity p_i45753_2_, double p_i45753_3_, double p_i45753_5_, double p_i45753_7_, float p_i45753_9_, boolean p_i45753_10_, boolean p_i45753_11_, List p_i45753_12_)
    {
        this(worldIn, p_i45753_2_, p_i45753_3_, p_i45753_5_, p_i45753_7_, p_i45753_9_, p_i45753_10_, p_i45753_11_);
        this.affectedBlockPositions.addAll(p_i45753_12_);
    }

    public Explosion(World worldIn, Entity p_i45754_2_, double p_i45754_3_, double p_i45754_5_, double p_i45754_7_, float p_i45754_9_, boolean p_i45754_10_, boolean p_i45754_11_)
    {
        this.explosionRNG = new Random();
        this.affectedBlockPositions = Lists.newArrayList();
        this.field_77288_k = Maps.newHashMap();
        this.worldObj = worldIn;
        this.exploder = p_i45754_2_;
        this.explosionSize = p_i45754_9_;
        this.explosionX = p_i45754_3_;
        this.explosionY = p_i45754_5_;
        this.explosionZ = p_i45754_7_;
        this.isFlaming = p_i45754_10_;
        this.isSmoking = p_i45754_11_;
    }

    /**
     * Does the first part of the explosion (destroy blocks)
     */
    public void doExplosionA()
    {
        HashSet var1 = Sets.newHashSet();
        boolean var2 = true;
        int var4;
        int var5;

        for (int var3 = 0; var3 < 16; ++var3)
        {
            for (var4 = 0; var4 < 16; ++var4)
            {
                for (var5 = 0; var5 < 16; ++var5)
                {
                    if (var3 == 0 || var3 == 15 || var4 == 0 || var4 == 15 || var5 == 0 || var5 == 15)
                    {
                        double var6 = (double)((float)var3 / 15.0F * 2.0F - 1.0F);
                        double var8 = (double)((float)var4 / 15.0F * 2.0F - 1.0F);
                        double var10 = (double)((float)var5 / 15.0F * 2.0F - 1.0F);
                        double var12 = Math.sqrt(var6 * var6 + var8 * var8 + var10 * var10);
                        var6 /= var12;
                        var8 /= var12;
                        var10 /= var12;
                        float var14 = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
                        double var15 = this.explosionX;
                        double var17 = this.explosionY;
                        double var19 = this.explosionZ;

                        for (float var21 = 0.3F; var14 > 0.0F; var14 -= 0.22500001F)
                        {
                            BlockPos var22 = new BlockPos(var15, var17, var19);
                            IBlockState var23 = this.worldObj.getBlockState(var22);

                            if (var23.getBlock().getMaterial() != Material.air)
                            {
                                float var24 = this.exploder != null ? this.exploder.getExplosionResistance(this, this.worldObj, var22, var23) : var23.getBlock().getExplosionResistance((Entity)null);
                                var14 -= (var24 + 0.3F) * 0.3F;
                            }

                            if (var14 > 0.0F && (this.exploder == null || this.exploder.func_174816_a(this, this.worldObj, var22, var23, var14)))
                            {
                                var1.add(var22);
                            }

                            var15 += var6 * 0.30000001192092896D;
                            var17 += var8 * 0.30000001192092896D;
                            var19 += var10 * 0.30000001192092896D;
                        }
                    }
                }
            }
        }

        this.affectedBlockPositions.addAll(var1);
        float var30 = this.explosionSize * 2.0F;
        var4 = MathHelper.floor_double(this.explosionX - (double)var30 - 1.0D);
        var5 = MathHelper.floor_double(this.explosionX + (double)var30 + 1.0D);
        int var31 = MathHelper.floor_double(this.explosionY - (double)var30 - 1.0D);
        int var7 = MathHelper.floor_double(this.explosionY + (double)var30 + 1.0D);
        int var32 = MathHelper.floor_double(this.explosionZ - (double)var30 - 1.0D);
        int var9 = MathHelper.floor_double(this.explosionZ + (double)var30 + 1.0D);
        List var33 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB((double)var4, (double)var31, (double)var32, (double)var5, (double)var7, (double)var9));
        Vec3 var11 = new Vec3(this.explosionX, this.explosionY, this.explosionZ);

        for (int var34 = 0; var34 < var33.size(); ++var34)
        {
            Entity var13 = (Entity)var33.get(var34);

            if (!var13.func_180427_aV())
            {
                double var35 = var13.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double)var30;

                if (var35 <= 1.0D)
                {
                    double var16 = var13.posX - this.explosionX;
                    double var18 = var13.posY + (double)var13.getEyeHeight() - this.explosionY;
                    double var20 = var13.posZ - this.explosionZ;
                    double var36 = (double)MathHelper.sqrt_double(var16 * var16 + var18 * var18 + var20 * var20);

                    if (var36 != 0.0D)
                    {
                        var16 /= var36;
                        var18 /= var36;
                        var20 /= var36;
                        double var37 = (double)this.worldObj.getBlockDensity(var11, var13.getEntityBoundingBox());
                        double var26 = (1.0D - var35) * var37;
                        var13.attackEntityFrom(DamageSource.setExplosionSource(this), (float)((int)((var26 * var26 + var26) / 2.0D * 8.0D * (double)var30 + 1.0D)));
                        double var28 = EnchantmentProtection.func_92092_a(var13, var26);
                        var13.motionX += var16 * var28;
                        var13.motionY += var18 * var28;
                        var13.motionZ += var20 * var28;

                        if (var13 instanceof EntityPlayer)
                        {
                            this.field_77288_k.put((EntityPlayer)var13, new Vec3(var16 * var26, var18 * var26, var20 * var26));
                        }
                    }
                }
            }
        }
    }

    /**
     * Does the second part of the explosion (sound, particles, drop spawn)
     */
    public void doExplosionB(boolean p_77279_1_)
    {
        this.worldObj.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

        if (this.explosionSize >= 2.0F && this.isSmoking)
        {
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
        }
        else
        {
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
        }

        Iterator var2;
        BlockPos var3;

        if (this.isSmoking)
        {
            var2 = this.affectedBlockPositions.iterator();

            while (var2.hasNext())
            {
                var3 = (BlockPos)var2.next();
                Block var4 = this.worldObj.getBlockState(var3).getBlock();

                if (p_77279_1_)
                {
                    double var5 = (double)((float)var3.getX() + this.worldObj.rand.nextFloat());
                    double var7 = (double)((float)var3.getY() + this.worldObj.rand.nextFloat());
                    double var9 = (double)((float)var3.getZ() + this.worldObj.rand.nextFloat());
                    double var11 = var5 - this.explosionX;
                    double var13 = var7 - this.explosionY;
                    double var15 = var9 - this.explosionZ;
                    double var17 = (double)MathHelper.sqrt_double(var11 * var11 + var13 * var13 + var15 * var15);
                    var11 /= var17;
                    var13 /= var17;
                    var15 /= var17;
                    double var19 = 0.5D / (var17 / (double)this.explosionSize + 0.1D);
                    var19 *= (double)(this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F);
                    var11 *= var19;
                    var13 *= var19;
                    var15 *= var19;
                    this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (var5 + this.explosionX * 1.0D) / 2.0D, (var7 + this.explosionY * 1.0D) / 2.0D, (var9 + this.explosionZ * 1.0D) / 2.0D, var11, var13, var15, new int[0]);
                    this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var5, var7, var9, var11, var13, var15, new int[0]);
                }

                if (var4.getMaterial() != Material.air)
                {
                    if (var4.canDropFromExplosion(this))
                    {
                        var4.dropBlockAsItemWithChance(this.worldObj, var3, this.worldObj.getBlockState(var3), 1.0F / this.explosionSize, 0);
                    }

                    this.worldObj.setBlockState(var3, Blocks.air.getDefaultState(), 3);
                    var4.onBlockDestroyedByExplosion(this.worldObj, var3, this);
                }
            }
        }

        if (this.isFlaming)
        {
            var2 = this.affectedBlockPositions.iterator();

            while (var2.hasNext())
            {
                var3 = (BlockPos)var2.next();

                if (this.worldObj.getBlockState(var3).getBlock().getMaterial() == Material.air && this.worldObj.getBlockState(var3.offsetDown()).getBlock().isFullBlock() && this.explosionRNG.nextInt(3) == 0)
                {
                    this.worldObj.setBlockState(var3, Blocks.fire.getDefaultState());
                }
            }
        }
    }

    public Map func_77277_b()
    {
        return this.field_77288_k;
    }

    /**
     * Returns either the entity that placed the explosive block, the entity that caused the explosion or null.
     */
    public EntityLivingBase getExplosivePlacedBy()
    {
        return this.exploder == null ? null : (this.exploder instanceof EntityTNTPrimed ? ((EntityTNTPrimed)this.exploder).getTntPlacedBy() : (this.exploder instanceof EntityLivingBase ? (EntityLivingBase)this.exploder : null));
    }

    public void func_180342_d()
    {
        this.affectedBlockPositions.clear();
    }

    public List func_180343_e()
    {
        return this.affectedBlockPositions;
    }
}
