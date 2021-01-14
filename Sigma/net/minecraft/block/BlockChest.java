package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

public class BlockChest extends BlockContainer {
    public static final PropertyDirection FACING_PROP = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private final Random rand = new Random();

    /**
     * 0 : Normal chest, 1 : Trapped chest
     */
    public final int chestType;
    private static final String __OBFID = "CL_00000214";

    protected BlockChest(int type) {
        super(Material.wood);
        setDefaultState(blockState.getBaseState().withProperty(BlockChest.FACING_PROP, EnumFacing.NORTH));
        chestType = type;
        setCreativeTab(CreativeTabs.tabDecorations);
        setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType() {
        return 2;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        if (access.getBlockState(pos.offsetNorth()).getBlock() == this) {
            setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
        } else if (access.getBlockState(pos.offsetSouth()).getBlock() == this) {
            setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
        } else if (access.getBlockState(pos.offsetWest()).getBlock() == this) {
            setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        } else if (access.getBlockState(pos.offsetEast()).getBlock() == this) {
            setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
        } else {
            setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        checkForSurroundingChests(worldIn, pos, state);
        Iterator var4 = EnumFacing.Plane.HORIZONTAL.iterator();

        while (var4.hasNext()) {
            EnumFacing var5 = (EnumFacing) var4.next();
            BlockPos var6 = pos.offset(var5);
            IBlockState var7 = worldIn.getBlockState(var6);

            if (var7.getBlock() == this) {
                checkForSurroundingChests(worldIn, var6, var7);
            }
        }
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(BlockChest.FACING_PROP, placer.func_174811_aO());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing var6 = EnumFacing.getHorizontal(MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3).getOpposite();
        state = state.withProperty(BlockChest.FACING_PROP, var6);
        BlockPos var7 = pos.offsetNorth();
        BlockPos var8 = pos.offsetSouth();
        BlockPos var9 = pos.offsetWest();
        BlockPos var10 = pos.offsetEast();
        boolean var11 = this == worldIn.getBlockState(var7).getBlock();
        boolean var12 = this == worldIn.getBlockState(var8).getBlock();
        boolean var13 = this == worldIn.getBlockState(var9).getBlock();
        boolean var14 = this == worldIn.getBlockState(var10).getBlock();

        if (!var11 && !var12 && !var13 && !var14) {
            worldIn.setBlockState(pos, state, 3);
        } else if (var6.getAxis() == EnumFacing.Axis.X && (var11 || var12)) {
            if (var11) {
                worldIn.setBlockState(var7, state, 3);
            } else {
                worldIn.setBlockState(var8, state, 3);
            }

            worldIn.setBlockState(pos, state, 3);
        } else if (var6.getAxis() == EnumFacing.Axis.Z && (var13 || var14)) {
            if (var13) {
                worldIn.setBlockState(var9, state, 3);
            } else {
                worldIn.setBlockState(var10, state, 3);
            }

            worldIn.setBlockState(pos, state, 3);
        }

        if (stack.hasDisplayName()) {
            TileEntity var15 = worldIn.getTileEntity(pos);

            if (var15 instanceof TileEntityChest) {
                ((TileEntityChest) var15).setCustomName(stack.getDisplayName());
            }
        }
    }

    public IBlockState checkForSurroundingChests(World worldIn, BlockPos p_176455_2_, IBlockState p_176455_3_) {
        if (worldIn.isRemote) {
            return p_176455_3_;
        } else {
            IBlockState var4 = worldIn.getBlockState(p_176455_2_.offsetNorth());
            IBlockState var5 = worldIn.getBlockState(p_176455_2_.offsetSouth());
            IBlockState var6 = worldIn.getBlockState(p_176455_2_.offsetWest());
            IBlockState var7 = worldIn.getBlockState(p_176455_2_.offsetEast());
            EnumFacing var8 = (EnumFacing) p_176455_3_.getValue(BlockChest.FACING_PROP);
            Block var9 = var4.getBlock();
            Block var10 = var5.getBlock();
            Block var11 = var6.getBlock();
            Block var12 = var7.getBlock();

            if (var9 != this && var10 != this) {
                boolean var21 = var9.isFullBlock();
                boolean var22 = var10.isFullBlock();

                if (var11 == this || var12 == this) {
                    BlockPos var23 = var11 == this ? p_176455_2_.offsetWest() : p_176455_2_.offsetEast();
                    IBlockState var24 = worldIn.getBlockState(var23.offsetNorth());
                    IBlockState var25 = worldIn.getBlockState(var23.offsetSouth());
                    var8 = EnumFacing.SOUTH;
                    EnumFacing var26;

                    if (var11 == this) {
                        var26 = (EnumFacing) var6.getValue(BlockChest.FACING_PROP);
                    } else {
                        var26 = (EnumFacing) var7.getValue(BlockChest.FACING_PROP);
                    }

                    if (var26 == EnumFacing.NORTH) {
                        var8 = EnumFacing.NORTH;
                    }

                    Block var19 = var24.getBlock();
                    Block var20 = var25.getBlock();

                    if ((var21 || var19.isFullBlock()) && !var22 && !var20.isFullBlock()) {
                        var8 = EnumFacing.SOUTH;
                    }

                    if ((var22 || var20.isFullBlock()) && !var21 && !var19.isFullBlock()) {
                        var8 = EnumFacing.NORTH;
                    }
                }
            } else {
                BlockPos var13 = var9 == this ? p_176455_2_.offsetNorth() : p_176455_2_.offsetSouth();
                IBlockState var14 = worldIn.getBlockState(var13.offsetWest());
                IBlockState var15 = worldIn.getBlockState(var13.offsetEast());
                var8 = EnumFacing.EAST;
                EnumFacing var16;

                if (var9 == this) {
                    var16 = (EnumFacing) var4.getValue(BlockChest.FACING_PROP);
                } else {
                    var16 = (EnumFacing) var5.getValue(BlockChest.FACING_PROP);
                }

                if (var16 == EnumFacing.WEST) {
                    var8 = EnumFacing.WEST;
                }

                Block var17 = var14.getBlock();
                Block var18 = var15.getBlock();

                if ((var11.isFullBlock() || var17.isFullBlock()) && !var12.isFullBlock() && !var18.isFullBlock()) {
                    var8 = EnumFacing.EAST;
                }

                if ((var12.isFullBlock() || var18.isFullBlock()) && !var11.isFullBlock() && !var17.isFullBlock()) {
                    var8 = EnumFacing.WEST;
                }
            }

            p_176455_3_ = p_176455_3_.withProperty(BlockChest.FACING_PROP, var8);
            worldIn.setBlockState(p_176455_2_, p_176455_3_, 3);
            return p_176455_3_;
        }
    }

    public IBlockState func_176458_f(World worldIn, BlockPos p_176458_2_, IBlockState p_176458_3_) {
        EnumFacing var4 = null;
        Iterator var5 = EnumFacing.Plane.HORIZONTAL.iterator();

        while (var5.hasNext()) {
            EnumFacing var6 = (EnumFacing) var5.next();
            IBlockState var7 = worldIn.getBlockState(p_176458_2_.offset(var6));

            if (var7.getBlock() == this) {
                return p_176458_3_;
            }

            if (var7.getBlock().isFullBlock()) {
                if (var4 != null) {
                    var4 = null;
                    break;
                }

                var4 = var6;
            }
        }

        if (var4 != null) {
            return p_176458_3_.withProperty(BlockChest.FACING_PROP, var4.getOpposite());
        } else {
            EnumFacing var8 = (EnumFacing) p_176458_3_.getValue(BlockChest.FACING_PROP);

            if (worldIn.getBlockState(p_176458_2_.offset(var8)).getBlock().isFullBlock()) {
                var8 = var8.getOpposite();
            }

            if (worldIn.getBlockState(p_176458_2_.offset(var8)).getBlock().isFullBlock()) {
                var8 = var8.rotateY();
            }

            if (worldIn.getBlockState(p_176458_2_.offset(var8)).getBlock().isFullBlock()) {
                var8 = var8.getOpposite();
            }

            return p_176458_3_.withProperty(BlockChest.FACING_PROP, var8);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        int var3 = 0;
        BlockPos var4 = pos.offsetWest();
        BlockPos var5 = pos.offsetEast();
        BlockPos var6 = pos.offsetNorth();
        BlockPos var7 = pos.offsetSouth();

        if (worldIn.getBlockState(var4).getBlock() == this) {
            if (isSurroundingBlockChest(worldIn, var4)) {
                return false;
            }

            ++var3;
        }

        if (worldIn.getBlockState(var5).getBlock() == this) {
            if (isSurroundingBlockChest(worldIn, var5)) {
                return false;
            }

            ++var3;
        }

        if (worldIn.getBlockState(var6).getBlock() == this) {
            if (isSurroundingBlockChest(worldIn, var6)) {
                return false;
            }

            ++var3;
        }

        if (worldIn.getBlockState(var7).getBlock() == this) {
            if (isSurroundingBlockChest(worldIn, var7)) {
                return false;
            }

            ++var3;
        }

        return var3 <= 1;
    }

    private boolean isSurroundingBlockChest(World worldIn, BlockPos p_176454_2_) {
        if (worldIn.getBlockState(p_176454_2_).getBlock() != this) {
            return false;
        } else {
            Iterator var3 = EnumFacing.Plane.HORIZONTAL.iterator();
            EnumFacing var4;

            do {
                if (!var3.hasNext()) {
                    return false;
                }

                var4 = (EnumFacing) var3.next();
            } while (worldIn.getBlockState(p_176454_2_.offset(var4)).getBlock() != this);

            return true;
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        TileEntity var5 = worldIn.getTileEntity(pos);

        if (var5 instanceof TileEntityChest) {
            var5.updateContainingBlockInfo();
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity var4 = worldIn.getTileEntity(pos);

        if (var4 instanceof IInventory) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) var4);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        } else {
            ILockableContainer var9 = getLockableContainer(worldIn, pos);

            if (var9 != null) {
                playerIn.displayGUIChest(var9);
            }

            return true;
        }
    }

    public ILockableContainer getLockableContainer(World worldIn, BlockPos p_180676_2_) {
        TileEntity var3 = worldIn.getTileEntity(p_180676_2_);

        if (!(var3 instanceof TileEntityChest)) {
            return null;
        } else {
            Object var4 = var3;

            if (cannotOpenChest(worldIn, p_180676_2_)) {
                return null;
            } else {
                Iterator var5 = EnumFacing.Plane.HORIZONTAL.iterator();

                while (var5.hasNext()) {
                    EnumFacing var6 = (EnumFacing) var5.next();
                    BlockPos var7 = p_180676_2_.offset(var6);
                    Block var8 = worldIn.getBlockState(var7).getBlock();

                    if (var8 == this) {
                        if (cannotOpenChest(worldIn, var7)) {
                            return null;
                        }

                        TileEntity var9 = worldIn.getTileEntity(var7);

                        if (var9 instanceof TileEntityChest) {
                            if (var6 != EnumFacing.WEST && var6 != EnumFacing.NORTH) {
                                var4 = new InventoryLargeChest("container.chestDouble", (ILockableContainer) var4, (TileEntityChest) var9);
                            } else {
                                var4 = new InventoryLargeChest("container.chestDouble", (TileEntityChest) var9, (ILockableContainer) var4);
                            }
                        }
                    }
                }

                return (ILockableContainer) var4;
            }
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing
     * the block.
     */
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityChest();
    }

    /**
     * Can this block provide power. Only wire currently seems to have this
     * change based on its state.
     */
    @Override
    public boolean canProvidePower() {
        return chestType == 1;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        if (!canProvidePower()) {
            return 0;
        } else {
            int var5 = 0;
            TileEntity var6 = worldIn.getTileEntity(pos);

            if (var6 instanceof TileEntityChest) {
                var5 = ((TileEntityChest) var6).numPlayersUsing;
            }

            return MathHelper.clamp_int(var5, 0, 15);
        }
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return side == EnumFacing.UP ? isProvidingWeakPower(worldIn, pos, state, side) : 0;
    }

    private boolean cannotOpenChest(World worldIn, BlockPos p_176457_2_) {
        return isBelowSolidBlock(worldIn, p_176457_2_) || isOcelotSittingOnChest(worldIn, p_176457_2_);
    }

    private boolean isBelowSolidBlock(World worldIn, BlockPos p_176456_2_) {
        return worldIn.getBlockState(p_176456_2_.offsetUp()).getBlock().isNormalCube();
    }

    private boolean isOcelotSittingOnChest(World worldIn, BlockPos p_176453_2_) {
        Iterator var3 = worldIn.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB(p_176453_2_.getX(), p_176453_2_.getY() + 1, p_176453_2_.getZ(), p_176453_2_.getX() + 1, p_176453_2_.getY() + 2, p_176453_2_.getZ() + 1)).iterator();
        EntityOcelot var5;

        do {
            if (!var3.hasNext()) {
                return false;
            }

            Entity var4 = (Entity) var3.next();
            var5 = (EntityOcelot) var4;
        } while (!var5.isSitting());

        return true;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        return Container.calcRedstoneFromInventory(getLockableContainer(worldIn, pos));
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing var2 = EnumFacing.getFront(meta);

        if (var2.getAxis() == EnumFacing.Axis.Y) {
            var2 = EnumFacing.NORTH;
        }

        return getDefaultState().withProperty(BlockChest.FACING_PROP, var2);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(BlockChest.FACING_PROP)).getIndex();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockChest.FACING_PROP});
    }
}
