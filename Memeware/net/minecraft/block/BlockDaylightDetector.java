package net.minecraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDaylightDetector;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDaylightDetector extends BlockContainer {
    public static final PropertyInteger field_176436_a = PropertyInteger.create("power", 0, 15);
    private final boolean field_176435_b;
    private static final String __OBFID = "CL_00000223";

    public BlockDaylightDetector(boolean p_i45729_1_) {
        super(Material.wood);
        this.field_176435_b = p_i45729_1_;
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176436_a, Integer.valueOf(0)));
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setHardness(0.2F);
        this.setStepSound(soundTypeWood);
        this.setUnlocalizedName("daylightDetector");
    }

    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
    }

    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        return ((Integer) state.getValue(field_176436_a)).intValue();
    }

    public void func_180677_d(World worldIn, BlockPos p_180677_2_) {
        if (!worldIn.provider.getHasNoSky()) {
            IBlockState var3 = worldIn.getBlockState(p_180677_2_);
            int var4 = worldIn.getLightFor(EnumSkyBlock.SKY, p_180677_2_) - worldIn.getSkylightSubtracted();
            float var5 = worldIn.getCelestialAngleRadians(1.0F);
            float var6 = var5 < (float) Math.PI ? 0.0F : ((float) Math.PI * 2F);
            var5 += (var6 - var5) * 0.2F;
            var4 = Math.round((float) var4 * MathHelper.cos(var5));
            var4 = MathHelper.clamp_int(var4, 0, 15);

            if (this.field_176435_b) {
                var4 = 15 - var4;
            }

            if (((Integer) var3.getValue(field_176436_a)).intValue() != var4) {
                worldIn.setBlockState(p_180677_2_, var3.withProperty(field_176436_a, Integer.valueOf(var4)), 3);
            }
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (playerIn.func_175142_cm()) {
            if (worldIn.isRemote) {
                return true;
            } else {
                if (this.field_176435_b) {
                    worldIn.setBlockState(pos, Blocks.daylight_detector.getDefaultState().withProperty(field_176436_a, state.getValue(field_176436_a)), 4);
                    Blocks.daylight_detector.func_180677_d(worldIn, pos);
                } else {
                    worldIn.setBlockState(pos, Blocks.daylight_detector_inverted.getDefaultState().withProperty(field_176436_a, state.getValue(field_176436_a)), 4);
                    Blocks.daylight_detector_inverted.func_180677_d(worldIn, pos);
                }

                return true;
            }
        } else {
            return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.daylight_detector);
    }

    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock(Blocks.daylight_detector);
    }

    public boolean isFullCube() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType() {
        return 3;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower() {
        return true;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDaylightDetector();
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(field_176436_a, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return ((Integer) state.getValue(field_176436_a)).intValue();
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{field_176436_a});
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        if (!this.field_176435_b) {
            super.getSubBlocks(itemIn, tab, list);
        }
    }
}
