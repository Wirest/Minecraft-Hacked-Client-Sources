package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBanner extends BlockContainer
{
    public static final PropertyDirection FACING_PROP = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyInteger ROTATION_PROP = PropertyInteger.create("rotation", 0, 15);
    private static final String __OBFID = "CL_00002143";

    protected BlockBanner()
    {
        super(Material.wood);
        float var1 = 0.25F;
        float var2 = 1.0F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var2, 0.5F + var1);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    public boolean isFullCube()
    {
        return false;
    }

    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos)
    {
        return true;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityBanner();
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.banner;
    }

    public Item getItem(World worldIn, BlockPos pos)
    {
        return Items.banner;
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     *  
     * @param chance The chance that each Item is actually spawned (1.0 = always, 0.0 = never)
     * @param fortune The player's fortune level
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        TileEntity var6 = worldIn.getTileEntity(pos);

        if (var6 instanceof TileEntityBanner)
        {
            ItemStack var7 = new ItemStack(Items.banner, 1, ((TileEntityBanner)var6).getBaseColor());
            NBTTagCompound var8 = new NBTTagCompound();
            var6.writeToNBT(var8);
            var8.removeTag("x");
            var8.removeTag("y");
            var8.removeTag("z");
            var8.removeTag("id");
            var7.setTagInfo("BlockEntityTag", var8);
            spawnAsEntity(worldIn, pos, var7);
        }
        else
        {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        }
    }

    public void harvestBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, IBlockState state, TileEntity te)
    {
        if (te instanceof TileEntityBanner)
        {
            ItemStack var6 = new ItemStack(Items.banner, 1, ((TileEntityBanner)te).getBaseColor());
            NBTTagCompound var7 = new NBTTagCompound();
            te.writeToNBT(var7);
            var7.removeTag("x");
            var7.removeTag("y");
            var7.removeTag("z");
            var7.removeTag("id");
            var6.setTagInfo("BlockEntityTag", var7);
            spawnAsEntity(worldIn, pos, var6);
        }
        else
        {
            super.harvestBlock(worldIn, playerIn, pos, state, (TileEntity)null);
        }
    }

    public static class BlockBannerHanging extends BlockBanner
    {
        private static final String __OBFID = "CL_00002140";

        public BlockBannerHanging()
        {
            this.setDefaultState(this.blockState.getBaseState().withProperty(FACING_PROP, EnumFacing.NORTH));
        }

        public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos)
        {
            EnumFacing var3 = (EnumFacing)access.getBlockState(pos).getValue(FACING_PROP);
            float var4 = 0.0F;
            float var5 = 0.78125F;
            float var6 = 0.0F;
            float var7 = 1.0F;
            float var8 = 0.125F;
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

            switch (BlockBanner.SwitchEnumFacing.SWITCH_MAP[var3.ordinal()])
            {
                case 1:
                default:
                    this.setBlockBounds(var6, var4, 1.0F - var8, var7, var5, 1.0F);
                    break;

                case 2:
                    this.setBlockBounds(var6, var4, 0.0F, var7, var5, var8);
                    break;

                case 3:
                    this.setBlockBounds(1.0F - var8, var4, var6, 1.0F, var5, var7);
                    break;

                case 4:
                    this.setBlockBounds(0.0F, var4, var6, var8, var5, var7);
            }
        }

        public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
        {
            EnumFacing var5 = (EnumFacing)state.getValue(FACING_PROP);

            if (!worldIn.getBlockState(pos.offset(var5.getOpposite())).getBlock().getMaterial().isSolid())
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }

            super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        }

        public IBlockState getStateFromMeta(int meta)
        {
            EnumFacing var2 = EnumFacing.getFront(meta);

            if (var2.getAxis() == EnumFacing.Axis.Y)
            {
                var2 = EnumFacing.NORTH;
            }

            return this.getDefaultState().withProperty(FACING_PROP, var2);
        }

        public int getMetaFromState(IBlockState state)
        {
            return ((EnumFacing)state.getValue(FACING_PROP)).getIndex();
        }

        protected BlockState createBlockState()
        {
            return new BlockState(this, new IProperty[] {FACING_PROP});
        }
    }

    public static class BlockBannerStanding extends BlockBanner
    {
        private static final String __OBFID = "CL_00002141";

        public BlockBannerStanding()
        {
            this.setDefaultState(this.blockState.getBaseState().withProperty(ROTATION_PROP, Integer.valueOf(0)));
        }

        public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
        {
            if (!worldIn.getBlockState(pos.offsetDown()).getBlock().getMaterial().isSolid())
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }

            super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        }

        public IBlockState getStateFromMeta(int meta)
        {
            return this.getDefaultState().withProperty(ROTATION_PROP, Integer.valueOf(meta));
        }

        public int getMetaFromState(IBlockState state)
        {
            return ((Integer)state.getValue(ROTATION_PROP)).intValue();
        }

        protected BlockState createBlockState()
        {
            return new BlockState(this, new IProperty[] {ROTATION_PROP});
        }
    }

    static final class SwitchEnumFacing
    {
        static final int[] SWITCH_MAP = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002142";

        static
        {
            try
            {
                SWITCH_MAP[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                SWITCH_MAP[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                SWITCH_MAP[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                SWITCH_MAP[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
