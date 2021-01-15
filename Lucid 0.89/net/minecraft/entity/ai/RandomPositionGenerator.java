package net.minecraft.entity.ai;

import java.util.Random;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RandomPositionGenerator
{
    /**
     * used to store a driection when the user passes a point to move towards or away from. WARNING: NEVER THREAD SAFE.
     * MULTIPLE findTowards and findAway calls, will share this var
     */
    private static Vec3 staticVector = new Vec3(0.0D, 0.0D, 0.0D);

    /**
     * finds a random target within par1(x,z) and par2 (y) blocks
     */
    public static Vec3 findRandomTarget(EntityCreature entitycreatureIn, int xz, int y)
    {
        return findRandomTargetBlock(entitycreatureIn, xz, y, (Vec3)null);
    }

    /**
     * finds a random target within par1(x,z) and par2 (y) blocks in the direction of the point par3
     */
    public static Vec3 findRandomTargetBlockTowards(EntityCreature entitycreatureIn, int xz, int y, Vec3 targetVec3)
    {
        staticVector = targetVec3.subtract(entitycreatureIn.posX, entitycreatureIn.posY, entitycreatureIn.posZ);
        return findRandomTargetBlock(entitycreatureIn, xz, y, staticVector);
    }

    /**
     * finds a random target within par1(x,z) and par2 (y) blocks in the reverse direction of the point par3
     */
    public static Vec3 findRandomTargetBlockAwayFrom(EntityCreature entitycreatureIn, int xz, int y, Vec3 targetVec3)
    {
        staticVector = (new Vec3(entitycreatureIn.posX, entitycreatureIn.posY, entitycreatureIn.posZ)).subtract(targetVec3);
        return findRandomTargetBlock(entitycreatureIn, xz, y, staticVector);
    }

    /**
     * searches 10 blocks at random in a within par1(x,z) and par2 (y) distance, ignores those not in the direction of
     * par3Vec3, then points to the tile for which creature.getBlockPathWeight returns the highest number
     */
    private static Vec3 findRandomTargetBlock(EntityCreature entitycreatureIn, int xz, int y, Vec3 targetVec3)
    {
        Random var4 = entitycreatureIn.getRNG();
        boolean var5 = false;
        int var6 = 0;
        int var7 = 0;
        int var8 = 0;
        float var9 = -99999.0F;
        boolean var10;

        if (entitycreatureIn.hasHome())
        {
            double var11 = entitycreatureIn.getHomePosition().distanceSq(MathHelper.floor_double(entitycreatureIn.posX), MathHelper.floor_double(entitycreatureIn.posY), MathHelper.floor_double(entitycreatureIn.posZ)) + 4.0D;
            double var13 = entitycreatureIn.getMaximumHomeDistance() + xz;
            var10 = var11 < var13 * var13;
        }
        else
        {
            var10 = false;
        }

        for (int var17 = 0; var17 < 10; ++var17)
        {
            int var12 = var4.nextInt(2 * xz + 1) - xz;
            int var18 = var4.nextInt(2 * y + 1) - y;
            int var14 = var4.nextInt(2 * xz + 1) - xz;

            if (targetVec3 == null || var12 * targetVec3.xCoord + var14 * targetVec3.zCoord >= 0.0D)
            {
                BlockPos var15;

                if (entitycreatureIn.hasHome() && xz > 1)
                {
                    var15 = entitycreatureIn.getHomePosition();

                    if (entitycreatureIn.posX > var15.getX())
                    {
                        var12 -= var4.nextInt(xz / 2);
                    }
                    else
                    {
                        var12 += var4.nextInt(xz / 2);
                    }

                    if (entitycreatureIn.posZ > var15.getZ())
                    {
                        var14 -= var4.nextInt(xz / 2);
                    }
                    else
                    {
                        var14 += var4.nextInt(xz / 2);
                    }
                }

                var12 += MathHelper.floor_double(entitycreatureIn.posX);
                var18 += MathHelper.floor_double(entitycreatureIn.posY);
                var14 += MathHelper.floor_double(entitycreatureIn.posZ);
                var15 = new BlockPos(var12, var18, var14);

                if (!var10 || entitycreatureIn.isWithinHomeDistanceFromPosition(var15))
                {
                    float var16 = entitycreatureIn.getBlockPathWeight(var15);

                    if (var16 > var9)
                    {
                        var9 = var16;
                        var6 = var12;
                        var7 = var18;
                        var8 = var14;
                        var5 = true;
                    }
                }
            }
        }

        if (var5)
        {
            return new Vec3(var6, var7, var8);
        }
        else
        {
            return null;
        }
    }
}
