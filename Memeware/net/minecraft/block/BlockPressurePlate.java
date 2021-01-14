package net.minecraft.block;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockPressurePlate extends BlockBasePressurePlate {
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    private final BlockPressurePlate.Sensitivity sensitivity;
    private static final String __OBFID = "CL_00000289";

    protected BlockPressurePlate(Material p_i45693_1_, BlockPressurePlate.Sensitivity p_i45693_2_) {
        super(p_i45693_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.valueOf(false)));
        this.sensitivity = p_i45693_2_;
    }

    protected int getRedstoneStrength(IBlockState p_176576_1_) {
        return ((Boolean) p_176576_1_.getValue(POWERED)).booleanValue() ? 15 : 0;
    }

    protected IBlockState setRedstoneStrength(IBlockState p_176575_1_, int p_176575_2_) {
        return p_176575_1_.withProperty(POWERED, Boolean.valueOf(p_176575_2_ > 0));
    }

    protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
        AxisAlignedBB var3 = this.getSensitiveAABB(pos);
        List var4;

        switch (BlockPressurePlate.SwitchSensitivity.SENSITIVITY_ARRAY[this.sensitivity.ordinal()]) {
            case 1:
                var4 = worldIn.getEntitiesWithinAABBExcludingEntity((Entity) null, var3);
                break;

            case 2:
                var4 = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, var3);
                break;

            default:
                return 0;
        }

        if (!var4.isEmpty()) {
            Iterator var5 = var4.iterator();

            while (var5.hasNext()) {
                Entity var6 = (Entity) var5.next();

                if (!var6.doesEntityNotTriggerPressurePlate()) {
                    return 15;
                }
            }
        }

        return 0;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(POWERED, Boolean.valueOf(meta == 1));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return ((Boolean) state.getValue(POWERED)).booleanValue() ? 1 : 0;
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{POWERED});
    }

    public static enum Sensitivity {
        EVERYTHING("EVERYTHING", 0),
        MOBS("MOBS", 1);

        private static final BlockPressurePlate.Sensitivity[] $VALUES = new BlockPressurePlate.Sensitivity[]{EVERYTHING, MOBS};
        private static final String __OBFID = "CL_00000290";

        private Sensitivity(String p_i45417_1_, int p_i45417_2_) {
        }
    }

    static final class SwitchSensitivity {
        static final int[] SENSITIVITY_ARRAY = new int[BlockPressurePlate.Sensitivity.values().length];
        private static final String __OBFID = "CL_00002078";

        static {
            try {
                SENSITIVITY_ARRAY[BlockPressurePlate.Sensitivity.EVERYTHING.ordinal()] = 1;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                SENSITIVITY_ARRAY[BlockPressurePlate.Sensitivity.MOBS.ordinal()] = 2;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
