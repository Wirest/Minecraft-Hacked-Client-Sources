package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVine extends Block
{
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool[] ALL_FACES = new PropertyBool[] {UP, NORTH, SOUTH, WEST, EAST};
    public static final int SOUTH_FLAG = getMetaFlag(EnumFacing.SOUTH);
    public static final int NORTH_FLAG = getMetaFlag(EnumFacing.NORTH);
    public static final int EAST_FLAG = getMetaFlag(EnumFacing.EAST);
    public static final int WEST_FLAG = getMetaFlag(EnumFacing.WEST);

    public BlockVine()
    {
        super(Material.vine);
        this.setDefaultState(this.blockState.getBaseState().withProperty(UP, Boolean.valueOf(false)).withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    @Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(UP, Boolean.valueOf(worldIn.getBlockState(pos.up()).getBlock().isBlockNormalCube()));
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    @Override
	public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
	public boolean isFullCube()
    {
        return false;
    }

    /**
     * Whether this Block can be replaced directly by other blocks (true for e.g. tall grass)
     */
    @Override
	public boolean isReplaceable(World worldIn, BlockPos pos)
    {
        return true;
    }

    @Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        float var4 = 1.0F;
        float var5 = 1.0F;
        float var6 = 1.0F;
        float var7 = 0.0F;
        float var8 = 0.0F;
        float var9 = 0.0F;
        boolean var10 = false;

        if (((Boolean)worldIn.getBlockState(pos).getValue(WEST)).booleanValue())
        {
            var7 = Math.max(var7, 0.0625F);
            var4 = 0.0F;
            var5 = 0.0F;
            var8 = 1.0F;
            var6 = 0.0F;
            var9 = 1.0F;
            var10 = true;
        }

        if (((Boolean)worldIn.getBlockState(pos).getValue(EAST)).booleanValue())
        {
            var4 = Math.min(var4, 0.9375F);
            var7 = 1.0F;
            var5 = 0.0F;
            var8 = 1.0F;
            var6 = 0.0F;
            var9 = 1.0F;
            var10 = true;
        }

        if (((Boolean)worldIn.getBlockState(pos).getValue(NORTH)).booleanValue())
        {
            var9 = Math.max(var9, 0.0625F);
            var6 = 0.0F;
            var4 = 0.0F;
            var7 = 1.0F;
            var5 = 0.0F;
            var8 = 1.0F;
            var10 = true;
        }

        if (((Boolean)worldIn.getBlockState(pos).getValue(SOUTH)).booleanValue())
        {
            var6 = Math.min(var6, 0.9375F);
            var9 = 1.0F;
            var4 = 0.0F;
            var7 = 1.0F;
            var5 = 0.0F;
            var8 = 1.0F;
            var10 = true;
        }

        if (!var10 && this.canPlaceOn(worldIn.getBlockState(pos.up()).getBlock()))
        {
            var5 = Math.min(var5, 0.9375F);
            var8 = 1.0F;
            var4 = 0.0F;
            var7 = 1.0F;
            var6 = 0.0F;
            var9 = 1.0F;
        }

        this.setBlockBounds(var4, var5, var6, var7, var8, var9);
    }

    @Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    /**
     * Check whether this Block can be placed on the given side
     */
    @Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        switch (BlockVine.SwitchEnumFacing.FACING_LOOKUP[side.ordinal()])
        {
            case 1:
                return this.canPlaceOn(worldIn.getBlockState(pos.up()).getBlock());

            case 2:
            case 3:
            case 4:
            case 5:
                return this.canPlaceOn(worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock());

            default:
                return false;
        }
    }

    private boolean canPlaceOn(Block blockIn)
    {
        return blockIn.isFullCube() && blockIn.blockMaterial.blocksMovement();
    }

    private boolean recheckGrownSides(World worldIn, BlockPos pos, IBlockState state)
    {
        IBlockState var4 = state;
        Iterator var5 = EnumFacing.Plane.HORIZONTAL.iterator();

        while (var5.hasNext())
        {
            EnumFacing var6 = (EnumFacing)var5.next();
            PropertyBool var7 = getPropertyFor(var6);

            if (((Boolean)state.getValue(var7)).booleanValue() && !this.canPlaceOn(worldIn.getBlockState(pos.offset(var6)).getBlock()))
            {
                IBlockState var8 = worldIn.getBlockState(pos.up());

                if (var8.getBlock() != this || !((Boolean)var8.getValue(var7)).booleanValue())
                {
                    state = state.withProperty(var7, Boolean.valueOf(false));
                }
            }
        }

        if (getNumGrownFaces(state) == 0)
        {
            return false;
        }
        else
        {
            if (var4 != state)
            {
                worldIn.setBlockState(pos, state, 2);
            }

            return true;
        }
    }

    @Override
	public int getBlockColor()
    {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    @Override
	public int getRenderColor(IBlockState state)
    {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    @Override
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
    {
        return worldIn.getBiomeGenForCoords(pos).getFoliageColorAtPos(pos);
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!worldIn.isRemote && !this.recheckGrownSides(worldIn, pos, state))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            if (worldIn.rand.nextInt(4) == 0)
            {
                byte var5 = 4;
                int var6 = 5;
                boolean var7 = false;
                label189:

                for (int var8 = -var5; var8 <= var5; ++var8)
                {
                    for (int var9 = -var5; var9 <= var5; ++var9)
                    {
                        for (int var10 = -1; var10 <= 1; ++var10)
                        {
                            if (worldIn.getBlockState(pos.add(var8, var10, var9)).getBlock() == this)
                            {
                                --var6;

                                if (var6 <= 0)
                                {
                                    var7 = true;
                                    break label189;
                                }
                            }
                        }
                    }
                }

                EnumFacing var17 = EnumFacing.random(rand);
                EnumFacing var23;

                if (var17 == EnumFacing.UP && pos.getY() < 255 && worldIn.isAirBlock(pos.up()))
                {
                    if (!var7)
                    {
                        IBlockState var19 = state;
                        Iterator var22 = EnumFacing.Plane.HORIZONTAL.iterator();

                        while (var22.hasNext())
                        {
                            var23 = (EnumFacing)var22.next();

                            if (rand.nextBoolean() || !this.canPlaceOn(worldIn.getBlockState(pos.offset(var23).up()).getBlock()))
                            {
                                var19 = var19.withProperty(getPropertyFor(var23), Boolean.valueOf(false));
                            }
                        }

                        if (((Boolean)var19.getValue(NORTH)).booleanValue() || ((Boolean)var19.getValue(EAST)).booleanValue() || ((Boolean)var19.getValue(SOUTH)).booleanValue() || ((Boolean)var19.getValue(WEST)).booleanValue())
                        {
                            worldIn.setBlockState(pos.up(), var19, 2);
                        }
                    }
                }
                else
                {
                    BlockPos var18;

                    if (var17.getAxis().isHorizontal() && !((Boolean)state.getValue(getPropertyFor(var17))).booleanValue())
                    {
                        if (!var7)
                        {
                            var18 = pos.offset(var17);
                            Block var21 = worldIn.getBlockState(var18).getBlock();

                            if (var21.blockMaterial == Material.air)
                            {
                                var23 = var17.rotateY();
                                EnumFacing var24 = var17.rotateYCCW();
                                boolean var25 = ((Boolean)state.getValue(getPropertyFor(var23))).booleanValue();
                                boolean var26 = ((Boolean)state.getValue(getPropertyFor(var24))).booleanValue();
                                BlockPos var27 = var18.offset(var23);
                                BlockPos var16 = var18.offset(var24);

                                if (var25 && this.canPlaceOn(worldIn.getBlockState(var27).getBlock()))
                                {
                                    worldIn.setBlockState(var18, this.getDefaultState().withProperty(getPropertyFor(var23), Boolean.valueOf(true)), 2);
                                }
                                else if (var26 && this.canPlaceOn(worldIn.getBlockState(var16).getBlock()))
                                {
                                    worldIn.setBlockState(var18, this.getDefaultState().withProperty(getPropertyFor(var24), Boolean.valueOf(true)), 2);
                                }
                                else if (var25 && worldIn.isAirBlock(var27) && this.canPlaceOn(worldIn.getBlockState(pos.offset(var23)).getBlock()))
                                {
                                    worldIn.setBlockState(var27, this.getDefaultState().withProperty(getPropertyFor(var17.getOpposite()), Boolean.valueOf(true)), 2);
                                }
                                else if (var26 && worldIn.isAirBlock(var16) && this.canPlaceOn(worldIn.getBlockState(pos.offset(var24)).getBlock()))
                                {
                                    worldIn.setBlockState(var16, this.getDefaultState().withProperty(getPropertyFor(var17.getOpposite()), Boolean.valueOf(true)), 2);
                                }
                                else if (this.canPlaceOn(worldIn.getBlockState(var18.up()).getBlock()))
                                {
                                    worldIn.setBlockState(var18, this.getDefaultState(), 2);
                                }
                            }
                            else if (var21.blockMaterial.isOpaque() && var21.isFullCube())
                            {
                                worldIn.setBlockState(pos, state.withProperty(getPropertyFor(var17), Boolean.valueOf(true)), 2);
                            }
                        }
                    }
                    else
                    {
                        if (pos.getY() > 1)
                        {
                            var18 = pos.down();
                            IBlockState var20 = worldIn.getBlockState(var18);
                            Block var11 = var20.getBlock();
                            IBlockState var12;
                            Iterator var13;
                            EnumFacing var14;

                            if (var11.blockMaterial == Material.air)
                            {
                                var12 = state;
                                var13 = EnumFacing.Plane.HORIZONTAL.iterator();

                                while (var13.hasNext())
                                {
                                    var14 = (EnumFacing)var13.next();

                                    if (rand.nextBoolean())
                                    {
                                        var12 = var12.withProperty(getPropertyFor(var14), Boolean.valueOf(false));
                                    }
                                }

                                if (((Boolean)var12.getValue(NORTH)).booleanValue() || ((Boolean)var12.getValue(EAST)).booleanValue() || ((Boolean)var12.getValue(SOUTH)).booleanValue() || ((Boolean)var12.getValue(WEST)).booleanValue())
                                {
                                    worldIn.setBlockState(var18, var12, 2);
                                }
                            }
                            else if (var11 == this)
                            {
                                var12 = var20;
                                var13 = EnumFacing.Plane.HORIZONTAL.iterator();

                                while (var13.hasNext())
                                {
                                    var14 = (EnumFacing)var13.next();
                                    PropertyBool var15 = getPropertyFor(var14);

                                    if (rand.nextBoolean() || !((Boolean)state.getValue(var15)).booleanValue())
                                    {
                                        var12 = var12.withProperty(var15, Boolean.valueOf(false));
                                    }
                                }

                                if (((Boolean)var12.getValue(NORTH)).booleanValue() || ((Boolean)var12.getValue(EAST)).booleanValue() || ((Boolean)var12.getValue(SOUTH)).booleanValue() || ((Boolean)var12.getValue(WEST)).booleanValue())
                                {
                                    worldIn.setBlockState(var18, var12, 2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static int getMetaFlag(EnumFacing face)
    {
        return 1 << face.getHorizontalIndex();
    }

    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState var9 = this.getDefaultState().withProperty(UP, Boolean.valueOf(false)).withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false));
        return facing.getAxis().isHorizontal() ? var9.withProperty(getPropertyFor(facing.getOpposite()), Boolean.valueOf(true)) : var9;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
	public int quantityDropped(Random random)
    {
        return 0;
    }

    @Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te)
    {
        if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears)
        {
            player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
            spawnAsEntity(worldIn, pos, new ItemStack(Blocks.vine, 1, 0));
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te);
        }
    }

    @Override
	public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(NORTH, Boolean.valueOf((meta & NORTH_FLAG) > 0)).withProperty(EAST, Boolean.valueOf((meta & EAST_FLAG) > 0)).withProperty(SOUTH, Boolean.valueOf((meta & SOUTH_FLAG) > 0)).withProperty(WEST, Boolean.valueOf((meta & WEST_FLAG) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        int var2 = 0;

        if (((Boolean)state.getValue(NORTH)).booleanValue())
        {
            var2 |= NORTH_FLAG;
        }

        if (((Boolean)state.getValue(EAST)).booleanValue())
        {
            var2 |= EAST_FLAG;
        }

        if (((Boolean)state.getValue(SOUTH)).booleanValue())
        {
            var2 |= SOUTH_FLAG;
        }

        if (((Boolean)state.getValue(WEST)).booleanValue())
        {
            var2 |= WEST_FLAG;
        }

        return var2;
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {UP, NORTH, EAST, SOUTH, WEST});
    }

    public static PropertyBool getPropertyFor(EnumFacing side)
    {
        switch (BlockVine.SwitchEnumFacing.FACING_LOOKUP[side.ordinal()])
        {
            case 1:
                return UP;

            case 2:
                return NORTH;

            case 3:
                return SOUTH;

            case 4:
                return EAST;

            case 5:
                return WEST;

            default:
                throw new IllegalArgumentException(side + " is an invalid choice");
        }
    }

    public static int getNumGrownFaces(IBlockState state)
    {
        int var1 = 0;
        PropertyBool[] var2 = ALL_FACES;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            PropertyBool var5 = var2[var4];

            if (((Boolean)state.getValue(var5)).booleanValue())
            {
                ++var1;
            }
        }

        return var1;
    }

    static final class SwitchEnumFacing
    {
        static final int[] FACING_LOOKUP = new int[EnumFacing.values().length];

        static
        {
            try
            {
                FACING_LOOKUP[EnumFacing.UP.ordinal()] = 1;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 5;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
