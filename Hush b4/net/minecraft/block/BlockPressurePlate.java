// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockPressurePlate extends BlockBasePressurePlate
{
    public static final PropertyBool POWERED;
    private final Sensitivity sensitivity;
    
    static {
        POWERED = PropertyBool.create("powered");
    }
    
    protected BlockPressurePlate(final Material materialIn, final Sensitivity sensitivityIn) {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockPressurePlate.POWERED, false));
        this.sensitivity = sensitivityIn;
    }
    
    @Override
    protected int getRedstoneStrength(final IBlockState state) {
        return state.getValue((IProperty<Boolean>)BlockPressurePlate.POWERED) ? 15 : 0;
    }
    
    @Override
    protected IBlockState setRedstoneStrength(final IBlockState state, final int strength) {
        return state.withProperty((IProperty<Comparable>)BlockPressurePlate.POWERED, strength > 0);
    }
    
    @Override
    protected int computeRedstoneStrength(final World worldIn, final BlockPos pos) {
        final AxisAlignedBB axisalignedbb = this.getSensitiveAABB(pos);
        List<? extends Entity> list = null;
        switch (this.sensitivity) {
            case EVERYTHING: {
                list = worldIn.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);
                break;
            }
            case MOBS: {
                list = worldIn.getEntitiesWithinAABB((Class<? extends Entity>)EntityLivingBase.class, axisalignedbb);
                break;
            }
            default: {
                return 0;
            }
        }
        if (!list.isEmpty()) {
            for (final Entity entity : list) {
                if (!entity.doesEntityNotTriggerPressurePlate()) {
                    return 15;
                }
            }
        }
        return 0;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockPressurePlate.POWERED, meta == 1);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((boolean)state.getValue((IProperty<Boolean>)BlockPressurePlate.POWERED)) ? 1 : 0;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockPressurePlate.POWERED });
    }
    
    public enum Sensitivity
    {
        EVERYTHING("EVERYTHING", 0), 
        MOBS("MOBS", 1);
        
        private Sensitivity(final String name, final int ordinal) {
        }
    }
}
