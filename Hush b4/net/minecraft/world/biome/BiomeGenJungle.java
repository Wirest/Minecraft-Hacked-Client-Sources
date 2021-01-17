// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenMelon;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockOldLog;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;

public class BiomeGenJungle extends BiomeGenBase
{
    private boolean field_150614_aC;
    private static final IBlockState field_181620_aE;
    private static final IBlockState field_181621_aF;
    private static final IBlockState field_181622_aG;
    
    static {
        field_181620_aE = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
        field_181621_aF = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, false);
        field_181622_aG = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, false);
    }
    
    public BiomeGenJungle(final int p_i45379_1_, final boolean p_i45379_2_) {
        super(p_i45379_1_);
        this.field_150614_aC = p_i45379_2_;
        if (p_i45379_2_) {
            this.theBiomeDecorator.treesPerChunk = 2;
        }
        else {
            this.theBiomeDecorator.treesPerChunk = 50;
        }
        this.theBiomeDecorator.grassPerChunk = 25;
        this.theBiomeDecorator.flowersPerChunk = 4;
        if (!p_i45379_2_) {
            this.spawnableMonsterList.add(new SpawnListEntry(EntityOcelot.class, 2, 1, 1));
        }
        this.spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10, 4, 4));
    }
    
    @Override
    public WorldGenAbstractTree genBigTreeChance(final Random rand) {
        return (rand.nextInt(10) == 0) ? this.worldGeneratorBigTree : ((rand.nextInt(2) == 0) ? new WorldGenShrub(BiomeGenJungle.field_181620_aE, BiomeGenJungle.field_181622_aG) : ((!this.field_150614_aC && rand.nextInt(3) == 0) ? new WorldGenMegaJungle(false, 10, 20, BiomeGenJungle.field_181620_aE, BiomeGenJungle.field_181621_aF) : new WorldGenTrees(false, 4 + rand.nextInt(7), BiomeGenJungle.field_181620_aE, BiomeGenJungle.field_181621_aF, true)));
    }
    
    @Override
    public WorldGenerator getRandomWorldGenForGrass(final Random rand) {
        return (rand.nextInt(4) == 0) ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }
    
    @Override
    public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
        super.decorate(worldIn, rand, pos);
        final int i = rand.nextInt(16) + 8;
        int j = rand.nextInt(16) + 8;
        int k = rand.nextInt(worldIn.getHeight(pos.add(i, 0, j)).getY() * 2);
        new WorldGenMelon().generate(worldIn, rand, pos.add(i, k, j));
        final WorldGenVines worldgenvines = new WorldGenVines();
        for (j = 0; j < 50; ++j) {
            k = rand.nextInt(16) + 8;
            final int l = 128;
            final int i2 = rand.nextInt(16) + 8;
            worldgenvines.generate(worldIn, rand, pos.add(k, 128, i2));
        }
    }
}
