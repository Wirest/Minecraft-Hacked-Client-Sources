package net.minecraft.world;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public class ChunkCache implements IBlockAccess {
    protected int chunkX;
    protected int chunkZ;
    protected Chunk[][] chunkArray;

    /**
     * set by !chunk.getAreLevelsEmpty
     */
    protected boolean hasExtendedLevels;

    /**
     * Reference to the World object.
     */
    protected World worldObj;
    private static final String __OBFID = "CL_00000155";

    public ChunkCache(World worldIn, BlockPos p_i45746_2_, BlockPos p_i45746_3_, int p_i45746_4_) {
        worldObj = worldIn;
        chunkX = p_i45746_2_.getX() - p_i45746_4_ >> 4;
        chunkZ = p_i45746_2_.getZ() - p_i45746_4_ >> 4;
        int var5 = p_i45746_3_.getX() + p_i45746_4_ >> 4;
        int var6 = p_i45746_3_.getZ() + p_i45746_4_ >> 4;
        chunkArray = new Chunk[var5 - chunkX + 1][var6 - chunkZ + 1];
        hasExtendedLevels = true;
        int var7;
        int var8;

        for (var7 = chunkX; var7 <= var5; ++var7) {
            for (var8 = chunkZ; var8 <= var6; ++var8) {
                chunkArray[var7 - chunkX][var8 - chunkZ] = worldIn.getChunkFromChunkCoords(var7, var8);
            }
        }

        for (var7 = p_i45746_2_.getX() >> 4; var7 <= p_i45746_3_.getX() >> 4; ++var7) {
            for (var8 = p_i45746_2_.getZ() >> 4; var8 <= p_i45746_3_.getZ() >> 4; ++var8) {
                Chunk var9 = chunkArray[var7 - chunkX][var8 - chunkZ];

                if (var9 != null && !var9.getAreLevelsEmpty(p_i45746_2_.getY(), p_i45746_3_.getY())) {
                    hasExtendedLevels = false;
                }
            }
        }
    }

    /**
     * set by !chunk.getAreLevelsEmpty
     */
    @Override
    public boolean extendedLevelsInChunkCache() {
        return hasExtendedLevels;
    }

    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        int var2 = (pos.getX() >> 4) - chunkX;
        int var3 = (pos.getZ() >> 4) - chunkZ;
        return chunkArray[var2][var3].func_177424_a(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
    }

    @Override
    public int getCombinedLight(BlockPos p_175626_1_, int p_175626_2_) {
        int var3 = func_175629_a(EnumSkyBlock.SKY, p_175626_1_);
        int var4 = func_175629_a(EnumSkyBlock.BLOCK, p_175626_1_);

        if (var4 < p_175626_2_) {
            var4 = p_175626_2_;
        }

        return var3 << 20 | var4 << 4;
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        if (pos.getY() >= 0 && pos.getY() < 256) {
            int var2 = (pos.getX() >> 4) - chunkX;
            int var3 = (pos.getZ() >> 4) - chunkZ;

            if (var2 >= 0 && var2 < chunkArray.length && var3 >= 0 && var3 < chunkArray[var2].length) {
                Chunk var4 = chunkArray[var2][var3];

                if (var4 != null) {
                    return var4.getBlockState(pos);
                }
            }
        }

        return Blocks.air.getDefaultState();
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(BlockPos pos) {
        return worldObj.getBiomeGenForCoords(pos);
    }

    private int func_175629_a(EnumSkyBlock p_175629_1_, BlockPos p_175629_2_) {
        if (p_175629_1_ == EnumSkyBlock.SKY && worldObj.provider.getHasNoSky()) {
            return 0;
        } else if (p_175629_2_.getY() >= 0 && p_175629_2_.getY() < 256) {
            int var3;

            if (getBlockState(p_175629_2_).getBlock().getUseNeighborBrightness()) {
                var3 = 0;
                EnumFacing[] var9 = EnumFacing.values();
                int var5 = var9.length;

                for (int var6 = 0; var6 < var5; ++var6) {
                    EnumFacing var7 = var9[var6];
                    int var8 = func_175628_b(p_175629_1_, p_175629_2_.offset(var7));

                    if (var8 > var3) {
                        var3 = var8;
                    }

                    if (var3 >= 15) {
                        return var3;
                    }
                }

                return var3;
            } else {
                var3 = (p_175629_2_.getX() >> 4) - chunkX;
                int var4 = (p_175629_2_.getZ() >> 4) - chunkZ;
                return chunkArray[var3][var4].getLightFor(p_175629_1_, p_175629_2_);
            }
        } else {
            return p_175629_1_.defaultLightValue;
        }
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        return getBlockState(pos).getBlock().getMaterial() == Material.air;
    }

    public int func_175628_b(EnumSkyBlock p_175628_1_, BlockPos p_175628_2_) {
        if (p_175628_2_.getY() >= 0 && p_175628_2_.getY() < 256) {
            int var3 = (p_175628_2_.getX() >> 4) - chunkX;
            int var4 = (p_175628_2_.getZ() >> 4) - chunkZ;
            return chunkArray[var3][var4].getLightFor(p_175628_1_, p_175628_2_);
        } else {
            return p_175628_1_.defaultLightValue;
        }
    }

    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction) {
        IBlockState var3 = getBlockState(pos);
        return var3.getBlock().isProvidingStrongPower(this, pos, var3, direction);
    }

    @Override
    public WorldType getWorldType() {
        return worldObj.getWorldType();
    }
}
