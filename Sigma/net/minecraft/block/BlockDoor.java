package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDoor extends Block {
    public static final PropertyDirection FACING_PROP = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool OPEN_PROP = PropertyBool.create("open");
    public static final PropertyEnum HINGEPOSITION_PROP = PropertyEnum.create("hinge", BlockDoor.EnumHingePosition.class);
    public static final PropertyBool POWERED_PROP = PropertyBool.create("powered");
    public static final PropertyEnum HALF_PROP = PropertyEnum.create("half", BlockDoor.EnumDoorHalf.class);
    private static final String __OBFID = "CL_00000230";

    protected BlockDoor(Material p_i45402_1_) {
        super(p_i45402_1_);
        setDefaultState(blockState.getBaseState().withProperty(BlockDoor.FACING_PROP, EnumFacing.NORTH).withProperty(BlockDoor.OPEN_PROP, Boolean.valueOf(false)).withProperty(BlockDoor.HINGEPOSITION_PROP, BlockDoor.EnumHingePosition.LEFT).withProperty(BlockDoor.POWERED_PROP, Boolean.valueOf(false)).withProperty(BlockDoor.HALF_PROP, BlockDoor.EnumDoorHalf.LOWER));
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos) {
        return BlockDoor.func_176516_g(BlockDoor.func_176515_e(blockAccess, pos));
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        func_150011_b(BlockDoor.func_176515_e(access, pos));
    }

    private void func_150011_b(int p_150011_1_) {
        float var2 = 0.1875F;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
        EnumFacing var3 = BlockDoor.func_176511_f(p_150011_1_);
        boolean var4 = BlockDoor.func_176516_g(p_150011_1_);
        boolean var5 = BlockDoor.func_176513_j(p_150011_1_);

        if (var4) {
            if (var3 == EnumFacing.EAST) {
                if (!var5) {
                    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
                } else {
                    setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
                }
            } else if (var3 == EnumFacing.SOUTH) {
                if (!var5) {
                    setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                } else {
                    setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
                }
            } else if (var3 == EnumFacing.WEST) {
                if (!var5) {
                    setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
                } else {
                    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
                }
            } else if (var3 == EnumFacing.NORTH) {
                if (!var5) {
                    setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
                } else {
                    setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        } else if (var3 == EnumFacing.EAST) {
            setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
        } else if (var3 == EnumFacing.SOUTH) {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
        } else if (var3 == EnumFacing.WEST) {
            setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        } else if (var3 == EnumFacing.NORTH) {
            setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (blockMaterial == Material.iron) {
            return true;
        } else {
            BlockPos var9 = state.getValue(BlockDoor.HALF_PROP) == BlockDoor.EnumDoorHalf.LOWER ? pos : pos.offsetDown();
            IBlockState var10 = pos.equals(var9) ? state : worldIn.getBlockState(var9);

            if (var10.getBlock() != this) {
                return false;
            } else {
                state = var10.cycleProperty(BlockDoor.OPEN_PROP);
                worldIn.setBlockState(var9, state, 2);
                worldIn.markBlockRangeForRenderUpdate(var9, pos);
                worldIn.playAuxSFXAtEntity(playerIn, ((Boolean) state.getValue(BlockDoor.OPEN_PROP)).booleanValue() ? 1003 : 1006, pos, 0);
                return true;
            }
        }
    }

    public void func_176512_a(World worldIn, BlockPos p_176512_2_, boolean p_176512_3_) {
        IBlockState var4 = worldIn.getBlockState(p_176512_2_);

        if (var4.getBlock() == this) {
            BlockPos var5 = var4.getValue(BlockDoor.HALF_PROP) == BlockDoor.EnumDoorHalf.LOWER ? p_176512_2_ : p_176512_2_.offsetDown();
            IBlockState var6 = p_176512_2_ == var5 ? var4 : worldIn.getBlockState(var5);

            if (var6.getBlock() == this && ((Boolean) var6.getValue(BlockDoor.OPEN_PROP)).booleanValue() != p_176512_3_) {
                worldIn.setBlockState(var5, var6.withProperty(BlockDoor.OPEN_PROP, Boolean.valueOf(p_176512_3_)), 2);
                worldIn.markBlockRangeForRenderUpdate(var5, p_176512_2_);
                worldIn.playAuxSFXAtEntity((EntityPlayer) null, p_176512_3_ ? 1003 : 1006, p_176512_2_, 0);
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (state.getValue(BlockDoor.HALF_PROP) == BlockDoor.EnumDoorHalf.UPPER) {
            BlockPos var5 = pos.offsetDown();
            IBlockState var6 = worldIn.getBlockState(var5);

            if (var6.getBlock() != this) {
                worldIn.setBlockToAir(pos);
            } else if (neighborBlock != this) {
                onNeighborBlockChange(worldIn, var5, var6, neighborBlock);
            }
        } else {
            boolean var9 = false;
            BlockPos var10 = pos.offsetUp();
            IBlockState var7 = worldIn.getBlockState(var10);

            if (var7.getBlock() != this) {
                worldIn.setBlockToAir(pos);
                var9 = true;
            }

            if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown())) {
                worldIn.setBlockToAir(pos);
                var9 = true;

                if (var7.getBlock() == this) {
                    worldIn.setBlockToAir(var10);
                }
            }

            if (var9) {
                if (!worldIn.isRemote) {
                    dropBlockAsItem(worldIn, pos, state, 0);
                }
            } else {
                boolean var8 = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(var10);

                if ((var8 || neighborBlock.canProvidePower()) && neighborBlock != this && var8 != ((Boolean) var7.getValue(BlockDoor.POWERED_PROP)).booleanValue()) {
                    worldIn.setBlockState(var10, var7.withProperty(BlockDoor.POWERED_PROP, Boolean.valueOf(var8)), 2);

                    if (var8 != ((Boolean) state.getValue(BlockDoor.OPEN_PROP)).booleanValue()) {
                        worldIn.setBlockState(pos, state.withProperty(BlockDoor.OPEN_PROP, Boolean.valueOf(var8)), 2);
                        worldIn.markBlockRangeForRenderUpdate(pos, pos);
                        worldIn.playAuxSFXAtEntity((EntityPlayer) null, var8 ? 1003 : 1006, pos, 0);
                    }
                }
            }
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(BlockDoor.HALF_PROP) == BlockDoor.EnumDoorHalf.UPPER ? null : func_176509_j();
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector
     * returning a ray trace hit.
     *
     * @param start The start vector
     * @param end   The end vector
     */
    @Override
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
        setBlockBoundsBasedOnState(worldIn, pos);
        return super.collisionRayTrace(worldIn, pos, start, end);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return pos.getY() >= 255 ? false : World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) && super.canPlaceBlockAt(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos.offsetUp());
    }

    @Override
    public int getMobilityFlag() {
        return 1;
    }

    public static int func_176515_e(IBlockAccess p_176515_0_, BlockPos p_176515_1_) {
        IBlockState var2 = p_176515_0_.getBlockState(p_176515_1_);
        int var3 = var2.getBlock().getMetaFromState(var2);
        boolean var4 = BlockDoor.func_176518_i(var3);
        IBlockState var5 = p_176515_0_.getBlockState(p_176515_1_.offsetDown());
        int var6 = var5.getBlock().getMetaFromState(var5);
        int var7 = var4 ? var6 : var3;
        IBlockState var8 = p_176515_0_.getBlockState(p_176515_1_.offsetUp());
        int var9 = var8.getBlock().getMetaFromState(var8);
        int var10 = var4 ? var3 : var9;
        boolean var11 = (var10 & 1) != 0;
        boolean var12 = (var10 & 2) != 0;
        return BlockDoor.func_176510_b(var7) | (var4 ? 8 : 0) | (var11 ? 16 : 0) | (var12 ? 32 : 0);
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return func_176509_j();
    }

    private Item func_176509_j() {
        return this == Blocks.iron_door ? Items.iron_door : (this == Blocks.spruce_door ? Items.spruce_door : (this == Blocks.birch_door ? Items.birch_door : (this == Blocks.jungle_door ? Items.jungle_door : (this == Blocks.acacia_door ? Items.acacia_door : (this == Blocks.dark_oak_door ? Items.dark_oak_door : Items.oak_door)))));
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
        BlockPos var5 = pos.offsetDown();

        if (playerIn.capabilities.isCreativeMode && state.getValue(BlockDoor.HALF_PROP) == BlockDoor.EnumDoorHalf.UPPER && worldIn.getBlockState(var5).getBlock() == this) {
            worldIn.setBlockToAir(var5);
        }
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    /**
     * Get the actual Block state of this Block at the given position. This
     * applies properties not visible in the metadata, such as fence
     * connections.
     */
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        IBlockState var4;

        if (state.getValue(BlockDoor.HALF_PROP) == BlockDoor.EnumDoorHalf.LOWER) {
            var4 = worldIn.getBlockState(pos.offsetUp());

            if (var4.getBlock() == this) {
                state = state.withProperty(BlockDoor.HINGEPOSITION_PROP, var4.getValue(BlockDoor.HINGEPOSITION_PROP)).withProperty(BlockDoor.POWERED_PROP, var4.getValue(BlockDoor.POWERED_PROP));
            }
        } else {
            var4 = worldIn.getBlockState(pos.offsetDown());

            if (var4.getBlock() == this) {
                state = state.withProperty(BlockDoor.FACING_PROP, var4.getValue(BlockDoor.FACING_PROP)).withProperty(BlockDoor.OPEN_PROP, var4.getValue(BlockDoor.OPEN_PROP));
            }
        }

        return state;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return (meta & 8) > 0 ? getDefaultState().withProperty(BlockDoor.HALF_PROP, BlockDoor.EnumDoorHalf.UPPER).withProperty(BlockDoor.HINGEPOSITION_PROP, (meta & 1) > 0 ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT).withProperty(BlockDoor.POWERED_PROP, Boolean.valueOf((meta & 2) > 0)) : getDefaultState().withProperty(BlockDoor.HALF_PROP, BlockDoor.EnumDoorHalf.LOWER).withProperty(BlockDoor.FACING_PROP, EnumFacing.getHorizontal(meta & 3).rotateYCCW()).withProperty(BlockDoor.OPEN_PROP, Boolean.valueOf((meta & 4) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        byte var2 = 0;
        int var3;

        if (state.getValue(BlockDoor.HALF_PROP) == BlockDoor.EnumDoorHalf.UPPER) {
            var3 = var2 | 8;

            if (state.getValue(BlockDoor.HINGEPOSITION_PROP) == BlockDoor.EnumHingePosition.RIGHT) {
                var3 |= 1;
            }

            if (((Boolean) state.getValue(BlockDoor.POWERED_PROP)).booleanValue()) {
                var3 |= 2;
            }
        } else {
            var3 = var2 | ((EnumFacing) state.getValue(BlockDoor.FACING_PROP)).rotateY().getHorizontalIndex();

            if (((Boolean) state.getValue(BlockDoor.OPEN_PROP)).booleanValue()) {
                var3 |= 4;
            }
        }

        return var3;
    }

    protected static int func_176510_b(int p_176510_0_) {
        return p_176510_0_ & 7;
    }

    public static boolean func_176514_f(IBlockAccess p_176514_0_, BlockPos p_176514_1_) {
        return BlockDoor.func_176516_g(BlockDoor.func_176515_e(p_176514_0_, p_176514_1_));
    }

    public static EnumFacing func_176517_h(IBlockAccess p_176517_0_, BlockPos p_176517_1_) {
        return BlockDoor.func_176511_f(BlockDoor.func_176515_e(p_176517_0_, p_176517_1_));
    }

    public static EnumFacing func_176511_f(int p_176511_0_) {
        return EnumFacing.getHorizontal(p_176511_0_ & 3).rotateYCCW();
    }

    protected static boolean func_176516_g(int p_176516_0_) {
        return (p_176516_0_ & 4) != 0;
    }

    protected static boolean func_176518_i(int p_176518_0_) {
        return (p_176518_0_ & 8) != 0;
    }

    protected static boolean func_176513_j(int p_176513_0_) {
        return (p_176513_0_ & 16) != 0;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockDoor.HALF_PROP, BlockDoor.FACING_PROP, BlockDoor.OPEN_PROP, BlockDoor.HINGEPOSITION_PROP, BlockDoor.POWERED_PROP});
    }

    public static enum EnumDoorHalf implements IStringSerializable {
        UPPER("UPPER", 0), LOWER("LOWER", 1);

        private static final BlockDoor.EnumDoorHalf[] $VALUES = new BlockDoor.EnumDoorHalf[]{UPPER, LOWER};
        private static final String __OBFID = "CL_00002124";

        private EnumDoorHalf(String p_i45726_1_, int p_i45726_2_) {
        }

        @Override
        public String toString() {
            return getName();
        }

        @Override
        public String getName() {
            return this == UPPER ? "upper" : "lower";
        }
    }

    public static enum EnumHingePosition implements IStringSerializable {
        LEFT("LEFT", 0), RIGHT("RIGHT", 1);

        private static final BlockDoor.EnumHingePosition[] $VALUES = new BlockDoor.EnumHingePosition[]{LEFT, RIGHT};
        private static final String __OBFID = "CL_00002123";

        private EnumHingePosition(String p_i45725_1_, int p_i45725_2_) {
        }

        @Override
        public String toString() {
            return getName();
        }

        @Override
        public String getName() {
            return this == LEFT ? "left" : "right";
        }
    }
}
