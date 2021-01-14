package net.minecraft.pathfinding;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.pathfinder.WalkNodeProcessor;

public class PathNavigateGround extends PathNavigate {
    protected WalkNodeProcessor field_179695_a;
    private boolean field_179694_f;
    private static final String __OBFID = "CL_00002246";

    public PathNavigateGround(EntityLiving p_i45875_1_, World worldIn) {
        super(p_i45875_1_, worldIn);
    }

    @Override
    protected PathFinder func_179679_a() {
        field_179695_a = new WalkNodeProcessor();
        field_179695_a.func_176175_a(true);
        return new PathFinder(field_179695_a);
    }

    /**
     * If on ground or swimming and can swim
     */
    @Override
    protected boolean canNavigate() {
        return theEntity.onGround || func_179684_h() && isInLiquid() || theEntity.isRiding() && theEntity instanceof EntityZombie && theEntity.ridingEntity instanceof EntityChicken;
    }

    @Override
    protected Vec3 getEntityPosition() {
        return new Vec3(theEntity.posX, func_179687_p(), theEntity.posZ);
    }

    private int func_179687_p() {
        if (theEntity.isInWater() && func_179684_h()) {
            int var1 = (int) theEntity.getEntityBoundingBox().minY;
            Block var2 = worldObj.getBlockState(new BlockPos(MathHelper.floor_double(theEntity.posX), var1, MathHelper.floor_double(theEntity.posZ))).getBlock();
            int var3 = 0;

            do {
                if (var2 != Blocks.flowing_water && var2 != Blocks.water) {
                    return var1;
                }

                ++var1;
                var2 = worldObj.getBlockState(new BlockPos(MathHelper.floor_double(theEntity.posX), var1, MathHelper.floor_double(theEntity.posZ))).getBlock();
                ++var3;
            } while (var3 <= 16);

            return (int) theEntity.getEntityBoundingBox().minY;
        } else {
            return (int) (theEntity.getEntityBoundingBox().minY + 0.5D);
        }
    }

    /**
     * Trims path data from the end to the first sun covered block
     */
    @Override
    protected void removeSunnyPath() {
        super.removeSunnyPath();

        if (field_179694_f) {
            if (worldObj.isAgainstSky(new BlockPos(MathHelper.floor_double(theEntity.posX), (int) (theEntity.getEntityBoundingBox().minY + 0.5D), MathHelper.floor_double(theEntity.posZ)))) {
                return;
            }

            for (int var1 = 0; var1 < currentPath.getCurrentPathLength(); ++var1) {
                PathPoint var2 = currentPath.getPathPointFromIndex(var1);

                if (worldObj.isAgainstSky(new BlockPos(var2.xCoord, var2.yCoord, var2.zCoord))) {
                    currentPath.setCurrentPathLength(var1 - 1);
                    return;
                }
            }
        }
    }

    /**
     * Returns true when an entity of specified size could safely walk in a
     * straight line between the two points. Args: pos1, pos2, entityXSize,
     * entityYSize, entityZSize
     */
    @Override
    protected boolean isDirectPathBetweenPoints(Vec3 p_75493_1_, Vec3 p_75493_2_, int p_75493_3_, int p_75493_4_, int p_75493_5_) {
        int var6 = MathHelper.floor_double(p_75493_1_.xCoord);
        int var7 = MathHelper.floor_double(p_75493_1_.zCoord);
        double var8 = p_75493_2_.xCoord - p_75493_1_.xCoord;
        double var10 = p_75493_2_.zCoord - p_75493_1_.zCoord;
        double var12 = var8 * var8 + var10 * var10;

        if (var12 < 1.0E-8D) {
            return false;
        } else {
            double var14 = 1.0D / Math.sqrt(var12);
            var8 *= var14;
            var10 *= var14;
            p_75493_3_ += 2;
            p_75493_5_ += 2;

            if (!func_179683_a(var6, (int) p_75493_1_.yCoord, var7, p_75493_3_, p_75493_4_, p_75493_5_, p_75493_1_, var8, var10)) {
                return false;
            } else {
                p_75493_3_ -= 2;
                p_75493_5_ -= 2;
                double var16 = 1.0D / Math.abs(var8);
                double var18 = 1.0D / Math.abs(var10);
                double var20 = var6 * 1 - p_75493_1_.xCoord;
                double var22 = var7 * 1 - p_75493_1_.zCoord;

                if (var8 >= 0.0D) {
                    ++var20;
                }

                if (var10 >= 0.0D) {
                    ++var22;
                }

                var20 /= var8;
                var22 /= var10;
                int var24 = var8 < 0.0D ? -1 : 1;
                int var25 = var10 < 0.0D ? -1 : 1;
                int var26 = MathHelper.floor_double(p_75493_2_.xCoord);
                int var27 = MathHelper.floor_double(p_75493_2_.zCoord);
                int var28 = var26 - var6;
                int var29 = var27 - var7;

                do {
                    if (var28 * var24 <= 0 && var29 * var25 <= 0) {
                        return true;
                    }

                    if (var20 < var22) {
                        var20 += var16;
                        var6 += var24;
                        var28 = var26 - var6;
                    } else {
                        var22 += var18;
                        var7 += var25;
                        var29 = var27 - var7;
                    }
                } while (func_179683_a(var6, (int) p_75493_1_.yCoord, var7, p_75493_3_, p_75493_4_, p_75493_5_, p_75493_1_, var8, var10));

                return false;
            }
        }
    }

    private boolean func_179683_a(int p_179683_1_, int p_179683_2_, int p_179683_3_, int p_179683_4_, int p_179683_5_, int p_179683_6_, Vec3 p_179683_7_, double p_179683_8_, double p_179683_10_) {
        int var12 = p_179683_1_ - p_179683_4_ / 2;
        int var13 = p_179683_3_ - p_179683_6_ / 2;

        if (!func_179692_b(var12, p_179683_2_, var13, p_179683_4_, p_179683_5_, p_179683_6_, p_179683_7_, p_179683_8_, p_179683_10_)) {
            return false;
        } else {
            for (int var14 = var12; var14 < var12 + p_179683_4_; ++var14) {
                for (int var15 = var13; var15 < var13 + p_179683_6_; ++var15) {
                    double var16 = var14 + 0.5D - p_179683_7_.xCoord;
                    double var18 = var15 + 0.5D - p_179683_7_.zCoord;

                    if (var16 * p_179683_8_ + var18 * p_179683_10_ >= 0.0D) {
                        Block var20 = worldObj.getBlockState(new BlockPos(var14, p_179683_2_ - 1, var15)).getBlock();
                        Material var21 = var20.getMaterial();

                        if (var21 == Material.air) {
                            return false;
                        }

                        if (var21 == Material.water && !theEntity.isInWater()) {
                            return false;
                        }

                        if (var21 == Material.lava) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    private boolean func_179692_b(int p_179692_1_, int p_179692_2_, int p_179692_3_, int p_179692_4_, int p_179692_5_, int p_179692_6_, Vec3 p_179692_7_, double p_179692_8_, double p_179692_10_) {
        Iterator var12 = BlockPos.getAllInBox(new BlockPos(p_179692_1_, p_179692_2_, p_179692_3_), new BlockPos(p_179692_1_ + p_179692_4_ - 1, p_179692_2_ + p_179692_5_ - 1, p_179692_3_ + p_179692_6_ - 1)).iterator();

        while (var12.hasNext()) {
            BlockPos var13 = (BlockPos) var12.next();
            double var14 = var13.getX() + 0.5D - p_179692_7_.xCoord;
            double var16 = var13.getZ() + 0.5D - p_179692_7_.zCoord;

            if (var14 * p_179692_8_ + var16 * p_179692_10_ >= 0.0D) {
                Block var18 = worldObj.getBlockState(var13).getBlock();

                if (!var18.isPassable(worldObj, var13)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void func_179690_a(boolean p_179690_1_) {
        field_179695_a.func_176176_c(p_179690_1_);
    }

    public boolean func_179689_e() {
        return field_179695_a.func_176173_e();
    }

    public void func_179688_b(boolean p_179688_1_) {
        field_179695_a.func_176172_b(p_179688_1_);
    }

    public void func_179691_c(boolean p_179691_1_) {
        field_179695_a.func_176175_a(p_179691_1_);
    }

    public boolean func_179686_g() {
        return field_179695_a.func_176179_b();
    }

    public void func_179693_d(boolean p_179693_1_) {
        field_179695_a.func_176178_d(p_179693_1_);
    }

    public boolean func_179684_h() {
        return field_179695_a.func_176174_d();
    }

    public void func_179685_e(boolean p_179685_1_) {
        field_179694_f = p_179685_1_;
    }
}
