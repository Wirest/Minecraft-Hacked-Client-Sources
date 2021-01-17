// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockPressurePlateWeighted extends BlockBasePressurePlate
{
    public static final PropertyInteger POWER;
    private final int field_150068_a;
    
    static {
        POWER = PropertyInteger.create("power", 0, 15);
    }
    
    protected BlockPressurePlateWeighted(final Material p_i46379_1_, final int p_i46379_2_) {
        this(p_i46379_1_, p_i46379_2_, p_i46379_1_.getMaterialMapColor());
    }
    
    protected BlockPressurePlateWeighted(final Material p_i46380_1_, final int p_i46380_2_, final MapColor p_i46380_3_) {
        super(p_i46380_1_, p_i46380_3_);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockPressurePlateWeighted.POWER, 0));
        this.field_150068_a = p_i46380_2_;
    }
    
    @Override
    protected int computeRedstoneStrength(final World worldIn, final BlockPos pos) {
        final int i = Math.min(worldIn.getEntitiesWithinAABB((Class<? extends Entity>)Entity.class, this.getSensitiveAABB(pos)).size(), this.field_150068_a);
        if (i > 0) {
            final float f = Math.min(this.field_150068_a, i) / (float)this.field_150068_a;
            return MathHelper.ceiling_float_int(f * 15.0f);
        }
        return 0;
    }
    
    @Override
    protected int getRedstoneStrength(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockPressurePlateWeighted.POWER);
    }
    
    @Override
    protected IBlockState setRedstoneStrength(final IBlockState state, final int strength) {
        return state.withProperty((IProperty<Comparable>)BlockPressurePlateWeighted.POWER, strength);
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 10;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockPressurePlateWeighted.POWER, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockPressurePlateWeighted.POWER);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPressurePlateWeighted.POWER });
    }
}
