package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockCommandBlock extends BlockContainer {
    public static final PropertyBool TRIGGERED_PROP = PropertyBool.create("triggered");
    private static final String __OBFID = "CL_00000219";

    public BlockCommandBlock() {
        super(Material.iron);
        setDefaultState(blockState.getBaseState().withProperty(BlockCommandBlock.TRIGGERED_PROP, Boolean.valueOf(false)));
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing
     * the block.
     */
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCommandBlock();
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!worldIn.isRemote) {
            boolean var5 = worldIn.isBlockPowered(pos);
            boolean var6 = ((Boolean) state.getValue(BlockCommandBlock.TRIGGERED_PROP)).booleanValue();

            if (var5 && !var6) {
                worldIn.setBlockState(pos, state.withProperty(BlockCommandBlock.TRIGGERED_PROP, Boolean.valueOf(true)), 4);
                worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
            } else if (!var5 && var6) {
                worldIn.setBlockState(pos, state.withProperty(BlockCommandBlock.TRIGGERED_PROP, Boolean.valueOf(false)), 4);
            }
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        TileEntity var5 = worldIn.getTileEntity(pos);

        if (var5 instanceof TileEntityCommandBlock) {
            ((TileEntityCommandBlock) var5).getCommandBlockLogic().trigger(worldIn);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
    }

    /**
     * How many world ticks before ticking
     */
    @Override
    public int tickRate(World worldIn) {
        return 1;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity var9 = worldIn.getTileEntity(pos);
        return var9 instanceof TileEntityCommandBlock ? ((TileEntityCommandBlock) var9).getCommandBlockLogic().func_175574_a(playerIn) : false;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        TileEntity var3 = worldIn.getTileEntity(pos);
        return var3 instanceof TileEntityCommandBlock ? ((TileEntityCommandBlock) var3).getCommandBlockLogic().getSuccessCount() : 0;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity var6 = worldIn.getTileEntity(pos);

        if (var6 instanceof TileEntityCommandBlock) {
            CommandBlockLogic var7 = ((TileEntityCommandBlock) var6).getCommandBlockLogic();

            if (stack.hasDisplayName()) {
                var7.func_145754_b(stack.getDisplayName());
            }

            if (!worldIn.isRemote) {
                var7.func_175573_a(worldIn.getGameRules().getGameRuleBooleanValue("sendCommandFeedback"));
            }
        }
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType() {
        return 3;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockCommandBlock.TRIGGERED_PROP, Boolean.valueOf((meta & 1) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;

        if (((Boolean) state.getValue(BlockCommandBlock.TRIGGERED_PROP)).booleanValue()) {
            var2 |= 1;
        }

        return var2;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockCommandBlock.TRIGGERED_PROP});
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(BlockCommandBlock.TRIGGERED_PROP, Boolean.valueOf(false));
    }
}
