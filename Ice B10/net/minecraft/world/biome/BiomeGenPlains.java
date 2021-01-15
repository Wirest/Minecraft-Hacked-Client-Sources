package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BiomeGenPlains extends BiomeGenBase
{
    protected boolean field_150628_aC;
    private static final String __OBFID = "CL_00000180";

    protected BiomeGenPlains(int p_i1986_1_)
    {
        super(p_i1986_1_);
        this.setTemperatureRainfall(0.8F, 0.4F);
        this.setHeight(height_LowPlains);
        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityHorse.class, 5, 2, 6));
        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.flowersPerChunk = 4;
        this.theBiomeDecorator.grassPerChunk = 10;
    }

    public BlockFlower.EnumFlowerType pickRandomFlower(Random p_180623_1_, BlockPos p_180623_2_)
    {
        double var3 = field_180281_af.func_151601_a((double)p_180623_2_.getX() / 200.0D, (double)p_180623_2_.getZ() / 200.0D);
        int var5;

        if (var3 < -0.8D)
        {
            var5 = p_180623_1_.nextInt(4);

            switch (var5)
            {
                case 0:
                    return BlockFlower.EnumFlowerType.ORANGE_TULIP;

                case 1:
                    return BlockFlower.EnumFlowerType.RED_TULIP;

                case 2:
                    return BlockFlower.EnumFlowerType.PINK_TULIP;

                case 3:
                default:
                    return BlockFlower.EnumFlowerType.WHITE_TULIP;
            }
        }
        else if (p_180623_1_.nextInt(3) > 0)
        {
            var5 = p_180623_1_.nextInt(3);
            return var5 == 0 ? BlockFlower.EnumFlowerType.POPPY : (var5 == 1 ? BlockFlower.EnumFlowerType.HOUSTONIA : BlockFlower.EnumFlowerType.OXEYE_DAISY);
        }
        else
        {
            return BlockFlower.EnumFlowerType.DANDELION;
        }
    }

    public void func_180624_a(World worldIn, Random p_180624_2_, BlockPos p_180624_3_)
    {
        double var4 = field_180281_af.func_151601_a((double)(p_180624_3_.getX() + 8) / 200.0D, (double)(p_180624_3_.getZ() + 8) / 200.0D);
        int var6;
        int var7;
        int var8;
        int var9;

        if (var4 < -0.8D)
        {
            this.theBiomeDecorator.flowersPerChunk = 15;
            this.theBiomeDecorator.grassPerChunk = 5;
        }
        else
        {
            this.theBiomeDecorator.flowersPerChunk = 4;
            this.theBiomeDecorator.grassPerChunk = 10;
            field_180280_ag.func_180710_a(BlockDoublePlant.EnumPlantType.GRASS);

            for (var6 = 0; var6 < 7; ++var6)
            {
                var7 = p_180624_2_.nextInt(16) + 8;
                var8 = p_180624_2_.nextInt(16) + 8;
                var9 = p_180624_2_.nextInt(worldIn.getHorizon(p_180624_3_.add(var7, 0, var8)).getY() + 32);
                field_180280_ag.generate(worldIn, p_180624_2_, p_180624_3_.add(var7, var9, var8));
            }
        }

        if (this.field_150628_aC)
        {
            field_180280_ag.func_180710_a(BlockDoublePlant.EnumPlantType.SUNFLOWER);

            for (var6 = 0; var6 < 10; ++var6)
            {
                var7 = p_180624_2_.nextInt(16) + 8;
                var8 = p_180624_2_.nextInt(16) + 8;
                var9 = p_180624_2_.nextInt(worldIn.getHorizon(p_180624_3_.add(var7, 0, var8)).getY() + 32);
                field_180280_ag.generate(worldIn, p_180624_2_, p_180624_3_.add(var7, var9, var8));
            }
        }

        super.func_180624_a(worldIn, p_180624_2_, p_180624_3_);
    }

    protected BiomeGenBase createMutatedBiome(int p_180277_1_)
    {
        BiomeGenPlains var2 = new BiomeGenPlains(p_180277_1_);
        var2.setBiomeName("Sunflower Plains");
        var2.field_150628_aC = true;
        var2.setColor(9286496);
        var2.field_150609_ah = 14273354;
        return var2;
    }
}
