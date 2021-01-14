package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.RegistryDefaulted;
import net.minecraft.world.World;

public class BlockDispenser extends BlockContainer {
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");

    /**
     * Registry for all dispense behaviors.
     */
    public static final RegistryDefaulted dispenseBehaviorRegistry = new RegistryDefaulted(new BehaviorDefaultDispenseItem());
    protected Random rand = new Random();
    private static final String __OBFID = "CL_00000229";

    protected BlockDispenser() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TRIGGERED, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn) {
        return 4;
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        this.setDefaultDirection(worldIn, pos, state);
    }

    private void setDefaultDirection(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            EnumFacing var4 = (EnumFacing) state.getValue(FACING);
            boolean var5 = worldIn.getBlockState(pos.offsetNorth()).getBlock().isFullBlock();
            boolean var6 = worldIn.getBlockState(pos.offsetSouth()).getBlock().isFullBlock();

            if (var4 == EnumFacing.NORTH && var5 && !var6) {
                var4 = EnumFacing.SOUTH;
            } else if (var4 == EnumFacing.SOUTH && var6 && !var5) {
                var4 = EnumFacing.NORTH;
            } else {
                boolean var7 = worldIn.getBlockState(pos.offsetWest()).getBlock().isFullBlock();
                boolean var8 = worldIn.getBlockState(pos.offsetEast()).getBlock().isFullBlock();

                if (var4 == EnumFacing.WEST && var7 && !var8) {
                    var4 = EnumFacing.EAST;
                } else if (var4 == EnumFacing.EAST && var8 && !var7) {
                    var4 = EnumFacing.WEST;
                }
            }

            worldIn.setBlockState(pos, state.withProperty(FACING, var4).withProperty(TRIGGERED, Boolean.valueOf(false)), 2);
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        } else {
            TileEntity var9 = worldIn.getTileEntity(pos);

            if (var9 instanceof TileEntityDispenser) {
                playerIn.displayGUIChest((TileEntityDispenser) var9);
            }

            return true;
        }
    }

    protected void func_176439_d(World worldIn, BlockPos p_176439_2_) {
        BlockSourceImpl var3 = new BlockSourceImpl(worldIn, p_176439_2_);
        TileEntityDispenser var4 = (TileEntityDispenser) var3.getBlockTileEntity();

        if (var4 != null) {
            int var5 = var4.func_146017_i();

            if (var5 < 0) {
                worldIn.playAuxSFX(1001, p_176439_2_, 0);
            } else {
                ItemStack var6 = var4.getStackInSlot(var5);
                IBehaviorDispenseItem var7 = this.func_149940_a(var6);

                if (var7 != IBehaviorDispenseItem.itemDispenseBehaviorProvider) {
                    ItemStack var8 = var7.dispense(var3, var6);
                    var4.setInventorySlotContents(var5, var8.stackSize == 0 ? null : var8);
                }
            }
        }
    }

    protected IBehaviorDispenseItem func_149940_a(ItemStack p_149940_1_) {
        return (IBehaviorDispenseItem) dispenseBehaviorRegistry.getObject(p_149940_1_ == null ? null : p_149940_1_.getItem());
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        boolean var5 = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.offsetUp());
        boolean var6 = ((Boolean) state.getValue(TRIGGERED)).booleanValue();

        if (var5 && !var6) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
            worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(true)), 4);
        } else if (!var5 && var6) {
            worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(false)), 4);
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            this.func_176439_d(worldIn, pos);
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDispenser();
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, BlockPistonBase.func_180695_a(worldIn, pos, placer)).withProperty(TRIGGERED, Boolean.valueOf(false));
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, BlockPistonBase.func_180695_a(worldIn, pos, placer)), 2);

        if (stack.hasDisplayName()) {
            TileEntity var6 = worldIn.getTileEntity(pos);

            if (var6 instanceof TileEntityDispenser) {
                ((TileEntityDispenser) var6).func_146018_a(stack.getDisplayName());
            }
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity var4 = worldIn.getTileEntity(pos);

        if (var4 instanceof TileEntityDispenser) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityDispenser) var4);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    /**
     * Get the position where the dispenser at the given Coordinates should dispense to.
     */
    public static IPosition getDispensePosition(IBlockSource coords) {
        EnumFacing var1 = getFacing(coords.getBlockMetadata());
        double var2 = coords.getX() + 0.7D * (double) var1.getFrontOffsetX();
        double var4 = coords.getY() + 0.7D * (double) var1.getFrontOffsetY();
        double var6 = coords.getZ() + 0.7D * (double) var1.getFrontOffsetZ();
        return new PositionImpl(var2, var4, var6);
    }

    /**
     * Get the facing of a dispenser with the given metadata
     */
    public static EnumFacing getFacing(int meta) {
        return EnumFacing.getFront(meta & 7);
    }

    public boolean hasComparatorInputOverride() {
        return true;
    }

    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        return Container.calcRedstoneFromInventory(worldIn.getTileEntity(pos));
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType() {
        return 3;
    }

    /**
     * Possibly modify the given BlockState before rendering it on an Entity (Minecarts, Endermen, ...)
     */
    public IBlockState getStateForEntityRender(IBlockState state) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(TRIGGERED, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        byte var2 = 0;
        int var3 = var2 | ((EnumFacing) state.getValue(FACING)).getIndex();

        if (((Boolean) state.getValue(TRIGGERED)).booleanValue()) {
            var3 |= 8;
        }

        return var3;
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{FACING, TRIGGERED});
    }
}
