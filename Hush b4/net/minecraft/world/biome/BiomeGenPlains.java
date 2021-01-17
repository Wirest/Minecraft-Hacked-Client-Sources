// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.world.World;
import net.minecraft.block.BlockFlower;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityHorse;

public class BiomeGenPlains extends BiomeGenBase
{
    protected boolean field_150628_aC;
    
    protected BiomeGenPlains(final int p_i1986_1_) {
        super(p_i1986_1_);
        this.setTemperatureRainfall(0.8f, 0.4f);
        this.setHeight(BiomeGenPlains.height_LowPlains);
        this.spawnableCreatureList.add(new SpawnListEntry(EntityHorse.class, 5, 2, 6));
        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.flowersPerChunk = 4;
        this.theBiomeDecorator.grassPerChunk = 10;
    }
    
    @Override
    public BlockFlower.EnumFlowerType pickRandomFlower(final Random rand, final BlockPos pos) {
        final double d0 = BiomeGenPlains.GRASS_COLOR_NOISE.func_151601_a(pos.getX() / 200.0, pos.getZ() / 200.0);
        if (d0 < -0.8) {
            final int j = rand.nextInt(4);
            switch (j) {
                case 0: {
                    return BlockFlower.EnumFlowerType.ORANGE_TULIP;
                }
                case 1: {
                    return BlockFlower.EnumFlowerType.RED_TULIP;
                }
                case 2: {
                    return BlockFlower.EnumFlowerType.PINK_TULIP;
                }
                default: {
                    return BlockFlower.EnumFlowerType.WHITE_TULIP;
                }
            }
        }
        else {
            if (rand.nextInt(3) > 0) {
                final int i = rand.nextInt(3);
                return (i == 0) ? BlockFlower.EnumFlowerType.POPPY : ((i == 1) ? BlockFlower.EnumFlowerType.HOUSTONIA : BlockFlower.EnumFlowerType.OXEYE_DAISY);
            }
            return BlockFlower.EnumFlowerType.DANDELION;
        }
    }
    
    @Override
    public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
        final double d0 = BiomeGenPlains.GRASS_COLOR_NOISE.func_151601_a((pos.getX() + 8) / 200.0, (pos.getZ() + 8) / 200.0);
        if (d0 < -0.8) {
            this.theBiomeDecorator.flowersPerChunk = 15;
            this.theBiomeDecorator.grassPerChunk = 5;
        }
        else {
            this.theBiomeDecorator.flowersPerChunk = 4;
            this.theBiomeDecorator.grassPerChunk = 10;
            BiomeGenPlains.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);
            for (int i = 0; i < 7; ++i) {
                final int j = rand.nextInt(16) + 8;
                final int k = rand.nextInt(16) + 8;
                final int l = rand.nextInt(worldIn.getHeight(pos.add(j, 0, k)).getY() + 32);
                BiomeGenPlains.DOUBLE_PLANT_GENERATOR.generate(worldIn, rand, pos.add(j, l, k));
            }
        }
        if (this.field_150628_aC) {
            BiomeGenPlains.DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.SUNFLOWER);
            for (int i2 = 0; i2 < 10; ++i2) {
                final int j2 = rand.nextInt(16) + 8;
                final int k2 = rand.nextInt(16) + 8;
                final int l2 = rand.nextInt(worldIn.getHeight(pos.add(j2, 0, k2)).getY() + 32);
                BiomeGenPlains.DOUBLE_PLANT_GENERATOR.generate(worldIn, rand, pos.add(j2, l2, k2));
            }
        }
        super.decorate(worldIn, rand, pos);
    }
    
    @Override
    protected BiomeGenBase createMutatedBiome(final int p_180277_1_) {
        final BiomeGenPlains biomegenplains = new BiomeGenPlains(p_180277_1_);
        biomegenplains.setBiomeName("Sunflower Plains");
        biomegenplains.field_150628_aC = true;
        biomegenplains.setColor(9286496);
        biomegenplains.field_150609_ah = 14273354;
        return biomegenplains;
    }
}
