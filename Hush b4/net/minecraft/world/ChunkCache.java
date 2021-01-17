// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class ChunkCache implements IBlockAccess
{
    protected int chunkX;
    protected int chunkZ;
    protected Chunk[][] chunkArray;
    protected boolean hasExtendedLevels;
    protected World worldObj;
    
    public ChunkCache(final World worldIn, final BlockPos posFromIn, final BlockPos posToIn, final int subIn) {
        this.worldObj = worldIn;
        this.chunkX = posFromIn.getX() - subIn >> 4;
        this.chunkZ = posFromIn.getZ() - subIn >> 4;
        final int i = posToIn.getX() + subIn >> 4;
        final int j = posToIn.getZ() + subIn >> 4;
        this.chunkArray = new Chunk[i - this.chunkX + 1][j - this.chunkZ + 1];
        this.hasExtendedLevels = true;
        for (int k = this.chunkX; k <= i; ++k) {
            for (int l = this.chunkZ; l <= j; ++l) {
                this.chunkArray[k - this.chunkX][l - this.chunkZ] = worldIn.getChunkFromChunkCoords(k, l);
            }
        }
        for (int i2 = posFromIn.getX() >> 4; i2 <= posToIn.getX() >> 4; ++i2) {
            for (int j2 = posFromIn.getZ() >> 4; j2 <= posToIn.getZ() >> 4; ++j2) {
                final Chunk chunk = this.chunkArray[i2 - this.chunkX][j2 - this.chunkZ];
                if (chunk != null && !chunk.getAreLevelsEmpty(posFromIn.getY(), posToIn.getY())) {
                    this.hasExtendedLevels = false;
                }
            }
        }
    }
    
    @Override
    public boolean extendedLevelsInChunkCache() {
        return this.hasExtendedLevels;
    }
    
    @Override
    public TileEntity getTileEntity(final BlockPos pos) {
        final int i = (pos.getX() >> 4) - this.chunkX;
        final int j = (pos.getZ() >> 4) - this.chunkZ;
        return this.chunkArray[i][j].getTileEntity(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
    }
    
    @Override
    public int getCombinedLight(final BlockPos pos, final int lightValue) {
        final int i = this.getLightForExt(EnumSkyBlock.SKY, pos);
        int j = this.getLightForExt(EnumSkyBlock.BLOCK, pos);
        if (j < lightValue) {
            j = lightValue;
        }
        return i << 20 | j << 4;
    }
    
    @Override
    public IBlockState getBlockState(final BlockPos pos) {
        if (pos.getY() >= 0 && pos.getY() < 256) {
            final int i = (pos.getX() >> 4) - this.chunkX;
            final int j = (pos.getZ() >> 4) - this.chunkZ;
            if (i >= 0 && i < this.chunkArray.length && j >= 0 && j < this.chunkArray[i].length) {
                final Chunk chunk = this.chunkArray[i][j];
                if (chunk != null) {
                    return chunk.getBlockState(pos);
                }
            }
        }
        return Blocks.air.getDefaultState();
    }
    
    @Override
    public BiomeGenBase getBiomeGenForCoords(final BlockPos pos) {
        return this.worldObj.getBiomeGenForCoords(pos);
    }
    
    private int getLightForExt(final EnumSkyBlock p_175629_1_, final BlockPos pos) {
        if (p_175629_1_ == EnumSkyBlock.SKY && this.worldObj.provider.getHasNoSky()) {
            return 0;
        }
        if (pos.getY() < 0 || pos.getY() >= 256) {
            return p_175629_1_.defaultLightValue;
        }
        if (this.getBlockState(pos).getBlock().getUseNeighborBrightness()) {
            int l = 0;
            EnumFacing[] values;
            for (int length = (values = EnumFacing.values()).length, n = 0; n < length; ++n) {
                final EnumFacing enumfacing = values[n];
                final int k = this.getLightFor(p_175629_1_, pos.offset(enumfacing));
                if (k > l) {
                    l = k;
                }
                if (l >= 15) {
                    return l;
                }
            }
            return l;
        }
        final int i = (pos.getX() >> 4) - this.chunkX;
        final int j = (pos.getZ() >> 4) - this.chunkZ;
        return this.chunkArray[i][j].getLightFor(p_175629_1_, pos);
    }
    
    @Override
    public boolean isAirBlock(final BlockPos pos) {
        return this.getBlockState(pos).getBlock().getMaterial() == Material.air;
    }
    
    public int getLightFor(final EnumSkyBlock p_175628_1_, final BlockPos pos) {
        if (pos.getY() >= 0 && pos.getY() < 256) {
            final int i = (pos.getX() >> 4) - this.chunkX;
            final int j = (pos.getZ() >> 4) - this.chunkZ;
            return this.chunkArray[i][j].getLightFor(p_175628_1_, pos);
        }
        return p_175628_1_.defaultLightValue;
    }
    
    @Override
    public int getStrongPower(final BlockPos pos, final EnumFacing direction) {
        final IBlockState iblockstate = this.getBlockState(pos);
        return iblockstate.getBlock().getStrongPower(this, pos, iblockstate, direction);
    }
    
    @Override
    public WorldType getWorldType() {
        return this.worldObj.getWorldType();
    }
}
