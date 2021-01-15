package net.minecraft.world;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public class ChunkCache implements IBlockAccess
{
    protected int chunkX;
    protected int chunkZ;
    protected Chunk[][] chunkArray;

    /** set by !chunk.getAreLevelsEmpty */
    protected boolean hasExtendedLevels;

    /** Reference to the World object. */
    protected World worldObj;

    public ChunkCache(World worldIn, BlockPos posFromIn, BlockPos posToIn, int subIn)
    {
        this.worldObj = worldIn;
        this.chunkX = posFromIn.getX() - subIn >> 4;
        this.chunkZ = posFromIn.getZ() - subIn >> 4;
        int var5 = posToIn.getX() + subIn >> 4;
        int var6 = posToIn.getZ() + subIn >> 4;
        this.chunkArray = new Chunk[var5 - this.chunkX + 1][var6 - this.chunkZ + 1];
        this.hasExtendedLevels = true;
        int var7;
        int var8;

        for (var7 = this.chunkX; var7 <= var5; ++var7)
        {
            for (var8 = this.chunkZ; var8 <= var6; ++var8)
            {
                this.chunkArray[var7 - this.chunkX][var8 - this.chunkZ] = worldIn.getChunkFromChunkCoords(var7, var8);
            }
        }

        for (var7 = posFromIn.getX() >> 4; var7 <= posToIn.getX() >> 4; ++var7)
        {
            for (var8 = posFromIn.getZ() >> 4; var8 <= posToIn.getZ() >> 4; ++var8)
            {
                Chunk var9 = this.chunkArray[var7 - this.chunkX][var8 - this.chunkZ];

                if (var9 != null && !var9.getAreLevelsEmpty(posFromIn.getY(), posToIn.getY()))
                {
                    this.hasExtendedLevels = false;
                }
            }
        }
    }

    /**
     * set by !chunk.getAreLevelsEmpty
     */
    @Override
	public boolean extendedLevelsInChunkCache()
    {
        return this.hasExtendedLevels;
    }

    @Override
	public TileEntity getTileEntity(BlockPos pos)
    {
        int var2 = (pos.getX() >> 4) - this.chunkX;
        int var3 = (pos.getZ() >> 4) - this.chunkZ;
        return this.chunkArray[var2][var3].getTileEntity(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
    }

    @Override
	public int getCombinedLight(BlockPos pos, int lightValue)
    {
        int var3 = this.getLightForExt(EnumSkyBlock.SKY, pos);
        int var4 = this.getLightForExt(EnumSkyBlock.BLOCK, pos);

        if (var4 < lightValue)
        {
            var4 = lightValue;
        }

        return var3 << 20 | var4 << 4;
    }

    @Override
	public IBlockState getBlockState(BlockPos pos)
    {
        if (pos.getY() >= 0 && pos.getY() < 256)
        {
            int var2 = (pos.getX() >> 4) - this.chunkX;
            int var3 = (pos.getZ() >> 4) - this.chunkZ;

            if (var2 >= 0 && var2 < this.chunkArray.length && var3 >= 0 && var3 < this.chunkArray[var2].length)
            {
                Chunk var4 = this.chunkArray[var2][var3];

                if (var4 != null)
                {
                    return var4.getBlockState(pos);
                }
            }
        }

        return Blocks.air.getDefaultState();
    }

    @Override
	public BiomeGenBase getBiomeGenForCoords(BlockPos pos)
    {
        return this.worldObj.getBiomeGenForCoords(pos);
    }

    private int getLightForExt(EnumSkyBlock p_175629_1_, BlockPos pos)
    {
        if (p_175629_1_ == EnumSkyBlock.SKY && this.worldObj.provider.getHasNoSky())
        {
            return 0;
        }
        else if (pos.getY() >= 0 && pos.getY() < 256)
        {
            int var3;

            if (this.getBlockState(pos).getBlock().getUseNeighborBrightness())
            {
                var3 = 0;
                EnumFacing[] var9 = EnumFacing.values();
                int var5 = var9.length;

                for (int var6 = 0; var6 < var5; ++var6)
                {
                    EnumFacing var7 = var9[var6];
                    int var8 = this.getLightFor(p_175629_1_, pos.offset(var7));

                    if (var8 > var3)
                    {
                        var3 = var8;
                    }

                    if (var3 >= 15)
                    {
                        return var3;
                    }
                }

                return var3;
            }
            else
            {
                var3 = (pos.getX() >> 4) - this.chunkX;
                int var4 = (pos.getZ() >> 4) - this.chunkZ;
                return this.chunkArray[var3][var4].getLightFor(p_175629_1_, pos);
            }
        }
        else
        {
            return p_175629_1_.defaultLightValue;
        }
    }

    /**
     * Checks to see if an air block exists at the provided location. Note that this only checks to see if the blocks
     * material is set to air, meaning it is possible for non-vanilla blocks to still pass this check.
     *  
     * @param pos The position of the block being checked.
     */
    @Override
	public boolean isAirBlock(BlockPos pos)
    {
        return this.getBlockState(pos).getBlock().getMaterial() == Material.air;
    }

    public int getLightFor(EnumSkyBlock p_175628_1_, BlockPos pos)
    {
        if (pos.getY() >= 0 && pos.getY() < 256)
        {
            int var3 = (pos.getX() >> 4) - this.chunkX;
            int var4 = (pos.getZ() >> 4) - this.chunkZ;
            return this.chunkArray[var3][var4].getLightFor(p_175628_1_, pos);
        }
        else
        {
            return p_175628_1_.defaultLightValue;
        }
    }

    @Override
	public int getStrongPower(BlockPos pos, EnumFacing direction)
    {
        IBlockState var3 = this.getBlockState(pos);
        return var3.getBlock().isProvidingStrongPower(this, pos, var3, direction);
    }

    @Override
	public WorldType getWorldType()
    {
        return this.worldObj.getWorldType();
    }
}
