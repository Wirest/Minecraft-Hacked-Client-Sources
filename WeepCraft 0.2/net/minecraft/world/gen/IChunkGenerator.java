package net.minecraft.world.gen;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public interface IChunkGenerator
{
    Chunk provideChunk(int x, int z);

    void populate(int x, int z);

    boolean generateStructures(Chunk chunkIn, int x, int z);

    List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos);

    @Nullable
    BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position, boolean p_180513_4_);

    void recreateStructures(Chunk chunkIn, int x, int z);

    boolean func_193414_a(World p_193414_1_, String p_193414_2_, BlockPos p_193414_3_);
}
