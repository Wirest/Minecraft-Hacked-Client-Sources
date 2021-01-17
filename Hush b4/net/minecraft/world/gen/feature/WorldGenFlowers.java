// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.BlockFlower;

public class WorldGenFlowers extends WorldGenerator
{
    private BlockFlower flower;
    private IBlockState field_175915_b;
    
    public WorldGenFlowers(final BlockFlower p_i45632_1_, final BlockFlower.EnumFlowerType p_i45632_2_) {
        this.setGeneratedBlock(p_i45632_1_, p_i45632_2_);
    }
    
    public void setGeneratedBlock(final BlockFlower p_175914_1_, final BlockFlower.EnumFlowerType p_175914_2_) {
        this.flower = p_175914_1_;
        this.field_175915_b = p_175914_1_.getDefaultState().withProperty(p_175914_1_.getTypeProperty(), p_175914_2_);
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        for (int i = 0; i < 64; ++i) {
            final BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.getHasNoSky() || blockpos.getY() < 255) && this.flower.canBlockStay(worldIn, blockpos, this.field_175915_b)) {
                worldIn.setBlockState(blockpos, this.field_175915_b, 2);
            }
        }
        return true;
    }
}
