package net.minecraft.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonBase extends Block
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool EXTENDED = PropertyBool.create("extended");

    /** This piston is the sticky one? */
    private final boolean isSticky;

    public BlockPistonBase(boolean isSticky)
    {
        super(Material.piston);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(EXTENDED, Boolean.valueOf(false)));
        this.isSticky = isSticky;
        this.setStepSound(soundTypePiston);
        this.setHardness(0.5F);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, getFacingFromEntity(worldIn, pos, placer)), 2);

        if (!worldIn.isRemote)
        {
            this.checkForMove(worldIn, pos, state);
        }
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!worldIn.isRemote)
        {
            this.checkForMove(worldIn, pos, state);
        }
    }

    @Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null)
        {
            this.checkForMove(worldIn, pos, state);
        }
    }

    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, getFacingFromEntity(worldIn, pos, placer)).withProperty(EXTENDED, Boolean.valueOf(false));
    }

    private void checkForMove(World worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing var4 = (EnumFacing)state.getValue(FACING);
        boolean var5 = this.shouldBeExtended(worldIn, pos, var4);

        if (var5 && !((Boolean)state.getValue(EXTENDED)).booleanValue())
        {
            if ((new BlockPistonStructureHelper(worldIn, pos, var4, true)).canMove())
            {
                worldIn.addBlockEvent(pos, this, 0, var4.getIndex());
            }
        }
        else if (!var5 && ((Boolean)state.getValue(EXTENDED)).booleanValue())
        {
            worldIn.setBlockState(pos, state.withProperty(EXTENDED, Boolean.valueOf(false)), 2);
            worldIn.addBlockEvent(pos, this, 1, var4.getIndex());
        }
    }

    private boolean shouldBeExtended(World worldIn, BlockPos pos, EnumFacing facing)
    {
        EnumFacing[] var4 = EnumFacing.values();
        int var5 = var4.length;
        int var6;

        for (var6 = 0; var6 < var5; ++var6)
        {
            EnumFacing var7 = var4[var6];

            if (var7 != facing && worldIn.isSidePowered(pos.offset(var7), var7))
            {
                return true;
            }
        }

        if (worldIn.isSidePowered(pos, EnumFacing.NORTH))
        {
            return true;
        }
        else
        {
            BlockPos var9 = pos.up();
            EnumFacing[] var10 = EnumFacing.values();
            var6 = var10.length;

            for (int var11 = 0; var11 < var6; ++var11)
            {
                EnumFacing var8 = var10[var11];

                if (var8 != EnumFacing.DOWN && worldIn.isSidePowered(var9.offset(var8), var8))
                {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * Called on both Client and Server when World#addBlockEvent is called
     */
    @Override
	public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam)
    {
        EnumFacing var6 = (EnumFacing)state.getValue(FACING);

        if (!worldIn.isRemote)
        {
            boolean var7 = this.shouldBeExtended(worldIn, pos, var6);

            if (var7 && eventID == 1)
            {
                worldIn.setBlockState(pos, state.withProperty(EXTENDED, Boolean.valueOf(true)), 2);
                return false;
            }

            if (!var7 && eventID == 0)
            {
                return false;
            }
        }

        if (eventID == 0)
        {
            if (!this.doMove(worldIn, pos, var6, true))
            {
                return false;
            }

            worldIn.setBlockState(pos, state.withProperty(EXTENDED, Boolean.valueOf(true)), 2);
            worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "tile.piston.out", 0.5F, worldIn.rand.nextFloat() * 0.25F + 0.6F);
        }
        else if (eventID == 1)
        {
            TileEntity var13 = worldIn.getTileEntity(pos.offset(var6));

            if (var13 instanceof TileEntityPiston)
            {
                ((TileEntityPiston)var13).clearPistonTileEntity();
            }

            worldIn.setBlockState(pos, Blocks.piston_extension.getDefaultState().withProperty(BlockPistonMoving.FACING, var6).withProperty(BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT), 3);
            worldIn.setTileEntity(pos, BlockPistonMoving.newTileEntity(this.getStateFromMeta(eventParam), var6, false, true));

            if (this.isSticky)
            {
                BlockPos var8 = pos.add(var6.getFrontOffsetX() * 2, var6.getFrontOffsetY() * 2, var6.getFrontOffsetZ() * 2);
                Block var9 = worldIn.getBlockState(var8).getBlock();
                boolean var10 = false;

                if (var9 == Blocks.piston_extension)
                {
                    TileEntity var11 = worldIn.getTileEntity(var8);

                    if (var11 instanceof TileEntityPiston)
                    {
                        TileEntityPiston var12 = (TileEntityPiston)var11;

                        if (var12.getFacing() == var6 && var12.isExtending())
                        {
                            var12.clearPistonTileEntity();
                            var10 = true;
                        }
                    }
                }

                if (!var10 && var9.getMaterial() != Material.air && canPush(var9, worldIn, var8, var6.getOpposite(), false) && (var9.getMobilityFlag() == 0 || var9 == Blocks.piston || var9 == Blocks.sticky_piston))
                {
                    this.doMove(worldIn, pos, var6, false);
                }
            }
            else
            {
                worldIn.setBlockToAir(pos.offset(var6));
            }

            worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "tile.piston.in", 0.5F, worldIn.rand.nextFloat() * 0.15F + 0.6F);
        }

        return true;
    }

    @Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState var3 = worldIn.getBlockState(pos);

        if (var3.getBlock() == this && ((Boolean)var3.getValue(EXTENDED)).booleanValue())
        {
            EnumFacing var5 = (EnumFacing)var3.getValue(FACING);

            if (var5 != null)
            {
                switch (BlockPistonBase.SwitchEnumFacing.FACING_LOOKUP[var5.ordinal()])
                {
                    case 1:
                        this.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
                        break;

                    case 2:
                        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
                        break;

                    case 3:
                        this.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
                        break;

                    case 4:
                        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
                        break;

                    case 5:
                        this.setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                        break;

                    case 6:
                        this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
                }
            }
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    @Override
	public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Add all collision boxes of this Block to the list that intersect with the given mask.
     *  
     * @param collidingEntity the Entity colliding with this Block
     */
    @Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }

    @Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    @Override
	public boolean isFullCube()
    {
        return false;
    }

    public static EnumFacing getFacing(int meta)
    {
        int var1 = meta & 7;
        return var1 > 5 ? null : EnumFacing.getFront(var1);
    }

    public static EnumFacing getFacingFromEntity(World worldIn, BlockPos clickedBlock, EntityLivingBase entityIn)
    {
        if (MathHelper.abs((float)entityIn.posX - clickedBlock.getX()) < 2.0F && MathHelper.abs((float)entityIn.posZ - clickedBlock.getZ()) < 2.0F)
        {
            double var3 = entityIn.posY + entityIn.getEyeHeight();

            if (var3 - clickedBlock.getY() > 2.0D)
            {
                return EnumFacing.UP;
            }

            if (clickedBlock.getY() - var3 > 0.0D)
            {
                return EnumFacing.DOWN;
            }
        }

        return entityIn.getHorizontalFacing().getOpposite();
    }

    public static boolean canPush(Block blockIn, World worldIn, BlockPos pos, EnumFacing direction, boolean allowDestroy)
    {
        if (blockIn == Blocks.obsidian)
        {
            return false;
        }
        else if (!worldIn.getWorldBorder().contains(pos))
        {
            return false;
        }
        else if (pos.getY() >= 0 && (direction != EnumFacing.DOWN || pos.getY() != 0))
        {
            if (pos.getY() <= worldIn.getHeight() - 1 && (direction != EnumFacing.UP || pos.getY() != worldIn.getHeight() - 1))
            {
                if (blockIn != Blocks.piston && blockIn != Blocks.sticky_piston)
                {
                    if (blockIn.getBlockHardness(worldIn, pos) == -1.0F)
                    {
                        return false;
                    }

                    if (blockIn.getMobilityFlag() == 2)
                    {
                        return false;
                    }

                    if (blockIn.getMobilityFlag() == 1)
                    {
                        if (!allowDestroy)
                        {
                            return false;
                        }

                        return true;
                    }
                }
                else if (((Boolean)worldIn.getBlockState(pos).getValue(EXTENDED)).booleanValue())
                {
                    return false;
                }

                return !(blockIn instanceof ITileEntityProvider);
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    private boolean doMove(World worldIn, BlockPos pos, EnumFacing direction, boolean extending)
    {
        if (!extending)
        {
            worldIn.setBlockToAir(pos.offset(direction));
        }

        BlockPistonStructureHelper var5 = new BlockPistonStructureHelper(worldIn, pos, direction, extending);
        List var6 = var5.getBlocksToMove();
        List var7 = var5.getBlocksToDestroy();

        if (!var5.canMove())
        {
            return false;
        }
        else
        {
            int var8 = var6.size() + var7.size();
            Block[] var9 = new Block[var8];
            EnumFacing var10 = extending ? direction : direction.getOpposite();
            int var11;
            BlockPos var12;

            for (var11 = var7.size() - 1; var11 >= 0; --var11)
            {
                var12 = (BlockPos)var7.get(var11);
                Block var13 = worldIn.getBlockState(var12).getBlock();
                var13.dropBlockAsItem(worldIn, var12, worldIn.getBlockState(var12), 0);
                worldIn.setBlockToAir(var12);
                --var8;
                var9[var8] = var13;
            }

            IBlockState var19;

            for (var11 = var6.size() - 1; var11 >= 0; --var11)
            {
                var12 = (BlockPos)var6.get(var11);
                var19 = worldIn.getBlockState(var12);
                Block var14 = var19.getBlock();
                var14.getMetaFromState(var19);
                worldIn.setBlockToAir(var12);
                var12 = var12.offset(var10);
                worldIn.setBlockState(var12, Blocks.piston_extension.getDefaultState().withProperty(FACING, direction), 4);
                worldIn.setTileEntity(var12, BlockPistonMoving.newTileEntity(var19, direction, extending, false));
                --var8;
                var9[var8] = var14;
            }

            BlockPos var16 = pos.offset(direction);

            if (extending)
            {
                BlockPistonExtension.EnumPistonType var17 = this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
                var19 = Blocks.piston_head.getDefaultState().withProperty(BlockPistonExtension.FACING, direction).withProperty(BlockPistonExtension.TYPE, var17);
                IBlockState var20 = Blocks.piston_extension.getDefaultState().withProperty(BlockPistonMoving.FACING, direction).withProperty(BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
                worldIn.setBlockState(var16, var20, 4);
                worldIn.setTileEntity(var16, BlockPistonMoving.newTileEntity(var19, direction, true, false));
            }

            int var18;

            for (var18 = var7.size() - 1; var18 >= 0; --var18)
            {
                worldIn.notifyNeighborsOfStateChange((BlockPos)var7.get(var18), var9[var8++]);
            }

            for (var18 = var6.size() - 1; var18 >= 0; --var18)
            {
                worldIn.notifyNeighborsOfStateChange((BlockPos)var6.get(var18), var9[var8++]);
            }

            if (extending)
            {
                worldIn.notifyNeighborsOfStateChange(var16, Blocks.piston_head);
                worldIn.notifyNeighborsOfStateChange(pos, this);
            }

            return true;
        }
    }

    /**
     * Possibly modify the given BlockState before rendering it on an Entity (Minecarts, Endermen, ...)
     */
    @Override
	public IBlockState getStateForEntityRender(IBlockState state)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.UP);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(EXTENDED, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        byte var2 = 0;
        int var3 = var2 | ((EnumFacing)state.getValue(FACING)).getIndex();

        if (((Boolean)state.getValue(EXTENDED)).booleanValue())
        {
            var3 |= 8;
        }

        return var3;
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING, EXTENDED});
    }

    static final class SwitchEnumFacing
    {
        static final int[] FACING_LOOKUP = new int[EnumFacing.values().length];

        static
        {
            try
            {
                FACING_LOOKUP[EnumFacing.DOWN.ordinal()] = 1;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.UP.ordinal()] = 2;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 5;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 6;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
