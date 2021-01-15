package net.minecraft.world;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;

public class Teleporter
{
    private final WorldServer worldServerInstance;

    /** A private Random() function in Teleporter */
    private final Random random;

    /** Stores successful portal placement locations for rapid lookup. */
    private final LongHashMap destinationCoordinateCache = new LongHashMap();

    /**
     * A list of valid keys for the destinationCoordainteCache. These are based on the X & Z of the players initial
     * location.
     */
    private final List destinationCoordinateKeys = Lists.newArrayList();

    public Teleporter(WorldServer worldIn)
    {
        this.worldServerInstance = worldIn;
        this.random = new Random(worldIn.getSeed());
    }

    public void placeInPortal(Entity entityIn, float rotationYaw)
    {
        if (this.worldServerInstance.provider.getDimensionId() != 1)
        {
            if (!this.placeInExistingPortal(entityIn, rotationYaw))
            {
                this.makePortal(entityIn);
                this.placeInExistingPortal(entityIn, rotationYaw);
            }
        }
        else
        {
            int var3 = MathHelper.floor_double(entityIn.posX);
            int var4 = MathHelper.floor_double(entityIn.posY) - 1;
            int var5 = MathHelper.floor_double(entityIn.posZ);
            byte var6 = 1;
            byte var7 = 0;

            for (int var8 = -2; var8 <= 2; ++var8)
            {
                for (int var9 = -2; var9 <= 2; ++var9)
                {
                    for (int var10 = -1; var10 < 3; ++var10)
                    {
                        int var11 = var3 + var9 * var6 + var8 * var7;
                        int var12 = var4 + var10;
                        int var13 = var5 + var9 * var7 - var8 * var6;
                        boolean var14 = var10 < 0;
                        this.worldServerInstance.setBlockState(new BlockPos(var11, var12, var13), var14 ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
                    }
                }
            }

            entityIn.setLocationAndAngles(var3, var4, var5, entityIn.rotationYaw, 0.0F);
            entityIn.motionX = entityIn.motionY = entityIn.motionZ = 0.0D;
        }
    }

    public boolean placeInExistingPortal(Entity entityIn, float rotationYaw)
    {
        double var4 = -1.0D;
        int var6 = MathHelper.floor_double(entityIn.posX);
        int var7 = MathHelper.floor_double(entityIn.posZ);
        boolean var8 = true;
        Object var9 = BlockPos.ORIGIN;
        long var10 = ChunkCoordIntPair.chunkXZ2Int(var6, var7);

        if (this.destinationCoordinateCache.containsItem(var10))
        {
            Teleporter.PortalPosition var12 = (Teleporter.PortalPosition)this.destinationCoordinateCache.getValueByKey(var10);
            var4 = 0.0D;
            var9 = var12;
            var12.lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
            var8 = false;
        }
        else
        {
            BlockPos var34 = new BlockPos(entityIn);

            for (int var13 = -128; var13 <= 128; ++var13)
            {
                BlockPos var16;

                for (int var14 = -128; var14 <= 128; ++var14)
                {
                    for (BlockPos var15 = var34.add(var13, this.worldServerInstance.getActualHeight() - 1 - var34.getY(), var14); var15.getY() >= 0; var15 = var16)
                    {
                        var16 = var15.down();

                        if (this.worldServerInstance.getBlockState(var15).getBlock() == Blocks.portal)
                        {
                            while (this.worldServerInstance.getBlockState(var16 = var15.down()).getBlock() == Blocks.portal)
                            {
                                var15 = var16;
                            }

                            double var17 = var15.distanceSq(var34);

                            if (var4 < 0.0D || var17 < var4)
                            {
                                var4 = var17;
                                var9 = var15;
                            }
                        }
                    }
                }
            }
        }

        if (var4 >= 0.0D)
        {
            if (var8)
            {
                this.destinationCoordinateCache.add(var10, new Teleporter.PortalPosition((BlockPos)var9, this.worldServerInstance.getTotalWorldTime()));
                this.destinationCoordinateKeys.add(Long.valueOf(var10));
            }

            double var35 = ((BlockPos)var9).getX() + 0.5D;
            double var36 = ((BlockPos)var9).getY() + 0.5D;
            double var37 = ((BlockPos)var9).getZ() + 0.5D;
            EnumFacing var18 = null;

            if (this.worldServerInstance.getBlockState(((BlockPos)var9).west()).getBlock() == Blocks.portal)
            {
                var18 = EnumFacing.NORTH;
            }

            if (this.worldServerInstance.getBlockState(((BlockPos)var9).east()).getBlock() == Blocks.portal)
            {
                var18 = EnumFacing.SOUTH;
            }

            if (this.worldServerInstance.getBlockState(((BlockPos)var9).north()).getBlock() == Blocks.portal)
            {
                var18 = EnumFacing.EAST;
            }

            if (this.worldServerInstance.getBlockState(((BlockPos)var9).south()).getBlock() == Blocks.portal)
            {
                var18 = EnumFacing.WEST;
            }

            EnumFacing var19 = EnumFacing.getHorizontal(entityIn.getTeleportDirection());

            if (var18 != null)
            {
                EnumFacing var20 = var18.rotateYCCW();
                BlockPos var21 = ((BlockPos)var9).offset(var18);
                boolean var22 = this.func_180265_a(var21);
                boolean var23 = this.func_180265_a(var21.offset(var20));

                if (var23 && var22)
                {
                    var9 = ((BlockPos)var9).offset(var20);
                    var18 = var18.getOpposite();
                    var20 = var20.getOpposite();
                    BlockPos var24 = ((BlockPos)var9).offset(var18);
                    var22 = this.func_180265_a(var24);
                    var23 = this.func_180265_a(var24.offset(var20));
                }

                float var38 = 0.5F;
                float var25 = 0.5F;

                if (!var23 && var22)
                {
                    var38 = 1.0F;
                }
                else if (var23 && !var22)
                {
                    var38 = 0.0F;
                }
                else if (var23)
                {
                    var25 = 0.0F;
                }

                var35 = ((BlockPos)var9).getX() + 0.5D;
                var36 = ((BlockPos)var9).getY() + 0.5D;
                var37 = ((BlockPos)var9).getZ() + 0.5D;
                var35 += var20.getFrontOffsetX() * var38 + var18.getFrontOffsetX() * var25;
                var37 += var20.getFrontOffsetZ() * var38 + var18.getFrontOffsetZ() * var25;
                float var26 = 0.0F;
                float var27 = 0.0F;
                float var28 = 0.0F;
                float var29 = 0.0F;

                if (var18 == var19)
                {
                    var26 = 1.0F;
                    var27 = 1.0F;
                }
                else if (var18 == var19.getOpposite())
                {
                    var26 = -1.0F;
                    var27 = -1.0F;
                }
                else if (var18 == var19.rotateY())
                {
                    var28 = 1.0F;
                    var29 = -1.0F;
                }
                else
                {
                    var28 = -1.0F;
                    var29 = 1.0F;
                }

                double var30 = entityIn.motionX;
                double var32 = entityIn.motionZ;
                entityIn.motionX = var30 * var26 + var32 * var29;
                entityIn.motionZ = var30 * var28 + var32 * var27;
                entityIn.rotationYaw = rotationYaw - var19.getHorizontalIndex() * 90 + var18.getHorizontalIndex() * 90;
            }
            else
            {
                entityIn.motionX = entityIn.motionY = entityIn.motionZ = 0.0D;
            }

            entityIn.setLocationAndAngles(var35, var36, var37, entityIn.rotationYaw, entityIn.rotationPitch);
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean func_180265_a(BlockPos pos)
    {
        return !this.worldServerInstance.isAirBlock(pos) || !this.worldServerInstance.isAirBlock(pos.up());
    }

    public boolean makePortal(Entity p_85188_1_)
    {
        byte var2 = 16;
        double var3 = -1.0D;
        int var5 = MathHelper.floor_double(p_85188_1_.posX);
        int var6 = MathHelper.floor_double(p_85188_1_.posY);
        int var7 = MathHelper.floor_double(p_85188_1_.posZ);
        int var8 = var5;
        int var9 = var6;
        int var10 = var7;
        int var11 = 0;
        int var12 = this.random.nextInt(4);
        int var13;
        double var14;
        int var16;
        double var17;
        int var19;
        int var20;
        int var21;
        int var22;
        int var23;
        int var24;
        int var25;
        int var26;
        int var27;
        double var32;
        double var33;

        for (var13 = var5 - var2; var13 <= var5 + var2; ++var13)
        {
            var14 = var13 + 0.5D - p_85188_1_.posX;

            for (var16 = var7 - var2; var16 <= var7 + var2; ++var16)
            {
                var17 = var16 + 0.5D - p_85188_1_.posZ;
                label271:

                for (var19 = this.worldServerInstance.getActualHeight() - 1; var19 >= 0; --var19)
                {
                    if (this.worldServerInstance.isAirBlock(new BlockPos(var13, var19, var16)))
                    {
                        while (var19 > 0 && this.worldServerInstance.isAirBlock(new BlockPos(var13, var19 - 1, var16)))
                        {
                            --var19;
                        }

                        for (var20 = var12; var20 < var12 + 4; ++var20)
                        {
                            var21 = var20 % 2;
                            var22 = 1 - var21;

                            if (var20 % 4 >= 2)
                            {
                                var21 = -var21;
                                var22 = -var22;
                            }

                            for (var23 = 0; var23 < 3; ++var23)
                            {
                                for (var24 = 0; var24 < 4; ++var24)
                                {
                                    for (var25 = -1; var25 < 4; ++var25)
                                    {
                                        var26 = var13 + (var24 - 1) * var21 + var23 * var22;
                                        var27 = var19 + var25;
                                        int var28 = var16 + (var24 - 1) * var22 - var23 * var21;

                                        if (var25 < 0 && !this.worldServerInstance.getBlockState(new BlockPos(var26, var27, var28)).getBlock().getMaterial().isSolid() || var25 >= 0 && !this.worldServerInstance.isAirBlock(new BlockPos(var26, var27, var28)))
                                        {
                                            continue label271;
                                        }
                                    }
                                }
                            }

                            var32 = var19 + 0.5D - p_85188_1_.posY;
                            var33 = var14 * var14 + var32 * var32 + var17 * var17;

                            if (var3 < 0.0D || var33 < var3)
                            {
                                var3 = var33;
                                var8 = var13;
                                var9 = var19;
                                var10 = var16;
                                var11 = var20 % 4;
                            }
                        }
                    }
                }
            }
        }

        if (var3 < 0.0D)
        {
            for (var13 = var5 - var2; var13 <= var5 + var2; ++var13)
            {
                var14 = var13 + 0.5D - p_85188_1_.posX;

                for (var16 = var7 - var2; var16 <= var7 + var2; ++var16)
                {
                    var17 = var16 + 0.5D - p_85188_1_.posZ;
                    label219:

                    for (var19 = this.worldServerInstance.getActualHeight() - 1; var19 >= 0; --var19)
                    {
                        if (this.worldServerInstance.isAirBlock(new BlockPos(var13, var19, var16)))
                        {
                            while (var19 > 0 && this.worldServerInstance.isAirBlock(new BlockPos(var13, var19 - 1, var16)))
                            {
                                --var19;
                            }

                            for (var20 = var12; var20 < var12 + 2; ++var20)
                            {
                                var21 = var20 % 2;
                                var22 = 1 - var21;

                                for (var23 = 0; var23 < 4; ++var23)
                                {
                                    for (var24 = -1; var24 < 4; ++var24)
                                    {
                                        var25 = var13 + (var23 - 1) * var21;
                                        var26 = var19 + var24;
                                        var27 = var16 + (var23 - 1) * var22;

                                        if (var24 < 0 && !this.worldServerInstance.getBlockState(new BlockPos(var25, var26, var27)).getBlock().getMaterial().isSolid() || var24 >= 0 && !this.worldServerInstance.isAirBlock(new BlockPos(var25, var26, var27)))
                                        {
                                            continue label219;
                                        }
                                    }
                                }

                                var32 = var19 + 0.5D - p_85188_1_.posY;
                                var33 = var14 * var14 + var32 * var32 + var17 * var17;

                                if (var3 < 0.0D || var33 < var3)
                                {
                                    var3 = var33;
                                    var8 = var13;
                                    var9 = var19;
                                    var10 = var16;
                                    var11 = var20 % 2;
                                }
                            }
                        }
                    }
                }
            }
        }

        int var29 = var8;
        int var15 = var9;
        var16 = var10;
        int var30 = var11 % 2;
        int var18 = 1 - var30;

        if (var11 % 4 >= 2)
        {
            var30 = -var30;
            var18 = -var18;
        }

        if (var3 < 0.0D)
        {
            var9 = MathHelper.clamp_int(var9, 70, this.worldServerInstance.getActualHeight() - 10);
            var15 = var9;

            for (var19 = -1; var19 <= 1; ++var19)
            {
                for (var20 = 1; var20 < 3; ++var20)
                {
                    for (var21 = -1; var21 < 3; ++var21)
                    {
                        var22 = var29 + (var20 - 1) * var30 + var19 * var18;
                        var23 = var15 + var21;
                        var24 = var16 + (var20 - 1) * var18 - var19 * var30;
                        boolean var34 = var21 < 0;
                        this.worldServerInstance.setBlockState(new BlockPos(var22, var23, var24), var34 ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
                    }
                }
            }
        }

        IBlockState var31 = Blocks.portal.getDefaultState().withProperty(BlockPortal.AXIS, var30 != 0 ? EnumFacing.Axis.X : EnumFacing.Axis.Z);

        for (var20 = 0; var20 < 4; ++var20)
        {
            for (var21 = 0; var21 < 4; ++var21)
            {
                for (var22 = -1; var22 < 4; ++var22)
                {
                    var23 = var29 + (var21 - 1) * var30;
                    var24 = var15 + var22;
                    var25 = var16 + (var21 - 1) * var18;
                    boolean var35 = var21 == 0 || var21 == 3 || var22 == -1 || var22 == 3;
                    this.worldServerInstance.setBlockState(new BlockPos(var23, var24, var25), var35 ? Blocks.obsidian.getDefaultState() : var31, 2);
                }
            }

            for (var21 = 0; var21 < 4; ++var21)
            {
                for (var22 = -1; var22 < 4; ++var22)
                {
                    var23 = var29 + (var21 - 1) * var30;
                    var24 = var15 + var22;
                    var25 = var16 + (var21 - 1) * var18;
                    this.worldServerInstance.notifyNeighborsOfStateChange(new BlockPos(var23, var24, var25), this.worldServerInstance.getBlockState(new BlockPos(var23, var24, var25)).getBlock());
                }
            }
        }

        return true;
    }

    /**
     * called periodically to remove out-of-date portal locations from the cache list. Argument par1 is a
     * WorldServer.getTotalWorldTime() value.
     */
    public void removeStalePortalLocations(long worldTime)
    {
        if (worldTime % 100L == 0L)
        {
            Iterator var3 = this.destinationCoordinateKeys.iterator();
            long var4 = worldTime - 600L;

            while (var3.hasNext())
            {
                Long var6 = (Long)var3.next();
                Teleporter.PortalPosition var7 = (Teleporter.PortalPosition)this.destinationCoordinateCache.getValueByKey(var6.longValue());

                if (var7 == null || var7.lastUpdateTime < var4)
                {
                    var3.remove();
                    this.destinationCoordinateCache.remove(var6.longValue());
                }
            }
        }
    }

    public class PortalPosition extends BlockPos
    {
        public long lastUpdateTime;

        public PortalPosition(BlockPos pos, long lastUpdate)
        {
            super(pos.getX(), pos.getY(), pos.getZ());
            this.lastUpdateTime = lastUpdate;
        }
    }
}
