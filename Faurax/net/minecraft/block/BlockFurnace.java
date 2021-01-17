package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class BlockFurnace extends BlockContainer
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private final boolean isBurning;
    private static boolean field_149934_M;
    private static final String __OBFID = "CL_00000248";

    protected BlockFurnace(boolean p_i45407_1_)
    {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.isBurning = p_i45407_1_;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(Blocks.furnace);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        this.func_176445_e(worldIn, pos, state);
    }

    private void func_176445_e(World worldIn, BlockPos p_176445_2_, IBlockState p_176445_3_)
    {
        if (!worldIn.isRemote)
        {
            Block var4 = worldIn.getBlockState(p_176445_2_.offsetNorth()).getBlock();
            Block var5 = worldIn.getBlockState(p_176445_2_.offsetSouth()).getBlock();
            Block var6 = worldIn.getBlockState(p_176445_2_.offsetWest()).getBlock();
            Block var7 = worldIn.getBlockState(p_176445_2_.offsetEast()).getBlock();
            EnumFacing var8 = (EnumFacing)p_176445_3_.getValue(FACING);

            if (var8 == EnumFacing.NORTH && var4.isFullBlock() && !var5.isFullBlock())
            {
                var8 = EnumFacing.SOUTH;
            }
            else if (var8 == EnumFacing.SOUTH && var5.isFullBlock() && !var4.isFullBlock())
            {
                var8 = EnumFacing.NORTH;
            }
            else if (var8 == EnumFacing.WEST && var6.isFullBlock() && !var7.isFullBlock())
            {
                var8 = EnumFacing.EAST;
            }
            else if (var8 == EnumFacing.EAST && var7.isFullBlock() && !var6.isFullBlock())
            {
                var8 = EnumFacing.WEST;
            }

            worldIn.setBlockState(p_176445_2_, p_176445_3_.withProperty(FACING, var8), 2);
        }
    }

    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (this.isBurning)
        {
            EnumFacing var5 = (EnumFacing)state.getValue(FACING);
            double var6 = (double)pos.getX() + 0.5D;
            double var8 = (double)pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
            double var10 = (double)pos.getZ() + 0.5D;
            double var12 = 0.52D;
            double var14 = rand.nextDouble() * 0.6D - 0.3D;

            switch (BlockFurnace.SwitchEnumFacing.field_180356_a[var5.ordinal()])
            {
                case 1:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6 - var12, var8, var10 + var14, 0.0D, 0.0D, 0.0D, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, var6 - var12, var8, var10 + var14, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;

                case 2:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6 + var12, var8, var10 + var14, 0.0D, 0.0D, 0.0D, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, var6 + var12, var8, var10 + var14, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;

                case 3:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6 + var14, var8, var10 - var12, 0.0D, 0.0D, 0.0D, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, var6 + var14, var8, var10 - var12, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;

                case 4:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6 + var14, var8, var10 + var12, 0.0D, 0.0D, 0.0D, new int[0]);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, var6 + var14, var8, var10 + var12, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity var9 = worldIn.getTileEntity(pos);

            if (var9 instanceof TileEntityFurnace)
            {
                playerIn.displayGUIChest((TileEntityFurnace)var9);
            }

            return true;
        }
    }

    public static void func_176446_a(boolean p_176446_0_, World worldIn, BlockPos p_176446_2_)
    {
        IBlockState var3 = worldIn.getBlockState(p_176446_2_);
        TileEntity var4 = worldIn.getTileEntity(p_176446_2_);
        field_149934_M = true;

        if (p_176446_0_)
        {
            worldIn.setBlockState(p_176446_2_, Blocks.lit_furnace.getDefaultState().withProperty(FACING, var3.getValue(FACING)), 3);
            worldIn.setBlockState(p_176446_2_, Blocks.lit_furnace.getDefaultState().withProperty(FACING, var3.getValue(FACING)), 3);
        }
        else
        {
            worldIn.setBlockState(p_176446_2_, Blocks.furnace.getDefaultState().withProperty(FACING, var3.getValue(FACING)), 3);
            worldIn.setBlockState(p_176446_2_, Blocks.furnace.getDefaultState().withProperty(FACING, var3.getValue(FACING)), 3);
        }

        field_149934_M = false;

        if (var4 != null)
        {
            var4.validate();
            worldIn.setTileEntity(p_176446_2_, var4);
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityFurnace();
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.func_174811_aO().getOpposite());
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.func_174811_aO().getOpposite()), 2);

        if (stack.hasDisplayName())
        {
            TileEntity var6 = worldIn.getTileEntity(pos);

            if (var6 instanceof TileEntityFurnace)
            {
                ((TileEntityFurnace)var6).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!field_149934_M)
        {
            TileEntity var4 = worldIn.getTileEntity(pos);

            if (var4 instanceof TileEntityFurnace)
            {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityFurnace)var4);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }

    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    public int getComparatorInputOverride(World worldIn, BlockPos pos)
    {
        return Container.calcRedstoneFromInventory(worldIn.getTileEntity(pos));
    }

    public Item getItem(World worldIn, BlockPos pos)
    {
        return Item.getItemFromBlock(Blocks.furnace);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 3;
    }

    /**
     * Possibly modify the given BlockState before rendering it on an Entity (Minecarts, Endermen, ...)
     */
    public IBlockState getStateForEntityRender(IBlockState state)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing var2 = EnumFacing.getFront(meta);

        if (var2.getAxis() == EnumFacing.Axis.Y)
        {
            var2 = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, var2);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING});
    }

    static final class SwitchEnumFacing
    {
        static final int[] field_180356_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002111";

        static
        {
            try
            {
                field_180356_a[EnumFacing.WEST.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                field_180356_a[EnumFacing.EAST.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_180356_a[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_180356_a[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
