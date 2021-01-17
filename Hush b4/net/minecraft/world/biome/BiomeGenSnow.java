// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenIcePath;
import net.minecraft.world.gen.feature.WorldGenIceSpike;

public class BiomeGenSnow extends BiomeGenBase
{
    private boolean field_150615_aC;
    private WorldGenIceSpike field_150616_aD;
    private WorldGenIcePath field_150617_aE;
    
    public BiomeGenSnow(final int p_i45378_1_, final boolean p_i45378_2_) {
        super(p_i45378_1_);
        this.field_150616_aD = new WorldGenIceSpike();
        this.field_150617_aE = new WorldGenIcePath(4);
        this.field_150615_aC = p_i45378_2_;
        if (p_i45378_2_) {
            this.topBlock = Blocks.snow.getDefaultState();
        }
        this.spawnableCreatureList.clear();
    }
    
    @Override
    public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
        if (this.field_150615_aC) {
            for (int i = 0; i < 3; ++i) {
                final int j = rand.nextInt(16) + 8;
                final int k = rand.nextInt(16) + 8;
                this.field_150616_aD.generate(worldIn, rand, worldIn.getHeight(pos.add(j, 0, k)));
            }
            for (int l = 0; l < 2; ++l) {
                final int i2 = rand.nextInt(16) + 8;
                final int j2 = rand.nextInt(16) + 8;
                this.field_150617_aE.generate(worldIn, rand, worldIn.getHeight(pos.add(i2, 0, j2)));
            }
        }
        super.decorate(worldIn, rand, pos);
    }
    
    @Override
    public WorldGenAbstractTree genBigTreeChance(final Random rand) {
        return new WorldGenTaiga2(false);
    }
    
    @Override
    protected BiomeGenBase createMutatedBiome(final int p_180277_1_) {
        final BiomeGenBase biomegenbase = new BiomeGenSnow(p_180277_1_, true).func_150557_a(13828095, true).setBiomeName(String.valueOf(this.biomeName) + " Spikes").setEnableSnow().setTemperatureRainfall(0.0f, 0.5f).setHeight(new Height(this.minHeight + 0.1f, this.maxHeight + 0.1f));
        biomegenbase.minHeight = this.minHeight + 0.3f;
        biomegenbase.maxHeight = this.maxHeight + 0.4f;
        return biomegenbase;
    }
}
