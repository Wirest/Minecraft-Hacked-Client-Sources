// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import net.minecraft.util.BlockPos;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.Chunk;
import java.util.Iterator;
import net.minecraft.util.MathHelper;
import java.util.Collection;
import net.minecraft.block.Block;
import com.google.common.collect.Lists;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import java.util.List;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderDebug implements IChunkProvider
{
    private static final List<IBlockState> field_177464_a;
    private static final int field_177462_b;
    private static final int field_181039_c;
    private final World world;
    
    static {
        field_177464_a = Lists.newArrayList();
        for (final Block block : Block.blockRegistry) {
            ChunkProviderDebug.field_177464_a.addAll(block.getBlockState().getValidStates());
        }
        field_177462_b = MathHelper.ceiling_float_int(MathHelper.sqrt_float((float)ChunkProviderDebug.field_177464_a.size()));
        field_181039_c = MathHelper.ceiling_float_int(ChunkProviderDebug.field_177464_a.size() / (float)ChunkProviderDebug.field_177462_b);
    }
    
    public ChunkProviderDebug(final World worldIn) {
        this.world = worldIn;
    }
    
    @Override
    public Chunk provideChunk(final int x, final int z) {
        final ChunkPrimer chunkprimer = new ChunkPrimer();
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                final int k = x * 16 + i;
                final int l = z * 16 + j;
                chunkprimer.setBlockState(i, 60, j, Blocks.barrier.getDefaultState());
                final IBlockState iblockstate = func_177461_b(k, l);
                if (iblockstate != null) {
                    chunkprimer.setBlockState(i, 70, j, iblockstate);
                }
            }
        }
        final Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
        chunk.generateSkylightMap();
        final BiomeGenBase[] abiomegenbase = this.world.getWorldChunkManager().loadBlockGeneratorData(null, x * 16, z * 16, 16, 16);
        final byte[] abyte = chunk.getBiomeArray();
        for (int i2 = 0; i2 < abyte.length; ++i2) {
            abyte[i2] = (byte)abiomegenbase[i2].biomeID;
        }
        chunk.generateSkylightMap();
        return chunk;
    }
    
    public static IBlockState func_177461_b(int p_177461_0_, int p_177461_1_) {
        IBlockState iblockstate = null;
        if (p_177461_0_ > 0 && p_177461_1_ > 0 && p_177461_0_ % 2 != 0 && p_177461_1_ % 2 != 0) {
            p_177461_0_ /= 2;
            p_177461_1_ /= 2;
            if (p_177461_0_ <= ChunkProviderDebug.field_177462_b && p_177461_1_ <= ChunkProviderDebug.field_181039_c) {
                final int i = MathHelper.abs_int(p_177461_0_ * ChunkProviderDebug.field_177462_b + p_177461_1_);
                if (i < ChunkProviderDebug.field_177464_a.size()) {
                    iblockstate = ChunkProviderDebug.field_177464_a.get(i);
                }
            }
        }
        return iblockstate;
    }
    
    @Override
    public boolean chunkExists(final int x, final int z) {
        return true;
    }
    
    @Override
    public void populate(final IChunkProvider p_73153_1_, final int p_73153_2_, final int p_73153_3_) {
    }
    
    @Override
    public boolean func_177460_a(final IChunkProvider p_177460_1_, final Chunk p_177460_2_, final int p_177460_3_, final int p_177460_4_) {
        return false;
    }
    
    @Override
    public boolean saveChunks(final boolean p_73151_1_, final IProgressUpdate progressCallback) {
        return true;
    }
    
    @Override
    public void saveExtraData() {
    }
    
    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }
    
    @Override
    public boolean canSave() {
        return true;
    }
    
    @Override
    public String makeString() {
        return "DebugLevelSource";
    }
    
    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(final EnumCreatureType creatureType, final BlockPos pos) {
        final BiomeGenBase biomegenbase = this.world.getBiomeGenForCoords(pos);
        return biomegenbase.getSpawnableList(creatureType);
    }
    
    @Override
    public BlockPos getStrongholdGen(final World worldIn, final String structureName, final BlockPos position) {
        return null;
    }
    
    @Override
    public int getLoadedChunkCount() {
        return 0;
    }
    
    @Override
    public void recreateStructures(final Chunk p_180514_1_, final int p_180514_2_, final int p_180514_3_) {
    }
    
    @Override
    public Chunk provideChunk(final BlockPos blockPosIn) {
        return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
    }
}
