package net.minecraft.block;

import java.util.Iterator;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public abstract class BlockLog extends BlockRotatedPillar
{
    public static final PropertyEnum LOG_AXIS = PropertyEnum.create("axis", BlockLog.EnumAxis.class);

    public BlockLog()
    {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHardness(2.0F);
        this.setStepSound(soundTypeWood);
    }

    @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        byte var4 = 4;
        int var5 = var4 + 1;

        if (worldIn.isAreaLoaded(pos.add(-var5, -var5, -var5), pos.add(var5, var5, var5)))
        {
            Iterator var6 = BlockPos.getAllInBox(pos.add(-var4, -var4, -var4), pos.add(var4, var4, var4)).iterator();

            while (var6.hasNext())
            {
                BlockPos var7 = (BlockPos)var6.next();
                IBlockState var8 = worldIn.getBlockState(var7);

                if (var8.getBlock().getMaterial() == Material.leaves && !((Boolean)var8.getValue(BlockLeaves.CHECK_DECAY)).booleanValue())
                {
                    worldIn.setBlockState(var7, var8.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(true)), 4);
                }
            }
        }
    }

    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(LOG_AXIS, BlockLog.EnumAxis.fromFacingAxis(facing.getAxis()));
    }

    public static enum EnumAxis implements IStringSerializable
    {
        X("X", 0, "x"),
        Y("Y", 1, "y"),
        Z("Z", 2, "z"),
        NONE("NONE", 3, "none");
        private final String name; 

        private EnumAxis(String p_i45708_1_, int p_i45708_2_, String name)
        {
            this.name = name;
        }

        @Override
		public String toString()
        {
            return this.name;
        }

        public static BlockLog.EnumAxis fromFacingAxis(EnumFacing.Axis axis)
        {
            switch (BlockLog.SwitchAxis.AXIS_LOOKUP[axis.ordinal()])
            {
                case 1:
                    return X;

                case 2:
                    return Y;

                case 3:
                    return Z;

                default:
                    return NONE;
            }
        }

        @Override
		public String getName()
        {
            return this.name;
        }
    }

    static final class SwitchAxis
    {
        static final int[] AXIS_LOOKUP = new int[EnumFacing.Axis.values().length];

        static
        {
            try
            {
                AXIS_LOOKUP[EnumFacing.Axis.X.ordinal()] = 1;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                AXIS_LOOKUP[EnumFacing.Axis.Y.ordinal()] = 2;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                AXIS_LOOKUP[EnumFacing.Axis.Z.ordinal()] = 3;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
