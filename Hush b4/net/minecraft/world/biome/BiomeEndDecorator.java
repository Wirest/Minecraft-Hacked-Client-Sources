// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.world.gen.feature.WorldGenSpikes;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeEndDecorator extends BiomeDecorator
{
    protected WorldGenerator spikeGen;
    
    public BiomeEndDecorator() {
        this.spikeGen = new WorldGenSpikes(Blocks.end_stone);
    }
    
    @Override
    protected void genDecorations(final BiomeGenBase biomeGenBaseIn) {
        this.generateOres();
        if (this.randomGenerator.nextInt(5) == 0) {
            final int i = this.randomGenerator.nextInt(16) + 8;
            final int j = this.randomGenerator.nextInt(16) + 8;
            this.spikeGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(i, 0, j)));
        }
        if (this.field_180294_c.getX() == 0 && this.field_180294_c.getZ() == 0) {
            final EntityDragon entitydragon = new EntityDragon(this.currentWorld);
            entitydragon.setLocationAndAngles(0.0, 128.0, 0.0, this.randomGenerator.nextFloat() * 360.0f, 0.0f);
            this.currentWorld.spawnEntityInWorld(entitydragon);
        }
    }
}
