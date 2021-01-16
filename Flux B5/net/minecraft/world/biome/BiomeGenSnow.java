package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenIcePath;
import net.minecraft.world.gen.feature.WorldGenIceSpike;
import net.minecraft.world.gen.feature.WorldGenTaiga2;

public class BiomeGenSnow extends BiomeGenBase
{
    private boolean field_150615_aC;
    private WorldGenIceSpike field_150616_aD = new WorldGenIceSpike();
    private WorldGenIcePath field_150617_aE = new WorldGenIcePath(4);
    private static final String __OBFID = "CL_00000174";

    public BiomeGenSnow(int p_i45378_1_, boolean p_i45378_2_)
    {
        super(p_i45378_1_);
        this.field_150615_aC = p_i45378_2_;

        if (p_i45378_2_)
        {
            this.topBlock = Blocks.snow.getDefaultState();
        }

        this.spawnableCreatureList.clear();
    }

    public void func_180624_a(World worldIn, Random p_180624_2_, BlockPos p_180624_3_)
    {
        if (this.field_150615_aC)
        {
            int var4;
            int var5;
            int var6;

            for (var4 = 0; var4 < 3; ++var4)
            {
                var5 = p_180624_2_.nextInt(16) + 8;
                var6 = p_180624_2_.nextInt(16) + 8;
                this.field_150616_aD.generate(worldIn, p_180624_2_, worldIn.getHorizon(p_180624_3_.add(var5, 0, var6)));
            }

            for (var4 = 0; var4 < 2; ++var4)
            {
                var5 = p_180624_2_.nextInt(16) + 8;
                var6 = p_180624_2_.nextInt(16) + 8;
                this.field_150617_aE.generate(worldIn, p_180624_2_, worldIn.getHorizon(p_180624_3_.add(var5, 0, var6)));
            }
        }

        super.func_180624_a(worldIn, p_180624_2_, p_180624_3_);
    }

    public WorldGenAbstractTree genBigTreeChance(Random p_150567_1_)
    {
        return new WorldGenTaiga2(false);
    }

    protected BiomeGenBase createMutatedBiome(int p_180277_1_)
    {
        BiomeGenBase var2 = (new BiomeGenSnow(p_180277_1_, true)).func_150557_a(13828095, true).setBiomeName(this.biomeName + " Spikes").setEnableSnow().setTemperatureRainfall(0.0F, 0.5F).setHeight(new BiomeGenBase.Height(this.minHeight + 0.1F, this.maxHeight + 0.1F));
        var2.minHeight = this.minHeight + 0.3F;
        var2.maxHeight = this.maxHeight + 0.4F;
        return var2;
    }
}
