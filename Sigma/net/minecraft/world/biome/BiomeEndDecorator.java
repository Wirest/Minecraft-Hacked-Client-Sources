package net.minecraft.world.biome;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenSpikes;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeEndDecorator extends BiomeDecorator {
    protected WorldGenerator spikeGen;
    private static final String __OBFID = "CL_00000188";

    public BiomeEndDecorator() {
        this.spikeGen = new WorldGenSpikes(Blocks.end_stone);
    }

    protected void genDecorations(BiomeGenBase p_150513_1_) {
        this.generateOres();

        if (this.randomGenerator.nextInt(5) == 0) {
            int var2 = this.randomGenerator.nextInt(16) + 8;
            int var3 = this.randomGenerator.nextInt(16) + 8;
            this.spikeGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.func_175672_r(this.field_180294_c.add(var2, 0, var3)));
        }

        if (this.field_180294_c.getX() == 0 && this.field_180294_c.getZ() == 0) {
            EntityDragon var4 = new EntityDragon(this.currentWorld);
            var4.setLocationAndAngles(0.0D, 128.0D, 0.0D, this.randomGenerator.nextFloat() * 360.0F, 0.0F);
            this.currentWorld.spawnEntityInWorld(var4);
        }
    }
}
