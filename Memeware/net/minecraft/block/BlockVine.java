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

public class BlockVine extends Block {
    public static final PropertyBool field_176277_a = PropertyBool.create("up");
    public static final PropertyBool field_176273_b = PropertyBool.create("north");
    public static final PropertyBool field_176278_M = PropertyBool.create("east");
    public static final PropertyBool field_176279_N = PropertyBool.create("south");
    public static final PropertyBool field_176280_O = PropertyBool.create("west");
    public static final PropertyBool[] field_176274_P = new PropertyBool[]{field_176277_a, field_176273_b, field_176279_N, field_176280_O, field_176278_M};
    public static final int field_176272_Q = func_176270_b(EnumFacing.SOUTH);
    public static final int field_176276_R = func_176270_b(EnumFacing.NORTH);
    public static final int field_176275_S = func_176270_b(EnumFacing.EAST);
    public static final int field_176271_T = func_176270_b(EnumFacing.WEST);
    private static final String __OBFID = "CL_00000330";

    public BlockVine() {
        super(Material.vine);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176277_a, Boolean.valueOf(false)).withProperty(field_176273_b, Boolean.valueOf(false)).withProperty(field_176278_M, Boolean.valueOf(false)).withProperty(field_176279_N, Boolean.valueOf(false)).withProperty(field_176280_O, Boolean.valueOf(false)));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(field_176277_a, Boolean.valueOf(worldIn.getBlockState(pos.offsetUp()).getBlock().isSolidFullCube()));
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    /**
     * Whether this Block can be replaced directly by other blocks (true for e.g. tall grass)
     */
    public boolean isReplaceable(World worldIn, BlockPos pos) {
        return true;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        float var3 = 0.0625F;
        float var4 = 1.0F;
        float var5 = 1.0F;
        float var6 = 1.0F;
        float var7 = 0.0F;
        float var8 = 0.0F;
        float var9 = 0.0F;
        boolean var10 = false;

        if (((Boolean) access.getBlockState(pos).getValue(field_176280_O)).booleanValue()) {
            var7 = Math.max(var7, 0.0625F);
            var4 = 0.0F;
            var5 = 0.0F;
            var8 = 1.0F;
            var6 = 0.0F;
            var9 = 1.0F;
            var10 = true;
        }

        if (((Boolean) access.getBlockState(pos).getValue(field_176278_M)).booleanValue()) {
            var4 = Math.min(var4, 0.9375F);
            var7 = 1.0F;
            var5 = 0.0F;
            var8 = 1.0F;
            var6 = 0.0F;
            var9 = 1.0F;
            var10 = true;
        }

        if (((Boolean) access.getBlockState(pos).getValue(field_176273_b)).booleanValue()) {
            var9 = Math.max(var9, 0.0625F);
            var6 = 0.0F;
            var4 = 0.0F;
            var7 = 1.0F;
            var5 = 0.0F;
            var8 = 1.0F;
            var10 = true;
        }

        if (((Boolean) access.getBlockState(pos).getValue(field_176279_N)).booleanValue()) {
            var6 = Math.min(var6, 0.9375F);
            var9 = 1.0F;
            var4 = 0.0F;
            var7 = 1.0F;
            var5 = 0.0F;
            var8 = 1.0F;
            var10 = true;
        }

        if (!var10 && this.func_150093_a(access.getBlockState(pos.offsetUp()).getBlock())) {
            var5 = Math.min(var5, 0.9375F);
            var8 = 1.0F;
            var4 = 0.0F;
            var7 = 1.0F;
            var6 = 0.0F;
            var9 = 1.0F;
        }

        this.setBlockBounds(var4, var5, var6, var7, var8, var9);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    /**
     * Check whether this Block can be placed on the given side
     */
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        switch (BlockVine.SwitchEnumFacing.field_177057_a[side.ordinal()]) {
            case 1:
                return this.func_150093_a(worldIn.getBlockState(pos.offsetUp()).getBlock());

            case 2:
            case 3:
            case 4:
            case 5:
                return this.func_150093_a(worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock());

            default:
                return false;
        }
    }

    private boolean func_150093_a(Block p_150093_1_) {
        return p_150093_1_.isFullCube() && p_150093_1_.blockMaterial.blocksMovement();
    }

    private boolean func_176269_e(World worldIn, BlockPos p_176269_2_, IBlockState p_176269_3_) {
        IBlockState var4 = p_176269_3_;
        Iterator var5 = EnumFacing.Plane.HORIZONTAL.iterator();

        while (var5.hasNext()) {
            EnumFacing var6 = (EnumFacing) var5.next();
            PropertyBool var7 = func_176267_a(var6);

            if (((Boolean) p_176269_3_.getValue(var7)).booleanValue() && !this.func_150093_a(worldIn.getBlockState(p_176269_2_.offset(var6)).getBlock())) {
                IBlockState var8 = worldIn.getBlockState(p_176269_2_.offsetUp());

                if (var8.getBlock() != this || !((Boolean) var8.getValue(var7)).booleanValue()) {
                    p_176269_3_ = p_176269_3_.withProperty(var7, Boolean.valueOf(false));
                }
            }
        }

        if (func_176268_d(p_176269_3_) == 0) {
            return false;
        } else {
            if (var4 != p_176269_3_) {
                worldIn.setBlockState(p_176269_2_, p_176269_3_, 2);
            }

            return true;
        }
    }

    public int getBlockColor() {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    public int getRenderColor(IBlockState state) {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        return worldIn.getBiomeGenForCoords(pos).func_180625_c(pos);
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!worldIn.isRemote && !this.func_176269_e(worldIn, pos, state)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            if (worldIn.rand.nextInt(4) == 0) {
                byte var5 = 4;
                int var6 = 5;
                boolean var7 = false;
                label189:

                for (int var8 = -var5; var8 <= var5; ++var8) {
                    for (int var9 = -var5; var9 <= var5; ++var9) {
                        for (int var10 = -1; var10 <= 1; ++var10) {
                            if (worldIn.getBlockState(pos.add(var8, var10, var9)).getBlock() == this) {
                                --var6;

                                if (var6 <= 0) {
                                    var7 = true;
                                    break label189;
                                }
                            }
                        }
                    }
                }

                EnumFacing var17 = EnumFacing.random(rand);
                EnumFacing var23;

                if (var17 == EnumFacing.UP && pos.getY() < 255 && worldIn.isAirBlock(pos.offsetUp())) {
                    if (!var7) {
                        IBlockState var19 = state;
                        Iterator var22 = EnumFacing.Plane.HORIZONTAL.iterator();

                        while (var22.hasNext()) {
                            var23 = (EnumFacing) var22.next();

                            if (rand.nextBoolean() || !this.func_150093_a(worldIn.getBlockState(pos.offset(var23).offsetUp()).getBlock())) {
                                var19 = var19.withProperty(func_176267_a(var23), Boolean.valueOf(false));
                            }
                        }

                        if (((Boolean) var19.getValue(field_176273_b)).booleanValue() || ((Boolean) var19.getValue(field_176278_M)).booleanValue() || ((Boolean) var19.getValue(field_176279_N)).booleanValue() || ((Boolean) var19.getValue(field_176280_O)).booleanValue()) {
                            worldIn.setBlockState(pos.offsetUp(), var19, 2);
                        }
                    }
                } else {
                    BlockPos var18;

                    if (var17.getAxis().isHorizontal() && !((Boolean) state.getValue(func_176267_a(var17))).booleanValue()) {
                        if (!var7) {
                            var18 = pos.offset(var17);
                            Block var21 = worldIn.getBlockState(var18).getBlock();

                            if (var21.blockMaterial == Material.air) {
                                var23 = var17.rotateY();
                                EnumFacing var24 = var17.rotateYCCW();
                                boolean var25 = ((Boolean) state.getValue(func_176267_a(var23))).booleanValue();
                                boolean var26 = ((Boolean) state.getValue(func_176267_a(var24))).booleanValue();
                                BlockPos var27 = var18.offset(var23);
                                BlockPos var16 = var18.offset(var24);

                                if (var25 && this.func_150093_a(worldIn.getBlockState(var27).getBlock())) {
                                    worldIn.setBlockState(var18, this.getDefaultState().withProperty(func_176267_a(var23), Boolean.valueOf(true)), 2);
                                } else if (var26 && this.func_150093_a(worldIn.getBlockState(var16).getBlock())) {
                                    worldIn.setBlockState(var18, this.getDefaultState().withProperty(func_176267_a(var24), Boolean.valueOf(true)), 2);
                                } else if (var25 && worldIn.isAirBlock(var27) && this.func_150093_a(worldIn.getBlockState(pos.offset(var23)).getBlock())) {
                                    worldIn.setBlockState(var27, this.getDefaultState().withProperty(func_176267_a(var17.getOpposite()), Boolean.valueOf(true)), 2);
                                } else if (var26 && worldIn.isAirBlock(var16) && this.func_150093_a(worldIn.getBlockState(pos.offset(var24)).getBlock())) {
                                    worldIn.setBlockState(var16, this.getDefaultState().withProperty(func_176267_a(var17.getOpposite()), Boolean.valueOf(true)), 2);
                                } else if (this.func_150093_a(worldIn.getBlockState(var18.offsetUp()).getBlock())) {
                                    worldIn.setBlockState(var18, this.getDefaultState(), 2);
                                }
                            } else if (var21.blockMaterial.isOpaque() && var21.isFullCube()) {
                                worldIn.setBlockState(pos, state.withProperty(func_176267_a(var17), Boolean.valueOf(true)), 2);
                            }
                        }
                    } else {
                        if (pos.getY() > 1) {
                            var18 = pos.offsetDown();
                            IBlockState var20 = worldIn.getBlockState(var18);
                            Block var11 = var20.getBlock();
                            IBlockState var12;
                            Iterator var13;
                            EnumFacing var14;

                            if (var11.blockMaterial == Material.air) {
                                var12 = state;
                                var13 = EnumFacing.Plane.HORIZONTAL.iterator();

                                while (var13.hasNext()) {
                                    var14 = (EnumFacing) var13.next();

                                    if (rand.nextBoolean()) {
                                        var12 = var12.withProperty(func_176267_a(var14), Boolean.valueOf(false));
                                    }
                                }

                                if (((Boolean) var12.getValue(field_176273_b)).booleanValue() || ((Boolean) var12.getValue(field_176278_M)).booleanValue() || ((Boolean) var12.getValue(field_176279_N)).booleanValue() || ((Boolean) var12.getValue(field_176280_O)).booleanValue()) {
                                    worldIn.setBlockState(var18, var12, 2);
                                }
                            } else if (var11 == this) {
                                var12 = var20;
                                var13 = EnumFacing.Plane.HORIZONTAL.iterator();

                                while (var13.hasNext()) {
                                    var14 = (EnumFacing) var13.next();
                                    PropertyBool var15 = func_176267_a(var14);

                                    if (rand.nextBoolean() || !((Boolean) state.getValue(var15)).booleanValue()) {
                                        var12 = var12.withProperty(var15, Boolean.valueOf(false));
                                    }
                                }

                                if (((Boolean) var12.getValue(field_176273_b)).booleanValue() || ((Boolean) var12.getValue(field_176278_M)).booleanValue() || ((Boolean) var12.getValue(field_176279_N)).booleanValue() || ((Boolean) var12.getValue(field_176280_O)).booleanValue()) {
                                    worldIn.setBlockState(var18, var12, 2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static int func_176270_b(EnumFacing p_176270_0_) {
        return 1 << p_176270_0_.getHorizontalIndex();
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState var9 = this.getDefaultState().withProperty(field_176277_a, Boolean.valueOf(false)).withProperty(field_176273_b, Boolean.valueOf(false)).withProperty(field_176278_M, Boolean.valueOf(false)).withProperty(field_176279_N, Boolean.valueOf(false)).withProperty(field_176280_O, Boolean.valueOf(false));
        return facing.getAxis().isHorizontal() ? var9.withProperty(func_176267_a(facing.getOpposite()), Boolean.valueOf(true)) : var9;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random) {
        return 0;
    }

    public void harvestBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, IBlockState state, TileEntity te) {
        if (!worldIn.isRemote && playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == Items.shears) {
            playerIn.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
            spawnAsEntity(worldIn, pos, new ItemStack(Blocks.vine, 1, 0));
        } else {
            super.harvestBlock(worldIn, playerIn, pos, state, te);
        }
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176273_b, Boolean.valueOf((meta & field_176276_R) > 0)).withProperty(field_176278_M, Boolean.valueOf((meta & field_176275_S) > 0)).withProperty(field_176279_N, Boolean.valueOf((meta & field_176272_Q) > 0)).withProperty(field_176280_O, Boolean.valueOf((meta & field_176271_T) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;

        if (((Boolean) state.getValue(field_176273_b)).booleanValue()) {
            var2 |= field_176276_R;
        }

        if (((Boolean) state.getValue(field_176278_M)).booleanValue()) {
            var2 |= field_176275_S;
        }

        if (((Boolean) state.getValue(field_176279_N)).booleanValue()) {
            var2 |= field_176272_Q;
        }

        if (((Boolean) state.getValue(field_176280_O)).booleanValue()) {
            var2 |= field_176271_T;
        }

        return var2;
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{field_176277_a, field_176273_b, field_176278_M, field_176279_N, field_176280_O});
    }

    public static PropertyBool func_176267_a(EnumFacing p_176267_0_) {
        switch (BlockVine.SwitchEnumFacing.field_177057_a[p_176267_0_.ordinal()]) {
            case 1:
                return field_176277_a;

            case 2:
                return field_176273_b;

            case 3:
                return field_176279_N;

            case 4:
                return field_176278_M;

            case 5:
                return field_176280_O;

            default:
                throw new IllegalArgumentException(p_176267_0_ + " is an invalid choice");
        }
    }

    public static int func_176268_d(IBlockState p_176268_0_) {
        int var1 = 0;
        PropertyBool[] var2 = field_176274_P;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            PropertyBool var5 = var2[var4];

            if (((Boolean) p_176268_0_.getValue(var5)).booleanValue()) {
                ++var1;
            }
        }

        return var1;
    }

    static final class SwitchEnumFacing {
        static final int[] field_177057_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002049";

        static {
            try {
                field_177057_a[EnumFacing.UP.ordinal()] = 1;
            } catch (NoSuchFieldError var5) {
                ;
            }

            try {
                field_177057_a[EnumFacing.NORTH.ordinal()] = 2;
            } catch (NoSuchFieldError var4) {
                ;
            }

            try {
                field_177057_a[EnumFacing.SOUTH.ordinal()] = 3;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                field_177057_a[EnumFacing.EAST.ordinal()] = 4;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                field_177057_a[EnumFacing.WEST.ordinal()] = 5;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
