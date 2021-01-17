// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import java.util.Random;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.Vec3;

public class RandomPositionGenerator
{
    private static Vec3 staticVector;
    
    static {
        RandomPositionGenerator.staticVector = new Vec3(0.0, 0.0, 0.0);
    }
    
    public static Vec3 findRandomTarget(final EntityCreature entitycreatureIn, final int xz, final int y) {
        return findRandomTargetBlock(entitycreatureIn, xz, y, null);
    }
    
    public static Vec3 findRandomTargetBlockTowards(final EntityCreature entitycreatureIn, final int xz, final int y, final Vec3 targetVec3) {
        RandomPositionGenerator.staticVector = targetVec3.subtract(entitycreatureIn.posX, entitycreatureIn.posY, entitycreatureIn.posZ);
        return findRandomTargetBlock(entitycreatureIn, xz, y, RandomPositionGenerator.staticVector);
    }
    
    public static Vec3 findRandomTargetBlockAwayFrom(final EntityCreature entitycreatureIn, final int xz, final int y, final Vec3 targetVec3) {
        RandomPositionGenerator.staticVector = new Vec3(entitycreatureIn.posX, entitycreatureIn.posY, entitycreatureIn.posZ).subtract(targetVec3);
        return findRandomTargetBlock(entitycreatureIn, xz, y, RandomPositionGenerator.staticVector);
    }
    
    private static Vec3 findRandomTargetBlock(final EntityCreature entitycreatureIn, final int xz, final int y, final Vec3 targetVec3) {
        final Random random = entitycreatureIn.getRNG();
        boolean flag = false;
        int i = 0;
        int j = 0;
        int k = 0;
        float f = -99999.0f;
        boolean flag2;
        if (entitycreatureIn.hasHome()) {
            final double d0 = entitycreatureIn.getHomePosition().distanceSq(MathHelper.floor_double(entitycreatureIn.posX), MathHelper.floor_double(entitycreatureIn.posY), MathHelper.floor_double(entitycreatureIn.posZ)) + 4.0;
            final double d2 = entitycreatureIn.getMaximumHomeDistance() + xz;
            flag2 = (d0 < d2 * d2);
        }
        else {
            flag2 = false;
        }
        for (int j2 = 0; j2 < 10; ++j2) {
            int l = random.nextInt(2 * xz + 1) - xz;
            int k2 = random.nextInt(2 * y + 1) - y;
            int i2 = random.nextInt(2 * xz + 1) - xz;
            if (targetVec3 == null || l * targetVec3.xCoord + i2 * targetVec3.zCoord >= 0.0) {
                if (entitycreatureIn.hasHome() && xz > 1) {
                    final BlockPos blockpos = entitycreatureIn.getHomePosition();
                    if (entitycreatureIn.posX > blockpos.getX()) {
                        l -= random.nextInt(xz / 2);
                    }
                    else {
                        l += random.nextInt(xz / 2);
                    }
                    if (entitycreatureIn.posZ > blockpos.getZ()) {
                        i2 -= random.nextInt(xz / 2);
                    }
                    else {
                        i2 += random.nextInt(xz / 2);
                    }
                }
                l += MathHelper.floor_double(entitycreatureIn.posX);
                k2 += MathHelper.floor_double(entitycreatureIn.posY);
                i2 += MathHelper.floor_double(entitycreatureIn.posZ);
                final BlockPos blockpos2 = new BlockPos(l, k2, i2);
                if (!flag2 || entitycreatureIn.isWithinHomeDistanceFromPosition(blockpos2)) {
                    final float f2 = entitycreatureIn.getBlockPathWeight(blockpos2);
                    if (f2 > f) {
                        f = f2;
                        i = l;
                        j = k2;
                        k = i2;
                        flag = true;
                    }
                }
            }
        }
        if (flag) {
            return new Vec3(i, j, k);
        }
        return null;
    }
}
