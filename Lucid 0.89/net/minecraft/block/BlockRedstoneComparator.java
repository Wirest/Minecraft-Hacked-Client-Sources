package net.minecraft.block;

import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneComparator extends BlockRedstoneDiode implements ITileEntityProvider
{
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyEnum MODE = PropertyEnum.create("mode", BlockRedstoneComparator.Mode.class);

    public BlockRedstoneComparator(boolean powered)
    {
        super(powered);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, Boolean.valueOf(false)).withProperty(MODE, BlockRedstoneComparator.Mode.COMPARE));
        this.isBlockContainer = true;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.comparator;
    }

    @Override
	public Item getItem(World worldIn, BlockPos pos)
    {
        return Items.comparator;
    }

    @Override
	protected int getDelay(IBlockState state)
    {
        return 2;
    }

    @Override
	protected IBlockState getPoweredState(IBlockState unpoweredState)
    {
        Boolean var2 = (Boolean)unpoweredState.getValue(POWERED);
        BlockRedstoneComparator.Mode var3 = (BlockRedstoneComparator.Mode)unpoweredState.getValue(MODE);
        EnumFacing var4 = (EnumFacing)unpoweredState.getValue(FACING);
        return Blocks.powered_comparator.getDefaultState().withProperty(FACING, var4).withProperty(POWERED, var2).withProperty(MODE, var3);
    }

    @Override
	protected IBlockState getUnpoweredState(IBlockState poweredState)
    {
        Boolean var2 = (Boolean)poweredState.getValue(POWERED);
        BlockRedstoneComparator.Mode var3 = (BlockRedstoneComparator.Mode)poweredState.getValue(MODE);
        EnumFacing var4 = (EnumFacing)poweredState.getValue(FACING);
        return Blocks.unpowered_comparator.getDefaultState().withProperty(FACING, var4).withProperty(POWERED, var2).withProperty(MODE, var3);
    }

    @Override
	protected boolean isPowered(IBlockState state)
    {
        return this.isRepeaterPowered || ((Boolean)state.getValue(POWERED)).booleanValue();
    }

    @Override
	protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity var4 = worldIn.getTileEntity(pos);
        return var4 instanceof TileEntityComparator ? ((TileEntityComparator)var4).getOutputSignal() : 0;
    }

    private int calculateOutput(World worldIn, BlockPos pos, IBlockState state)
    {
        return state.getValue(MODE) == BlockRedstoneComparator.Mode.SUBTRACT ? Math.max(this.calculateInputStrength(worldIn, pos, state) - this.getPowerOnSides(worldIn, pos, state), 0) : this.calculateInputStrength(worldIn, pos, state);
    }

    @Override
	protected boolean shouldBePowered(World worldIn, BlockPos pos, IBlockState state)
    {
        int var4 = this.calculateInputStrength(worldIn, pos, state);

        if (var4 >= 15)
        {
            return true;
        }
        else if (var4 == 0)
        {
            return false;
        }
        else
        {
            int var5 = this.getPowerOnSides(worldIn, pos, state);
            return var5 == 0 ? true : var4 >= var5;
        }
    }

    @Override
	protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state)
    {
        int var4 = super.calculateInputStrength(worldIn, pos, state);
        EnumFacing var5 = (EnumFacing)state.getValue(FACING);
        BlockPos var6 = pos.offset(var5);
        Block var7 = worldIn.getBlockState(var6).getBlock();

        if (var7.hasComparatorInputOverride())
        {
            var4 = var7.getComparatorInputOverride(worldIn, var6);
        }
        else if (var4 < 15 && var7.isNormalCube())
        {
            var6 = var6.offset(var5);
            var7 = worldIn.getBlockState(var6).getBlock();

            if (var7.hasComparatorInputOverride())
            {
                var4 = var7.getComparatorInputOverride(worldIn, var6);
            }
            else if (var7.getMaterial() == Material.air)
            {
                EntityItemFrame var8 = this.findItemFrame(worldIn, var5, var6);

                if (var8 != null)
                {
                    var4 = var8.func_174866_q();
                }
            }
        }

        return var4;
    }

    private EntityItemFrame findItemFrame(World worldIn, final EnumFacing facing, BlockPos pos)
    {
        List var4 = worldIn.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1), new Predicate()
        {
            public boolean apply(Entity entityIn)
            {
                return entityIn != null && entityIn.getHorizontalFacing() == facing;
            }
            @Override
			public boolean apply(Object p_apply_1_)
            {
                return this.apply((Entity)p_apply_1_);
            }
        });
        return var4.size() == 1 ? (EntityItemFrame)var4.get(0) : null;
    }

    @Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!playerIn.capabilities.allowEdit)
        {
            return false;
        }
        else
        {
            state = state.cycleProperty(MODE);
            worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "random.click", 0.3F, state.getValue(MODE) == BlockRedstoneComparator.Mode.SUBTRACT ? 0.55F : 0.5F);
            worldIn.setBlockState(pos, state, 2);
            this.onStateChange(worldIn, pos, state);
            return true;
        }
    }

    @Override
	protected void updateState(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isBlockTickPending(pos, this))
        {
            int var4 = this.calculateOutput(worldIn, pos, state);
            TileEntity var5 = worldIn.getTileEntity(pos);
            int var6 = var5 instanceof TileEntityComparator ? ((TileEntityComparator)var5).getOutputSignal() : 0;

            if (var4 != var6 || this.isPowered(state) != this.shouldBePowered(worldIn, pos, state))
            {
                if (this.isFacingTowardsRepeater(worldIn, pos, state))
                {
                    worldIn.updateBlockTick(pos, this, 2, -1);
                }
                else
                {
                    worldIn.updateBlockTick(pos, this, 2, 0);
                }
            }
        }
    }

    private void onStateChange(World worldIn, BlockPos pos, IBlockState state)
    {
        int var4 = this.calculateOutput(worldIn, pos, state);
        TileEntity var5 = worldIn.getTileEntity(pos);
        int var6 = 0;

        if (var5 instanceof TileEntityComparator)
        {
            TileEntityComparator var7 = (TileEntityComparator)var5;
            var6 = var7.getOutputSignal();
            var7.setOutputSignal(var4);
        }

        if (var6 != var4 || state.getValue(MODE) == BlockRedstoneComparator.Mode.COMPARE)
        {
            boolean var9 = this.shouldBePowered(worldIn, pos, state);
            boolean var8 = this.isPowered(state);

            if (var8 && !var9)
            {
                worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(false)), 2);
            }
            else if (!var8 && var9)
            {
                worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(true)), 2);
            }

            this.notifyNeighbors(worldIn, pos, state);
        }
    }

    @Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (this.isRepeaterPowered)
        {
            worldIn.setBlockState(pos, this.getUnpoweredState(state).withProperty(POWERED, Boolean.valueOf(true)), 4);
        }

        this.onStateChange(worldIn, pos, state);
    }

    @Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);
        worldIn.setTileEntity(pos, this.createNewTileEntity(worldIn, 0));
    }

    @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
        this.notifyNeighbors(worldIn, pos, state);
    }

    /**
     * Called on both Client and Server when World#addBlockEvent is called
     */
    @Override
	public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam)
    {
        super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
        TileEntity var6 = worldIn.getTileEntity(pos);
        return var6 == null ? false : var6.receiveClientEvent(eventID, eventParam);
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityComparator();
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(POWERED, Boolean.valueOf((meta & 8) > 0)).withProperty(MODE, (meta & 4) > 0 ? BlockRedstoneComparator.Mode.SUBTRACT : BlockRedstoneComparator.Mode.COMPARE);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
	public int getMetaFromState(IBlockState state)
    {
        byte var2 = 0;
        int var3 = var2 | ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();

        if (((Boolean)state.getValue(POWERED)).booleanValue())
        {
            var3 |= 8;
        }

        if (state.getValue(MODE) == BlockRedstoneComparator.Mode.SUBTRACT)
        {
            var3 |= 4;
        }

        return var3;
    }

    @Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING, MODE, POWERED});
    }

    @Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(POWERED, Boolean.valueOf(false)).withProperty(MODE, BlockRedstoneComparator.Mode.COMPARE);
    }

    public static enum Mode implements IStringSerializable
    {
        COMPARE("COMPARE", 0, "compare"),
        SUBTRACT("SUBTRACT", 1, "subtract");
        private final String name; 

        private Mode(String p_i45731_1_, int p_i45731_2_, String name)
        {
            this.name = name;
        }

        @Override
		public String toString()
        {
            return this.name;
        }

        @Override
		public String getName()
        {
            return this.name;
        }
    }
}
