// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.BlockBush;
import net.minecraft.world.gen.feature.WorldGenerator;

public class GeneratorBushFeature extends WorldGenerator
{
    private BlockBush field_175908_a;
    
    public GeneratorBushFeature(final BlockBush p_i45633_1_) {
        this.field_175908_a = p_i45633_1_;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        for (int i = 0; i < 64; ++i) {
            final BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.getHasNoSky() || blockpos.getY() < 255) && this.field_175908_a.canBlockStay(worldIn, blockpos, this.field_175908_a.getDefaultState())) {
                worldIn.setBlockState(blockpos, this.field_175908_a.getDefaultState(), 2);
            }
        }
        return true;
    }
}
