package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
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
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSnow extends Block
{
    public static final PropertyInteger LAYERS_PROP = PropertyInteger.create("layers", 1, 8);
    private static final String __OBFID = "CL_00000309";

    protected BlockSnow()
    {
        super(Material.snow);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LAYERS_PROP, Integer.valueOf(1)));
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBoundsForItemRender();
    }

    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos)
    {
        return ((Integer)blockAccess.getBlockState(pos).getValue(LAYERS_PROP)).intValue() < 5;
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        int var4 = ((Integer)state.getValue(LAYERS_PROP)).intValue() - 1;
        float var5 = 0.125F;
        return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)((float)pos.getY() + (float)var4 * var5), (double)pos.getZ() + this.maxZ);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        this.getBoundsForLayers(0);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos)
    {
        IBlockState var3 = access.getBlockState(pos);
        this.getBoundsForLayers(((Integer)var3.getValue(LAYERS_PROP)).intValue());
    }

    protected void getBoundsForLayers(int p_150154_1_)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, (float)p_150154_1_ / 8.0F, 1.0F);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        IBlockState var3 = worldIn.getBlockState(pos.offsetDown());
        Block var4 = var3.getBlock();
        return var4 != Blocks.ice && var4 != Blocks.packed_ice ? (var4.getMaterial() == Material.leaves ? true : (var4 == this && ((Integer)var3.getValue(LAYERS_PROP)).intValue() == 7 ? true : var4.isOpaqueCube() && var4.blockMaterial.blocksMovement())) : false;
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        this.checkAndDropBlock(worldIn, pos, state);
    }

    private boolean checkAndDropBlock(World worldIn, BlockPos p_176314_2_, IBlockState p_176314_3_)
    {
        if (!this.canPlaceBlockAt(worldIn, p_176314_2_))
        {
            this.dropBlockAsItem(worldIn, p_176314_2_, p_176314_3_, 0);
            worldIn.setBlockToAir(p_176314_2_);
            return false;
        }
        else
        {
            return true;
        }
    }

    public void harvestBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, IBlockState state, TileEntity te)
    {
        spawnAsEntity(worldIn, pos, new ItemStack(Items.snowball, ((Integer)state.getValue(LAYERS_PROP)).intValue() + 1, 0));
        worldIn.setBlockToAir(pos);
        playerIn.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.snowball;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 0;
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (worldIn.getLightFor(EnumSkyBlock.BLOCK, pos) > 11)
        {
            this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
            worldIn.setBlockToAir(pos);
        }
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return side == EnumFacing.UP ? true : super.shouldSideBeRendered(worldIn, pos, side);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(LAYERS_PROP, Integer.valueOf((meta & 7) + 1));
    }

    /**
     * Whether this Block can be replaced directly by other blocks (true for e.g. tall grass)
     */
    public boolean isReplaceable(World worldIn, BlockPos pos)
    {
        return ((Integer)worldIn.getBlockState(pos).getValue(LAYERS_PROP)).intValue() == 1;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(LAYERS_PROP)).intValue() - 1;
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {LAYERS_PROP});
    }
}
