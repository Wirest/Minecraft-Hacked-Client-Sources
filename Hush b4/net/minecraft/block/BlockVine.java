// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.entity.EntityLivingBase;
import java.util.Random;
import net.minecraft.world.ColorizerFoliage;
import java.util.Iterator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockVine extends Block
{
    public static final PropertyBool UP;
    public static final PropertyBool NORTH;
    public static final PropertyBool EAST;
    public static final PropertyBool SOUTH;
    public static final PropertyBool WEST;
    public static final PropertyBool[] ALL_FACES;
    
    static {
        UP = PropertyBool.create("up");
        NORTH = PropertyBool.create("north");
        EAST = PropertyBool.create("east");
        SOUTH = PropertyBool.create("south");
        WEST = PropertyBool.create("west");
        ALL_FACES = new PropertyBool[] { BlockVine.UP, BlockVine.NORTH, BlockVine.SOUTH, BlockVine.WEST, BlockVine.EAST };
    }
    
    public BlockVine() {
        super(Material.vine);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockVine.UP, false).withProperty((IProperty<Comparable>)BlockVine.NORTH, false).withProperty((IProperty<Comparable>)BlockVine.EAST, false).withProperty((IProperty<Comparable>)BlockVine.SOUTH, false).withProperty((IProperty<Comparable>)BlockVine.WEST, false));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return state.withProperty((IProperty<Comparable>)BlockVine.UP, worldIn.getBlockState(pos.up()).getBlock().isBlockNormalCube());
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean isReplaceable(final World worldIn, final BlockPos pos) {
        return true;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess worldIn, final BlockPos pos) {
        final float f = 0.0625f;
        float f2 = 1.0f;
        float f3 = 1.0f;
        float f4 = 1.0f;
        float f5 = 0.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        boolean flag = false;
        if (worldIn.getBlockState(pos).getValue((IProperty<Boolean>)BlockVine.WEST)) {
            f5 = Math.max(f5, 0.0625f);
            f2 = 0.0f;
            f3 = 0.0f;
            f6 = 1.0f;
            f4 = 0.0f;
            f7 = 1.0f;
            flag = true;
        }
        if (worldIn.getBlockState(pos).getValue((IProperty<Boolean>)BlockVine.EAST)) {
            f2 = Math.min(f2, 0.9375f);
            f5 = 1.0f;
            f3 = 0.0f;
            f6 = 1.0f;
            f4 = 0.0f;
            f7 = 1.0f;
            flag = true;
        }
        if (worldIn.getBlockState(pos).getValue((IProperty<Boolean>)BlockVine.NORTH)) {
            f7 = Math.max(f7, 0.0625f);
            f4 = 0.0f;
            f2 = 0.0f;
            f5 = 1.0f;
            f3 = 0.0f;
            f6 = 1.0f;
            flag = true;
        }
        if (worldIn.getBlockState(pos).getValue((IProperty<Boolean>)BlockVine.SOUTH)) {
            f4 = Math.min(f4, 0.9375f);
            f7 = 1.0f;
            f2 = 0.0f;
            f5 = 1.0f;
            f3 = 0.0f;
            f6 = 1.0f;
            flag = true;
        }
        if (!flag && this.canPlaceOn(worldIn.getBlockState(pos.up()).getBlock())) {
            f3 = Math.min(f3, 0.9375f);
            f6 = 1.0f;
            f2 = 0.0f;
            f5 = 1.0f;
            f4 = 0.0f;
            f7 = 1.0f;
        }
        this.setBlockBounds(f2, f3, f4, f5, f6, f7);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
    }
    
    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos, final EnumFacing side) {
        switch (side) {
            case UP: {
                return this.canPlaceOn(worldIn.getBlockState(pos.up()).getBlock());
            }
            case NORTH:
            case SOUTH:
            case WEST:
            case EAST: {
                return this.canPlaceOn(worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock());
            }
            default: {
                return false;
            }
        }
    }
    
    private boolean canPlaceOn(final Block blockIn) {
        return blockIn.isFullCube() && blockIn.blockMaterial.blocksMovement();
    }
    
    private boolean recheckGrownSides(final World worldIn, final BlockPos pos, IBlockState state) {
        final IBlockState iblockstate = state;
        for (final Object enumfacing : EnumFacing.Plane.HORIZONTAL) {
            final PropertyBool propertybool = getPropertyFor((EnumFacing)enumfacing);
            if (state.getValue((IProperty<Boolean>)propertybool) && !this.canPlaceOn(worldIn.getBlockState(pos.offset((EnumFacing)enumfacing)).getBlock())) {
                final IBlockState iblockstate2 = worldIn.getBlockState(pos.up());
                if (iblockstate2.getBlock() == this && iblockstate2.getValue((IProperty<Boolean>)propertybool)) {
                    continue;
                }
                state = state.withProperty((IProperty<Comparable>)propertybool, false);
            }
        }
        if (getNumGrownFaces(state) == 0) {
            return false;
        }
        if (iblockstate != state) {
            worldIn.setBlockState(pos, state, 2);
        }
        return true;
    }
    
    @Override
    public int getBlockColor() {
        return ColorizerFoliage.getFoliageColorBasic();
    }
    
    @Override
    public int getRenderColor(final IBlockState state) {
        return ColorizerFoliage.getFoliageColorBasic();
    }
    
    @Override
    public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass) {
        return worldIn.getBiomeGenForCoords(pos).getFoliageColorAtPos(pos);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        if (!worldIn.isRemote && !this.recheckGrownSides(worldIn, pos, state)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote && worldIn.rand.nextInt(4) == 0) {
            final int i = 4;
            int j = 5;
            boolean flag = false;
        Label_0115:
            for (int k = -i; k <= i; ++k) {
                for (int l = -i; l <= i; ++l) {
                    for (int i2 = -1; i2 <= 1; ++i2) {
                        if (worldIn.getBlockState(pos.add(k, i2, l)).getBlock() == this && --j <= 0) {
                            flag = true;
                            break Label_0115;
                        }
                    }
                }
            }
            final EnumFacing enumfacing1 = EnumFacing.random(rand);
            final BlockPos blockpos1 = pos.up();
            if (enumfacing1 == EnumFacing.UP && pos.getY() < 255 && worldIn.isAirBlock(blockpos1)) {
                if (!flag) {
                    IBlockState iblockstate2 = state;
                    for (final Object enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
                        if (rand.nextBoolean() || !this.canPlaceOn(worldIn.getBlockState(blockpos1.offset((EnumFacing)enumfacing2)).getBlock())) {
                            iblockstate2 = iblockstate2.withProperty((IProperty<Comparable>)getPropertyFor((EnumFacing)enumfacing2), false);
                        }
                    }
                    if (iblockstate2.getValue((IProperty<Boolean>)BlockVine.NORTH) || iblockstate2.getValue((IProperty<Boolean>)BlockVine.EAST) || iblockstate2.getValue((IProperty<Boolean>)BlockVine.SOUTH) || iblockstate2.getValue((IProperty<Boolean>)BlockVine.WEST)) {
                        worldIn.setBlockState(blockpos1, iblockstate2, 2);
                    }
                }
            }
            else if (enumfacing1.getAxis().isHorizontal() && !state.getValue((IProperty<Boolean>)getPropertyFor(enumfacing1))) {
                if (!flag) {
                    final BlockPos blockpos2 = pos.offset(enumfacing1);
                    final Block block1 = worldIn.getBlockState(blockpos2).getBlock();
                    if (block1.blockMaterial == Material.air) {
                        final EnumFacing enumfacing3 = enumfacing1.rotateY();
                        final EnumFacing enumfacing4 = enumfacing1.rotateYCCW();
                        final boolean flag2 = state.getValue((IProperty<Boolean>)getPropertyFor(enumfacing3));
                        final boolean flag3 = state.getValue((IProperty<Boolean>)getPropertyFor(enumfacing4));
                        final BlockPos blockpos3 = blockpos2.offset(enumfacing3);
                        final BlockPos blockpos4 = blockpos2.offset(enumfacing4);
                        if (flag2 && this.canPlaceOn(worldIn.getBlockState(blockpos3).getBlock())) {
                            worldIn.setBlockState(blockpos2, this.getDefaultState().withProperty((IProperty<Comparable>)getPropertyFor(enumfacing3), true), 2);
                        }
                        else if (flag3 && this.canPlaceOn(worldIn.getBlockState(blockpos4).getBlock())) {
                            worldIn.setBlockState(blockpos2, this.getDefaultState().withProperty((IProperty<Comparable>)getPropertyFor(enumfacing4), true), 2);
                        }
                        else if (flag2 && worldIn.isAirBlock(blockpos3) && this.canPlaceOn(worldIn.getBlockState(pos.offset(enumfacing3)).getBlock())) {
                            worldIn.setBlockState(blockpos3, this.getDefaultState().withProperty((IProperty<Comparable>)getPropertyFor(enumfacing1.getOpposite()), true), 2);
                        }
                        else if (flag3 && worldIn.isAirBlock(blockpos4) && this.canPlaceOn(worldIn.getBlockState(pos.offset(enumfacing4)).getBlock())) {
                            worldIn.setBlockState(blockpos4, this.getDefaultState().withProperty((IProperty<Comparable>)getPropertyFor(enumfacing1.getOpposite()), true), 2);
                        }
                        else if (this.canPlaceOn(worldIn.getBlockState(blockpos2.up()).getBlock())) {
                            worldIn.setBlockState(blockpos2, this.getDefaultState(), 2);
                        }
                    }
                    else if (block1.blockMaterial.isOpaque() && block1.isFullCube()) {
                        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)getPropertyFor(enumfacing1), true), 2);
                    }
                }
            }
            else if (pos.getY() > 1) {
                final BlockPos blockpos5 = pos.down();
                final IBlockState iblockstate3 = worldIn.getBlockState(blockpos5);
                final Block block2 = iblockstate3.getBlock();
                if (block2.blockMaterial == Material.air) {
                    IBlockState iblockstate4 = state;
                    for (final Object enumfacing5 : EnumFacing.Plane.HORIZONTAL) {
                        if (rand.nextBoolean()) {
                            iblockstate4 = iblockstate4.withProperty((IProperty<Comparable>)getPropertyFor((EnumFacing)enumfacing5), false);
                        }
                    }
                    if (iblockstate4.getValue((IProperty<Boolean>)BlockVine.NORTH) || iblockstate4.getValue((IProperty<Boolean>)BlockVine.EAST) || iblockstate4.getValue((IProperty<Boolean>)BlockVine.SOUTH) || iblockstate4.getValue((IProperty<Boolean>)BlockVine.WEST)) {
                        worldIn.setBlockState(blockpos5, iblockstate4, 2);
                    }
                }
                else if (block2 == this) {
                    IBlockState iblockstate5 = iblockstate3;
                    for (final Object enumfacing6 : EnumFacing.Plane.HORIZONTAL) {
                        final PropertyBool propertybool = getPropertyFor((EnumFacing)enumfacing6);
                        if (rand.nextBoolean() && state.getValue((IProperty<Boolean>)propertybool)) {
                            iblockstate5 = iblockstate5.withProperty((IProperty<Comparable>)propertybool, true);
                        }
                    }
                    if (iblockstate5.getValue((IProperty<Boolean>)BlockVine.NORTH) || iblockstate5.getValue((IProperty<Boolean>)BlockVine.EAST) || iblockstate5.getValue((IProperty<Boolean>)BlockVine.SOUTH) || iblockstate5.getValue((IProperty<Boolean>)BlockVine.WEST)) {
                        worldIn.setBlockState(blockpos5, iblockstate5, 2);
                    }
                }
            }
        }
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        final IBlockState iblockstate = this.getDefaultState().withProperty((IProperty<Comparable>)BlockVine.UP, false).withProperty((IProperty<Comparable>)BlockVine.NORTH, false).withProperty((IProperty<Comparable>)BlockVine.EAST, false).withProperty((IProperty<Comparable>)BlockVine.SOUTH, false).withProperty((IProperty<Comparable>)BlockVine.WEST, false);
        return facing.getAxis().isHorizontal() ? iblockstate.withProperty((IProperty<Comparable>)getPropertyFor(facing.getOpposite()), true) : iblockstate;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return null;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer player, final BlockPos pos, final IBlockState state, final TileEntity te) {
        if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears) {
            player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Blocks.vine, 1, 0));
        }
        else {
            super.harvestBlock(worldIn, player, pos, state, te);
        }
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockVine.SOUTH, (meta & 0x1) > 0).withProperty((IProperty<Comparable>)BlockVine.WEST, (meta & 0x2) > 0).withProperty((IProperty<Comparable>)BlockVine.NORTH, (meta & 0x4) > 0).withProperty((IProperty<Comparable>)BlockVine.EAST, (meta & 0x8) > 0);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        if (state.getValue((IProperty<Boolean>)BlockVine.SOUTH)) {
            i |= 0x1;
        }
        if (state.getValue((IProperty<Boolean>)BlockVine.WEST)) {
            i |= 0x2;
        }
        if (state.getValue((IProperty<Boolean>)BlockVine.NORTH)) {
            i |= 0x4;
        }
        if (state.getValue((IProperty<Boolean>)BlockVine.EAST)) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockVine.UP, BlockVine.NORTH, BlockVine.EAST, BlockVine.SOUTH, BlockVine.WEST });
    }
    
    public static PropertyBool getPropertyFor(final EnumFacing side) {
        switch (side) {
            case UP: {
                return BlockVine.UP;
            }
            case NORTH: {
                return BlockVine.NORTH;
            }
            case SOUTH: {
                return BlockVine.SOUTH;
            }
            case EAST: {
                return BlockVine.EAST;
            }
            case WEST: {
                return BlockVine.WEST;
            }
            default: {
                throw new IllegalArgumentException(side + " is an invalid choice");
            }
        }
    }
    
    public static int getNumGrownFaces(final IBlockState state) {
        int i = 0;
        PropertyBool[] all_FACES;
        for (int length = (all_FACES = BlockVine.ALL_FACES).length, j = 0; j < length; ++j) {
            final PropertyBool propertybool = all_FACES[j];
            if (state.getValue((IProperty<Boolean>)propertybool)) {
                ++i;
            }
        }
        return i;
    }
}
