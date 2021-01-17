// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.chunk;

import java.util.Random;
import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EmptyChunk extends Chunk
{
    public EmptyChunk(final World worldIn, final int x, final int z) {
        super(worldIn, x, z);
    }
    
    @Override
    public boolean isAtLocation(final int x, final int z) {
        return x == this.xPosition && z == this.zPosition;
    }
    
    @Override
    public int getHeightValue(final int x, final int z) {
        return 0;
    }
    
    public void generateHeightMap() {
    }
    
    @Override
    public void generateSkylightMap() {
    }
    
    @Override
    public Block getBlock(final BlockPos pos) {
        return Blocks.air;
    }
    
    @Override
    public int getBlockLightOpacity(final BlockPos pos) {
        return 255;
    }
    
    @Override
    public int getBlockMetadata(final BlockPos pos) {
        return 0;
    }
    
    @Override
    public int getLightFor(final EnumSkyBlock p_177413_1_, final BlockPos pos) {
        return p_177413_1_.defaultLightValue;
    }
    
    @Override
    public void setLightFor(final EnumSkyBlock p_177431_1_, final BlockPos pos, final int value) {
    }
    
    @Override
    public int getLightSubtracted(final BlockPos pos, final int amount) {
        return 0;
    }
    
    @Override
    public void addEntity(final Entity entityIn) {
    }
    
    @Override
    public void removeEntity(final Entity entityIn) {
    }
    
    @Override
    public void removeEntityAtIndex(final Entity entityIn, final int p_76608_2_) {
    }
    
    @Override
    public boolean canSeeSky(final BlockPos pos) {
        return false;
    }
    
    @Override
    public TileEntity getTileEntity(final BlockPos pos, final EnumCreateEntityType p_177424_2_) {
        return null;
    }
    
    @Override
    public void addTileEntity(final TileEntity tileEntityIn) {
    }
    
    @Override
    public void addTileEntity(final BlockPos pos, final TileEntity tileEntityIn) {
    }
    
    @Override
    public void removeTileEntity(final BlockPos pos) {
    }
    
    @Override
    public void onChunkLoad() {
    }
    
    @Override
    public void onChunkUnload() {
    }
    
    @Override
    public void setChunkModified() {
    }
    
    @Override
    public void getEntitiesWithinAABBForEntity(final Entity entityIn, final AxisAlignedBB aabb, final List<Entity> listToFill, final Predicate<? super Entity> p_177414_4_) {
    }
    
    @Override
    public <T extends Entity> void getEntitiesOfTypeWithinAAAB(final Class<? extends T> entityClass, final AxisAlignedBB aabb, final List<T> listToFill, final Predicate<? super T> p_177430_4_) {
    }
    
    @Override
    public boolean needsSaving(final boolean p_76601_1_) {
        return false;
    }
    
    @Override
    public Random getRandomWithSeed(final long seed) {
        return new Random(this.getWorld().getSeed() + this.xPosition * this.xPosition * 4987142 + this.xPosition * 5947611 + this.zPosition * this.zPosition * 4392871L + this.zPosition * 389711 ^ seed);
    }
    
    @Override
    public boolean isEmpty() {
        return true;
    }
    
    @Override
    public boolean getAreLevelsEmpty(final int startY, final int endY) {
        return true;
    }
}
